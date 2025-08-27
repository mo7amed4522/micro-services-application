<template>
  <ele-modal form :width="550" :model-value="modelValue" :title="props.type == 1 ? t('common.receivingPaymentText') : t('common.paymentDisbursementText')" @update:modelValue="updateModelValue">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="130px" @submit.prevent="">
      <el-form-item :label="t('common.changeAmount')" prop="amount">
        <el-input v-model="form.amount" :placeholder="t('common.changeAmountText')" clearable />
      </el-form-item>

      <el-form-item :label="t('common.notes')" prop="remark">
        <el-input v-model="form.desc" type="textarea" :rows="2" :placeholder="t('common.enterNotes')" clearable />
      </el-form-item>

      <el-form-item :label="t('utrOrder.operationType')" prop="type">
        <el-radio-group v-model="form.type">
          <el-radio :value="1">{{ t('accountChangeRecord.payeeText') }}</el-radio>
          <el-radio :value="2">{{ t('accountChangeRecord.payerText') }}</el-radio>
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
  import { updateAmountApi } from '@/api/merchant';
  import { useI18n } from 'vue-i18n';

  const { t } = useI18n();

  const emit = defineEmits(['done', 'update:modelValue']);

  const props = defineProps({
    modelValue: {
      type: Boolean,
      default: false
    },
    data: {
      type: Object,
      default: () => null
    }
  });

  /** 提交状态 */
  const loading = ref(false);

  /** 表单实例 */
  const formRef = ref(null);

  /** 表单数据 */
  const [form, resetFields, assignFields] = useFormData({
    amount: '',
    desc: '',
    id: '',
    type: 1
  });

  /** 表单验证规则 */
  const rules = reactive({
    amount: [{ required: true, message: t('common.changeAmountText'), trigger: 'blur' }],
    type: [{ required: true, message: t('common.selectTypeText'), trigger: 'change' }]
  });

  /** 保存编辑 */
  const save = () => {
    formRef.value?.validate?.((valid) => {
      if (!valid) {
        return;
      }
      loading.value = true;
      updateAmountApi({
        ...form,
        amount: parseFloat(form.amount)
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
          form.type = 1
        }
      } else {
        resetFields();
        formRef.value?.clearValidate?.();
      }
    }
  );
</script>
