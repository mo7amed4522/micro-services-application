import request from '@/utils/request';
import { setToken, tokenGenerate } from '@/utils/token-util';

/**
 * 登录
 */
export async function login(data) {
  const res = await request.post('/base/login', data);
  if (res.data.code === 0) {
    setToken(res.data.data.token, 1);
    localStorage.setItem('userInfo', JSON.stringify(res.data.data.user));
    return res.data.msg;
  }

  return Promise.reject(new Error(res.data.msg));
}

/**
 * 获取验证码
 */
export async function getCaptcha() {
  let timestamp = Date.now().toString();
  let token = tokenGenerate(timestamp);
  const res = await request.get('/admin/captcha', {
    responseType: 'blob',
    params: {
      timestamp: timestamp,
      token: token
    }
  });
  if (res.status === 200) {
    return res.data;
  }
  return Promise.reject(new Error('Network error or request not sent'));
}

export async function getSessionId() {
  const res = await request.get('/admin/token');
  if (res.data.code === 200 && res.data.data) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.message));
}
