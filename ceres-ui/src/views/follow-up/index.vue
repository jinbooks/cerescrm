<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="68px">
          <el-form-item>
            <el-button @click="handleQuery">{{ t('org.button.query') }}</el-button>
            <el-button @click="resetQuery">{{ t('org.button.reset') }}</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <el-card class="common-card">
      <div class="btn-form">
        <el-button @click="onBatchDelete" :disabled="ids.length === 0" type="danger">{{t('org.button.deleteBatch')}}</el-button>
      </div>
      <el-table v-loading="loading" :data="dataList"
                border
                @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" align="center"/>
        <el-table-column prop="category" :label="t('BusinessCategory')" align="center" min-width="50"
                         :show-overflow-tooltip="true">
          <template #default="scope">
            <dict-tag-number :options="categories" :value="scope.row.category"/>
          </template>
        </el-table-column>
        <el-table-column prop="relationName" :label="t('BNName')" align="center" min-width="100"
                         :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column prop="followType" :label="t('FollowUpWay')" align="center" min-width="50"
                         :show-overflow-tooltip="true">
          <template #default="scope">
            <dict-tag-number :options="follow_types" :value="scope.row.followType"/>
          </template>
        </el-table-column>
        <el-table-column prop="title" :label="t('FollowUpTitle')" align="center" min-width="70"
                         :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column prop="content" :label="t('FollowUpContent')" align="center" min-width="150"
                         :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column prop="result" :label="t('FollowUpResult')" align="center" min-width="50"
                         :show-overflow-tooltip="true">
          <template #default="scope">
            <dict-tag-number :options="results" :value="scope.row.result"/>
          </template>
        </el-table-column>
        <el-table-column prop="followUserName" :label="t('FollowUpPeople')" align="center" min-width="70"
                         :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column prop="followAt" :label="t('FollowUpTime')" align="center" min-width="100"
                         :show-overflow-tooltip="true">
        </el-table-column>
      </el-table>
      <pagination
          v-show="total > 0"
          :total="total"
          v-model:page="queryParams.pageNumber"
          v-model:limit="queryParams.pageSize"
          :page-sizes="queryParams.pageSizeOptions"
          @pagination="getList"
      />
    </el-card>
  </div>
</template>


<script setup lang="ts">
import {getCurrentInstance, reactive, ref, toRefs} from "vue";
import {ElForm} from "element-plus";
import {delLead, fetchPage} from "@/api/followup/follopup";
import modal from "@/plugins/modal";
import {useI18n} from "vue-i18n";
import {formatAmount} from "@/utils";

const {proxy} = getCurrentInstance()!;
const {categories, follow_types, results}
    = proxy?.useDict("categories", "follow_types", "results");
const {t} = useI18n()
const data: any = reactive({
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    pageSizeOptions: [10, 20, 50]
  }
});
const dataList = ref([]);
const open = ref(false);
const loading = ref(true);
const id: any = ref(undefined);
const leadId: any = ref(undefined);
const total = ref(0);
const title = ref("");
const ids = ref([]);
const selectionlist: any = ref<any>([]);
const formRef = ref<InstanceType<typeof ElForm> | null>(null);
const {queryParams} = toRefs(data);

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  // queryParams.value.name = undefined;
  // queryParams.value.status = undefined;
  handleQuery();
}

function getList() {
  loading.value = true;
  fetchPage(queryParams.value).then((response: any) => {
    const res = response.data;
    dataList.value = res.records;
    total.value = res.total;
    loading.value = false;
  });
}

/** 多选删除操作*/
function onBatchDelete(): any {
  modal.confirm(t('jbx.confirm.text.delete')).then(function () {
    return delLead({listIds: ids.value});
  }).then((res: any) => {
    if (res.code === 0) {
      handleQuery();
      modal.msgSuccess(t('jbx.alert.delete.success'));
    } else {
      modal.msgError(res.message);
    }
  }).catch(() => {
  });
}

function handleSelectionChange(selection: any) {
  selectionlist.value = selection;
  ids.value = selectionlist.value.map((item: any) => item.id);
}

getList();
</script>

<style lang="scss" scoped>
.btn-form {
  margin-bottom: 10px;
}

.common-card {
  margin-bottom: 10px;
}

.app-container {
  padding: 0;
  background-color: #f5f7fa;
}
</style>
