<template>
  <ele-page>
    <ele-card>
      <!-- 搜索表单 -->
      <search-form :userList="userList" @search="onSearch" />
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
        <template #toolbar>
          <el-button type="primary" :icon="PlusOutlined" class="ele-btn-icon" @click="handleEdit()">{{ t('utrOrder.addUtrOrder') }}</el-button>
        </template>
        <!-- 表头工具按钮 -->
        <template #tools></template>

        <template #action="{ row }">
          <el-button type="danger" link icon="delete" v-if="row.status == 1 || row.status == 3" @click="handleClose(row)">{{ t('utrOrder.closeOrderText') }}</el-button>
        </template>
      </ele-pro-table>
    </ele-card>
    <!-- utr补单弹窗 -->
    <edit-from v-model="showEdit" :data="current" @done="reload" />
  </ele-page>
</template>
<script setup>
  import { ref, reactive, computed, onMounted, watch } from 'vue';
  import SearchForm from './components/search-form.vue';
  import { PlusOutlined } from '@/components/icons';
  import { EleMessage } from 'ele-admin-plus';
  import { getOrderPayReversalListApi, closeStatusApi } from '@/api/order/index.js';
  import dayjs from 'dayjs';
  import { getExportWorkbook } from '@/config/use-global-config';
  import { download } from '@/utils/common';
  import EditFrom from './components/edit-form.vue';
  import { ElMessageBox } from 'element-plus/es';
  import { ElMessage } from 'element-plus';
  import { useMessageBox } from 'ele-admin-plus/es';
  import { useI18n } from 'vue-i18n';
  import { _timeFormat, maskMiddle8 } from '@/utils/public';
  import { getUserList } from '@/api/user';

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
  const apiGroupMap = ref({});
  const apiGroupOptions = ref([]);
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

  const showEdit = ref(false);

  // utr补单弹窗
  const handleEdit = (row) => {
    current.value = row ?? null;
    showEdit.value = true;
  };

  // 关闭订单
  const handleClose = async (row) => {
    messageBox
      .confirm('确认关闭订单吗? ', '操作确认', {
        type: 'warning',
        draggable: true
      })
      .then(async () => {
        try {
          const res = await closeStatusApi({ id: row.id });
          EleMessage.success(res);
          doReload();
        } catch (error) {
          EleMessage.error(error.message);
        }
      })
      .catch(() => {});
  };

  const statusMap = {
    1: t('common.processing'),
    2: t('common.successText'),
    3: t('common.timeoutText'),
    '-1': t('common.fail'),
    '-2': t('common.closed')
  };

  const sourceTypeMap = {
    1: 'upi-QR',
    2: 'upi-bank',
    3: 'bank-bank'
  }

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
        label: t('common.statusText'),
        align: 'center',
        minWidth: 120,
        formatter: (row, column, cellValue, index) => {
          return statusMap[cellValue];
        }
      },
      {
        columnKey: 'cPayPoolAccountName',
        prop: 'cPayPoolAccountName',
        label: t('common.paymentAccountName'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'utr',
        prop: 'utr',
        label: t('utrOrder.utrCode'),
        align: 'center',
        minWidth: 120,
        formatter: (row, column, cellValue, index) => {
          return userInfo.value?.authority?.authorityId === 1005 ? maskMiddle8(row.utr) : row.utr
        }
      },
      {
        columnKey: 'orderNo',
        prop: 'orderNo',
        label: t('orderList.orderNo'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'oprType',
        prop: 'oprType',
        label: t('utrOrder.operationType'),
        align: 'center',
        minWidth: 120,
        formatter: (row, column, cellValue, index) => {
          return row.oprType == 1 ? t('common.autoReceipt') : t('common.manualReceipt');
        }
      },
      {
        columnKey: 'amount',
        prop: 'amount',
        label: t('utrOrder.amount'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'cardNumber',
        prop: 'cardNumber',
        label: t('utrOrder.cardNumber'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'sourceType',
        prop: 'sourceType',
        label: t('utrOrder.paySource'),
        align: 'center',
        minWidth: 120,
        formatter: (row, column, cellValue, index) => {
          return sourceTypeMap[cellValue];
        }
      },
      {
        columnKey: 'remark',
        prop: 'remark',
        label: t('common.notes'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'updateTime',
        prop: 'updateTime',
        label: t('common.updateTime'),
        align: 'center',
        minWidth: 120,
        formatter: (row, column, cellValue, index) => {
          return _timeFormat(cellValue);
        }
      },
      {
        columnKey: 'createByNickname',
        prop: 'createByNickname',
        label: t('common.createBy'),
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
        columnKey: 'action',
        prop: 'action',
        label: t('common.action'),
        align: 'center',
        slot: 'action',
        width: 140,
        fixed: 'right'
      }
    ];
  });

  /** 表格数据源 */
  // 表格数据源, 数据源可以直接为数组或一个 function 返回一个 Promise
  const datasource = async ({ pages, where, orders, filters }) => {
    try {
      const data = await getOrderPayReversalListApi({
        ...where,
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
    const data = await getOrderPayReversalListApi({ ...where, page: pages.page || 1, pageSize: 999999 });
    return data.list;
  };

  // 导出配置
  const exportConfig = reactive({
    fileName: 'UTR订单冲正列表',
    datasource: exportSource
  });

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
  });
</script>
