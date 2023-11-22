// 各个服务的服务名，作用：gateway根据url前缀匹配转发到各个微服务
const serviceConfig = {

  //微服务前缀
  system: '/api-system',
  auth: '/api-sas',
  resource: '/api-resource',
  //客户端授权
  clientId: 'saas',
  clientSecret: 'Zeros9999!#@',
  grantType: 'captcha',
  captcha: "captcha_pwd",
  mobileSms: 'sms',
  authUserType: 'SysUser'

}

export default serviceConfig