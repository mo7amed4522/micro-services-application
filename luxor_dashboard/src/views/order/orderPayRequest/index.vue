<template>
  <ele-page>
    <ele-card>
      <!-- 搜索表单 -->
      <search-form @search="onSearch" />
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
          <!-- <el-button icon="compass" @click="onSync">同步API</el-button> -->
        </template>
        <!-- 表头工具按钮 -->
        <template #tools></template>

        <template #exceptionTypeSlot="{ row }">
          <el-tag v-if="row.exceptionType == 0" type="success">{{ t('common.normal') }}</el-tag>
          <el-tag v-else type="danger">未知(后期修改)</el-tag>
        </template>

        <template #statusSlot="{ row }">
          <el-tag v-if="row.status == 2" type="success">{{ t('common.successText') }}</el-tag>
          <el-tag v-if="row.status == 1" type="danger">{{ t('common.exceptionText') }}</el-tag>
        </template>

        <template #action="{ row }">
          <el-button type="danger" link icon="delete" @click="handleDraw(row)">{{ t('common.deleted') }}</el-button>
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
  import { getOrderPayRequestListApi } from '@/api/order/index.js';
  import dayjs from 'dayjs';
  import { getExportWorkbook } from '@/config/use-global-config';
  import { download } from '@/utils/common';
  import { ElMessageBox } from 'element-plus/es';
  import { ElMessage } from 'element-plus';
  import { useI18n } from 'vue-i18n';
  import { _timeFormat } from '@/utils/public';

  const { t } = useI18n();

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
        columnKey: 'id',
        prop: 'id',
        label: 'ID',
        align: 'center'
      },
      {
        columnKey: 'requestData',
        prop: 'requestData',
        label: t('orderPayRequest.requestParams'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'reponseData',
        prop: 'reponseData',
        label: t('orderPayRequest.responseInfo'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'reponseTime',
        prop: 'reponseTime',
        label: t('orderPayRequest.responseTime'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'exceptionType',
        prop: 'exceptionType',
        label: t('orderPayRequest.exceptionType'),
        slot: 'exceptionTypeSlot',
        align: 'center',
        minWidth: 120,
        formatter: (row, column, cellValue, index) => {
          return row.exceptionType == 0 ? t('common.normal') : '';
        }
      },
      {
        columnKey: 'status',
        prop: 'status',
        label: t('common.statusText'),
        slot: 'statusSlot',
        align: 'center',
        minWidth: 120,
        formatter: (row, column, cellValue, index) => {
          return row.status == 2 ? t('common.successText') : t('common.exceptionText');
        }
      },
      {
        columnKey: 'thirdOrderNo',
        prop: 'thirdOrderNo',
        label: t('utrOrder.thirdTradeNo'),
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
        columnKey: 'createTime',
        prop: 'createTime',
        label: t('common.createTime'),
        align: 'center',
        minWidth: 120,
        formatter: (row, column, cellValue, index) => {
          return _timeFormat(cellValue);
        }
      }
      // {
      //   columnKey: 'action',
      //   prop: 'action',
      //   label: '操作',
      //   align: 'center',
      //   slot: 'action',
      //   width: 200,
      //   fixed: 'right'
      // }
    ];
  });

  /** 表格数据源 */
  // 表格数据源, 数据源可以直接为数组或一个 function 返回一个 Promise
  const datasource = async ({ pages, where, orders, filters }) => {
    try {
      const data = await getOrderPayRequestListApi({
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
    const data = await getOrderPayRequestListApi({ ...where, page: pages.page || 1, pageSize: 999999 });
    return data.list;
  };

  // 导出配置
  const exportConfig = reactive({
    fileName: '支付订单请求列表',
    datasource: exportSource
  });

  onMounted(() => {});
</script>