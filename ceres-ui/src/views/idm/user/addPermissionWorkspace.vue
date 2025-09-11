<template>
    <div class="queryForm">
      <el-form :model="queryParams" ref="queryForm" :inline="true" @submit.native.prevent>
         <el-form-item :label="t('workSpace')">
          <el-input
              v-model="queryParams.bookName"
              clearable
              @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button @click="handleQuery">{{ t('org.button.query') }}</el-button>
          <el-button @click="resetQuery">{{ t('org.button.reset') }}</el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table v-loading="loading" :data="list" border @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" align="center"/>
      <el-table-column prop="workspaceId" label="编码" min-width="70" align="left"/>
      <el-table-column prop="name" :label="t('commonName')" min-width="100" align="left"/>
    </el-table>
    <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNumber"
        v-model:limit="queryParams.pageSize"
        :page-sizes="queryParams.pageSizeOptions"
        @pagination="getList"
    />
</template>

<script setup lang="ts">
import modal from "@/plugins/modal";
import {useI18n} from "vue-i18n";
import {
  ref,
  getCurrentInstance,
  reactive,
  toRefs,
  watch,
  PropType
} from "vue";
import * as userApi from "@/api/system/user";

const {t} = useI18n()

const {proxy} = getCurrentInstance()!;

const emit: any = defineEmits(['onSubmitSuccess'])

const props: any = defineProps({
  permissionOpen: {
    type: Boolean,
    default: false
  },
  username: {
    type: String,
    default: undefined
  },
  userId: {
    type: String,
    default: undefined
  }
})

const data: any = reactive({
  queryParams: {
    username: '',
    userId:'',
    pageSize: 5,
    pageNumber: 1,
    pageSizeOptions: [5, 10, 20]
  }
})
const loading: any = ref(true);
const list: any = ref<any>([]);
const total: any = ref(0);
const selectionlist: any = ref<any>([]);
const ids: any = ref<any>([]);
const names: any = ref<any>([]);
//会计准则
const standardList: any = ref<any>([]);

const {queryParams} = toRefs(data)

// 监听 open 变化
watch(
    () => props.permissionOpen,
    (val: any) => {
      if (val && props.username) {
        queryParams.value.username = props.username;
        queryParams.value.userId = props.userId;
        getList();
      } else {
        reset();
      }
    },
    {immediate: true}
);

function reset(): any {
  queryParams.value = {
    username: '',
    userId: '',
    pageSize: 5,
    pageNumber: 1,
    pageSizeOptions: [5, 10, 20]
  }
  loading.value = true;
}

function handleQuery(): any {
  queryParams.value.pageNumber = 1;
  getList();
}

function resetQuery(): any {
  queryParams.value.pageNumber = 1;
  queryParams.value.roleName = undefined;
  getList();
}

function getList(): any {
  userApi.userNotAccessWorkspace(queryParams.value).then((res: any) =>  {
    if (res.code === 0) {
      list.value = res.data.records;
      total.value = res.data.total;
      loading.value = false;
    }
  })
}


/** 多选操作*/
function handleSelectionChange(selection: any): any {
  selectionlist.value = selection;

  const idsAndNames: any = selectionlist.value.reduce((acc: any, item: any) => {
    acc.ids.push(item.workspaceId);
    return acc;
  }, { ids: [], names: [] });

  ids.value = idsAndNames.ids;
  // names.value = idsAndNames.names;
}

function submitAdd(): any {
    userApi.addAccessWorkspace({workspaceIds: ids.value, username: props.username,userId:props.userId}).then((res: any) =>  {
    if (res.code === 0) {
      modal.msgSuccess(t('org.success.add'));
      emit('onSubmitSuccess');
      handleQuery();
    } else {
      modal.msgError(res.message);
    }
  })
}

defineExpose({submitAdd})
</script>
