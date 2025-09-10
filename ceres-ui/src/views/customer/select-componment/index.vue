<template>
  <div class="table-select-input">
    <el-input :model-value="inputValue" readonly>
      <template #append>
        <el-button @click="handleOpenDialog">选择</el-button>
      </template>
    </el-input>

    <el-dialog
        v-model="visible"
        title="选择客户"
        width="1200px"
        @close="handleClose">
      <div class="app-container">
        <el-card class="common-card">
          <el-form :inline="true" label-width="80px">
            <el-form-item label="客户名称">
              <el-input v-model="queryParams.customerName" placeholder="请输入客户名称"/>
            </el-form-item>
            <el-form-item label="客户分组">
              <el-select style="width: 240px" v-model="queryParams.segmentId" clearable placeholder="请选择">
                <el-option v-for="(item, index) in dataSegmentList" :key="index"
                           :value="item.id" :label="item.segmentName"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button @click="handleQuery">查询</el-button>
              <el-button @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
        <el-card class="common-card">
          <el-table v-loading="loading" :data="dataList" border @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" v-if="multiSelect"></el-table-column>
            <el-table-column width="55" align="center" :show-overflow-tooltip="true" v-else>
              <template #default="{row}">
                <el-radio v-model="singleSelectId" :value="row.id"></el-radio>
              </template>
            </el-table-column>
            <el-table-column prop="customerName" label="客户名称" align="center"></el-table-column>
            <el-table-column prop="customerType" label="客户类型" align="center"></el-table-column>
            <el-table-column prop="segmentName" label="所在分层" align="center">
              <template #default="scope">
                <text v-if="scope.row.segmentId" :style="{color: getSegmentColor(scope.row.segmentId)}">
                  {{ scope.row.segmentName }}
                </text>
              </template>
            </el-table-column>
            <el-table-column prop="industry" label="所属行业" align="center"></el-table-column>
            <el-table-column prop="phone" label="联系电话" align="center"></el-table-column>
            <el-table-column prop="email" label="邮箱" align="center"></el-table-column>
            <el-table-column prop="customerHead" :label="t('PersonCharge')" align="center"></el-table-column>
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
import {listCustomer,} from '@/api/customer/customer'
import {listCustomerSegment} from '@/api/customer/customer-segment'
import {useI18n} from 'vue-i18n'

const {t} = useI18n()

const props = defineProps({
  modelValue: [String, Array],
  multiSelect: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:modelValue', 'select'])

const data: any = reactive({
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    pageSizeOptions: [10, 20, 50],
    customerName: '',
    customerType: '',
    customerHead: '',
    segmentId: ''
  },
  form: {}
})

interface SegmentModel {
  id: string
  segmentName: string,
  colorCode: string
}

const dataList = ref<any>([])
const dataSegmentList = ref<Array<SegmentModel>>([])
const dataSegmentMap = ref<Map<String, SegmentModel>>(new Map())
const loading = ref(true)
const total = ref(0)
const visible = ref(false)
const singleSelectId = ref<string | undefined>()
const selectedIds = ref<string[]>([])
const {proxy} = getCurrentInstance()!;
const {queryParams} = toRefs(data)

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
  }).map((item: any) => item.customerName).join(',')
})

function handleQuery() {
  queryParams.value.pageNumber = 1
  getList()
}

function resetQuery() {
  queryParams.value.customerName = ''
  queryParams.value.segmentId = ''
  handleQuery()
}

function getList() {
  loading.value = true
  listCustomer(queryParams.value).then((response: any) => {
    dataList.value = response.data.records
    total.value = response.data.total
    loading.value = false
  })
}

function getSegmentList() {
  listCustomerSegment({
    pageNumber: 1,
    pageSize: 10000,
  }).then((response: any) => {
    dataSegmentList.value = response.data.records
    dataSegmentList.value.forEach((item: SegmentModel) => {
      dataSegmentMap.value.set(item.id, item)
    })
  })
}

function getSegmentColor(id: string) {
  return dataSegmentMap.value.get(id)?.colorCode
}

function handleClose() {
  visible.value = false
}

function handleSubmit() {
  let selectedValue: string | string[] = props.multiSelect ? selectedIds.value : singleSelectId.value || ''
  emit('update:modelValue', selectedValue)
  emit('select', selectedValue, inputValue.value)
  handleClose()
}

function handleSelectionChange(selection: any) {
  selectedIds.value = selection.map((item: any) => item.id)
}

// 处理打开对话框
function handleOpenDialog() {
  visible.value = true
  getList()
  getSegmentList()
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
</style>
