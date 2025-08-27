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
        </template>
        <!-- 表头工具按钮 -->
        <template #tools></template>

        <template #validSlot="{ row }">
          <ele-dot v-if="row.valid === 1" :text="t('common.enable')" type="success" size="8px" />
          <ele-dot v-else-if="row.valid === 0" :text="t('common.disable')" size="8px" type="danger" :ripple="false" />
        </template>

        <template #action="{ row }">
          <el-button type="primary" link icon="Promotion" @click="handleRecover(row)">{{ t('common.recover') }}</el-button>
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
  import { getCPayPoolAccountListByDelList, recoverCPayPoolAccountApi } from '@/api/merchant/index.js';
  import { useMessageBox } from 'ele-admin-plus/es';
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

  // 删除
  const handleRecover = async (row) => {
    messageBox
      .confirm(t('common.confirmRecover'), t('common.operaConfirm'), {
        type: 'warning',
        draggable: true
      })
      .then(async () => {
        try {
          const res = await recoverCPayPoolAccountApi({ id: row.id });
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
        label: t('common.status'),
        slot: 'validSlot',
        align: 'center',
        minWidth: 120,
        formatter: (row, column, cellValue, index) => {
          return cellValue == 1 ? t('common.enable') : t('common.disable');
        }
      },
      {
        columnKey: 'name',
        prop: 'name',
        label: t('common.accountNameText'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'amountActual',
        prop: 'amountActual',
        label: t('common.balanceText'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'qrType',
        prop: 'qrType',
        label: t('payMethod.type'),
        align: 'center',
        minWidth: 120,
        formatter: (row, column, cellValue, index) => {
          return cellValue === 0 ? t('payMethod.qrCode') : t('payMethod.bankCard');
        }
      },
      {
        columnKey: 'limitFlag',
        prop: 'limitFlag',
        label: t('common.limitFlagText'),
        align: 'center',
        minWidth: 120,
        formatter: (row, column, cellValue, index) => {
          return cellValue === 1 ? t('common.close') : t('common.open');
        }
      },
      {
        columnKey: 'successCountPerDay',
        prop: 'successCountPerDay',
        label: t('common.successCountPerDayText'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'successAmountPerDay',
        prop: 'successAmountPerDay',
        label: t('common.successAmountPerDayText'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'bankMark',
        prop: 'bankMark',
        label: t('payMethod.bankCode'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'bankName',
        prop: 'bankName',
        label: t('payMethod.bankName'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'cardName',
        prop: 'cardName',
        label: t('payMethod.cardholderName'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'cardNumber',
        prop: 'cardNumber',
        label: t('payMethod.cardNumber'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'cardPhone',
        prop: 'cardPhone',
        label: t('common.reservedPhone'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'limitAmountMin',
        prop: 'limitAmountMin',
        label: t('payMethod.minAmountLimit'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'limitAmountMax',
        prop: 'limitAmountMax',
        label: t('payMethod.maxAmountLimit'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'qrContent',
        prop: 'qrContent',
        label: t('payMethod.qrCodeContent'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'qrcode',
        prop: 'qrcode',
        label: t('payMethod.scanCodeUrl'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'qrcodeMsg',
        prop: 'qrcodeMsg',
        label: t('payMethod.qrCodeRemark'),
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
      {
        columnKey: 'createTime',
        prop: 'createTime',
        label: t('common.createTime'),
        align: 'center',
        minWidth: 180,
        formatter: (row, column, cellValue, index) => {
          return cellValue ? _timeFormat(cellValue) : '-';
        }
      },
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
      const data = await getCPayPoolAccountListByDelList({
        ...where,
        type: where?.type?.length ? where.type.join(',') : '2,3,4,5,6',
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
