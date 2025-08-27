<template>
  <ele-page>
    <ele-card>
      <!-- 搜索表单 -->
      <search-form :userList="userList" @search="onSearch" />
      <el-form label-width="120px">
        <el-row :gutter="8">
          <el-col :lg="4" :md="12" :sm="12" :xs="24">
            <el-form-item :label="t('common.refreshTimerText') + '：'">
              <el-switch v-model="autoReload" :inactive-text="t('common.close')" :active-text="t('common.open')" inline-prompt @change="toggleAutoReload" />
            </el-form-item>
          </el-col>
          <el-col :lg="4" :md="12" :sm="12" :xs="24">
            <el-form-item :label="t('common.autoRefreshText') + '：'">
              <span>
                <span style="color: red">{{ countdown }}</span>
                秒
              </span>
            </el-form-item>
          </el-col>
          <el-col :lg="16" :md="12" :sm="12" :xs="24">
            <el-form-item :label="t('common.stisticsText') + '：'">
              <span style="margin-right: 20px; color: red">{{ t('common.averageDisbursementTime') }}: {{ formatTime(statData?.avgTime) }}</span>
              <span style="margin-right: 20px; color: red">{{ t('common.disbursementAmountStatistics') }}: {{ statData?.totalAmount }}</span>
              <span style="margin-right: 20px; color: red">{{ t('common.successCount') }}: {{ statData?.successOrderCount }}</span>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <ele-pro-table
        ref="tableRef"
        row-key="id"
        :columns="columns"
        :datasource="datasource"
        :response="response"
        v-model:current="current"
        v-model:selections="selections"
        :show-overflow-tooltip="true"
        :border="bordered"
        :sticky="!fixedHeight"
        :toolbar="{ theme: 'default' }"
        :default-sort="{ prop: 'createTime', order: 'descending' }"
        :footer-style="{ paddingBottom: '12px' }"
        :tableStyle="{ paddingBottom: '0', color: 'rgba(0, 0, 0, 0.65)' }"
        :export-config="exportConfig"
        :print-config="{ datasource: exportSource }"
      >
        <template #toolbar></template>
        <!-- 表头工具按钮 -->
        <template #tools></template>

        <template #statusSlot="{ row }">
          <el-tag v-if="row.status == 1" type="warning">{{ t('outOrder.payOutingText') }}</el-tag>
          <el-tag v-if="row.status == 2" type="success">{{ t('outOrder.payOutSuccessText') }}</el-tag>
          <el-tag v-if="row.status == -1" type="danger">{{ t('outOrder.payOutFailText') }}</el-tag>
        </template>

        <template #mUserSlot="{ row }">
          <div>{{ t('merchantChannel.merchantName') }}： {{ row.mUserName }}</div>
          <div>{{ t('merchant.merchantNum') }}： {{ row.mUserNo }}</div>
        </template>

        <template #notifyStatusSlot="{ row }">
          <el-tag v-if="row.status == 1" type="warning">{{ t('common.callbackIn') }}</el-tag>
          <el-tag v-if="row.status == 2" type="success">{{ t('common.successText') }}</el-tag>
          <el-tag v-if="row.status == -1" type="danger">{{ t('common.fail') }}</el-tag>
        </template>

        <template #action="{ row }">
          <div style="display: flex; flex-wrap: wrap; justify-content: center">
            <template v-if="userInfo?.authority?.authorityId == 1006">
              <el-button type="primary" link icon="edit" v-if="row.lockStatus === 2" @click="handleChangeRemark(row)">{{ t('common.editRemarkText') }}</el-button>
              <el-divider direction="vertical" v-if="row.lockStatus === 2" />
              <el-button type="primary" link icon="unlock" v-if="row.lockStatus === 1" @click="handleLock(row)">{{ t('common.unlock') }}</el-button>
              <el-button type="primary" link icon="lock" v-if="row.lockStatus === 2" @click="handleLock(row)">{{ t('common.lock') }}</el-button>

              <el-divider direction="vertical" v-if="row.lockStatus === 1 && row.status != -1" />
              <template v-if="row.lockStatus === 1">
                <el-button type="primary" link icon="check" v-if="row.status === 1" @click="handleReset(row, 'examine')">{{ t('common.orderAuditText') }}</el-button>
                <el-button type="primary" link icon="check" v-if="row.status === 2" @click="handleReset(row, 'replenishment')">{{ t('common.disbursementSupplement') }}</el-button>
              </template>
              <el-divider direction="vertical" v-if="row.status !== 1" />
              <el-button type="primary" link icon="switch" v-if="row.status !== 1" @click="handleRepush(row)">{{ t('common.rePushText') }}</el-button>
              <el-divider direction="vertical" />
            </template>
            <el-button type="primary" link icon="view" @click="handleView(row)">{{ t('common.detailText') }}</el-button>
          </div>
        </template>
      </ele-pro-table>
    </ele-card>
    <!-- 重置表单 -->
    <reset-form v-model="showReset" :data="current" :actionType="actionType" @done="reloadPage" />
    <!-- 详情表单 -->
    <view-form v-model="showView" :data="current" @done="reloadPage" />
    <!-- 修改备注 -->
    <message-form v-model="showMessage" :data="current" @done="reloadPage" />
  </ele-page>
