import request from '@/utils/request';
import serviceConfig from '@/api/serviceConfig'

export function pageList(query) {
  return request({
    url: serviceConfig.system + '/muti_tenancy/list_pages',
    method: 'post',
    data: query
  })
}
export function addTenant(data) {
  return request({
    url: serviceConfig.system + '/muti_tenancy/save',
    method: 'post',
    data: data
  })
}
export function getById(id) {
  return request({
    url: serviceConfig.system + '/muti_tenancy/getById/' + id,
    method: 'get'
  })
}

export function updateTenant(data) {
  return request({
    url: serviceConfig.system + '/muti_tenancy/update',
    data: data,
    method: 'post'
  })
}

export function deleteTenant(id) {
  return request({
    url: serviceConfig.system + '/muti_tenancy/deleted/' + id,
    method: 'delete'
  })
}