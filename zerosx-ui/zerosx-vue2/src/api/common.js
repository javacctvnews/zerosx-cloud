import request from '@/utils/request'
import serviceConfig from './serviceConfig'

export function deleteFile(objectName) {
  return request({
    url: serviceConfig.system + '/delete_file/' + objectName + '/delete',
    method: 'get'
  })
}
// 省市区数据
export function areas() {
  return request({
    url: serviceConfig.system + '/areas',
    method: 'get'
  })
}

//租户下拉框
export function operators() {
  return request({
    url: serviceConfig.system + '/muti_tenancy/select_options',
    method: 'post',
    data: {}
  })
}