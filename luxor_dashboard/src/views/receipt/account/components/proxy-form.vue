<template>
  <ele-modal form :width="550" :model-value="modelValue" :title="t('common.detailText')" @update:modelValue="updateModelValue">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="130px" @submit.prevent="">
      <el-form-item :label="t('merchant.username')" prop="user">
        <el-input v-model="form.user" disabled :placeholder="t('mockOrder.enterText')" clearable />
      </el-form-item>

      <el-form-item :label="t('adminUser.password')" prop="password">
        <el-input v-model="form.password" disabled :placeholder="t('mockOrder.enterText')" clearable />
      </el-form-item>

      <el-form-item :label="t('common.ipText')" prop="ip">
        <el-input v-model="form.ip" disabled :placeholder="t('mockOrder.enterText')" clearable />
      </el-form-item>

      <el-form-item :label="t('common.notes')" prop="remark">
        <el-input v-model="form.remark" disabled type="textarea" :rows="3" :placeholder="t('common.enterNotes')" clearable />
      </el-form-item>

      <el-form-item :label="t('common.statusText')" prop="valid">
        <el-radio-group v-model="form.valid" disabled>
          <el-radio :value="1">{{ t('common.enable') }}</el-radio>
          <el-radio :value="-1">{{ t('common.disable') }}</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="updateModelValue(false)">{{ t('common.close') }}</el-button>
      <!-- <el-button type="primary" :loading="loading" @click="updateModelValue(false)">{{ t('common.saveText') }}</el-button> -->
    </template>
  </ele-modal>
</template>

<script setup>
  import { ref, reactive, watch } from 'vue';
  import { EleMessage } from 'ele-admin-plus/es';
  import { useFormData } from '@/utils/use-form-data';
  import { QuestionCircleOutlined } from '@/components/icons/index.js';
  import { createReceiptAccountApi, updateReceiptAccountApi } from '@/api/receipt';
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
    ip: '',
    password: '',
    remark: '',
    user: '',
    valid: ''
  });

  /** 表单验证规则 */
  const rules = reactive({
    user: [{ required: true, message: t('mockOrder.enterText'), trigger: 'blur' }],
    password: [{ required: true, message: t('mockOrder.enterText'), trigger: 'blur' }],
    ip: [{ required: true, message: t('mockOrder.enterText'), trigger: 'blur' }],
    valid: [{ required: true, message: t('mockOrder.selectText'), trigger: 'blur' }]
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

  watch(
    () => props.modelValue,
    (modelValue) => {
      if (modelValue) {
        if (props.data) {
          assignFields({
            ...props.data
          });
        }
      } else {
        resetFields();
        formRef.value?.clearValidate?.();
      }
    }
  );
</script>
