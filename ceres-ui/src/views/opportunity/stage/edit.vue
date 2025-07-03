<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="40%"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px" inline-message>
        <el-form-item label="阶段名称">
          <el-input v-model="form.name"/>
        </el-form-item>
        <el-form-item label="阶段编码">
          <el-input v-model="form.code"/>
        </el-form-item>
        <el-form-item label="成功概率">
          <el-slider v-model="form.probability" placement="bottom"/>
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sortOrder" controls-position="right" :min="0" />
        </el-form-item>
        <el-form-item label="阶段颜色">
          <div class="color-picker">
            <div
                v-for="color in colors"
                :key="color"
                class="color-circle"
                :style="{ backgroundColor: color, border: form.color === color ? '2px solid black' : '2px solid #ccc' }"
                @click="form.color = color"
            ></div>
          </div>
        </el-form-item>
        <el-form-item label="阶段描述">
          <el-input
              type="textarea"
              v-model="form.description"
              maxlength="100"
              show-word-limit
          />
        </el-form-item>
        <el-form-item prop="status" :label="$t('jbx.text.status.status')">
          <el-switch
              :width="44"
              v-model="form.isActive"
              :active-value="0"
              :inactive-value="1"
             >
          </el-switch>
        </el-form-item>
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
import {ElForm} from "element-plus";
import {reactive, ref, toRefs, watch, computed} from "vue";
import {useI18n} from "vue-i18n";
import modal from "@/plugins/modal";
import {getOne, saveOne, updateOne} from "@/api/opportunity/oppStage";

const emit: any = defineEmits(['dialogOfClosedMethods'])
const dialogStatus: any = ref(false);
const formRef = ref<InstanceType<typeof ElForm> | null>(null);
const {t} = useI18n()

const props = defineProps<{
  title?: string;
  open?: boolean;
  formId?: string;
}>();


interface FormModel {
  name: string
  code: string
  probability: number
  sortOrder: number
  color: string
  isActive: number
  description: string
}


interface FormState {
  form: FormModel
  rules: Record<string, any>
}

const data = reactive<FormState>({
  form: {
    name: '',
    code: '',
    probability: 50,
    sortOrder: 1,
    color: '#409EFF',
    isActive: 0,
    description: ''
  },
  rules: {}
})
const {form, rules} = toRefs(data);
const span = ref(12);
// 可选颜色列表（颜色码字符串数组）
const colors: string[] = [
  '#409EFF', // 蓝色
  '#9B59B6', // 紫色
  '#F39C12', // 橙色
  '#E74C3C', // 红色
  '#2ECC71', // 绿色
  '#7F8C8D', // 灰色
  '#E91E63', // 粉色
  '#1ABC9C',  // 青色
]
const loading: any = ref(false);

watch(
    () => props.open,
    (val: any) => {
      if (val) {
        dialogStatus.value = props.open;
        if (props.formId) {
          getOne(props.formId).then((res: any) => {
            form.value = res.data;
          });
        } else {
          reset();
        }
      } else {
        reset();
      }
    },
    {immediate: true}
);

function dialogOfClosedMethods(val: any): any {
  dialogStatus.value = false;
  emit('dialogOfClosedMethods', val);
}

function reset() {
  form.value = {
    name: '',
    code: '',
    probability: 50,
    sortOrder: 1,
    color: '#409EFF',
    isActive: 0,
    description: ''
  }
  formRef?.value?.resetFields();
}

function submitForm() {
  const handleResponse: any = (res: any, successMessage: any) => {
    if (res.code === 0) {
      modal.msgSuccess(successMessage);
      dialogOfClosedMethods(true);
      reset();
    } else {
      modal.msgError(res.message);
    }
    loading.value = false;
  };

  formRef?.value?.validate((valid: any) => {
    if (valid) {
      loading.value = true;
      const operation: any = props.formId ? updateOne : saveOne;
      const successMessage: any = props.formId
          ? t('org.success.update')
          : t('org.success.add');
      operation(form.value).then((res: any) => handleResponse(res, successMessage));
    }
  });
}


</script>

<style scoped lang="scss">
.color-picker {
  display: flex;
  gap: 10px;
  align-items: center;
}

.color-circle {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  cursor: pointer;
  transition: border 0.2s;
}
</style>

