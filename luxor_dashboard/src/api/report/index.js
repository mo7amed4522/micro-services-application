import request from '@/utils/request';

// 收款方式日统计列表
export async function getCPayPoolAccountBillDailyListApi(params) {
  const res = await request.get('/cPayPoolAccountBillDaily/getCPayPoolAccountBillDailyList', { params });
  if (res.data.code === 0 && res.data.data) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 收款方式小时统计列表
export async function getCPayPoolAccountBillHourListApi(params) {
  const res = await request.get('/cPayPoolAccountBillHour/getCPayPoolAccountBillHourList', { params });
  if (res.data.code === 0 && res.data.data) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 支付池日统计
export async function getCPayPoolBillDailyListApi(params) {
  const res = await request.get('/cPayPoolBillDaily/getCPayPoolBillDailyList', { params });
  if (res.data.code === 0 && res.data.data) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 商户流水统计
export async function getMBillPayDailyListApi(params) {
  const res = await request.get('/mBillPayDaily/getMBillPayDailyList', { params });
  if (res.data.code === 0 && res.data.data) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 操作员补单统计
export async function orderPayReversalLogListApi(params) {
  const res = await request.get('/orderPayReversal/logList', { params });
  if (res.data.code === 0 && res.data.data) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 操作员出款统计
export async function orderPayoutLogApi(params) {
  const res = await request.get('/orderPayout/logList', { params });
  if (res.data.code === 0 && res.data.data) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 收款方式日统计-统计
export async function getCPayPoolAccountBillDailyStatisticsApi(params) {
  const res = await request.get('/cPayPoolAccountBillDaily/getCPayPoolAccountBillDailyStatistics', { params });
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}

// 收款方式小时统计-统计
export async function getCPayPoolAccountBillHourStatisticsApi(params) {
  const res = await request.get('/cPayPoolAccountBillHour/getCPayPoolAccountBillHourStatistics', { params });
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}