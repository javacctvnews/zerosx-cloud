import request from '@/utils/request';
import serviceConfig from '@/api/serviceConfig'

export function pageList(query) {
  return request({
    url: serviceConfig.system + '/sys_dept/page_list',
    method: 'post',
    data: query
  })
}

export function tableTree(query) {
  return request({
    url: serviceConfig.system + '/sys_dept/table_tree',
    method: 'post',
    data: query
  })
}

export function detpTreeSelect(query) {
  return request({
    url: serviceConfig.system + '/sys_dept/tree_select',
    method: 'post',
    data: query
  })
}
export function addSysDept(data) {
  return request({
    url: serviceConfig.system + '/sys_dept/save',
    method: 'post',
    data: data
  })
}
export function queryById(id) {
  return request({
    url: serviceConfig.system + '/sys_dept/queryById/' + id,
    method: 'get'
  })
}

export function updateSysDept(data) {
  return request({
    url: serviceConfig.system + '/sys_dept/update',
    data: data,
    method: 'post'
  })
}

export function deleteSysDept(id) {
  return request({
    url: serviceConfig.system + '/sys_dept/delete/' + id,
    method: 'delete'
  })
}