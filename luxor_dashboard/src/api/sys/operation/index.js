import request from '@/utils/request';

// 分页查询操作记录列表
export async function getSysOperationRecordListApi(params) {
  const res = await request.get('/sysOperationRecord/getSysOperationRecordList', { params });
  if (res.data.code === 0) {
    return res.data.data;
  }
  return Promise.reject(new Error(res.data.msg));
}