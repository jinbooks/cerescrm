<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="55%"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryForm" :inline="true" @submit.native.prevent>
          <el-form-item :label="t('workSpace')">
            <el-input
                v-model="queryParams.name"
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
      <div class="button-top">
        <el-button
            @click="handleAdd"
            type="primary">
          {{ $t('jbx.text.add') }}
        </el-button>
        <el-button
            type="danger"
            :disabled="ids.length === 0"
            @click="onBatchDelete"
        >{{ t('org.button.deleteBatch') }}
        </el-button>
      </div>
      <el-table v-loading="loading" :data="groupsList" border @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" align="center"/>
        <el-table-column prop="workspaceId" :label="t('jbx.text.id')" min-width="80" align="left"/>
        <el-table-column prop="name" :label="t('workSpace')" min-width="100" align="left"/>
        <el-table-column :label="$t('jbx.text.action')" min-width="50" align="center">
          <template #default="scope">
            <el-button type="danger"  @click="onDelete(scope.row)">
              {{$t("jbx.text.delete")}}
            </el-button>
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
      <!--新增对话框-->
      <el-dialog v-model="addPermissionFlag" width="800" :title="addPermissionTitle" align-center :close-on-click-modal="false">
        <addPermissionBook
          ref="addPermissionBookRef"
          :permission-open="addPermissionFlag"
          :username="username"
          :userId="formId"
          @onSubmitSuccess="addSuccess">
        </addPermissionBook>
        <template #footer>
          <div class="dialog-footer">
            <el-button type="primary" @click="confirmAdd">{{ t('jbx.text.confirm') }}</el-button>
            <el-button @click="cancelAdd">{{ t('systemCancel') }}</el-button>
          </div>
        </template>
      </el-dialog>
    </template>
  </el-drawer>
</template>

<script setup lang="ts">
import modal from "@/plugins/modal";
import {ref, getCurrentInstance, reactive, toRefs, watch} from "vue";
import {useI18n} from "vue-i18n";
import * as userApi from "@/api/system/user";
import addPermissionBook from './addPermissionWorkspace.vue'
import { set2String } from '@/utils';

const {t} = useI18n()

const {proxy} = getCurrentInstance()!;

const emit: any = defineEmits(['groupDrawerClose'])


const props: any = defineProps({
  title: {
    type: String,
    default: ""
  },
  bookOpen: {
    type: Boolean,
    default: false
  },
  formId: {
    type: String,
    default: null
  },
  username: {
    type: String,
    default: ''
  }
})

const data: any = reactive({
  queryParams: {
    userId: '',
    username: '',
    memberName: '',
    name: '',
    pageSize: 10,
    pageNumber: 1,
    pageSizeOptions: [10, 20, 30]
  }
})

const {queryParams} = toRefs(data)

const dialogStatus: any = ref(false);
const groupsList: any = ref<any>([]);
const total: any = ref(0);
const loading: any = ref(true);
const selectionlist: any = ref<any>([]);
const ids: any = ref<any>([]);
const addPermissionFlag: any = ref(false);
const addPermissionTitle: any = ref('')
//会计准则
const standardList: any = ref<any>([]);

// 监听 open 变化
watch(
    () => props.bookOpen,
    (val: any) => {
      if (val) {
        dialogStatus.value = props.bookOpen
        if (props.formId) {
          queryParams.value.userId = props.formId;
          queryParams.value.username = props.username;
          queryParams.value.memberName = props.username;
          getList();
        } else {
          reset();
        }
      } else {
        reset();
      }
    },
    {immediate: true}
);

function getList(): any {
  userApi.userAccessWorkspace(queryParams.value).then((res: any) =>  {
    if (res.code === 0) {
      groupsList.value = res.data.records;
      total.value = res.data.total;
      loading.value = false;
    }
  })
}

function reset(): any {
  queryParams.value = {
    userId: '',
    username: '',
    memberName: '',
    pageSize: 10,
    pageNumber: 1,
    pageSizeOptions: [10, 20, 30]
  };
  loading.value = true;
}

function dialogOfClosedMethods(val: any): any {
  dialogStatus.value = false;
  emit('bookDrawerClose', val);
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

/** 多选操作*/
function handleSelectionChange(selection: any): any {
  selectionlist.value = selection;
  ids.value = selectionlist.value.map((item: any) =>  item.id);
}

/**
 * 新增用户组
 */
function handleAdd(): any {
  addPermissionFlag.value = true;
  addPermissionTitle.value = t('jbx.text.add')
}

function cancelAdd(): any {
  addPermissionFlag.value = false;

}

/**
 * 确认新增
 */
function confirmAdd(): any {
  proxy.$refs.addPermissionBookRef.submitAdd();
}

function addSuccess(): any {
  handleQuery();
  addPermissionFlag.value = false;
}

function onBatchDelete(): any {
  modal.confirm(t('jbx.confirm.text.delete')).then(function () {
    let workspaceIds: any = set2String(ids.value);
    return userApi.deleteAccessWorkspace({ids: workspaceIds});
  }).then((res: any) =>  {
    if (res.code === 0) {
      handleQuery();
      modal.msgSuccess(t('jbx.alert.delete.success'));
    } else {
      modal.msgError(res.message);
    }
  }).catch(() => {
  });
}

function onDelete(row: any): any {
  modal.confirm(t('org.deleteTip1') + row.name + t('org.deleteTip2')).then(function () {
    console.log(row.id)
    return userApi.deleteAccessWorkspace({ids: row.id});
  }).then((res: any) =>  {
    if (res.code === 0) {
      handleQuery();
      modal.msgSuccess(t('jbx.alert.delete.success'));
    } else {
      modal.msgError(res.message);
    }
  }).catch(() => {
  });
}
</script>

<style lang="scss" scoped>
.button-top {
  margin-bottom: 10px;
}
</style>
