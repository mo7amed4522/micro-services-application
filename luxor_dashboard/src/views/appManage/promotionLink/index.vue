<template>
  <ele-page>
    <!-- 搜索表单 -->
    <search-form :appList="appList" :domainList="domainList" :templateList="templateList" @search="onSearch" />
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
          <el-button type="primary" :icon="PlusOutlined" class="ele-btn-icon" @click="handleEdit()">新增链接</el-button>
        </template>
        <!-- 表头工具按钮 -->
        <template #tools></template>

        <template #action="{ row }">
          <el-link type="primary" :underline="false" icon="edit" @click="handleEdit(row)">修改</el-link>
          <el-divider direction="vertical" />
          <el-link type="danger" :underline="false" icon="Delete" @click="handleDelete(row)">{{ t('common.deleted') }}</el-link>
        </template>
      </ele-pro-table>
    </ele-card>

    <!-- 修改表单 -->
    <edit-form v-model="showEdit" :data="current" :appList="appList" :domainList="domainList" :templateList="templateList" @done="reload" />
  </ele-page>
</template>
<script setup>
  import { ref, reactive, computed, onMounted, watch } from 'vue';
  import SearchForm from './components/search-form.vue';
  import { EleMessage } from 'ele-admin-plus';
  import EditForm from './components/edit-form.vue';
  import { getCPromotionLinkListApi, deleteCPromotionLinkApi, getAppPageListApi, getCDominManageListApi, getCPageTemplateListApi } from '@/api/app'
  import { useMessageBox } from 'ele-admin-plus/es';
  import { PlusOutlined } from '@/components/icons';
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

  const showEdit = ref(false);

  /** 打开编辑弹窗 */
  const handleEdit = (row) => {
    current.value = row ?? null;
    showEdit.value = true;
  };

  // 删除
  const handleDelete = async (row) => {
    messageBox
      .confirm(t('common.deleteConfirmText'), t('common.deleteConfirm'), {
        type: 'warning',
        draggable: true
      })
      .then(async () => {
        try {
          const res = await deleteCPromotionLinkApi({ id: row.id });
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
        columnKey: 'name',
        prop: 'name',
        label: '链接名称',
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'appName',
        prop: 'appName',
        label: '所属应用',
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'templateName',
        prop: 'templateName',
        label: '所属模版',
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'domainURL',
        prop: 'domainURL',
        label: '所属域名',
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'iosRedirectUrl',
        prop: 'iosRedirectUrl',
        label: 'ios跳转url',
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'url',
        prop: 'url',
        label: '包网下载地址',
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'isAllowEditUrl',
        prop: 'isAllowEditUrl',
        label: '允许修改url',
        align: 'center',
        minWidth: 120,
        formatter: (row, column, cellValue, index) => {
          return cellValue === 1 ? '允许' : '不允许';
        }
      },
      {
        columnKey: 'remark',
        prop: 'remark',
        label: '备注',
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
      const data = await getCPromotionLinkListApi({
        ...where,
        page: pages.page || 1, // 使用 pages.currentPage 或默认值 1
        pageSize: pages.limit || 10 // 使用 pages.pageSize 或默认值 10
      });
      return data;
    } catch (e) {
      EleMessage.error(e.message);
    }
  };

  const appList = ref([]);
  const getAppList = async () => {
    const res = await getAppPageListApi({
      page: 1,  
      pageSize: 1000
    });
    appList.value = res.list;
  };

  const domainList = ref([]);
  const getDomainList = async () => {
    const res = await getCDominManageListApi({
      page: 1,
      pageSize: 1000
    });
    domainList.value = res.list;
  };

  const templateList = ref([]);
  const getTemplateList = async () => {
    const res = await getCPageTemplateListApi({
      page: 1,
      pageSize: 1000
    });
    templateList.value = res.list;
  };


  onMounted(() => {
    getAppList();
    getDomainList();
    getTemplateList();
  });
</script>

<style lang="scss" scoped></style>
