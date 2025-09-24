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
            <el-form-item :label="t('contractName')">
              <el-select v-model="form.contractId" filterable :placeholder="t('selectContract')" @change="selectContract">
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
            <el-form-item :label="t('contractCode')" prop="contractCode">
              <el-input v-model="form.contractCode" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item :label="t('contractName')" prop="contractName">
              <el-input v-model="form.contractName" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="span" v-show="false">
            <el-form-item :label="t('cstCode')" prop="customerId">
              <el-input v-model="form.customerId" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item :label="t('customerName')" prop="customerName">
              <el-input v-model="form.customerName" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item :label="t('contractName')" prop="contractAmount">
              <el-input v-model="form.contractAmount" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item :label="t('currency')" prop="currency">
              <el-input v-model="form.currency" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item :label="t('taxRate')" prop="taxRate">
              <el-input v-model="form.taxRate" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item :label="t('RemainingAmount')" prop="contractReceivableAmount">
              <el-input v-model="form.contractReceivableAmount" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item :label="t('TaxpayerNo')" prop="taxNumber">
              <el-input v-model="form.taxNumber" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item :label="t('bankAccount')" prop="taxBankAccount">
              <el-input v-model="form.taxBankAccount" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item :label="t('BankOpening')" prop="taxBank">
              <el-input v-model="form.taxBank" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item :label="t('bankAccount1')" prop="taxBankAccountNumber">
              <el-input v-model="form.taxBankAccountNumber" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item :label="t('contactAddress')" prop="taxContact">
              <el-input v-model="form.taxContact" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item :label="t('ContractAmountRatio')" prop="percentage">
              <el-input v-model="form.percentage"/>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item :label="t('amount')" prop="amount">
              <el-input v-model="form.amount"/>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item :label="t('taxAmount')" prop="taxAmount">
              <el-input v-model="form.taxAmount"/>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item :label="t('AfterTaxAmount')" prop="afterTaxAmount">
              <el-input v-model="form.afterTaxAmount"/>
            </el-form-item>         </el-col>
          <el-col :span="span">
            <el-form-item :label="t('InvoicingDate')" prop="invoiceDate">
              <el-date-picker v-model="form.invoiceDate"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"/>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item :label="t('PaymentDate')" prop="receiveDate">
              <el-date-picker v-model="form.receiveDate"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"/>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item :label="t('org.status')" prop="status">
              <el-select v-model="form.status" :placeholder="t('pleaseSelect')">
                <el-option :label="t('Invoicing')" value="1"/>
                <el-option :label="t('ReceivingPayments')" value="2"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="t('remark')" prop="remark">
              <el-input v-model="form.remark" type="textarea" :rows="3"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </template>
    <template #footer>
      <div style="flex: auto">
        <el-button @click="dialogOfClosedMethods(false)">{{ t('org.cancel') }}</el-button>
        <el-button type="warning" @click="compute">{{t('calculate')}}</el-button>
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
import Decimal from 'decimal.js'

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
  contractAmount: Decimal
  contractReceivableAmount: Decimal
  currency: string
  percentage: number
  taxRate: number
  amount: Decimal
  taxAmount: Decimal
  afterTaxAmount: Decimal
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
    contractAmount :new Decimal(0.00),
    contractReceivableAmount:new Decimal(0.00),
    currency: '',
    taxRate: 0,
    percentage: 0,
    amount: new Decimal(0.00),
    taxAmount: new Decimal(0.00),
    afterTaxAmount: new Decimal(0.00),
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
      {required: true, message: t('contactTip'), trigger: 'blur'}
    ],
    phone: [
      {required: true, message: t('phoneTipInput'), trigger: 'blur'},
      {
        pattern: /^1[3456789]\d{8}$/,
        message: t('phoneTipRule'),
        trigger: 'blur'
      }
    ],
    email: [
      {
        pattern: /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/,
        message: t('emailTipInput'),
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
    if(res.data.payAmount){
      form.value.contractReceivableAmount = new Decimal(res.data.amount).minus( new Decimal(res.data.payAmount))
    }else{
      form.value.contractReceivableAmount = new Decimal(res.data.amount)
    }

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
    form.value.amount = new Decimal(form.value.contractAmount).mul(form.value.percentage).div(100)
  }else{
    form.value.amount = new Decimal(form.value.amount)
  }

    form.value.taxAmount =  new Decimal(form.value.amount.minus(form.value.amount.div(1 + form.value.taxRate/100)).toFixed(2))
  form.value.afterTaxAmount = new Decimal(form.value.amount.minus(form.value.taxAmount).toFixed(2))
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
