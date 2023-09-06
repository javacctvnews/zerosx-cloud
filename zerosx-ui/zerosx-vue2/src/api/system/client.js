import request from '@/utils/request'
import serviceConfig from '@/api/serviceConfig'

export function pageList(data) {
  return request({
    url: serviceConfig.auth + '/oauth_client_details/list_page',
    data: data,
    method: 'post'
  })
}
