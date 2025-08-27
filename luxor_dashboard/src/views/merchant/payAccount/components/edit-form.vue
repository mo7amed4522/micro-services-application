<template>
  <ele-modal form :width="550" :model-value="modelValue" :title="isUpdate ? t('common.edit') : t('common.add')" @update:modelValue="updateModelValue">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="140px" @submit.prevent="">
      <el-form-item :label="t('common.accountNameText')" prop="name">
        <el-input v-model="form.name" :placeholder="t('common.enterAccountNameText')" clearable />
      </el-form-item>

      <el-form-item :label="t('common.typeText')" prop="type" v-if="props.cPayPoolType === 'payout'">
        <el-select clearable v-model="form.type" :disabled="isUpdate" :placeholder="t('common.selectTypeText')" class="ele-fluid">
          <el-option v-for="(item, index) in typeList" :key="index" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <div v-if="form.type !== 4 && form.type !== 6">
        <el-form-item :label="t('payMethod.showTypeText')" prop="qrType">
          <el-radio-group v-model="form.qrType">
            <el-radio :value="0" v-if="props.cPayPoolType === 'payment'">{{ t('payMethod.qrCode') }}</el-radio>
            <el-radio :value="1">{{ t('payMethod.bankCard') }}</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item :label="t('payMethod.upiId')" prop="upiId" v-if="form.type !== 2 && form.type !== 3 && form.qrType === 0">
          <el-input v-model="form.upiId" :placeholder="t('payMethod.upiIdText')" clearable />
        </el-form-item>

        <template v-if="form.type !== 2 && form.type !== 3">
          <!-- 临时取消限制 -->
          <el-form-item :label="t('common.limitFlagText')" prop="limitFlag">
            <el-radio-group v-model="form.limitFlag">
              <el-radio :value="1">{{ t('common.close') }}</el-radio>
              <el-radio :value="2">{{ t('common.open') }}</el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item prop="successCountPerDay">
            <template #label>
              <div>
                <span style="color: red; margin-right: 4px" v-if="form.limitFlag === 1">*</span>
                {{ t('common.successCountPerDayText') }}
              </div>
            </template>
            <el-input v-model="form.successCountPerDay" :placeholder="t('mockOrder.enterText')" clearable>
              <template #append>
                <span :style="{ color: form.limitFlag === 1 ? 'green' : 'red' }">{{ form.limitFlag === 1 ? t('common.effective') : t('common.ineffective') }}</span>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item :label="t('common.successAmountPerDayText')" prop="successAmountPerDay">
            <template #label>
              <div>
                <span style="color: red; margin-right: 4px" v-if="form.limitFlag === 1">*</span>
                {{ t('common.successAmountPerDayText') }}
              </div>
            </template>
            <el-input v-model="form.successAmountPerDay" :placeholder="t('mockOrder.enterText')" clearable>
              <template #append>
                <span :style="{ color: form.limitFlag === 1 ? 'green' : 'red' }">{{ form.limitFlag === 1 ? t('common.effective') : t('common.ineffective') }}</span>
              </template>
            </el-input>
          </el-form-item>
        </template>

        <el-form-item :label="t('payMethod.scanCodeUrl')" prop="qrcode" v-if="form.qrType === 0">
          <el-input v-model="form.qrcode" :placeholder="t('payMethod.enterScanCodeUrl')" clearable />
        </el-form-item>

        <template v-if="form.type !== 2 && form.type !== 3">
          <el-form-item :label="t('payMethod.minAmount')" prop="limitAmountMin">
            <el-input v-model="form.limitAmountMin" :placeholder="t('payMethod.enterMinAmount')" clearable />
          </el-form-item>
          <el-form-item :label="t('payMethod.maxAmount')" prop="limitAmountMax">
            <el-input v-model="form.limitAmountMax" :placeholder="t('payMethod.enterMaxAmount')" clearable />
          </el-form-item>
        </template>
      </div>

      <el-form-item :label="t('common.notes')" prop="remark">
        <el-input v-model="form.remark" :placeholder="t('common.enterNotes')" clearable />
      </el-form-item>
      <el-form-item :label="t('common.status')" prop="valid">
        <el-radio-group v-model="form.valid">
          <el-radio :value="0">{{ t('common.disable') }}</el-radio>
          <el-radio :value="1">{{ t('common.enable') }}</el-radio>
        </el-radio-group>
      </el-form-item>

      <!-- 快捷选择账户 -->
      <el-form-item :label="t('common.paymentAccountText')" prop="cPayPoolCardsId">
        <el-select-v2
          v-model="form.cPayPoolCardsId"
          :options="payPoolCardsList"
          :props="{ label: 'name', value: 'id' }"
          clearable
          filterable
          :disabled="isUpdate"
          :placeholder="t('common.selectAccountText')"
          @change="changePayPoolCardsId"
        />
      </el-form-item>

      <el-form-item :label="t('payMethod.bankText')" prop="bankId">
        <el-select clearable v-model="form.bankId" disabled :placeholder="t('payMethod.selectBank')" class="ele-fluid" @change="changeBank">
          <el-option v-for="(item, index) in bankList" :key="index" :label="item.bankName" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('payMethod.ifscCode')" prop="bankMark">
        <el-input v-model="form.bankMark" disabled :placeholder="t('payMethod.enterIfscCode')" clearable />
      </el-form-item>
      <el-form-item :label="t('payMethod.cardholderName')" prop="cardName">
        <el-input v-model="form.cardName" disabled :disabled="isUpdate" :placeholder="t('payMethod.enterCardholderName')" clearable />
      </el-form-item>
      <el-form-item :label="t('payMethod.cardNumber')" prop="cardNumber">
        <el-input v-model="form.cardNumber" disabled :disabled="isUpdate" :placeholder="t('payMethod.enterCardNumber')" clearable />
      </el-form-item>
      <template v-if="form.type !== 2 && form.type !== 3">
        <el-form-item :label="t('payMethod.reservePhone')" prop="cardPhone">
          <el-input v-model="form.cardPhone" disabled :placeholder="t('payMethod.enterReservePhone')" clearable />
        </el-form-item>
      </template>
    </el-form>
    <template #footer>
      <el-button @click="updateModelValue(false)">{{ t('common.cancelText') }}</el-button>
      <el-button type="primary" :loading="loading" @click="save">{{ t('common.saveText') }}</el-button>
    </template>
  </ele-modal>
