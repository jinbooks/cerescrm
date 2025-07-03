<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="45%"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px" inline-message>
        <el-row :gutter="20">
          <el-col :span="span"  v-show="!form.contractId">
            <el-form-item label="合同名称">
              <el-select v-model="form.contractId" filterable placeholder="请选择合同" @change="selectContract">
                <el-option
                    v-for="item in contractList"
                    :key="item.id"
                    :label="item.contractName"
                    :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="合同编码" prop="contractCode">
              <el-input v-model="form.contractCode" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="合同名称" prop="contractName">
              <el-input v-model="form.contractName" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="span" v-show="false">
            <el-form-item label="客户编码" prop="customerId">
              <el-input v-model="form.customerId" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="客户名称" prop="customerName">
              <el-input v-model="form.customerName" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="合同金额" prop="contractAmount">
              <el-input v-model="form.contractAmount" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="货币" prop="currency">
              <el-input v-model="form.currency" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="税率(%)" prop="taxRate">
              <el-input v-model="form.taxRate" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="剩余金额" prop="contractReceivableAmount">
              <el-input v-model="form.contractReceivableAmount" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="纳税人识别号" prop="taxNumber">
              <el-input v-model="form.taxNumber" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="银行账户" prop="taxBankAccount">
              <el-input v-model="form.taxBankAccount" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="开户银行" prop="taxBank">
              <el-input v-model="form.taxBank" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="银行账号" prop="taxBankAccountNumber">
              <el-input v-model="form.taxBankAccountNumber" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="联系地址" prop="taxContact">
              <el-input v-model="form.taxContact" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="合同额占比(%)" prop="percentage">
              <el-input v-model="form.percentage"/>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="金额" prop="amount">
              <el-input v-model="form.amount"/>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="税费" prop="taxAmount">
              <el-input v-model="form.taxAmount"/>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="税后金额" prop="afterTaxAmount">
              <el-input v-model="form.afterTaxAmount"/>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="开票日期" prop="invoiceDate">
              <el-date-picker v-model="form.invoiceDate" 
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"/>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="收款日期" prop="receiveDate">
              <el-date-picker v-model="form.receiveDate"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"/>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择">
                <el-option label="开票" value="1"/>
                <el-option label="收款" value="2"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" :rows="3"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </template>
    <template #footer>
      <div style="flex: auto">
        <el-button @click="dialogOfClosedMethods(false)">{{ t('org.cancel') }}</el-button>
        <el-button type="warning" @click="compute">计算</el-button>
        <el-button type="primary" @click="submitForm">{{ t('org.confirm') }}</el-button>
      </div>
    </template>
  </el-drawer>
</template>

<script setup lang="ts">
import {ElForm} from 'element-plus'
import {reactive, ref, toRefs, watch} from 'vue'
import {useI18n} from 'vue-i18n'
import modal from '@/plugins/modal'
import * as contractService from '@/api/customer/contract'
import {listCustomer} from "@/api/customer/customer";
import * as receivePaymentService  from '@/api/receivepayment/receive-payment'
import * as customerService from '@/api/customer/customer'

const emit = defineEmits(['dialogOfClosedMethods'])
const dialogStatus = ref(false)
const formRef = ref<InstanceType<typeof ElForm> | null>(null)
const {t} = useI18n()

interface FormModel {
  id?: string
  contractId?: string
  contractCode?: string
  contractName?: string
  customerId?: string
  customerName?: string
  taxNumber?: string
  taxContact?: string
  taxBank?: string
  taxBankAccount?: string
  taxBankAccountNumber?: string
  contactName: string
  contractAmount: number
  contractReceivableAmount: number
  currency: string
  percentage: number
  taxRate: number
  amount: number
  taxAmount: number
  afterTaxAmount: number
  invoiceDate: string
  receiveDate: string
  status: string
  jobTitle: string
  phone: string
  email: string
  address: string
  remark: string
}

interface FormState {
  form: FormModel
  rules: Record<string, any>,
  contractList: Array<{ id: string; contractName: string }>
}

const props = defineProps<{
  title?: string
  open?: boolean
  formId?: string
  customerId?: string
}>()

