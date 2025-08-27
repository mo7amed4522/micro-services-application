<template>
  <ele-modal form :width="550" :model-value="modelValue" :title="isUpdate ? t('common.edit') : t('common.add')" @update:modelValue="updateModelValue">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="140px" @submit.prevent="">
      <el-form-item :label="t('channelConfig.configName')" prop="name">
        <el-input v-model="form.name" :placeholder="t('channelConfig.enterConfigName')" clearable />
      </el-form-item>
      <el-form-item :label="t('orderList.channelType')" prop="type">
        <el-radio-group v-model="form.type">
          <el-radio :value="1">{{ t('channelConfig.payText') }}</el-radio>
          <el-radio :value="2">{{ t('channelConfig.payOutText') }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item :label="t('channelConfig.sourceType')" prop="channelType">
        <el-radio-group v-model="form.channelType">
          <el-radio :value="1">{{ t('common.selfDevelop') }}</el-radio>
          <el-radio :value="0">{{ t('common.thirdParty') }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item :label="t('channelConfig.channelPayTypeText')" prop="payType">
        <el-radio-group v-model="form.payType">
          <el-radio value="1">{{ t('payMethod.qrCode') }}</el-radio>
          <el-radio value="2">{{ t('payMethod.bankCard') }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item :label="t('channelConfig.configValueText')" prop="channelValue">
        <el-input v-model="form.channelValue" :placeholder="t('channelConfig.enterConfigValue')" clearable />
      </el-form-item>
      <template v-if="form.channelType === 0">
        <el-form-item :label="t('channelConfig.thirdCode')" prop="channelCode">
          <el-input v-model="form.channelCode" :placeholder="t('channelConfig.enterThirdCode')" clearable />
        </el-form-item>

        <el-form-item :label="t('channelConfig.thirdMerchantNum')" prop="thirdMerchantNo">
          <el-input v-model="form.thirdMerchantNo" :placeholder="t('channelConfig.enterThirdMerchantNum')" clearable />
        </el-form-item>

        <el-form-item :label="t('channelConfig.thirdRequestUrl')" prop="thirdUrl">
          <el-input v-model="form.thirdUrl" :placeholder="t('channelConfig.enterThirdRequestUrl')" clearable />
        </el-form-item>
        <el-form-item :label="t('channelConfig.thirdPublicKey')" prop="thirdPublicKey">
          <el-input v-model="form.thirdPublicKey" type="textarea" :rows="2" :placeholder="t('channelConfig.enterThirdPublicKey')" clearable />
        </el-form-item>
        <el-form-item :label="t('channelConfig.thirdPrivateKey')" prop="thirdPrivateKey">
          <el-input v-model="form.thirdPrivateKey" type="textarea" :rows="2" :placeholder="t('channelConfig.enterThirdPrivateKey')" clearable />
        </el-form-item>
      </template>

      <el-form-item :label="t('common.notes')" prop="remark">
        <el-input v-model="form.remark" type="textarea" :rows="2" :placeholder="t('common.enterNotes')" clearable />
      </el-form-item>

      <el-form-item :label="t('common.status')" prop="valid">
        <el-radio-group v-model="form.valid">
          <el-radio :value="1">{{ t('common.enable') }}</el-radio>
          <el-radio :value="0">{{ t('common.disable') }}</el-radio>
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
  import { createCPayConfigApi, updateCPayConfigApi } from '@/api/payConfig';
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
    payType: '1',
    channelCode: '',
    channelType: 1,
    channelValue: '',
    name: '',
    remark: '',
    thirdMerchantNo: '',
    thirdPrivateKey: '',
    thirdPublicKey: '',
    thirdUrl: '',
    valid: 1,
    type: 1,
  });

  /** 表单验证规则 */
  const rules = reactive({
    name: [{ required: true, message: t('channelConfig.enterConfigName'), trigger: 'blur' }],
    payType: [{ required: true, message: t('channelConfig.selectChannelPayType'), trigger: ['change'] }],
    channelType: [{ required: true, message: t('channelConfig.selectSourceType'), trigger: ['change'] }],
    type: [{ required: true, message: t('channelConfig.selectChannelType'), trigger: ['change'] }],
    channelValue: [{ required: true, message: t('channelConfig.enterConfigValue'), trigger: 'blur' }],
    channelCode: [{ required: true, message: t('channelConfig.enterThirdCode'), trigger: 'blur' }],
    thirdMerchantNo: [{ required: true, message: t('channelConfig.enterThirdMerchantNum'), trigger: 'blur' }],
    thirdUrl: [{ required: true, message: t('channelConfig.enterThirdRequestUrl'), trigger: 'blur' }],
    thirdPublicKey: [{ required: true, message: t('channelConfig.enterThirdPublicKey'), trigger: 'blur' }],
    thirdPrivateKey: [{ required: true, message: t('channelConfig.enterThirdPrivateKey'), trigger: 'blur' }],
    valid: [{ required: true, message: t('common.selectStatus'), trigger: ['change', 'blur'] }]
  });

  /** 保存编辑 */
  const save = () => {
    formRef.value?.validate?.((valid) => {
      if (!valid) {
        return;
      }
      loading.value = true;
      const saveOrUpdate = isUpdate.value ? updateCPayConfigApi : createCPayConfigApi;
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
