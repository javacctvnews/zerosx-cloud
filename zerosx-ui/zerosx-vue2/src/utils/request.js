import axios from 'axios'
import { Notification, MessageBox, Message, Loading } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'
import errorCode from '@/utils/errorCode'
import { tansParams, blobValidate } from "@/utils/ruoyi";
import cache from '@/plugins/cache'
import { saveAs } from 'file-saver'


let downloadLoadingInstance;
// 是否显示重新登录
export let isRelogin = { show: false };

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'
// 创建axios实例
const service = axios.create({
  // axios中请求配置有baseURL选项，表示请求URL公共部分
  baseURL: process.env.VUE_APP_BASE_API,
  // 超时
  timeout: 30000
})

// request拦截器
service.interceptors.request.use(config => {
  // 是否需要设置 token
  const isToken = (config.headers || {}).isToken === false
  // 是否需要防止数据重复提交
  const isRepeatSubmit = (config.headers || {}).repeatSubmit === false
  if (getToken() && !isToken) {
    config.headers['Authorization'] = 'Bearer ' + getToken() // 让每个请求携带自定义token 请根据实际情况自行修改
  }
  // get请求映射params参数
  if (config.method === 'get' && config.params) {
    let url = config.url + '?' + tansParams(config.params);
    url = url.slice(0, -1);
    config.params = {};
    config.url = url;
  }
  if (!isRepeatSubmit && (config.method === 'post' || config.method === 'put')) {
    const requestObj = {
      url: config.url,
      data: typeof config.data === 'object' ? JSON.stringify(config.data) : config.data,
      time: new Date().getTime()
    }
    const sessionObj = cache.session.getJSON('sessionObj')
    if (sessionObj === undefined || sessionObj === null || sessionObj === '') {
      cache.session.setJSON('sessionObj', requestObj)
    } else {
      const s_url = sessionObj.url;                  // 请求地址
      const s_data = sessionObj.data;                // 请求数据
      const s_time = sessionObj.time;                // 请求时间
      const interval = 500;                         // 间隔时间(ms)，小于此时间视为重复提交
      if (s_data === requestObj.data && requestObj.time - s_time < interval && s_url === requestObj.url) {
        const message = '操作过于频繁或数据正在处理，请勿重复提交';
        console.warn(`[${s_url}]: ` + message)
        return Promise.reject(new Error(message))
      } else {
        cache.session.setJSON('sessionObj', requestObj)
      }
    }
  }
  return config
}, error => {
  console.log(error)
  Promise.reject(error)
})

// 响应拦截器
service.interceptors.response.use(res => {
  // 未设置状态码则默认成功状态
  const code = res.data.code || 0;
  // 获取错误信息
  const msg = res.data.msg || errorCode[code] || errorCode['default']
  //console.log(code, msg);
  // 二进制数据则直接返回
  if (res.request.responseType === 'blob' || res.request.responseType === 'arraybuffer') {
    return res.data
  }
  //非0表示有异常
  if (code !== 0) {
    Notification.error({ title: msg })
    return Promise.reject('error')
  } else {
    return res.data
  }
},
  error => {
    console.log(error)
    if (error.response && error.response.data) {
      let errData = error.response.data;
      console.log('errData', errData)
      const respCode = errData.code || 500;
      if (respCode === 401) {
        if (!isRelogin.show) {
          isRelogin.show = true;
          MessageBox.confirm('登录状态已过期，您可以继续留在该页面，或者重新登录', '系统提示', { confirmButtonText: '重新登录', cancelButtonText: '取消', type: 'warning' }).then(() => {
            isRelogin.show = false;
            store.dispatch('LogOut').then(() => {
              location.href = '/index';
            })
          }).catch(() => {
            isRelogin.show = false;
          });
        }
        return Promise.reject('无效的会话，或者会话已过期，请重新登录。')
      } else {
        if (errData.msg) {
          Message({ message: errData.msg, type: 'error', duration: 5 * 1000 })
          return Promise.reject('error')
        } else {
          let { msg } = error;
          console.log('msg: ', msg)
          if (msg == "Network Error") {
            msg = "后端接口连接异常";
          } else if (msg.includes("timeout")) {
            msg = "系统接口请求超时";
          } else if (msg.includes("Request failed with status code")) {
            msg = "系统接口" + msg.substr(msg.length - 3) + "异常";
          }
          Message({ message: msg, type: 'error', duration: 5 * 1000 })
          return Promise.reject(error)
        }
      }
    } else {
      console.log(error)
      let { message } = error;
      if (message == "Network Error") {
        message = "后端接口连接异常";
      } else if (message.includes("timeout")) {
        message = "系统接口请求超时";
      } else if (message.includes("Request failed with status code")) {
        message = "系统接口" + message.substr(message.length - 3) + "异常";
      }
      Message({ message: message, type: 'error', duration: 3 * 1000 })
      return Promise.reject(error)
    }

  }
)

// 通用下载方法
export function download(url, params, filename, config) {
  console.log(params)
  downloadLoadingInstance = Loading.service({ text: "正在下载数据，请稍候", spinner: "el-icon-loading", background: "rgba(0, 0, 0, 0.7)", })
  return service.post(url, params, {
    data: params,
    //transformRequest: [(params) => { return tansParams(params) }],
    //headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    headers: { 'Content-Type': 'application/json' },
    responseType: 'blob',
    ...config
  }).then(async (data) => {
    const isBlob = blobValidate(data);
    if (isBlob) {
      const blob = new Blob([data])
      saveAs(blob, filename)
    } else {
      const resText = await data.text();
      const rspObj = JSON.parse(resText);
      const errMsg = errorCode[rspObj.code] || rspObj.msg || errorCode['default']
      Message.error(errMsg);
    }
    downloadLoadingInstance.close();
  }).catch((r) => {
    console.error(r)
    Message.error('下载文件出现错误，请联系管理员！')
    downloadLoadingInstance.close();
  })
}

export default service
