<template>
  <ele-modal form :width="550" :model-value="modelValue" :title="isUpdate ? t('common.edit') : t('common.add')" @update:modelValue="updateModelValue">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="130px" @submit.prevent="">
      <el-form-item :label="t('payMethod.bankName')" prop="bankName">
        <el-select v-model="form.bankName" clearable filterable :empty-values="[null, undefined]" :placeholder="t('payMethod.selectBank')">
          <el-option v-for="item in receiptConfitList" :key="item.id" :value="item.configValue" :label="item.configValue" />
        </el-select>
      </el-form-item>

      <el-form-item :label="t('common.selectAgent')" prop="receiptProxyId">
        <el-select v-model="form.receiptProxyId" clearable filterable :empty-values="[null, undefined]" :placeholder="t('common.pleaseSelectAgent')">
          <el-option v-for="item in props.agentList" :key="item.id" :value="item.id" :label="item.user" />
        </el-select>
      </el-form-item>

      <el-form-item :label="t('common.paymentMethodText')" prop="cPayPoolAccountId">
        <el-select v-model="form.cPayPoolAccountId" clearable filterable :empty-values="[null, undefined]" :placeholder="t('common.selectPaymentMethodText')">
          <el-option v-for="item in props.payPoolAccountList" :key="item.id" :value="item.id" :label="item.name" />
        </el-select>
      </el-form-item>

      <el-form-item :label="t('common.deviceId')" prop="deviceId">
        <el-input v-model="form.deviceId" :placeholder="t('common.enterDeviceId')" clearable />
      </el-form-item>

      <el-form-item :label="t('common.loginUserName')" prop="loginUserName">
        <el-input v-model="form.loginUserName" :placeholder="t('common.enterLoginUserName')" clearable />
      </el-form-item>

      <el-form-item :label="t('common.loginUserId')" prop="loginUserId">
        <el-input v-model="form.loginUserId" :placeholder="t('common.enterLoginUserId')" clearable />
      </el-form-item>

      <el-form-item :label="t('common.loginPassword')" prop="loginPwd">
        <el-input v-model="form.loginPwd" :placeholder="t('common.enterLoginPassword')" clearable />
      </el-form-item>

      <el-form-item :label="t('common.notes')" prop="remark">
        <el-input v-model="form.remark" type="textarea" :rows="3" :placeholder="t('common.enterNotes')" clearable />
      </el-form-item>

      <el-form-item :label="t('common.statusText')" prop="status">
        <el-radio-group v-model="form.status">
          <el-radio :value="1">{{ t('common.online') }}</el-radio>
          <el-radio :value="-1">{{ t('common.offline') }}</el-radio>
          <el-radio :value="3">{{ t('common.manualMaintain') }}</el-radio>
        </el-radio-group>
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
  import { createReceiptAccountApi, updateReceiptAccountApi, getReceiptConfigListApi } from '@/api/receipt';
  import { useI18n } from 'vue-i18n';

  const { t } = useI18n();

  const emit = defineEmits(['done', 'update:modelValue']);

  const props = defineProps({
    /** 弹窗是否打开 */
    modelValue: Boolean,
    /** 修改回显的数据 */
    data: Object,
    payPoolAccountList: Array,
    agentList: Array
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
    bankName: null,
    bankNumber: '',
    cPayPoolAccountId: null,
    deviceId: '',
    loginPwd: '',
    loginUserId: '',
    loginUserName: '',
    status: -1,
    remark: '',
    receiptProxyId: null
  });

  /** 表单验证规则 */
  const rules = reactive({
    bankName: [{ required: true, message: t('payMethod.selectBank'), trigger: 'blur' }],
    bankNumber: [{ required: true, message: t('payMethod.enterCardNumber'), trigger: 'blur' }],
    cPayPoolAccountId: [{ required: true, message: t('common.selectPaymentMethodText'), trigger: 'change' }],
    deviceId: [{ required: true, message: t('common.enterDeviceId'), trigger: 'blur' }],
    loginUserId: [{ required: true, message: t('common.enterLoginUserId'), trigger: 'blur' }],
    loginUserName: [{ required: true, message: t('common.enterLoginUserName'), trigger: 'blur' }],
    loginPwd: [{ required: true, message: t('common.enterLoginPassword'), trigger: 'blur' }],
    status: [{ required: true, message: t('common.selectStatusText'), trigger: 'change' }],
    receiptProxyId: [{ required: true, message: t('common.pleaseSelectAgent'), trigger: 'change' }]
  });

  /** 保存编辑 */
  const save = () => {
    formRef.value?.validate?.((valid) => {
      if (!valid) {
        return;
      }
      loading.value = true;
      const saveOrUpdate = isUpdate.value ? updateReceiptAccountApi : createReceiptAccountApi;
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

  const receiptConfitList = ref([]);

  const getReceiptConfigList = async () => {
    try {
      const res = await getReceiptConfigListApi({
        configKey: 'bankNameKey'
      });
      receiptConfitList.value = res.list;
    } catch (e) {
      EleMessage.error(e.message);
    }
  };

  watch(
    () => props.modelValue,
    (modelValue) => {
      if (modelValue) {
        getReceiptConfigList();
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
