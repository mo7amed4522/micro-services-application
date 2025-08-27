<template>
  <ele-page>
    <ele-card>
      <!-- 搜索表单 -->
      <search-form @search="onSearch" />
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
          <el-button type="primary" :icon="PlusOutlined" class="ele-btn-icon" @click="handleEdit()">{{ t('common.add') }}</el-button>
        </template>
        <!-- 表头工具按钮 -->
        <template #tools>
          <!-- <div style="display: flex; align-items: center; flex-wrap: wrap">
            <div>高度铺满&nbsp;</div>
            <el-switch v-model="fixedHeight" size="small" />
            <el-divider direction="vertical" />
          </div> -->
        </template>

        <template #enable="{ row }">
          <ele-dot v-if="row.enable === 1" :text="t('common.enable')" type="success" size="8px" />
          <ele-dot v-else-if="row.enable === 2" :text="t('common.disable')" size="8px" type="danger" :ripple="false" />
        </template>

        <template #googleAuthSecret="{ row }">
          <el-button v-if="row.google_auth_secret" type="primary" link @click="openQRCodeDialog(row)">{{ t('common.viewQrcode') }}</el-button>
        </template>

        <template #authorityNameSlot="{ row }">
          <span>{{ row?.authority?.authorityName }}</span>
        </template>

        <!--        <template #lastTime="{ row }">-->
        <!--          <div>{{ row.last_time }}</div>-->
        <!--          <div>{{ row.last_ip }}</div>-->
        <!--        </template>-->

        <template #action="{ row }">
          <el-button type="primary" link icon="edit" @click="handleUpdatePass(row)">{{ t('layout.header.password') }}</el-button>
          <el-button type="primary" link icon="edit" @click="handleEdit(row)">{{ t('common.edit') }}</el-button>
          <el-button type="danger" link icon="delete" @click="deleteUserFunc(row)">{{ t('common.deleted') }}</el-button>
        </template>
      </ele-pro-table>
    </ele-card>
  </ele-page>

  <edit-from v-model="showEdit" :data="current" @done="reload" />
  <!-- 二维码弹窗 -->
  <ele-modal form :width="550" :model-value="qrCodeModelValue" :title="t('common.viewQrcode')" @update:modelValue="qrCodeModelValue = false">
    <div style="text-align: center">
      <el-image v-if="qrCodeSrc" :src="qrCodeSrc" style="width: 260px; height: 260px" fit="contain"></el-image>
    </div>
    <div class="qrcode-secret">
      <div class="title">{{ googleAuthSecret }}</div>
      <el-button type="primary" @click="copyText(googleAuthSecret, t)">{{ t('common.copy') }}</el-button>
    </div>
    <template #footer>
      <el-button @click="qrCodeModelValue = false">{{ t('common.cancelText') }}</el-button>
    </template>
  </ele-modal>

  <!-- 修改密码弹窗 -->
  <password-form v-model="showPassword" :data="current" @done="reload" />

</template>
<script setup>
  import { ref, reactive, computed, onMounted, watch } from 'vue';
  import SearchForm from './components/search-form.vue';
  import { PlusOutlined } from '@/components/icons';
  import { EleMessage } from 'ele-admin-plus';
  import dayjs from 'dayjs';
  import EditFrom from './components/edit-form.vue';
  import PasswordForm from './components/password-form.vue'
  import { ElMessage, ElMessageBox } from 'element-plus';
  import { getUserList, deleteUser } from '@/api/user/index.js';
  import { useMessageBox } from 'ele-admin-plus/es';
  import { useI18n } from 'vue-i18n';
  import { copyText } from '@/utils/public.js';
  import QRCode from 'qrcode';
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

  const qrCodeModelValue = ref(false);
  const qrCodeSrc = ref('');

  const googleAuthSecret = ref('');

  // 打开二维码弹窗并加载二维码
  const openQRCodeDialog = async (row) => {
    // 确保二维码是最新生成的
    qrCodeSrc.value = await getQRCodeUrl(row.google_auth_secret);
    googleAuthSecret.value = row.google_auth_secret.split('secret=')[1];
    qrCodeModelValue.value = true;
  };

  // 方法：根据给定的文本生成二维码的图片 URL
  const getQRCodeUrl = async (text) => {
    try {
      // 使用 await 确保二维码生成完成，返回 URL 字符串
      const url = await QRCode.toDataURL(text);
      return url;
    } catch (error) {
      EleMessage.error('二维码生成失败');
      console.error(error);
    }
  };

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

  // 修改密码
  const showPassword = ref(false);

  const handleUpdatePass = (row) => {
    current.value = row ?? null;
    showPassword.value = true;
  }

  // 删除用户
  const deleteUserFunc = async (row) => {
    messageBox
      .confirm(t('common.deleteConfirmText'), t('common.deleteConfirm'), {
        type: 'warning',
        draggable: true
      })
      .then(async () => {
        try {
          const res = await deleteUser({ id: row.ID });
          ElMessage.success(res.msg);
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
        columnKey: 'ID',
        prop: 'ID',
        label: 'ID',
        align: 'center'
      },
      {
        columnKey: 'google_auth_secret',
        prop: 'google_auth_secret',
        label: t('common.googleAuthText'),
        slot: 'googleAuthSecret',
        align: 'center',
        minWidth: 140
      },
      {
        columnKey: 'userName',
        prop: 'userName',
        label: t('merchant.username'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'nickName',
        prop: 'nickName',
        label: t('adminUser.nickName'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'email',
        prop: 'email',
        label: t('merchant.emailText'),
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'authorityName',
        prop: 'authorityName',
        label: t('authority.roleName'),
        slot: 'authorityNameSlot',
        align: 'center',
        minWidth: 120
      },
      {
        columnKey: 'enable',
        prop: 'enable',
        label: t('common.status'),
        align: 'center',
        slot: 'enable',
        minWidth: 120,
        formatter: (row, column, cellValue, index) => {
          return cellValue == 1 ? t('common.enable') : t('common.disable');
        }
      },
      {
        columnKey: 'CreatedAt',
        prop: 'CreatedAt',
        label: t('common.createTime'),
        align: 'center',
        width: 180,
        formatter: (row, column, cellValue) => {
          return cellValue ? _timeFormat(cellValue) : '';
        }
      },
      {
        columnKey: 'action',
        prop: 'action',
        label: t('common.action'),
        align: 'center',
        slot: 'action',
        width: 260
      }
    ];
  });

  /** 表格数据源 */
  // 表格数据源, 数据源可以直接为数组或一个 function 返回一个 Promise
  const datasource = async ({ pages, where, orders, filters }) => {
    try {
      const data = await getUserList({
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
  .qrcode-secret {
    margin-top: 10px;
    display: flex;
    align-items: center;
    justify-content: center;
    .title {
      margin-right: 10px;
    }
  }
</style>
