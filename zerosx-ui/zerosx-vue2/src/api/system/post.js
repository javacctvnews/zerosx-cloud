import request from '@/utils/request'
import serviceConfig from '@/api/serviceConfig'

// 查询岗位列表
export function listPost(query) {
  return request({
    url: serviceConfig.system + '/sys_post/page_list',
    method: 'post',
    data: query
  })
}

// 查询岗位详细
export function getPost(postId) {
  return request({
    url: serviceConfig.system + '/sys_post/queryById/' + postId,
    method: 'get'
  })
}

// 新增岗位
export function addPost(data) {
  return request({
    url: serviceConfig.system + '/sys_post/save',
    method: 'post',
    data: data
  })
}

// 修改岗位
export function updatePost(data) {
  return request({
    url: serviceConfig.system + '/sys_post/update',
    method: 'post',
    data: data
  })
}

// 删除岗位
export function delPost(postId) {
  return request({
    url: serviceConfig.system + '/sys_post/delete/' + postId,
    method: 'delete'
  })
}
