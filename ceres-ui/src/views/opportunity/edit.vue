<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="45%"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px" inline-message>
        <h4 style="color: dodgerblue">商机属性</h4>
        <el-row :gutter="20">
          <el-col :span="span">
            <el-form-item label="商机名称">
              <el-input v-model="form.name"/>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="商机描述">
              <el-input
                  type="textarea"
                  v-model="form.description"
                  maxlength="100"
                  show-word-limit
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="span">
            <el-form-item label="商机类型">
              <el-select v-model="form.type" clearable>
                <el-option
                    v-for="dict in opportunityTypes"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="来源类型">
              <el-select v-model="form.sourceType" clearable :disabled="!!leadId">
                <el-option
                    v-for="dict in opportunitySourceTypes"
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
            <el-form-item label="客户">
              <customer-select v-model="form.customerId"/>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="联系人">
              <contact-select v-model="form.contactId"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="span">
            <el-form-item label="负责人">
              <owner-select v-model="form.ownerId"/>
            </el-form-item>
          </el-col>
          <el-col :span="span" v-if="form.sourceType === 1">
            <el-form-item label="来源线索">
              <lead-select v-model="form.leadId" :is-convert="!!leadId"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" v-if="!form.leadId">
          <el-col :span="span">
            <el-form-item label="姓名">
              <el-input v-model="form.peopleName"/>
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
              <el-rate v-model="form.qualityScore" :colors="colors"/>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="状态">
              <el-select v-model="form.status" clearable>
                <el-option
                    v-for="dict in opportunityStatus"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <h4 style="color: goldenrod">金额信息</h4>
        <el-row :gutter="20">
          <el-col :span="span">
            <el-form-item label="商机金额">
              <el-input-number :min="0" :precision="2" v-model="form.amount" style="width: 100%">
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="预期金额">
              <el-input-number :min="0" :precision="2" v-model="form.expectedAmount" style="width: 100%">
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="span">
            <el-form-item label="预估成本">
              <el-input-number :min="0" :precision="2" v-model="form.cost" style="width: 100%">
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="成功概率">
              <el-slider v-model="form.probability" placement="bottom"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="span">
            <el-form-item label="加权金额">
              <el-input-number :min="0" :precision="2" :model-value="weightedAmountDisplay" style="width: 100%"
                               disabled>
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="预估利润">
              <el-input-number :precision="2" :model-value="profitDisplay" style="width: 100%" disabled>
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <h4 style="color: darkseagreen">时间信息</h4>
        <el-row :gutter="20">
          <el-col :span="span">
            <el-form-item label="预计成交日期">
              <el-date-picker
                  v-model="form.expectedCloseDate"
                  type="datetime"
                  placeholder="选择时间"
                  format="YYYY-MM-DD HH:mm:ss"
                  value-format="YYYY-MM-DD HH:mm:ss"
                  style="width: calc(100%);"
              />
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="首次接触日期">
              <el-date-picker
                  v-model="form.firstContactDate"
                  type="datetime"
                  placeholder="选择时间"
                  format="YYYY-MM-DD HH:mm:ss"
                  value-format="YYYY-MM-DD HH:mm:ss"
                  style="width: calc(100%);"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <h4 style="color: orangered">竞争信息</h4>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="竞争对手">
              <editor v-model="form.competitors" :height="250"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="span" v-if="form.status === 2">
            <el-form-item label="赢单原因">
              <el-input
                  type="textarea"
                  v-model="form.winReason"
                  maxlength="100"
                  show-word-limit
              />
            </el-form-item>
          </el-col>
          <el-col :span="span" v-if="form.status === 3">
            <el-form-item label="输单原因">
              <el-input
                  type="textarea"
                  v-model="form.loseReason"
                  maxlength="100"
                  show-word-limit
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
import {reactive, ref, toRefs, watch, computed, nextTick} from "vue";
import {useI18n} from "vue-i18n";
import type {LabeledValue, GroupedOptions} from '@/types/commnon';
import modal from "@/plugins/modal";
import {getOne, saveOne, updateOne} from "@/api/opportunity/opportunity";
import {pcTextArr} from 'element-china-area-data'
import Editor from "@/components/Editor/index.vue";
import CustomerSelect from "./select-component/customer.vue"
import ContactSelect from "./select-component/contact.vue"
import OwnerSelect from "@/views/lead/select-component/index.vue"
import LeadSelect from "./select-component/lead.vue"

