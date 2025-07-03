<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="50%"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
      <el-card class="common-card query-box">
        <el-form :inline="true" label-width="80px">
          <el-form-item label="产品编码">
            <el-input v-model="queryParams.productCode" placeholder="请输入产品编码"/>
          </el-form-item>
          <el-form-item label="产品名称">
            <el-input v-model="queryParams.productName" placeholder="请输入产品名称"/>
          </el-form-item>
          <el-form-item>
            <el-button @click="handleQuery">查询</el-button>
            <el-button @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>

      <el-card class="common-card">
        <div class="btn-form">
          <el-button type="primary" @click="handleAdd">新增</el-button>
        </div>
        <el-table v-loading="loading" :data="dataList" border  show-summary :summary-method="getSummaries" >
          <el-table-column prop="productCode" label="产品编码" align="center"></el-table-column>
          <el-table-column prop="productName" label="产品名称" align="center"></el-table-column>
          <el-table-column prop="productVersion" label="版本" align="center"></el-table-column>
          <el-table-column prop="productUnit" label="单位" align="center"></el-table-column>
          <el-table-column prop="price" label="价格" align="center"></el-table-column>
          <el-table-column prop="quantity" label="购买数量" align="center"></el-table-column>
          <el-table-column prop="originalPrice" label="原价" align="center"></el-table-column>
          <el-table-column prop="discount" label="折扣率(%)" align="center"></el-table-column>
          <el-table-column prop="amount" label="价格" align="center"></el-table-column>
          <el-table-column label="操作" align="center" width="120">
          <template #default="scope">
            <el-button icon="Edit" link @click="handleUpdate(scope.row)"></el-button>
            <el-button icon="Delete" link type="danger" @click="handleDelete(scope.row)"></el-button>
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

      <contract-product-edit-form
        :title="title"
        :open="openProduct"
        :form-id="id"
        :contract-Id="contractId"
        @dialogFormOfClosedMethods="dialogFormOfClosedMethods"
    ></contract-product-edit-form>

    </template>
    <template #footer>
      <div style="flex: auto">
        <el-button @click="dialogOfClosedMethods(false)">{{ t('org.cancel') }}</el-button>
      </div>
    </template>
  </el-drawer>
</template>

<script setup lang="ts">
import {reactive, ref, toRefs, watch} from 'vue'
import {useI18n} from 'vue-i18n'
import * as contractProductService  from '@/api/contract/contract-product'
import modal from '@/plugins/modal'
import {h} from 'vue'
import type {VNode} from 'vue'
import contractProductEditForm from './contract-product-edit.vue'

const emit = defineEmits(['dialogOfClosedMethods'])
const {t} = useI18n()

const data: any = reactive({
  queryParams: {
    contractId:"",
    pageNumber: 1,
    pageSize: 10,
    pageSizeOptions: [10, 20, 50],
    contactName: '',
    gender: '',
    phone: ''
  },
  form: {}
})
const dialogStatus = ref(false)
const dataList = ref([])
const loading = ref(true)
const id = ref<string | undefined>()
const contractId = ref<string | undefined>()  
const total = ref(0)
const title = ref('')
const openProduct = ref(false)
let tableSummary: any = ref([]);
const {queryParams, form} = toRefs(data)

const props = defineProps<{
  title?: string
  open?: boolean
  formId?: string
  contractId?: string
}>()


watch(
    () => props.open,
    (val) => {
      console.log("open contractId " +props.formId)
      if(props.open){
        tableSummary = ref([]);
        title.value = props.title ||''
        queryParams.value.contractId = props.formId
        contractId.value = props.formId
        dialogStatus.value= true;
        getList()
      }
    },
    {immediate: true}
)

function getList() {
  loading.value = true
  contractProductService.fetch(queryParams.value).then((response: any) => {
    dataList.value = response.data.records
    total.value = response.data.total
    loading.value = false
  })
}

function handleAdd() {
  id.value = undefined
  title.value = '新增产品'
  openProduct.value = true
}

function handleQuery() {
  queryParams.value.pageNumber = 1
  getList()
}

function resetQuery() {
  queryParams.value.productCode = ''
  queryParams.value.productName = ''
  queryParams.value.status = ''
  handleQuery()
}

function handleUpdate(row: any) {
  openProduct.value = true
  id.value = row.id
  title.value = '编辑产品'
  console.log("handleUpdate " +row.id);
}

function handleDelete(row: any) {
  const _ids = row.id 
  modal.confirm(`是否确认删除编号为"${_ids}"的数据项？`).then(() => {
    return contractProductService.del([_ids])
  }).then((res: any) => {
    if (res.code === 0) {
      getList()
      modal.msgSuccess(t('jbx.alert.delete.success'))
    } else {
      modal.msgError(res.message)
    }
  }).catch(() => {
  })
}

function dialogOfClosedMethods(val: boolean) {
  dialogStatus.value= false
  openProduct.value = false
  id.value = undefined
  emit('dialogOfClosedMethods', val)
}

function dialogFormOfClosedMethods(val: boolean) {
  console.log("dialogFormOfClosedMethods");
  openProduct.value = false
  id.value = undefined
  getList()
}


const getSummaries = () => {
  const sums: (string | VNode)[] = []

  tableSummary.quantity = 0;
  tableSummary.amount = 0;

  dataList.value.forEach((column, index) => {
    console.log(column);
    tableSummary.quantity = tableSummary.quantity+ column["quantity"];
    tableSummary.amount = tableSummary.amount +  column["amount"];
  })

  if (tableSummary != null) {
    let sumsIndex = 0;
    sums[sumsIndex++] = h('div', {style: {textDecoration: 'underline'}}, ['合计',])
    console.log("tableSummary " + tableSummary);
    sums[sumsIndex++] = "";
    sums[sumsIndex++] = "";
    sums[sumsIndex++] = "";
    sums[sumsIndex++] = "";
    sums[sumsIndex++] = tableSummary.quantity;
    sums[sumsIndex++] = "";
    sums[sumsIndex++] = "";
    sums[sumsIndex++] = tableSummary.amount;
  }
  return sums
}

</script>
