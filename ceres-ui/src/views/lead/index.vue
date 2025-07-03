<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="68px">
          <el-form-item label="姓名">
            <el-input v-model="queryParams.name"/>
          </el-form-item>
          <el-form-item label="电话">
            <el-input v-model="queryParams.phone"/>
          </el-form-item>
          <el-form-item label="公司">
            <el-input v-model="queryParams.company"/>
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="queryParams.status" clearable style="width: 120px">
              <el-option
                  v-for="dict in status_manage"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="优先级">
            <el-select v-model="queryParams.priority" clearable style="width: 120px">
              <el-option
                  v-for="dict in priority"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button @click="handleQuery">{{ t('org.button.query') }}</el-button>
            <el-button @click="resetQuery">{{ t('org.button.reset') }}</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <div class="total-card">
      <el-card class="top-card">
        <div class="items-flex">
          <div class="icon-items" style="background-color: #eef4ff; color: #409EFF;">
            <el-icon>
              <User/>
            </el-icon>
          </div>
          <div class="items-info">
            <p class="items-title">总线索数</p>
            <p class="items-data">{{ statistics.totalLead }}</p>
          </div>
        </div>
      </el-card>
      <el-card class="top-card">
        <div class="items-flex">
          <div class="icon-items" style="  background-color: #e6f9f0;color: #00b96b;">
            <el-icon>
              <TrendCharts/>
            </el-icon>
          </div>
          <div class="items-info">
            <p class="items-title">有意向</p>
            <p class="items-data">{{ statistics.intentionCount }}</p>
          </div>
        </div>
      </el-card>
      <el-card class="top-card">
        <div class="items-flex">
          <div class="icon-items" style="  background-color: #fff8e6;color: #faad14;">
            <el-icon>
              <Clock/>
            </el-icon>
          </div>
          <div class="items-info">
            <p class="items-title">待跟进</p>
            <p class="items-data">{{ statistics.pendingFollowUpCount }}</p>
          </div>
        </div>
      </el-card>
      <el-card class="top-card">
        <div class="items-flex">
          <div class="icon-items" style=" background-color: #f6f0ff;color: #722ed1;">
            <el-icon>
              <School/>
            </el-icon>
          </div>
          <div class="items-info">
            <p class="items-title">转化率</p>
            <p class="items-data">{{ statistics.conversionRate }}%</p>
          </div>
        </div>
      </el-card>
    </div>
    <el-card class="common-card">
      <div class="btn-form">
        <el-button type="primary" @click="handleAdd">新增</el-button>
        <el-button @click="onBatchDelete" :disabled="ids.length === 0" type="danger">批量删除</el-button>
      </div>
      <el-table v-loading="loading" :data="dataList"
                border
                @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" align="center"/>
        <el-table-column label="跟进" align="center" width="60"
                         :show-overflow-tooltip="true">
          <template #default="scope">
            <el-button icon="Comment" type="warning" link @click="followUp(scope.row)"></el-button>
          </template>
        </el-table-column>
        <el-table-column label="转商机" align="center" width="70"
                         :show-overflow-tooltip="true">
          <template #default="scope">
            <el-button icon="Connection" link type="success" @click="convertOpp(scope.row)" :disabled="scope.row.status === 4"></el-button>
          </template>
        </el-table-column>
        
        <el-table-column prop="leadCode" label="线索编码" align="center" width="140"
                         :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column prop="name" label="姓名" align="left" header-align="center" min-width="80"
                         :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column prop="phone" label="电话" align="left" header-align="center"  width="120"
                         :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column prop="email" label="邮箱" align="left" header-align="center"  min-width="80"
                         :show-overflow-tooltip="true">
        </el-table-column>
         <el-table-column prop="province" label="地区" align="left" header-align="center"  width="80"
                         :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column prop="company" label="公司" align="left" header-align="center"  min-width="100"
                         :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column prop="ownerName" label="负责人"  align="center"  width="120"
                         :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column prop="status" label="状态" align="center" width="80"
                         :show-overflow-tooltip="true">
          <template #default="scope"  >
            <dict-tag-number :options="status_manage" :value="scope.row.status"/>
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级" align="center" width="80"
                         :show-overflow-tooltip="true">
          <template #default="scope">
           <span v-if="scope.row.priority === 1"><el-tag type="info">低</el-tag></span>
           <span v-if="scope.row.priority === 2"><el-tag type="warning">中</el-tag></span>
           <span v-if="scope.row.priority === 3"><el-tag type="danger">高</el-tag></span>
          </template>
        </el-table-column>
        <el-table-column prop="budget" label="预算" align="right" header-align="center" width="120"
                         :show-overflow-tooltip="true">
          <template #default="scope">
            {{ formatAmount(scope.row.budget) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="110">
          <template #default="scope">
            <el-button icon="View"  link @click="handleShow(scope.row)"></el-button>
            <el-button icon="Edit" link @click="handleUpdate(scope.row)"></el-button>
            <el-button icon="Delete" type="danger" link @click="handleDelete(scope.row)"></el-button>
<!--            <el-dropdown style="margin-left: 8px">
              <el-button link icon="MoreFilled"></el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click.native="convertOpp(scope.row)" v-if="scope.row.status !== 4">
                    <span>转化商机</span>
                  </el-dropdown-item>
                  <el-dropdown-item divided @click.native="handleDelete(scope.row)">
                    <span style="color: red">{{ $t('jbx.text.delete') }}</span>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>-->
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
               :source-types="source_type"
               :books-industries="books_industry"
               :status-manages="status_manage"
               :priorities="priority"
               @dialogOfClosedMethods="dialogOfClosedMethods"
    ></edit-form>
    <edit-opp :title="title" :open="openOpp"
               :lead-id="leadId"
               :opportunity-types="opportunity_type"
               :opportunity-source-types="opportunity_source_type"
               :opportunity-status="opportunity_status"
               :priorities="priority"
               :books-industries="books_industry"
               @dialogOfClosedMethods="dialogOfClosedMethods"
    ></edit-opp>
    <follow-up-form :relation-id="id" :open="followUpOpen" :title="title"
                    :follow-up-type="1" :follow-user-id="ownerId"
                    @dialogOfClosedMethods="dialogOfClosedMethods">
    </follow-up-form>
  </div>
</template>
<script setup lang="ts">
import {reactive, ref, toRefs, getCurrentInstance} from "vue";
import {useI18n} from "vue-i18n";
import {delLead, fetchPage} from "@/api/lead/lead";
import {ElForm} from "element-plus";
import modal from "@/plugins/modal";
import editForm from "./edit.vue"
import editOpp from "@/views/opportunity/edit.vue"
import {useRouter} from "vue-router";
import {formatAmount} from "../../utils";
import FollowUpForm from "@/views/follow-up/edit.vue"

const {t} = useI18n()
const {proxy} = getCurrentInstance()!;
const {source_type, books_industry, status_manage, priority}
    = proxy?.useDict("source_type", "books_industry", "status_manage", "priority");
const {opportunity_type, opportunity_source_type, opportunity_status}
    = proxy?.useDict("opportunity_type", "opportunity_source_type", "opportunity_status");

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
const dataList = ref([]);
const statistics = ref({
  totalLead: 0 as number, // 对应 Long
  intentionCount: 0 as number, // 对应 Long
  pendingFollowUpCount: 0 as number, // 对应 Long
  conversionRate: 0 as number // 对应 BigDecimal，通常用 number 表示
})
const open = ref(false);
const openOpp = ref(false);
const followUpOpen = ref(false);
const loading = ref(true);
const id: any = ref(undefined);
const ownerId: any = ref(undefined);
const leadId: any = ref(undefined);
const total = ref(0);
const title = ref("");
const ids = ref([]);
const selectionlist: any = ref<any>([]);
const formRef = ref<InstanceType<typeof ElForm> | null>(null);
const {queryParams, form, rules} = toRefs(data);
const editTitle: any = ref('');

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  queryParams.value.name = undefined;
  queryParams.value.status = undefined;
  queryParams.value.company = undefined;
  handleQuery();
}

function getList() {
  loading.value = true;
  fetchPage(queryParams.value).then((response: any) => {
    const res = response.data;
    dataList.value = res.leadPage.records;
    total.value = res.leadPage.total;

    // 接收统计数据
    statistics.value = {
      totalLead: res.totalLead ?? 0,
      intentionCount: res.intentionCount ?? 0,
      pendingFollowUpCount: res.pendingFollowUpCount ?? 0,
      conversionRate: res.conversionRate ?? 0
    };

    loading.value = false;
  });
}

/*关闭抽屉*/
function dialogOfClosedMethods(val: any): any {
  open.value = false;
  openOpp.value = false;
  followUpOpen.value = false;
  id.value = undefined;
  ownerId.value = undefined;
  leadId.value = undefined;
  if (val) {
    getList();
  }
}

function handleUpdate(row: any) {
  id.value = row.id;
  title.value = "修改线索";
  open.value = true;
}

function handleShow(row: any) {
  router.push({path: '/lead/detail', query: {id: row.id}});
}

function handleDelete(row: any) {
  const _ids = row.id || ids.value;
  modal.confirm('是否确认删除线索编号为"' + _ids + '"的数据项？').then(function () {
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

function handleAdd() {
  id.value = undefined;
  title.value = "新增线索";
  open.value = true;
}

function handleSelectionChange(selection: any) {
  selectionlist.value = selection;
  ids.value = selectionlist.value.map((item: any) => item.id);
}

function resetForm() {
  form.value = {}
  formRef?.value?.resetFields();
}

/*
* 转成商机
*/
function convertOpp(row: any) {
  leadId.value = row.id;
  openOpp.value = true;
  title.value = "线索转化商机";
  id.value = undefined;
}

function followUp(row: any) {
  id.value = row.id;
  ownerId.value = row.ownerId;
  title.value = "线索跟进";
  followUpOpen.value = true;
}

getList()
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

.top-card {
  height: 8vh;
  border-radius: 8px;
  /* 去除 el-card 默认的 padding */
  --el-card-padding: 0;
}

.items-flex {
  display: flex;
  align-items: center; /* 垂直居中 */
  height: 100%; /* 填满 card 的高度 */
  padding: 0 16px; /* 手动控制左右内边距 */
  /* 方法1：外边距 */
  margin-top: -4px; /* 向上移动一点 */
}

.icon-items {
  border-radius: 8px;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  font-size: 20px;
}

.items-info {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.items-title {
  margin: 0;
  font-size: 14px;
  color: #666;
}

.items-data {
  margin: 0;
  font-size: 20px;
  font-weight: bold;
  color: #000;
}

.total-card {
  display: grid;
  grid-template-columns: repeat(4, 1fr); /* 每行两个 */
  gap: 10px; /* 卡片间距 */
  margin-bottom: 10px;
}
</style>
