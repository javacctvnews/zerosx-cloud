import request from '@/utils/request';
import serviceConfig from '@/api/serviceConfig'

export function pageList(query) {
  return request({
    url: serviceConfig.system + '/sys_post/page_list',
    method: 'post',
    data: query
  })
}
export function addSysPost(data) {
  return request({
    url: serviceConfig.system + '/sys_post/save',
    method: 'post',
    data: data
  })
}
export function queryById(id) {
  return request({
    url: serviceConfig.system + '/sys_post/queryById/' + id,
    method: 'get'
  })
}

export function updateSysPost(data) {
  return request({
    url: serviceConfig.system + '/sys_post/update',
    data: data,
    method: 'post'
  })
}

export function deleteSysPost(id) {
  return request({
    url: serviceConfig.system + '/sys_post/delete/' + id,
    method: 'delete'
  })
}

export function postSelectList(data) {
  return request({
    url: serviceConfig.system + '/sys_post/select_list',
    method: 'post',
    data: data
  })
}