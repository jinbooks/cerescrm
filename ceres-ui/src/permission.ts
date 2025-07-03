import router from './router'
import {ElMessage} from 'element-plus'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import {getToken} from '@/utils/Auth'
import {isHttp} from '@/utils/Validate'
import useUserStore from '@/store/modules/user'
import booksSetStore from '@/store/modules/workspaceStore'
import useSettingsStore from '@/store/modules/settings'
import usePermissionStore from '@/store/modules/permission'
import {loginPreGet} from "@/api/login.js";
import logoUrl from "@/assets/logo/logo.png";
import appStore from "@/store/modules/app.js";
import Layout from '@/layout/index.vue'
import ParentView from '@/components/ParentView/index.vue'

NProgress.configure({showSpinner: false});

const whiteList: any = ['/login', '/register', '/callback', '/forgot','/license'];

router.beforeEach(async (to: any, from: any, next: any) => {
    NProgress.start();
    const token = getToken();

    if (token) {
        to.meta.title && useSettingsStore().setTitle(to.meta.title);

        if (to.path === '/login') {
            next({path: '/'});
            NProgress.done();
        } else if (whiteList.includes(to.path)) {
            next();
        } else {
            const userStore = useUserStore();

            if (userStore.roles.length === 0) {
                try {
                    const res = await loginPreGet();
                    if (res.code === 0) {
                        const staticAppInfo: any = res.data.inst;
                        staticAppInfo.logo = logoUrl;
                        appStore().setAppInfo(staticAppInfo);
                    }

                    // 👉 等待账套加载完成
                    await booksSetStore().refreshData();

                    await userStore.currentUser();
                    const accessRoutes = await usePermissionStore().generateRoutes();
                    addRoutes(accessRoutes, null, "", 1)

                    next({...to, replace: true});
                } catch (err: any) {
                    console.error(err);
                    await userStore.logOut();
                    ElMessage.error(err);
                    next({path: '/'});
                }
            } else {
                next();
            }
        }
    } else {
        if (whiteList.includes(to.path)) {
            next();
        } else {
            next(`/login?redirect=${to.fullPath}`);
            NProgress.done();
        }
    }
});

function addRoutes(routes: any, lastRoute: any, parentRoute?: any, level?: any) {
    routes.forEach((route: any) => {
        if (!isHttp(route.path) && !lastRoute) {
            router.addRoute(route);
        }
        if (level > 2 && lastRoute && route.path && route.path != "index") {
            route.path = route.path.startsWith("/") ? "" : "/" + route.path;
            router.addRoute(lastRoute.path, {
                id: route.parentId + "__",
                path: "",
                component: Layout,
                name: route.name + "pp",
                redirect: route.redirect,
                meta: {
                    title: lastRoute.meta.title,
                    icon: lastRoute.meta.icon,
                    activeMenu: route.parentId
                },
                children: [route]
            });
        }
        if (route.children) {
            parentRoute = parentRoute + (parentRoute.endsWith("/") || route.path.startsWith("/") ? "" : "/") + route.path;
            addRoutes(route.children, route, parentRoute, level + 1);
        }
    });
}

router.afterEach(() => {
    NProgress.done()
})
