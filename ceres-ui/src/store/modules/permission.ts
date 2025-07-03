import auth from '@/plugins/auth'
import router, {constantRoutes, dynamicRoutes} from '@/router'
import {getRouters} from '@/api/menu'
import Layout from '@/layout/index.vue'
import ParentView from '@/components/ParentView/index.vue'
import InnerLink from '@/layout/components/InnerLink/index.vue'
import {defineStore} from "pinia";

// 匹配views里面所有的.vue文件
const modules: any = import.meta.glob('./../../views/**/*.vue')

const usePermissionStore: any = defineStore(
    'permission',
    {
        state: () => ({
            routes: [],
            addRoutes: [],
            defaultRoutes: [],
            topbarRouters: [],
            sidebarRouters: []
        }),
        actions: {
            setRoutes(routes: any) {
                this.addRoutes = routes
                this.routes = constantRoutes.concat(routes)
            },
            setDefaultRoutes(routes: any) {
                this.defaultRoutes = constantRoutes.concat(routes)
            },
            setTopbarRoutes(routes: any) {
                this.topbarRouters = routes
            },
            setSidebarRouters(routes: any) {
                this.sidebarRouters = routes
            },
            generateRoutes() {
                return new Promise((resolve: any) => {
                    // 向后端请求路由数据
                    getRouters().then((res: any) => {
                        const sdata: any = JSON.parse(JSON.stringify(res.data))
                        const rdata: any = JSON.parse(JSON.stringify(res.data))
                        const defaultData: any = JSON.parse(JSON.stringify(res.data))
                        const sidebarRoutes: any = filterAsyncRouter(sdata)
                        const rewriteRoutes: any = filterAsyncRouter(rdata, false, true)
                        const defaultRoutes: any = filterAsyncRouter(defaultData)
                        const asyncRoutes: any = filterDynamicRoutes(dynamicRoutes)
                        asyncRoutes.forEach((route: any) => {
                            router.addRoute(route)
                        })
                        this.setRoutes(rewriteRoutes)
                        this.setSidebarRouters(constantRoutes.concat(sidebarRoutes))
                        this.setDefaultRoutes(sidebarRoutes)
                        this.setTopbarRoutes(defaultRoutes)
                        resolve(rewriteRoutes)
                    }).catch((err: any) => {
                        console.error(err)
                        resolve([])
                    })
                })
            }
        }
    })

// 遍历后台传来的路由字符串，转换为组件对象
function filterAsyncRouter(asyncRouterMap: any, lastRouter: any = false, type: any = false): any {
    return asyncRouterMap.filter((route: any) => {
        const component = route.component

        if (type && route.children) {
            route.children = filterChildren(route.children)
        }
        let isChildrenRoute = false
        if (route.component) {
            // Layout ParentView 组件特殊处理
            if (route.component === 'Layout') {
                route.component = Layout
            } else if (route.component === 'ParentView') {
                route.component = ParentView
            } else if (route.component === 'InnerLink') {
                route.component = InnerLink
            } else {
                route.component = loadView(component)
                isChildrenRoute = true

                if (lastRouter && lastRouter.path && route.path.indexOf(lastRouter.path) > -1) {
                    route.path = route.path.substring(route.path.indexOf(lastRouter.path) + lastRouter.path.length)
                }
                if (route.path && route.path.startsWith('/')) {
                    route.path = route.path.substring(1)
                }
            }
        }

        if (route.children != null && route.children && route.children.length > 0) {
            if (isChildrenRoute) {
                const indexRoute = JSON.parse(JSON.stringify(route))
                indexRoute.id = new Date().getTime() + "__"
                indexRoute.parentId = route.id
                indexRoute.children = null
                indexRoute.redirect = null
                indexRoute.path = ""
                indexRoute.component = loadView(component)
                route.children.push(indexRoute)
            }
            route.children = filterAsyncRouter(route.children, route, type)
        } else {
            delete route['children']
            delete route['redirect']
        }
        return true
    })
}

function filterChildren(childrenMap: any[], lastRouter: any = false): any[] {
    let children: any[] = [];

    for (const el of childrenMap) {
        const isParentView = el.component === 'ParentView';
        const hasChildren = el.children && Array.isArray(el.children) && el.children.length > 0;

        // 构建当前节点的路径
        let currentPath = el.path;
        if (lastRouter) {
            currentPath = `${lastRouter.path}/${el.path}`.replace(/\/+/g, '/');
        }

        if (isParentView && !lastRouter) {
            // ParentView 类型：提取子项并拼接路径
            if (hasChildren) {
                for (const c of el.children) {
                    const childPath = `${el.path}/${c.path}`.replace(/\/+/g, '/');
                    const newChild = {...c, path: childPath};

                    if (newChild.children && Array.isArray(newChild.children)) {
                        newChild.children = filterChildren(newChild.children, newChild);
                    }

                    children.push(newChild);
                }
            }
        } else {
            // 普通情况：直接构建当前节点
            const node = {...el, path: currentPath};

            if (hasChildren) {
                node.children = filterChildren(el.children, node);
                if (node.children.length === 0) {
                    delete node.children;
                } else {
                    children.push(node);
                    continue;
                }
            }

            // 如果无 children 或 children 已被删除，保留当前节点
            children.push(node);
        }
    }

    return children;
}

// 动态路由遍历，验证是否具备权限
export function filterDynamicRoutes(routes: any): any {
    const res: any = []
    routes.forEach((route: any) => {
        // 忽略按钮权限路由
        if (route.meta?.isButton) {
            return;
        }

        if (route.permissions) {
            if (auth.hasPermiOr(route.permissions)) {
                res.push(route)
            }
        } else if (route.roles) {
            if (auth.hasRoleOr(route.roles)) {
                res.push(route)
            }
        }
    })
    return res
}

export const loadView: any = (view: any) => {
    let res;
    for (const path in modules) {
        const dir: any = path.split('views/')[1].split('.vue')[0];
        if (dir === view) {
            res = () => modules[path]();
        }
    }
    return res;
}

export default usePermissionStore