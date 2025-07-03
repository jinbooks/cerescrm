import request from '@/utils/Request'

export function fetchPage(query: any) {
    return request({
        url: '/opp-stage/fetch',
        method: 'get',
        params: query
    })
}

export function delLead(data: any) {
    return request({
        url: '/opp-stage/delete',
        method: 'delete',
        data: data
    })
}

export function getOne(id: string) {
    return request({
        url: `/opp-stage/get/${id}`,
        method: 'get',
    })
}

export function saveOne(data: any) {
    return request({
        url: '/opp-stage/save',
        method: 'post',
        data: data
    })
}

export function updateOne(data: any) {
    return request({
        url: '/opp-stage/update',
        method: 'put',
        data: data
    })
}

export function getAll() {
    return request({
        url: '/opp-stage/get-all',
        method: 'get',
    })
}
