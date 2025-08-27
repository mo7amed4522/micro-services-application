import request from '@/utils/request';

export async function createApp(data) {
  const res = await request.post('/appManage/createApp', data);
  if (res.data.code === 0 && res.data.data) {
    return res.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 修改
export async function updateApp(data) {
  const res = await request.put('/appManage/updateApp', data);
  if (res.data.code === 0 && res.data.data) {
    return res.data;
  }
  return Promise.reject(new Error(res.data.msg));
}
