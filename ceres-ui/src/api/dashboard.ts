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

export function getTrendAnalysis(year: number): any {
    return request({
        url: '/dashboard/trendAnalysis',
        method: 'get',
        params: {
            year: year
        }
    })
}