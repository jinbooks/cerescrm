<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="50%"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
      <el-card class="common-card query-box">
        <el-form :inline="true" label-width="80px">
          <el-form-item :label="t('jbx.text.id')">
            <el-input v-model="queryParams.code" placeholder="请输入编码"/>
          </el-form-item>
          <el-form-item :label="t('commonName')">
            <el-input v-model="queryParams.name" placeholder="请输入名称"/>
          </el-form-item>
          <el-form-item>
            <el-button @click="handleQuery">{{ t('org.button.query') }}</el-button>
            <el-button @click="resetQuery">{{ t('org.button.reset') }}</el-button>
          </el-form-item>
        </el-form>
      </el-card>

      <el-card class="common-card">
        <div class="btn-form">
          <el-button type="primary" @click="handleAdd">新增</el-button>
        </div>
        <el-table v-loading="loading" :data="dataList" border >
          <el-table-column prop="code" :label="t('jbx.text.id')" align="left"  width="120"></el-table-column>
          <el-table-column prop="name" label="分类" align="left"></el-table-column>
          <!--<el-table-column prop="parentId" label="父级" align="center"></el-table-column>-->
          <el-table-column :label="t('org.operate')" align="center" width="120">
          <template #default="scope">
            <el-button icon="Edit" link @click="handleUpdate(scope.row)"></el-button>
            <el-button icon="Delete" link type="danger" @click="handleDelete(scope.row)"></el-button>
          </template>
        </el-table-column>
        </el-table>
        <pagination
            v-show="total > 0"
            :total="total"
            v-model:page="queryParams.pageNumber"
            v-model:limit="queryParams.pageSize"
            :page-sizes="queryParams.pageSizeOptions"
            @pagination="getList"
        />
      </el-card>

      <edit-form
        :title="title"
        :open="openProductCategory"
        :form-id="id"
        :contract-Id="contractId"
        @dialogFormOfClosedMethods="dialogFormOfClosedMethods"
    ></edit-form>

    </template>
    <template #footer>
      <div style="flex: auto">
        <el-button @click="dialogOfClosedMethods(false)">{{ t('org.cancel') }}</el-button>
      </div>
    </template>
  </el-drawer>
</template>

<script setup lang="ts">
import {reactive, ref, toRefs, watch} from 'vue'
import {useI18n} from 'vue-i18n'
import * as productCategoryService  from '@/api/product/product-category'
import modal from '@/plugins/modal'
import editForm from './edit.vue'

const emit = defineEmits(['dialogOfClosedMethods'])
const {t} = useI18n()

const data: any = reactive({
  queryParams: {
    contractId:"",
    pageNumber: 1,
    pageSize: 10,
    pageSizeOptions: [10, 20, 50],
    contactName: '',
    gender: '',
    phone: ''
  },
  form: {}
})
const dialogStatus = ref(false)
const dataList = ref([])
const loading = ref(true)
const id = ref<string | undefined>()
const contractId = ref<string | undefined>()
const total = ref(0)
const title = ref('')
const openProductCategory = ref(false)
let tableSummary: any = ref([]);
const {queryParams, form} = toRefs(data)

const props = defineProps<{
  title?: string
  open?: boolean
  formId?: string
  contractId?: string
}>()


watch(
    () => props.open,
    (val) => {
      console.log("open contractId " +props.formId)
      if(props.open){
        tableSummary = ref([]);
        title.value = props.title ||''
        queryParams.value.contractId = props.formId
        contractId.value = props.formId
        dialogStatus.value= true;
        getList()
      }
    },
    {immediate: true}
)

function getList() {
  loading.value = true
  productCategoryService.fetch(queryParams.value).then((response: any) => {
    dataList.value = response.data.records
    total.value = response.data.total
    loading.value = false
  })
}

function handleAdd() {
  id.value = undefined
  title.value = '新增'
  openProductCategory.value = true
}

function handleQuery() {
  queryParams.value.pageNumber = 1
  getList()
}

function resetQuery() {
  queryParams.value.productCode = ''
  queryParams.value.productName = ''
  queryParams.value.status = ''
  handleQuery()
}

function handleUpdate(row: any) {
  openProductCategory.value = true
  id.value = row.id
  title.value = t('jbx.text.edit')
  console.log("handleUpdate " +row.id);
}

function handleDelete(row: any) {
  const _ids = row.id
  modal.confirm(`${t('deleteTipCommon')}${_ids}${t('deleteTipLead1')}`).then(() => {
    return productCategoryService.del([_ids])
  }).then((res: any) => {
    if (res.code === 0) {
      getList()
      modal.msgSuccess(t('jbx.alert.delete.success'))
    } else {
      modal.msgError(res.message)
    }
  }).catch(() => {
  })
}

function dialogOfClosedMethods(val: boolean) {
  //dialogStatus.value= false
  id.value = undefined
  emit('dialogOfClosedMethods', val)
}

function dialogFormOfClosedMethods(val: boolean) {
  console.log("dialogFormOfClosedMethods");
  openProductCategory.value = false
  id.value = undefined
  getList()
}

</script>