const emit: any = defineEmits(['dialogOfClosedMethods'])
const dialogStatus: any = ref(false);
const formRef = ref<InstanceType<typeof ElForm> | null>(null);
const {t} = useI18n()

const props = defineProps<{
  title?: string;
  open?: boolean;
  formId?: string;
  leadId?: string;
  opportunityTypes?: LabeledValue[];
  opportunitySourceTypes?: LabeledValue[];
  opportunityStatus?: LabeledValue[];
  priorities?: LabeledValue[];
  booksIndustries?: GroupedOptions[];
}>();

interface FormModel {
  name: string
  description: string
  type: number
  sourceType: number
  sourceDetail: string
  industry: number
  province: string,
  city: string,
  priority: number
  qualityScore: number
  amount: number
  expectedAmount: number
  cost: number
  probability: number
  expectedCloseDate: string
  firstContactDate: string
  status: number
  competitors: string
  winReason: string
  loseReason: string
  customerId: string
  contactId: string
  ownerId: string
  leadId: string
  peopleName: string
  company: string
}


interface FormState {
  form: FormModel
  rules: Record<string, any>
}

const data = reactive<FormState>({
  form: {
    name: '',
    description: '',
    type: 1,
    sourceType: 1,
    sourceDetail: '',
    industry: 1,
    province: '',
    city: '',
    priority: 1,
    qualityScore: 3,
    amount: 0,
    expectedAmount: 0,
    cost: 0,
    probability: 50,
    expectedCloseDate: '',
    firstContactDate: '',
    status: 1,
    competitors: '',
    winReason: '',
    loseReason: '',
    customerId: '',
    contactId: '',
    ownerId: '',
    leadId: '',
    peopleName: '',
    company: ''
  },
  rules: {}
})
const {form, rules} = toRefs(data);
const span = ref(12);
const colors = ref(['#99A9BF', '#F7BA2A', '#FF9900'])
const loading: any = ref(false);
const region = ref<string[]>([]) // 用于省市联动选择
const weightedAmountDisplay = computed(() => {
  const expected = form.value.expectedAmount || 0;
  const probability = form.value.probability || 0;
  return Number((expected * (probability / 100)).toFixed(2));
});

const profitDisplay = computed(() => {
  const expected = form.value.expectedAmount || 0;
  const cost = form.value.cost || 0;
  return Number((expected - cost).toFixed(2));
});


watch(
    () => props.open,
    (val: any) => {
      if (val) {
        dialogStatus.value = props.open;
        if (props.formId) {
          getOne(props.formId).then((res: any) => {
            form.value = res.data.opportunity;
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
        // 线索转化
        if (props.leadId) {
          nextTick(() => {
            form.value.leadId = props.leadId;
          });
        }
      } else {
        reset();
      }
    },
    { immediate: true }
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
    name: '',
    description: '',
    type: 1,
    sourceType: 1,
    sourceDetail: '',
    industry: 1,
    province: '',
    city: '',
    priority: 1,
    qualityScore: 3,
    amount: 0,
    expectedAmount: 0,
    cost: 0,
    probability: 50,
    expectedCloseDate: '',
    firstContactDate: '',
    status: 1,
    competitors: '',
    winReason: '',
    loseReason: '',
    customerId: '',
    contactId: '',
    ownerId: '',
    leadId: '',
    peopleName: '',
    company: ''
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

      // 👇 构造带 isLeadConvert 的对象
      const isLeadConvert = props.leadId != null && props.leadId !== '';
      const payload = {
        ...form.value,
        isLeadConvert
      };
      operation(payload).then((res: any) => handleResponse(res, successMessage));
    }
  });
}


</script>

