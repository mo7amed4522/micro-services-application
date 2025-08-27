<template>
  <ele-modal form :width="550" :model-value="modelValue" :title="isUpdate ? t('common.edit') : t('common.add')" @update:modelValue="updateModelValue">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="140px" @submit.prevent="">
      <el-form-item :label="t('payMethod.bankText')" prop="type">
        <el-select clearable v-model="form.type" :disabled="isUpdate" :placeholder="t('payMethod.selectBank')" class="ele-fluid" @change="changeBank">
          <el-option v-for="(item, index) in typeList" :key="index" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      
      <el-form-item :label="t('payMethod.showTypeText')" prop="qrType">
        <el-radio-group v-model="form.qrType">
          <el-radio :value="0">{{ t('payMethod.qrCode') }}</el-radio>
          <el-radio :value="1">{{ t('payMethod.bankCard') }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <!-- <el-form-item label="实际金额" prop="amountActual">
        <el-input v-model="form.amountActual" placeholder="请输入实际金额" clearable />
      </el-form-item> -->
      <el-form-item :label="t('payMethod.upiId')" prop="upiId">
        <el-input v-model="form.upiId" :placeholder="t('payMethod.upiIdText')" clearable />
      </el-form-item>
      <div v-if="form.qrType === 0">
        <el-form-item :label="t('payMethod.qrCodeContent')" prop="qrContent">
          <el-input v-model="form.qrContent" type="textarea" :rows="2" :placeholder="t('payMethod.enterQrCodeContent')" clearable />
        </el-form-item>
        <el-form-item :label="t('payMethod.scanCodeUrl')" prop="qrcode">
          <el-input v-model="form.qrcode" :placeholder="t('payMethod.enterScanCodeUrl')" clearable />
        </el-form-item>
        <el-form-item :label="t('payMethod.qrCodeRemark')" prop="qrcodeMsg">
          <el-input v-model="form.qrcodeMsg" :placeholder="t('payMethod.enterQrCodeRemark')" clearable />
        </el-form-item>
      </div>
      <el-form-item :label="t('payMethod.bankText')" prop="bankId">
        <el-select clearable v-model="form.bankId" :placeholder="t('payMethod.selectBank')" class="ele-fluid" @change="changeBank">
          <el-option v-for="(item, index) in bankList" :key="index" :label="item.bankName" :value="item.id" />
        </el-select>
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

      <el-form-item :label="t('payMethod.minAmount')" prop="limitAmountMin">
        <el-input v-model="form.limitAmountMin" :placeholder="t('payMethod.enterMinAmount')" clearable />
      </el-form-item>
      <el-form-item :label="t('payMethod.maxAmount')" prop="limitAmountMax">
        <el-input v-model="form.limitAmountMax" :placeholder="t('payMethod.enterMaxAmount')" clearable />
      </el-form-item>

      <el-form-item :label="t('common.notes')" prop="remark">
        <el-input v-model="form.remark" :placeholder="t('common.enterNotes')" clearable />
      </el-form-item>
      <el-form-item :label="t('common.status')" prop="valid">
        <el-radio-group v-model="form.valid">
          <el-radio :value="0">{{ t('common.disable') }}</el-radio>
          <el-radio :value="1">{{ t('common.enable') }}</el-radio>
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
  import { createCPayPoolAccountApi, updateCPayPoolAccountApi } from '@/api/merchant';
  import { getPayBankListApi } from '@/api/payConfig';
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
    },
    cPayPoolId: {
      type: Number,
      default: 0
    }
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
    cPayPoolId: '',
    // amountActual: '', // 实际金额
    bankId: null,
    bankMark: '',
    bankName: '',
    cardName: '',
    cardNumber: '',
    cardPhone: '',
    limitAmountMax: '',
    limitAmountMin: '',
    qrContent: '',
    qrType: 0,
    qrcode: '',
    qrcodeMsg: '',
    remark: '',
    valid: 1,
    upiId: '', // UPI ID
    type: null
  });

  const bankList = ref([]);

  const typeList = ref([
    // 收款账户1  出款账户2  备付金账户3  代理4  即收款也出款账户5   三方6
    { label: t('common.paymentAccountText'), value: 1 },
    { label: t('common.payoutAccountText'), value: 2 },
    { label: t('common.provisionsText'), value: 3 },
    { label: t('common.agentText'), value: 4 },
    { label: t('common.payOrPayoutAccountText'), value: 5 },
    { label: t('common.thirdPartyText'), value: 6 }
  ])

  /** 表单验证规则 */
  const rules = reactive({
    type: [{ required: true, message: '请选择类型', trigger: 'change' }],
    // amountActual: [{ required: true, message: '请输入二维码的实际金额', trigger: 'blur' }],
    upiId: [{ required: true, message: t('payMethod.upiIdText'), trigger: 'blur' }],
    bankId: [{ required: true, message: t('payMethod.selectBank'), trigger: 'change' }],
    cardName: [{ required: true, message: t('payMethod.enterCardholderName'), trigger: 'blur' }],
    cardNumber: [{ required: true, message: t('payMethod.enterCardNumber'), trigger: 'blur' }],
    cardPhone: [{ required: true, message: t('payMethod.enterReservePhone'), trigger: 'blur' }],
    limitAmountMin: [{ required: true, message: t('payMethod.enterMinAmount'), trigger: 'blur' }],
    limitAmountMax: [{ required: true, message: t('payMethod.enterMaxAmount'), trigger: 'blur' }],
    qrContent: [{ required: true, message: t('payMethod.enterQrCodeContent'), trigger: 'blur' }],
    qrType: [{ required: true, message: '请选择二维码类型', trigger: 'change' }],
    qrcode: [{ required: true, message: t('payMethod.enterScanCodeUrl'), trigger: 'blur' }],
    valid: [{ required: true, message: '请选择是否启用', trigger: 'change' }]
  });

  // 选择银行
  const changeBank = (bankId) => {
    const bank = bankList.value.find((item) => item.id === bankId);
    form.bankMark = bank.bankCode;
    form.bankName = bank.bankName;
  };

  // 获取银行列表
  const getBankList = async () => {
    const res = await getPayBankListApi({
      page: 1,
      pageSize: 9999
    });
    bankList.value = res.list;
  };

  /** 保存编辑 */
  const save = () => {
    formRef.value?.validate?.((valid) => {
      if (!valid) {
        return;
      }
      loading.value = true;
      const saveOrUpdate = isUpdate.value ? updateCPayPoolAccountApi : createCPayPoolAccountApi;
      form.amountActual = parseFloat(form.amountActual);
      form.limitAmountMin = parseFloat(form.limitAmountMin);
      form.limitAmountMax = parseFloat(form.limitAmountMax);
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
        getBankList();
        if (props.data) {
          assignFields({
            ...props.data
          });
          isUpdate.value = true;
        } else {
          isUpdate.value = false;
          form.cPayPoolId = props.cPayPoolId;
        }
      } else {
        resetFields();
        formRef.value?.clearValidate?.();
      }
    }
  );
</script>
