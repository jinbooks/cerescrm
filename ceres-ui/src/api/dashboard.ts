import request from '@/utils/Request'

export function getDashboard(year: number): any {
    return request({
        url: '/dashboard',
        method: 'get',
        params: {
            year: year
        }
    })
}