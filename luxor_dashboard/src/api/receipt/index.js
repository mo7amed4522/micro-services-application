import request from '@/utils/request';

// 自动回执列表
export async function getReceiptAccountListApi(params) {
  const res = await request.get('/receiptAccount/getReceiptAccountList', { params });
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 修改自动回执
export async function updateReceiptAccountApi(data) {
  const res = await request.put('/receiptAccount/updateReceiptAccount', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 删除自动回执
export async function deleteReceiptAccountApi(params) {
  const res = await request.delete('/receiptAccount/deleteReceiptAccount', { params });
  if (res.data.code === 0) {
    return res.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 新增自动回执
export async function createReceiptAccountApi(data) {
  const res = await request.post('/receiptAccount/createReceiptAccount', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 回执配置列表
export async function getReceiptConfigListApi(params) {
  const res = await request.get('/receiptConfig/getReceiptConfigList', { params });
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 修改回执配置
export async function updateReceiptConfigApi(data) {
  const res = await request.put('/receiptConfig/updateReceiptConfig', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 删除回执配置
export async function deleteReceiptConfigApi(params) {
  const res = await request.delete('/receiptConfig/deleteReceiptConfig', { params });
  if (res.data.code === 0) {
    return res.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 新增回执配置
export async function createReceiptConfigApi(data) {
  const res = await request.post('/receiptConfig/createReceiptConfig', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 回执订单记录列表
export async function getReceiptOrderReqListApi(params) {
  const res = await request.get('/receiptOrderReq/getReceiptOrderReqList', { params });
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 新增回执订单记录
export async function createReceiptOrderReqApi(data) {
  const res = await request.post('/receiptOrderReq/createReceiptOrderReq', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}


// 代理配置
// 代理配置列表页
export async function getReceiptProxyListApi(params) {
  const res = await request.get('/receiptProxy/getReceiptProxyList', { params });
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 新增代理配置
export async function createReceiptProxyApi(data) {
  const res = await request.post('/receiptProxy/createReceiptProxy', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 删除代理配置
export async function deleteReceiptProxyApi(params) {
  const res = await request.delete('/receiptProxy/deleteReceiptProxy', { params });
  if (res.data.code === 0) {
    return res.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 修改代理配置
export async function updateReceiptProxyApi(data) {
  const res = await request.put('/receiptProxy/updateReceiptProxy', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 回执日志列表
export async function getReceiptLogsListApi(params) {
  const res = await request.get('/receiptLogs/getReceiptLogsList', { params });
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 查看代理详情
export async function findReceiptProxyApi(params) {
  const res = await request.get('/receiptProxy/findReceiptProxy', { params });
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}
