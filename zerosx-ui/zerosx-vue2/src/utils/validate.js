/**
 * 正则表达式常量集合
 */
export const patternConsts = {
  //密码规则：必须包含大小写字母和数字的组合，不能使用特殊字符
  pwd: /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,20}$/,
  //手机号码
  phone: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
  //邮箱
  email: /^[A-Za-z0-9+_.-]+@(.+)$/,
}

/**
 * @param {string} path
 * @returns {Boolean}
 */
export function isExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path)
}

/**
 * @param {string} str
 * @returns {callback}
 */
export function validUsername(rule, value, callback) {
  if (!value) {
    return callback(new Error('用户账号不能为空'));
  }
  const valid_map = ['admin', 'editor']
  if (valid_map.indexOf(value.trim()) >= 0) {
    return callback(new Error('用户账号不能包含关键词：' + valid_map));
  }
  //用户名正则，4到16位（字母，数字，下划线，减号）
  const uPattern = /^[a-zA-Z0-9_-]{4,16}$/;
  if (!uPattern.test("iFat3")) {
    return callback(new Error('用户账号只能是4-16位的字母或数字或下划线或减号'));
  }
  callback();
}

/**
 * @param {string} url
 * @returns {Boolean}
 */
export function validURL(url) {
  const reg = /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/
  return reg.test(url)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validLowerCase(str) {
  const reg = /^[a-z]+$/
  return reg.test(str)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validUpperCase(str) {
  const reg = /^[A-Z]+$/
  return reg.test(str)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validAlphabets(str) {
  const reg = /^[A-Za-z]+$/
  return reg.test(str)
}

/**
 * @param {string} email
 * @returns {Boolean}
 */
export function validEmail(email) {
  const reg = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
  return reg.test(email)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function isString(str) {
  if (typeof str === 'string' || str instanceof String) {
    return true
  }
  return false
}

/**
 * @param {Array} arg
 * @returns {Boolean}
 */
export function isArray(arg) {
  if (typeof Array.isArray === 'undefined') {
    return Object.prototype.toString.call(arg) === '[object Array]'
  }
  return Array.isArray(arg)
}

// 验证长度（变量形式），长度，是否包含空格，是否包含特殊符号，是否验证全中文，是否验证全英文，是否验证全数字，是否是正整数
export function validateInput(rule, value, callback, minLen, maxLen, checkSpace, checkSign, checkCN, checkEN, checkNum, isStr, isNum) {
  setTimeout(() => {
    if (value === '' || value == null || value === 'undefined') {
      callback()
      return
    }
    // 长度
    if (value.length < minLen) {
      callback(new Error('长度不能小于' + minLen))
    }
    if (value.length > maxLen) {
      callback(new Error('长度不能大于' + maxLen))
    }
    // 验证空格
    if (checkSpace) {
      const reg = /^[^ ]+$/
      if (!reg.test(value)) {
        callback(new Error('不能包含空格'))
      }
    }
    if (checkSign) {
      const reg = /[`~!@#$%^&*()_\-+=<>?:"{}|,.\/;'\\[\]·~！@#￥%……&*（）——\-+={}|《》？：“”【】、；‘’，。、]/
      // let reg=/((?=[\x21-\x7e]+)[^A-Za-z0-9])/
      if (reg.test(value)) {
        callback(new Error('不能包含任何特殊字符'))
      }
    }
    if (checkCN) {
      const reg = /.*[\u4e00-\u9fa5]{1,}.*/
      if (reg.test(value)) {
        callback(new Error('不能包含汉字'))
      }
    }
    if (checkEN) {
      const reg = /.*[a-zA-Z]{1,}.*/
      if (reg.test(value)) {
        callback(new Error('不能包含英文'))
      }
    }
    if (checkNum) {
      const reg = /.*[0-9]{1,}.*/
      // let reg = /^-?\d*\.{0,1}\d{0,6}$/
      if (reg.test(value)) {
        callback(new Error('不能包含数字'))
      }
    }
    if (isStr) {
      // 字母数字下划线
      const reg = /^[\u4e00-\u9fa5a-zA-Z0-9_-]+$/
      if (!reg.test(value)) {
        callback(new Error('必须是中英文,数字,或"_","-"'))
      }
    }
    if (isNum) {
      // 数字
      const reg = /^\d+$|^\d+[.]?\d{0,6}$/
      if (!reg.test(value)) {
        callback(new Error('必须数字，且小数位最多为6'))
      }
    }
    callback()
  }, 100)
}