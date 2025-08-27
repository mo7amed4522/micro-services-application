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
        :sticky="!fixedHeight"
        :toolbar="{ theme: 'default' }"
        :default-sort="{ prop: 'createTime', order: 'descending' }"
        :footer-style="{ paddingBottom: '12px' }"
        :tableStyle="{ paddingBottom: '0', color: 'rgba(0, 0, 0, 0.65)' }"
      >
        <template #toolbar>
          <el-button type="primary" :icon="PlusOutlined" class="ele-btn-icon" @click="handleEdit()">{{ t('merchant.addBtn') }}</el-button>
        </template>
        <!-- 表头工具按钮 -->
        <template #tools>
        </template>

        <template #enable="{ row }">
          <el-switch v-model="row.enable" :active-value="1" :inactive-value="0" />
        </template>

        <template #statusSlot="{ row }">
          <ele-dot v-if="row.status === 1" :text="t('common.enable')" type="success" size="8px" />
          <ele-dot v-else-if="row.status === 0" :text="t('common.disable')" size="8px" type="danger" :ripple="false" />
        </template>

        <template #action="{ row }">
          <el-link type="primary" :underline="false" icon="edit" @click="handleChanel(row)">{{ t('merchant.assignChannel') }}</el-link>
          <el-divider direction="vertical" />
          <el-link type="primary" :underline="false" icon="edit" @click="handleAssignLabel(row)">{{ t('merchant.tags') }}</el-link>
          <el-divider direction="vertical" />
          <ele-dropdown
            :items="[
              {
                title: t('common.edit'),
                command: 'edit',
                icon: EditOutlined
              },
              {
                title: row.status == 1 ? t('merchant.disabled') : t('common.enable'),
                command: 'disabled',
                icon: MinusCircleOutlined
              },
              {
                title: t('common.deleted'),
                command: 'del',
                icon: DeleteOutlined,
                danger: true,
                divided: true
              }
            ]"
            style="display: inline"
            @command="(command) => onDropClick(command, row)"
          >
            <el-link type="primary" :underline="false">
              <span>{{ t('common.more') }}</span>
              <el-icon :size="12" style="vertical-align: -1px; margin-left: 2px">
                <ArrowDown />
              </el-icon>
            </el-link>
          </ele-dropdown>
        </template>
      </ele-pro-table>
    </ele-card>

    <!-- 修改表单 -->
    <edit-from v-model="showEdit" :data="current" @done="reload" />
    <!-- 分配渠道 -->
    <assign-channel v-model="showBindChannel" :data="current" @done="reload" />
    <!-- 分配标签 -->
    <assign-label v-model="showAssignLabel" :data="current" @done="reload" />
  </ele-page>
</template>
<script setup>
  import { ref, reactive, computed, onMounted, watch } from 'vue';
  import dayjs from 'dayjs';
  import SearchForm from './components/search-form.vue';
  import { PlusOutlined } from '@/components/icons';
  import { EleMessage } from 'ele-admin-plus';
  import EditFrom from './components/edit-form.vue';
  import AssignChannel from '@/views/merchant/index/components/assign-channel.vue';
  import AssignLabel from './components/assign-label.vue'
  import { getMUserList, deleteMUserApi, updateMUserStatusApi } from '@/api/merchant/index.js';
  import { ArrowDown, DeleteOutlined, EditOutlined, MinusCircleOutlined } from '@/components/icons/index.js';
  import { useMessageBox } from 'ele-admin-plus/es';
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

  const showBindChannel = ref(false);

  const handleChanel = (row) => {
    current.value = row ?? null;
    showBindChannel.value = true;
  };

  const showAssignLabel = ref(false)

  const handleAssignLabel = (row) => {
    current.value = row ?? null;
    showAssignLabel.value = true;
  }

  // 删除
  const handleDelete = async (row) => {
    messageBox
      .confirm(t('common.deleteConfirmText'), t('common.deleteConfirm'), {
        type: 'warning',
        draggable: true
      })
      .then(async () => {
        try {
          const res = await deleteMUserApi({ id: row.id });
          EleMessage.success(res.msg);
          doReload();
        } catch (error) {
          EleMessage.error(error.message);
        }
      })
      .catch(() => {});
  };

  // 禁用/启用
  const handleStatus = async (row) => {
    let msg = row.status == 1 ? t('common.disable') : t('common.enable');
    messageBox
      .confirm(t('common.confirmText') + msg + '? ', t('common.operaConfirm'), {
        type: 'warning',
        draggable: true
      })
      .then(async () => {
        try {
          const res = await updateMUserStatusApi({ id: row.id, status: row.status == 1 ? 0 : 1 });
          EleMessage.success(res);
          doReload();
        } catch (error) {
          EleMessage.error(error.message);
        }
      })
      .catch(() => {
        console.log('取消操作')
      });
  };

  const onDropClick = (command, row) => {
    if (command === 'edit') {
      handleEdit(row);
    } else if (command === 'del') {
      handleDelete(row);
    } else if (command === 'disabled') {
      handleStatus(row);
    }
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
        columnKey: 'status',
        prop: 'status',
        label: t('merchant.status'),
        align: 'center',
        slot: 'statusSlot',
        minWidth: 120,
        formatter: (row, column, cellValue, index) => {
          return cellValue == 1 ? t('common.enable') : t('common.disable');
        }
      },
      {
        columnKey: 'valid',
        prop: 'valid',
        label: t('merchant.payOutStatus'),
        align: 'center',
        formatter: (row) => (row.valid === 1 ? t('common.valid') : t('common.invalid')),
        minWidth: 120
      },
      {
        columnKey: 'name',
        prop: 'name',
        label: t('merchant.username'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'no',
        prop: 'no',
        label: t('merchant.merchantNum'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'isLimitIp',
        prop: 'isLimitIp',
        label: t('merchant.restriction'),
        align: 'center',
        formatter: (row) => (row.isLimitIp === 1 ? t('common.open') : t('common.close')),
        minWidth: 120
      },
      {
        columnKey: 'remark',
        prop: 'remark',
        label: t('common.notes'),
        align: 'center',
        minWidth: 180
      },
      {
        columnKey: 'createdAt',
        prop: 'createdAt',
        label: t('common.createTime'),
        align: 'center',
        minWidth: 180,
        formatter: (row, column, cellValue, index) => {
          return _timeFormat(cellValue);
        }
      },
      {
        columnKey: 'action',
        prop: 'action',
        label: t('common.action'),
        align: 'center',
        slot: 'action',
        width: 280,
        fixed: 'right'
      }
    ];
  });

  /** 表格数据源 */
  // 表格数据源, 数据源可以直接为数组或一个 function 返回一个 Promise
  const datasource = async ({ pages, where, orders, filters }) => {
    try {
      const data = await getMUserList({
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

<style lang="scss" scoped></style>
