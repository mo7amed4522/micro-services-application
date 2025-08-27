<!-- 搜索表单 -->
<template>
  <ele-card :body-style="{ paddingBottom: '2px' }">
    <el-form label-width="110px" @keyup.enter="search" @submit.prevent="">
      <el-row :gutter="8">
        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('common.orderTypeText')">
            <el-select clearable v-model="form.orderType" :empty-values="[null, undefined]" :placeholder="t('mockOrder.selectText')" class="ele-fluid">
              <el-option v-for="(item, index) in orderTypeOptions" :key="index" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('utrOrder.utrCode')">
            <el-input clearable v-model="form.utr" class="ele-fluid" :placeholder="t('utrOrder.enterUtrCode')" />
          </el-form-item>
        </el-col>

        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('payMethod.bankName')" prop="bankName">
            <el-select v-model="form.bankName" clearable filterable :empty-values="[null, undefined]" :placeholder="t('payMethod.selectBank')">
              <el-option v-for="item in props.receiptConfitList" :key="item.id" :value="item.configValue" :label="item.configValue" />
            </el-select>
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

  const emit = defineEmits(['search']);

  const props = defineProps({
    receiptConfitList: {
      type: Array,
      default: () => []
    }
  });

  /** 表单数据 */
  const [form, resetFields] = useFormData({
    status: '',
    utr: '',
    status: '',
    bankName: null,
    orderType: null
  });

  const orderTypeOptions = computed(() => [
    { label: t('common.all'), value: '' },
    { label: t('common.depositText'), value: 1 },
    { label: t('channelConfig.payOutText'), value: 2 }
  ]);

  const statusOptions = computed(() => [
    { label: t('common.all'), value: '' },
    { label: t('common.handleFailText'), value: -1 },
    { label: t('common.todoText'), value: 1 },
    { label: t('common.handleSuccessText'), value: 2 }
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
