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
            <el-select clearable v-model="form.payType" :empty-values="[null, undefined]" :placeholder="t('mockOrder.selectPayType')" class="ele-fluid" @change="getConfigAndPayPoolList">
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
          <el-form-item prop="mchUserNo">
            <template #label>
              <span>
                {{ t('mockOrder.merchantUserIdText') }}
                <span class="label-color">(mchUserNo)</span>
              </span>
            </template>
            <el-input v-model="form.mchUserNo" :placeholder="t('mockOrder.enterText')" clearable />
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="12" :xs="24">
          <el-form-item prop="mchUserName">
            <template #label>
              <span>
                {{ t('mockOrder.merchantUserNameText') }}
                <span class="label-color">(mchUserName)</span>
              </span>
            </template>
            <el-input v-model="form.mchUserName" :placeholder="t('mockOrder.enterText')" clearable />
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
          <el-form-item prop="returnUrl">
            <template #label>
              <span>
                {{ t('mockOrder.frontNotifyUrlText') }}
                <span class="label-color">(returnUrl)</span>
              </span>
            </template>
            <el-input v-model="form.returnUrl" :placeholder="t('mockOrder.enterText')" clearable />
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

        <el-col :lg="12" :md="12" :sm="12" :xs="24">
          <el-form-item prop="labelCode">
            <template #label>
              <span>
                {{ t('mockOrder.tagCodeText') }}
                <span class="label-color">(labelCode)</span>
              </span>
            </template>
            <el-input v-model="form.labelCode" :placeholder="t('mockOrder.enterText')" clearable />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="60">
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

        <el-col :lg="12" :md="12" :sm="12" :xs="24">
          <el-form-item prop="cPayPoolId">
            <template #label>
              <span>
                {{ t('utrOrder.channelAndPoolText') }}
                <span class="label-color">(cPayPoolId)</span>
              </span>
            </template>
            <el-select v-model="form.cPayPoolId" clearable :empty-values="[null, undefined]" :placeholder="t('utrOrder.selectChannelAndPool')" @change="getPayPoolAccountList">
              <el-option v-for="(item, pIndex) in payPoolListOptions" :key="pIndex" :label="item.cPayPoolName + '(' + item.name + ')'" :value="item.cPayPoolId" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="60">
        <el-col :lg="12" :md="12" :sm="12" :xs="24" v-if="form.cPayPoolId">
          <el-form-item prop="cPayPoolAccountId">
            <template #label>
              <span>
                {{ t('utrOrder.channelAndPoolText') }}
                <span class="label-color">(cPayPoolAccountId)</span>
              </span>
            </template>
            <el-select v-model="form.cPayPoolAccountId" clearable :empty-values="[null, undefined]" :placeholder="t('utrOrder.selectBankCard')">
              <el-option v-for="(item, cardIndex) in bankCardOptions" :key="cardIndex" :label="item.cardNumber + '(' + item.cPoolAccountName + ')'" :value="item.id" />
            </el-select>
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
      <el-row :gutter="60" v-if="Object.keys(responseData).length">
        <el-col :lg="24" :md="24" :sm="24" :xs="24">
          <el-form-item :label="t('mockOrder.responseParamsText')">
            <div class="card">{{ JSON.stringify(responseData) }}</div>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <div class="footer">
      <el-button type="primary" :loading="loading" @click="save">{{ t('common.submitText') }}</el-button>
    </div>
  </div>
</template>
<script setup>
  import { ref, reactive, onMounted } from 'vue';
  import { useI18n } from 'vue-i18n';
  import { generateSign } from '@/utils/public';
  import { mockPayOrderTestApi, getConfigAndPayPoolListByApi, getPayPoolAccountListApi } from '@/api/order';
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

    return 'test' + orderId;
  };

  const form = ref({
    payType: '1', // 支付类型
    mchNo: 'msLkxPAPLGYE', // 商户号
    mchUserNo: '788985', // 商户下的用户标识
    mchUserName: '派大星', // 商户下的用户名称
    thirdOrderNo: generateOrderNumber(),
    amount: '100.00', // 订单金额
    notifyUrl: 'http://www.baidu.com', // 服务端后台支付结果通知地址
    returnUrl: 'http://www.baidu.com',
    remark: 'test',
    labelCode: 'WIN_TAG',
    currency: 'inr',
    sign: '', // 签名
    cPayPoolId: null, // 支付池id
    cPayPoolAccountId: null // 支付账户id
  });

  const payTypeOptions = ref([
    { label: 'upi', value: '1' },
    { label: 'bank', value: '2' },
    { label: 'imps', value: '3' }
  ]);

  const rules = reactive({
    payType: [{ required: true, message: t('mockOrder.selectText'), trigger: 'change' }],
    mchNo: [{ required: true, message: t('mockOrder.enterText'), trigger: 'blur' }],
    mchUserNo: [{ required: true, message: t('mockOrder.enterText'), trigger: 'blur' }],
    mchUserName: [{ required: true, message: t('mockOrder.enterText'), trigger: 'blur' }],
    thirdOrderNo: [{ required: true, message: t('mockOrder.enterText'), trigger: 'blur' }],
    amount: [{ required: true, message: t('mockOrder.enterText'), trigger: 'blur' }],
    notifyUrl: [{ required: true, message: t('mockOrder.enterText'), trigger: 'blur' }],
    returnUrl: [{ required: true, message: t('mockOrder.enterText'), trigger: 'blur' }],
    remark: [{ required: true, message: t('mockOrder.enterText'), trigger: 'blur' }],
    labelCode: [{ required: true, message: t('mockOrder.enterText'), trigger: 'blur' }],
    currency: [{ required: true, message: t('mockOrder.enterText'), trigger: 'blur' }],
    cPayPoolId: [{ required: true, message: t('utrOrder.selectChannelAndPool'), trigger: 'change' }],
    cPayPoolAccountId: [{ required: true, message: t('utrOrder.selectBankCard'), trigger: 'change' }]
  });

  const responseData = ref({});

  const save = () => {
    formRef.value?.validate?.((valid) => {
      if (!valid) {
        return;
      }
      form.value.amount = formatToTwoDecimals(form.value.amount);
      delete form.value.cPayPoolId;
      form.value.sign = generateSign(form.value, 'demo123456');
      loading.value = true;
      mockPayOrderTestApi({
        ...form.value
      })
        .then((res) => {
          loading.value = false;
          form.value.thirdOrderNo = generateOrderNumber();
          responseData.value = res.data;
          ElMessage({
            message: res.msg,
            type: 'success',
            onClose: () => {
              window.open(res.data.payUrl, '_blank');
            }
          });
        })
        .catch((e) => {
          loading.value = false;
          ElMessage.error(e.message);
        });
    });
  };

  const payPoolListOptions = ref([]);

  // 获取支付池列表
  const getConfigAndPayPoolList = async (payType) => {
    form.value.cPayPoolId = null;
    form.value.cPayPoolAccountId = null;
    const res = await getConfigAndPayPoolListByApi({ payType });
    payPoolListOptions.value = res;
  };

  const bankCardOptions = ref([]);

  // 获取银行账户列表
  const getPayPoolAccountList = async (cPayPoolId) => {
    form.value.cPayPoolAccountId = null;
    const res = await getPayPoolAccountListApi({ cPayPoolId });
    bankCardOptions.value = res;
  };

  onMounted(() => {
    getConfigAndPayPoolList(form.value.payType);
  });
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
