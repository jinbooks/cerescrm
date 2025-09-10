<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="90px">
          <el-form-item :label="t('commonName')">
            <el-input v-model="queryParams.name"/>
          </el-form-item>
          <el-form-item :label="t('commonNo')">
            <el-input v-model="queryParams.opportunityCode"/>
          </el-form-item>
          <el-form-item :label="t('org.status')">
            <el-select v-model="queryParams.status" clearable style="width: 240px">
              <el-option
                  v-for="dict in opportunity_status"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item :label="t('priority')">
            <el-select v-model="queryParams.priority" clearable style="width: 240px">
              <el-option
                  v-for="dict in priority"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item :label="t('jbx.organizations.typeCompany')">
            <el-input v-model="queryParams.customerName"/>
          </el-form-item>
          <el-form-item :label="t('ContactName')">
            <el-input v-model="queryParams.contactName"/>
          </el-form-item>
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
      <div class="kanban-board">
        <div class="stage-column" v-for="stage in stageList" :key="stage.id">
          <div
              class="stage-header"
              :style="{background: `linear-gradient(90deg, ${stage.color}, ${adjustColor(stage.color)})`}"
          >
            {{ stage.name }}
          </div>
          <draggable
              :list="opportunityMap[stage.id]"
              itemKey="id"
              group="opportunities"
              :data-stage-id="stage.id"
              @end="onCardDrop"
              class="card-list"
          >
            <template #item="{element}">
              <div class="card" @mouseleave="hideActions(element.id)">
                <div class="card-header">
                  <el-checkbox
                      :model-value="isChecked(element.id)"
                      @change="() => toggleCheck(element)"
                  ></el-checkbox>
                  <span class="title"><strong>{{ element.opportunityCode }}</strong></span>
                </div>
                <div class="card-content">
                  <div class="name">
                    <strong>{{element.name }}</strong>
                  </div>
                  <div class="contact">
                    {{ element.peopleName }} - {{element.company}}
                  </div>
                  <div class="amount">{{ formatAmount(element.amount)}}</div>
                  <div class="date">{{element.createdDate}}</div>
                </div>

                <!-- 右下角操作按钮区域 -->
                <div class="card-actions">
                  <!-- 更多按钮 -->
                  <el-button
                      class="more-btn"
                      :class="{ 'active': showActionsMap[element.id] }"
                      circle
                      size="small"
                      @click="toggleActions(element.id)"
                      @mouseenter="showActions(element.id)"
                  >
                    <el-icon><MoreFilled /></el-icon>
                  </el-button>

                  <!-- 操作按钮组 -->
                  <div
                      class="actions-menu"
                      :class="{ 'show': showActionsMap[element.id] }"
                  >
                    <el-button
                        class="action-btn view-btn"
                        circle
                        size="small"
                        @click="handleShow(element)"
                        :title="t('buttonShow')"
                    >
                      <el-icon><View /></el-icon>
                    </el-button>
                    <el-button
                        class="action-btn edit-btn"
                        circle
                        size="small"
                        @click="handleUpdate(element)"
                        :title="t('org.titleEdit')"
                    >
                      <el-icon><Edit /></el-icon>
                    </el-button>
                    <el-button
                        class="action-btn delete-btn"
                        circle
                        size="small"
                        type="danger"
                        @click="handleDelete(element)"
                        :title="t('org.button.delete')"
                    >
                      <el-icon><Delete /></el-icon>
                    </el-button>
                    <el-button
                        class="action-btn follow-btn"
                        circle
                        size="small"
                        type="primary"
                        @click="followUp(element)"
                        :title="t('followUp')"
                    >
                      <el-icon><DocumentChecked /></el-icon>
                    </el-button>
                  </div>
                </div>
              </div>
            </template>
          </draggable>
        </div>
      </div>
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
               :opportunity-types="opportunity_type"
               :opportunity-source-types="opportunity_source_type"
               :opportunity-status="opportunity_status"
               :priorities="priority"
               :books-industries="books_industry"
               @dialogOfClosedMethods="dialogOfClosedMethods"
    ></edit-form>
    <follow-up-form :relation-id="id" :open="followUpOpen" :title="title"
                    :follow-up-type="3" :follow-user-id="ownerId"
                    @dialogOfClosedMethods="dialogOfClosedMethods">
    </follow-up-form>
  </div>
</template>

