<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="600px"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px" inline-message>
        <el-row :gutter="20">
          <el-col v-if="!customerId" :span="span">
            <el-form-item :label="t('customerName')">
              <customer-select v-model="form.customerId"/>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="姓名" prop="contactName">
              <el-input v-model="form.contactName"/>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item :label="t('jbx.organizations.phone')" prop="phone">
              <el-input v-model="form.phone"/>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item :label="t('wechat')" prop="wechat">
              <el-input v-model="form.wechat"/>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item :label="t('jbx.users.gender')" prop="gender">
              <el-select v-model="form.gender" :placeholder="t('pleaseSelect')">
                <el-option label="男" value="男"/>
                <el-option label="女" value="女"/>
                <el-option label="未知" value="未知"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="span">
            <el-form-item label="职位名称" prop="jobTitle">
              <el-input v-model="form.jobTitle"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="span">
            <el-form-item label="电子邮箱" prop="email">
              <el-input v-model="form.email"/>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="地址" prop="address">
              <el-input v-model="form.address"/>
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
import {getCustomerContactOne, saveCustomerContact, updateCustomerContact} from '@/api/customer/people'
import {listCustomer} from "@/api/customer/customer";
import CustomerSelect from "../select-componment/index.vue"

const emit = defineEmits(['dialogOfClosedMethods'])
const dialogStatus = ref(false)
const formRef = ref<InstanceType<typeof ElForm> | null>(null)
const {t} = useI18n()

interface FormModel {
  id?: string
  customerId?: string
  customerName?: string
  contactName: string
  gender: string
  jobTitle: string
  phone: string
  email: string
  address: string
  remark: string
  wechat: string
}

interface FormState {
  form: FormModel
  rules: Record<string, any>,
  customerList: Array<{ id: string; customerName: string }>
}

const props = defineProps<{
  title?: string
  open?: boolean
  formId?: string
  customerId?: string
}>()

const data = reactive<FormState>({
  form: {
    customerId: '',
    customerName: '',
    contactName: '',
    gender: '未知',
    jobTitle: '',
    phone: '',
    email: '',
    address: '',
    remark: '',
    wechat: ''
  },
  rules: {
    contactName: [
      {required: true, message: '请输入联系人姓名', trigger: 'blur'}
    ],
    phone: [
      {required: true, message: '请输入手机号码', trigger: 'blur'}
    ],
    email: [
      {
        pattern: /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/,
        message: '请输入正确的电子邮箱',
        trigger: 'blur'
      }
    ]
  },
  customerList: []
})

const {form, rules, customerList} = toRefs(data)
const span = ref(24)

watch(
    () => props.open,
    (val) => {
      if (val) {
        dialogStatus.value = val
        if (props.formId) {
          getCustomerContactOne(props.formId).then((res: any) => {
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
    contactName: '',
    customerId: props.customerId || '',
    gender: '未知',
    jobTitle: '',
    phone: '',
    email: '',
    address: '',
    remark: '',
    wechat: ''
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
      const operation = props.formId ? updateCustomerContact : saveCustomerContact
      const successMessage = props.formId ? t('org.success.update') : t('org.success.add')
      operation(form.value).then((res: any) => handleResponse(res, successMessage))
    }
  })
}
</script>
