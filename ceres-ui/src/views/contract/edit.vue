<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="45%"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px" inline-message>
        <el-row :gutter="20">
          <el-col :span="span">
            <el-form-item label="合同编码">
              <el-input v-model="form.code" style="width: 100%"/>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="合同名称">
              <el-input v-model="form.contractName"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="span">
            <el-form-item label="客户名称">
              <customer-select v-model="form.customerId"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="span">
            <el-form-item label="合同金额">
              <el-input-number v-model="form.amount" :precision="2" :step="1000" :min="0" style="width: 100%"/>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="合同税率(%)">
              <el-input-number v-model="form.taxRate" :precision="2" :step="1" :max="100" :min="0" style="width: 100%"/>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="货币类型">
              <el-input v-model="form.currency" placeholder="如：CNY / USD"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="合同内容">
              <editor v-model="form.content" :height="300"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="span">
            <el-form-item label="签订日期">
              <el-date-picker
                  v-model="form.signingDate"
                  type="date"
                  placeholder="选择日期"
                  format="YYYY-MM-DD"
                  value-format="YYYY-MM-DD"
                  style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="生效日期">
              <el-date-picker
                  v-model="form.effectiveDate"
                  type="date"
                  placeholder="选择日期"
                  format="YYYY-MM-DD"
                  value-format="YYYY-MM-DD"
                  style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="span">
            <el-form-item label="到期日期">
              <el-date-picker
                  v-model="form.expirationDate"
                  type="date"
                  placeholder="选择日期"
                  format="YYYY-MM-DD"
                  value-format="YYYY-MM-DD"
                  style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item :label="t('org.status')">
              <el-select v-model="form.status" placeholder="请选择">
                <el-option label="草稿" value="草稿"/>
                <el-option label="待审批" value="待审批"/>
                <el-option label="审批通过" value="审批通过"/>
                <el-option label="审批拒绝" value="审批拒绝"/>
                <el-option label="履行中" value="履行中"/>
                <el-option label="已完成" value="已完成"/>
                <el-option label="已终止" value="已终止"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </template>
    <template #footer>
      <div style="flex: auto">
        <el-button @click="dialogOfClosedMethods(false)">{{ t('org.cancel') }}</el-button>
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
import {getContractOne, saveContract, updateContract} from '@/api/customer/contract'
// 客户列表用于下拉选择客户（假设从接口获取）
import {listCustomer} from '@/api/customer/customer'
import Editor from "@/components/Editor/index.vue"
import CustomerSelect from "../customer/select-componment/index.vue"
import SelectProduct from "../product/select-product/index.vue"

const emit = defineEmits(['dialogOfClosedMethods'])
const dialogStatus = ref(false)
const formRef = ref<InstanceType<typeof ElForm> | null>(null)
const {t} = useI18n()

interface FormModel {
  id?: string
  code?: string
  contractName?: string
  customerId?: string
  amount: number
  currency: string
  status: string
  signingDate: string
  effectiveDate: string
  expirationDate: string
  content: string
  taxRate: number
}

interface FormState {
  form: FormModel
  rules: Record<string, any>
  customerList: Array<{ id: string; customerName: string }>
}

const props = defineProps<{
  title?: string
  open?: boolean
  formId?: string
}>()

const data = reactive<FormState>({
  form: {
    code: '',
    contractName: '',
    customerId: '',
    content: '',
    taxRate: 0,
    amount: 0,
    currency: 'CNY',
    status: '草稿',
    signingDate: '',
    effectiveDate: '',
    expirationDate: ''
  },
  rules: {},
  customerList: []
})

const {form, rules, customerList} = toRefs(data)
const span = ref(12)

watch(
    () => props.open,
    (val) => {
      if (val) {
        dialogStatus.value = val
        if (props.formId) {
          getContractOne(props.formId).then((res: any) => {
            form.value = res.data
          })
        } else {
          reset()
        }
      } else {
        reset()
      }
      fetchCustomerList()
    },
    {immediate: true}
)

function fetchCustomerList() {
  listCustomer({}).then((res: any) => {
    customerList.value = res.data.records
  })
}

function dialogOfClosedMethods(val: any) {
  dialogStatus.value = false
  emit('dialogOfClosedMethods', val)
}

function reset() {
  form.value = {
    contractName: '',
    customerId: '',
    content: '',
    taxRate: 0,
    amount: 0,
    currency: 'CNY',
    status: '草稿',
    signingDate: '',
    effectiveDate: '',
    expirationDate: ''
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
      const operation = props.formId ? updateContract : saveContract
      const successMessage = props.formId ? t('org.success.update') : t('org.success.add')
      operation(form.value).then((res: any) => handleResponse(res, successMessage))
    }
  })
}
</script>
