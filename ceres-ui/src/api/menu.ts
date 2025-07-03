import request from '@/utils/Request'
import {handleTree} from '@/utils/Jinbooks'
import useUserStore from '@/store/modules/user'
import i18n from '@/languages'

const {t} = i18n.global;

const mapMenu: any = {}
const mapParentIds: any = {}
const mapChildren: any = {}
const additionalMenu: any = {}

/**
 * 转换成router可用的格式
 * @param menu
 */
function formatMenu(menu: any): any {
    const icon: any = (menu.resStyle || "list").replace("anticon-", '')
    let requestUrl: any = menu.requestUrl;
    // 斜杠开头去掉
    if (requestUrl && menu.requestUrl.startsWith('/')) {
        requestUrl = menu.requestUrl.substring(1);
    }

    if(menu.classify === 'PAGE'){
        console.log(menu.parentId && menu.parentId.startsWith('/') ? menu.parentId.substring(1) : menu.parentId)
    }

    return {
        "id": menu.id,
        "parentId": menu.parentId,
        "name": menu.id,
        "path": menu.requestUrl || "",
        "query": menu.params || null,
        "hidden": menu.classify === 'PAGE' || (menu.isVisible !== 'y'),
        "redirect": "noRedirect",
        "component": mapParentIds[menu.id] && mapChildren[menu.id]
            .filter((t: any) => t.isVisible === 'y' && t.classify === 'MENU').length > 0
            ? "ParentView"
            : requestUrl,
        "alwaysShow": !!mapParentIds[menu.id] && mapChildren[menu.id]
            .filter((t: any) => t.isVisible === 'y' && t.classify === 'MENU').length > 0,
        "permissions": [menu.permission],
        "meta": {
            "title": menu.i18n && t(menu.i18n).indexOf('.') < 0
                ? t(menu.i18n)
                : menu.resName,
            "icon": icon,
            "noCache": menu.isCache === 'n',
            "link": menu.isFrame === 'y' ? menu.requestUrl : null,
            "activeMenu": menu.classify === 'PAGE'
                ? (menu.parentId && menu.parentId.startsWith('/') ? menu.parentId.substring(1) : menu.parentId)
                : null,
            "isButton": menu.classify === 'BUTTON'
        },
        "raw": menu
    }
}

// 获取路由
export const getRouters: any = () => {
    return new Promise((resolve: any, reject: any) => {
        request({
            url: '/open/func/list?_allow_anonymous=true&appId=1',
            method: 'get'
        }).then((res: any) => {
            // 区分菜单权限和按钮权限
            const allFunctions = res.data.functions.filter((t: any) => t.status === "1");

            // 提取按钮权限
            const buttonPerms = allFunctions
                .filter((t: any) => t.classify === 'BUTTON')
                .map((t: any) => t.permission);

            // 处理菜单项（MENU和PAGE）
            const menus = allFunctions
                .filter((t: any) => ['MENU', 'PAGE'].includes(t.classify))
                .sort((a: any, b: any) => a.sortIndex - b.sortIndex)
            menus.forEach((item: any) => {
                mapMenu[item.id] = item
                mapParentIds[item.parentId] = item
                if (!mapChildren[item.parentId]) {
                    mapChildren[item.parentId] = []
                }
                mapChildren[item.parentId].push(item)
            })
            // 预处理空菜单,过滤出不需要显示在菜单中，但需要定义路由的菜单（附件的隐藏菜单）
            // for (let key in mapChildren) {
            //     const children: any = mapChildren[key]
            //     children.filter((t: any) => t.isVisible !== 'y').forEach((t: any) => {
            //         additionalMenu[t.id] = formatMenu(t)
            //     })
            // }

            // 转换为路由格式
            let tree: any = menus
                .filter((t: any) => {
                    return !additionalMenu[t.id] &&
                        !(t.requestUrl && t.requestUrl.startsWith('/redirect'));
                })
                .map((menu: any) => {
                    const formatted = formatMenu(menu);
                    // PAGE类型设置隐藏
                    if (menu.classify === 'PAGE') {
                        formatted.hidden = true;
                    }
                    return formatted;
                })

            tree = handleTree(tree || [])
            for (let key in additionalMenu) {
                tree.push(additionalMenu[key])
            }
            tree = tree.map((t: any) => {
                if (t.path && !t.path.startsWith('/')) {
                    t.path = '/' + t.path
                }
                if (!t.children || t.children.length < 1) {
                    const r: any = JSON.parse(JSON.stringify(t))
                    r.children = [t]
                    r.component = 'Layout'
                    r.path = ''
                    r.name = null
                    r.permissions = null
                    return r
                } else {
                    t.component = 'Layout'
                    return t
                }
            })

            // 将首页重定向到第一个页面
            if (tree.length > 0) {
                const index: any = tree[0].children[0]
                tree[0].redirect = index.path
            }

            // 合并权限（包含按钮权限）
            useUserStore().permissions = [...new Set([...res.data.functions.map((t: any) => t.permission), ...buttonPerms])];

            resolve({
                code: 200,
                data: tree
            })
        }).catch((err: any) => {
            reject(err)
        })
    })
}
