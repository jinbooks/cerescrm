<template>
  <div class="table-select-input">
    <el-input :model-value="inputValue" readonly>
      <template #append>
        <el-button @click="handleOpenDialog" :disabled="isConvert">{{t('jbx.text.select')}}</el-button>
      </template>
    </el-input>

    <el-dialog
        v-model="visible"
        title="选择线索"
        width="1200px"
        @close="handleClose">
      <div class="app-container">
        <el-card class="common-card">
          <div class="queryForm">
            <el-form :model="queryParams" :inline="true" label-width="68px" >
              <el-form-item :label="t('jbx.users.displayName')">
                <el-input v-model="queryParams.name"/>
              </el-form-item>
              <el-form-item :label="t('jbx.organizations.typeCompany')">
                <el-input v-model="queryParams.company"/>
              </el-form-item>
              <el-form-item :label="t('org.status')">
                <el-select v-model="queryParams.status" clearable style="width: 240px">
                  <el-option
                      v-for="dict in status_manage"
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
        <el-card class="common-card">
          <el-table v-loading="loading" :data="dataList" border @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" v-if="multiSelect"></el-table-column>
            <!-- 单选 -->
            <el-table-column width="45" align="center" :show-overflow-tooltip="true" v-else>
              <template #default="{row}">
                <el-radio v-model="tempSingleSelectId" :value="row.id"></el-radio>
              </template>
            </el-table-column>
            <el-table-column prop="leadCode" :label="t('clueCoding')" align="center" width="140"
                             :show-overflow-tooltip="true">
            </el-table-column>
            <el-table-column prop="name" :label="t('jbx.users.displayName')" align="left" header-align="center" min-width="80"
                             :show-overflow-tooltip="true">
            </el-table-column>
            <el-table-column prop="phone" :label="t('jbx.institutions.phone')" align="left" header-align="center"  width="120"
                             :show-overflow-tooltip="true">
            </el-table-column>
            <el-table-column prop="email" :label="t('jbx.institutions.email')" align="left" header-align="center"  min-width="80"
                             :show-overflow-tooltip="true">
            </el-table-column>
            <el-table-column prop="province" :label="t('area')" align="left" header-align="center"  width="80"
                             :show-overflow-tooltip="true">
            </el-table-column>
            <el-table-column prop="company" :label="t('jbx.organizations.typeCompany')" align="left" header-align="center"  min-width="100"
                             :show-overflow-tooltip="true">
            </el-table-column>
            <el-table-column prop="ownerName" :label="t('PersonCharge')"  align="center"  width="120"
                             :show-overflow-tooltip="true">
            </el-table-column>
            <el-table-column prop="status" :label="t('org.status')" align="center" width="80"
                             :show-overflow-tooltip="true">
              <template #default="scope"  >
                <dict-tag-number :options="status_manage" :value="scope.row.status"/>
              </template>
            </el-table-column>
            <el-table-column prop="priority" :label="t('priority')" align="center" width="80"
                             :show-overflow-tooltip="true">
              <template #default="scope">
                <span v-if="scope.row.priority === 1"><el-tag type="info">{{t('low')}}</el-tag></span>
                <span v-if="scope.row.priority === 2"><el-tag type="warning">{{t('middle')}}</el-tag></span>
                <span v-if="scope.row.priority === 3"><el-tag type="danger">{{t('high')}}</el-tag></span>
              </template>
            </el-table-column>
            <el-table-column prop="budget" :label="t('budget')" align="right" header-align="center" width="120"
                             :show-overflow-tooltip="true">
              <template #default="scope">
                {{ formatAmount(scope.row.budget) }}
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
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleClose">{{ t('org.cancel') }}</el-button>
          <el-button type="primary" @click="handleSubmit">{{ t('org.confirm') }}</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import {computed, getCurrentInstance, reactive, ref, toRefs, watch} from 'vue'
import {useI18n} from 'vue-i18n'
import {fetchPage} from "@/api/lead/lead";
import {formatAmount} from "@/utils";

const {t} = useI18n()
const {proxy} = getCurrentInstance()!;
const {status_manage}
    = proxy?.useDict("status_manage");

const props = defineProps({
  modelValue: [String, Array],
  multiSelect: {
    type: Boolean,
    default: false
  },
  isConvert: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:modelValue', 'select'])

const data: any = reactive({
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    pageSizeOptions: [10, 20, 50]
  }
})


const dataList = ref<any>([])
const loading = ref(true)
const total = ref(0)
const visible = ref(false)
const singleSelectId = ref<string | undefined>()
const selectedIds = ref<string[]>([])
const {queryParams} = toRefs(data)
const tempSelectedIds = ref<string[]>([])
const tempSingleSelectId = ref<string | undefined>()

// 监听 modelValue 的变化并更新 inputValue
watch(() => props.modelValue, (newValue) => {
  if (newValue !== inputValue.value) {
    if (props.multiSelect) {
      selectedIds.value = newValue as string[]
    } else {
      singleSelectId.value = newValue as string
    }
  }
})

// 输入框的值，与 modelValue 同步
const inputValue = computed(() => {
  const ids = props.multiSelect ? selectedIds.value : [singleSelectId.value]
  return dataList.value.filter((item: any) => {
    return ids.includes(item.id)
  }).map((item: any) => item.leadCode).join(',')
})

function handleQuery() {
  queryParams.value.pageNumber = 1
  getList()
}

function resetQuery() {
  queryParams.value.name = undefined
  queryParams.value.status = undefined
  queryParams.value.company = undefined
  handleQuery()
}

function getList() {
  loading.value = true
  fetchPage(queryParams.value).then((response: any) => {
    const res = response.data;
    dataList.value = res.leadPage.records;
    total.value = res.leadPage.total;
    loading.value = false;
  });
}

function handleClose() {
  visible.value = false
}

function handleSubmit() {
  // 把临时值同步到真正的选中值中
  if (props.multiSelect) {
    selectedIds.value = [...tempSelectedIds.value]
  } else {
    singleSelectId.value = tempSingleSelectId.value
  }

  const selectedValue: string | string[] = props.multiSelect
      ? selectedIds.value
      : singleSelectId.value || ''

  emit('update:modelValue', selectedValue)
  emit('select', selectedValue, inputValue.value)
  handleClose()
}

function handleSelectionChange(selection: any) {
  tempSelectedIds.value = selection.map((item: any) => item.id)
}

// 处理打开对话框
function handleOpenDialog() {
  visible.value = true
  // 复制当前已选项到临时变量
  tempSelectedIds.value = [...selectedIds.value]
  tempSingleSelectId.value = singleSelectId.value
  getList()
}

getList()
</script>

<style scoped>
.table-select-input {
  width: 100%;
}

.dialog-footer {
  text-align: right;
}

:deep(.el-dialog) {
  border-top-left-radius: 0 !important;
  border-top-right-radius: 0 !important;
  /* 可选 - 去掉 margin 顶部空隙 */
  margin-top: 0 !important;
  top: 0 !important;
  transform: none !important;
}

.common-card {
  margin-bottom: 10px;
}

.app-container {
  padding: 0;
  background-color: #f5f7fa;
}
</style>
