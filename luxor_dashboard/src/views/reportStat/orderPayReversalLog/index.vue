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
        </template>
        <!-- 表头工具按钮 -->
        <template #tools>
        </template>

        <template #validSlot="{ row }">
          <ele-dot v-if="row.valid === 1" :text="t('common.enable')" type="success" size="8px" />
          <ele-dot v-else-if="row.valid === 0" :text="t('common.disable')" size="8px" type="danger" :ripple="false" />
        </template>

        <template #avgDelayTime="{ row }">
          <span>{{ formatTime(row.avgDelayTime) }}</span>
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
  import { formatTime } from '@/utils/public';
  import { orderPayReversalLogListApi } from '@/api/report/index.js';
  import { useMessageBox } from 'ele-admin-plus/es';
  import { getUserList } from '@/api/user'
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
        columnKey: 'username',
        prop: 'username',
        label: t('operatorOrderReplenish.operatorText'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'successCount',
        prop: 'successCount',
        label: t('operatorOrderReplenish.successOrderCountText'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'afterCount',
        prop: 'afterCount',
        label: t('operatorOrderReplenish.playerFillsFirst'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'beforeCount',
        prop: 'beforeCount',
        label: t('operatorOrderReplenish.PlayerFillsLater'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'outCount',
        prop: 'outCount',
        label: t('operatorOrderReplenish.timeoutOrderCountText'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'avgDelayTime',
        prop: 'avgDelayTime',
        label: t('operatorOrderReplenish.averageTimeDifferenceText'),
        align: 'center',
        slot: 'avgDelayTime',
        minWidth: 120
      },
      {
        columnKey: 'successRatioBefore',
        prop: 'successRatioBefore',
        label: t('common.percentageText'),
        align: 'center',
        minWidth: 120,
        formatter: (row) => {
          return row.successRatioBefore + '%';
        }
      }
    ];
  });

  /** 表格数据源 */
  const datasource = async ({ pages, where, orders, filters }) => {
    try {
      const data = await orderPayReversalLogListApi({
        ...where,
        page: pages.page || 1, // 使用 pages.currentPage 或默认值 1
        pageSize: pages.limit || 10 // 使用 pages.pageSize 或默认值 10
      });
      return data;
    } catch (e) {
      EleMessage.error(e.message);
    }
  };

  const userList = ref([])

  const getAllUserList = async () => {
    const res = await getUserList({
      page: 1,
      pageSize: 9999
    });
    userList.value = res.list
  }

  onMounted(() => {
    getAllUserList()
  });
</script>

<style lang="scss" scoped></style>
