import request from '@/utils/request';

export async function getAuthorityBtnApi(data) {
  const res = await request.post('/authorityBtn/getAuthorityBtn', data);
  if (res.data.code === 0 && res.data.data) {
    return res.data.data.list;
  }
  return Promise.reject(new Error(res.data.msg));
}

export async function setAuthorityBtnApi(data) {
  const res = await request.post('/authorityBtn/setAuthorityBtn', data);
  if (res.data.code === 0 && res.data.data) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

export async function canRemoveAuthorityBtnApi(data) {
  const res = await request.post('/authorityBtn/canRemoveAuthorityBtn', data);
  if (res.data.code === 0 && res.data.data) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}
