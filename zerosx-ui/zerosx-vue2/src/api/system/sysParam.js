import request from '@/utils/request';
import serviceConfig from '@/api/serviceConfig'

export function pageList(query) {
  return request({
    url: serviceConfig.system + '/sys_param/page_list',
    method: 'post',
    data: query
  })
}
export function addSysParam(data) {
  return request({
    url: serviceConfig.system + '/sys_param/save',
    method: 'post',
    data: data
  })
}
export function queryById(id) {
  return request({
    url: serviceConfig.system + '/sys_param/queryById/' + id,
    method: 'get'
  })
}

export function updateSysParam(data) {
  return request({
    url: serviceConfig.system + '/sys_param/update',
    data: data,
    method: 'post'
  })
}

export function deleteSysParam(id) {
  return request({
    url: serviceConfig.system + '/sys_param/delete/' + id,
    method: 'delete'
  })
}
// 按编码查询系统参数
export function queryByKey(data) {
  return request({
    url: serviceConfig.system + '/sys_param/queryByKey',
    data: data,
    method: 'post'
  })
}