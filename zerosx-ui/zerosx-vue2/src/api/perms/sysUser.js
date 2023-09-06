import request from '@/utils/request';
import serviceConfig from '@/api/serviceConfig'

export function pageList(query) {
  return request({
    url: serviceConfig.system + '/sys_user/page_list',
    method: 'post',
    data: query
  })
}
export function addSysUser(data) {
  return request({
    url: serviceConfig.system + '/sys_user/save',
    method: 'post',
    data: data
  })
}
export function queryById(id) {
  return request({
    url: serviceConfig.system + '/sys_user/queryById/' + id,
    method: 'get'
  })
}

export function updateSysUser(data) {
  return request({
    url: serviceConfig.system + '/sys_user/update',
    data: data,
    method: 'post'
  })
}

export function deleteSysUser(id) {
  return request({
    url: serviceConfig.system + '/sys_user/delete/' + id,
    method: 'delete'
  })
}

// 查询授权角色
export function getAuthRole(userId) {
  return request({
    url: '/system/user/authRole/' + userId,
    method: 'get'
  })
}
// 查询用户个人信息
export function getUserProfile() {
  return request({
    url: serviceConfig.system + '/sys_user/profile',
    method: 'get'
  })
}

// 查询用户详细
export function getUser(userId) {
  return request({
    url: '/system/user/' + parseStrEmpty(userId),
    method: 'get'
  })
}

// 用户头像上传
export function uploadAvatar(data) {
  return request({
    url: serviceConfig.system + '/sys_user/profile/avatar',
    method: 'post',
    data: data
  })
}

// 修改用户个人信息
export function updateUserProfile(data) {
  return request({
    url: serviceConfig.system +'/sys_user/update_profile',
    method: 'put',
    data: data
  })
}

// 用户密码重置
export function updateUserPwd(data) {
  return request({
    url: serviceConfig.system +'/sys_user/update_pwd',
    method: 'post',
    params: data
  })
}