import request from '@/utils/request'
import serviceConfig from '@/api/serviceConfig'

// 查询登录日志列表
export function list(query) {
  return request({
    url: serviceConfig.auth + '/oauth_token_record/page_list',
    method: 'post',
    data: query
  })
}

// 删除登录日志
export function delLogininfor(infoId) {
  return request({
    url: serviceConfig.auth + '/oauth_token_record/delete/' + infoId,
    method: 'delete'
  })
}

// 清空登录日志
export function cleanLoginLog() {
  return request({
    url: serviceConfig.auth + '/oauth_token_record/delete_all',
    method: 'delete'
  })
}
