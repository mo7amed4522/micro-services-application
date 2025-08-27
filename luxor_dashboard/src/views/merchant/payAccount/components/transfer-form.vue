<template>
  <ele-modal form :width="550" :model-value="modelValue" :title="t('common.paymentPoolTransfer')" @update:modelValue="updateModelValue">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="120px" @submit.prevent="">
      <el-form-item :label="t('common.paymentPoolText')" prop="cPayPoolId">
        <el-select clearable v-model="form.cPayPoolId" :empty-values="[null, undefined]" :placeholder="t('common.selectPaymentPoolText')" class="ele-fluid">
          <el-option v-for="(item, index) in payPoolList" :key="index" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>

      <el-form-item :label="t('common.notes')" prop="remark">
        <el-input v-model="form.remark" type="textarea" :rows="2" :placeholder="t('common.enterNotes')" clearable />
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
  import { transferCPayPoolApi } from '@/api/merchant';
  import { getCPayPoolListApi } from '@/api/merchant/index.js';
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
    id: '',
    cPayPoolId: '',
    remark: '',
  });

  /** 表单验证规则 */
  const rules = reactive({
    cPayPoolId: [{ required: true, message: t('common.selectPaymentPoolText'), trigger: 'blur' }],
  });

  /** 保存编辑 */
  const save = () => {
    formRef.value?.validate?.((valid) => {
      if (!valid) {
        return;
      }
      loading.value = true;
      transferCPayPoolApi({
        ...form
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

  const payPoolList = ref([]);

  // 获取支付池列表
  const getPayPoolList = async () => {
    const res = await getCPayPoolListApi({
      page: 1,
      pageSize: 9999
    });
    payPoolList.value = res.list;
  }

  watch(
    () => props.modelValue,
    (modelValue) => {
      if (modelValue) {
        getPayPoolList()
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
