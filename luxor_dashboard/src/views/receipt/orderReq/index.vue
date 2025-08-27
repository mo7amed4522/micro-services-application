<template>
  <ele-page>
    <!-- 搜索表单 -->
    <search-form :receiptConfitList="receiptConfitList" @search="onSearch" />
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
      >
        <template #toolbar>
          <el-button type="primary" :icon="PlusOutlined" class="ele-btn-icon" @click="handleEdit()">{{ t('common.payoutAdd') }}</el-button>
        </template>
        <!-- 表头工具按钮 -->
        <template #tools></template>
        <!-- return cellValue === 1 ? t('common.todoText') : cellValue === 2 ? t('common.handleSuccessText') : t('common.handleFailText'); -->
        <template #statusSlot="{ row }">
          <ele-dot v-if="row.status === 1" :text="t('common.todoText')" type="warning" size="8px" />
          <ele-dot v-if="row.status === 2" :text="t('common.handleSuccessText')" type="success" size="8px" />
          <ele-dot v-else-if="row.status === -1" :text="t('common.handleFailText')" size="8px" type="danger" :ripple="false" />
        </template>
      </ele-pro-table>
    </ele-card>
    <!-- 修改表单 -->
    <edit-from v-model="showEdit" :data="current" @done="reload" />
  </ele-page>
</template>
<script setup>
  import { ref, reactive, computed, onMounted, watch } from 'vue';
  import SearchForm from './components/search-form.vue';
  import { PlusOutlined } from '@/components/icons';
  import { EleMessage } from 'ele-admin-plus';
  import dayjs from 'dayjs';
  import { getReceiptOrderReqListApi, getReceiptConfigListApi } from '@/api/receipt/index';
  import { ArrowDown, DeleteOutlined, EditOutlined, MinusCircleOutlined } from '@/components/icons/index.js';
  import { useMessageBox } from 'ele-admin-plus/es';
  import { useI18n } from 'vue-i18n';
  import { _timeFormat } from '@/utils/public';
  import EditFrom from './components/edit-form.vue';

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

  const showEdit = ref(false);

  /** 打开编辑弹窗 */
  const handleEdit = (row) => {
    current.value = row ?? null;
    showEdit.value = true;
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
        columnKey: 'bankName',
        prop: 'bankName',
        label: t('payMethod.bankName'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'utr',
        prop: 'utr',
        label: t('utrOrder.utrCode'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'amount',
        prop: 'amount',
        label: t('mockOrder.orderAmountText'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'order_type',
        prop: 'order_type',
        label: t('common.orderTypeText'),
        align: 'center',
        minWidth: 120,
        formatter: (row, column, cellValue, index) => {
          return cellValue === 1 ? t('common.depositText') : t('channelConfig.payOutText');
        }
      },
      {
        columnKey: 'date',
        prop: 'date',
        label: t('common.handleTimeText'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'desc',
        prop: 'desc',
        label: t('common.rowDataText'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'message',
        prop: 'message',
        label: t('common.description'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'status',
        prop: 'status',
        label: t('common.statusText'),
        align: 'center',
        slot: 'statusSlot',
        minWidth: 180,
        formatter: (row, column, cellValue, index) => {
          // 1待处理 2处理成功 -1处理失败
          return cellValue === 1 ? t('common.todoText') : cellValue === 2 ? t('common.handleSuccessText') : t('common.handleFailText');
        }
      },

      {
        columnKey: 'successTime',
        prop: 'successTime',
        label: t('common.matchSuccessTime'),
        align: 'center',
        minWidth: 180,
        formatter: (row, column, cellValue, index) => {
          return cellValue ? _timeFormat(cellValue) : '-';
        }
      },

      {
        columnKey: 'createTime',
        prop: 'createTime',
        label: t('common.createTime'),
        align: 'center',
        minWidth: 180,
        formatter: (row, column, cellValue, index) => {
          return cellValue ? _timeFormat(cellValue) : '-';
        }
      }
    ];
  });

  /** 表格数据源 */
  const datasource = async ({ pages, where, orders, filters }) => {
    try {
      const data = await getReceiptOrderReqListApi({
        ...where,
        page: pages.page || 1, // 使用 pages.currentPage 或默认值 1
        pageSize: pages.limit || 10 // 使用 pages.pageSize 或默认值 10
      });
      return data;
    } catch (e) {
      EleMessage.error(e.message);
    }
  };

  const receiptConfitList = ref([]);

  const getReceiptConfigList = async () => {
    try {
      const res = await getReceiptConfigListApi({
        configKey: 'bankNameKey'
      });
      receiptConfitList.value = res.list;
    } catch (e) {
      EleMessage.error(e.message);
    }
  };

  onMounted(() => {
    getReceiptConfigList();
  });
</script>

<style lang="scss" scoped></style>