<script setup lang="ts">
import {getCurrentInstance, reactive, ref, toRefs} from "vue";
import {useRouter} from "vue-router";
import {useI18n} from "vue-i18n";
import {fetchPage, delLead, updateOpportunityStage} from "@/api/opportunity/opportunity";
import modal from "@/plugins/modal";
import editForm from "./edit.vue"
import {ElForm} from "element-plus";
import {getAll} from "@/api/opportunity/oppStage";
import draggable from 'vuedraggable'
import {formatAmount} from "../../utils";
import FollowUpForm from "@/views/follow-up/edit.vue";

const {t} = useI18n()
const {proxy} = getCurrentInstance()!;
const {opportunity_type, opportunity_source_type, opportunity_status, priority, books_industry}
    = proxy?.useDict("opportunity_type", "opportunity_source_type", "opportunity_status", "priority", "books_industry");


interface Stage {
  id: string;
  name: string;
  color: string;
}

// 商机数据结构：
interface Opportunity {
  id: string;
  amount: number;
  stageId: string;
}

const dataList = ref([]);
const stageList = ref<Stage[]>([]);
// 按阶段分组
const opportunityMap = reactive<Record<string, Opportunity[]>>({});
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
const followUpOpen = ref(false);
const loading = ref(true);
const id: any = ref(undefined);
const total = ref(0);
const title = ref("");
const ids = ref([]);
const selectionlist: any = ref<any>([]);
const selectedList = ref<any[]>([]);
const ownerId: any = ref(undefined);




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
    // 分类到对应阶段
    stageList.value.forEach(stage => {
      opportunityMap[stage.id] = dataList.value.filter((item: any) => item.stageId === stage.id);
    });
    loading.value = false;
  });
}

//获取阶段列表
function getStageAllList() {
  getAll().then((response: any) => {
    stageList.value = response.data;
    // 初始化 opportunityMap
    stageList.value.forEach(stage => {
      opportunityMap[stage.id] = [];
    });
  })
}

function handleAdd() {
  id.value = undefined;
  title.value = t('newOpp');
  open.value = true;
}