</template>
<script setup>
  import { ref, reactive, computed, onMounted, watch, onBeforeUnmount } from 'vue';
  import SearchForm from './components/search-form.vue';
  import { EleMessage } from 'ele-admin-plus';
  import { getOrderPayoutListApi, pushOrderPayoutCallbackApi, orderPayoutLockApi, getPayoutOrderStaticsApi } from '@/api/order/index.js';
  import { getUserList } from '@/api/user';
  import dayjs from 'dayjs';
  import { getExportWorkbook } from '@/config/use-global-config';
  import { download } from '@/utils/common';
  import ResetForm from './components/reset-form.vue';
  import ViewForm from './components/view-form.vue';
  import MessageForm from './components/message-form.vue';
  import { PlusOutlined } from '@/components/icons';
  import { ElMessageBox } from 'element-plus/es';
  import { ElMessage } from 'element-plus';
  import { useMessageBox } from 'ele-admin-plus/es';
  import { formatTime } from '@/utils/public';
  import { useI18n } from 'vue-i18n';
  import { _timeFormat } from '@/utils/public';

  const { t } = useI18n();
  const messageBox = useMessageBox();

  const userInfo = computed(() => {
    return JSON.parse(localStorage.getItem('userInfo'));
  });

  /** 表格实例 */
  const tableRef = ref(null);
  const bordered = ref(true);

  /** 表格搜索参数 */
  const lastWhere = reactive({});
  /** 表格选中数据 */
  const selections = ref([]);
  /** 表格单选选中数据 */
  const current = ref(null);
  /** 表格固定高度 */
  const fixedHeight = ref(false);

  // 倒计时刷新
  const autoReload = ref(false);
  const countdown = ref(10);
  let reloadInterval = null;
  let countdownInterval = null;

  const toggleAutoReload = () => {
    if (autoReload.value) {
      startAutoReload(); // 开启自动刷新
    } else {
      stopAutoReload(); // 停止自动刷新
    }
  };

  // 开启倒计时
  const startAutoReload = () => {
    if (reloadInterval !== null || countdownInterval !== null) return; // 避免重复启动
    reloadInterval = window.setInterval(async () => {
      onSearch();
      getPayoutOrderStatics();
      countdown.value = 10;
    }, 10000);

    // 每秒更新一次倒计时
    countdownInterval = window.setInterval(() => {
      if (countdown.value > 0) {
        countdown.value--;
      }
    }, 1000);
  };

  // 停止倒计时
  const stopAutoReload = () => {
    if (reloadInterval !== null) {
      clearInterval(reloadInterval);
      reloadInterval = null;
    }
    if (countdownInterval !== null) {
      clearInterval(countdownInterval);
      countdownInterval = null;
    }
    countdown.value = 10; // 重置倒计时
    console.log('Auto-reload stopped');
  };

  /** 刷新表格 */
  const reload = (where) => {
    tableRef.value?.reload?.({ page: 1, where });
  };

  /** 搜索事件 */
  const onSearch = (where) => {
    Object.assign(lastWhere, where);
    getPayoutOrderStatics();
    doReload();
  };

  /** 表格搜索 */
  const doReload = () => {
    reload(lastWhere);
  };

  const response = ref({
    dataName: 'list', // 数据列表的字段名称, 支持嵌套, 例如: 'data.rows'
    countName: 'total' // 数据总数的字段名称, 例如: 'data.total'
  });

  const showMessage = ref(false);

  const handleChangeRemark = (row) => {
    current.value = row ?? null;
    showMessage.value = true;
  };

  const showReset = ref(false);

  const actionType = ref('');

  const handleReset = (row, type) => {
    actionType.value = type;
    current.value = row ?? null;
    showReset.value = true;
  };

  const showView = ref(false);

  const handleView = (row) => {
    current.value = row ?? null;
    showView.value = true;
  };

  // 不修改分页刷新
  const reloadPage = () => {
    tableRef.value?.reload?.();
  };

  // 锁定/解锁
  const handleLock = async (row) => {
    let tipsMsg = '';
    if (row.lockStatus === 2) {
      tipsMsg = t('common.confirmLock');
    } else {
      tipsMsg = t('common.confirmUnlock');
    }
    messageBox
      .confirm(tipsMsg, t('common.operaConfirm'), {
        type: 'warning',
        draggable: true
      })
      .then(async () => {
        try {
          const res = await orderPayoutLockApi({ id: row.id, type: row.lockStatus === 2 ? 1 : 2 });
          EleMessage.success(res.msg);
          reloadPage();
        } catch (error) {
          EleMessage.error(error.message);
        }
      })
      .catch(() => {});
  };

  // 重推
  const handleRepush = async (row) => {
    messageBox
      .confirm(t('common.rePushConfirmText'), t('common.operaConfirm'), {
        type: 'warning',
        draggable: true
      })
      .then(async () => {
        try {
          const res = await pushOrderPayoutCallbackApi({ orderNo: row.orderNo });
          EleMessage.success(res);
          reloadPage();
        } catch (error) {
          EleMessage.error(error.message);
        }
      })
      .catch(() => {});
  };

  const failMessageMap = {
    1: t('common.paymentInfoError'),
    2: t('common.multipleRebound'),
    3: t('common.transferFail'),
    4: t('common.merchantClose')
  };

  const statusMap = {
    1: t('outOrder.payOutingText'),
    2: t('outOrder.payOutSuccessText'),
    '-1': t('outOrder.payOutFailText')
  };

  const notifyStatusMap = {
    1: t('common.callbackIn'),
    2: t('common.successText'),
    '-1': t('common.fail')
  };

  /** 表格列配置 */
  const columns = computed(() => {
    return [
      {
        type: 'selection',
        columnKey: 'selection',
        width: 56,
        align: 'center'
      },
      {
        columnKey: 'id',
        prop: 'id',
        label: 'ID',
        align: 'center'
      },
      {
        columnKey: 'status',
        prop: 'status',
        label: t('orderList.orderStatus'),
        slot: 'statusSlot',
        align: 'center',
        minWidth: 120,
        formatter: (row, column, cellValue, index) => {
          return statusMap[cellValue] ?? '-';
        }
      },
      // 回调状态 notifyStatus
      {
        columnKey: 'notifyStatus',
        prop: 'notifyStatus',
        label: t('common.callbackStatusText'),
        slot: 'notifyStatusSlot',
        align: 'center',
        minWidth: 120,
        formatter: (row, column, cellValue, index) => {
          return notifyStatusMap[cellValue] ?? '-';
        }
      },
      {
        columnKey: 'orderNo',
        prop: 'orderNo',
        label: t('orderList.orderNo'),
        align: 'center',
        minWidth: 180
      },
      {
        columnKey: 'cPayPoolAccountName',
        prop: 'cPayPoolAccountName',
        label: t('common.payoutMethod'),
        align: 'center',
        minWidth: 180
      },
      {
        columnKey: 'cPayConfigName',
        prop: 'cPayConfigName',
        label: t('merchant.channelName'),
        align: 'center',
        minWidth: 180
      },
      {
        columnKey: 'merchantName',
        prop: 'merchantName',
        label: t('orderList.merchantInfo'),
        slot: 'mUserSlot',
        align: 'center',
        minWidth: 120,
        formatter: (row, column, cellValue, index) => {
          return `${t('orderList.merchantName')}：${row.mUserName}\r\n${t('merchant.merchantNum')}：${row.mUserNo}`;
        }
      },
      {
        columnKey: 'merSubUser',
        prop: 'merSubUser',
        label: t('orderList.userName'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'labelCode',
        prop: 'labelCode',
        label: t('tagConfig.tagCode'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'amount',
        prop: 'amount',
        label: t('orderList.tradeAmount'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'thirdOrderNo',
        prop: 'thirdOrderNo',
        label: t('orderList.thirdTradeNo'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'failMessageType',
        prop: 'failMessageType',
        label: t('common.failReasonText'),
        align: 'center',
        minWidth: 120,
        formatter: (row, column, cellValue, index) => {
          return failMessageMap[cellValue] ?? '-';
        }
      },
      {
        columnKey: 'message',
        prop: 'message',
        label: t('common.notes'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'createTime',
        prop: 'createTime',
        label: t('common.createTime'),
        align: 'center',
        minWidth: 120,
        formatter: (row, column, cellValue, index) => {
          return _timeFormat(cellValue);
        }
      },
      {
        columnKey: 'payTime',
        prop: 'payTime',
        label: t('common.paymentTime'),
        align: 'center',
        minWidth: 160,
        formatter: (row, column, cellValue, index) => {
          return cellValue ? _timeFormat(cellValue) : '';
        }
      },
      {
        columnKey: 'diffTime',
        prop: 'diffTime',
        label: t('common.handleTimeText'),
        align: 'center',
        minWidth: 120,
        formatter: (row, column, cellValue, index) => {
          return cellValue > 0 ? formatTime(cellValue) : '-';
        }
      },
      {
        columnKey: 'updateByNickname',
        prop: 'updateByNickname',
        label: t('operatorOrderReplenish.operatorText'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'action',
        prop: 'action',
        label: t('common.action'),
        align: 'center',
        slot: 'action',
        width: 280,
        fixed: 'right'
      }
    ];
  });

  /** 表格数据源 */
  // 表格数据源, 数据源可以直接为数组或一个 function 返回一个 Promise
  const datasource = async ({ pages, where, orders, filters }) => {
    try {
      const data = await getOrderPayoutListApi({
        ...where,
        status: where.hasOwnProperty('status') ? where.status : 1,
        page: pages.page || 1, // 使用 pages.currentPage 或默认值 1
        pageSize: pages.limit || 10 // 使用 pages.pageSize 或默认值 10
      });
      return data;
    } catch (e) {
      EleMessage.error(e.message);
    }
  };

  // 导出和打印全部数据的数据源
  const exportSource = async ({ orders, filters, where, pages }) => {
    const data = await getOrderPayoutListApi({ ...where, page: pages.page || 1, pageSize: 999999 });
    return data.list;
  };

  // 导出配置
  const exportConfig = reactive({
    fileName: '出款订单列表',
    datasource: exportSource
  });

  const statData = ref({});

  // 获取订单统计
  const getPayoutOrderStatics = async () => {
    const res = await getPayoutOrderStaticsApi({
      ...lastWhere,
      status: lastWhere.hasOwnProperty('status') ? lastWhere.status : 1
    });
    statData.value = res;
  };

  const userList = ref([]);

  const getAllUserList = async () => {
    const res = await getUserList({
      page: 1,
      pageSize: 9999
    });
    userList.value = res.list;
  };

  onMounted(() => {
    getAllUserList();
    getPayoutOrderStatics();
  });
  // 组件销毁前清除定时器
  onBeforeUnmount(() => {
    stopAutoReload(); // 防止内存泄漏
  });
</script>
