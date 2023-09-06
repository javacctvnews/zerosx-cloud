import request from '@/utils/request'
import serviceConfig from '@/api/serviceConfig'

// 查询字典类型列表
export function listType(query) {
  return request({
    url: serviceConfig.system + '/sysDictType_page',
    method: 'post',
    data: query
  })
}

// 查询字典类型详细
export function getType(dictId) {
  return request({
    url: serviceConfig.system + '/getDictTypeById/' + dictId,
    method: 'get'
  })
}

// 新增字典类型
export function addType(data) {
  return request({
    url: serviceConfig.system + '/sysDictType_insert',
    method: 'post',
    data: data
  })
}

// 修改字典类型
export function updateType(data) {
  return request({
    url: serviceConfig.system + '/sysDictType_update',
    method: 'put',
    data: data
  })
}

// 删除字典类型
export function delType(dictId) {
  return request({
    url: serviceConfig.system + '/sysDictType_delete/' + dictId,
    method: 'delete'
  })
}

// 刷新字典缓存
export function refreshCache() {
  return request({
    url: serviceConfig.system + '/sysDictData/init',
    method: 'get'
  })
}

// 获取字典选择框列表
export function optionselect() {
  return request({
    url: serviceConfig.system + '/sysDictType_list',
    method: 'post',
    data:{}
  })
}