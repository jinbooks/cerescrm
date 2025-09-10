<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="80px">
          <el-form-item label="客户名称">
            <el-input v-model="queryParams.customerName" placeholder="请输入客户名称"/>
          </el-form-item>
          <el-form-item label="客户类型">
            <el-select style="width: 240px" v-model="queryParams.customerType" clearable placeholder="请选择">
              <el-option label="个人" value="个人"/>
              <el-option label="企业" value="企业"/>
            </el-select>
          </el-form-item>
          <el-form-item :label="t('PersonCharge')">
            <el-input v-model="queryParams.customerHead" placeholder="请输入负责人"/>
          </el-form-item>
          <el-form-item label="客户分组">
            <el-select style="width: 240px" v-model="queryParams.segmentId" clearable placeholder="请选择">
              <el-option v-for="(item, index) in dataSegmentList" :key="index"
                         :value="item.id" :label="item.segmentName"></el-option>
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
        <el-button type="primary" @click="handleAdd">新增客户</el-button>
        <el-button @click="onBatchDelete" :disabled="ids.length === 0">{{t('org.button.deleteBatch')}}</el-button>
      </div>
      <el-table v-loading="loading" :data="dataList" border @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column label="跟进" align="center" width="60"
                         :show-overflow-tooltip="true">
          <template #default="scope">
            <el-button icon="Comment" type="warning" link @click="followUp(scope.row)"></el-button>
          </template>
        </el-table-column>
        <el-table-column prop="customerName" label="客户名称" align="left" header-align="center" ></el-table-column>
        <el-table-column prop="customerType" label="客户类型" align="center"  width="80"></el-table-column>
        <el-table-column prop="segmentName" label="所在分层" align="center"  width="120">
          <template #default="scope">
            <el-tag type="info" v-if="scope.row.segmentId" effect="plain">
              <text  :style="{color: getSegmentColor(scope.row.segmentId)}">
                {{ scope.row.segmentName }}
              </text>
            </el-tag>

          </template>
        </el-table-column>
        <el-table-column prop="industry" label="所属行业" align="center"></el-table-column>
        <el-table-column prop="customerFrom" label="客户来源" align="center"></el-table-column>
        <el-table-column prop="province" label="地区" align="center"
                         :show-overflow-tooltip="true">
          <template #default="scope">{{formatRegion(scope.row.province, scope.row.city)}}</template>
        </el-table-column>
        <el-table-column prop="phone" label="联系电话" align="center" width="120"></el-table-column>
        <el-table-column prop="wechat" label="微信" align="center" width="120"></el-table-column>
        <el-table-column prop="email" label="邮箱" align="center"  width="180"></el-table-column>
        <el-table-column prop="customerHead" :label="t('PersonCharge')" align="center"  width="120"></el-table-column>
        <el-table-column :label="t('org.operate')" align="center" width="140">
          <template #default="scope">
            <el-button icon="View" link @click="handleShow(scope.row)"></el-button>
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

    <!-- 编辑抽屉 -->
    <edit-form
        :title="title"
        :open="open"
        :form-id="id"
        @dialogOfClosedMethods="dialogOfClosedMethods"
    ></edit-form>
    <!-- 跟进-->
    <follow-up-form :relation-id="id" :open="followUpOpen" :title="title"
                    :follow-up-type="2"
                    @dialogOfClosedMethods="dialogOfClosedMethods">
    </follow-up-form>
  </div>
</template>

<script setup lang="ts">
import {getCurrentInstance, reactive, ref, toRefs, computed} from 'vue'
import {useI18n} from 'vue-i18n'
import {listCustomer, delCustomer} from '@/api/customer/customer'
import {listCustomerSegment} from '@/api/customer/customer-segment'
import modal from '@/plugins/modal'
import editForm from './edit.vue'
import {useRouter} from "vue-router";
import FollowUpForm from "@/views/follow-up/edit.vue";
import {formatRegion} from "@/utils/CommonFieldFormat";

const {t} = useI18n()
const router = useRouter()
const data: any = reactive({
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    pageSizeOptions: [10, 20, 50],
    customerName: '',
    customerType: '',
    customerHead: '',
    segmentId: ''
  },
  form: {}
})

interface SegmentModel {
  id: string
  segmentName: string,
  colorCode: string
}

const dataList = ref([])
const dataSegmentList = ref<Array<SegmentModel>>([])
const dataSegmentMap = ref<Map<String, SegmentModel>>(new Map())
const loading = ref(true)
const id = ref<string | undefined>()
const total = ref(0)
const title = ref('')
const open = ref(false)
const followUpOpen = ref(false);
const ids = ref<string[]>([])
const selectionlist = ref<any>([])
const {proxy} = getCurrentInstance()!;
const {queryParams, form} = toRefs(data)
const tableSelectDialog = ref()

function handleQuery() {
  queryParams.value.pageNumber = 1
  getList()
}

function resetQuery() {
  queryParams.value.customerName = ''
  queryParams.value.customerType = ''
  queryParams.value.customerHead = ''
  handleQuery()
}

function getList() {
  loading.value = true
  listCustomer(queryParams.value).then((response: any) => {
    dataList.value = response.data.records
    total.value = response.data.total
    loading.value = false
  })
}

function getSegmentList() {
  listCustomerSegment({
    pageNumber: 1,
    pageSize: 10000,
  }).then((response: any) => {
    dataSegmentList.value = response.data.records
    dataSegmentList.value.forEach((item: SegmentModel) => {
      dataSegmentMap.value.set(item.id, item)
    })
  })
}

function getSegmentColor(id: string) {
  return dataSegmentMap.value.get(id)?.colorCode
}

function dialogOfClosedMethods(val: boolean) {
  console.log("index dialogOfClosedMethods")
  open.value = false
  id.value = undefined
  followUpOpen.value = false;
  if (val) {
    getList()
  }
}

function handleAdd() {
  id.value = undefined
  title.value = '新增客户'
  open.value = true
  getSegmentList()
}

function handleShow(row: any) {
  router.push({path: '/customer/detail', query: {id: row.id}});
}

function handleUpdate(row: any) {
  id.value = row.id
  title.value = '编辑客户'
  open.value = true
  getSegmentList()
}

function handleDelete(row: any) {
  const _ids = row.id || ids.value
  modal.confirm(`是否确认删除客户编号为"${_ids}"的数据项？`).then(() => {
    return delCustomer(_ids)
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
    return delCustomer(ids.value)
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

function followUp(row: any) {
  id.value = row.id;
  title.value = "客户跟进";
  followUpOpen.value = true;
}

getList()
getSegmentList()
</script>
