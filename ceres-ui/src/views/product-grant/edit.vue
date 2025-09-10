<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="750px"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px" inline-message>
        <el-row :gutter="20">
          <el-col :span="span">
            <el-form-item label="客户信息" prop="customerId">
              <customer-select v-model="form.customerId"
                               @select="(ids, names) => {form.customerName = names}"/>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="产品信息" prop="productId">
              <select-product v-model="form.productId"
                              @select="(ids, names) => selectProductChange(ids,names)"></select-product>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="产品版本" prop="productVersion">
              <el-input v-model="form.productVersion" disabled/>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="授权方式" prop="grantType">
              <el-input v-model="form.grantType"/>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="授权码" prop="license">
              <el-input v-model="form.license"/>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="授权日期" prop="grantDate">
              <el-date-picker v-model="form.grantDate"
                              format="YYYY-MM-DD"
                              value-format="YYYY-MM-DD"/>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item :label="t('ExpiryDate')" prop="expirationDate">
              <el-date-picker v-model="form.expirationDate"
                              format="YYYY-MM-DD"
                              value-format="YYYY-MM-DD"/>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" :placeholder="t('pleaseSelect')">
                <el-option label="生效" value="1"/>
                <el-option label="失效" value="2"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="t('remark')">
              <editor v-model="form.content" :height="150"/>
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
import SelectProduct from "../product/select-product/index.vue"
import * as productGrantService from '@/api/product-grant/product-grant'
import CustomerSelect from "@/views/customer/select-componment/index.vue";
import * as productService  from '@/api/product/product'

const emit = defineEmits(['dialogOfClosedMethods'])
const dialogStatus = ref(false)
const formRef = ref<InstanceType<typeof ElForm> | null>(null)
const {t} = useI18n()


interface FormModel {
  id?: string
  customerId?: string
  customerName?: string
  productId?: string
  productCode?: string
  productName?: string
  productVersion?: string
  grantType?: string
  license?: string
  grantDate?: string
  expirationDate?: string
  content?: string
  status?: string
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
      {required: true, message: t('phoneTipInput'), trigger: 'blur'},
      {
        pattern: /^1[3456789]\d{8}$/,
        message: '请输入正确的手机号码',
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
const span = ref(24)

watch(
    () => props.open,
    (val) => {
      if (val) {
        dialogStatus.value = val
        if (props.formId) {
          productGrantService.get(props.formId).then((res: any) => {
            form.value = res.data
          })
        } else {
          reset()
        }
      } else {
        reset()
      }
    },
    {immediate: true}
)

function dialogOfClosedMethods(val: any) {
  dialogStatus.value = false
  emit('dialogOfClosedMethods', val)
}

function reset() {
  form.value = {
    status: '1',
    jobTitle: '',
    phone: '',
    email: '',
    address: '',
    remark: ''
  }
  formRef?.value?.resetFields()
}

function selectProductChange(ids:any, names:any){
  form.value.productName = names
  productService.get(ids).then((res: any) => {
    console.log(res.data);
    form.value.productName=res.data.productName;
    form.value.productCode=res.data.productCode;
    form.value.productId=res.data.id;
    form.value.productVersion=res.data.version;
  })
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
      const operation = props.formId ? productGrantService.update : productGrantService.save
      const successMessage = props.formId ? t('org.success.update') : t('org.success.add')
      operation(form.value).then((res: any) => handleResponse(res, successMessage))
    }
  })
}
</script>
