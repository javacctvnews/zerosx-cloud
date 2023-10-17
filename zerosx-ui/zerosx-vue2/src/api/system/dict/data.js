import request from '@/utils/request'
import serviceConfig from '../../serviceConfig'

// 查询字典数据列表
export function listData(query) {
  return request({
    url: serviceConfig.resource + '/sysDictData_page',
    method: 'post',
    data: query
  })
}

// 查询字典数据详细
export function getData(dictCode) {
  return request({
    url: serviceConfig.resource + '/getDictById/' + dictCode,
    method: 'get'
  })
}

// 根据字典类型查询字典数据信息
export function getDicts(dictType) {
  return request({
    url: serviceConfig.resource + '/sysDictData_selectList/' + dictType,
    method: 'get'
  })
}

// 新增字典数据
export function addData(data) {
  return request({
    url: serviceConfig.resource + '/sysDictData_insert',
    method: 'post',
    data: data
  })
}

// 修改字典数据
export function updateData(data) {
  return request({
    url: serviceConfig.resource + '/sysDictData_update',
    method: 'put',
    data: data
  })
}

// 删除字典数据
export function delData(dictCode) {
  return request({
    url: serviceConfig.resource + '/sysDictData_delete/' + dictCode,
    method: 'delete'
  })
}
