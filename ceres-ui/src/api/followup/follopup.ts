import request from '@/utils/Request'

export function fetchPage(query: any) {
    return request({
        url: '/follow-up/fetch',
        method: 'get',
        params: query
    })
}

export function delLead(data: any) {
    return request({
        url: '/follow-up/delete',
        method: 'delete',
        data: data
    })
}

export function saveOne(data: any) {
    return request({
        url: '/follow-up/save',
        method: 'post',
        data: data
    })
}

