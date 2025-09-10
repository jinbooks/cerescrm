<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="45%"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
      <el-card class="common-card">
        <el-table v-loading="loading" :data="dataList" border  show-summary :summary-method="getSummaries" >
          <el-table-column prop="invoiceDate" label="开票日期" align="center"></el-table-column>
          <el-table-column prop="receiveDate" label="收款日期" align="center"></el-table-column>
          <el-table-column prop="status" :label="t('org.status')" align="center">
            <template #default="scope">
              <el-tag type="primary" v-show="scope.row.status=='1'">开票</el-tag>
              <el-tag type="success" v-show="scope.row.status=='2'">收款</el-tag>
              </template>
          </el-table-column>
           <!--
          <el-table-column prop="contractCode" label="合同编码" align="center"></el-table-column>
          <el-table-column prop="contractName" label="合同名称" align="center"></el-table-column>
         
          <el-table-column prop="customerName" label="客户名称" align="center"></el-table-column>
          <el-table-column prop="currency" label="币种" align="center"></el-table-column>
          -->
          <el-table-column prop="percentage" label="合同额占比(%)" align="center"></el-table-column>
          <el-table-column prop="amount" label="金额" align="center"></el-table-column>
          <el-table-column prop="taxAmount" label="税额" align="center"></el-table-column>
          <el-table-column prop="afterTaxAmount" label="税后金额" align="center"></el-table-column>
          
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
import * as receivePaymentService  from '@/api/receivepayment/receive-payment'
import {h} from 'vue'
import type {VNode} from 'vue'

const emit = defineEmits(['dialogOfClosedMethods'])
const {t} = useI18n()

const data: any = reactive({
  queryParams: {
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
const total = ref(0)
const title = ref('')
const open = ref(false)
let tableSummary: any = ref([]);
const {queryParams, form} = toRefs(data)

const props = defineProps<{
  title?: string
  open?: boolean
  formId?: string
}>()


watch(
    () => props.open,
    (val) => {
      console.log("open contractId " +props.formId)
      if(props.open){
        tableSummary = ref([]);
        title.value = props.title ||''
        queryParams.value.contractId = props.formId
        dialogStatus.value= true;
        getList()
      }
    },
    {immediate: true}
)

function getList() {
  loading.value = true
  receivePaymentService.fetch(queryParams.value).then((response: any) => {
    dataList.value = response.data.records
    total.value = response.data.total
    loading.value = false
  })
}

function dialogOfClosedMethods(val: boolean) {
  dialogStatus.value= false
  open.value = false
  id.value = undefined
  emit('dialogOfClosedMethods', val)
}

const getSummaries = () => {
  const sums: (string | VNode)[] = []

  tableSummary.percentage = 0;
  tableSummary.amount = 0;
  tableSummary.taxAmount = 0;
  tableSummary.afterTaxAmount = 0;
  dataList.value.forEach((column, index) => {
    console.log(column);
    tableSummary.percentage = tableSummary.percentage+ column["percentage"];
    tableSummary.amount = tableSummary.amount +  column["amount"];
    tableSummary.taxAmount = tableSummary.taxAmount +  column["taxAmount"];
    tableSummary.afterTaxAmount = tableSummary.afterTaxAmount +  column["afterTaxAmount"];
  })

  if (tableSummary != null) {
    let sumsIndex = 0;
    sums[sumsIndex++] = h('div', {style: {textDecoration: 'underline'}}, ['合计',])
    console.log("tableSummary " + tableSummary);
    sums[sumsIndex++] = "";
    sums[sumsIndex++] = "";
    sums[sumsIndex++] = tableSummary.percentage;
    sums[sumsIndex++] = tableSummary.amount;
    sums[sumsIndex++] = tableSummary.taxAmount;
     sums[sumsIndex++] = tableSummary.afterTaxAmount;
  }
  return sums
}

</script>
