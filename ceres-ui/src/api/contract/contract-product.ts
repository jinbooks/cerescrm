import request from '@/utils/Request'

export function fetch(query: any) {
    return request({
        url: '/contractProduct/fetch',
        method: 'get',
        params: query
    })
}

export function get(id: string) {
    return request({
        url: `/contractProduct/get/${id}`,
        method: 'get',
    })
}

export function save(data: any) {
    return request({
        url: '/contractProduct/save',
        method: 'post',
        data: data
    })
}

export function update(data: any) {
    return request({
        url: '/contractProduct/update',
        method: 'put',
        data: data
    })
}

export function del(ids: any) {
    return request({
        url: '/contractProduct/delete',
        method: 'delete',
        data: {
            listIds: ids
        }
    })
}
