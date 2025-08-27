<template>
  <ele-page>
    <!-- Search form -->
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
        <!-- Header tool buttons -->
        <template #tools></template>

        <template #statusSlot="{ row }">
          <ele-dot v-if="row.status === 1" :text="t('common.enable')" type="success" size="8px" />
          <ele-dot v-else-if="row.status === 0" :text="t('common.disable')" size="8px" type="danger" :ripple="false" />
        </template>

        <template #appNameSlot="{ row }">
          <div class="app-info-card">
            <!-- APP Icon -->
            <div class="app-icon">
              <img v-if="row.appIcon" :src="getImageUrl(row.appIcon)" :alt="row.appName" />
              <div v-else class="default-icon">
                <el-icon size="24"><Picture /></el-icon>
              </div>
            </div>

            <!-- APP information -->
            <div class="app-details">
              <!-- APP Name -->
              <div class="app-name">{{ row.appName || 'Not set' }}</div>

              <!-- 域名ID -->
              <div class="app-id">
                <span class="label">ID:</span>
                <span class="value">{{ row.domainId || 'Not set' }}</span>
              </div>

              <!-- APP Notes -->
              <div class="app-remark">{{ row.appRemark || 'No remarks yet' }}</div>
            </div>
          </div>
        </template>

        <template #action="{ row }">
          <el-link type="primary" :underline="false" icon="edit" @click="handleEdit(row)">{{ t('common.edit') }}</el-link>
          <el-divider direction="vertical" />
          <el-link type="danger" :underline="false" icon="Delete" @click="handleDelete(row)">{{ t('common.deleted') }}</el-link>
        </template>
      </ele-pro-table>
    </ele-card>

    <!-- Editing app -->
    <edit-from v-model="showEdit" :data="current" @done="reload" />
  </ele-page>
</template>
<script setup>
  import { deleteAppApi, getAppPageListApi } from '@/api/app/index.js';
  import { PlusOutlined } from '@/components/icons';
  import { _timeFormat } from '@/utils/public.js';
  import { Picture } from '@element-plus/icons-vue';
  import { EleMessage } from 'ele-admin-plus';
  import { useMessageBox } from 'ele-admin-plus/es';
  import { computed, onMounted, reactive, ref } from 'vue';
  import { useI18n } from 'vue-i18n';
  import EditFrom from './components/edit-form.vue';
  import SearchForm from './components/search-form.vue';

  const { t } = useI18n();

  const messageBox = useMessageBox();

  // Define the base URL of the image
  const imageBaseUrl = import.meta.env.VITE_APP_IMAGE_URL || '';

  // How to get the full image URL
  const getImageUrl = (imagePath) => {
    if (!imagePath) return '';
    if (imagePath.startsWith('http')) return imagePath; // If it is already a complete URL
    return imageBaseUrl + imagePath;
  };
  /** Table Example */
  const tableRef = ref(null);
  const bordered = ref(true);

  /** Table search parameters */
  const lastWhere = reactive({});
  /** Select data in the table */
  const selections = ref([]);
  /** Select data in table */
  const current = ref(null);
  /** 表格固定高度 */
  const fixedHeight = ref(false);
  /** Refresh the table */
  const reload = (where) => {
    tableRef.value?.reload?.({ page: 1, where });
  };

  /** Search events */
  const onSearch = (where) => {
    Object.assign(lastWhere, where);
    doReload();
  };

  /** Table Search */
  const doReload = () => {
    reload(lastWhere);
  };

  const response = ref({
    dataName: 'list', // Field name of the data list, nested is supported, for example: 'data.rows''
    countName: 'total' // The field name of the total data, for example: 'data.total'
  });

  const showEdit = ref(false);

  /** Open the edit popup */
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
        columnKey: 'appName',
        prop: 'appName',
        label: t('appManage.appInfo'),
        slot: 'appNameSlot',
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'projectPath',
        prop: 'projectPath',
        label: t('appManage.apkFile'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'status',
        prop: 'status',
        label: t('common.status'),
        slot: 'statusSlot',
        align: 'center',
        minWidth: 120,
        formatter: (row, column, cellValue, index) => {
          return cellValue == 1 ? t('common.enable') : t('common.disable');
        }
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
        label: 'operate',
        align: 'center',
        slot: 'action',
        width: 160,
        fixed: 'right'
      }
    ];
  });

  /** Table data source */
  const datasource = async ({ pages, where, orders, filters }) => {
    try {
      const data = await getAppPageListApi({
        ...where,
        page: pages.page || 1, // Use pages.currentPage or the default value of 1
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
  .app-info-card {
    display: flex;
    align-items: flex-start;
    gap: 12px;
    padding: 8px;

    .app-icon {
      flex-shrink: 0;
      width: 48px;
      height: 48px;
      border-radius: 8px;
      overflow: hidden;
      border: 1px solid #e4e7ed;
      display: flex;
      align-items: center;
      justify-content: center;
      background: #f5f7fa;

      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }

      .default-icon {
        color: #c0c4cc;
        display: flex;
        align-items: center;
        justify-content: center;
      }
    }

    .app-details {
      flex: 1;
      min-width: 0;

      .app-name {
        font-size: 14px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
        line-height: 1.4;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        text-align: left;
      }

      .app-id {
        font-size: 12px;
        color: #606266;
        margin-bottom: 4px;
        display: flex;
        align-items: center;
        gap: 4px;

        .label {
          color: #909399;
          font-weight: 500;
        }

        .value {
          color: #67c23a;
          font-family: 'Courier New', monospace;
          font-weight: 600;
        }
      }

      .app-remark {
        font-size: 12px;
        color: #909399;
        line-height: 1.3;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        text-align: left;
      }
    }
  }

  // 悬停效果
  .app-info-card:hover {
    .app-icon {
      border-color: #67c23a;
      background: #f0f9ff;
    }

    .app-details .app-name {
      color: #67c23a;
    }
  }
</style>
