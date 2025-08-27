import request from '@/utils/request';

// app列表
export async function getAppPageListApi(data) {
  const res = await request.post('/appManage/getAppPage', data);
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 新增app配置
export async function createAppApi(data) {
  const res = await request.post('/appManage/createApp', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 修改app配置
export async function updateAppApi(data) {
  const res = await request.put('/appManage/updateApp', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 删除app配置
export async function deleteAppApi(params) {
  const res = await request.delete('/appManage/deleteAppById', { params });
  if (res.data.code === 0) {
    return res.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 域名配置列表
export async function getCDominManageListApi(params) {
  const res = await request.get('/cDominManage/getCDominManageList', { params });
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 域名配置添加
export async function createCDominManageApi(data) {
  const res = await request.post('/cDominManage/createCDominManage', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 修改域名配置状态
export async function visibleCDominManageApi(data) {
  const res = await request.put('/cDominManage/visibleCDominManage', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 创建子域名
export async function createCSubDominManageApi(data) {
  const res = await request.post('/cSubDominManage/createCSubDominManage', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 创建模版
export async function createCPageTemplateApi(data) {
  const res = await request.post('/cPageTemplate/createCPageTemplate', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 更新模版
export async function updateCPageTemplateApi(data) {
  const res = await request.put('/cPageTemplate/updateCPageTemplate', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 删除模版
export async function deleteCPageTemplateApi(params) {
  const res = await request.delete('/cPageTemplate/deleteCPageTemplate', { params });
  if (res.data.code === 0) {
    return res.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 模版列表
export async function getCPageTemplateListApi(params) {
  const res = await request.get('/cPageTemplate/getCPageTemplateList', { params });
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 添加推广链接
export async function createCPromotionLinkApi(data) {
  const res = await request.post('/cPromotionLink/createCPromotionLink', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 修改推广链接
export async function updateCPromotionLinkApi(data) {
  const res = await request.put('/cPromotionLink/updateCPromotionLink', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 删除推广链接
export async function deleteCPromotionLinkApi(params) {
  const res = await request.delete('/cPromotionLink/deleteCPromotionLink', { params });
  if (res.data.code === 0) {
    return res.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 推广链接列表
export async function getCPromotionLinkListApi(params) {
  const res = await request.get('/cPromotionLink/getCPromotionLinkList', { params });
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}