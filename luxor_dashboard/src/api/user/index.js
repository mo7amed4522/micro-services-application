import request from '@/utils/request';

// @Tags User
// @Summary 分页获取用户列表
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body modelInterface.PageInfo true "分页获取用户列表"
// @Success 200 {string} json "{"success":true,"data":{},"msg":"获取成功"}"
// @Router /user/getUserList [post]
export async function getUserList(data) {
  const res = await request.post('/user/getUserList', data);
  if (res.data.code === 0 && res.data.data) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// @Tags User
// @Summary 设置用户权限
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body api.setUserAuthorities true "设置用户权限"
// @Success 200 {string} json "{"success":true,"data":{},"msg":"修改成功"}"
// @Router /user/setUserAuthorities [post]
export async function setUserAuthorities(data) {
  const res = await request.post('/user/setUserAuthorities', data);
  if (res.data.code === 0 && res.data.data) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

export async function register(data) {
  const res = await request.post('/user/admin_register', data);
  if (res.data.code === 0 && res.data.data) {
    return res.data;
  }
  return Promise.reject(new Error(res.data));
}

export async function deleteUser(data) {
  const res = await request.delete('/user/deleteUser', { data });
  if (res.data.code === 0 && res.data.data) {
    return res.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

export async function setUserInfo(data) {
  const res = await request.put('/user/setUserInfo', data);
  if (res.data.code === 0 && res.data.data) {
    return res.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

export async function resetPassword(data) {
  const res = await request.post('/user/resetPassword', data);
  if (res.data.code === 0 && res.data.data) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

export async function getAuthorityList(data) {
  const res = await request.post('/authority/getAuthorityList', data);
  if (res.data.code === 0 && res.data.data) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 修改密码
export async function updatePasswordApi(data) {
  const res = await request.post('/user/updatePassword', data);
  if (res.data.code === 0 && res.data.data) {
    return res.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

