import request from '@/utils/request';

// @Router /authority/getAuthorityList [post]
export async function getAuthorityList(data) {
  const res = await request.post('/authority/getAuthorityList', data);
  if (res.data.code === 0 && res.data.data) {
    return res.data.data.list;
  }
  return Promise.reject(new Error(res.data.msg));
}

// @Summary 删除角色
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body {authorityId uint} true "删除角色"
// @Success 200 {string} json "{"success":true,"data":{},"msg":"获取成功"}"
// @Router /authority/deleteAuthority [post]
export async function deleteAuthority(data) {
  const res = await request.post('/authority/deleteAuthority', data);
  if (res.data.code === 0 && res.data.data) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// @Summary 创建角色
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body api.CreateAuthorityPatams true "创建角色"
// @Success 200 {string} json "{"success":true,"data":{},"msg":"获取成功"}"
// @Router /authority/createAuthority [post]
export async function createAuthority(data) {
  const res = await request.post('/authority/createAuthority', data);
  if (res.data.code === 0 && res.data.data) {
    return res.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// @Tags authority
// @Summary 拷贝角色
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body api.CreateAuthorityPatams true "拷贝角色"
// @Success 200 {string} json "{"success":true,"data":{},"msg":"拷贝成功"}"
// @Router /authority/copyAuthority [post]
export async function copyAuthority(data) {
  const res = await request.post('/authority/copyAuthority', data);
  if (res.data.code === 0 && res.data.data) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// @Summary 设置角色资源权限
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body sysModel.SysAuthority true "设置角色资源权限"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"设置成功"}"
// @Router /authority/setDataAuthority [post]
export async function setDataAuthority(data) {
  const res = await request.post('/authority/setDataAuthority', data);
  if (res.data.code === 0 && res.data.data) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// @Summary 修改角色
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body model.SysAuthority true "修改角色"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"设置成功"}"
// @Router /authority/setDataAuthority [post]
export async function updateAuthority(data) {
  const res = await request.put('/authority/updateAuthority', data);
  if (res.data.code === 0 && res.data.data) {
    return res.data;
  }
  return Promise.reject(new Error(res.data.msg));
}
