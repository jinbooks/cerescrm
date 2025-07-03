import request from '@/utils/Request'

export function fetch(query: any) {
    return request({
        url: '/receivePayment/fetch',
        method: 'get',
        params: query
    })
}

export function get(id: string) {
    return request({
        url: `/receivePayment/get/${id}`,
        method: 'get',
    })
}

export function save(data: any) {
    return request({
        url: '/receivePayment/save',
        method: 'post',
        data: data
    })
}

export function update(data: any) {
    return request({
        url: '/receivePayment/update',
        method: 'put',
        data: data
    })
}

export function del(ids: any) {
    return request({
        url: '/receivePayment/delete',
        method: 'delete',
        data: {
            listIds: ids
        }
    })
}
