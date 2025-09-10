<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="40%"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px" inline-message>
        <el-tabs
            v-model="activeName"
            type="card"
            class="demo-tabs"
        >
          <el-tab-pane label="基本信息" name="first">

            <el-row :gutter="20">
              <el-col :span="span">
                <el-form-item label="客户名称" prop="customerName">
                  <el-input v-model="form.customerName"/>
                </el-form-item>
              </el-col>
              <el-col :span="span">
                <el-form-item label="客户类型" prop="customerType">
                  <el-select v-model="form.customerType" placeholder="请选择">
                    <el-option label="个人" value="个人"/>
                    <el-option label="企业" value="企业"/>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="客户分组">
              <el-select v-model="form.segmentId" clearable placeholder="请选择">
                <el-option v-for="(item, index) in dataSegmentList" :key="index"
                           :value="item.id" :label="item.segmentName"></el-option>
              </el-select>
            </el-form-item>
            <el-row :gutter="20">
              <el-col :span="span">
                <el-form-item label="所属行业" prop="industry">
                  <el-input v-model="form.industry"/>
                </el-form-item>
              </el-col>
              <el-col :span="span">
                <el-form-item label="所在地区">
                  <el-cascader
                      v-model="region"
                      :options="pcTextArr"
                      :props="{ emitPath: true, checkStrictly: true }"
                      clearable
                      filterable
                      placeholder="请选择省市"
                      style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="span">
                <el-form-item label="地址" prop="address">
                  <el-input v-model="form.address"/>
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
                <el-form-item label="联系电话" prop="phone">
                  <el-input v-model="form.phone"/>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="span">
                <el-form-item label="微信" prop="wechat">
                  <el-input v-model="form.wechat"/>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="span">
                <el-form-item label="官网网址" prop="website">
                  <el-input v-model="form.website"/>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="span">
                <el-form-item label="备注">
                  <editor v-model="form.content" :height="250"/>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="span">
                <el-form-item :label="t('PersonCharge')" prop="customerHead">
                  <el-input v-model="form.customerHead"/>
                </el-form-item>
              </el-col>
              <el-col :span="span">
                <el-form-item label="来源渠道" prop="customerFrom">
                  <el-select v-model="form.customerFrom" placeholder="请选择">
                    <el-option label="搜索引擎" value="搜索引擎"/>
                    <el-option label="直接访问" value="直接访问"/>
                    <el-option label="邮件营销" value="邮件营销"/>
                    <el-option label="社交媒体" value="社交媒体"/>
                    <el-option label="广告" value="广告"/>
                    <el-option label="销售人员介绍" value="销售人员介绍"/>
                    <el-option label="客户介绍" value="客户介绍"/>
                    <el-option label="其他渠道" value="其他渠道"/>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
          <el-tab-pane label="开票信息" name="second">
            <el-row :gutter="20">
              <el-col :span="span">
                <el-form-item label="纳税人识别号" prop="taxNumber">
                  <el-input v-model="form.taxNumber"/>
                </el-form-item>
              </el-col>
              <el-col :span="span">
                <el-form-item label="账户名称" prop="taxBankAccount">
                  <el-input v-model="form.taxBankAccount"/>
                </el-form-item>
              </el-col>
              <el-col :span="span">
                <el-form-item label="开户银行" prop="taxBank">
                  <el-input v-model="form.taxBank"/>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="span">
                <el-form-item label="银行账户" prop="taxBankAccountNumber">
                  <el-input v-model="form.taxBankAccountNumber"/>
                </el-form-item>
              </el-col>
              <el-col :span="span">
                <el-form-item label="联系方式" prop="taxContact">
                  <el-input v-model="form.taxContact"/>
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
        </el-tabs>

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
import {getCustomerOne, saveCustomer, updateCustomer} from '@/api/customer/customer'
import {listCustomerSegment} from '@/api/customer/customer-segment'

const region = ref<string[]>([]) // 用于省市联动选择
import { pcTextArr  } from 'element-china-area-data'

const activeName = ref('first')
const emit = defineEmits(['dialogOfClosedMethods'])
const dialogStatus = ref(false)
const formRef = ref<InstanceType<typeof ElForm> | null>(null)
const {t} = useI18n()

interface FormModel {
  id?: string
  customerName: string
  customerType: string
  segmentId: string
  industry: string
  province?: string
  city?: string
  address: string
  email: string
  phone: string
  website: string
  content?: string
  taxNumber: string
  taxContact: string
  taxBank: string
  taxBankAccount: string
  taxBankAccountNumber: string
  customerHead: string
  customerFrom: string
  wechat: string
}

interface FormState {
  form: FormModel
  rules: Record<string, any>
}

const props = defineProps<{
  title?: string
  open?: boolean
  formId?: string
}>()

interface SegmentModel {
  id?: string
  segmentName: string
}

const data = reactive<FormState>({
  form: {
    customerName: '',
    customerType: '',
    segmentId: '',
    industry: '',
    address: '',
    email: '',
    phone: '',
    website: '',
    taxNumber: '',
    taxContact: '',
    taxBank: '',
    taxBankAccount: '',
    taxBankAccountNumber: '',
    customerHead: '',
    customerFrom: '',
    wechat: ''
  },
  rules: {
    customerName: [
      {required: true, message: "客户名称不能为空", trigger: 'blur'}
    ],
    customerType: [
      {required: true, message: "客户类型不能为空", trigger: 'blur'}
    ],
  }
})

const {form, rules} = toRefs(data)
const dataSegmentList = ref<Array<SegmentModel>>([])
const span = ref(24)

watch(
    () => props.open,
    (val) => {
      if (val) {
        dialogStatus.value = val
        getSegmentList()
        if (props.formId) {
          getCustomerOne(props.formId).then((res: any) => {
            form.value = res.data
            const regionArr: any[] = [];
            if (res.data.province) {
              regionArr.push(res.data.province);
              if (res.data.city) {
                regionArr.push(res.data.city);
              }
            }
            region.value = regionArr;
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

watch(region, (val) => {
  if (Array.isArray(val)) {
    data.form.province = val[0] || '';
    data.form.city = val[1] || '';
  } else {
    data.form.province = '';
    data.form.city = '';
  }
})

function dialogOfClosedMethods(val: any) {
  dialogStatus.value = false
  emit('dialogOfClosedMethods', val)
}

function reset() {
  form.value = {
    customerName: '',
    customerType: '',
    segmentId: '',
    industry: '',
    address: '',
    email: '',
    phone: '',
    website: '',
    taxNumber: '',
    taxContact: '',
    taxBank: '',
    taxBankAccount: '',
    taxBankAccountNumber: '',
    customerHead: '',
    customerFrom: '',
    wechat: ''
  }
  formRef?.value?.resetFields()
}

function getSegmentList() {
  listCustomerSegment({
    pageNumber: 1,
    pageSize: 10000,
  }).then((response: any) => {
    dataSegmentList.value = response.data.records
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
      const operation = props.formId ? updateCustomer : saveCustomer
      const successMessage = props.formId ? t('org.success.update') : t('org.success.add')
      operation(form.value).then((res: any) => handleResponse(res, successMessage))
    }
  })
}
</script>
