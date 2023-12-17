import request from '@/utils/request';
import serviceConfig from '@/api/serviceConfig'

export function pageList(query) {
  return request({
    url: serviceConfig.leaf + '/leaf_alloc/page_list',
    method: 'post',
    data: query
  })
}
export function addLeafAlloc(data) {
  return request({
    url: serviceConfig.leaf + '/leaf_alloc/save',
    method: 'post',
    data: data
  })
}
export function queryById(id) {
  return request({
    url: serviceConfig.leaf + '/leaf_alloc/queryById/' + id,
    method: 'get'
  })
}

export function updateLeafAlloc(data) {
  return request({
    url: serviceConfig.leaf + '/leaf_alloc/update',
    data: data,
    method: 'post'
  })
}

export function deleteLeafAlloc(id) {
  return request({
    url: serviceConfig.leaf + '/leaf_alloc/delete/' + id,
    method: 'delete'
  })
}
