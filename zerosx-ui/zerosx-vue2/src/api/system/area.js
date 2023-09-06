import request from '@/utils/request'
import serviceConfig from '@/api/serviceConfig'

export function areaTree(nodeValue) {
  return request({
    url: serviceConfig.system + '/area_city_source/lazy_tree/' + nodeValue,
    method: 'get',
  })
}
export function addArea(data) {
  return request({
    url: serviceConfig.system + '/area_city_source/save',
    method: 'post',
    data: data
  })
}

export function updateArea(data) {
  return request({
    url: serviceConfig.system + '/area_city_source/update',
    method: 'post',
    data: data
  })
}

export function deleteArea(id) {
  return request({
    url: serviceConfig.system + '/area_city_source/delete/' + id,
    method: 'get'
  })
}
