<template>
  <ele-page>
    <ele-card>
      <ele-pro-table
        ref="tableRef"
        row-key="authorityId"
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
          <el-button type="primary" :icon="PlusOutlined" class="ele-btn-icon" @click="handleEdit()">{{ t('common.add') }}</el-button>
        </template>
        <!-- 表头工具按钮 -->
        <template #tools>
        </template>

        <template #action="{ row }">
          <el-button type="primary" link icon="setting" @click="openDrawer(row)">{{ t('authority.setAuthority') }}</el-button>
          <!-- <el-button type="primary" link icon="plus" @click="handleEdit(row)">新增子角色</el-button> -->
          <!-- <el-button type="primary" link icon="copy-document" @click="handleEdit(row)">拷贝</el-button> -->
          <el-button type="primary" link icon="edit" @click="handleEdit(row)">{{ t('common.edit') }}</el-button>
        </template>
      </ele-pro-table>
    </ele-card>
  </ele-page>

   <edit-from v-model="showEdit" :data="currentData" @done="reload" />
  <!--  <api-form v-model="showPermission" :data="currentData" @done="reload" />-->
  <el-drawer v-if="drawer" v-model="drawer" :with-header="false" size="40%" :title="t('authority.roleConfigText')">
    <el-tabs :before-leave="autoEnter" type="border-card">
      <el-tab-pane :label="t('authority.roleMenuText')">
        <Menus ref="menus" :row="activeRow" @changeRow="changeRow" />
      </el-tab-pane>
      <el-tab-pane :label="t('authority.roleApiText')">
        <Apis ref="apis" :row="activeRow" @changeRow="changeRow" />
      </el-tab-pane>
    </el-tabs>
  </el-drawer>
</template>
<script setup>
  import { ref, reactive, computed, onMounted, watch } from 'vue';
  import SearchForm from './components/search-form.vue';
  import { PlusOutlined } from '@/components/icons';
  import { EleMessage } from 'ele-admin-plus';
  import EditFrom from './components/edit-form.vue';
  import Menus from './components/menus.vue';
  import { getAuthorityList } from '@/api/user/index.js';
  import Apis from '@/views/sys/authority/components/api.vue';
  import { useI18n } from 'vue-i18n';

  const { t } = useI18n();

  /** 表格实例 */
  const tableRef = ref(null);
  const bordered = ref(true);

  /** 表格搜索参数 */
  const lastWhere = reactive({});

  const currentData = ref({});
  const menus = ref(null);
  const apis = ref(null);
  const drawer = ref(false);
  const dialogType = ref('add');
  const activeRow = ref({});

  const autoEnter = (activeName, oldActiveName) => {
    const paneArr = [menus, apis];
    if (oldActiveName) {
      if (paneArr[oldActiveName].value.needConfirm) {
        paneArr[oldActiveName].value.enterAndNext();
        paneArr[oldActiveName].value.needConfirm = false;
      }
    }
  };

  const openDrawer = (row) => {
    activeRow.value = row ?? null
    drawer.value = true;
  };

  const changeRow = (key, value) => {
    activeRow.value[key] = value;
  };

  /** 刷新表格 */
  const reload = (where) => {
    tableRef.value?.reload?.({ page: 1, where });
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
  const showPermission = ref(false);

  const handleEdit = (row) => {
    currentData.value = row ?? null;
    showEdit.value = true;
  };
  const handlePermission = (row) => {
    currentData.value = row || {};
    showPermission.value = true;
  };

  /** 表格列配置 */
  const columns = computed(() => {
    return [
      {
        columnKey: 'authorityId',
        prop: 'authorityId',
        label: t('authority.roleId'),
        align: 'center'
      },
      {
        columnKey: 'authorityName',
        prop: 'authorityName',
        label: t('authority.roleName'),
        align: 'center'
      },
      {
        columnKey: 'action',
        prop: 'action',
        label: t('common.action'),
        align: 'center',
        slot: 'action',
        width: 350
      }
    ];
  });
  /** 表格数据源 */
  // 表格数据源, 数据源可以直接为数组或一个 function 返回一个 Promise
  const datasource = async ({ pages, where, orders, filters }) => {
    try {
      const data = await getAuthorityList({
        ...where
      });
      return data;
    } catch (e) {
      EleMessage.error(e.message);
    }
  };
  onMounted(async () => {});

  /** 表格选中数据 */
  const selections = ref([]);

  /** 表格单选选中数据 */
  const current = ref(null);

  /** 表格固定高度 */
  const fixedHeight = ref(false);
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
