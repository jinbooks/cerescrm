import request from '@/utils/Request'

export function fetchPage(query: any) {
    return request({
        url: '/opportunity/fetch',
        method: 'get',
        params: query
    })
}

export function delLead(data: any) {
    return request({
        url: '/opportunity/delete',
        method: 'delete',
        data: data
    })
}

export function getOne(id: string) {
    return request({
        url: `/opportunity/get/${id}`,
        method: 'get',
    })
}

export function saveOne(data: any) {
    return request({
        url: '/opportunity/save',
        method: 'post',
        data: data
    })
}

export function updateOne(data: any) {
    return request({
        url: '/opportunity/update',
        method: 'put',
        data: data
    })
}

export function updateOpportunityStage(data: any) {
    return request({
        url: '/opportunity/updateOpportunityStage',
        method: 'put',
        data: data
    })
}
