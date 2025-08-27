<template>
  <ele-modal form :width="550" :model-value="modelValue" :title="t('layout.header.password')" @update:modelValue="updateModelValue">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="140px" @submit.prevent="">
      <el-form-item :label="t('common.newPwd')" prop="password">
        <el-input type="password" v-model="form.password" :placeholder="t('common.enterNewPwd')" clearable />
      </el-form-item>
      <el-form-item :label="t('common.confirmPwd')" prop="rePassword">
        <el-input type="password" v-model="form.rePassword" :placeholder="t('common.enterConfirmPwd')" clearable />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="updateModelValue(false)">{{ t('common.cancelText') }}</el-button>
      <el-button type="primary" :loading="loading" @click="save">{{ t('common.saveText') }}</el-button>
    </template>
  </ele-modal>
</template>

<script setup>
  import { ref, reactive, watch } from 'vue';
  import { EleMessage } from 'ele-admin-plus/es';
  import { useFormData } from '@/utils/use-form-data';
  import { updatePasswordApi } from '@/api/user';
  import { useI18n } from 'vue-i18n';

  const { t } = useI18n();

  const emit = defineEmits(['done', 'update:modelValue']);

  const props = defineProps({
    /** 弹窗是否打开 */
    modelValue: Boolean,
    /** 修改回显的数据 */
    data: Object
  });

  /** 提交状态 */
  const loading = ref(false);

  /** 表单实例 */
  const formRef = ref(null);

  /** 表单数据 */
  const [form, resetFields, assignFields] = useFormData({
    adminId: '',
    password: '',
    rePassword: ''
  });

  const validateRePassword = (rule, value, callback) => {
    if (!value) {
      callback(new Error(t('common.enterConfirmPwd')));
    } else if (value !== form.password) {
      callback(new Error(t('common.pwdNotEqual')));
    } else {
      callback();
    }
  };

  /** 表单验证规则 */
  const rules = reactive({
    password: [{ required: true, message: t('common.enterNewPwd'), trigger: 'blur' }],
    rePassword: [
    { required: true, message: t('common.enterConfirmPwd'), trigger: 'blur' },
      { validator: validateRePassword, trigger: 'blur' }]
  });

  /** 保存编辑 */
  const save = () => {
    formRef.value?.validate?.((valid) => {
      if (!valid) {
        return;
      }
      loading.value = true;
      updatePasswordApi({ ...form })
        .then(res => {
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

  watch(
    () => props.modelValue,
    (modelValue) => {
      if (modelValue) {
        form.adminId = props.data.ID;
      } else {
        resetFields();
        formRef.value?.clearValidate?.();
      }
    }
  );
</script>
