import request from '@/utils/request';

// 支付配置标签列表
export async function getPayLabelListApi(params) {
  const res = await request.get('/payLabel/getPayLabelList', { params });
  if (res.data.code === 0 && res.data.data) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 新增支付配置标签列表
export async function createPayLabelApi(data) {
  const res = await request.post('/payLabel/createPayLabel', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 修改支付配置标签列表
export async function updatePayLabelApi(data) {
  const res = await request.put('/payLabel/updatePayLabel', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 删除支付配置标签列表
export async function deletePayLabel(params) {
  const res = await request.delete('/payLabel/deletePayLabel', { params });
  if (res.data.code === 0) {
    return res.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 支付渠道列表
export async function getCPayConfigListApi(params) {
  const res = await request.get('/cPayConfig/getCPayConfigList', { params });
  if (res.data.code === 0 && res.data.data) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 新增支付渠道
export async function createCPayConfigApi(data) {
  const res = await request.post('/cPayConfig/createCPayConfig', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 修改支付渠道
export async function updateCPayConfigApi(data) {
  const res = await request.put('/cPayConfig/updateCPayConfig', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 删除支付渠道
export async function deleteCPayConfigApi(params) {
  const res = await request.delete('/cPayConfig/deleteCPayConfig', { params });
  if (res.data.code === 0) {
    return res.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 银行字典列表
export async function getPayBankListApi(params) {
  const res = await request.get('/payBank/getPayBankList', { params });
  if (res.data.code === 0 && res.data.data) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 新增银行字典
export async function createPayBankApi(data) {
  const res = await request.post('/payBank/createPayBank', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 修改银行字典
export async function updatePayBankApi(data) {
  const res = await request.put('/payBank/updatePayBank', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 删除银行字典
export async function deletePayBankApi(params) {
  const res = await request.delete('/payBank/deletePayBank', { params });
  if (res.data.code === 0) {
    return res.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 新增支付池标签
export async function createCPayPoolLabelApi(data) {
  const res = await request.post('/cPayPoolLabel/createCPayPoolLabel', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 删除商户支付池标签
export async function deleteCPayPoolLabelApi(params) {
  const res = await request.delete('/cPayPoolLabel/deleteCPayPoolLabel', { params });
  if (res.data.code === 0) {
    return res.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 修改商户支付池标签
export async function updateCPayPoolLabelApi(data) {
  const res = await request.put('/cPayPoolLabel/updateCPayPoolLabel', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 商户支付池标签列表
export async function getCPayPoolLabelListApi(params) {
  const res = await request.get('/cPayPoolLabel/getCPayPoolLabelList', { params });
  if (res.data.code === 0 && res.data.data) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}