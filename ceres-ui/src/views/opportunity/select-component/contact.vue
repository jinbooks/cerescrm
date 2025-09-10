<template>
  <div class="table-select-input">
    <el-input :model-value="inputValue" readonly>
      <template #append>
        <el-button @click="handleOpenDialog">{{t('jbx.text.select')}}</el-button>
      </template>
    </el-input>

    <el-dialog
        v-model="visible"
        :title="t('selectContact')"
        width="1200px"
        @close="handleClose">
      <div class="app-container">
        <el-card class="common-card">
          <el-form :inline="true" label-width="80px">
            <el-form-item :label="t('contact')">
              <el-input v-model="queryParams.contactName" :placeholder="t('contactTip')"/>
            </el-form-item>
            <el-form-item :label="t('jbx.users.gender')">
              <el-select style="width: 120px" v-model="queryParams.gender" clearable :placeholder="t('pleaseSelect')">
                <el-option :label="t('jbx.users.genderMale')" value="男"/>
                <el-option :label="t('jbx.users.genderFemale')" value="女"/>
                <el-option :label="t('jbx.users.marriedUnknown')" value="未知"/>
              </el-select>
            </el-form-item>
            <el-form-item :label="t('jbx.organizations.phone')">
              <el-input v-model="queryParams.phone" :placeholder="t('phoneTip')"/>
            </el-form-item>
            <el-form-item>
              <el-button @click="handleQuery">{{ t('org.button.query') }}</el-button>
              <el-button @click="resetQuery">{{ t('org.button.reset') }}</el-button>
            </el-form-item>
          </el-form>
        </el-card>
        <el-card class="common-card">
          <el-table v-loading="loading" :data="dataList" border @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" v-if="multiSelect"></el-table-column>
            <!-- 单选 -->
            <el-table-column width="45" align="center" :show-overflow-tooltip="true" v-else>
              <template #default="{row}">
                <el-radio v-model="tempSingleSelectId" :value="row.id"></el-radio>
              </template>
            </el-table-column>
            <el-table-column prop="customerName" :label="t('customerName')" align="center"></el-table-column>
            <el-table-column prop="contactName" :label="t('ContactName')" align="center"></el-table-column>
            <el-table-column prop="gender" :label="t('jbx.users.gender')" align="center"></el-table-column>
            <el-table-column prop="jobTitle" :label="t('jbx.users.jobTitle')" align="center"></el-table-column>
            <el-table-column prop="phone" :label="t('jbx.organizations.phone')" align="center"></el-table-column>
            <el-table-column prop="email" :label="t('jbx.institutions.email')" align="center"></el-table-column>
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
          <el-button @click="handleClose">{{ t('org.cancel') }}</el-button>
          <el-button type="primary" @click="handleSubmit">{{ t('org.confirm') }}</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import {computed, getCurrentInstance, reactive, ref, toRefs, watch} from 'vue'
import {useI18n} from 'vue-i18n'
import {listCustomerContact} from "@/api/customer/people";

const {t} = useI18n()

const props = defineProps({
  modelValue: [String, Array],
  multiSelect: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:modelValue', 'select'])

const data: any = reactive({
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    pageSizeOptions: [10, 20, 50]
  }
})


const dataList = ref<any>([])
const loading = ref(true)
const total = ref(0)
const visible = ref(false)
const singleSelectId = ref<string | undefined>()
const selectedIds = ref<string[]>([])
const {proxy} = getCurrentInstance()!;
const {queryParams} = toRefs(data)
const tempSelectedIds = ref<string[]>([])
const tempSingleSelectId = ref<string | undefined>()

// 监听 modelValue 的变化并更新 inputValue
watch(() => props.modelValue, (newValue) => {
  if (newValue !== inputValue.value) {
    if (props.multiSelect) {
      selectedIds.value = newValue as string[]
    } else {
      singleSelectId.value = newValue as string
    }
  }
})

// 输入框的值，与 modelValue 同步
const inputValue = computed(() => {
  const ids = props.multiSelect ? selectedIds.value : [singleSelectId.value]
  return dataList.value.filter((item: any) => {
    return ids.includes(item.id)
  }).map((item: any) => item.contactName).join(',')
})

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

function handleClose() {
  visible.value = false
}

function handleSubmit() {
  // 把临时值同步到真正的选中值中
  if (props.multiSelect) {
    selectedIds.value = [...tempSelectedIds.value]
  } else {
    singleSelectId.value = tempSingleSelectId.value
  }

  const selectedValue: string | string[] = props.multiSelect
      ? selectedIds.value
      : singleSelectId.value || ''

  emit('update:modelValue', selectedValue)
  emit('select', selectedValue, inputValue.value)
  handleClose()
}

function handleSelectionChange(selection: any) {
  tempSelectedIds.value = selection.map((item: any) => item.id)
}

// 处理打开对话框
function handleOpenDialog() {
  visible.value = true
  // 复制当前已选项到临时变量
  tempSelectedIds.value = [...selectedIds.value]
  tempSingleSelectId.value = singleSelectId.value
  getList()
}

getList()
</script>

<style scoped>
.table-select-input {
  width: 100%;
}

.dialog-footer {
  text-align: right;
}

:deep(.el-dialog) {
  border-top-left-radius: 0 !important;
  border-top-right-radius: 0 !important;
  /* 可选 - 去掉 margin 顶部空隙 */
  margin-top: 0 !important;
  top: 0 !important;
  transform: none !important;
}
</style>
