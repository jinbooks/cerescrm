<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="45%"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px" inline-message>
        <h4 style="color: dodgerblue">基本信息</h4>
        <el-row :gutter="20">
          <el-col :span="span">
            <el-form-item label="姓名">
              <el-input v-model="form.name"/>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="公司">
              <el-input v-model="form.company"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="span">
            <el-form-item label="职位">
              <el-input v-model="form.title"/>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="手机号">
              <el-input v-model="form.phone"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="span">
            <el-form-item label="邮箱">
              <el-input v-model="form.email"/>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="微信号">
              <el-input v-model="form.wechat"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
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
            <el-form-item label="负责人">
              <owner-select v-model="form.ownerId"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="线索需求">
              <editor v-model="form.demand" :height="250"/>
            </el-form-item>
          </el-col>
        </el-row>
        <h4 style="color: orangered">线索属性</h4>
        <el-row :gutter="20">
          <el-col :span="span">
            <el-form-item label="来源类型">
              <el-select v-model="form.sourceType" clearable>
                <el-option
                    v-for="dict in sourceTypes"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="来源详情">
              <el-input
                  type="textarea"
                  v-model="form.sourceDetail"
                  maxlength="100"
                  show-word-limit
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="span">
            <el-form-item label="行业">
              <el-select v-model="form.industry" clearable filterable>
                <el-option-group
                    v-for="group in booksIndustries"
                    :key="group.label"
                    :label="group.label"
                >
                  <el-option
                      v-for="item in group.options"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                  />
                </el-option-group>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="预算">
              <el-input-number :min="0" :precision="2" v-model="form.budget" style="width: 100%">
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <h4 style="color: darkseagreen">状态管理</h4>
        <el-row :gutter="20">
          <el-col :span="span">
            <el-form-item label="状态">
              <el-select v-model="form.status" clearable>
                <el-option
                    v-for="dict in statusManages"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="优先级">
              <el-select v-model="form.priority" clearable>
                <el-option
                    v-for="dict in priorities"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="span">
            <el-form-item label="质量评分">
              <el-rate v-model="form.qualityScore" :colors="colors" />
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="最后联系时间">
              <el-date-picker
                  v-model="form.lastContactTime"
                  type="datetime"
                  placeholder="选择时间"
                  format="YYYY-MM-DD HH:mm:ss"
                  value-format="YYYY-MM-DD HH:mm:ss"
                  style="width: calc(100%);"
              />
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
import {ElForm} from "element-plus";
import {reactive, ref, toRefs, watch} from "vue";
import {useI18n} from "vue-i18n";
import type { LabeledValue, GroupedOptions } from '@/types/commnon';
import modal from "@/plugins/modal";
import {getOne, saveOne, updateOne} from "@/api/lead/lead";
import { pcTextArr  } from 'element-china-area-data'
import OwnerSelect from "./select-component/index.vue";
import Editor from "@/components/Editor/index.vue";

const emit: any = defineEmits(['dialogOfClosedMethods'])
const dialogStatus: any = ref(false);
const formRef = ref<InstanceType<typeof ElForm> | null>(null);
const {t} = useI18n()

const props = defineProps<{
  title?: string;
  open?: boolean;
  formId?: string;
  sourceTypes?: LabeledValue[];
  booksIndustries?: GroupedOptions[];
  statusManages?: LabeledValue[];
  priorities?: LabeledValue[];
}>();

interface FormModel {
  leadCode: string,           //线索编码
  name: string                // 姓名
  company: string             // 公司
  title: string               // 职位
  phone: string               // 手机号
  email: string               // 邮箱
  wechat: string              // 微信号
  sourceType: number          // 来源类型
  sourceDetail: string        // 来源详情
  industry: number            // 行业
  budget: number              // 预算
  status: number              // 状态
  priority: number            // 优先级
  qualityScore: number        // 质量评分（星级）
  nextFollowTime: string      // 下次跟进时间
  lastContactTime: string     // 最后联系时间
  province: string,
  city: string,
  followUpTimes: number
  ownerId: string
  demand: string
}


interface FormState {
  form: FormModel
  rules: Record<string, any>
}

const data = reactive<FormState>({
  form: {
    leadCode: '',
    name: '',
    company: '',
    title: '',
    phone: '',
    email: '',
    wechat: '',
    sourceType: 1,
    sourceDetail: '',
    industry: 1,
    budget: 0,
    status: 1,
    priority: 1,
    qualityScore: 3,
    nextFollowTime: '',
    lastContactTime: '',
    province: '',
    city: '',
    followUpTimes: 0,
    ownerId: '',
    demand: ''
  },
  rules: {}
})
const {form, rules} = toRefs(data);
const span = ref(12);
const colors = ref(['#99A9BF', '#F7BA2A', '#FF9900'])
const loading: any = ref(false);
const region = ref<string[]>([]) // 用于省市联动选择


watch(
    () => props.open,
    (val: any) => {
      if (val) {
        dialogStatus.value = props.open;
        if (props.formId) {
          reset();
          getOne(props.formId).then((res: any) => {
            form.value = res.data.lead;
            const regionArr: any[] = [];
            if (form.value.province) {
              regionArr.push(form.value.province);
              if (form.value.city) {
                regionArr.push(form.value.city);
              }
            }
            region.value = regionArr;
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

watch(region, (val) => {
  data.form.province = val[0] || ''
  data.form.city = val[1] || ''
})


function dialogOfClosedMethods(val: any): any {
  dialogStatus.value = false;
  emit('dialogOfClosedMethods', val);
}

function reset() {
  form.value = {
    leadCode: '',
    name: '',
    company: '',
    title: '',
    phone: '',
    email: '',
    wechat: '',
    sourceType: 1,
    sourceDetail: '',
    industry: 1,
    budget: 0,
    status: 1,
    priority: 1,
    qualityScore: 3,
    nextFollowTime: '',
    lastContactTime: '',
    province: '',
    city: '',
    followUpTimes: 0,
    ownerId: '',
    demand: ''
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

function updateTime(field: 'nextFollowTime' | 'lastContactTime') {
  const now = new Date()
  const pad = (n: number) => String(n).padStart(2, '0')
  const format = (d: Date) =>
      `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`

  data.form[field] = format(now)
}

</script>
