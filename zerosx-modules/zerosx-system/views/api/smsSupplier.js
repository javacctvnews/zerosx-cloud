import request from '@/utils/request';
import serviceConfig from '@/api/serviceConfig'

export function pageList(query) {
  return request({
    url: serviceConfig.system + '/sms_supplier/page_list',
    method: 'post',
    data: query
  })
}
export function addSmsSupplier(data) {
  return request({
    url: serviceConfig.system + '/sms_supplier/save',
    method: 'post',
    data: data
  })
}
export function queryById(id) {
  return request({
    url: serviceConfig.system + '/sms_supplier/queryById/' + id,
    method: 'get'
  })
}

export function updateSmsSupplier(data) {
  return request({
    url: serviceConfig.system + '/sms_supplier/update',
    data: data,
    method: 'post'
  })
}

export function deleteSmsSupplier(id) {
  return request({
    url: serviceConfig.system + '/sms_supplier/delete/' + id,
    method: 'delete'
  })
}