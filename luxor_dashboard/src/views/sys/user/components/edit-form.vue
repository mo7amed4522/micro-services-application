<!-- 编辑弹窗 -->
<template>
  <div>
    <ele-drawer :model-value="modelValue" :title="isUpdate ? t('common.edit') : t('common.add')" :append-to-body="true" :size="640" style="max-width: 100%" @update:modelValue="updateModelValue">
      <el-form ref="formRef" :model="form" label-width="110px">
        <template v-if="!isUpdate">
          <el-form-item :label="t('merchant.username')" prop="userName">
            <el-input v-model="form.userName" :placeholder="t('merchant.usernameText')" />
          </el-form-item>
          <el-form-item :label="t('adminUser.password')" prop="password">
            <el-input v-model="form.password" :placeholder="t('adminUser.enterPassword')" />
          </el-form-item>
        </template>
        <el-form-item :label="t('adminUser.nickName')" prop="nickName">
          <el-input v-model="form.nickName" :placeholder="t('adminUser.enterNickName')" />
        </el-form-item>
        <el-form-item :label="t('merchant.emailText')" prop="email">
          <el-input v-model="form.email" :placeholder="t('merchant.enterEmail')" />
        </el-form-item>
        <el-form-item :label="t('adminUser.userRoleText')" prop="authorityId">
          <el-cascader
            v-model="form.authorityIds"
            style="width: 100%"
            :options="authOptions"
            :show-all-levels="false"
            :props="{ multiple: true, checkStrictly: true, label: 'authorityName', value: 'authorityId', disabled: 'disabled', emitPath: false }"
            :clearable="false"
            :placeholder="t('adminUser.selectRole')"
          />
        </el-form-item>
        <el-form-item :label="t('common.updateGoogleAuth')">
          <el-switch v-model="form.isUpdateGoogleSecret" :active-value="true" :inactive-value="false" />
        </el-form-item>
        <el-form-item :label="t('common.status')">
          <el-switch v-model="form.enable" :active-value="1" :inactive-value="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="updateModelValue(false)">{{ t('common.cancelText') }}</el-button>
        <el-button type="primary" :loading="loading" @click="save">{{ t('common.saveText') }}</el-button>
      </template>
    </ele-drawer>
  </div>
</template>

<script setup>
  import { ref, reactive, watch, computed, nextTick } from 'vue';
  import { EleMessage, isExternalLink } from 'ele-admin-plus/es';
  import { useFormData } from '@/utils/use-form-data';
  import { getAuthorityList, register, setUserAuthorities, setUserInfo } from '@/api/user';
  import { ElMessage } from 'element-plus';
  import { useI18n } from 'vue-i18n';

  const { t } = useI18n();

  const emit = defineEmits(['done', 'update:modelValue']);

  const props = defineProps({
    /** 弹窗是否打开 */
    modelValue: Boolean,
    /** 修改回显的数据 */
    data: Object
  });
  /** 是否是修改 */
  const isUpdate = ref(false);

  /** 提交状态 */
  const loading = ref(false);

  const isEnable = ref(false);

  /** 表单实例 */
  const formRef = ref(null);

  /** 表单数据 */
  const [form, resetFields, assignFields] = useFormData({
    ID: void 0,
    nickName: '',
    userName: '',
    password: '',
    headerImg: '',
    authorityId: '',
    authorityIds: [],
    enable: 1,
    email: '',
    isUpdateGoogleSecret: false,
  });

  const setAuthorityOptions = (AuthorityData, optionsData) => {
    AuthorityData &&
      AuthorityData.forEach((item) => {
        if (item.children && item.children.length) {
          const option = {
            authorityId: item.authorityId,
            authorityName: item.authorityName,
            children: []
          };
          setAuthorityOptions(item.children, option.children);
          optionsData.push(option);
        } else {
          const option = {
            authorityId: item.authorityId,
            authorityName: item.authorityName
          };
          optionsData.push(option);
        }
      });
  };

  const authOptions = ref([]);
  const setOptions = (authData) => {
    authOptions.value = [];
    setAuthorityOptions(authData, authOptions.value);
  };

  // 弹窗相关
  const userInfo = ref({
    userName: '',
    password: '',
    nickName: '',
    headerImg: '',
    authorityId: '',
    authorityIds: [],
    enable: 1
  });

  const tempAuth = {};
  const changeAuthority = async (row, flag, removeAuth) => {
    if (flag) {
      if (!removeAuth) {
        tempAuth[row.ID] = [...row.authorityIds];
      }
      return;
    }
    await nextTick();
    const res = await setUserAuthorities({
      ID: row.ID,
      authorityIds: row.authorityIds
    });
    if (res.code === 0) {
      ElMessage({ type: 'success', message: t('adminUser.roleSetSuccess') });
    } else {
      if (!removeAuth) {
        row.authorityIds = [...tempAuth[row.ID]];
        delete tempAuth[row.ID];
      } else {
        row.authorityIds = [removeAuth, ...row.authorityIds];
      }
    }
  };

  const setAuthorityIds = () => {
    if (props.data) {
      // 直接处理单个用户对象
      props.data.authorityIds =
        props.data.authorities &&
        props.data.authorities.map((i) => {
          return i.authorityId;
        });
    }
  };

  /** 保存编辑 */
  const save = () => {
    formRef.value?.validate?.((valid) => {
      if (!valid) {
        console.log('验证失败');
        return;
      }
      loading.value = true;
      form.authorityId = form.authorityIds[0];
      const saveOrUpdate = isUpdate.value ? setUserInfo : register;
      saveOrUpdate({
        ...form
      })
        .then((res) => {
          loading.value = false;
          EleMessage.success(res.msg);
          updateModelValue(false);
          emit('done');
        })
        .catch((e) => {
          loading.value = false;
          EleMessage.error(e.message);
        });
    });
  };

  const initPage = async () => {
    const res = await getAuthorityList();
    setOptions(res);
  };

  initPage();

  /** 更新modelValue */
  const updateModelValue = (value) => {
    emit('update:modelValue', value);
    if (!value) {
      isUpdate.value = false; // 关闭弹窗时重置 isUpdate
    }
  };

  watch(
    () => props.modelValue,
    (modelValue) => {
      if (modelValue) {
        if (props.data) {
          console.log(isUpdate.value, '---------修改');
          setAuthorityIds();
          assignFields({
            ...props.data
          });
          isUpdate.value = true;
        } else {
          isUpdate.value = false;
        }
      } else {
        resetFields();
        formRef.value?.clearValidate?.();
      }
    },
    { immediate: true }
  );
</script>
