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
        <el-button type="primary" @click="handleAdd">{{t('jbx.text.add')}}</el-button>
        <el-button @click="onBatchDelete" :disabled="ids.length === 0" type="danger">{{t('org.button.deleteBatch')}}</el-button>
      </div>
      <el-table v-loading="loading" :data="dataList"
                border
                @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" align="center"/>
        <el-table-column prop="code" :label="t('stageCode')" align="center" min-width="100"
                         :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column prop="name" :label="t('stageName')" align="center" min-width="100"
                         :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column prop="colorCode" :label="t('colorFlag')" align="center">
          <template #default="scope">
            <span :style="{ color: scope.row.color, fontWeight: 'bold' }">●</span> {{ scope.row.color }}
          </template>
        </el-table-column>
        <el-table-column :label="t('org.operate')" align="center" width="120">
          <template #default="scope">
            <el-button icon="Edit" link @click="handleUpdate(scope.row)"></el-button>
            <el-button icon="Delete" link type="danger"  @click="handleDelete(scope.row)"></el-button>
          </template>
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
    <edit-form :title="title" :open="open"
               :form-id="id"
               @dialogOfClosedMethods="dialogOfClosedMethods"
    ></edit-form>
  </div>
</template>

<script setup lang="ts">

import {getCurrentInstance, reactive, ref, toRefs} from "vue";
import {useRouter} from "vue-router";
import {useI18n} from "vue-i18n";
import {fetchPage, delLead} from "@/api/opportunity/oppStage";
import modal from "@/plugins/modal";
import editForm from "./edit.vue"
import {ElForm} from "element-plus";
const {t} = useI18n()
const {proxy} = getCurrentInstance()!;

const dataList = ref([]);
const router = useRouter()
const data: any = reactive({
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    pageSizeOptions: [10, 20, 50]
  },
  form: {},
  rules: {},
});
const {queryParams, form, rules} = toRefs(data);
const open = ref(false);
const loading = ref(true);
const id: any = ref(undefined);
const total = ref(0);
const title = ref("");
const ids = ref([]);
const selectionlist: any = ref<any>([]);


function handleSelectionChange(selection: any) {
  selectionlist.value = selection;
  ids.value = selectionlist.value.map((item: any) => item.id);
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

function handleAdd() {
  id.value = undefined;
  title.value = t('addStage');
  open.value = true;
}

function handleDelete(row: any) {
  const _ids = row.id || ids.value;
  modal.confirm(t('deleteStageTip') + _ids + t('deleteTip2')).then(function () {
    return delLead({listIds: [_ids]});
  }).then((res: any) => {
    if (res.code === 0) {
      getList();
      modal.msgSuccess(t('jbx.alert.delete.success'));
    } else {
      modal.msgError(res.message);
    }
  }).catch(() => {
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

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  handleQuery();
}

function handleUpdate(row: any) {
  id.value = row.id;
  title.value = t('updateStage');
  open.value = true;
}


/*关闭抽屉*/
function dialogOfClosedMethods(val: any): any {
  open.value = false;
  id.value = undefined;
  if (val) {
    getList();
  }
}

getList();
</script>
