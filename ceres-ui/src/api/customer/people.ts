import request from '@/utils/Request'

export function listCustomerContact(query: any) {
    return request({
        url: '/people/fetch',
        method: 'get',
        params: query
    })
}

export function getCustomerContactOne(id: string) {
    return request({
        url: `/people/get/${id}`,
        method: 'get',
    })
}

export function saveCustomerContact(data: any) {
    return request({
        url: '/people/save',
        method: 'post',
        data: data
    })
}

export function updateCustomerContact(data: any) {
    return request({
        url: '/people/update',
        method: 'put',
        data: data
    })
}

export function delCustomerContact(ids: any) {
    return request({
        url: '/people/delete',
        method: 'delete',
        data: {
            listIds: ids
        }
    })
}
