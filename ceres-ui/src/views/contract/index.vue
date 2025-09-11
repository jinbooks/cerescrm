<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="80px">
          <el-form-item :label="t('contractName')">
            <el-input v-model="queryParams.contractName" placeholder="请输入合同名称"/>
          </el-form-item>
          <el-form-item :label="t('SigningDate')">
            <el-date-picker
                v-model="queryParams.startSigningDate"
                type="date"
                :placeholder="t('startDate')"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
            />
            <span style="margin: 0 10px">{{t('to')}}</span>
            <el-date-picker
                v-model="queryParams.endSigningDate"
                type="date"
                :placeholder="t('endDate')"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
            />
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
        <el-button type="primary" @click="handleAdd">{{t('newContract')}}</el-button>
        <el-button @click="onBatchDelete" :disabled="ids.length === 0">{{t('org.button.deleteBatch')}}</el-button>
      </div>
      <el-table v-loading="loading" :data="dataList" border @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column :label="t('followUp')" align="center" width="60"
                         :show-overflow-tooltip="true">
          <template #default="scope">
            <el-button icon="Comment" type="warning" link @click="followUp(scope.row)"></el-button>
          </template>
        </el-table-column>
        <el-table-column prop="code" :label="t('contractCode')" align="left" header-align="center" width="120"></el-table-column>
        <el-table-column prop="contractName" :label="t('contractName')" align="left" header-align="center"></el-table-column>
        <el-table-column prop="customerName" :label="t('customerName')" align="left" header-align="center">
          <template #default="scope">
            <router-link :to="'/customer/detail?id=' + scope.row.customerId">
              <el-link type="primary">{{ scope.row.customerName }}</el-link>
            </router-link>
          </template>
        </el-table-column>
        <el-table-column :label="t('product')" align="center" width="60">
          <template #default="scope">
            <el-button link icon="ShoppingCartFull" @click="viewProduct(scope.row)"></el-button>
          </template>
        </el-table-column>
        <el-table-column prop="amount" :label="t('contractName')" align="right"  header-align="center"  width="120"></el-table-column>
        <!--<el-table-column prop="invoiceAmount" label="开票金额" align="right" header-align="center"  width="120"></el-table-column>-->
        <el-table-column prop="receiveAmount" :label="t('ReceiptAmount')" align="right" header-align="center"  width="120"></el-table-column>
        <el-table-column prop="invoiceAmount" :label="t('RemainingAmount')" align="right" header-align="center" width="120">
          <template #default="scope">
            {{ scope.row.amount -  scope.row.payAmount}}
          </template>
        </el-table-column>
        <el-table-column prop="status" :label="t('org.status')" align="center" width="100">
          <template #default="scope">
            <el-tag :type="getStatusLabel(scope.row.status).type">
              {{ getStatusLabel(scope.row.status).label }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="signingDate" :label="t('SigningDate')"  align="center" header-align="center" width="100"></el-table-column>
        <el-table-column prop="expirationDate" :label="t('ExpiryDate')"  align="center" header-align="center" width="100"></el-table-column>
        <el-table-column :label="t('Repayment')" align="center" width="60">
          <template #default="scope">
            <el-button link icon="View" @click="viewPayment(scope.row)"></el-button>
          </template>
        </el-table-column>
        <el-table-column :label="t('org.operate')" align="center" width="120">
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

    <edit-form
        v-if="open"
        :title="title"
        :open="open"
        :form-id="id"
        @dialogOfClosedMethods="dialogOfClosedMethods"
    ></edit-form>
    <receive-payment-list
        :title="title"
        :open="openPayment"
        :form-id="id"
        @dialogOfClosedMethods="dialogOfClosedMethods"
    ></receive-payment-list>

    <contract-product
        :title="title"
        :open="openProduct"
        :form-id="id"
        @dialogOfClosedMethods="dialogOfClosedMethods"
    ></contract-product>
    <!-- 跟进-->
    <follow-up-form :relation-id="id" :open="followUpOpen" :title="title"
                    :follow-up-type="4"
                    @dialogOfClosedMethods="dialogOfClosedMethods">
    </follow-up-form>
  </div>
</template>

<script setup lang="ts">
import {reactive, ref, toRefs} from 'vue'
import {useI18n} from 'vue-i18n'
import {listContract, delContract} from '@/api/customer/contract'
import modal from '@/plugins/modal'
import editForm from './edit.vue'
import receivePaymentList from '../receivepayment/receive-payment-list.vue'
import contractProduct from './contract-product.vue'
import FollowUpForm from "@/views/follow-up/edit.vue";

const {t} = useI18n()

const data: any = reactive({
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    pageSizeOptions: [10, 20, 50],
    contractName: '',
    startSigningDate: '',
    endSigningDate: ''
  },
  form: {}
})
const dataList = ref([])
const loading = ref(true)
const id = ref<string | undefined>()
const total = ref(0)
const title = ref('')
const open = ref(false)
const openPayment = ref(false)
const openProduct = ref(false)
const ids = ref<string[]>([])
const selectionlist = ref<any>([])
const followUpOpen = ref(false);

const {queryParams, form} = toRefs(data)

function handleQuery() {
  queryParams.value.pageNumber = 1
  getList()
}

function resetQuery() {
  queryParams.value.contractName = ''
  queryParams.value.startSigningDate = ''
  queryParams.value.endSigningDate = ''
  handleQuery()
}

function getList() {
  loading.value = true
  listContract(queryParams.value).then((response: any) => {
    dataList.value = response.data.records
    total.value = response.data.total
    loading.value = false
  })
}

function dialogOfClosedMethods(val: boolean) {
  open.value = false
  openPayment.value = false
  openProduct.value = false
  id.value = undefined
  followUpOpen.value = false;
  if (val) {
    getList()
  }
}

function handleAdd() {
  id.value = undefined
  title.value = t('newContract')
  open.value = true
}

function viewProduct(row: any) {
  id.value = row.id
  title.value = t('showProduct')
  openProduct.value = true
  console.log(title.value +" , id "+id.value );
}


function viewPayment(row: any) {
  id.value = row.id
  title.value = t('showRepay')
  openPayment.value = true
  console.log(title.value +" , id "+id.value );
}


function handleUpdate(row: any) {
  id.value = row.id
  title.value = t('editContract')
  open.value = true
}

function handleDelete(row: any) {
  const _ids = row.id || ids.value
  modal.confirm(`${t('deleteTipCommon')}${_ids}${t('deleteTipLead1')}`).then(() => {
    return delContract([_ids])
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
    return delContract( ids.value)
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

// 状态标签样式
function getStatusLabel(status: string) {
  switch (status) {
    case '草稿':
      return {label: t('draft'), type: ''}
    case '待审批':
      return {label: t('AwaitingApproval'), type: 'warning'}
    case '审批通过':
      return {label: t('Approved'), type: 'success'}
    case '审批拒绝':
      return {label: t('ApprovalRejection'), type: 'danger'}
    case '履行中':
      return {label: t('InProgress'), type: 'info'}
    case '已完成':
      return {label: t('Completed'), type: 'success'}
    case '已终止':
      return {label: t('Terminated'), type: 'danger'}
    default:
      return {label: status, type: ''}
  }
}

function followUp(row: any) {
  id.value = row.id;
  title.value = t('contractFollow');
  followUpOpen.value = true;
}


getList()
</script>
