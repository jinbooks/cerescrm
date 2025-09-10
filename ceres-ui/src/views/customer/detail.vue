<template>
  <div class="app-container">
    <el-card class="common-card detail-header">
      <el-divider><span>客户基本信息</span></el-divider>
      <el-descriptions :column="4" border label-class-name="label-style" style="margin-top: 15px">
        <el-descriptions-item :label="t('customerName')">{{ customer.customerName }}</el-descriptions-item>
        <el-descriptions-item :label="t('customerType')">{{ customer.customerType }}</el-descriptions-item>
        <el-descriptions-item :label="t('belongIndustry')">{{ customer.industry }}</el-descriptions-item>
        <el-descriptions-item :label="t('area')">{{ customer.province +"/" +customer.city}}</el-descriptions-item>

        <el-descriptions-item :label="t('PersonCharge')">{{ customer.customerHead }}</el-descriptions-item>
        <el-descriptions-item :label="t('jbx.organizations.phone')">{{ customer.phone }}</el-descriptions-item>
        <el-descriptions-item :label="t('jbx.institutions.email')">{{ customer.email }}</el-descriptions-item>
        <el-descriptions-item :label="t('wechat')">{{ customer.wechat }}</el-descriptions-item>
        <el-descriptions-item :label="t('webUrl')">{{ customer.website || t('none') }}</el-descriptions-item>
        <el-descriptions-item :label="t('sourceChannel')">{{ customer.customerFrom }}</el-descriptions-item>

        <el-descriptions-item :label="t('LastPurchaseTime')">{{ customer.lastPurchaseTime || t('none') }}</el-descriptions-item>
        <el-descriptions-item :label="t('CumulativeCA')">{{ customer.totalSpending || 0 }} {{t('Yuan')}}</el-descriptions-item>
        <el-descriptions-item :label="t('ServiceSatisfaction')">
          <el-rate v-model="customer.serviceSatisfaction" disabled/>
        </el-descriptions-item>
        <el-descriptions-item :label="t('customerLayer')">{{ customer.segmentName || t('Uncategorized') }}</el-descriptions-item>

      </el-descriptions>
      <el-divider><span>{{t('BillingInfo')}}</span></el-divider>
      <el-descriptions :column="3" border label-class-name="label-style" style="margin-top: 15px">
        <el-descriptions-item :label="t('TaxpayerNo')">{{ customer.taxNumber }}</el-descriptions-item>
        <el-descriptions-item :label="t('accountName')">{{ customer.taxBankAccount }}</el-descriptions-item>

        <el-descriptions-item :label="t('BankOpening')">{{ customer.taxBank }}</el-descriptions-item>
        <el-descriptions-item :label="t('bankAccount')">{{ customer.taxBankAccountNumber }}</el-descriptions-item>

        <el-descriptions-item :label="t('contactWay')">{{ customer.taxContact }}</el-descriptions-item>
      </el-descriptions>
      <el-descriptions :column="2" border label-class-name="label-style" style="margin-top: 15px">
        <el-descriptions-item :label="t('remark')"><editor v-model="customer.content" :height="150" /></el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-tabs v-model="activeTab" type="card">
      <el-tab-pane :label="t('contact')" name="contact"></el-tab-pane>
      <el-tab-pane :label="t('contract')" name="contract"></el-tab-pane>
    </el-tabs>

    <el-card v-if="activeTab==='contact'" class="common-card">
      <div class="btn-form">
        <el-button type="primary" @click="handleContactAdd">{{t('newContact')}}</el-button>
      </div>
      <el-table :data="contacts" border style="width: 100%">
        <el-table-column prop="contactName" :label="t('jbx.users.displayName')" align="center"></el-table-column>
        <el-table-column prop="gender" :label="t('jbx.users.gender')" align="center"></el-table-column>
        <el-table-column prop="jobTitle" :label="t('jbx.users.jobTitle')" align="center"></el-table-column>
        <el-table-column prop="phone" :label="t('jbx.organizations.phone')" align="center"></el-table-column>
        <el-table-column prop="email" :label="t('jbx.institutions.email')" align="center"></el-table-column>
        <el-table-column :label="t('org.operate')" align="center" width="120">
          <template #default="scope">
            <el-button link icon="Edit" @click="editContact(scope.row)"></el-button>
            <el-button link icon="Delete" type="danger" @click="handleContactDelete(scope.row)"></el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination
          v-show="contactTotal > 0"
          :total="contactTotal"
          v-model:page="contactQuery.pageNumber"
          v-model:limit="contactQuery.pageSize"
          :page-sizes="contactQuery.pageSizeOptions"
          @pagination="getContacts"
      />
    </el-card>
    <el-card v-if="activeTab==='contract'" class="common-card">
      <el-table :data="contracts" border style="width: 100%">
        <el-table-column prop="code" :label="t('contractCode')" header-align="center" min-width="50"></el-table-column>
        <el-table-column prop="contractName" :label="t('contractName')" header-align="center" min-width="50">
          <template #default="scope">
            <el-button link type="primary" @click="handleContractDetail(scope.row)">{{ scope.row.contractName }}</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="amount" :label="t('contractAmount')" align="center"></el-table-column>
        <!--<el-table-column prop="currency" label="货币类型" align="center"></el-table-column>
        <el-table-column prop="taxRate" label="税率" align="center"></el-table-column>-->
        <el-table-column prop="signingDate" :label="t('SigningDate')"  align="center" header-align="center" width="100"></el-table-column>
        <!--<el-table-column prop="effectiveDate" label="生效日期"  align="center"  header-align="center" width="100"></el-table-column>-->
        <el-table-column prop="expirationDate" :label="t('ExpiryDate')"  align="center" header-align="center" width="100"></el-table-column>
        <el-table-column prop="status" :label="t('org.status')" align="center" width="120">
          <template #default="scope">
            <el-tag :type="getStatusLabel(scope.row.status).type">
              {{ getStatusLabel(scope.row.status).label }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" :label="t('Repayment')" align="center" width="120">
          <template #default="scope">
            <el-button link icon="CreditCard" @click="viewPayment(scope.row)"></el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination
          v-show="contractTotal > 0"
          :total="contractTotal"
          v-model:page="contractQuery.pageNumber"
          v-model:limit="contractQuery.pageSize"
          :page-sizes="contractQuery.pageSizeOptions"
          @pagination="getContracts"
      />
    </el-card>

    <edit-contact-form
        :title="titleContact"
        :open="openContact"
        :form-id="idContact"
        :customer-id="customerId"
        @dialogOfClosedMethods="dialogOfClosedMethods"
    ></edit-contact-form>
    <el-drawer v-model="openContract" :close-on-click-modal="false" size="1200px">
      <template #header>
        <h4>{{ titleContract }}</h4>
      </template>
      <template #default>
        <div v-if="contentContract">
          <Editor v-model="contentContract" :min-height="500" :read-only="true"></Editor>
        </div>
        <div v-else>{{t('UndefinedContractContent')}}</div>
      </template>
    </el-drawer>

    <receive-payment-list
        :title="titlePayment"
        :open="openPayment"
        :form-id="contractId"
        @dialogOfClosedMethods="dialogOfClosedMethods"
    ></receive-payment-list>
  </div>
</template>

<script setup lang="ts">
import {ref, onMounted} from 'vue'
import {useI18n} from 'vue-i18n'
import {useRouter} from 'vue-router'
import {getCustomerOne} from '@/api/customer/customer'
import {delCustomerContact, listCustomerContact as fetchContactPage} from '@/api/customer/people'
import {listContract as fetchContractPage, getContractOne} from '@/api/customer/contract'
import editContactForm from './people/edit.vue'
import modal from "@/plugins/modal";
import Editor from "@/components/Editor/index.vue"
import receivePaymentList from '../receivepayment/receive-payment-list.vue'

const {t} = useI18n()
const router = useRouter()

const idContact = ref<string | undefined>()
const titleContact = ref('')
const openContact = ref(false)

const contentContract = ref<string>()
const titleContract = ref('')
const openContract = ref(false)

const titlePayment = ref('')
const openPayment = ref(false)
const contractId = ref('')

// 获取路由参数
const customerId = router.currentRoute.value.query.id as string

// 当前激活的 tab
const activeTab = ref('contact')

// 客户基础信息
const customer = ref<any>({})

// 联系人相关
const contacts = ref<any[]>([])
const contactQuery = ref({
  pageNumber: 1,
  pageSize: 10,
  pageSizeOptions: [10, 20, 50],
  customerId: customerId
})
const contactTotal = ref(0)

// 合同相关
const contracts = ref<any[]>([])
const contractQuery = ref({
  pageNumber: 1,
  pageSize: 10,
  pageSizeOptions: [10, 20, 50],
  customerId: customerId
})
const contractTotal = ref(0)

// 加载客户详情
function loadCustomerDetail() {
  if (!customerId) return
  getCustomerOne(customerId).then((res: any) => {
    customer.value = res.data
  })
}

// 加载联系人列表
function getContacts() {
  contactQuery.value.customerId = customerId
  fetchContactPage(contactQuery.value).then((res: any) => {
    contacts.value = res.data.records
    contactTotal.value = res.data.total
  })
}

// 加载合同列表
function getContracts() {
  contractQuery.value.customerId = customerId
  fetchContractPage(contractQuery.value).then((res: any) => {
    contracts.value = res.data.records
    contractTotal.value = res.data.total
  })
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

// 编辑联系人
function editContact(row: any) {
  idContact.value = row.id
  titleContact.value = t('updateContact')
  openContact.value = true
}

function dialogOfClosedMethods(val: boolean) {
  openContact.value = false
  openPayment.value = false
  idContact.value = undefined
  if (val) {

  }
}

function handleContactAdd() {
  idContact.value = undefined
  titleContact.value = t('addContact')
  openContact.value = true
}

function viewPayment(row: any) {
  contractId.value = row.id
  titlePayment.value = t('showRepay')
  openPayment.value = true
  console.log(titlePayment.value +" , id "+contractId.value );
}

function handleContactDelete(row: any) {
  const _ids = row.id
  modal.confirm(`${t('deleteTipCommon')}${_ids}${t('deleteTipLead1')}`).then(() => {
    return delCustomerContact({listIds: [_ids]})
  }).then((res: any) => {
    if (res.code === 0) {
      getContacts()
      modal.msgSuccess(t('jbx.alert.delete.success'))
    } else {
      modal.msgError(res.message)
    }
  }).catch(() => {
  })
}

function handleContractDetail(row: any): void {
  getContractOne(row.id).then((res: any) => {
    contentContract.value = res.data.content
    titleContract.value = t('ContractDetails')
    openContract.value = true
  })
}

onMounted(() => {
  loadCustomerDetail()
  getContacts()
  getContracts()
})
</script>

<style scoped>

</style>
