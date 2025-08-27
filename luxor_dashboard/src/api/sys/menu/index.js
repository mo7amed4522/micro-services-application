import request from '@/utils/request';

/**
 * 分页查询菜单
 */
export async function pageMenus(params) {
  const res = await request.get('/system/menu/page', { params });
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.message));
}

/**
 * 获取menu列表
 */
export async function listMenus(data) {
  const res = await request.post('/menu/getMenuList', data);
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

/**
 * 获取menu列表
 */
export async function getMenuList(data) {
  const res = await request.post('/menu/getMenuList', data);
  if (res.data.code === 0 && res.data.data) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

/**
 * 新增基础menu
 */
export async function addBaseMenu(data) {
  const res = await request.post('/menu/addBaseMenu', data);
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

/**
 * 获取基础路由列表
 */
export async function getBaseMenuTree(data) {
  const res = await request.post('/menu/getBaseMenuTree', data);
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

/**
 * 添加用户menu关联关系
 */
export async function addMenuAuthority(data) {
  const res = await request.post('/menu/addMenuAuthority', data);
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

/**
 * 获取用户menu关联关系
 */
export async function getMenuAuthority(data) {
  const res = await request.post('/menu/getMenuAuthority', data);
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

/**
 * 修改menu列表
 */
export async function updateBaseMenu(data) {
  const res = await request.post('/menu/updateBaseMenu', data);
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

/**
 * 根据id获取菜单
 */
export async function getBaseMenuById(data) {
  const res = await request.post('/menu/getBaseMenuById', data);
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// @Summary 删除menu
// @Produce  application/json
// @Param ID float64
// @Router /menu/deleteBaseMenu [post]
export async function deleteBaseMenu(data) {
  const res = await request.post('/menu/deleteBaseMenu', data);
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

/**
 * 修改menu列表
 */
export async function canRemoveAuthorityBtnApi(data) {
  const res = await request.post('/authorityBtn/canRemoveAuthorityBtn', data);
  if (res.data.code === 0) {
    return res.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

/**
 * 添加菜单
 */
export async function addMenu(data) {
  const res = await request.post('/system/menu', data);
  if (res.data.code === 0) {
    return res.data.message;
  }
  return Promise.reject(new Error(res.data.message));
}

/**
 * 修改菜单
 */
export async function updateMenu(data) {
  const res = await request.put('/system/menu', data);
  if (res.data.code === 0) {
    return res.data.message;
  }
  return Promise.reject(new Error(res.data.message));
}

/**
 * 删除菜单
 */
export async function removeMenu(id) {
  const res = await request.delete('/system/menu/' + id);
  if (res.data.code === 0) {
    return res.data.message;
  }
  return Promise.reject(new Error(res.data.message));
}

/**
 * 获取所有的Api 不分页
 */
export async function getAllApis(data) {
  const res = await request.post('/api/getAllApis', data);
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}
