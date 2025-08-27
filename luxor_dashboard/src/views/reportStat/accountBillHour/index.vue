<template>
  <ele-page>
    <!-- 搜索表单 -->
    <search-form :channelList="channelList" @search="onSearch" />
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
          <span style="margin-right: 10px">{{ t('payMethodDay.callCount') }}： {{ statData?.totalTimes }}</span>
          <span style="margin-right: 10px">{{ t('payMethodDay.successAmount') }}： {{ statData?.totalMoney }}</span>
          <span style="margin-right: 10px">{{ t('accountBillDaily.successRateText') }}： {{ statData?.totalRatio }}%</span>
          <span>{{ t('payMethodDay.successCount') }}： {{ statData?.totalSuccessCount }}</span>
        </template>
        <!-- 表头工具按钮 -->
        <template #tools></template>

        <template #cPayPoolAccountValidSlot="{ row }">
          <ele-dot v-if="row.cPayPoolAccountValid === 1" :text="t('common.enable')" type="success" size="8px" />
          <ele-dot v-else-if="row.cPayPoolAccountValid === 0" :text="t('common.disable')" size="8px" type="danger" :ripple="false" />
        </template>

        <template #action="{ row }">
          <el-link type="primary" :underline="false" icon="edit" @click="handleEdit(row)">{{ t('common.edit') }}</el-link>
          <el-divider direction="vertical" />
          <el-link type="danger" :underline="false" icon="Delete" @click="handleDelete(row)">{{ t('common.deleted') }}</el-link>
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
  import { getCPayPoolAccountBillHourListApi, getCPayPoolAccountBillHourStatisticsApi } from '@/api/report/index.js';
  import { ArrowDown, DeleteOutlined, EditOutlined, MinusCircleOutlined } from '@/components/icons/index.js';
  import { useMessageBox } from 'ele-admin-plus/es';
  import { getCPayConfigListApi } from '@/api/payConfig';
  import { _timeFormat } from '@/utils/public.js';
  import { useI18n } from 'vue-i18n';

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
    getStatData();
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

  // 删除
  const handleDelete = async (row) => {
    console.log(row);
    messageBox
      .confirm('确认删除吗? ', '删除确认', {
        type: 'warning',
        draggable: true
      })
      .then(async () => {
        try {
          const res = await deleteCPayConfigApi({ id: row.id });
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
        columnKey: 'date',
        prop: 'date',
        label: t('payMethodDay.dataStr'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'hour',
        prop: 'hour',
        label: t('common.hour'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'cPayConfigName',
        prop: 'cPayConfigName',
        label: t('merchantChannel.channelName'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'cPayPoolName',
        prop: 'cPayPoolName',
        label: t('orderList.poolName'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'cPayPoolAccountName',
        prop: 'cPayPoolAccountName',
        label: t('orderList.payMethodName'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'requestCountDay',
        prop: 'requestCountDay',
        label: t('payMethodDay.callCount'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'successAmountDay',
        prop: 'successAmountDay',
        label: t('payMethodDay.successAmount'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'successCountDay',
        prop: 'successCountDay',
        label: t('payMethodDay.successCount'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'cPayPoolAccountValid',
        prop: 'cPayPoolAccountValid',
        label: t('common.accountStatusText'),
        align: 'center',
        slot: 'cPayPoolAccountValidSlot',
        minWidth: 120
      },
      {
        columnKey: 'successRatio',
        prop: 'successRatio',
        label: t('accountBillDaily.successRateText'),
        align: 'center',
        minWidth: 180,
        formatter: (row, column, cellValue, index) => {
          return cellValue ? `${cellValue}%` : '-';
        }
      },
      {
        columnKey: 'updateTime',
        prop: 'updateTime',
        label: t('common.updateTime'),
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
      const data = await getCPayPoolAccountBillHourListApi({
        ...where,
        timeType: where.hasOwnProperty('timeType') ? where.timeType : 2,
        page: pages.page || 1, // 使用 pages.currentPage 或默认值 1
        pageSize: pages.limit || 10 // 使用 pages.pageSize 或默认值 10
      });
      return data;
    } catch (e) {
      EleMessage.error(e.message);
    }
  };

  // 获取渠道配置列表
  const channelList = ref([]);

  const getChannelList = async () => {
    try {
      const res = await getCPayConfigListApi({
        page: 1,
        pageSize: 1000,
        type: 1
      });
      channelList.value = res.list;
    } catch (error) {
      EleMessage.error(error.message);
    }
  };

  const statData = ref({});

  // 统计
  const getStatData = async () => {
    try {
      const res = await getCPayPoolAccountBillHourStatisticsApi({
        ...lastWhere,
        timeType: lastWhere.hasOwnProperty('timeType') ? lastWhere.timeType : 2,
      })
      statData.value = res
    } catch (error) {
      console.log(error);
    }
  }

  onMounted(() => {
    getChannelList();
    getStatData()
  });
</script>

<style lang="scss" scoped></style>
