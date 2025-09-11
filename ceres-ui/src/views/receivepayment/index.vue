<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="80px">
          <el-form-item :label="t('contractCode')">
            <el-input v-model="queryParams.contractCode" :placeholder="t('contractCodeInput')" />
          </el-form-item>
          <el-form-item :label="t('contractName')">
            <el-input v-model="queryParams.contractName" :placeholder="t('contractNameInput')"/>
          </el-form-item>
          <el-form-item :label="t('customerName')">
            <el-input v-model="queryParams.customerName" :placeholder="t('customerTip')"/>
          </el-form-item>
          <el-form-item prop="status" :label="t('org.status')" align="center" width="80" >
            <el-select style="width: 120px" v-model="queryParams.status" clearable :placeholder="t('pleaseSelect')">
              <el-option :label="t('all')" value=""/>
              <el-option :label="t('Invoicing')" value="1"/>
              <el-option :label="t('ReceivingPayments')" value="2"/>
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
      <div class="btn-form">
        <el-button type="primary" @click="handleAdd">{{t('jbx.text.button')}}</el-button>
        <el-button @click="onBatchDelete" :disabled="ids.length === 0">{{t('org.button.deleteBatch')}}</el-button>
      </div>
      <el-table v-loading="loading" :data="dataList" border @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="contractCode" :label="t('contractCode')" align="left" header-align="center" ></el-table-column>
        <el-table-column prop="contractName" :label="t('contractName')" align="left" header-align="center" ></el-table-column>
        <el-table-column prop="customerName" :label="t('customerName')" align="left" header-align="center" ></el-table-column>
        <!--<el-table-column prop="currency" label="币种" align="center"></el-table-column>-->
        <el-table-column prop="percentage" :label="t('Proportion')" align="center"  width="80"></el-table-column>
        <el-table-column prop="amount" :label="t('amount')"  align="right" header-align="center" width="120"></el-table-column>
        <el-table-column prop="taxAmount" :label="t('tax')"  align="right" header-align="center" width="120"></el-table-column>
        <el-table-column prop="afterTaxAmount" :label="t('AfterTaxAmount')"  align="right" header-align="center" width="120"></el-table-column>
        <el-table-column prop="invoiceDate" :label="t('InvoicingDate')"  align="center" header-align="center" width="100"></el-table-column>
        <el-table-column prop="receiveDate" :label="t('PaymentDate')"  align="center" header-align="center" width="100" ></el-table-column>
         <el-table-column prop="status" :label="t('org.status')" align="center" width="80" >
            <template #default="scope">
              <el-tag type="primary" v-show="scope.row.status=='1'">{{t('Invoicing')}}</el-tag>
              <el-tag type="success" v-show="scope.row.status=='2'">{{t('ReceivingPayments')}}</el-tag>
              </template>
          </el-table-column>
        <el-table-column :label="t('org.operate')" align="center" width="120">
          <template #default="scope">
            <el-button link icon="Edit" @click="handleUpdate(scope.row)"></el-button>
            <el-button link icon="Delete" type="danger" @click="handleDelete(scope.row)"></el-button>
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

    <edit-form
        :title="title"
        :open="open"
        :form-id="id"
        @dialogOfClosedMethods="dialogOfClosedMethods"
    ></edit-form>
  </div>
</template>

<script setup lang="ts">
import {reactive, ref, toRefs} from 'vue'
import {useI18n} from 'vue-i18n'
import * as receivePaymentService  from '@/api/receivepayment/receive-payment'
import modal from '@/plugins/modal'
import editForm from './edit.vue'

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
const dataList = ref([])
const loading = ref(true)
const id = ref<string | undefined>()
const total = ref(0)
const title = ref('')
const open = ref(false)
const ids = ref<string[]>([])
const selectionlist = ref<any>([])

const {queryParams, form} = toRefs(data)

function handleQuery() {
  queryParams.value.pageNumber = 1
  getList()
}

function resetQuery() {
  queryParams.value.contactName = ''
  queryParams.value.gender = ''
  queryParams.value.phone = ''
  handleQuery()
}

function getList() {
  loading.value = true
  receivePaymentService.fetch(queryParams.value).then((response: any) => {
    dataList.value = response.data.records
    total.value = response.data.total
    loading.value = false
  })
}

function dialogOfClosedMethods(val: boolean) {
  open.value = false
  id.value = undefined
  if (val) {
    getList()
  }
}

function handleAdd() {
  id.value = undefined
  title.value = t('jbx.text.add')
  open.value = true
}

function handleUpdate(row: any) {
  id.value = row.id
  title.value = t('jbx.text.edit')
  open.value = true
}

function handleDelete(row: any) {
  const _ids = row.id || ids.value
  modal.confirm(`${t('deleteTipCommon')}${_ids}${t('deleteTipLead1')}`).then(() => {
    return receivePaymentService.del([_ids])
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

function onBatchDelete() {
  modal.confirm(t('jbx.confirm.text.delete')).then(() => {
    return receivePaymentService.del(ids.value)
  }).then((res: any) => {
    if (res.code === 0) {
      handleQuery()
      modal.msgSuccess(t('jbx.alert.delete.success'))
    } else {
      modal.msgError(res.message)
    }
  }).catch(() => {
  })
}

function handleSelectionChange(selection: any) {
  selectionlist.value = selection
  ids.value = selectionlist.value.map((item: any) => item.id)
}

getList()
</script>
