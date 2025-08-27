<template>
  <ele-modal form :width="550" :model-value="modelValue" :title="isUpdate ? t('merchant.editTitle') : t('merchant.addBtn')" @update:modelValue="updateModelValue">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="140px" @submit.prevent="">
      <el-form-item :label="t('merchant.username')" prop="name">
        <el-input v-model="form.name" :placeholder="t('merchant.usernameText')" clearable />
      </el-form-item>
      <el-form-item :label="t('merchant.passwordText')" prop="loginPwd">
        <el-input v-model="form.loginPwd" :placeholder="t('merchant.enterPassword')" clearable />
      </el-form-item>
      <el-form-item :label="t('merchant.payPassword')" prop="payPwd">
        <el-input v-model="form.payPwd" :placeholder="t('merchant.enterPayPassword')" clearable />
      </el-form-item>
      <!-- <el-form-item :label="t('merchant.emailText')" prop="email">
        <el-input v-model="form.email" :placeholder="t('merchant.enterEmail')" clearable />
      </el-form-item>
      <el-form-item :label="t('merchant.phoneText')" prop="phone">
        <el-input v-model="form.phone" :placeholder="t('merchant.enterPhone')" clearable />
      </el-form-item> -->
      <el-form-item :label="t('merchant.restriction')" prop="isLimitIp">
        <el-radio-group v-model="form.isLimitIp">
          <el-radio :value="1">{{ t('common.open') }}</el-radio>
          <el-radio :value="0">{{ t('common.close') }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item :label="t('merchant.payOutStatus')" prop="valid">
        <el-switch v-model="form.valid" :active-value="1" :inactive-value="0" :active-text="t('common.valid')" :inactive-text="t('common.invalid')" inline-prompt />
      </el-form-item>
      <el-form-item :label="t('merchant.status')" prop="status">
        <el-switch v-model="form.status" :active-value="1" :inactive-value="0" :active-text="t('common.disable')" :inactive-text="t('common.enable')" inline-prompt />
      </el-form-item>
      <el-form-item :label="t('merchant.publicKey')" prop="publicKey">
        <el-input v-model="form.publicKey" type="textarea" :placeholder="t('merchant.enterPublicKey')" :rows="3" clearable />
      </el-form-item>
      <el-form-item :label="t('merchant.privateKey')" prop="privateKey">
        <el-input v-model="form.privateKey" type="textarea" :placeholder="t('merchant.enterPrivateKey')" :rows="3" clearable />
      </el-form-item>
      <el-form-item :label="t('common.notes')" prop="remark">
        <el-input v-model="form.remark" type="textarea" :placeholder="t('common.enterNotes')" :rows="3" clearable />
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
  import { QuestionCircleOutlined } from '@/components/icons/index.js';
  import { addMUserApi, updateMUserApi } from '@/api/merchant'
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

  /** 表单实例 */
  const formRef = ref(null);

  /** 表单数据 */
  const [form, resetFields, assignFields] = useFormData({
    id: void 0,
    email: '',
    name: '',
    loginPwd: '',
    phone: '',
    payPwd: '',
    isLimitIp: 0,
    remark: '',
    valid: 0,
    status: 0,
    privateKey: '',
    publicKey: '',
    no: '', // 商户号
  });

  /** 表单验证规则 */
  const rules = reactive({
    name: [{ required: true, message: t('merchant.usernameText'), trigger: 'blur' }],
    loginPwd: [{ required: true, message: t('merchant.enterPassword'), trigger: 'blur' }],
    payPwd: [{ required: true, message: t('merchant.enterPayPassword'), trigger: 'blur' }],
    email: [
      { required: true, message: t('merchant.enterEmail'), trigger: 'blur' },
      { type: 'email', message: t('merchant.enterEmailTips'), trigger: 'blur' }
    ],
    phone: [
      { required: true, message: t('merchant.enterPhone'), trigger: 'blur' },
      { pattern: /^1[3-9]\d{9}$/, message: t('merchant.enterPhoneTips'), trigger: 'blur' }
    ],
    isLimitIp: [{ required: true, message: t('merchant.restrictionText'), trigger: 'change' }],
    valid: [{ required: true, message: t('merchant.selectPayOutStatus'), trigger: 'change' }],
    status: [{ required: true, message: t('merchant.selectMerchantStatus'), trigger: 'change' }],
    publicKey: [{ required: true, message: t('merchant.enterPublicKey'), trigger: 'blur' }],
    privateKey: [{ required: true, message: t('merchant.enterPrivateKey'), trigger: 'blur' }]
  });

  /** 保存编辑 */
  const save = () => {
    formRef.value?.validate?.((valid) => {
      if (!valid) {
        return;
      }
      loading.value = true;
      const saveOrUpdate = isUpdate.value ? updateMUserApi : addMUserApi;
      saveOrUpdate({ ...form })
        .then((msg) => {
          loading.value = false;
          EleMessage.success(msg);
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
