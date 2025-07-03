<template>
  <!-- 替换 el-drawer 为 el-dialog -->
  <el-dialog v-model="dialogStatus" title="客户分层管理" :width="600"
             style="top: 150px"
             :close-on-click-modal="false"
             @close="dialogOfClosedMethods(false)">
    <template #default>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px" inline-message>
        <div class="form-container">
          <el-row :gutter="20" class="form-row">
            <el-col :span="24">
              <el-form-item label="分层名称" prop="segmentName">
                <el-input v-model="form.segmentName" placeholder="请输入分层名称"/>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20" class="form-row">
            <el-col :span="24">
              <el-form-item label="颜色代码" prop="colorCode">
                <el-color-picker v-model="form.colorCode" show-alpha color-format="hex"/>
                <span class="color-code-text">{{ form.colorCode }}</span>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20" class="form-row">
            <el-col :span="24">
              <el-form-item label="描述信息" prop="description">
                <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入描述信息"/>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20" class="form-row">
            <el-col :span="24">
              <el-form-item label="分层规则" prop="ruleCondition">
                <el-input
                    v-model="form.ruleCondition"
                    type="textarea"
                    :rows="4"
                    placeholder="输入SQL表达式，如：totalSpending > 10000 AND serviceSatisfaction >= 4"
                    class="rule-textarea"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </div>
      </el-form>
    </template>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogOfClosedMethods(false)">{{ t('org.cancel') }}</el-button>
        <el-button type="primary" @click="submitForm">{{ t('org.confirm') }}</el-button>
      </div>
    </template>
  </el-dialog>
</template>
<script setup lang="ts">
import {ElForm} from 'element-plus'
import {reactive, ref, toRefs, watch} from 'vue'
import {useI18n} from 'vue-i18n'
import modal from '@/plugins/modal'
import {getCustomerSegmentOne, saveCustomerSegment, updateCustomerSegment} from '@/api/customer/customer-segment'

const emit = defineEmits(['dialogOfClosedMethods'])
const dialogStatus = ref(false)
const formRef = ref<InstanceType<typeof ElForm> | null>(null)
const {t} = useI18n()

interface FormModel {
  id?: string
  segmentName: string
  description: string
  ruleCondition: string
  colorCode: string
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

const data = reactive<FormState>({
  form: {
    segmentName: '',
    description: '',
    ruleCondition: '',
    colorCode: '#FFFFFF'
  },
  rules: {
    segmentName: [
      {required: true, message: "名称不能为空", trigger: 'blur'}
    ],
  }
})

const {form, rules} = toRefs(data)
const span = ref(24)

watch(
    () => props.open,
    (val) => {
      if (val) {
        dialogStatus.value = val
        if (props.formId) {
          getCustomerSegmentOne(props.formId).then((res: any) => {
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
    segmentName: '',
    description: '',
    ruleCondition: '',
    colorCode: '#FFFFFF'
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
      const operation = props.formId ? updateCustomerSegment : saveCustomerSegment
      const successMessage = props.formId ? t('org.success.update') : t('org.success.add')
      operation(form.value).then((res: any) => handleResponse(res, successMessage))
    }
  })
}
</script>
