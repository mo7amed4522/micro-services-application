<template>
  <ele-modal
    form
    :width="680"
    :model-value="modelValue"
    :title="props.actionType === 'examine' ? t('common.orderAuditText') : t('common.disbursementSupplement')"
    @update:modelValue="updateModelValue"
  >
    <el-form ref="formRef" :model="form" :rules="rules" label-width="140px" @submit.prevent="">
      <el-form-item :label="t('payMethod.bankName') + '：'">
        <span>{{ props.data.cardBank }}</span>
        <el-button class="btn" type="primary" link @click="copyText(props.data.cardBank, t)">{{ t('common.copy') }}</el-button>
      </el-form-item>
      <el-form-item :label="t('payMethod.ifscCode') + '：'">
        <span>{{ props.data.ifsc }}</span>
        <el-button class="btn" type="primary" link @click="copyText(props.data.ifsc, t)">{{ t('common.copy') }}</el-button>
      </el-form-item>
      <el-form-item :label="t('outOrder.payeeName') + '：'">
        <span>{{ props.data.cardName }}</span>
        <el-button class="btn" type="primary" link @click="copyText(props.data.cardName, t)">{{ t('common.copy') }}</el-button>
      </el-form-item>
      <el-form-item :label="t('outOrder.payeeCardNumber') + '：'">
        <span>{{ props.data.cardNumber }}</span>
        <el-button class="btn" type="primary" link @click="copyText(props.data.cardNumber, t)">{{ t('common.copy') }}</el-button>
      </el-form-item>
      <el-form-item :label="t('outOrder.actualTradeAmount') + '：'">
        <span>{{ props.data.actualAmount }}</span>
      </el-form-item>
      <el-form-item :label="t('orderList.orderNo') + '：'">
        <span>{{ form.orderNo }}</span>
        <el-button class="btn" type="primary" link @click="copyText(form.orderNo, t)">{{ t('common.copy') }}</el-button>
      </el-form-item>
      <el-form-item :label="t('orderList.thirdTradeNo') + '：'">
        <span>{{ form.thirdOrderNo }}</span>
        <el-button class="btn" type="primary" link @click="copyText(form.thirdOrderNo, t)">{{ t('common.copy') }}</el-button>
      </el-form-item>
      <el-form-item :label="t('orderList.oldOrderStatus') + '：'">
        <span>{{ props.actionType === 'examine' ? t('common.paymentRequestText') : t('common.paymentSuccessText') }}</span>
      </el-form-item>
      <el-form-item :label="t('common.copyOperationText') + '：'">
        <el-button type="primary" @click="copy">{{ t('common.oneCopyText') }}</el-button>
      </el-form-item>
      <el-form-item :label="t('orderList.editStatusText')" prop="status" v-if="props.actionType === 'examine'">
        <el-select clearable v-model="form.status" :empty-values="[null, undefined]" :placeholder="t('orderList.selectOrderStatus')" class="ele-fluid" @click="statusChange">
          <el-option v-for="(item, index) in statusOptions" :key="index" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>

      <template v-if="form.status == 2 || props.actionType === 'replenishment'">
        <el-form-item :label="t('channelConfig.channelType')" prop="type">
          <el-select clearable v-model="form.type" :empty-values="[null, undefined]" :placeholder="t('channelConfig.selectChannelType')" class="ele-fluid" @change="channelTypeChange">
            <el-option v-for="(item, index) in channelTypeOptions" :key="index" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>

        <el-form-item :label="t('common.payoutAccountText')" prop="cPayPoolAccountID">
          <el-select clearable v-model="form.cPayPoolAccountID" :empty-values="[null, undefined]" filterable :placeholder="t('common.selectPayoutAccountText')" class="ele-fluid">
            <el-option v-for="(item, index) in payoutAccountOptions" :key="index" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
      </template>

      <el-form-item :label="t('common.failReasonText')" prop="failMessageId" v-if="form.status === -1">
        <el-select clearable v-model="form.failMessageId" :empty-values="[null, undefined]" :placeholder="t('common.selectFailReasonText')" class="ele-fluid" @change="failMessageChange">
          <el-option v-for="(item, index) in failMessageList" :key="index" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>

      <el-form-item :label="t('common.notes')" prop="remark" v-if="props.actionType === 'examine'">
        <el-input v-model="form.message" type="textarea" :rows="3" :placeholder="t('common.enterNotes')" />
      </el-form-item>

      <el-form-item :label="t('common.notes')" prop="remark" v-if="props.actionType === 'replenishment'">
        <el-input v-model="form.remark" type="textarea" :rows="3" :placeholder="t('common.enterNotes')" />
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
  import { payoutOrderResetApi, getListByTypeApi, replenishmentPayoutOrderApi } from '@/api/order';
  import { copyText } from '@/utils/public';
  import { useI18n } from 'vue-i18n';

  const { t } = useI18n();

  const emit = defineEmits(['done', 'update:modelValue']);

  const props = defineProps({
    /** 弹窗是否打开 */
    modelValue: Boolean,
    /** 修改回显的数据 */
    data: Object,
    actionType: {
      type: String,
      default: 'examine'
    }
  });

  /** 提交状态 */
  const loading = ref(false);

  /** 表单实例 */
  const formRef = ref(null);

  /** 表单数据 */
  const [form, resetFields, assignFields] = useFormData({
    id: void 0,
    orderNo: '',
    status: '',
    cPayPoolAccountID: null,
    id: '', // 订单id
    type: null,
    remark: '',
    failMessage: '',
    failMessageId: null,
    message: '',
    thirdOrderNo: '' // 第三方订单号
  });

  /** 表单验证规则 */
  const rules = reactive({
    status: [{ required: true, message: t('common.selectStatus'), trigger: 'change' }],
    type: [{ required: true, message: t('channelConfig.selectChannelType'), trigger: 'change' }],
    cPayPoolAccountID: [{ required: true, message: t('common.selectPayoutAccountText'), trigger: 'change' }],
    failMessageId: [{ required: true, message: t('common.selectFailReasonText'), trigger: 'change' }]
  });

  const statusOptions = computed(() => [
    { id: 2, name: t('outOrder.payOutSuccessText') },
    { id: -1, name: t('outOrder.payOutFailText') }
  ]);

  const channelTypeOptions = computed(() => [
    { label: t('common.selfPayText'), value: 2 },
    { label: t('common.agentPayText'), value: 4 },
    { label: t('common.thirdPartyText'), value: 6 }
  ]);

  const failMessageList = computed(() => [
    { label: t('common.paymentInfoError'), enLabel: 'Payment information error', value: 1 },
    { label: t('common.multipleRebound'), enLabel: 'Multiple rejections', value: 2 },
    { label: t('common.transferFail'), enLabel: 'Transfer failure', value: 3 },
    { label: t('common.merchantClose'), enLabel: 'Merchant requests closure', value: 4 }
  ]);

  const failMessageChange = (val) => {
    const item = failMessageList.value.find((item) => item.value === val);
    form.failMessage = item.enLabel;
  };

  const statusChange = (val) => {
    if (val === 2) {
      form.filMessageId = null;
      form.failMessage = '';
    }
  };

  // 出款账户
  const payoutAccountOptions = ref([]);

  // 渠道类型改变
  const channelTypeChange = async (val) => {
    const res = await getListByTypeApi({
      page: 1,
      pageSize: 9999,
      type: val == 2 ? '1,2,3' : val
    });
    payoutAccountOptions.value = res.list;
    if (val == -1) {
      form.cPayPoolAccountID = null;
    }
  };

  // 一键复制
  const copy = () => {
    const paymentStr = props.actionType === 'examine' ? t('common.paymentRequestText') : t('common.paymentSuccessText');
    const str =
      t('payMethod.bankName') +
      '：' +
      props.data.cardBank +
      '\n' +
      t('payMethod.ifscCode') +
      '：' +
      props.data.ifsc +
      '\n' +
      t('outOrder.payeeName') +
      '：' +
      props.data.cardName +
      '\n' +
      t('outOrder.payeeCardNumber') +
      '：' +
      props.data.cardNumber +
      '\n' +
      t('outOrder.actualTradeAmount') +
      '：' +
      props.data.actualAmount +
      '\n' +
      t('orderList.orderNo') +
      '：' +
      props.data.orderNo +
      '\n' +
      t('orderList.oldOrderStatus') +
      '：' +
      paymentStr;
    copyText(str, t);
  };

  /** 保存编辑 */
  const save = () => {
    formRef.value?.validate?.((valid) => {
      if (!valid) {
        return;
      }
      loading.value = true;
      const saveOrUpdate = props.actionType === 'examine' ? payoutOrderResetApi : replenishmentPayoutOrderApi;
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
          form.cPayPoolAccountID = null;
          form.status = null;
          form.remark = null;
        }
      } else {
        resetFields();
        formRef.value?.clearValidate?.();
      }
    }
  );
</script>
<style lang="scss" scoped>
  .btn {
    margin-left: 20px;
  }
</style>
