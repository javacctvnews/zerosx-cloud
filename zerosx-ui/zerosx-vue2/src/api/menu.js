import request from '@/utils/request'
import serviceConfig from './serviceConfig'

// 获取路由
export const getRouters = () => {
  return request({
    url: serviceConfig.system + '/getRouters',
    method: 'get'
  })
}