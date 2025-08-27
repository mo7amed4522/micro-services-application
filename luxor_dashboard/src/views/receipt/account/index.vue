<template>
  <ele-page>
    <!-- 搜索表单 -->
    <search-form @search="onSearch" />
    <ele-card
      :flex-table="fixedHeight"
      :body-style="{ paddingBottom: '4px' }"
      :style="{
        minHeight: fixedHeight ? '380px' : void 0,
        marginBottom: fixedHeight ? '0px' : void 0
      }"
    >
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
        :toolbar="{ theme: 'default' }"
        :default-sort="{ prop: 'createTime', order: 'descending' }"
        :footer-style="{ paddingBottom: '12px' }"
        :tableStyle="{ paddingBottom: '0', color: 'rgba(0, 0, 0, 0.65)' }"
        :pagination="{ pageSizes: [10, 20, 30, 50, 100, 300, 500, 1000] }"
      >
        <template #toolbar>
          <el-button type="primary" :icon="PlusOutlined" class="ele-btn-icon" @click="handleEdit()">{{ t('common.add') }}</el-button>
        </template>
        <!-- 表头工具按钮 -->
        <template #tools></template>

        <template #validSlot="{ row }">
          <ele-dot v-if="row.status === 1" :text="t('common.online')" type="success" size="8px" />
          <ele-dot v-else-if="row.status === -1" :text="t('common.offline')" size="8px" type="danger" :ripple="false" />
          <ele-dot v-else-if="row.status === 2" :text="t('common.waitOffline')" size="8px" type="danger" :ripple="false" />
          <ele-dot v-else-if="row.status === 3" :text="t('common.maintainStatus')" size="8px" type="danger" :ripple="false" />
        </template>

        <template #statusSlot="{ row }">
          <div v-if="row.offlineMessageType === 1">{{ t('common.loginFail') }}</div>
          <div v-if="row.offlineMessageType === 2">{{ t('common.reachThreshold') }}</div>
          <div v-if="row.offlineMessageType === 3">{{ t('common.utrNotSuccess') }}</div>
          <div v-if="row.offlineMessageType === 4">{{ t('common.utrNotSuccess2') }}</div>
          <div v-if="row.offlineMessageType === 5">{{ t('common.reachTempQuota') }}</div>
          <div v-if="row.offlineMessageType === 6">{{ t('common.utrNotSuccess3') }}</div>
          <div v-if="row.offlineMessageType === 7">{{ t('common.utrNotSuccess4') }}</div>
          <div v-if="row.offlineMessageType === -1">{{ t('common.unknown') }}</div>
        </template>

        <template #action="{ row }">
          <div style="display: flex; flex-wrap: wrap; gap: 10px">
            <el-link type="danger" :underline="false" icon="pointer" @click="handleUpdateStatus(row)" v-if="row.status === 1">{{ t('common.offline') }}</el-link>
            <el-link type="primary" :underline="false" icon="pointer" @click="handleUpdateStatus(row)" v-if="row.status === -1">{{ t('common.online') }}</el-link>

            <template v-if="row.status === -1">
              <el-divider direction="vertical" />
              <el-link type="primary" :underline="false" icon="edit" @click="handleEdit(row)">{{ t('common.edit') }}</el-link>
              <el-divider direction="vertical" />
              <el-link type="danger" :underline="false" icon="Delete" @click="handleDelete(row)">{{ t('common.deleted') }}</el-link>
              <el-divider direction="vertical" />
              <el-link type="danger" :underline="false" icon="Warning" @click="handleMaintain(row)">{{ t('common.maintain') }}</el-link>
            </template>

            <!-- 强制下线 -->
            <template v-if="row.status === 2">
              <el-divider direction="vertical" />
              <el-link type="danger" :underline="false" icon="Warning" @click="handleForceOffline(row)">{{ t('common.forceOffline') }}</el-link>
            </template>

            <!-- 停止维护 -->
            <template v-if="row.status === 3">
              <el-divider direction="vertical" />
              <el-link type="danger" :underline="false" icon="Warning" @click="handleForceOffline(row)">{{ t('common.stopMaintain') }}</el-link>
            </template>

            <el-divider direction="vertical" />
            <el-link type="primary" :underline="false" icon="view" @click="handleProxyData(row)">{{ t('common.viewCurrentAgent') }}</el-link>
            <el-divider direction="vertical" />
            <el-link type="primary" :underline="false" icon="view" @click="handleProxyTable(row)">{{ t('common.historyAgent') }}</el-link>
          </div>
        </template>
      </ele-pro-table>
    </ele-card>

    <!-- 修改表单 -->
    <edit-from v-model="showEdit" :data="current" :agentList="agentList" :payPoolAccountList="payPoolAccountList" @done="reload" />
    <!-- 当前代理详情 -->
    <proxy-form v-model="showProxy" :data="proxyData" @done="reload" />
    <!-- 历史代理table -->
    <proxy-table v-model="showProxyTable" :data="current" @done="reload" />
  </ele-page>
