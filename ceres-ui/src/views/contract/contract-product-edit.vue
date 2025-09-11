<template>
  <el-dialog v-model="dialogFormStatus" :close-on-click-modal="false" size="750px"
             @close="dialogFormOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px" inline-message>
        <el-row :gutter="20">
                    <el-col :span="span">
            <el-form-item :label="t('productInfo')" prop="productId">
              <select-product v-model="form.productId"
                              @select="(ids, names) => selectProductChange(ids,names)"></select-product>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item :label="t('productCode')" prop="productCode" disabled>
              <el-input v-model="form.productCode" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="span" >
            <el-form-item :label="t('version')" prop="productVersion" >
              <el-input v-model="form.productVersion"  disabled/>
            </el-form-item>
          </el-col>
          <el-col :span="span" >
            <el-form-item :label="t('unit')" prop="productUnit" >
              <el-input v-model="form.productUnit"  disabled/>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item :label="t('inStock')" prop="stock">
              <el-input v-model="form.stock"  disabled/>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item :label="t('price')" prop="price" >
              <el-input v-model="form.price" disabled />
            </el-form-item>
          </el-col>

          <el-col :span="span">
            <el-form-item :label="t('PurchaseQuantity')" prop="quantity" >
              <el-input v-model="form.quantity" />
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item :label="t('discount')" prop="discount" >
              <el-input v-model="form.discount" />
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item :label="t('OriginalPrice')" prop="originalPrice" >
              <el-input v-model="form.originalPrice"  />
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item :label="t('DiscountedPrices')" prop="amount" >
              <el-input v-model="form.amount" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </template>
    <template #footer>
      <div style="flex: auto">
        <el-button @click="dialogFormOfClosedMethods(false)">{{ t('org.cancel') }}</el-button>
        <el-button type="warning" @click="calculate">{{t('calculate')}}</el-button>
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
import * as contractProductService  from '@/api/contract/contract-product'
import * as productService  from '@/api/product/product'
import SelectProduct from "../product/select-product/index.vue"

const emit = defineEmits(['dialogFormOfClosedMethods'])
const dialogFormStatus = ref(false)
const formRef = ref<InstanceType<typeof ElForm> | null>(null)
const {t} = useI18n()

interface FormModel {
  id?: string
  contractId?: string
  productId?: string
  productCode?: string
  productName?: string
  productUnit?: string
  productVersion?: string
  price: number
  quantity: number
  originalPrice?: number
  stock?: number
  discount: number
  amount?: number
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
  contractId?: string
}>()

const data = reactive<FormState>({
  form: {
    price :0,
    quantity:0,
    discount:0,
    originalPrice:0.00,
    amount:0.00,
    status: '1',
    jobTitle: '',
    phone: '',
    email: '',
    address: '',
    remark: ''
  },
  rules: {
    contactName: [
      {required: true, message: t('contactTip'), trigger: 'blur'}
    ],
    phone: [
      {required: true, message: t('phoneTipInput'), trigger: 'blur'},
      {
        pattern: /^1[3456789]\d{8}$/,
        message: t('phoneTipRule'),
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
        console.log("watch "+ val)
        console.log("contractId "+ props.contractId)
        dialogFormStatus.value = val
        if (props.formId) {
          contractProductService.get(props.formId).then((res: any) => {
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


function selectProductChange(ids:any, names:any){
  form.value.productName = names
  productService.get(ids).then((res: any) => {
    console.log(res.data);
    form.value.productName=res.data.productName;
    form.value.productCode=res.data.productCode;
    form.value.productId=res.data.id;
    form.value.contractId =props.contractId;
    form.value.productVersion=res.data.version;
    form.value.productUnit=res.data.productUnit;
    form.value.stock=res.data.stock;
    form.value.price=res.data.price;
  })
}

function calculate() {
  form.value.originalPrice = Number.parseFloat((form.value.price * form.value.quantity).toFixed(2))
  form.value.amount = Number.parseFloat((form.value.originalPrice * form.value.discount /100).toFixed(2))
}


function dialogFormOfClosedMethods(val: any) {
  dialogFormStatus.value = false
  emit('dialogFormOfClosedMethods', val)
}

function reset() {
  form.value = {
    price:0,
    quantity:0,
    discount:0,
    originalPrice:0.00,
    amount:0.00,
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
      dialogFormOfClosedMethods(true)
      reset()
    } else {
      modal.msgError(res.message)
    }
  }

  formRef?.value?.validate((valid: any) => {
    if (valid) {
      const operation = props.formId ? contractProductService.update : contractProductService.save
      const successMessage = props.formId ? t('org.success.update') : t('org.success.add')
      operation(form.value).then((res: any) => handleResponse(res, successMessage))
    }
  })
}
</script>
