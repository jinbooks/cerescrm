<template>
  <div class="app-container">
    <el-card class="common-card detail-header">
      <el-divider><span>客户基本信息</span></el-divider>
      <el-descriptions :column="4" border label-class-name="label-style" style="margin-top: 15px">
        <el-descriptions-item label="客户名称">{{ customer.customerName }}</el-descriptions-item>
        <el-descriptions-item label="客户类型">{{ customer.customerType }}</el-descriptions-item>
        <el-descriptions-item label="所属行业">{{ customer.industry }}</el-descriptions-item>
        <el-descriptions-item label="地区">{{ customer.province +"/" +customer.city}}</el-descriptions-item>

        <el-descriptions-item :label="t('PersonCharge')">{{ customer.customerHead }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ customer.phone }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ customer.email }}</el-descriptions-item>
        <el-descriptions-item label="微信">{{ customer.wechat }}</el-descriptions-item>
        <el-descriptions-item label="官网网址">{{ customer.website || '无' }}</el-descriptions-item>
        <el-descriptions-item label="来源渠道">{{ customer.customerFrom }}</el-descriptions-item>

        <el-descriptions-item label="最近购买时间">{{ customer.lastPurchaseTime || '无' }}</el-descriptions-item>
        <el-descriptions-item label="累计消费金额">{{ customer.totalSpending || 0 }} 元</el-descriptions-item>
        <el-descriptions-item label="服务满意度">
          <el-rate v-model="customer.serviceSatisfaction" disabled/>
        </el-descriptions-item>
        <el-descriptions-item label="客户分层">{{ customer.segmentName || '未分类' }}</el-descriptions-item>

      </el-descriptions>
      <el-divider><span>开票信息</span></el-divider>
      <el-descriptions :column="3" border label-class-name="label-style" style="margin-top: 15px">
        <el-descriptions-item label="纳税人识别号">{{ customer.taxNumber }}</el-descriptions-item>
        <el-descriptions-item label="账户名称">{{ customer.taxBankAccount }}</el-descriptions-item>

        <el-descriptions-item label="开户银行">{{ customer.taxBank }}</el-descriptions-item>
        <el-descriptions-item label="银行账户">{{ customer.taxBankAccountNumber }}</el-descriptions-item>

        <el-descriptions-item label="联系方式">{{ customer.taxContact }}</el-descriptions-item>
      </el-descriptions>
      <el-descriptions :column="2" border label-class-name="label-style" style="margin-top: 15px">
        <el-descriptions-item label="备注"><editor v-model="customer.content" :height="150" /></el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-tabs v-model="activeTab" type="card">
      <el-tab-pane label="联系人" name="contact"></el-tab-pane>
      <el-tab-pane label="合同" name="contract"></el-tab-pane>
    </el-tabs>

    <el-card v-if="activeTab==='contact'" class="common-card">
      <div class="btn-form">
        <el-button type="primary" @click="handleContactAdd">新增联系人</el-button>
      </div>
      <el-table :data="contacts" border style="width: 100%">
        <el-table-column prop="contactName" label="姓名" align="center"></el-table-column>
        <el-table-column prop="gender" label="性别" align="center"></el-table-column>
        <el-table-column prop="jobTitle" label="职位" align="center"></el-table-column>
        <el-table-column prop="phone" label="联系电话" align="center"></el-table-column>
        <el-table-column prop="email" label="邮箱" align="center"></el-table-column>
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
        <el-table-column prop="code" label="合同编码" header-align="center" min-width="50"></el-table-column>
        <el-table-column prop="contractName" label="合同名称" header-align="center" min-width="50">
          <template #default="scope">
            <el-button link type="primary" @click="handleContractDetail(scope.row)">{{ scope.row.contractName }}</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="amount" label="合同金额" align="center"></el-table-column>
        <!--<el-table-column prop="currency" label="货币类型" align="center"></el-table-column>
        <el-table-column prop="taxRate" label="税率" align="center"></el-table-column>-->
        <el-table-column prop="signingDate" label="签订日期"  align="center" header-align="center" width="100"></el-table-column>
        <!--<el-table-column prop="effectiveDate" label="生效日期"  align="center"  header-align="center" width="100"></el-table-column>-->
        <el-table-column prop="expirationDate" label="到期日期"  align="center" header-align="center" width="100"></el-table-column>
        <el-table-column prop="status" :label="t('org.status')" align="center" width="120">
          <template #default="scope">
            <el-tag :type="getStatusLabel(scope.row.status).type">
              {{ getStatusLabel(scope.row.status).label }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="回款" align="center" width="120">
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
        <div v-else>未定义合同内容</div>
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
      return {label: '草稿', type: ''}
    case '待审批':
      return {label: '待审批', type: 'warning'}
    case '审批通过':
      return {label: '审批通过', type: 'success'}
    case '审批拒绝':
      return {label: '审批拒绝', type: 'danger'}
    case '履行中':
      return {label: '履行中', type: 'info'}
    case '已完成':
      return {label: '已完成', type: 'success'}
    case '已终止':
      return {label: '已终止', type: 'danger'}
    default:
      return {label: status, type: ''}
  }
}

// 编辑联系人
function editContact(row: any) {
  idContact.value = row.id
  titleContact.value = '编辑联系人'
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
  titleContact.value = '新增联系人'
  openContact.value = true
}

function viewPayment(row: any) {
  contractId.value = row.id
  titlePayment.value = '查看回款'
  openPayment.value = true
  console.log(titlePayment.value +" , id "+contractId.value );
}

function handleContactDelete(row: any) {
  const _ids = row.id
  modal.confirm(`是否确认删除编号为"${_ids}"的数据项？`).then(() => {
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
    titleContract.value = '合同详情'
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
