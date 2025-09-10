<template>
  <el-breadcrumb class="app-breadcrumb" separator="/">
    <transition-group name="breadcrumb">
      <el-breadcrumb-item v-for="(item,index) in levelList" :key="item.path">
        <span v-if="item.redirect === 'noRedirect' || index === levelList.length - 1">{{ item.meta.title }}</span>
        <a class="no-redirect" v-else @click.prevent="handleLink(item)">{{ item.meta.title }}</a>
      </el-breadcrumb-item>
    </transition-group>
  </el-breadcrumb>
</template>

<script setup lang="ts">
import {useRoute, useRouter} from "vue-router";
import {ref, watchEffect} from "vue";
import {useI18n} from "vue-i18n";
import i18n from "@/languages";

const route: any = useRoute();
const router: any = useRouter();
const levelList: any = ref<any>([])

function getBreadcrumb(): any {
  // only show routes with meta.title
  let matched: any = route.matched.filter((item: any) => item.meta && item.meta.title);
  const first: any = matched[0]
  // 判断是否为首页
  if (!isDashboard(first)) {
    matched = [{path: '/index', meta: {title: i18n.global.t(import.meta.env.VITE_APP_TITLE || 'appTitle')}}].concat(matched)
  }

  levelList.value = matched
      .filter((item: any) => item.meta?.title && item.meta?.breadcrumb !== false)
      .map((item: any) => {
        // if(item.children && item.children.length == 1){
        //   item.meta.title = item.children[0].meta.title
        // }
        return item
      })
      .reduce((acc: any, item: any) => {
        const title = item.meta.title;
        if (!acc.seen.has(title)) {
          acc.seen.add(title);
          acc.result.push(item);
        }
        return acc;
      }, {seen: new Set(), result: []}).result;

  console.log(levelList.value)
}

function isDashboard(route: any): any {
  const name: any = route && route.name
  if (!name) {
    return false
  }
  return name.trim() === 'Index'
}

function handleLink(item: any): any {
  const {redirect, path} = item
  if (redirect) {
    router.push(redirect)
    return
  }
  router.push(path)
}

watchEffect(() => {
  if (route.path.startsWith('/redirect/')) {
    return
  }
  getBreadcrumb()
})
getBreadcrumb();
</script>

<style lang='scss' scoped>
@import "@/assets/styles/variables.module";

.app-breadcrumb.el-breadcrumb {
  display: inline-block;
  line-height: $base-navbar-height;
  margin-right: 8px;

  span, a {
    cursor: pointer;
  }

  .no-redirect {
    cursor: pointer;
    color: #97a8be;
  }
}
</style>