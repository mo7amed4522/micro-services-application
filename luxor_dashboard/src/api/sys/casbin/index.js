import request from '@/utils/request';

// @Tags authority
// @Summary 更改角色api权限
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body api.CreateAuthorityPatams true "更改角色api权限"
// @Success 200 {string} json "{"success":true,"data":{},"msg":"获取成功"}"
// @Router /casbin/UpdateCasbin [post]
export async function UpdateCasbin(data) {
  const res = await request.post('/casbin/updateCasbin', data);
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.message));
}

// @Tags casbin
// @Summary 获取权限列表
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body api.CreateAuthorityPatams true "获取权限列表"
// @Success 200 {string} json "{"success":true,"data":{},"msg":"获取成功"}"
// @Router /casbin/getPolicyPathByAuthorityId [post]
export async function getPolicyPathByAuthorityId(data) {
  const res = await request.post('/casbin/getPolicyPathByAuthorityId', data);
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.message));
}
