<template>
  <ele-page>
    <ele-card>
      <!-- 搜索表单 -->
      <search-form @search="onSearch" :apiGroup="apiGroupOptions" />
      <ele-pro-table
        ref="tableRef"
        row-key="memberid"
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
      >
        <template #toolbar>
          <!-- <el-button icon="compass" @click="onSync">同步API</el-button> -->
        </template>
        <!-- 表头工具按钮 -->
        <template #tools>
          <div style="display: flex; align-items: center; flex-wrap: wrap">
            <div>高度铺满&nbsp;</div>
            <el-switch v-model="fixedHeight" size="small" />
            <el-divider direction="vertical" />
          </div>
        </template>


        <template #validSlot="{ row }">
          <el-tag v-if="row.cType == 1" type="success">启用</el-tag>
          <el-tag v-if="row.cType == 2" type="danger">停用</el-tag>
        </template>

        <template #action="{ row }">
          <el-button type="danger" link icon="delete" @click="handleDraw(row)">删除</el-button>
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
  import { getOrderPayPlayerListApi } from '@/api/order/index.js';
  import { ElMessageBox } from 'element-plus/es';
  import { ElMessage } from 'element-plus';

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
        columnKey: 'id',
        prop: 'id',
        label: 'ID',
        align: 'center'
      },
      {
        columnKey: 'historyAccount',
        prop: 'historyAccount',
        label: '历史账号',
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'playerMsg',
        prop: 'playerMsg',
        label: '用户标识名称/描述',
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'remark',
        prop: 'remark',
        label: '备注',
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'valid',
        prop: 'valid',
        label: '是否启用',
        slot: 'validSlot',
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'updateTime',
        prop: 'updateTime',
        label: '更新时间',
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'createBy',
        prop: 'createBy',
        label: '创建人',
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'createTime',
        prop: 'createTime',
        label: '创建时间',
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'action',
        prop: 'action',
        label: '操作',
        align: 'center',
        slot: 'action',
        width: 200,
        fixed: 'right'
      }
    ];
  });

  /** 表格数据源 */
  // 表格数据源, 数据源可以直接为数组或一个 function 返回一个 Promise
  const datasource = async ({ pages, where, orders, filters }) => {
    try {
      const data = await getOrderPayPlayerListApi({
        ...where,
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

<style lang="scss" scoped>
  /* 按钮风格增加灰色背景 */
  .demo-card-button :deep(.el-tabs__header) {
    background: var(--el-bg-color-page);
    box-shadow: -8px 0 0 0 var(--el-bg-color-page);
  }

  /* 自定义样式 */
  div.demo-tabs.ele-tabs :deep(.el-tabs__header) {
    --ele-tab-padding: 0;
    --ele-tab-height: 46px;
    --ele-tab-font-size: 16px;
    --ele-tab-simple-hover-color: var(--el-color-primary);
    --ele-tab-simple-hover-bg: none;
    --ele-tab-simple-active-bg: none;
    --ele-tab-simple-active-weight: normal;

    .el-tabs__item {
      & + .el-tabs__item {
        margin-left: 40px;
      }

      &.is-active::after {
        height: 3px;
        width: 20px !important;
        left: 50%;
        transform: translateX(-50%);
        display: block;
      }
    }
  }
</style>
