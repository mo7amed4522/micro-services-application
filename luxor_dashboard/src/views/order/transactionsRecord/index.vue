<template>
  <ele-page>
    <!-- 搜索表单 -->
    <search-form :cPayPoolAccountList="cPayPoolAccountList" @search="onSearch" />
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
        </template>
        <!-- 表头工具按钮 -->
        <template #tools></template>

        <template #validSlot="{ row }">
          <ele-dot v-if="row.valid === 1" :text="t('common.enable')" type="success" size="8px" />
          <ele-dot v-else-if="row.valid === 0" :text="t('common.disable')" size="8px" type="danger" :ripple="false" />
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
  import { getTransactionsListApi } from '@/api/order'
  import { getCPayPoolAccountListApi } from '@/api/merchant'
  import { useMessageBox } from 'ele-admin-plus/es';
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
        columnKey: 'id',
        prop: 'id',
        label: 'ID',
        align: 'center',
        minWidth: 80
      },
      {
        columnKey: 'cPayPoolAccountName',
        prop: 'cPayPoolAccountName',
        label: t('common.accountText'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'type',
        prop: 'type',
        label: t('accountChangeRecord.accountChangeTypeText'),
        align: 'center',
        minWidth: 120,
        formatter: (row, column, cellValue, index) => {
          return cellValue === 1 ? t('accountChangeRecord.payeeText') : t('accountChangeRecord.payerText');
        }
      },
      // {
      //   columnKey: 'beforeAmount',
      //   prop: 'beforeAmount',
      //   label: t('accountChangeRecord.beforeAmountText'),
      //   align: 'center',
      //   minWidth: 120
      // },
      {
        columnKey: 'amount',
        prop: 'amount',
        label: t('accountChangeRecord.amountText'),
        align: 'center',
        minWidth: 120
      },
      // {
      //   columnKey: 'afterAmount',
      //   prop: 'afterAmount',
      //   label: t('accountChangeRecord.afterAmountText'),
      //   align: 'center',
      //   minWidth: 120
      // },
      
      {
        columnKey: 'desc',
        prop: 'desc',
        label: t('common.notes'),
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
      const data = await getTransactionsListApi({
        ...where,
        page: pages.page || 1, // 使用 pages.currentPage 或默认值 1
        pageSize: pages.limit || 10 // 使用 pages.pageSize 或默认值 10
      });
      return data;
    } catch (e) {
      EleMessage.error(e.message);
    }
  };

  const cPayPoolAccountList = ref([])

  const getPayPoolAccountList = async () => {
    const res = await getCPayPoolAccountListApi({
      page: 1,
      pageSize: 9999,
    })
    cPayPoolAccountList.value = res.list
  }

  onMounted(() => {
    getPayPoolAccountList()
  });
</script>

<style lang="scss" scoped></style>
