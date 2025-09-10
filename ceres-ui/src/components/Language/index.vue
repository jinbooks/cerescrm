<template>
  <div>
    <el-dropdown trigger="click" @command="handleSetLanguage">
      <div class="size-icon--style">
        <svg-icon class-name="language" icon-class="language2"/>
      </div>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item v-for="item of languages" :key="item.value" :disabled="locale === item.value"
                            :command="item.value">
            {{ item.label }}
          </el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>

<script setup>
import {useI18n} from 'vue-i18n'
import {getLangList, setLang} from '@/languages'

const {locale} = useI18n()
const languages = getLangList()
const handleSetLanguage = (lang) => {
  locale.value = lang
  setLang(lang)
  window.location.reload()
}
</script>

<style lang='scss' scoped>
@import "@/assets/styles/variables.module";

.size-icon--style {
  font-size: 16px;
}
</style>
