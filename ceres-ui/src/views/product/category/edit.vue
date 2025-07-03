<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="750px"
             @close="dialogFormOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px" inline-message>
        <el-row :gutter="20">
          <el-col :span="span">
            <el-form-item label="编码" prop="code">
              <el-input v-model="form.code"  />
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="分类" prop="name">
              <el-input v-model="form.name"  />
            </el-form-item>
          </el-col>
          <el-col :span="span" >
            <el-form-item label="父级" prop="parentId">
                <el-tree-select
                  v-model="form.parentId"
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
      </el-form>
    </template>
    <template #footer>
      <div style="flex: auto">
        <el-button @click="dialogFormOfClosedMethods(false)">{{ t('org.cancel') }}</el-button>
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
import * as productCategoryService  from '@/api/product/product-category'

const emit = defineEmits(['dialogFormOfClosedMethods'])
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
  code?: string
  name?: string
  parentId?: string
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
const span = ref(24)

watch(
    () => props.open,
    (val) => {
      if (val) {
        dialogStatus.value = val
        if (props.formId) {
          productCategoryService.get(props.formId).then((res: any) => {
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



function dialogFormOfClosedMethods(val: any) {
  dialogStatus.value = false
  emit('dialogFormOfClosedMethods', val)
}

function reset() {
  form.value = {
    remark: ''
  }
  formRef?.value?.resetFields()
}

function submitForm() {
  const handleResponse = (res: any, successMessage: string) => {
    if (res.code === 0) {
      modal.msgSuccess(successMessage)
      dialogFormOfClosedMethods(true)
      reset()
    } else {
      modal.msgError(res.message)
    }
  }

  formRef?.value?.validate((valid: any) => {
    if (valid) {
      const operation = props.formId ? productCategoryService.update : productCategoryService.save
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
