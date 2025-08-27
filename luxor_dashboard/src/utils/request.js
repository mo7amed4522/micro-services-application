/**
 * axios实例
 */
import { LAYOUT_PATH } from '@/config/setting';
import router from '@/router';
import axios from 'axios';
import { ElMessageBox } from 'element-plus/es';
import { unref } from 'vue';
import { logout, toURLSearch } from './common';
import { getToken, setToken } from './token-util';

/** Create an Axios instance */
const service = axios.create({
  baseURL: '/api',
  // 超时时间60s
  timeout: 60000,
});
/**
 * Adding a request interceptor
 */

service.interceptors.request.use(
  (config) => {
    const excludedRoutes = ['/admin/captcha'];
    // Check if the currently requested URL is in the exclusion list
    const isExcluded = excludedRoutes.some((route) => config.url.includes(route));

    if (!isExcluded) {
      // If not in the exclusion list, add the header
      const token = getToken();
      if (token) {
        config.headers['x-token'] = getToken();
      }
    }

    // Get request processing array and object type parameters
    if (config.method === 'get' && config.params) {
      config.url = toURLSearch(config.params, config.url);
      config.params = {};
    }
    return config;
  },
  (error) => {
    console.error(error);
    return Promise.reject(new Error('Network Error'));
  }
);

/**
 * Adding a response interceptor
 */
service.interceptors.response.use(
  (res) => {
    // Login expiration processing
    if (res.data?.code === 401) {
      handleUnauthorized();
      return Promise.reject(new Error(res.data.message));
    }
    // 续期token
    const newToken = res.headers['token'];
    if (newToken) {
      setToken(newToken);
    }
    return res;
  },
  (error) => {
    if (error.status === 401) {
      handleUnauthorized()
    }
    // Interface timeout processing
    if (error.code === 'ECONNABORTED' || error.message.includes('timeout')) {
      console.log('Interface timeout Interface timeout Interface timeout Interface timeout Interface timeout Interface timeout Interface timeout Interface timeout');
      return Promise.reject(new Error('The request timed out, please try again later'));
    }
    return Promise.reject(new Error('Network Error'));
  }
);

const handleUnauthorized = () => {
  const { path, fullPath } = unref(router.currentRoute);
  if (path == LAYOUT_PATH) {
    logout(true, void 0, router.push);
  } else if (path !== '/login') {
    ElMessageBox.close();
    ElMessageBox.alert('The login status has expired, please log out and log in again!', 'System prompts', {
      confirmButtonText: 'Log in again',
      callback: (action) => {
        if (action === 'confirm') {
          logout(false, fullPath);
        }
      },
      type: 'warning',
      draggable: true
    });
  } else {
    logout(true, void 0, router.push);
  }
};

export default service;
