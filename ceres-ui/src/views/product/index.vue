<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="80px">
          <el-form-item label="产品编码">
            <el-input v-model="queryParams.productCode" placeholder="请输入产品编码"/>
          </el-form-item>
          <el-form-item label="产品名称">
            <el-input v-model="queryParams.productName" placeholder="请输入产品名称"/>
          </el-form-item>
          <el-form-item label="分类">
                <el-tree-select
                  v-model="queryParams.categoryId"
                  :data="categorysData"
                  :props="defaultProps"
                  filterable
                  clearable
                  check-strictly="true"
                  style="width: 240px"  :placeholder="t('pleaseSelect')"
                ></el-tree-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select style="width: 120px" v-model="queryParams.status" clearable :placeholder="t('pleaseSelect')">
              <el-option :label="t('all')" value=""/>
              <el-option label="正常" value="1"/>
              <el-option label="下线" value="2"/>
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
        <el-button type="primary" @click="handleCategory">分类管理</el-button>
      </div>
      <el-table v-loading="loading" :data="dataList" border @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="productCode" label="产品编码" align="left" header-align="center" ></el-table-column>
        <el-table-column prop="productName" label="产品名称" align="left" header-align="center" ></el-table-column>
        <el-table-column prop="version" label="版本" align="left" header-align="center"  width="100"></el-table-column>
        <el-table-column prop="price" label="价格" align="right" header-align="center" width="120"></el-table-column>
        <el-table-column prop="stock" label="库存" align="right" header-align="center" width="120"></el-table-column>
        <el-table-column prop="productUnit" label="单位" align="center"  width="80"></el-table-column>
        <el-table-column prop="releaseDate" label="发布日期"  align="center" header-align="center" width="100"></el-table-column>
        <el-table-column prop="lifeCycle" label="周期(月)" align="center" width="120"></el-table-column>
        <el-table-column prop="endOfLifeDate" label="下线日期"  align="center" header-align="center" width="100"></el-table-column>
        <el-table-column prop="categoryId" label="产品分类"  align="center" header-align="center" width="160">
          <template #default="scope">
            <el-tree-select
                  v-model="scope.row.categoryId"
                  :data="categorysData"
                  :props="defaultProps"
                  filterable
                  disabled
                  style="width: 140px"
                />
           </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" align="center" width="100">
            <template #default="scope">
                <el-tag type="primary" v-show="scope.row.status == '1'">正常</el-tag>
                <el-tag type="success" v-show="scope.row.status == '2'">下线</el-tag>
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

    <category-list
        :title="title"
        :open="openCategory"
        :form-id="id"
        @dialogOfClosedMethods="dialogOfClosedMethods"
    ></category-list>

  </div>
</template>

<script setup lang="ts">
import {reactive, ref, toRefs} from 'vue'
import {useI18n} from 'vue-i18n'
import * as productService  from '@/api/product/product'
import modal from '@/plugins/modal'
import editForm from './edit.vue'
import categoryList from './category/category.vue'
import * as productCategoryService  from '@/api/product/product-category'

const {t} = useI18n()

const data: any = reactive({
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    pageSizeOptions: [10, 20, 50],
    contactName: '',
    categoryId: '',
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
const openCategory = ref(false)
const ids = ref<string[]>([])
const selectionlist = ref<any>([])

const {queryParams, form} = toRefs(data)

const defaultProps: any = ref({
  value: 'id',
  children: 'children',
  label: 'name'
})

const categorysData = ref<null>()

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
  productService.fetch(queryParams.value).then((response: any) => {
    dataList.value = response.data.records
    total.value = response.data.total
    loading.value = false
  })
}

function dialogOfClosedMethods(val: boolean) {
  console.log("index dialogOfClosedMethods")
  open.value = false
  openCategory.value= false
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

function handleCategory() {
  console.log("index openCategory")
  id.value = undefined
  title.value = '分类管理'
  openCategory.value = true
}

function handleUpdate(row: any) {
  id.value = row.id
  title.value = '编辑'
  open.value = true
}

function handleDelete(row: any) {
  const _ids = row.id || ids.value
  modal.confirm(`${t('deleteTipCommon')}${_ids}${t('deleteTipLead1')}`).then(() => {
    return productService.del([_ids])
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
    return productService.del(ids.value)
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

function categorysTree() {
  productCategoryService.tree("").then((res: any) => {
    categorysData.value = res.data
     console.log("categorysData tree "+categorysData.value);
  })
}

categorysTree();

getList()
</script>
