<template>
  <div @click="click">
    <svg-icon icon-class="tool"/>
    <span style="margin-left: 5px">{{ t('cleanLocalSession') }}</span>
  </div>
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n"
import { ElMessageBox } from "element-plus"

const { t } = useI18n()

const click = () => {
     ElMessageBox.confirm(
      t('confirmCleanLocalData'),        // 确定要清理本地缓存数据吗？
      t('systemNotice'),                 // 系统提示
      {
        confirmButtonText: t('jbx.text.confirm'),  // 确认
        cancelButtonText: t('systemCancel'),       // 取消
        type: 'warning'
      }).then(() => {
        window.localStorage.clear()
        window.location.reload()
      }).catch(() => {
      });
    }
</script>

<style scoped>
.tool2 {
  display: inline-block;
  cursor: pointer;
  fill: #5a5e66;
  width: 20px;
  height: 20px;
  vertical-align: 10px;
}
</style>
