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
      >
        <template #toolbar>
          <el-button type="primary" :icon="PlusOutlined" class="ele-btn-icon" @click="handleEdit()">新增api</el-button>
          <el-button icon="refresh" @click="onFresh">刷新缓存</el-button>
          <el-button icon="compass" @click="onSync">同步API</el-button>
        </template>
        <!-- 表头工具按钮 -->
        <template #tools>
          <div style="display: flex; align-items: center; flex-wrap: wrap">
            <div>高度铺满&nbsp;</div>
            <el-switch v-model="fixedHeight" size="small" />
            <el-divider direction="vertical" />
          </div>
        </template>

        <template #enable="{ row }">
          <el-switch v-model="row.enable" :active-value="1" :inactive-value="0" />
        </template>

        <template #action="{ row }">
          <el-button type="primary" link icon="edit" @click="handleEdit(row)">编辑</el-button>
          <el-button type="danger" link icon="delete" @click="handleDraw(row)">删除</el-button>
        </template>
      </ele-pro-table>
    </ele-card>
    <api-edit v-model="showEdit" :data="current" @done="reload" :apiGroup="apiGroupOptions" />
  </ele-page>
</template>
<script setup>
  import { ref, reactive, computed, onMounted, watch } from 'vue';
  import SearchForm from './components/search-form.vue';
  import { PlusOutlined } from '@/components/icons';
  import { EleMessage } from 'ele-admin-plus';
  import EditFrom from './components/edit-form.vue';
  import { getUserList } from '@/api/user/index.js';
  import { freshCasbin, getAllApis, getApiGroups, getApiList, syncApi } from '@/api/sys/api/index.js';
  import ApiEdit from '@/views/sys/api/components/api-edit.vue';
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

  const showEdit = ref(false);
  /** 打开编辑弹窗 */
  const handleEdit = (row) => {
    current.value = row ?? null;
    showEdit.value = true;
  };

  const onFresh = async () => {
    ElMessageBox.confirm('确定要刷新缓存吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(async () => {
      const res = await freshCasbin();
      ElMessage.success(res);
    });
  };

  const syncApiData = ref({
    newApis: [],
    deleteApis: [],
    ignoreApis: []
  });
  const syncApiFlag = ref(false);
  const onSync = async () => {
    const res = await syncApi();
    // res.data.newApis.forEach((item) => {
    //   item.apiGroup = apiGroupMap.value[item.path.split('/')[1]];
    // });
    syncApiData.value = res.data;
    syncApiFlag.value = true;
    ElMessage.success(res.msg);
  };
  /** 表格列配置 */
  const columns = computed(() => {
    return [
      {
        columnKey: 'ID',
        prop: 'ID',
        label: 'ID',
        align: 'center'
      },
      {
        columnKey: 'path',
        prop: 'path',
        label: 'API路径',
        align: 'center'
      },
      {
        columnKey: 'apiGroup',
        prop: 'apiGroup',
        label: 'API分组',
        align: 'center'
      },
      {
        columnKey: 'description',
        prop: 'description',
        label: 'API简介',
        align: 'center'
      },
      {
        columnKey: 'method',
        prop: 'method',
        label: '请求',
        align: 'center'
      },
      {
        columnKey: 'action',
        prop: 'action',
        label: '操作',
        align: 'center',
        slot: 'action',
        width: 200
      }
    ];
  });

  /** 表格数据源 */
  const datasource = async ({ pages, where, orders, filters }) => {
    try {
      const data = await getApiList({
        ...where,
        page: pages.page || 1, // 使用 pages.currentPage 或默认值 1
        pageSize: pages.limit || 10 // 使用 pages.pageSize 或默认值 10
      });
      await getGroup();
      return data;
    } catch (e) {
      EleMessage.error(e.message);
    }
  };

  const getGroup = async () => {
    try {
      const res = await getApiGroups();
      const groups = res.groups;
      apiGroupOptions.value = groups.map((item) => ({ label: item, value: item }));
      apiGroupMap.value = res.apiGroupMap;
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
