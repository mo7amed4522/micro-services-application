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
      >
        <template #toolbar>
          <!-- <el-button type="primary" :icon="PlusOutlined" class="ele-btn-icon" @click="handleEdit()">{{ t('common.add') }}</el-button> -->
        </template>
        <!-- 表头工具按钮 -->
        <template #tools></template>

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
      </ele-pro-table>
    </ele-card>
  </ele-page>
</template>
<script setup>
  import { ref, reactive, computed, onMounted, watch } from 'vue';
  import SearchForm from './components/search-form.vue';
  import { PlusOutlined } from '@/components/icons';
  import { EleMessage } from 'ele-admin-plus';
  import dayjs from 'dayjs';
  import { getReceiptLogsListApi } from '@/api/receipt/index';
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
        columnKey: 'receiptAccountId',
        prop: 'receiptAccountId',
        label: t('common.receiptAccountId'),
        align: 'center',
        minWidth: 120
      },
      // {
      //   columnKey: 'message',
      //   prop: 'message',
      //   label: t('common.description'),
      //   align: 'center',
      //   minWidth: 120
      // },
      // {
      //   columnKey: 'status',
      //   prop: 'status',
      //   label: t('common.statusText'),
      //   align: 'center',
      //   slot: 'statusSlot',
      //   minWidth: 180,
      //   formatter: (row, column, cellValue, index) => {
      //     // 1人工上线，-1人工下线，3人工维护，4自动下线
      //     if (cellValue === 1) {
      //       return t('common.manualOnline');
      //     } else if (cellValue === -1) {
      //       return t('common.manualOffline');
      //     } else if (cellValue === 3) {
      //       return t('common.manualMaintain');
      //     } else if (cellValue === 4) {
      //       return t('common.autoOffline');
      //     }
      //   }
      // },
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
          }
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
      const data = await getReceiptLogsListApi({
        ...where,
        page: pages.page || 1, // 使用 pages.currentPage 或默认值 1
        pageSize: pages.limit || 10 // 使用 pages.pageSize 或默认值 10
      });
      return data;
    } catch (e) {
      EleMessage.error(e.message);
    }
  };

  onMounted(() => {});
</script>

<style lang="scss" scoped></style>
