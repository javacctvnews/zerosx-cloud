import request from '@/utils/request'
import serviceConfig from '@/api/serviceConfig'

export function pageList(query) {
  return request({
    url: serviceConfig.auth + '/oauth_client_details/list_page',
    method: 'post',
    data: query
  })
}
export function addOauthClientDetails(data) {
  return request({
    url: serviceConfig.auth + '/oauth_client_details/save',
    method: 'post',
    data: data
  })
}
export function queryById(id) {
  return request({
    url: serviceConfig.auth + '/oauth_client_details/queryById/' + id,
    method: 'get'
  })
}

export function updateOauthClientDetails(data) {
  return request({
    url: serviceConfig.auth + '/oauth_client_details/edit',
    data: data,
    method: 'post'
  })
}

// 修改应用密码
export function updatePwd(data) {
  return request({
    url: serviceConfig.auth + '/oauth_client_details/edit_pwd',
    data: data,
    method: 'post'
  })
}

export function deleteOauthClientDetails(id) {
  return request({
    url: serviceConfig.auth + '/oauth_client_details/delete/' + id,
    method: 'delete'
  })
}
