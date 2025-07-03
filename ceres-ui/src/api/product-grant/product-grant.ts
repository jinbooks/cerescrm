import request from '@/utils/Request'

export function fetch(query: any) {
    return request({
        url: '/productGrant/fetch',
        method: 'get',
        params: query
    })
}

export function get(id: string) {
    return request({
        url: `/productGrant/get/${id}`,
        method: 'get',
    })
}

export function save(data: any) {
    return request({
        url: '/productGrant/save',
        method: 'post',
        data: data
    })
}

export function update(data: any) {
    return request({
        url: '/productGrant/update',
        method: 'put',
        data: data
    })
}

export function del(ids: any) {
    return request({
        url: '/productGrant/delete',
        method: 'delete',
        data: {
            listIds: ids
        }
    })
}

export function license(license: string) {
    return request({
        url: `/productGrant/license/${license}`,
        method: 'get',
    })
}
