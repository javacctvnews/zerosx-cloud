import request from '@/utils/request'
import serviceConfig from '@/api/serviceConfig'

// 查询操作日志列表
export function list(query) {
  return request({
    url: serviceConfig.system + '/system_operator_log/page_list',
    method: 'post',
    data: query
  })
}

// 删除操作日志
export function delOperlog(operId) {
  return request({
    url: serviceConfig.system + '/system_operator_log/delete/' + operId,
    method: 'delete'
  })
}
// 删除操作日志
export function queryById(operId) {
  return request({
    url: serviceConfig.system + '/system_operator_log/queryById/' + operId,
    method: 'get'
  })
}

// 清空操作日志
export function cleanOperlog() {
  return request({
    url: serviceConfig.system + '/system_operator_log/clean',
    method: 'post'
  })
}

