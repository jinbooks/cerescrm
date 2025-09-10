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
            <el-form-item label="产品编码" prop="productCode">
              <el-input v-model="form.productCode"  />
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="产品名称" prop="productName">
              <el-input v-model="form.productName"  />
            </el-form-item>
          </el-col>
          <el-col :span="span" >
            <el-form-item label="版本" prop="version">
              <el-input v-model="form.version"  />
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="价格" prop="price">
              <el-input v-model="form.price"  />
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="库存" prop="stock">
              <el-input v-model="form.stock"  />
            </el-form-item>
          </el-col>
          <el-col :span="span" >
            <el-form-item label="单位" prop="productUnit">
              <el-input v-model="form.productUnit"  />
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="发布日期" prop="releaseDate">
              <el-date-picker v-model="form.releaseDate"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"/>
            </el-form-item>
          </el-col>
                    <el-col :span="span">
            <el-form-item label="周期(月)" prop="lifeCycle">
              <el-input v-model="form.lifeCycle"  />
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="下线日期" prop="endOfLifeDate">
              <el-date-picker v-model="form.endOfLifeDate"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"/>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" :placeholder="t('pleaseSelect')">
                <el-option label="正常" value="1"/>
                <el-option label="下线" value="2"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="span" >
            <el-form-item label="分类" prop="categoryId">
                <el-tree-select
                  v-model="form.categoryId"
                  :data="categorysData"
                  :props="defaultProps"
                  filterable
                  clearable
                  check-strictly="true"
                  style="width: 240px"
                />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="产品介绍">
              <editor v-model="form.content" :height="300"/>
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
import * as productService  from '@/api/product/product'
import * as productCategoryService  from '@/api/product/product-category'

const emit = defineEmits(['dialogOfClosedMethods'])
const dialogStatus = ref(false)
const formRef = ref<InstanceType<typeof ElForm> | null>(null)
const {t} = useI18n()

const defaultProps: any = ref({
  value: 'id',
  children: 'children',
  label: 'name'
})

const categorysData = ref<null>(null)

interface FormModel {
  id?: string
  productCode?: string
  productName?: string
  productUnit?: string
  version?: string
  price?: number
  stock?: number
  releaseDate?: string
  lifeCycle?: number
  endOfLifeDate?: string
  categoryId?: string
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
    lifeCycle:12,
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
          productService.get(props.formId).then((res: any) => {
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
    lifeCycle:12,
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
      const operation = props.formId ? productService.update : productService.save
      const successMessage = props.formId ? t('org.success.update') : t('org.success.add')
      operation(form.value).then((res: any) => handleResponse(res, successMessage))
    }
  })
}

function categorysTree() {
  productCategoryService.tree("").then((res: any) => {
    categorysData.value = res.data
     console.log("categorysData tree "+categorysData.value);
  })
}

categorysTree();
</script>
