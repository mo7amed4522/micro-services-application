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
        <template #toolbar></template>
        <!-- 表头工具按钮 -->
        <template #tools></template>

        <template #statusSlot="{ row }">
          <el-tag v-if="row.status == 1" type="warning">{{ t('outOrder.payOutingText') }}</el-tag>
          <el-tag v-if="row.status == 2" type="success">{{ t('outOrder.payOutSuccessText') }}</el-tag>
          <el-tag v-if="row.status == -1" type="danger">{{ t('outOrder.payOutFailText') }}</el-tag>
        </template>

        <template #cTypeSlot="{ row }">
          <el-tag v-if="row.cType == 1" type="success">{{ t('common.selfDevelop') }}</el-tag>
          <el-tag v-if="row.cType == 2" type="danger">{{ t('common.thirdParty') }}</el-tag>
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
  import { getOrderPayoutLogListApi } from '@/api/order';
  import { getUserList } from '@/api/user';
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

  const statusMap = {
    1: t('outOrder.payOutingText'),
    2: t('outOrder.payOutSuccessText'),
    '-1': t('outOrder.payOutFailText')
  };

  const cTypeMap = {
    1: t('common.selfDevelop'),
    2: t('common.thirdParty')
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
      {
        columnKey: 'orderNo',
        prop: 'orderNo',
        label: t('orderList.orderNo'),
        align: 'center',
        minWidth: 180
      },
      {
        columnKey: 'cType',
        prop: 'cType',
        label: t('channelConfig.channelType'),
        slot: 'cTypeSlot',
        align: 'center',
        minWidth: 120,
        formatter: (row, column, cellValue, index) => {
          return cTypeMap[cellValue] ?? '-';
        }
      },
      {
        columnKey: 'oldCPayPoolAccountName',
        prop: 'oldCPayPoolAccountName',
        label: t('common.preAccountText'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'cPayPoolAccountName',
        prop: 'cPayPoolAccountName',
        label: t('common.postAccountText'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'actualAmount',
        prop: 'actualAmount',
        label: t('orderList.tradeAmount'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'operatorName',
        prop: 'operatorName',
        label: t('operatorOrderReplenish.operatorText'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'ifsc',
        prop: 'ifsc',
        label: t('payMethod.ifscCode'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'cardBank',
        prop: 'cardBank',
        label: t('payMethod.bankName'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'cardName',
        prop: 'cardName',
        label: t('outOrder.payeeName'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'cardNumber',
        prop: 'cardNumber',
        label: t('outOrder.payeeCardNumber'),
        align: 'center',
        minWidth: 120
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
      const data = await getOrderPayoutLogListApi({
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