function handleDelete(row: any) {
  const _ids = row.id || ids.value;
  modal.confirm(t('deleteTipOpp') + _ids + t('deleteTipLead1')).then(function () {
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
  queryParams.value.opportunityCode = undefined;
  queryParams.value.name = undefined;
  queryParams.value.status = undefined;
  queryParams.value.priority = undefined;
  queryParams.value.customerName = undefined;
  queryParams.value.contactName = undefined;
  handleQuery();
}

function handleShow(row: any) {
  router.push({path: '/opportunity/detail', query: {id: row.id}});
}

function handleUpdate(row: any) {
  id.value = row.id;
  title.value = t('updateOpp');
  open.value = true;
}


/*关闭抽屉*/
function dialogOfClosedMethods(val: any): any {
  open.value = false;
  id.value = undefined;
  followUpOpen.value = false;
  ownerId.value = undefined;
  if (val) {
    getList();
  }
}

// 拖拽结束后更新商机阶段
function onCardDrop(evt: any) {
  const item = evt.item.__vue__ || evt.item._underlying_vm_;
  const toContainer = evt.to as HTMLElement;
  const newStageId = toContainer.dataset.stageId;
  if (newStageId && item) {
    item.stageId = newStageId;

    const data = {
      oppId: item.id,
      stageId: item.stageId,
    }

    updateOpportunityStage(data).then((res: any) => {
      if (res.code === 0 && res.data === "修改成功") {
        modal.msgSuccess(t('stageUpdateOk'));
      }
      if (res.code === 2) {
        modal.msgError(t('stageUpdateFail'));
      }
    });

    console.log(`${t('opportunity')} ${item.id} ${t('moveStage')} ${newStageId}`);
  }

}

function adjustColor(hex: string) {
  // 简单加深颜色（可换更精细算法）
  let r = parseInt(hex.slice(1, 3), 16) - 30;
  let g = parseInt(hex.slice(3, 5), 16) - 30;
  let b = parseInt(hex.slice(5, 7), 16) - 30;
  r = Math.max(0, r);
  g = Math.max(0, g);
  b = Math.max(0, b);
  return `rgb(${r},${g},${b})`;
}

function isChecked(id: string) {
  return selectedList.value.some(item => item.id === id);
}

function toggleCheck(item: any) {
  const index = selectedList.value.findIndex(i => i.id === item.id);
  if (index > -1) {
    selectedList.value.splice(index, 1);
  } else {
    selectedList.value.push(item);
  }
  handleSelectionChangeFromKanban();
}

function handleSelectionChangeFromKanban() {
  selectionlist.value = [...selectedList.value];
  ids.value = selectionlist.value.map(item => item.id);
}

// 控制每个卡片的操作按钮显示状态
const showActionsMap = reactive<Record<string | number, boolean>>({})
// 显示操作按钮
const showActions = (id: string | number): void => {
  showActionsMap[id] = true
}
// 隐藏操作按钮
const hideActions = (id: string | number): void => {
  // 添加延迟，避免鼠标移动过快时按钮闪烁
  setTimeout(() => {
    showActionsMap[id] = false
  }, 200)
}
// 切换操作按钮显示状态
const toggleActions = (id: string | number): void => {
  showActionsMap[id] = !showActionsMap[id]
}

function followUp(row: any) {
  id.value = row.id;
  ownerId.value = row.ownerId;
  title.value = t('oppFU');
  followUpOpen.value = true;
}

getStageAllList();
getList();
</script>

<style scoped lang="scss">

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

.kanban-board {
  display: flex;
  gap: 20px;
  overflow-x: auto;
  padding: 10px;
}

.stage-column {
  flex: 0 0 250px;
  background: #f5f6fa;
  border-radius: 8px;
  padding: 10px;
}

.stage-header {
  background: linear-gradient(90deg, #5b60ff, #8a4fff);
  color: white;
  padding: 10px;
  border-radius: 6px;
  margin-bottom: 10px;
  text-align: center;
}

.card {
  position: relative;
  background: #fff;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  padding: 6px;
  padding-bottom: 50px; /* 为右下角按钮留出空间 */
  margin-bottom: 12px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  min-height: 120px; /* 确保卡片有足够高度 */
}

.card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  border-color: #409eff;
}

.card-header {
  display: flex;
  align-items: center;
  margin-bottom: 4px;
}

.card-header .el-checkbox {
  margin-right: 8px;
}

.title {
  flex: 1;
  font-size: 14px;
  color: #303133;
  line-height: 1.4;
}

.card-content {
  padding-left: 24px; /* 对齐复选框后的内容 */
}

.name {
  font-size: 15px;
  margin-bottom: 6px;
}

.contact {
  color: #606266;
  font-size: 13px;
  margin-bottom: 6px;
}

.amount {
  color: #409eff;
  font-weight: 600;
  font-size: 14px;
  margin-bottom: 6px;
}

.date {
  color: #909399;
  font-size: 12px;
}

/* 右下角操作按钮样式 */
.card-actions {
  position: absolute;
  bottom: 8px;
  left: 8px;  /* 改为 left */
  display: flex;
  align-items: center;
  gap: 8px;
  z-index: 20;
}

.more-btn {
  background: #f5f7fa;
  border-color: #dcdfe6;
  color: #606266;
  transition: all 0.3s ease;
  z-index: 21;
  position: relative;
}

.more-btn:hover,
.more-btn.active {
  background: #409eff;
  border-color: #409eff;
  color: #fff;
  transform: rotate(90deg);
}

.actions-menu {
  display: flex;
  align-items: center;
  gap: 6px;
  opacity: 0;
  visibility: hidden;
  transform: translateX(10px);
  transition: all 0.3s ease;
}

.actions-menu.show {
  opacity: 1;
  visibility: visible;
  transform: translateX(0);
}

.action-btn {
  width: 28px;
  height: 28px;
  transition: all 0.2s ease;
}

.view-btn {
  background: #e1f3d8;
  border-color: #b3d999;
  color: #67c23a;
}

.view-btn:hover {
  background: #67c23a;
  border-color: #67c23a;
  color: #fff;
}

.edit-btn {
  background: #fdf6ec;
  border-color: #f5dab1;
  color: #e6a23c;
}

.edit-btn:hover {
  background: #e6a23c;
  border-color: #e6a23c;
  color: #fff;
}

.delete-btn {
  background: #fef0f0;
  border-color: #fbc4c4;
  color: #f56c6c;
}

.delete-btn:hover {
  background: #f56c6c;
  border-color: #f56c6c;
  color: #fff;
}

.follow-btn {
  background: aliceblue;
  border-color: skyblue;
  color: #409eff;
}

.follow-btn:hover {
  background: #409eff;
  border-color: skyblue;
  color: #fff;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .card {
    padding: 12px;
  }

  .card-actions {
    bottom: 8px;
    right: 8px;
  }

  .more-btn,
  .action-btn {
    width: 24px;
    height: 24px;
  }
}

/* 为卡片列表添加一些基础样式 */
.card-list {
  min-height: 100px;
  padding: 2px;
}
</style>
