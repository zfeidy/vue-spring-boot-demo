import axios from '../libs/request'

export const hello = () => {
    return axios.request({
        url: '/api/hello',
        method: 'get'
    })
}