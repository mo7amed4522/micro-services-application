<!-- 搜索表单 -->
<template>
  <ele-card :body-style="{ paddingBottom: '2px' }">
    <el-form label-width="100px" @keyup.enter="search" @submit.prevent="">
      <el-row :gutter="8">
        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('common.searchOrderNumber')">
            <el-input clearable v-model="form.keyword" class="ele-fluid" :placeholder="t('common.pleaseEnterOrderNumber')" />
          </el-form-item>
        </el-col>
        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('common.paymentAccountText')">
            <el-input clearable v-model="form.cPayPoolAccountName" class="ele-fluid" :placeholder="t('common.pleaseEnterPaymentAccount')" />
          </el-form-item>
        </el-col>
        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('common.statusText')">
            <el-select clearable v-model="form.status" :empty-values="[null, undefined]" :placeholder="t('common.selectStatusText')" class="ele-fluid">
              <el-option v-for="(item, index) in statusOptions" :key="index" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('utrOrder.operationType')">
            <el-select clearable v-model="form.oprType" :empty-values="[null, undefined]" :placeholder="t('utrOrder.operationType')" class="ele-fluid">
              <el-option v-for="(item, index) in oprTypeOptions" :key="index" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('operatorOrderReplenish.operatorText')">
            <el-select clearable v-model="form.operateId" :empty-values="[null, undefined]" :placeholder="t('operatorOrderReplenish.selectOperator')" class="ele-fluid">
              <el-option v-for="(item, index) in props.userList" :key="index" :label="item.userName" :value="item.ID" />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('payMethod.cardNumber')">
            <el-input clearable v-model="form.cardNumber" class="ele-fluid" :placeholder="t('payMethod.enterCardNumber')" />
          </el-form-item>
        </el-col>

        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('utrOrder.utrCode')">
            <el-input clearable v-model="form.utr" class="ele-fluid" :placeholder="t('utrOrder.enterUtrCode')" />
          </el-form-item>
        </el-col>

        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('utrOrder.paySource')">
            <el-select clearable v-model="form.sourceType" :empty-values="[null, undefined]" :placeholder="t('utrOrder.selectPaySource')" class="ele-fluid">
              <el-option v-for="(item, index) in sourceTypeList" :key="index" :label="item.label" :value="item.value" />
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
    cardNumber: '',
    utr: '',
    startAmount: '',
    endAmount: '',
    sourceType: '',
    cPayPoolAccountName: '',
    oprType: ''
  });

  const statusOptions = computed(() => [
    { label: t('common.all'), value: '' },
    { label: t('common.processing'), value: 1 },
    { label: t('common.successText'), value: 2 },
    { label: t('common.timeoutText'), value: 3 },
    { label: t('common.fail'), value: -1 },
    { label: t('common.closed'), value: -2 }
  ]);

  const oprTypeOptions = computed(() => [
    { label: t('common.all'), value: '' },
    { label: t('common.autoReceipt'), value: 1 },
    { label: t('common.manualReceipt'), value: 2 }
  ]);

  const sourceTypeList = computed(() => [
    { label: t('common.all'), value: '' },
    { label: 'upi-QR', value: 1 },
    { label: 'upi-bank', value: 2 },
    { label: 'bank-bank', value: 3 }
  ]);

  /** 日期范围 */
  const dateRange = ref(['', '']);

  /** 搜索 */
  const search = () => {
    const [d1, d2] = dateRange.value ?? [];
    emit('search', {
      ...form,
      startTime: d1 ? `${d1}` : '',
      endTime: d2 ? `${d2}` : ''
    });
  };

  /**  重置 */
  const reset = () => {
    resetFields();
    dateRange.value = ['', ''];
    search();
  };

  onMounted(() => {});
</script>
