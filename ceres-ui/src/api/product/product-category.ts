import request from '@/utils/Request'

export function fetch(query: any) {
    return request({
        url: '/productCategory/fetch',
        method: 'get',
        params: query
    })
}

export function get(id: string) {
    return request({
        url: `/productCategory/get/${id}`,
        method: 'get',
    })
}

export function save(data: any) {
    return request({
        url: '/productCategory/save',
        method: 'post',
        data: data
    })
}

export function update(data: any) {
    return request({
        url: '/productCategory/update',
        method: 'put',
        data: data
    })
}

export function del(ids: any) {
    return request({
        url: '/productCategory/delete',
        method: 'delete',
        data: {
            listIds: ids
        }
    })
}

export function tree(id: string) {
    return request({
        url: `/productCategory/tree`,
        method: 'get',
    })
}
