import request from '@/utils/request';
import serviceConfig from '@/api/serviceConfig'

export function pageList(query) {
  return request({
    url: serviceConfig.system + '/sys_role/page_list',
    method: 'post',
    data: query
  })
}
export function addSysRole(data) {
  return request({
    url: serviceConfig.system + '/sys_role/save',
    method: 'post',
    data: data
  })
}
export function queryById(id) {
  return request({
    url: serviceConfig.system + '/sys_role/queryById/' + id,
    method: 'get'
  })
}

export function updateSysRole(data) {
  return request({
    url: serviceConfig.system + '/sys_role/update',
    data: data,
    method: 'post'
  })
}

export function deleteSysRole(id) {
  return request({
    url: serviceConfig.system + '/sys_role/delete/' + id,
    method: 'delete'
  })
}

export function roleMenuTree(data) {
  return request({
    url: serviceConfig.system + '/sys_menu/roleMenuTree',
    method: 'post',
    data: data
  })
}

export function roleSelectList(data) {
  return request({
    url: serviceConfig.system + '/sys_role/select_list',
    method: 'post',
    data: data
  })
}
