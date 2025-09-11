<template>
  <div class="table-select-input">
    <el-input :model-value="inputValue" readonly>
      <template #append>
        <el-button @click="handleOpenDialog">{{t('jbx.text.select')}}</el-button>
      </template>
    </el-input>

    <el-dialog
        v-model="visible"
        title="选择产品"
        width="1200px"
        @close="handleClose"
    >
      <div class="app-container">
        <el-card class="common-card query-box">
          <el-form :inline="true" label-width="80px">
            <el-form-item :label="t('productCode')">
              <el-input v-model="queryParams.productCode" placeholder="请输入产品编码"/>
            </el-form-item>
            <el-form-item :label="t('productName')">
              <el-input v-model="queryParams.productName" placeholder="请输入产品名称"/>
            </el-form-item>
            <el-form-item label="分类">
                <el-tree-select
                  v-model="queryParams.categoryId"
                  :data="categorysData"
                  :props="defaultProps"
                  filterable
                  clearable
                  style="width: 200px"  :placeholder="t('pleaseSelect')"
                ></el-tree-select>
            </el-form-item>
            <!-- <el-form-item :label="t('org.status')">
              <el-select style="width: 120px" v-model="queryParams.status" clearable :placeholder="t('pleaseSelect')">
                <el-option :label="t('all')" value=""/>
                <el-option label="正常" value="1"/>
                <el-option label="下线" value="2"/>
              </el-select>
            </el-form-item>-->
            <el-form-item>
               <el-button @click="handleClose">{{ t('org.cancel') }}</el-button>
          <el-button type="primary" @click="handleSubmit">{{ t('org.confirm') }}</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card class="common-card">
          <el-table
              v-loading="loading"
              :data="dataList"
              border
              @selection-change="handleSelectionChange"
          >
            <el-table-column type="selection" width="55" align="center" v-if="multiSelect"></el-table-column>
            <el-table-column width="55" align="center" v-else>
              <template #default="{row}">
                <el-radio v-model="singleSelectId" :value="row.id">{{ '' }}</el-radio>
              </template>
            </el-table-column>
            <el-table-column prop="productCode" :label="t('productCode')" align="center"></el-table-column>
            <el-table-column prop="productName" :label="t('productName')" align="center"></el-table-column>
            <el-table-column prop="version" :label="t('version')" align="center"></el-table-column>
            <el-table-column prop="price" label="价格" align="center"></el-table-column>
            <el-table-column prop="stock" label="库存" align="center"></el-table-column>
            <el-table-column prop="productUnit" label="单位" align="center"></el-table-column>
            <el-table-column prop="releaseDate" label="发布日期" align="center"></el-table-column>
            <el-table-column prop="lifeCycle" label="周期(月)" align="center"></el-table-column>
            <el-table-column prop="endOfLifeDate" label="下线日期" align="center"></el-table-column>
           <el-table-column prop="status" :label="t('org.status')" align="center">
              <template #default="scope">
                <el-tag type="primary" v-show="scope.row.status == '1'">正常</el-tag>
                <el-tag type="success" v-show="scope.row.status == '2'">下线</el-tag>
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
      </div>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleClose">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import {computed, reactive, ref, toRefs, watch} from 'vue'
import * as productService from '@/api/product/product'
import * as productCategoryService  from '@/api/product/product-category'

const props = defineProps({
  modelValue: [String, Array],
  multiSelect: {
    type: Boolean,
    default: false
  }
})

const defaultProps: any = ref({
  value: 'id',
  children: 'children',
  label: 'name'
})

const categorysData = ref<null>()

const emit = defineEmits(['update:modelValue', 'select'])

const data: any = reactive({
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    pageSizeOptions: [10, 20, 50],
    productCode: '',
    productName: '',
    status: ''
  },
  form: {}
})

const dataList = ref<any>([])
const loading = ref(true)
const total = ref(0)
const visible = ref(false)
const singleSelectId = ref<string | undefined>()
const selectedIds = ref<string[]>([])

const {queryParams} = toRefs(data)

// 监听 modelValue 变化并更新内部状态
watch(() => props.modelValue, (newValue) => {
  if (props.multiSelect) {
    selectedIds.value = newValue as string[]
  } else {
    singleSelectId.value = newValue as string
  }
}, {immediate: true})

// 输入框显示值
const inputValue = computed(() => {
  const ids = props.multiSelect ? selectedIds.value : [singleSelectId.value]
  return dataList.value
      .filter((item: any) => ids.includes(item.id))
      .map((item: any) => item.productName)
      .join(',')
})

function handleOpenDialog() {
  visible.value = true
  getList()
}

function handleClose() {
  visible.value = false
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

function getList() {
  loading.value = true
  productService.fetch(queryParams.value).then((response: any) => {
    dataList.value = response.data.records || []
    total.value = response.data.total || 0
    loading.value = false
  })
}

function handleSelectionChange(selection: any[]) {
  if (props.multiSelect) {
    selectedIds.value = selection.map(i => i.id)
  } else {
    singleSelectId.value = selection.length ? selection[0].id : undefined
  }
}

function handleSubmit() {
  console.log("id "+singleSelectId.value+" name "+inputValue.value);
  emit('update:modelValue', props.multiSelect ? selectedIds.value : singleSelectId.value)
  emit('select', singleSelectId.value, inputValue.value)
  handleClose()
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
<style scoped lang="scss">
.table-select-input {
  width: 100%;
}
</style>
