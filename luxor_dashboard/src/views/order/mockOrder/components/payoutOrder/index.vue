<template>
  <div class="form">
    <el-form ref="formRef" :model="form" :rules="rules" label-position="top" @submit.prevent="">
      <el-row :gutter="60">
        <el-col :lg="12" :md="12" :sm="12" :xs="24">
          <el-form-item prop="payType">
            <template #label>
              <span>
                {{ t('mockOrder.payTypeText') }}
                <span class="label-color">(payType)</span>
              </span>
            </template>
            <el-select clearable v-model="form.payType" :empty-values="[null, undefined]" :placeholder="t('mockOrder.selectPayType')" class="ele-fluid">
              <el-option v-for="(item, index) in payTypeOptions" :key="index" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="12" :xs="24">
          <el-form-item prop="mchNo">
            <template #label>
              <span>
                {{ t('merchant.merchantNum') }}
                <span class="label-color">(mchNo)</span>
              </span>
            </template>
            <el-input v-model="form.mchNo" disabled :placeholder="t('mockOrder.enterMerchantNumText')" clearable />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="60">
        <el-col :lg="12" :md="12" :sm="12" :xs="24">
          <el-form-item prop="cardName">
            <template #label>
              <span>
                {{ t('outOrder.payeeName') }}
                <span class="label-color">(cardName)</span>
              </span>
            </template>
            <el-input v-model="form.cardName" :placeholder="t('mockOrder.enterText')" clearable />
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="12" :xs="24">
          <el-form-item prop="cardNumber">
            <template #label>
              <span>
                {{ t('mockOrder.payeeAccountText') }}
                <span class="label-color">(cardNumber)</span>
              </span>
            </template>
            <el-input v-model="form.cardNumber" :placeholder="t('mockOrder.enterText')" clearable />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="60">
        <el-col :lg="12" :md="12" :sm="12" :xs="24">
          <el-form-item prop="cardBank">
            <template #label>
              <span>
                {{ t('mockOrder.payeeBankText') }}
                <span class="label-color">(cardBank)</span>
              </span>
            </template>
            <el-input v-model="form.cardBank" :placeholder="t('mockOrder.enterText')" clearable />
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="12" :xs="24">
          <el-form-item prop="ifsc">
            <template #label>
              <span>
                {{ t('payMethod.ifscCode') }}
                <span class="label-color">(ifsc)</span>
              </span>
            </template>
            <el-input v-model="form.ifsc" :placeholder="t('mockOrder.enterText')" clearable />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="60">
        <el-col :lg="12" :md="12" :sm="12" :xs="24">
          <el-form-item prop="thirdOrderNo">
            <template #label>
              <span>
                {{ t('mockOrder.merchantOrderNoText') }}
                <span class="label-color">(thirdOrderNo)</span>
              </span>
            </template>
            <el-input v-model="form.thirdOrderNo" :placeholder="t('mockOrder.enterText')" clearable />
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="12" :xs="24">
          <el-form-item prop="amount">
            <template #label>
              <span>
                {{ t('mockOrder.orderAmountText') }}
                <span class="label-color">(amount)</span>
              </span>
            </template>
            <el-input v-model="form.amount" :placeholder="t('mockOrder.enterText')" clearable />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="60">
        <el-col :lg="12" :md="12" :sm="12" :xs="24">
          <el-form-item prop="notifyUrl">
            <template #label>
              <span>
                {{ t('mockOrder.notifyUrlText') }}
                <span class="label-color">(notifyUrl)</span>
              </span>
            </template>
            <el-input v-model="form.notifyUrl" :placeholder="t('mockOrder.enterText')" clearable />
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="12" :xs="24">
          <el-form-item prop="currency">
            <template #label>
              <span>
                {{ t('mockOrder.currencyText') }}
                <span class="label-color">(currency)</span>
              </span>
            </template>
            <el-input v-model="form.currency" disabled :placeholder="t('mockOrder.enterText')" clearable />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="60">
        <el-col :lg="12" :md="12" :sm="12" :xs="24">
          <el-form-item prop="remark">
            <template #label>
              <span>
                {{ t('mockOrder.remarkText') }}
                <span class="label-color">(remark)</span>
              </span>
            </template>
            <el-input v-model="form.remark" :placeholder="t('mockOrder.enterText')" clearable />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="60" v-if="form?.sign">
        <el-col :lg="24" :md="24" :sm="24" :xs="24">
          <el-form-item :label="t('mockOrder.requestParamsText')">
            <div class="card">{{ JSON.stringify(form) }}</div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="60" v-if="Object.keys(payoutOrderInfo).length">
        <el-col :lg="24" :md="24" :sm="24" :xs="24">
          <el-form-item :label="t('mockOrder.responseParamsText')">
            <div class="card">{{ JSON.stringify(payoutOrderInfo) }}</div>
          </el-form-item>
        </el-col>
      </el-row>

      <template v-if="Object.keys(payoutOrderInfo).length">
        <el-row :gutter="60">
          <el-col :lg="12" :md="12" :sm="12" :xs="24">
            <el-form-item :label="t('mockOrder.platformOrderNoText')">
              <span>{{ payoutOrderInfo?.orderNo }}</span>
            </el-form-item>
          </el-col>
          <el-col :lg="12" :md="12" :sm="12" :xs="24">
            <el-form-item :label="t('mockOrder.merchantOrderNo')">
              <span>{{ payoutOrderInfo?.thirdOrderNo }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="60">
          <el-col :lg="12" :md="12" :sm="12" :xs="24">
            <el-form-item :label="t('mockOrder.orderAmountText')">
              <span>{{ payoutOrderInfo?.amount }}</span>
            </el-form-item>
          </el-col>
          <el-col :lg="12" :md="12" :sm="12" :xs="24">
            <el-form-item :label="t('mockOrder.orderTimeText')">
              <span>{{ _timeFormat(payoutOrderInfo?.orderTime) }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="60">
          <el-col :lg="12" :md="12" :sm="12" :xs="24">
            <el-form-item :label="t('mockOrder.payType')">
              <span>银行卡</span>
            </el-form-item>
          </el-col>
          <el-col :lg="12" :md="12" :sm="12" :xs="24">
            <el-form-item :label="t('mockOrder.orderStatusText')">
              <el-tag v-if="payoutOrderInfo?.status == 1" type="warning">{{ t('outOrder.payOutingText') }}</el-tag>
              <el-tag v-if="payoutOrderInfo?.status == 2" type="success">{{ t('outOrder.payOutSuccessText') }}</el-tag>
              <el-tag v-if="payoutOrderInfo?.status == -1" type="danger">{{ t('outOrder.payOutFailText') }}</el-tag>
            </el-form-item>
          </el-col>
        </el-row>
      </template>
    </el-form>
    <div class="footer">
      <el-button type="primary" :loading="loading" @click="save">{{ t('common.submitText') }}</el-button>
    </div>
  </div>
</template>
<script setup>
  import { ref, reactive } from 'vue';
  import { useI18n } from 'vue-i18n';
  import { generateSign, _timeFormat } from '@/utils/public';
  import { mockPayoutOrderApi } from '@/api/order';
  import { ElMessage } from 'element-plus';
  import { formatToTwoDecimals } from '@/utils/public';

  const { t } = useI18n();

  /** 提交状态 */
  const loading = ref(false);

  /** 表单实例 */
  const formRef = ref(null);

  // 生成订单号
  const generateOrderNumber = () => {
    const length = Math.floor(Math.random() * (32 - 14 + 1)) + 14; // 随机生成14到32字符之间的长度
    let orderId = '';

    // 生成纯数字
    for (let i = 0; i < length; i++) {
      orderId += Math.floor(Math.random() * 10); // 生成0-9的数字
    }

    return orderId;
  };

  const form = ref({
    payType: '1', // 支付类型
    mchNo: 'msLkxPAPLGYE', // 商户号
    cardName: '许三', // 商户下的用户标识
    cardNumber: '6222028970042409623', // 商户下的用户名称
    thirdOrderNo: generateOrderNumber(),
    amount: '100.00', // 订单金额
    notifyUrl: 'http://www.baidu.com', // 服务端后台支付结果通知地址
    cardBank: '印度银行',
    ifsc: 'ICIC0001234',
    remark: 'test',
    currency: 'inr',
    sign: '' // 签名
  });

  const payTypeOptions = ref([{ label: '银行卡', value: '1' }]);

  const rules = reactive({
    payType: [{ required: true, message: t('mockOrder.selectText'), trigger: 'change' }],
    mchNo: [{ required: true, message: t('mockOrder.enterText'), trigger: 'blur' }],
    cardName: [{ required: true, message: t('mockOrder.enterText'), trigger: 'blur' }],
    cardNumber: [{ required: true, message: t('mockOrder.enterText'), trigger: 'blur' }],
    thirdOrderNo: [{ required: true, message: t('mockOrder.enterText'), trigger: 'blur' }],
    amount: [{ required: true, message: t('mockOrder.enterText'), trigger: 'blur' }],
    notifyUrl: [{ required: true, message: t('mockOrder.enterText'), trigger: 'blur' }],
    cardBank: [{ required: true, message: t('mockOrder.enterText'), trigger: 'blur' }],
    ifsc: [{ required: true, message: t('mockOrder.enterText'), trigger: 'blur' }],
    remark: [{ required: true, message: t('mockOrder.enterText'), trigger: 'blur' }],
    currency: [{ required: true, message: t('mockOrder.enterText'), trigger: 'blur' }]
  });

  const payoutOrderInfo = ref({});

  const save = () => {
    formRef.value?.validate?.((valid) => {
      if (!valid) {
        return;
      }
      form.value.amount = formatToTwoDecimals(form.value.amount)
      form.value.sign = generateSign(form.value, 'demo123456');
      loading.value = true;
      mockPayoutOrderApi({
        ...form.value
      })
        .then((res) => {
          loading.value = false;
          form.value.thirdOrderNo = generateOrderNumber();
          ElMessage({
            message: res.msg,
            type: 'success'
          });
          payoutOrderInfo.value = res.data;
        })
        .catch((e) => {
          loading.value = false;
          ElMessage.error(e.message);
        });
    });
  };
</script>
<style lang="scss" scoped>
  .form {
    padding: 20px 40px;
    box-sizing: border-box;
  }
  .footer {
    text-align: center;
  }
  .card {
    width: 100%;
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    padding: 20px;
    box-sizing: border-box;
    word-wrap: break-word;
  }
  .label-color {
    color: red;
    margin-left: 5px;
  }
</style>
