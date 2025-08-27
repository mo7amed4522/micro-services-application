import request from '@/utils/request';

// 订单列表
export async function getOrderPayListApi(params) {
  const res = await request.get('/orderPay/getOrderPayList', { params });
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 订单操作日志列表
export async function getOrderPayOperLogListApi(params) {
  const res = await request.get('/orderPayOperLog/getOrderPayOperLogList', { params });
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 订单列表-玩家信息
export async function getOrderPayPlayerListApi(params) {
  const res = await request.get('/orderPayPlayer/getOrderPayPlayerList', { params });
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 订单列表-请求报文
export async function getOrderPayRequestListApi(params) {
  const res = await request.get('/orderPayRequest/getOrderPayRequestList', { params });
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 订单列表-冲正记录
export async function getOrderPayReversalListApi(params) {
  const res = await request.get('/orderPayReversal/getOrderPayReversalList', { params });
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 订单列表-用户信息
export async function getOrderPayUserListApi(params) {
  const res = await request.get('/orderPayUser/getOrderPayUserList', { params });
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 提款订单列表
export async function getOrderPayoutListApi(params) {
  const res = await request.get('/orderPayout/getOrderPayoutList', { params });
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 提款订单操作日志列表
export async function getOrderPayoutOperLogListApi(params) {
  const res = await request.get('/orderPayoutOperLog/getOrderPayoutOperLogList', { params });
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 提款订单-玩家信息
export async function getOrderPayoutPlayerListApi(params) {
  const res = await request.get('/orderPayoutPlayer/getOrderPayoutPlayerList', { params });
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 提款订单-请求报文
export async function getOrderPayoutRequestListApi(params) {
  const res = await request.get('/orderPayoutRequest/getOrderPayoutRequestList', { params });
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 提款订单-用户信息
export async function getOrderPayoutUserListApi(params) {
  const res = await request.get('/orderPayoutUser/getOrderPayoutUserList', { params });
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 下分订单分页列表
export async function getPayoutListApi(params) {
  const res = await request.get('/order/payoutList', { params });
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 手动推送第三方接口并更新
export async function payoutManualApi(data) {
  const res = await request.post('/order/payout/manual', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 渠道及支付池下拉列表
export async function getConfigAndPayPoolListApi(params) {
  const res = await request.get('/orderPayReversal/getConfigAndPayPoolList', { params });
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 根据支付池id获取下面所有收款方式卡号 cPayPoolId
export async function getPayPoolAccountListApi(params) {
  const res = await request.get('/orderPayReversal/GetPayPoolAccountList', { params });
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 关闭订单
export async function closeStatusApi(data) {
  const res = await request.put('/orderPayReversal/closeStatus', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// utr补单
export async function createOrderPayReversalApi(data) {
  const res = await request.post('/orderPayReversal/createOrderPayReversal', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 出款审核
export async function payoutOrderResetApi(data) {
  const res = await request.put('/orderPayout/orderReset', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 出款补单
export async function replenishmentPayoutOrderApi(data) {
  const res = await request.post('/orderPayout/replenishment', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 订单列表重置请求
export async function orderPayResetApi(data) {
  const res = await request.put('/orderPay/orderPayReset', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 重推订单数据
export async function pushOrderPayCallbackApi(data) {
  const res = await request.put('/orderPay/pushOrderPayCallback', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 出款重推订单数据
export async function pushOrderPayoutCallbackApi(data) {
  const res = await request.put('/orderPayout/pushOrderPayoutCallback', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 模拟下单接口
export async function mockPayOrderApi(data) {
  const res = await request.post('/open/api/order/pay', data);
  if (res.data.code === 0) {
    return res.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 出款下单模拟接口
export async function mockPayoutOrderApi(data) {
  const res = await request.post('/open/api/order/payout', data);
  if (res.data.code === 0) {
    return res.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 账变记录列表
export async function getTransactionsListApi(params) {
  const res = await request.get('/transactions/list', { params });
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 锁定/解锁订单
export async function orderPayoutLockApi(data) {
  const res = await request.post('/orderPayout/lock', data);
  if (res.data.code === 0) {
    return res.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 根据类型获取出款账户
export async function getListByTypeApi(params) {
  const res = await request.get('/cPayPoolAccount/getListByType', { params });
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 订单列表统计
export async function getPayOrderStaticsApi(params) {
  const res = await request.get('/orderPay/getOrderStatics', { params });
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 出款订单统计
export async function getPayoutOrderStaticsApi(params) {
  const res = await request.get('/orderPayout/getPayoutOrderStatics', { params });
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 修改备注
export async function updateOrderPayoutApi(data) {
  const res = await request.put('/orderPayout/updateOrderPayout', data);
  if (res.data.code === 0) {
    return res.data.msg;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 出款补单记录列表
export async function getOrderPayoutLogListApi(params) {
  const res = await request.get('/orderPayout/getOrderPayoutLogList', { params });
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 模拟下单运营版
export async function mockPayOrderTestApi(data) {
  const res = await request.post('/orderPay/payTest', data);
  if (res.data.code === 0) {
    return res.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

export async function getConfigAndPayPoolListByApi(params) {
  const res = await request.get('/orderPayReversal/getConfigAndPayPoolListByApi', { params });
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

export async function getTransactionsCardListApi(params) {
  const res = await request.get('/transactions/card/list', { params });
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}