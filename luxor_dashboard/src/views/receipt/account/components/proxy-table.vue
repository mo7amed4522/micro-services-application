<template>
  <ele-modal form :width="1100" :model-value="modelValue" :title="t('common.historyAgent')" @update:modelValue="updateModelValue">
    <ele-pro-table
      ref="tableHistoryRef"
      row-key="id"
      :columns="columns"
      :datasource="datasource"
      :response="response"
      :show-overflow-tooltip="true"
      :border="bordered"
      :toolbar="{ theme: 'default' }"
      :default-sort="{ prop: 'createTime', order: 'descending' }"
      :footer-style="{ paddingBottom: '12px' }"
      :tableStyle="{ paddingBottom: '0', color: 'rgba(0, 0, 0, 0.65)' }"
      :pagination="{ pageSizes: [10, 20, 30, 50, 100, 300, 500, 1000] }"
    >
      <template #toolbar></template>
      <!-- 表头工具按钮 -->
      <template #tools></template>

      <template #validSlot="{ row }">
        <ele-dot v-if="row.valid === 1" :text="t('common.enable')" type="success" size="8px" />
        <ele-dot v-else-if="row.valid === -1" :text="t('common.disable')" size="8px" type="danger" :ripple="false" />
      </template>
    </ele-pro-table>

    <template #footer>
      <el-button @click="updateModelValue(false)">{{ t('common.cancelText') }}</el-button>
      <el-button type="primary" :loading="loading" @click="updateModelValue(false)">{{ t('common.saveText') }}</el-button>
    </template>
  </ele-modal>
</template>

<script setup>
  import { ref, computed, watch } from 'vue';
  import { EleMessage } from 'ele-admin-plus/es';
  import { getReceiptProxyListApi } from '@/api/receipt/index';
  import { useI18n } from 'vue-i18n';
  import { _timeFormat } from '@/utils/public';

  const { t } = useI18n();

  const emit = defineEmits(['done', 'update:modelValue']);

  const props = defineProps({
    /** 弹窗是否打开 */
    modelValue: Boolean,
    data: Object
  });

  const bordered = ref(true);

  const response = ref({
    dataName: 'list', // 数据列表的字段名称, 支持嵌套, 例如: 'data.rows'
    countName: 'total' // 数据总数的字段名称, 例如: 'data.total'
  });

  /** 提交状态 */
  const loading = ref(false);

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
        columnKey: 'ip',
        prop: 'ip',
        label: t('common.ipText'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'user',
        prop: 'user',
        label: t('merchant.username'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'password',
        prop: 'password',
        label: t('adminUser.password'),
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
        columnKey: 'valid',
        prop: 'valid',
        label: t('common.statusText'),
        slot: 'validSlot',
        align: 'center',
        minWidth: 120,
        formatter: (row, column, cellValue, index) => {
          // 1启用 -1停用
          return cellValue === 1 ? t('common.enable') : t('common.disable');
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
      const ipHistory = props.data?.ipHistory || '';
      const data = await getReceiptProxyListApi({
        ...where,
        ipHistory: ipHistory,
        page: pages.page || 1, // 使用 pages.currentPage 或默认值 1
        pageSize: pages.limit || 10 // 使用 pages.pageSize 或默认值 10
      });
      return data;
    } catch (e) {
      EleMessage.error(e.message);
    }
  };

  /** 更新modelValue */
  const updateModelValue = (value) => {
    emit('update:modelValue', value);
  };

  const tableHistoryRef = ref(null);

  watch(
    () => props.modelValue,
    (modelValue) => {
      if (modelValue && props.data?.ipHistory) {
        // 表格手动触发刷新
        tableHistoryRef.value?.reload();
      }
    }
  );
</script>
