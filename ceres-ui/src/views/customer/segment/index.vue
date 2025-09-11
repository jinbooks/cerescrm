<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryRef" :inline="true">
          <el-form-item :label="t('commonName')">
            <el-input v-model="queryParams.segmentName" :placeholder="t('nameTipInput')"/>
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
        <el-button type="primary" @click="handleAdd">{{t('newSegment')}}</el-button>
        <el-button @click="onBatchDelete" :disabled="ids.length === 0">{{t('org.button.deleteBatch')}}</el-button>
      </div>
      <el-table v-loading="loading" :data="dataList" border @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="segmentName" :label="t('segmentName')" align="center">
          <template #default="scope">
            <el-tag type="info" v-if="scope.row.id" effect="plain">
              <text  :style="{color: scope.row.colorCode}">
                {{ scope.row.segmentName }}
              </text>
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" :label="t('jbx.text.description')"  align="left" header-align="center" ></el-table-column>
        <el-table-column prop="colorCode" :label="t('colorFlag')" align="center">
          <template #default="scope">
            <span :style="{ color: scope.row.colorCode, fontWeight: 'bold' }">●</span> {{ scope.row.colorCode }}
          </template>
        </el-table-column>
        <el-table-column :label="t('org.operate')" align="center" width="120">
          <template #default="scope">
            <el-button icon="Edit" link @click="handleUpdate(scope.row)"></el-button>
            <el-button icon="Delete" link @click="handleDelete(scope.row)"></el-button>
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
import {listCustomerSegment, delCustomerSegment} from '@/api/customer/customer-segment'
import modal from '@/plugins/modal'
import editForm from './edit.vue'

const {t} = useI18n()

const data: any = reactive({
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    pageSizeOptions: [10, 20, 50],
    segmentName: ''
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
  queryParams.value.segmentName = ''
  handleQuery()
}

function getList() {
  loading.value = true
  listCustomerSegment(queryParams.value).then((response: any) => {
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
  title.value = t('newCstSeg')
  open.value = true
}

function handleUpdate(row: any) {
  id.value = row.id
  title.value = t('updateCstSeg')
  open.value = true
}

function handleDelete(row: any) {
  const _ids = row.id || ids.value
  modal.confirm(`${t('deleteTipCommon')}${_ids}${t('deleteTipLead1')}`).then(() => {
    return delCustomerSegment({listIds: [_ids]})
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
    return delCustomerSegment({listIds: ids.value})
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
