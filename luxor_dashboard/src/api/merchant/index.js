import request from '@/utils/request';

// 商户列表
export async function getMUserList(params) {
  const res = await request.get('/mUser/getMUserList', { params });
  if (res.data.code === 0 && res.data.data) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 新增商户
export async function addMUserApi(data) {
  const res = await request.post('/mUser/createMUser', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 修改商户
export async function updateMUserApi(data) {
  const res = await request.put('/mUser/updateMUser', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 删除商户
export async function deleteMUserApi(params) {
  const res = await request.delete('/mUser/deleteMUser', { params });
  if (res.data.code === 0) {
    return res.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 商户标签列表
export async function getMUserLabelListApi(params) {
  const res = await request.get('/mUserLabel/getMUserLabelList', { params });
  if (res.data.code === 0 && res.data.data) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 新增商户标签
export async function createMUserLabelApi(data) {
  const res = await request.post('/mUserLabel/createMUserLabel', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 修改商户标签
export async function updateMUserLabelApi(data) {
  const res = await request.put('/mUserLabel/updateMUserLabel', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 删除商户标签
export async function deleteMUserLabelApi(params) {
  const res = await request.delete('/mUserLabel/deleteMUserLabel', { params });
  if (res.data.code === 0) {
    return res.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 商户钱包列表
export async function getMUserWalletListApi(params) {
  const res = await request.get('/mUserWallet/getMUserWalletList', { params });
  if (res.data.code === 0 && res.data.data) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 新增商户钱包
export async function createMUserWalletApi(data) {
  const res = await request.post('/mUserWallet/createMUserWallet', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 修改商户钱包
export async function updateMUserWalletApi(data) {
  const res = await request.put('/mUserWallet/updateMUserWallet', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 删除商户钱包
export async function deleteMUserWalletApi(params) {
  const res = await request.delete('/mUserWallet/deleteMUserWallet', { params });
  if (res.data.code === 0) {
    return res.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 收款方式列表
export async function getCPayPoolAccountListApi(params) {
  const res = await request.get('/cPayPoolAccount/getCPayPoolAccountList', { params });
  if (res.data.code === 0 && res.data.data) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 新增收款方式
export async function createCPayPoolAccountApi(data) {
  const res = await request.post('/cPayPoolAccount/createCPayPoolAccount', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 修改收款方式
export async function updateCPayPoolAccountApi(data) {
  const res = await request.put('/cPayPoolAccount/updateCPayPoolAccount', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 删除收款方式
export async function deleteCPayPoolAccountApi(params) {
  const res = await request.delete('/cPayPoolAccount/deleteCPayPoolAccount', { params });
  if (res.data.code === 0) {
    return res.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 商户支付池列表
export async function getCPayPoolListApi(params) {
  const res = await request.get('/cPayPool/getCPayPoolList', { params });
  if (res.data.code === 0 && res.data.data) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 新增商户支付池
export async function createCPayPoolApi(data) {
  const res = await request.post('/cPayPool/createCPayPool', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 修改商户支付池
export async function updateCPayPoolApi(data) {
  const res = await request.put('/cPayPool/updateCPayPool', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 删除商户支付池
export async function deleteCPayPoolApi(params) {
  const res = await request.delete('/cPayPool/deleteCPayPool', { params });
  if (res.data.code === 0) {
    return res.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 商户支付池配置列表
export async function getCPayPoolUserListApi(params) {
  const res = await request.get('/cPayPoolUser/getCPayPoolUserList', { params });
  if (res.data.code === 0 && res.data.data) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 新增商户支付池配置
export async function createCPayPoolUserApi(data) {
  const res = await request.post('/cPayPoolUser/createCPayPoolUser', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 修改商户支付池配置
export async function updateCPayPoolUserApi(data) {
  const res = await request.put('/cPayPoolUser/updateCPayPoolUser', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 删除商户支付池配置
export async function deleteCPayPoolUserApi(params) {
  const res = await request.delete('/cPayPoolUser/deleteCPayPoolUser', { params });
  if (res.data.code === 0) {
    return res.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 商户渠道配置列表
export async function getMPayChannelConfigListApi(params) {
  const res = await request.get('/mPayChannelConfig/getMPayChannelConfigList', { params });
  if (res.data.code === 0 && res.data.data) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 新增商户渠道配置
export async function createMPayChannelConfigApi(data) {
  const res = await request.post('/mPayChannelConfig/createMPayChannelConfig', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 修改商户渠道配置
export async function updateMPayChannelConfigApi(data) {
  const res = await request.put('/mPayChannelConfig/updateMPayChannelConfig', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 删除商户渠道配置
export async function deleteMPayChannelConfigApi(params) {
  const res = await request.delete('/mPayChannelConfig/deleteMPayChannelConfig', { params });
  if (res.data.code === 0) {
    return res.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 商户下玩家列表
export async function getOrderPayUserListApi(params) {
  const res = await request.get('/orderPayUser/getOrderPayUserList', { params });
  if (res.data.code === 0 && res.data.data) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 新增商户下玩家
export async function createOrderPayUserApi(data) {
  const res = await request.post('/orderPayUser/createOrderPayUser', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 修改商户下玩家
export async function updateOrderPayUserApi(data) {
  const res = await request.put('/orderPayUser/updateOrderPayUser', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 删除商户下玩家
export async function deleteOrderPayUserApi(params) {
  const res = await request.delete('/orderPayUser/deleteOrderPayUser', { params });
  if (res.data.code === 0) {
    return res.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 更新商户状态
export async function updateMUserStatusApi(data) {
  const res = await request.put('/mUser/updateMUserStatus', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 配置赠送方式
export async function configMUserApi(data) {
  const res = await request.post('/mUser/configMUser', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 获取当前登录商户的信息
export async function currentMUserApi(params) {
  const res = await request.get('/mUser/currentMUser', { params });
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 获取赠送方式配置
export async function getConfigMUserApi(params) {
  const res = await request.get('/mUser/getConfigMUser', { params });
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 账变操作
export async function updateAmountApi(data) {
  const res = await request.post('/cPayPoolAccount/updateAmount', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 转移支付池
export async function transferCPayPoolApi(data) {
  const res = await request.put('/cPayPoolAccount/transferCPayPool', data);
  if (res.data.code === 0) {
    return res.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 收款/出款账户列表
export async function getCPayPoolAccountListByDelList(params) {
  const res = await request.get('/cPayPoolAccount/getCPayPoolAccountListByDel', { params });
  if (res.data.code === 0 && res.data.data) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 恢复删除账户
export async function recoverCPayPoolAccountApi(params) {
  const res = await request.delete('/cPayPoolAccount/recoverCPayPoolAccount', { params });
  if (res.data.code === 0) {
    return res.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 收款账户列表
export async function getCPayPoolCardsListApi(params) {
  const res = await request.get('/cPayPoolCards/getCPayPoolCardsList', { params });
  if (res.data.code === 0 && res.data.data) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 删除收款账户
export async function deleteCPayPoolCardsApi(params) {
  const res = await request.delete('cPayPoolCards/deleteCPayPoolCards', { params });
  if (res.data.code === 0) {
    return res.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 创建收款账户
export async function createCPayPoolCardsApi(data) {
  const res = await request.post('/cPayPoolCards/createCPayPoolCards', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 修改收款账户
export async function updateCPayPoolCardsApi(data) {
  const res = await request.put('/cPayPoolCards/updateCPayPoolCards', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 收款账户-删除列表
export async function getCPayPoolCardsByDelApi(params) {
  const res = await request.get('/cPayPoolCards/getCPayPoolCardsByDel', { params });
  if (res.data.code === 0 && res.data.data) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 恢复收款账户
export async function recoverCPayPoolCardsApi(params) {
  const res = await request.delete('/cPayPoolCards/recoverCPayPoolCards', { params });
  if (res.data.code === 0) {
    return res.data;
  }
  return Promise.reject(new Error(res.data.msg));
}
