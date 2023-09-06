import request from '@/utils/request';
import serviceConfig from '@/api/serviceConfig'

export function pageList(query) {
  return request({
    url: serviceConfig.system + '/oss_file/list_pages',
    method: 'post',
    data: query
  })
}

export function fullDelete(ids) {
  return request({
    url: serviceConfig.system + '/oss_file/full_delete/' + ids,
    method: 'get'
  })
}