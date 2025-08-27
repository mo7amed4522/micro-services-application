<!-- 搜索表单 -->
<template>
  <ele-card :body-style="{ paddingBottom: '2px' }">
    <el-form label-width="100px" @keyup.enter="search" @submit.prevent="">
      <el-row :gutter="8">
        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('common.searchOrderNumber')">
            <el-input clearable v-model="form.keyword" class="ele-fluid" :placeholder="t('common.searchOrderNumberText')" />
          </el-form-item>
        </el-col>
        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('utrOrder.utrCode')">
            <el-input clearable v-model="form.utrNo" class="ele-fluid" :placeholder="t('utrOrder.enterUtrCode')" />
          </el-form-item>
        </el-col>
        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('orderList.orderStatus')">
            <el-select clearable v-model="form.status" :empty-values="[null, undefined]" :placeholder="t('orderList.selectOrderStatus')" class="ele-fluid">
              <el-option v-for="(item, index) in statusOptions" :key="index" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('utrOrder.paySource')">
            <el-select clearable v-model="form.payType" :empty-values="[null, undefined]" :placeholder="t('utrOrder.selectPaySource')" class="ele-fluid">
              <el-option v-for="(item, index) in payTypeOptions" :key="index" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('merchant.merchantNum')">
            <el-input clearable v-model="form.mUserNo" class="ele-fluid" :placeholder="t('mockOrder.enterMerchantNumText')" />
          </el-form-item>
        </el-col>

        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('orderList.merchantName')">
            <el-input clearable v-model="form.mUserName" class="ele-fluid" :placeholder="t('common.enterMerchantName')" />
          </el-form-item>
        </el-col>

        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('orderList.payMethodName')">
            <el-input clearable v-model="form.cPayPoolAccountName" class="ele-fluid" :placeholder="t('common.pleaseEnterPaymentMethod')" />
          </el-form-item>
        </el-col>

        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('orderList.poolName')">
            <el-input clearable v-model="form.cPayPoolName" class="ele-fluid" :placeholder="t('payPool.enterPoolName')" />
          </el-form-item>
        </el-col>

        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('common.cardNumber')">
            <el-input clearable v-model="form.cardNumber" class="ele-fluid" :placeholder="t('common.enterCardNumber')" />
          </el-form-item>
        </el-col>

        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('operatorOrderReplenish.operatorText')">
            <el-select clearable v-model="form.operateId" :empty-values="[null, undefined]" :placeholder="t('operatorOrderReplenish.selectOperator')" class="ele-fluid">
              <el-option v-for="(item, index) in props.userList" :key="index" :label="item.userName" :value="item.ID" />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :lg="6" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('common.amountRange')">
            <div style="display: flex; align-items: center">
              <el-input clearable v-model="form.startAmount" class="ele-fluid" :placeholder="t('mockOrder.enterText')" />
              <span style="margin: 0 4px">-</span>
              <el-input clearable v-model="form.endAmount" class="ele-fluid" :placeholder="t('mockOrder.enterText')" />
            </div>
          </el-form-item>
        </el-col>

        <el-col :lg="6" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('common.paymentTime')">
            <el-date-picker
              unlink-panels
              type="datetimerange"
              v-model="payTimeDateRange"
              range-separator="-"
              format="YYYY-MM-DD HH:mm"
              value-format="YYYY-MM-DD HH:mm"
              :start-placeholder="t('common.beginTimeText')"
              :end-placeholder="t('common.endTimeText')"
              class="ele-fluid"
            />
          </el-form-item>
        </el-col>

        <el-col :lg="6" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('common.createTimeText')">
            <el-date-picker
              unlink-panels
              type="datetimerange"
              v-model="dateRange"
              range-separator="-"
              format="YYYY-MM-DD HH:mm"
              value-format="YYYY-MM-DD HH:mm"
              :start-placeholder="t('common.beginTimeText')"
              :end-placeholder="t('common.endTimeText')"
              class="ele-fluid"
            />
          </el-form-item>
        </el-col>
        <el-col :lg="5" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('utrOrder.utrCode')">
            <el-checkbox-group v-model="utrStatusOptions">
              <el-checkbox-button key="1" :value="1">{{ t('common.utrFilter') }}</el-checkbox-button>
            </el-checkbox-group>
          </el-form-item>
        </el-col>

        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item label-width="16px">
            <el-button type="primary" @click="search">{{ t('common.search') }}</el-button>
            <el-button @click="reset">{{ t('common.reset') }}</el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </ele-card>
</template>

<script setup>
  import { ref, onMounted, computed } from 'vue';
  import { useFormData } from '@/utils/use-form-data';
  import { useI18n } from 'vue-i18n';

  const { t } = useI18n();

  const props = defineProps({
    userList: {
      type: Array,
      default: () => []
    }
  });

  const emit = defineEmits(['search']);

  /** 表单数据 */
  const [form, resetFields] = useFormData({
    keyword: '',
    status: '',
    utrNo: '',
    payType: '',
    mUserNo: '',
    mUserName: '',
    startAmount: '',
    endAmount: '',
    cPayPoolAccountName: '',
    cPayPoolName: '',
    operateId: null,
    utrStatus: '',
    cardNumber: '',
  });

  const utrStatusOptions = ref([]);

  const statusOptions = computed(() => [
    { label: t('common.all'), value: '' },
    { label: t('common.inPaymentText'), value: 1 },
    { label: t('common.paySuccessText'), value: 2 },
    { label: t('common.failText'), value: -1 }
  ]);

  const payTypeOptions = computed(() => [
    { label: t('common.all'), value: '' },
    { label: 'upi', value: 1 },
    { label: 'bank', value: 2 },
    { label: 'imps', value: 3 }
  ]);

  /** 日期范围 */
  const dateRange = ref(['', '']);

  const payTimeDateRange = ref(['', '']);

  /** 搜索 */
  const search = () => {
    const [d1, d2] = dateRange.value ?? [];
    const [d3, d4] = payTimeDateRange.value ?? [];
    emit('search', {
      ...form,
      utrStatus: utrStatusOptions.value.length > 0 ? 1 : '',
      startTime: d1 ? `${d1}` : '',
      endTime: d2 ? `${d2}` : '',
      startPayTime: d3 ? `${d3}` : '',
      endPayTime: d4 ? `${d4}` : ''
    });
  };

  /**  重置 */
  const reset = () => {
    resetFields();
    dateRange.value = ['', ''];
    payTimeDateRange.value = ['', ''];
    search();
  };

  onMounted(() => {});
</script>
