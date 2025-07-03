import request from '@/utils/Request'

// 查询会计科目列表
export function listBooksSets(query : any): any {
    return request({
        url: '/workspace/fetch',
        method: 'get',
        params: query
    })
}

export function saveOne(data : any): any {
    return request({
        url: '/workspace/save',
        method: 'post',
        data: data
    })
}

export function updateOne(data : any): any {
    return request({
        url: '/workspace/update',
        method: 'put',
        data: data
    })
}

export function getOne(id : any): any {
    return request({
        url: `/workspace/get/${id}`,
        method: 'get',
    })
}

export function deleteBatch(data : any): any {
    return request({
        url: '/workspace/delete',
        method: 'delete',
        data: data
    })
}

export function listStore(): any {
    return request({
        url: '/workspace/fetchAll',
        method: 'get'
    })
}


