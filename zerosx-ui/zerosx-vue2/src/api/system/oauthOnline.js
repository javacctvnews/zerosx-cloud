import request from '@/utils/request'
import serviceConfig from '@/api/serviceConfig'

// 分页查询
export function pageList(query) {
  return request({
    url: serviceConfig.auth + '/token/page_list',
    method: 'post',
    data: query
  })
}

// 客户端下拉框
export function getClients() {
  return request({
    url: serviceConfig.auth + '/oauth_client_details/select',
    method: 'post',
    data: {}
  })
}

// 清空所有
export function handleDeleteAll(data) {
  return request({
    url: serviceConfig.auth + '/token/clean_token_data',
    method: 'post',
    data: data
  })
}

// 强退
export function forceLogout(data) {
  return request({
    url: serviceConfig.auth + '/token/logout',
    method: 'post',
    data: data
  })
}
