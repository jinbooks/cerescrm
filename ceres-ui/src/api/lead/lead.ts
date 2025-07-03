import request from '@/utils/Request'

export function fetchPage(query: any) {
    return request({
        url: '/lead/fetch',
        method: 'get',
        params: query
    })
}

export function delLead(data: any) {
    return request({
        url: '/lead/delete',
        method: 'delete',
        data: data
    })
}

export function getOne(id: string) {
    return request({
        url: `/lead/get/${id}`,
        method: 'get',
    })
}

export function saveOne(data: any) {
    return request({
        url: '/lead/save',
        method: 'post',
        data: data
    })
}

export function updateOne(data: any) {
    return request({
        url: '/lead/update',
        method: 'put',
        data: data
    })
}
