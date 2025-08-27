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

        <template #visibleSlot="{ row }">
          <ele-dot v-if="row.visible === 1" text="显示" type="success" size="8px" />
          <ele-dot v-else-if="row.visible === -1" text="隐藏" size="8px" type="danger" :ripple="false" />
        </template>

        <template #statusSlot="{ row }">
          <ele-dot v-if="row.status === 1" text="匹配中" type="success" size="8px" />
          <ele-dot v-else-if="row.status === 2" text="正确匹配" size="8px" type="success" :ripple="false" />
          <ele-dot v-else-if="row.status === 3" text="匹配失败" size="8px" type="danger" :ripple="false" />
        </template>

        <template #action="{ row }">
          <el-link type="primary" :underline="false" icon="Plus" @click="handleAddSubDomain(row, false)">添加子域名</el-link>
          <el-divider direction="vertical" />
          <el-link :type="row.visible === 1 ? 'danger' : 'primary'" icon="Open" :underline="false" @click="handleVisible(row)">{{ row.visible === 1 ? '隐藏' : '显示' }}</el-link>
          <el-divider direction="vertical" />
          <el-link type="primary" :underline="false" icon="view" @click="handleAddSubDomain(row, true)">查看解析设置</el-link>
        </template>
      </ele-pro-table>
    </ele-card>

    <!-- 编辑app -->
    <edit-from v-model="showEdit" :data="current" @done="reload" />

    <!-- 添加子域名 -->
    <sub-domain-form v-model="showSubDomain" :data="current" :is-view="isView" @done="reload" />
  </ele-page>
</template>
<script setup>
  import { ref, reactive, computed, onMounted, watch } from 'vue';
  import SearchForm from './components/search-form.vue';
  import { PlusOutlined } from '@/components/icons';
  import { EleMessage } from 'ele-admin-plus';
  import dayjs from 'dayjs';
  import EditFrom from './components/edit-form.vue';
  import SubDomainForm from './components/subdomain-form.vue';
  import { getCDominManageListApi, visibleCDominManageApi } from '@/api/app/index.js';
  import { useMessageBox } from 'ele-admin-plus/es';
  import { _timeFormat } from '@/utils/public.js';
  import { useI18n } from 'vue-i18n';

  const { t } = useI18n();

  const messageBox = useMessageBox();

  // 定义图片基础URL
  const imageBaseUrl = import.meta.env.VITE_APP_IMAGE_URL || '';

  // 获取完整图片URL的方法
  const getImageUrl = (imagePath) => {
    if (!imagePath) return '';
    if (imagePath.startsWith('http')) return imagePath; // 如果已经是完整URL
    return imageBaseUrl + imagePath;
  };
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

  const showSubDomain = ref(false);
  const isView = ref(false);

  const handleAddSubDomain = (row, isViewBool) => {
    current.value = row ?? null;
    showSubDomain.value = true;
    isView.value = isViewBool;
  };

  const showEdit = ref(false);

  /** 打开编辑弹窗 */
  const handleEdit = (row) => {
    current.value = row ?? null;
    showEdit.value = true;
  };

  // 显示/隐藏 使用messageBox提示
  const handleVisible = async (row) => {
    messageBox
      .confirm(row.visible === 1 ? '确定要隐藏吗？' : '确定要显示吗？', '提示', {
        type: 'warning',
        draggable: true
      })
      .then(async () => {
        try {
          const res = await visibleCDominManageApi({ id: row.id, visible: row.visible === 1 ? -1 : 1 });
          EleMessage.success(res.msg);
          doReload();
        } catch (error) {
          EleMessage.error(error.message);
        }
      });
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
          const res = await deleteAppApi({ id: row.id });
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
        columnKey: 'id',
        prop: 'id',
        label: 'ID',
        align: 'center'
      },
      {
        columnKey: 'domain',
        prop: 'domain',
        label: '域名地址',
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'resolveIp',
        prop: 'resolveIp',
        label: '解析IP',
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'accessType',
        prop: 'accessType',
        label: '接入方式',
        align: 'center',
        minWidth: 120,
        formatter: (row, column, cellValue, index) => {
          switch (cellValue) {
            case 1:
              return '域名托管';
            default:
              return '-';
          }
        }
      },
      {
        columnKey: 'status',
        prop: 'status',
        label: '状态',
        slot: 'statusSlot',
        align: 'center',
        minWidth: 120,
        formatter: (row, column, cellValue, index) => {
          switch (cellValue) {
            case 1:
              return '匹配中';
            case 2:
              return '正确匹配';
            case 3:
              return '匹配失败';
            default:
              return '-';
          }
        }
      },
      {
        columnKey: 'visible',
        prop: 'visible',
        label: '显示状态',
        slot: 'visibleSlot',
        align: 'center',
        minWidth: 120,
        formatter: (row, column, cellValue, index) => {
          switch (cellValue) {
            case 1:
              return '显示';
            case -1:
              return '隐藏';
            default:
              return '-';
          }
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
        label: '操作',
        align: 'center',
        slot: 'action',
        width: 320,
        fixed: 'right'
      }
    ];
  });

  /** 表格数据源 */
  const datasource = async ({ pages, where, orders, filters }) => {
    try {
      const data = await getCDominManageListApi({
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
