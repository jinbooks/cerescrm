<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="80px">
          <el-form-item label="联系人">
            <el-input v-model="queryParams.contactName" placeholder="请输入联系人姓名"/>
          </el-form-item>
          <el-form-item label="性别">
            <el-select style="width: 120px" v-model="queryParams.gender" clearable placeholder="请选择">
              <el-option label="男" value="男"/>
              <el-option label="女" value="女"/>
              <el-option label="未知" value="未知"/>
            </el-select>
          </el-form-item>
          <el-form-item label="联系电话">
            <el-input v-model="queryParams.phone" placeholder="请输入联系电话"/>
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
        <el-button type="primary" @click="handleAdd">新增联系人</el-button>
        <el-button @click="onBatchDelete" :disabled="ids.length === 0">批量删除</el-button>
      </div>
      <el-table v-loading="loading" :data="dataList" border @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="customerName" label="客户名称" align="left" header-align="center"></el-table-column>
        <el-table-column prop="contactName" label="联系人姓名" align="left" header-align="center"></el-table-column>
        <el-table-column prop="gender" label="性别" align="center"></el-table-column>
        <el-table-column prop="jobTitle" label="职位" align="left" header-align="center"></el-table-column>
        <el-table-column prop="phone" label="联系电话" align="left" header-align="center"></el-table-column>
        <el-table-column prop="email" label="邮箱" align="left" header-align="center"></el-table-column>
        <el-table-column label="操作" align="center" width="120">
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
import {listCustomerContact, delCustomerContact} from '@/api/customer/people'
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
  listCustomerContact(queryParams.value).then((response: any) => {
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
  title.value = '新增联系人'
  open.value = true
}

function handleUpdate(row: any) {
  id.value = row.id
  title.value = '编辑联系人'
  open.value = true
}

function handleDelete(row: any) {
  const _ids = row.id || ids.value
  modal.confirm(`是否确认删除编号为"${_ids}"的数据项？`).then(() => {
    return delCustomerContact( [_ids])
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
    return delCustomerContact( ids.value)
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
