import { removeToken } from '@/utils/token-util';

/**
 * 退出登录
 * @param route 是否使用路由跳转
 * @param from 登录后跳转的地址
 * @param push 路由跳转方法
 */
export function logout(route, from, push) {
  removeToken();
  // 删除
  localStorage.removeItem('userInfo');
  if (route && push) {
    push({
      path: '/login',
      query: from ? { from: encodeURIComponent(from) } : void 0
    });
    return;
  }
  // 这样跳转避免再次登录重复注册动态路由, hash 路由模式使用 location.reload();
  const BASE_URL = import.meta.env.BASE_URL;
  const url = BASE_URL + 'login';
  location.replace(from ? `${url}?from=${encodeURIComponent(from)}` : url);
}

/**
 * 下载文件
 * @param data 二进制数据
 * @param name 文件名
 * @param type 文件类型
 */
export function download(data, name, type) {
  const blob = new Blob([data], { type: type || 'application/octet-stream' });
  const url = window.URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = name;
  a.style.display = 'none';
  document.body.appendChild(a);
  a.click();
  document.body.removeChild(a);
  URL.revokeObjectURL(url);
}

/**
 * 参数转url字符串
 * @param params 参数
 * @param url 需要拼接参数的地址
 */
export function toURLSearch(params, url) {
  if (typeof params !== 'object' || params == null) {
    return '';
  }
  const result = transformParams(params)
    .map((d) => `${encodeURIComponent(d[0])}=${encodeURIComponent(d[1])}`)
    .join('&');
  if (!url) {
    return result;
  }
  return (url.includes('?') ? `${url}&` : `${url}?`) + result;
}

/**
 * 参数转表单数据
 * @param params 参数
 */
export function toFormData(params) {
  const formData = new FormData();
  if (typeof params !== 'object' || params == null) {
    return formData;
  }
  transformParams(params).forEach((d) => {
    formData.append(d[0], d[1]);
  });
  return formData;
}

/**
 * get请求处理数组和对象类型参数
 * @param params 参数
 */
export function transformParams(params) {
  const result = [];
  if (params != null && typeof params === 'object') {
    Object.keys(params).forEach((key) => {
      const value = params[key];
      if (value != null && value !== '') {
        if (typeof value === 'object' && !isBlobFile(value)) {
          getObjectParamsArray(value).forEach((item) => {
            result.push([`${key}${item[0]}`, item[1]]);
          });
        } else {
          result.push([key, value]);
        }
      }
    });
  }
  return result;
}

/**
 * 对象转参数数组
 * @param obj 对象
 */
export function getObjectParamsArray(obj) {
  const result = [];
  Object.keys(obj).forEach((key) => {
    const value = obj[key];
    if (value != null && value !== '') {
      const name = `[${key}]`;
      if (typeof value === 'object' && !isBlobFile(value)) {
        getObjectParamsArray(value).forEach((item) => {
          result.push([`${name}${item[0]}`, item[1]]);
        });
      } else {
        result.push([name, value]);
      }
    }
  });
  return result;
}

/**
 * 判断是否是文件
 * @param obj 对象
 */
export function isBlobFile(obj) {
  return obj != null && (obj instanceof Blob || obj instanceof File);
}

/**
 * 生成32位随机字符串
 */
export function getRandom32() {
  // 生成32字节的十六进制字符串
  const array = new Uint8Array(16);
  let arr = window.crypto.getRandomValues(array);
  return Array.from(arr)
    .map((b) => b.toString(16).padStart(2, '0'))
    .join('');
}

export function getBeijingHour() {
  const date = new Date();
  const options = { timeZone: 'Asia/Shanghai', hour: '2-digit', hour12: false };
  const dateTimeFormat = new Intl.DateTimeFormat('en-US', options);
  return dateTimeFormat.format(date); // 返回当前北京时间的小时（24小时制）
}
