import request from '@/utils/request'
import serviceConfig from '../serviceConfig'


// 查询菜单列表
export function listMenu(query) {
  return request({
    url: serviceConfig.system + '/menu/list',
    method: 'post',
    data: query
  })
}

// 查询菜单详细
export function getMenu(menuId) {
  return request({
    url: serviceConfig.system + '/getMenuById/' + menuId,
    method: 'get'
  })
}


// 查询菜单下拉树结构
export function treeselect() {
  return request({
    url: '/system/menu/treeselect',
    method: 'get'
  })
}

// 根据角色ID查询菜单下拉树结构
export function roleMenuTreeselect(roleId) {
  return request({
    url: '/system/menu/roleMenuTreeselect/' + roleId,
    method: 'get'
  })
}

// 新增菜单
export function addMenu(data) {
  return request({
    url: serviceConfig.system + '/menu/save',
    method: 'post',
    data: data
  })
}

// 修改菜单
export function updateMenu(data) {
  return request({
    url: serviceConfig.system + '/menu/update',
    method: 'post',
    data: data
  })
}

// 删除菜单
export function delMenu(menuId) {
  return request({
    url: serviceConfig.system + '/menu/delete/' + menuId,
    method: 'delete'
  })
}
