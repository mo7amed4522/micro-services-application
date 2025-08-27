<!-- 编辑弹窗 -->
<template>
  <div>
    <ele-drawer :model-value="modelValue" :title="isUpdate ? '修改用户' : '新增用户'" :append-to-body="true" :size="640" style="max-width: 100%" @update:modelValue="updateModelValue">
      <template #footer>
        <el-button @click="updateModelValue(false)">取消</el-button>
        <el-button type="primary" :loading="loading" @click="save">保存</el-button>
      </template>
      <el-form ref="formRef" :model="form" label-width="80px">
        <template v-if="!isUpdate">
          <el-form-item label="用户名" prop="userName">
            <el-input v-model="form.userName" placeholder="" />
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input v-model="form.password" placeholder="" />
          </el-form-item>
        </template>
        <el-form-item label="昵称" prop="nickName">
          <el-input v-model="form.nickName" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item label="用户角色" prop="authorityId">
          <el-cascader
            v-model="form.authorityIds"
            style="width: 100%"
            :options="authOptions"
            :show-all-levels="false"
            :props="{ multiple: true, checkStrictly: true, label: 'authorityName', value: 'authorityId', disabled: 'disabled', emitPath: false }"
            :clearable="false"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.enable" :active-value="1" :inactive-value="2" />
        </el-form-item>
      </el-form>
    </ele-drawer>
  </div>
</template>

<script setup>
  import { ref, reactive, watch, computed, nextTick } from 'vue';
  import { EleMessage, isExternalLink } from 'ele-admin-plus/es';
  import { useFormData } from '@/utils/use-form-data';
  import { getAuthorityList, register, setUserAuthorities, setUserInfo } from '@/api/user';
  import { ElMessage } from 'element-plus';

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
    enable: 1
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
      ElMessage({ type: 'success', message: '角色设置成功' });
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
