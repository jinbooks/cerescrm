import request from '@/utils/Request'

export function listCustomer(query: any) {
    return request({
        url: '/customer/fetch',
        method: 'get',
        params: query
    })
}

export function getCustomerOne(id: string) {
    return request({
        url: `/customer/get/${id}`,
        method: 'get',
    })
}

export function saveCustomer(data: any) {
    return request({
        url: '/customer/save',
        method: 'post',
        data: data
    })
}

export function updateCustomer(data: any) {
    return request({
        url: '/customer/update',
        method: 'put',
        data: data
    })
}

export function delCustomer(ids: any) {
    return request({
        url: '/customer/delete',
        method: 'delete',
        data: {
            listIds: ids instanceof Array ? ids : [ids]
        }
    })
}
