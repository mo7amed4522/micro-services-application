<template>
  <ele-modal form :width="550" :model-value="modelValue" :title="t('orderList.resetOrderStatusText')" @update:modelValue="updateModelValue">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="120px" @submit.prevent="">
      <el-form-item :label="t('orderList.orderNo')">
        <span>{{ form.orderNo }}</span>
      </el-form-item>
      <el-form-item :label="t('orderList.oldOrderStatus')">
        <span>{{ t('orderList.payApplyText') }}</span>
      </el-form-item>
      <el-form-item :label="t('orderList.editStatusText')" prop="status">
        <el-select clearable v-model="form.status" :empty-values="[null, undefined]" :placeholder="t('orderList.selectOrderStatus')" class="ele-fluid">
          <el-option v-for="(item, index) in statusOptions" :key="index" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="updateModelValue(false)">{{ t('common.cancelText') }}</el-button>
      <el-button type="primary" :loading="loading" @click="save">{{ t('common.saveText') }}</el-button>
    </template>
  </ele-modal>
</template>

<script setup>
  import { ref, reactive, watch, computed } from 'vue';
  import { EleMessage } from 'ele-admin-plus/es';
  import { useFormData } from '@/utils/use-form-data';
  import { QuestionCircleOutlined } from '@/components/icons/index.js';
  import { orderPayResetApi } from '@/api/order';
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
    orderNo: '',
    status: ''
  });

  /** 表单验证规则 */
  const rules = reactive({
    status: [{ required: true, message: t('common.selectStatus'), trigger: 'change' }]
  });

  const statusOptions = computed(() => [
    { id: 2, name: t('common.paySuccessText') },
    { id: -1, name: t('common.failText') }
  ]);

  /** 保存编辑 */
  const save = () => {
    formRef.value?.validate?.((valid) => {
      if (!valid) {
        return;
      }
      loading.value = true;
      orderPayResetApi({ ...form })
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
          form.status = null;
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
