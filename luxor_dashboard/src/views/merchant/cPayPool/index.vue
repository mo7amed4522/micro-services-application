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
          <el-button type="primary" :icon="PlusOutlined" class="ele-btn-icon" @click="handleEdit()">{{ t('common.add') }}</el-button>
        </template>
        <!-- 表头工具按钮 -->
        <template #tools></template>

        <template #validSlot="{ row }">
          <ele-dot v-if="row.valid === 1" :text="t('common.enable')" type="success" size="8px" />
          <ele-dot v-else-if="row.valid === 0" :text="t('common.disable')" size="8px" type="danger" :ripple="false" />
        </template>

        <template #allAccountNumSlot="{ row }">
          <div>{{ t('common.totalText') }}: {{ row.allAccountNum }}</div>
          <div style="color: #52c41a">{{ t('common.enableText') }}: {{ row.validAccountNum }}</div>
          <div style="color: #ff4d4f">{{ t('common.disableText') }}: {{ row.invalidAccountNum }}</div>
        </template>

        <template #action="{ row }">
          <el-link type="primary" :underline="false" icon="edit" @click="handlePayPool(row)">{{ t('payPool.addPayMethod') }}</el-link>
          <el-divider direction="vertical" />
          <el-link type="primary" :underline="false" icon="edit" @click="handleAssignLabel(row)">{{ t('merchant.tags') }}</el-link>
          <el-divider direction="vertical" />
          <el-link type="primary" :underline="false" icon="edit" @click="handleEdit(row)">{{ t('common.edit') }}</el-link>
          <el-divider direction="vertical" />
          <el-link type="danger" :underline="false" icon="Delete" @click="handleDelete(row)">{{ t('common.deleted') }}</el-link>
        </template>
      </ele-pro-table>
    </ele-card>

    <!-- 修改表单 -->
    <edit-from v-model="showEdit" :data="current" @done="reload" />
    <!-- 新增收款账号表单 -->
    <pool-form v-model="showPayPool" :cPayPoolId="cPayPoolId" cPayPoolType="payment" @done="reload" />
    <!-- 分配标签 -->
    <assign-label v-model="showAssignLabel" :data="current" @done="reload" />
  </ele-page>
</template>
<script setup>
  import { ref, reactive, computed, onMounted, watch } from 'vue';
  import SearchForm from './components/search-form.vue';
  import { PlusOutlined } from '@/components/icons';
  import { EleMessage } from 'ele-admin-plus';
  import dayjs from 'dayjs';
  import EditFrom from './components/edit-form.vue';
  import PoolForm from '@/views/merchant/payAccount/components/edit-form.vue';
  import AssignLabel from './components/assign-label.vue';
  import { getCPayPoolListApi, deleteCPayPoolApi } from '@/api/merchant/index.js';
  import { ArrowDown, DeleteOutlined, EditOutlined, MinusCircleOutlined } from '@/components/icons/index.js';
  import { useMessageBox } from 'ele-admin-plus/es';
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

  const showAssignLabel = ref(false);

  const handleAssignLabel = (row) => {
    current.value = row ?? null;
    showAssignLabel.value = true;
  };

  const showPayPool = ref(false);
  const cPayPoolId = ref(null);

  // 打开新增收款方式弹窗
  const handlePayPool = (row) => {
    cPayPoolId.value = row.id;
    showPayPool.value = true;
  };

  // 删除
  const handleDelete = async (row) => {
    console.log(row);
    messageBox
      .confirm(t('common.deleteConfirmText'), t('common.deleteConfirm'), {
        type: 'warning',
        draggable: true
      })
      .then(async () => {
        try {
          const res = await deleteCPayPoolApi({ id: row.id });
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
        label: t('payPool.poolName'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'code',
        prop: 'code',
        label: t('payPool.poolCode'),
        align: 'center',
        minWidth: 120
      },
      // {
      //   columnKey: 'limitAmount',
      //   prop: 'limitAmount',
      //   label: t('payPool.totalAmountLimit'),
      //   align: 'center',
      //   minWidth: 120
      // },
      // {
      //   columnKey: 'limitAmountDay',
      //   prop: 'limitAmountDay',
      //   label: t('payPool.dailyAmountLimit'),
      //   align: 'center',
      //   minWidth: 120
      // },
      {
        columnKey: 'limitAmountMin',
        prop: 'limitAmountMin',
        label: t('payPool.singleMinAmount'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'limitAmountMax',
        prop: 'limitAmountMax',
        label: t('payPool.singleMaxAmount'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'allAccountNum',
        prop: 'allAccountNum',
        label: t('common.accountCount'),
        slot: 'allAccountNumSlot',
        align: 'center',
        minWidth: 120,
        formatter: (row) => {
          return `${t('common.totalText')}：${row.allAccountNum}\r\n${t('common.enableText')}：${row.validAccountNum}\r\n${t('common.disableText')}：${row.invalidAccountNum}`;
        }
      },
      {
        columnKey: 'remark',
        prop: 'remark',
        label: t('common.notes'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'createdAt',
        prop: 'createdAt',
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
        label: '操作',
        align: 'center',
        slot: 'action',
        width: 370,
        fixed: 'right'
      }
    ];
  });

  /** 表格数据源 */
  const datasource = async ({ pages, where, orders, filters }) => {
    try {
      const data = await getCPayPoolListApi({
        ...where,
        page: pages.page || 1, // 使用 pages.currentPage 或默认值 1
        pageSize: pages.limit || 10 // 使用 pages.pageSize 或默认值 10
      });
      return data;
    } catch (e) {
      EleMessage.error(e.message);
    }
  };

  onMounted(() => {
  });
</script>

<style lang="scss" scoped></style>
