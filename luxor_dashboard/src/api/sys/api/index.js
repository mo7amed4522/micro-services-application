import request from '@/utils/request';

// @Tags api
// @Summary 分页获取角色列表
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body modelInterface.PageInfo true "分页获取用户列表"
// @Success 200 {string} json "{"success":true,"data":{},"msg":"获取成功"}"
// @Router /api/getApiList [post]
// {
//  page     int
//	pageSize int
// }
export async function getApiList(data) {
  const res = await request.post('/api/getApiList', data);
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// @Tags Api
// @Summary 创建基础api
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body api.CreateApiParams true "创建api"
// @Success 200 {string} json "{"success":true,"data":{},"msg":"获取成功"}"
// @Router /api/createApi [post]
export async function createApi(data) {
  const res = await request.post('/api/createApi', data);
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// @Tags menu
// @Summary 根据id获取菜单
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body api.GetById true "根据id获取菜单"
// @Success 200 {string} json "{"success":true,"data":{},"msg":"获取成功"}"
// @Router /menu/getApiById [post]
export async function getApiById(data) {
  const res = await request.post('/api/getApiById', data);
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// @Tags Api
// @Summary 更新api
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body api.CreateApiParams true "更新api"
// @Success 200 {string} json "{"success":true,"data":{},"msg":"更新成功"}"
// @Router /api/updateApi [post]
export async function updateApi(data) {
  const res = await request.post('/api/updateApi', data);
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// @Tags Api
// @Summary 更新api
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body api.CreateApiParams true "更新api"
// @Success 200 {string} json "{"success":true,"data":{},"msg":"更新成功"}"
// @Router /api/setAuthApi [post]
export async function setAuthApi(data) {
  const res = await request.post('/api/setAuthApi', data);
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// @Tags Api
// @Summary 获取所有的Api 不分页
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Success 200 {string} json "{"success":true,"data":{},"msg":"获取成功"}"
// @Router /api/getAllApis [post]
export async function getAllApis(data) {
  const res = await request.post('/api/getAllApis', data);
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// @Tags Api
// @Summary 删除指定api
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body dbModel.Api true "删除api"
// @Success 200 {string} json "{"success":true,"data":{},"msg":"删除成功"}"
// @Router /api/deleteApi [post]
export async function deleteApi(data) {
  const res = await request.post('/api/deleteApi', data);
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// @Tags SysApi
// @Summary 删除选中Api
// @Security ApiKeyAuth
// @accept application/json
// @Produce application/json
// @Param data body request.IdsReq true "ID"
// @Success 200 {string} string "{"success":true,"data":{},"msg":"删除成功"}"
// @Router /api/deleteApisByIds [delete]
export async function deleteApisByIds(data) {
  const res = await request.post('/api/deleteApisByIds', data);
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// FreshCasbin
// @Tags      SysApi
// @Summary   刷新casbin缓存
// @accept    application/json
// @Produce   application/json
// @Success   200   {object}  response.Response{msg=string}  "刷新成功"
// @Router    /api/freshCasbin [get]
export async function freshCasbin(params) {
  const res = await request.get('/api/freshCasbin', { params });
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

export async function syncApi(params) {
  const res = await request.get('/api/syncApi', { params });
  if (res.data.code === 0) {
    return res.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

export async function getApiGroups(params) {
  const res = await request.get('/api/getApiGroups', { params });
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

export async function ignoreApi(data) {
  const res = await request.post('/api/ignoreApi', data);
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

export async function enterSyncApi(data) {
  const res = await request.post('/api/enterSyncApi', data);
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}
