<template>
  <ele-page>
    <!-- 搜索表单 -->
    <search-form :userList="userList" @search="onSearch" />
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
          <span>{{ t('operationRecord.operationRecordListText') }}</span>
        </template>
        <!-- 表头工具按钮 -->
        <template #tools></template>

        <template #validSlot="{ row }">
          <ele-dot v-if="row.valid === 1" :text="t('common.enable')" type="success" size="8px" />
          <ele-dot v-else-if="row.valid === 0" :text="t('common.disable')" size="8px" type="danger" :ripple="false" />
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
  import { getSysOperationRecordListApi } from '@/api/sys/operation';
  import { ArrowDown, DeleteOutlined, EditOutlined, MinusCircleOutlined } from '@/components/icons/index.js';
  import { useMessageBox } from 'ele-admin-plus/es';
  import { getUserList } from '@/api/user'
  import { useI18n } from 'vue-i18n';
  import { _timeFormat } from '@/utils/public'

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
        columnKey: 'ID',
        prop: 'ID',
        label: 'ID',
        align: 'center',
        minWidth: 80
      },
      {
        columnKey: 'user_id',
        prop: 'user_id',
        label: t('operatorOrderReplenish.operatorText'),
        align: 'center',
        minWidth: 120,
        formatter: (row) => {
          return row.user?.userName;
        }
      },
      // {
      //   columnKey: 'user',
      //   prop: 'user',
      //   label: t('orderPayRequest.requestParams'),
      //   align: 'center',
      //   minWidth: 120,
      //   formatter: (row) => {
      //     return JSON.stringify(row.user);
      //   }
      // },
      {
        columnKey: 'path',
        prop: 'path',
        label: t('operationRecord.requestPathText'),
        align: 'center',
        minWidth: 120
      },
      // {
      //   columnKey: 'agent',
      //   prop: 'agent',
      //   label: t('operationRecord.agentText'),
      //   align: 'center',
      //   minWidth: 120
      // },

      {
        columnKey: 'status',
        prop: 'status',
        label: t('operationRecord.requestStatusText'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'method',
        prop: 'method',
        label: t('operationRecord.requestMethodText'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'body',
        prop: 'body',
        label: t('operationRecord.requestBodyText'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'resp',
        prop: 'resp',
        label: t('operationRecord.responseBodyText'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'latency',
        prop: 'latency',
        label: t('operationRecord.delayText'),
        align: 'center',
        minWidth: 120,
        // 现在是纳秒，需要处理成毫秒，后面增加单位ms
        formatter: (row) => {
          return row.latency ? (row.latency / 1000000).toFixed(2) + 'ms' : '-';
        }
      },
      {
        columnKey: 'ip',
        prop: 'ip',
        label: t('operationRecord.ipText'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'error_message',
        prop: 'error_message',
        label: t('operationRecord.errorMessageText'),
        align: 'center',
        minWidth: 120
      },

      {
        columnKey: 'CreatedAt',
        prop: 'CreatedAt',
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
      const data = await getSysOperationRecordListApi({
        ...where,
        page: pages.page || 1, // 使用 pages.currentPage 或默认值 1
        pageSize: pages.limit || 10 // 使用 pages.pageSize 或默认值 10
      });
      return data;
    } catch (e) {
      EleMessage.error(e.message);
    }
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
  });
</script>

<style lang="scss" scoped></style>
