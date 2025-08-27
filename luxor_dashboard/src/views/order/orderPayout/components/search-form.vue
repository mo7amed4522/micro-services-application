<!-- 搜索表单 -->
<template>
  <ele-card :body-style="{ paddingBottom: '2px' }">
    <el-form label-width="110px" @keyup.enter="search" @submit.prevent="">
      <el-row :gutter="8">
        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('common.searchOrderNumber')">
            <el-input clearable v-model="form.keyword" class="ele-fluid" :placeholder="t('common.searchOrderNumberText')" />
          </el-form-item>
        </el-col>
        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('common.failReasonText')">
            <el-select clearable v-model="form.failMessageId" :empty-values="[null, undefined]" :placeholder="t('common.selectFailReasonText')" class="ele-fluid">
              <el-option v-for="(item, index) in failMessageList" :key="index" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('merchant.merchantNum')">
            <el-input clearable v-model="form.mUserNo" class="ele-fluid" :placeholder="t('mockOrder.enterMerchantNumText')" />
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
          <el-form-item :label="t('common.payoutMethod')">
            <el-input clearable v-model="form.cPayPoolAccountName" class="ele-fluid" :placeholder="t('common.enterPayoutMethod')" />
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
          <el-form-item :label="t('common.balanceRange')">
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
  import { ref, onMounted } from 'vue';
  import { useFormData } from '@/utils/use-form-data';
  import { useI18n } from 'vue-i18n';

  const { t } = useI18n();

  const emit = defineEmits(['search']);

  const props = defineProps({
    userList: {
      type: Array,
      default: () => []
    }
  });

  /** 表单数据 */
  const [form, resetFields] = useFormData({
    keyword: '',
    status: 1,
    failMessageId: null,
    mUserNo: '',
    startAmount: '',
    endAmount: '',
    cPayPoolAccountName: '',
    operateId: null
  });

  const failMessageList = ref([
    { label: t('common.paymentInfoError'), enLabel: 'Payment information error', value: 1 },
    { label: t('common.multipleRebound'), enLabel: 'Multiple rejections', value: 2 },
    { label: t('common.transferFail'), enLabel: 'Transfer failure', value: 3 },
    { label: t('common.merchantClose'), enLabel: 'Merchant requests closure', value: 4 }
  ]);

  const statusOptions = ref([
    { label: t('common.all'), value: '' },
    { label: t('outOrder.payOutingText'), value: 1 },
    { label: t('outOrder.payOutSuccessText'), value: 2 },
    { label: t('outOrder.payOutFailText'), value: -1 }
  ]);

  /** 日期范围 */
  const dateRange = ref(['', '']);

  const payTimeDateRange = ref(['', '']);

  /** 搜索 */
  /** 搜索 */
  const search = () => {
    const [d1, d2] = dateRange.value ?? [];
    const [d3, d4] = payTimeDateRange.value ?? [];
    emit('search', {
      ...form,
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
