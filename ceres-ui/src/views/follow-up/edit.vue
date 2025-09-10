<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="40%"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px" inline-message>
        <el-form-item label="业务类别">
          <el-select v-model="form.category" disabled>
            <el-option
                v-for="dict in categories"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="跟进方式">
          <el-select v-model="form.followType">
            <el-option
                v-for="dict in follow_types"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="跟进标题">
          <el-input v-model="form.title"/>
        </el-form-item>
        <el-form-item label="跟进内容">
          <el-input
              type="textarea"
              v-model="form.content"
              maxlength="200"
              show-word-limit
          />
        </el-form-item>
        <el-form-item label="跟进结果">
          <el-select v-model="form.result">
            <el-option
                v-for="dict in results"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="跟进时间">
          <el-date-picker
              v-model="form.followAt"
              type="datetime"
              placeholder="选择时间"
              format="YYYY-MM-DD HH:mm:ss"
              value-format="YYYY-MM-DD HH:mm:ss"
              style="width: calc(100%);"
          />
        </el-form-item>
        <el-form-item label="下次跟进时间">
          <el-date-picker
              v-model="form.nextFollowTime"
              type="datetime"
              placeholder="选择时间"
              format="YYYY-MM-DD HH:mm:ss"
              value-format="YYYY-MM-DD HH:mm:ss"
              style="width: calc(100%);"
          />
        </el-form-item>
        <el-form-item label="下次提醒时间">
          <el-date-picker
              v-model="form.remindTime"
              type="datetime"
              placeholder="选择时间"
              format="YYYY-MM-DD HH:mm:ss"
              value-format="YYYY-MM-DD HH:mm:ss"
              style="width: calc(100%);"
          />
        </el-form-item>
        <el-form-item label="跟进人">
          <follow-user-select v-model="form.followUserId"/>
        </el-form-item>
        <el-form-item label="备注">
          <el-input
              type="textarea"
              v-model="form.remark"
              maxlength="100"
              show-word-limit
          />
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
import {getCurrentInstance, nextTick, reactive, ref, toRefs, watch} from "vue";
import {useI18n} from "vue-i18n";
import modal from "@/plugins/modal";
import {saveOne} from "@/api/followup/follopup";
import {pcTextArr} from 'element-china-area-data'
import FollowUserSelect from "@/views/follow-up/select-component/index.vue";

const emit: any = defineEmits(['dialogOfClosedMethods'])
const dialogStatus: any = ref(false);
const formRef = ref<InstanceType<typeof ElForm> | null>(null);
const {t} = useI18n()

const {proxy} = getCurrentInstance()!;
const {categories, follow_types, results}
    = proxy?.useDict("categories", "follow_types", "results");

const props = defineProps<{
  title?: string;
  open?: boolean;
  relationId?: string;
  followUpType?: number;
  followUserId?: string;
}>();

interface FormModel {
  category: number,
  followType: number,
  title: string,
  content: string,
  result: number,
  followAt: string,
  nextFollowTime: string,
  remindTime: string,
  followUserId: string,
  remark: string,
}


interface FormState {
  form: FormModel
  rules: Record<string, any>
}

const data = reactive<FormState>({
  form: {
    category: 1,
    followType: 1,
    title: "",
    content: "",
    result: 1,
    followAt: "",
    nextFollowTime: "",
    remindTime: "",
    followUserId: "",
    remark: "",
  },
  rules: {}
})
const {form, rules} = toRefs(data);
const loading: any = ref(false);


watch(
    () => props.open,
    (val: any) => {
      if (val) {
        dialogStatus.value = props.open;
        nextTick(() => {
          if (props.followUserId) {
            form.value.followUserId = props.followUserId;
          }
          form.value.category = props.followUpType as number ;
        });
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
    category: 1,
    followType: 1,
    title: "",
    content: "",
    result: 1,
    followAt: "",
    nextFollowTime: "",
    remindTime: "",
    followUserId: "",
    remark: "",
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
      const data = {
        ...form.value,
        relationId: props.relationId
      }
      const operation: any = saveOne;
      const successMessage: any = t('org.success.add');
      operation(data).then((res: any) => handleResponse(res, successMessage));
    }
  });
}
</script>
