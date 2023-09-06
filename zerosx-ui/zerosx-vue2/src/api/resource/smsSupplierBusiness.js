import request from '@/utils/request';
import serviceConfig from '@/api/serviceConfig'

export function pageList(query) {
  return request({
    url: serviceConfig.system + '/sms_supplier_business/page_list',
    method: 'post',
    data: query
  })
}
export function addSmsSupplierBusiness(data) {
  return request({
    url: serviceConfig.system + '/sms_supplier_business/save',
    method: 'post',
    data: data
  })
}
export function queryById(id) {
  return request({
    url: serviceConfig.system + '/sms_supplier_business/queryById/' + id,
    method: 'get'
  })
}

export function updateSmsSupplierBusiness(data) {
  return request({
    url: serviceConfig.system + '/sms_supplier_business/update',
    data: data,
    method: 'post'
  })
}

export function deleteSmsSupplierBusiness(id) {
  return request({
    url: serviceConfig.system + '/sms_supplier_business/delete/' + id,
    method: 'delete'
  })
}