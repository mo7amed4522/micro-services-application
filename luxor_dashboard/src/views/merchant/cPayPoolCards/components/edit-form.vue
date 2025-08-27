<template>
  <ele-modal form :width="550" :model-value="modelValue" :title="isUpdate ? t('common.edit') : t('common.add')" @update:modelValue="updateModelValue">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="120px" @submit.prevent="">
      <el-form-item :label="t('common.accountNameText')" prop="name">
        <el-input v-model="form.name" :placeholder="t('common.enterAccountNameText')" clearable />
      </el-form-item>
      <el-form-item :label="t('payMethod.bankText')" prop="bankId">
        <el-select clearable v-model="form.bankId" :placeholder="t('payMethod.selectBank')" class="ele-fluid" @change="changeBank">
          <el-option v-for="(item, index) in bankList" :key="index" :label="item.bankName" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('common.thresholdText')" prop="maxAmount">
        <el-input v-model="form.maxAmount" :placeholder="t('common.noLimitText')" clearable />
      </el-form-item>
      <el-form-item :label="t('payMethod.ifscCode')" prop="bankMark">
        <el-input v-model="form.bankMark" :placeholder="t('payMethod.enterIfscCode')" clearable />
      </el-form-item>
      <el-form-item :label="t('payMethod.cardholderName')" prop="cardName">
        <el-input v-model="form.cardName" :disabled="isUpdate" :placeholder="t('payMethod.enterCardholderName')" clearable />
      </el-form-item>
      <el-form-item :label="t('payMethod.cardNumber')" prop="cardNumber">
        <el-input v-model="form.cardNumber" :disabled="isUpdate" :placeholder="t('payMethod.enterCardNumber')" clearable />
      </el-form-item>
      <el-form-item :label="t('payMethod.reservePhone')" prop="cardPhone">
        <el-input v-model="form.cardPhone" :placeholder="t('payMethod.enterReservePhone')" clearable />
      </el-form-item>
      <el-form-item :label="t('common.notes')" prop="remark">
        <el-input v-model="form.remark" :placeholder="t('common.enterNotes')" clearable />
      </el-form-item>
      <el-form-item :label="t('common.status')" prop="valid">
        <el-radio-group v-model="form.valid">
          <el-radio :value="2">{{ t('common.disable') }}</el-radio>
          <el-radio :value="1">{{ t('common.enable') }}</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="updateModelValue(false)">取消</el-button>
      <el-button type="primary" :loading="loading" @click="save">保存</el-button>
    </template>
  </ele-modal>
</template>

<script setup>
  import { ref, reactive, watch } from 'vue';
  import { EleMessage } from 'ele-admin-plus/es';
  import { useFormData } from '@/utils/use-form-data';
  import { QuestionCircleOutlined } from '@/components/icons/index.js';
  import { createCPayPoolCardsApi, updateCPayPoolCardsApi } from '@/api/merchant';
  import { getPayBankListApi } from '@/api/payConfig';
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
    name: '',
    valid: 1,
    remark: '',
    cardName: '',
    cardNumber: '',
    bankMark: '',
    bankName: '',
    cardPhone: '',
    bankId: null,
    maxAmount: 0,
  });

  // 选择银行
  const changeBank = (bankId) => {
    const bank = bankList.value.find((item) => item.id === bankId);
    form.bankMark = bank.bankCode;
    form.bankName = bank.bankName;
  };

  const bankList = ref([]);

  // 获取银行列表
  const getBankList = async () => {
    const res = await getPayBankListApi({
      page: 1,
      pageSize: 9999
    });
    bankList.value = res.list;
  };

  /** 表单验证规则 */
  const rules = reactive({
    name: [{ required: true, message: t('common.pleaseEnterPaymentMethod'), trigger: 'blur' }],
    bankMark: [{ required: true, message: t('payMethod.enterIfscCode'), trigger: 'change' }],
    bankId: [{ required: true, message: t('payMethod.selectBank'), trigger: 'change' }],
    cardName: [{ required: true, message: t('payMethod.enterCardholderName'), trigger: 'blur' }],
    cardNumber: [{ required: true, message: t('payMethod.enterCardNumber'), trigger: 'blur' }],
    cardPhone: [{ required: true, message: t('payMethod.enterReservePhone'), trigger: 'blur' }],
    valid: [{ required: true, message: '请选择是否启用', trigger: 'change' }],
    maxAmount: [{ required: true, message: t('common.pleaseEnterThreshold'), trigger: 'blur' }]
  });

  /** 保存编辑 */
  const save = () => {
    formRef.value?.validate?.((valid) => {
      if (!valid) {
        return;
      }
      loading.value = true;
      const saveOrUpdate = isUpdate.value ? updateCPayPoolCardsApi : createCPayPoolCardsApi;
      saveOrUpdate({ 
        ...form,
        maxAmount: parseFloat(form.maxAmount)
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
      getBankList();
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
