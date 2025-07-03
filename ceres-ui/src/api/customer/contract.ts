import request from '@/utils/Request'

export function listContract(query: any) {
    return request({
        url: '/contract/fetch',
        method: 'get',
        params: query
    })
}

export function getContractOne(id: string) {
    return request({
        url: `/contract/get/${id}`,
        method: 'get',
    })
}

export function saveContract(data: any) {
    return request({
        url: '/contract/save',
        method: 'post',
        data: data
    })
}

export function updateContract(data: any) {
    return request({
        url: '/contract/update',
        method: 'put',
        data: data
    })
}

export function delContract(ids: any) {
    return request({
        url: '/contract/delete',
        method: 'delete',
        data: {
            listIds: ids
        }
    })
}
