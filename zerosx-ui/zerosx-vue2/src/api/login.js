import request from '@/utils/request'
import { obj2formUrlEncoded } from '@/utils'
import serviceConfig from './serviceConfig'

// 登录方法
export function login(username, password, code, uuid) {
  return request({
    url: '/auth/login',
    headers: {
      isToken: false
    },
    method: 'post',
    data: { username, password, code, uuid }
  })
}

// 注册方法
export function register(data) {
  return request({
    url: '/auth/register',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}

// 获取用户详细信息
export function getInfo() {
  return request({
    url: serviceConfig.system + '/sys_user/getInfo',
    method: 'get'
  })
}

// 获取验证码
export function getCodeImg() {
  return request({
    url: serviceConfig.auth + '/auth/getImgCode',
    headers: {
      isToken: false
    },
    method: 'get',
    timeout: 20000
  })
}

//获取短信验证码

export function smsCode(data) {
  return request({
    url: serviceConfig.auth + '/auth/getSmsCode',
    headers: {
      isToken: false
    },
    method: 'post',
    timeout: 20000,
    data: data
  })
}

// 刷新方法
export function refreshToken() {
  return request({
    url: '/auth/refresh',
    method: 'post'
  })
}

// 退出方法
export function logout() {
  return request({
    url: serviceConfig.auth + '/token/logout',
    method: 'post',
    data: {}
  })
}


export function requestPostLogin(userInfo) {
  let clientContent = serviceConfig.clientId + ":" + serviceConfig.clientSecret;
  const client_base64 = window.btoa(clientContent)
  userInfo.user_auth_type = serviceConfig.authUserType;
  return request({
    url: '/oauth/token' + obj2formUrlEncoded(userInfo),
    headers: {
      Authorization: "Basic " + client_base64
    },
    method: 'post',
    timeout: 20000,
    data: {}
  })
}