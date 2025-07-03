import request from '@/utils/Request'

export function listCustomerSegment(query: any) {
    return request({
        url: '/customer/segment/fetch',
        method: 'get',
        params: query
    })
}

export function getCustomerSegmentOne(id: string) {
    return request({
        url: `/customer/segment/get/${id}`,
        method: 'get',
    })
}

export function saveCustomerSegment(data: any) {
    return request({
        url: '/customer/segment/save',
        method: 'post',
        data: data
    })
}

export function updateCustomerSegment(data: any) {
    return request({
        url: '/customer/segment/update',
        method: 'put',
        data: data
    })
}

export function delCustomerSegment(ids: any) {
    return request({
        url: '/customer/segment/delete',
        method: 'delete',
        data: {
            listIds: ids
        }
    })
}