</template>
<script setup>
  import { ref, reactive, computed, onMounted, watch } from 'vue';
  import SearchForm from './components/search-form.vue';
  import { PlusOutlined } from '@/components/icons';
  import { EleMessage } from 'ele-admin-plus';
  import dayjs from 'dayjs';
  import EditFrom from './components/edit-form.vue';
  import ProxyForm from './components/proxy-form.vue';
  import proxyTable from './components/proxy-table.vue';
  import { getReceiptAccountListApi, deleteReceiptAccountApi, updateReceiptAccountApi, getReceiptProxyListApi, findReceiptProxyApi } from '@/api/receipt/index';
  import { getCPayPoolAccountListApi } from '@/api/merchant/index.js';
  import { ArrowDown, DeleteOutlined, EditOutlined, MinusCircleOutlined } from '@/components/icons/index.js';
  import { useMessageBox } from 'ele-admin-plus/es';
  import { useI18n } from 'vue-i18n';
  import { _timeFormat } from '@/utils/public';

  const { t } = useI18n();

  const messageBox = useMessageBox();
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
  /** 刷新表格 */
  const reload = (where) => {
    tableRef.value?.reload?.({ page: 1, where });
  };

  /** 搜索事件 */
  const onSearch = (where) => {
    Object.assign(lastWhere, where);
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

  const showProxyTable = ref(false);

  const handleProxyTable = (row) => {
    current.value = row ?? null;
    showProxyTable.value = true;
  };

  const showProxy = ref(false);

  const proxyData = ref({});

  const handleProxyData = async (row) => {
    if (!row.receiptProxyId) {
      return EleMessage.error(t('common.noBindAgent'));
    }
    try {
      const res = await findReceiptProxyApi({ id: row.receiptProxyId });
      proxyData.value = res;
      showProxy.value = true;
    } catch (error) {
      EleMessage.error(error.message);
    }
  };

  const showEdit = ref(false);

  /** 打开编辑弹窗 */
  const handleEdit = (row) => {
    current.value = row ?? null;
    showEdit.value = true;
  };

  // 维护
  const handleMaintain = async (row) => {
    messageBox
      .confirm(t('common.confirmMaintain'), t('common.confirmStatus'), {
        type: 'warning',
        draggable: true
      })
      .then(async () => {
        try {
          const res = await updateReceiptAccountApi({ id: row.id, status: 3 });
          doReload();
          EleMessage.success(res);
        } catch (error) {
          EleMessage.error(error.message);
        }
      })
      .catch(() => {});
  };

  // 强制下线
  const handleForceOffline = async (row) => {
    messageBox
      .confirm(t('common.confirmForceOffline'), t('common.confirmStatus'), {
        type: 'warning',
        draggable: true
      })
      .then(async () => {
        try {
          const res = await updateReceiptAccountApi({ id: row.id, status: -1 });
          doReload();
          EleMessage.success(res);
        } catch (error) {
          EleMessage.error(error.message);
        }
      })
      .catch(() => {});
  };

  // 更新状态
  const handleUpdateStatus = async (row) => {
    messageBox
      .confirm(t('common.confirmStatus'), t('common.operaConfirm'), {
        type: 'warning',
        draggable: true
      })
      .then(async () => {
        try {
          const res = await updateReceiptAccountApi({ id: row.id, status: row.status === 1 ? -1 : 1 });
          doReload();
          EleMessage.success(res);
        } catch (error) {
          EleMessage.error(error.message);
        }
      })
      .catch(() => {});
  };

  // 删除
  const handleDelete = async (row) => {
    messageBox
      .confirm(t('common.deleteConfirmText'), t('common.deleteConfirm'), {
        type: 'warning',
        draggable: true
      })
      .then(async () => {
        try {
          const res = await deleteReceiptAccountApi({ id: row.id });
          EleMessage.success(res.msg);
          doReload();
        } catch (error) {
          EleMessage.error(error.message);
        }
      })
      .catch(() => {});
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
        align: 'center',
        minWidth: 80
      },
      {
        columnKey: 'valid',
        prop: 'valid',
        label: t('common.statusText'),
        slot: 'validSlot',
        align: 'center',
        minWidth: 120,
        formatter: (row, column, cellValue, index) => {
          // /-1下线  1上线  2等待下线  3维护状态
          if (cellValue === -1) {
            return t('common.offline');
          } else if (cellValue === 1) {
            return t('common.online');
          } else if (cellValue === 2) {
            return t('common.waitOffline');
          } else if (cellValue === 3) {
            return t('common.manualMaintain');
          }
        }
      },
      {
        columnKey: 'offlineMessageType',
        prop: 'offlineMessageType',
        label: t('common.failReasonText'),
        align: 'center',
        slot: 'statusSlot',
        minWidth: 180,
        formatter: (row, column, cellValue, index) => {
          if (cellValue === 1) {
            return t('common.loginFail');
          } else if (cellValue === 2) {
            return t('common.reachThreshold');
          } else if (cellValue === 3) {
            return t('common.utrNotSuccess');
          } else if (cellValue === 4) {
            return t('common.utrNotSuccess2');
          } else if (cellValue === 5) {
            return t('common.reachTempQuota');
          } else if (cellValue === 6) {
            return t('common.utrNotSuccess3');
          } else if (cellValue === 7) {
            return t('common.utrNotSuccess4');
          } else if (cellValue === -1) {
            return t('common.unknown');
          } else if (cellValue === 0) {
            return '';
          }
        }
      },
      {
        columnKey: 'bankName',
        prop: 'bankName',
        label: t('payMethod.bankName'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'loginUserName',
        prop: 'loginUserName',
        label: t('common.loginUserName'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'loginUserId',
        prop: 'loginUserId',
        label: t('common.loginUserId'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'loginPwd',
        prop: 'loginPwd',
        label: t('common.loginPassword'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'deviceId',
        prop: 'deviceId',
        label: t('common.deviceId'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'cPayPoolAccountName',
        prop: 'cPayPoolAccountName',
        label: t('common.paymentMethod'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'remark',
        prop: 'remark',
        label: t('common.notes'),
        align: 'center',
        minWidth: 120
      },
      // {
      //   columnKey: 'createdAt',
      //   prop: 'createdAt',
      //   label: t('common.createTime'),
      //   align: 'center',
      //   minWidth: 180,
      //   formatter: (row, column, cellValue, index) => {
      //     return cellValue ? _timeFormat(cellValue) : '-';
      //   }
      // },
      {
        columnKey: 'action',
        prop: 'action',
        label: t('common.action'),
        align: 'center',
        slot: 'action',
        width: 260,
        fixed: 'right'
      }
    ];
  });

  /** 表格数据源 */
  const datasource = async ({ pages, where, orders, filters }) => {
    try {
      const data = await getReceiptAccountListApi({
        ...where,
        page: pages.page || 1, // 使用 pages.currentPage 或默认值 1
        pageSize: pages.limit || 10 // 使用 pages.pageSize 或默认值 10
      });
      return data;
    } catch (e) {
      EleMessage.error(e.message);
    }
  };

  const payPoolAccountList = ref([]);

  const getPayPoolAccountList = async () => {
    try {
      const res = await getCPayPoolAccountListApi({
        type: '1,5',
        page: 1,
        pageSize: 500
      });
      payPoolAccountList.value = res.list;
    } catch (e) {
      console.log(e);
    }
  };

  // 代理列表
  const agentList = ref([]);

  const getAgentList = async () => {
    try {
      const res = await getReceiptProxyListApi({
        page: 1,
        pageSize: 500,
        status: 1
      });
      agentList.value = res.list;
    } catch (e) {
      console.log(e);
    }
  };

  onMounted(() => {
    getPayPoolAccountList();
    getAgentList();
  });
</script>

<style lang="scss" scoped></style>
