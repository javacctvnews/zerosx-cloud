import request from '@/utils/request'
import serviceConfig from './serviceConfig'

// 上传文件
export function uploadFile(data) {
  return request({
    url: serviceConfig.resource + '/upload_file',
    method: 'post',
    data: data,
    timeout: 20000
  })
}

// 删除上传的文件
export function deleteFile(objectName) {
  return request({
    url: serviceConfig.resource + '/delete_file/' + objectName + '/delete',
    method: 'delete'
  })
}
// 省市区数据
export function areas() {
  return request({
    url: serviceConfig.resource + '/areas',
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