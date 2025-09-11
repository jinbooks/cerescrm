<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="80px">
          <el-form-item :label="t('customerName')">
            <el-input v-model="queryParams.customerName" :placeholder="t('customerTip')"/>
          </el-form-item>
          <el-form-item label="产品编码">
            <el-input v-model="queryParams.productCode" placeholder="请输入产品编码"/>
          </el-form-item>
          <el-form-item label="产品名称">
            <el-input v-model="queryParams.productName" placeholder="请输入产品名称"/>
          </el-form-item>
          <el-form-item label="状态">
            <el-select style="width: 120px" v-model="queryParams.status" clearable :placeholder="t('pleaseSelect')">
              <el-option label="全部" value=""/>
              <el-option label="生效" value="1"/>
              <el-option label="失效" value="2"/>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button @click="handleQuery">{{ t('org.button.query') }}</el-button>
            <el-button @click="resetQuery">{{ t('org.button.reset') }}</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <el-card class="common-card">
      <div class="btn-form">
        <el-button type="primary" @click="handleAdd">新增</el-button>
        <el-button @click="onBatchDelete" :disabled="ids.length === 0">{{t('org.button.deleteBatch')}}</el-button>
      </div>
      <el-table v-loading="loading" :data="dataList" border @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <!--<el-table-column prop="customerId" label="客户编码"  align="left" header-align="center" ></el-table-column>-->
        <el-table-column prop="customerName" :label="t('customerName')"  align="left" header-align="center" ></el-table-column>
        <el-table-column prop="productCode" label="产品编码" align="left" header-align="center"></el-table-column>
        <el-table-column prop="productName" label="产品名称" align="left" header-align="center"></el-table-column>
        <el-table-column prop="productVersion" label="版本" align="left" header-align="center"></el-table-column>
        <el-table-column prop="license" label="授权码" align="left" header-align="center"></el-table-column>
        <el-table-column prop="grantType" label="授权方式"  align="center" header-align="center" width="100"></el-table-column>
        <el-table-column prop="grantDate" label="授权日期"  align="center" header-align="center" width="100" ></el-table-column>
        <el-table-column prop="expirationDate" :label="t('ExpiryDate')" align="center"></el-table-column>
        <el-table-column prop="status" label="状态" align="center"  width="100">
            <template #default="scope">
              <el-tag type="primary" v-show="scope.row.status=='1'">生效</el-tag>
              <el-tag type="success" v-show="scope.row.status=='2'">失效</el-tag>
              </template>
          </el-table-column>
        <el-table-column :label="t('org.operate')" align="center" width="120">
          <template #default="scope">
            <el-button link icon="Edit" @click="handleUpdate(scope.row)"></el-button>
            <el-button link icon="Delete" type="danger" @click="handleDelete(scope.row)"></el-button>
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
        :open="open"
        :form-id="id"
        @dialogOfClosedMethods="dialogOfClosedMethods"
    ></edit-form>
  </div>
</template>

<script setup lang="ts">
import {reactive, ref, toRefs} from 'vue'
import {useI18n} from 'vue-i18n'
import * as productGrantService  from '@/api/product-grant/product-grant'
import modal from '@/plugins/modal'
import editForm from './edit.vue'

const {t} = useI18n()

const data: any = reactive({
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    pageSizeOptions: [10, 20, 50],
    contactName: '',
    gender: '',
    phone: ''
  },
  form: {}
})
const dataList = ref([])
const loading = ref(true)
const id = ref<string | undefined>()
const total = ref(0)
const title = ref('')
const open = ref(false)
const ids = ref<string[]>([])
const selectionlist = ref<any>([])

const {queryParams, form} = toRefs(data)

function handleQuery() {
  queryParams.value.pageNumber = 1
  getList()
}

function resetQuery() {
  queryParams.value.contactName = ''
  queryParams.value.gender = ''
  queryParams.value.phone = ''
  handleQuery()
}

function getList() {
  loading.value = true
  productGrantService.fetch(queryParams.value).then((response: any) => {
    dataList.value = response.data.records
    total.value = response.data.total
    loading.value = false
  })
}

function dialogOfClosedMethods(val: boolean) {
  open.value = false
  id.value = undefined
  if (val) {
    getList()
  }
}

function handleAdd() {
  id.value = undefined
  title.value = '新增'
  open.value = true
}

function handleUpdate(row: any) {
  id.value = row.id
  title.value = '编辑'
  open.value = true
}

function handleDelete(row: any) {
  const _ids = row.id || ids.value
  modal.confirm(`${t('deleteTipCommon')}${_ids}${t('deleteTipLead1')}`).then(() => {
    return productGrantService.del([_ids])
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

function onBatchDelete() {
  modal.confirm(t('jbx.confirm.text.delete')).then(() => {
    return productGrantService.del(ids.value)
  }).then((res: any) => {
    if (res.code === 0) {
      handleQuery()
      modal.msgSuccess(t('jbx.alert.delete.success'))
    } else {
      modal.msgError(res.message)
    }
  }).catch(() => {
  })
}

function handleSelectionChange(selection: any) {
  selectionlist.value = selection
  ids.value = selectionlist.value.map((item: any) => item.id)
}

getList()
</script>
