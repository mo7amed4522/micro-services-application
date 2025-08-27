<template>
  <ele-page>
    <ele-card>
      <!-- 搜索表单 -->
      <search-form @search="onSearch" :apiGroup="apiGroupOptions" />
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
          <span>下分订单列表</span>
        </template>
        <!-- 表头工具按钮 -->
        <template #tools>
          <div style="display: flex; align-items: center; flex-wrap: wrap">
            <div>高度铺满&nbsp;</div>
            <el-switch v-model="fixedHeight" size="small" />
            <el-divider direction="vertical" />
          </div>
        </template>

        <template #statusBox="{ row }">
          <!-- 处理中 0 第一次成功: 1 第一二次都成功 3  第一次失败，第二次成功 2  第一次成功，第二次失败 7 -->
          <el-tag v-if="row.status === 0" type="warning">处理中</el-tag>
          <el-tag v-if="row.status === 1" type="warning">第一次成功</el-tag>
          <el-tag v-if="row.status === 2" type="warning">第一次失败，第二次成功</el-tag>
          <el-tag v-if="row.status === 3" type="warning">第一二次都成功</el-tag>
          <el-tag v-if="row.status === 7" type="warning">第一次成功，第二次失败</el-tag>
        </template>

        <template #action="{ row }">
          <el-button type="primary" link v-if="row.status !== 1" @click="handleManual(row)">上分重推</el-button>
        </template>
      </ele-pro-table>
    </ele-card>
  </ele-page>
</template>
<script setup>
  import { ref, reactive, computed, onMounted, watch, h } from 'vue';
  import SearchForm from './components/search-form.vue';
  import { PlusOutlined } from '@/components/icons';
  import { EleMessage } from 'ele-admin-plus';
  import { getPayoutListApi, payoutManualApi } from '@/api/order/index.js';
  import dayjs from 'dayjs';
  import { getExportWorkbook } from '@/config/use-global-config';
  import { download } from '@/utils/common';
  import { ElMessageBox } from 'element-plus/es';
  import { ElMessage } from 'element-plus';
  import { _timeFormat } from '@/utils/public'
  import { useMessageBox } from 'ele-admin-plus/es';

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
        columnKey: 'merchantNo',
        prop: 'merchantNo',
        label: '商户号',
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'merchantName',
        prop: 'merchantName',
        label: '商户名',
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'userName',
        prop: 'userName',
        label: '用户名',
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'amount',
        prop: 'amount',
        label: '金额',
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'sign',
        prop: 'sign',
        label: '签名',
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'thirdOrderNo',
        prop: 'thirdOrderNo',
        label: '订单号',
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'status',
        prop: 'status',
        label: '状态',
        slot: 'statusBox',
        align: 'center',
        minWidth: 160
      },
      {
        columnKey: 'immediateAmount',
        prop: 'immediateAmount',
        label: '首次上分金额',
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'secondDayAmount',
        prop: 'secondDayAmount',
        label: '二次上分金额',
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'config',
        prop: 'config',
        label: '赠送配置',
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'updatedAt',
        prop: 'updatedAt',
        label: '更新时间',
        align: 'center',
        minWidth: 120,
        formatter: (row, column, cellValue, index) => {
          return _timeFormat(cellValue);
        }
      },
      {
        columnKey: 'createdAt',
        prop: 'createdAt',
        label: '创建时间',
        align: 'center',
        minWidth: 120,
        formatter: (row, column, cellValue, index) => {
          return _timeFormat(cellValue);
        }
      },
      {
        columnKey: 'action',
        prop: 'action',
        label: '操作',
        align: 'center',
        slot: 'action',
        minWidth: 160,
        fixed: 'right'
      }
    ];
  });

  const isAdmin = ref(false)

  /** 表格数据源 */
  // 表格数据源, 数据源可以直接为数组或一个 function 返回一个 Promise
  const datasource = async ({ pages, where, orders, filters }) => {
    try {
      const data = await getPayoutListApi({
        ...where,
        page: pages.page || 1, // 使用 pages.currentPage 或默认值 1
        pageSize: pages.limit || 10 // 使用 pages.pageSize 或默认值 10
      });
      return data;
    } catch (e) {
      EleMessage.error(e.message);
    }
  };

  // 重推
  const handleManual = async (row) => {
    messageBox
      .confirm('确认上分重推吗? ', '操作确认', {
        type: 'warning',
        draggable: true
      })
      .then(async () => {
        try {
          const res = await payoutManualApi({ thirdOrderNo: row.thirdOrderNo });
          EleMessage.success(res);
          doReload();
        } catch (error) {
          EleMessage.error(error.message);
        }
      })
      .catch(() => {});
  };

  // 导出和打印全部数据的数据源
  const exportSource = async ({ orders, filters, where, pages }) => {
    const data = await getPayoutListApi({ ...where, page: pages.page || 1, pageSize: 999999 });
    return data.list;
  };

  // 导出配置
  const exportConfig = reactive({
    fileName: '下分订单列表',
    datasource: exportSource,
    beforeExport: (params) => {
      const { fileName, closeModal, bodyCols, bodyData, headerData, data } = params;
      const workbook = getExportWorkbook(params);
      const sheet = workbook.getWorksheet('Sheet1');
      const handleExcelData = async () => {
        // 输出 workbook
        const excelData = await workbook.xlsx.writeBuffer();
        return excelData;
      };

      handleExcelData().then((data) => {
        download(data, `${fileName}.xlsx`);
        closeModal();
      });
      return false;
    }
  });

  onMounted(() => {});
</script>

<style lang="scss" scoped>
</style>