</template>

<script setup>
  import { ref, reactive, watch, computed, nextTick } from 'vue';
  import { EleMessage } from 'ele-admin-plus/es';
  import { useFormData } from '@/utils/use-form-data';
  import { QuestionCircleOutlined } from '@/components/icons/index.js';
  import { createCPayPoolAccountApi, updateCPayPoolAccountApi, getCPayPoolCardsListApi } from '@/api/merchant';
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
    },
    cPayPoolType: {
      type: String,
      default: ''
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
    cPayPoolCardsId: null,
    name: '',
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
    qrType: 1,
    qrcode: '',
    remark: '',
    valid: 1,
    upiId: '', // UPI ID
    type: null,
    limitFlag: 1, // 1 关闭 2开启
    successCountPerDay: '',
    successAmountPerDay: ''
  });

  const changePayPoolCardsId = (val) => {
    const payPoolCards = payPoolCardsList.value.find((item) => item.id === val);
    if (payPoolCards) {
      form.bankId = payPoolCards.bankId;
      form.bankMark = payPoolCards.bankMark;
      form.bankName = payPoolCards.bankName;
      form.cardName = payPoolCards.cardName;
      form.cardNumber = payPoolCards.cardNumber;
      form.cardPhone = payPoolCards.cardPhone;
    } else {
      form.bankId = null;
      form.bankMark = '';
      form.bankName = '';
      form.cardName = '';
      form.cardNumber = '';
      form.cardPhone = '';
    }
  };

  const bankList = ref([]);

  const typeList = computed(() => [
    // 收款账户1  出款账户2  备付金账户3  代理4  即收款也出款账户5   三方6
    { label: t('common.payoutAccountText'), value: 2 },
    { label: t('common.provisionsText'), value: 3 },
    { label: t('common.agentText'), value: 4 },
    { label: t('common.thirdPartyText'), value: 6 }
  ]);

  /** 表单验证规则 */
  const rules = reactive({
    name: [{ required: true, message: t('common.pleaseEnterPaymentMethod'), trigger: 'blur' }],
    type: [{ required: true, message: t('common.selectTypeText'), trigger: 'change' }],
    limitFlag: [
      { required: true, message: t('mockOrder.selectText'), trigger: 'change' },
      {
        validator: (rule, value, callback) => {
          if (value === 0) callback(new Error(t('mockOrder.selectText')));
          else callback();
        },
        trigger: 'change'
      }
    ],
    // successCountPerDay: [{ required: true, message: t('mockOrder.enterText'), trigger: 'blur' }],
    // successAmountPerDay: [{ required: true, message: t('mockOrder.enterText'), trigger: 'blur' }],
    successCountPerDay: [
      {
        validator: (rule, value, callback) => {
          if (form.limitFlag === 1 && !value) {
            callback(new Error(t('mockOrder.enterText')));
          } else {
            callback();
          }
        },
        trigger: 'blur' // 根据需求调整触发事件
      }
    ],
    successAmountPerDay: [
      {
        validator: (rule, value, callback) => {
          if (form.limitFlag === 1 && !value) {
            callback(new Error(t('mockOrder.enterText')));
          } else {
            callback();
          }
        },
        trigger: 'blur'
      }
    ],

    bankMark: [{ required: true, message: t('payMethod.enterIfscCode'), trigger: 'change' }],
    upiId: [{ required: true, message: t('payMethod.upiIdText'), trigger: 'blur' }],
    bankId: [{ required: true, message: t('payMethod.selectBank'), trigger: 'change' }],
    cardName: [{ required: true, message: t('payMethod.enterCardholderName'), trigger: 'blur' }],
    cardNumber: [{ required: true, message: t('payMethod.enterCardNumber'), trigger: 'blur' }],
    cardPhone: [{ required: true, message: t('payMethod.enterReservePhone'), trigger: 'blur' }],
    limitAmountMin: [{ required: true, message: t('payMethod.enterMinAmount'), trigger: 'blur' }],
    limitAmountMax: [{ required: true, message: t('payMethod.enterMaxAmount'), trigger: 'blur' }],
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
      form.successCountPerDay = Number(form.successCountPerDay);
      form.successAmountPerDay = parseFloat(form.successAmountPerDay);
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

  const payPoolCardsList = ref([]);

  const getPayPoolCardsList = async () => {
    try {
      const res = await getCPayPoolCardsListApi({
        page: 1,
        pageSize: 1000
      });
      payPoolCardsList.value = res.list;
      console.log(payPoolCardsList.value, 'sdfsdfdsf');
    } catch (error) {
      EleMessage.error(error.message);
    }
  };

  watch(
    () => props.modelValue,
    async (modelValue) => {
      if (modelValue) {
        if (props.data) {
          assignFields({
            ...props.data
          });
          isUpdate.value = true;
        } else {
          isUpdate.value = false;
          if (props.cPayPoolType == 'payment') {
            form.cPayPoolId = props.cPayPoolId;
            form.type = 1;
          } else {
            // 出款账户不绑定支付池id
            form.cPayPoolId = null;
          }
        }
        await getBankList();
        await getPayPoolCardsList();
      } else {
        resetFields();
        formRef.value?.clearValidate?.();
      }
    }
  );

  watch(
    () => form.limitFlag,
    () => {
      nextTick(() => {
        formRef.value?.validateField(['successCountPerDay', 'successAmountPerDay']);
      });
    }
  );
</script>