const data = reactive<FormState>({
  form: {
    contractId: '',
    contractCode: '',
    contractName: '',
    customerId: '',
    customerName: '',
    contactName: '',
    contractAmount :0.00,
    contractReceivableAmount:0.00,
    currency: '',
    taxRate: 0,
    percentage: 0,
    amount: 0.00,
    taxAmount: 0.00,
    afterTaxAmount: 0.00,
    invoiceDate: '',
    receiveDate: '',
    status: '1',
    jobTitle: '',
    phone: '',
    email: '',
    address: '',
    remark: ''
  },
  rules: {
    contactName: [
      {required: true, message: '请输入联系人姓名', trigger: 'blur'}
    ],
    phone: [
      {required: true, message: '请输入手机号码', trigger: 'blur'},
      {
        pattern: /^1[3456789]\d{8}$/,
        message: '请输入正确的手机号码',
        trigger: 'blur'
      }
    ],
    email: [
      {
        pattern: /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/,
        message: '请输入正确的电子邮箱',
        trigger: 'blur'
      }
    ]
  },
  contractList: []
})

const {form, rules, contractList} = toRefs(data)
const span = ref(12)

watch(
    () => props.open,
    (val) => {
      if (val) {
        dialogStatus.value = val
        if (props.formId) {
          receivePaymentService.get(props.formId).then((res: any) => {
            form.value = res.data
            selectContract(res.data.contractId);
          })
        } else {
          reset()
          fetchContractList()
        }
      } else {
        reset()
      }
    },
    {immediate: true}
)

function fetchContractList() {
  contractService.listContract({}).then((res: any) => {
    contractList.value = res.data.records
  })
}

function selectContract(val:any) {
  contractService.getContractOne(val).then((res: any) => {
    console.log(res.data);
    form.value.contractId=res.data.id
    form.value.contractCode=res.data.code
    form.value.contractName=res.data.contractName
    form.value.customerId=res.data.customerId
    form.value.customerName=res.data.customerName
    form.value.currency=res.data.currency
    form.value.taxRate=res.data.taxRate
    form.value.contractAmount =res.data.amount
    form.value.contractReceivableAmount = res.data.amount - res.data.payAmount

    customerService.getCustomerOne(form.value.customerId+"").then((res: any) => {
      form.value.taxNumber=res.data.taxNumber;
      form.value.taxContact=res.data.taxContact;
      form.value.taxBank=res.data.taxBank;
      form.value.taxBankAccount=res.data.taxBankAccount;
      form.value.taxBankAccountNumber=res.data.taxBankAccountNumber;
    })
  })
}


function compute(val: any) {
  if(form.value.percentage > 0){
    form.value.amount = (form.value.contractAmount * form.value.percentage) /100
  }
  form.value.taxAmount = form.value.amount * form.value.taxRate /100
  form.value.afterTaxAmount = form.value.amount - form.value.taxAmount
}

function dialogOfClosedMethods(val: any) {
  dialogStatus.value = false
  emit('dialogOfClosedMethods', val)
}

function reset() {
  form.value = {
    contactName: '',
    customerId: props.customerId || '',
    contractAmount: 0.00,
    contractReceivableAmount:0.00,
    currency: '',
    percentage: 0,
    taxRate: 0,
    amount: 0.00,
    taxAmount: 0.00,
    afterTaxAmount: 0.00,
    invoiceDate: '',
    receiveDate: '',
    status: '1',
    jobTitle: '',
    phone: '',
    email: '',
    address: '',
    remark: ''
  }
  formRef?.value?.resetFields()
}

function submitForm() {
  const handleResponse = (res: any, successMessage: string) => {
    if (res.code === 0) {
      modal.msgSuccess(successMessage)
      dialogOfClosedMethods(true)
      reset()
    } else {
      modal.msgError(res.message)
    }
  }

  formRef?.value?.validate((valid: any) => {
    if (valid) {
      const operation = props.formId ? receivePaymentService.update : receivePaymentService.save
      const successMessage = props.formId ? t('org.success.update') : t('org.success.add')
      operation(form.value).then((res: any) => handleResponse(res, successMessage))
    }
  })
}
</script>
