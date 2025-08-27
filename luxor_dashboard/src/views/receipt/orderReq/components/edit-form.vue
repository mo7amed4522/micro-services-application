<template>
  <ele-modal form :width="550" :model-value="modelValue" :title="isUpdate ? t('common.edit') : t('common.add')" @update:modelValue="updateModelValue">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="130px" @submit.prevent="">
      <el-form-item :label="t('payMethod.bankName')" prop="bankName">
        <el-select v-model="form.bankName" clearable filterable :empty-values="[null, undefined]" :placeholder="t('mockOrder.selectText')">
          <el-option v-for="item in receiptConfitList" :key="item.id" :value="item.configValue" :label="item.configValue" />
        </el-select>
      </el-form-item>

      <el-form-item :label="t('common.receiptAccount')" prop="receiptAccountId">
        <el-select v-model="form.receiptAccountId" clearable filterable :empty-values="[null, undefined]" :placeholder="t('mockOrder.selectText')">
          <el-option v-for="item in receiptAccountList" :key="item.id" :value="item.id" :label="item.loginUserName" />
        </el-select>
      </el-form-item>

      <el-form-item :label="t('mockOrder.orderAmountText')" prop="amount">
        <el-input v-model="form.amount" :placeholder="t('mockOrder.enterText')" clearable />
      </el-form-item>

      <el-form-item :label="t('utrOrder.utrCode')" prop="utr">
        <el-input v-model="form.utr" :placeholder="t('mockOrder.enterText')" clearable />
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
  import { createReceiptOrderReqApi, getReceiptConfigListApi, getReceiptAccountListApi } from '@/api/receipt';
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
    bankName: null,
    utr: '',
    amount: '',
    receiptAccountId: null
  });

  /** 表单验证规则 */
  const rules = reactive({
    bankName: [{ required: true, message: t('mockOrder.selectText'), trigger: 'change' }],
    amount: [{ required: true, message: t('mockOrder.enterText'), trigger: 'blur' }],
    utr: [{ required: true, message: t('mockOrder.enterText'), trigger: 'blur' }],
    receiptAccountId: [{ required: true, message: t('mockOrder.selectText'), trigger: 'change' }]
  });

  /** 保存编辑 */
  const save = () => {
    formRef.value?.validate?.((valid) => {
      if (!valid) {
        return;
      }
      loading.value = true;
      const saveOrUpdate = isUpdate.value ? createReceiptOrderReqApi : createReceiptOrderReqApi;
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

  const receiptAccountList = ref([]);

  const getReceiptAccountList = async () => {
    try {
      const res = await getReceiptAccountListApi({
        page: 1,
        pageSize: 500
      });
      receiptAccountList.value = res.list;
      console.log(receiptAccountList.value)
    } catch (e) {
      EleMessage.error(e.message);
    }
  }

  watch(
    () => props.modelValue,
    (modelValue) => {
      if (modelValue) {
        getReceiptConfigList();
        getReceiptAccountList()
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
