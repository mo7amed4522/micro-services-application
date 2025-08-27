<!-- 编辑弹窗 -->
<template>
  <div>
    <ele-drawer :model-value="modelValue" :title="isUpdate ? t('common.edit') : t('common.add')" :append-to-body="true" :size="640" style="max-width: 100%" @update:modelValue="updateModelValue">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item :label="t('authority.parentRoleText')" prop="parentId">
          <el-select clearable v-model="form.parentId" :disabled="!isUpdate" :empty-values="[null, undefined]" :placeholder="t('authority.selectParentRole')" class="ele-fluid">
            <el-option v-for="(item, index) in authorityList" :key="index" :label="item.authorityName" :value="item.authorityId" />
          </el-select>
        </el-form-item>
        <el-form-item :label="t('authority.roleId')" prop="authorityId">
          <el-input v-model="form.authorityId" :disabled="isUpdate" :placeholder="t('authority.enterRoleId')" />
        </el-form-item>
        <el-form-item :label="t('authority.roleName')" prop="authorityName">
          <el-input v-model="form.authorityName" :placeholder="t('authority.enterRoleName')" />
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
  import { ref, reactive, watch, computed } from 'vue';
  import { EleMessage, isExternalLink } from 'ele-admin-plus/es';
  import { useFormData } from '@/utils/use-form-data';
  import { getAuthorityList } from '@/api/user';
  import { createAuthority, updateAuthority } from '@/api/authority';
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
    authorityId: '',
    authorityName: '',
    parentId: 0
  });

  const rules = reactive({
    parentId: [{ required: true, message: t('authority.selectParentRole'), trigger: 'blur' }],
    authorityName: [{ required: true, message: t('authority.enterRoleName'), trigger: 'blur' }],
    authorityId: [
      { required: true, message: t('authority.enterRoleId'), trigger: 'blur' },
      {
        validator: (rule, value, callback) => {
          if (!value) {
            callback(new Error(t('authority.roleIdNotEmpty')));
          } else if (!/^[1-9]\d*$/.test(value)) {
            callback(new Error(t('authority.roleIdMustBePositiveInteger')));
          } else {
            callback();
          }
        },
        trigger: 'blur'
      }
    ]
  });

  /** 保存编辑 */
  const save = () => {
    formRef.value?.validate?.((valid) => {
      if (!valid) {
        console.log('验证失败');
        return;
      }
      loading.value = true;
      const saveOrUpdate = isUpdate.value ? updateAuthority : createAuthority;
      saveOrUpdate({
        ...form,
        authorityId: Number(form.authorityId)
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

  /** 更新modelValue */
  const updateModelValue = (value) => {
    emit('update:modelValue', value);
  };

  // 获取角色列表
  const authorityList = ref([]);

  const authortyList = async () => {
    const res = await getAuthorityList();
    res.unshift({
      authorityId: 0,
      authorityName: '跟角色(严格模式下为当前用户角色)'
    })
    authorityList.value = res
  };

  watch(
    () => props.modelValue,
    (modelValue) => {
      if (modelValue) {
        authortyList();
        if (props.data) {
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
    }
  );
</script>
