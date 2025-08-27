<template>
  <ele-modal form :width="550" :model-value="modelValue" :title="t('common.edit')" @update:modelValue="updateModelValue">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="120px" @submit.prevent="">
      <el-form-item :label="t('merchantChannel.merchantName')" prop="mUserName">
        <el-input v-model="form.mUserName" disabled clearable />
      </el-form-item>
      <el-form-item :label="t('merchantChannel.minPayAmount')" prop="payMinMoney">
        <el-input v-model="form.payMinMoney" :placeholder="t('merchantChannel.enterMinPayAmount')" clearable />
      </el-form-item>
      <el-form-item :label="t('merchantChannel.maxPayAmount')" prop="payMaxMoney">
        <el-input v-model="form.payMaxMoney" :placeholder="t('merchantChannel.enterMaxPayAmount')" clearable />
      </el-form-item>
      <el-form-item :label="t('common.notes')" prop="remark">
        <el-input v-model="form.remark" type="textarea" :rows="2" :placeholder="t('common.enterNotes')" clearable />
      </el-form-item>
      <el-form-item :label="t('common.statusText')" prop="valid">
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
  import { createMPayChannelConfigApi, updateMPayChannelConfigApi } from '@/api/merchant';
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
    merAccountId: null,
    payMaxMoney: 0,
    payMinMoney: 0,
    remark: '',
    valid: 1,
    mUserName: '',
  });

  /** 表单验证规则 */
  const rules = reactive({
    payMaxMoney: [{ required: true, message: t('merchantChannel.enterMaxPayAmount'), trigger: 'blur' }],
    payMinMoney: [{ required: true, message: t('merchantChannel.enterMinPayAmount'), trigger: 'blur' }]
  });

  /** 保存编辑 */
  const save = () => {
    formRef.value?.validate?.((valid) => {
      if (!valid) {
        return;
      }
      loading.value = true;
      form.merAccountId = parseInt(form.merAccountId);
      const saveOrUpdate = isUpdate.value ? updateMPayChannelConfigApi : createMPayChannelConfigApi;
      saveOrUpdate({ 
        ...form,
        payMinMoney: parseFloat(form.payMinMoney),
        payMaxMoney: parseFloat(form.payMaxMoney)
       })
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
<style>
.el-input-number .el-input-number__decrease {
  left: 1px !important;
}
</style>
