import request from '@/utils/request';
import serviceConfig from '@/api/serviceConfig'

export function pageList(query) {
  return request({
    url: serviceConfig.resource + '/oss_supplier/page_list',
    method: 'post',
    data: query
  })
}
export function addOssSupplier(data) {
  return request({
    url: serviceConfig.resource + '/oss_supplier/save',
    method: 'post',
    data: data
  })
}
export function queryById(id) {
  return request({
    url: serviceConfig.resource + '/oss_supplier/queryById/' + id,
    method: 'get'
  })
}

export function updateOssSupplier(data) {
  return request({
    url: serviceConfig.resource + '/oss_supplier/update',
    data: data,
    method: 'post'
  })
}

export function deleteOssSupplier(id) {
  return request({
    url: serviceConfig.resource + '/oss_supplier/delete/' + id,
    method: 'delete'
  })
}