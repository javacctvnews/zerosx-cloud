import request from '@/utils/request'
import serviceConfig from '@/api/serviceConfig'
// 查询部门列表
export function listDept(query) {
  return request({
    url: serviceConfig.system + '/sys_dept/page_list',
    method: 'post',
    data: query
  })
}

// 查询部门列表（排除节点）
export function listDeptExcludeChild(deptId) {
  return request({
    url: serviceConfig.system + '/system/dept/list/exclude/' + deptId,
    method: 'get'
  })
}

// 查询部门详细
export function getDept(deptId) {
  return request({
    url: serviceConfig.system + '/sys_dept/queryById/' + deptId,
    method: 'get'
  })
}

// 新增部门
export function addDept(data) {
  return request({
    url: serviceConfig.system + '/sys_dept/save',
    method: 'post',
    data: data
  })
}

// 修改部门
export function updateDept(data) {
  return request({
    url: serviceConfig.system + '/sys_dept/update',
    method: 'post',
    data: data
  })
}

// 删除部门
export function delDept(deptId) {
  return request({
    url: serviceConfig.system + '/sys_dept/delete/' + deptId,
    method: 'delete'
  })
}