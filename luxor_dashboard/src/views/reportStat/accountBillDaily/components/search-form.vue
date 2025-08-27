<!-- 搜索表单 -->
<template>
  <ele-card :body-style="{ paddingBottom: '2px' }">
    <el-form label-width="110px" @keyup.enter="search" @submit.prevent="">
      <el-row :gutter="8">
        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('merchantChannel.channelName')">
            <el-select clearable v-model="form.cPayConfigId" :empty-values="[null, undefined]" :placeholder="t('merchant.selectChannel')" class="ele-fluid">
              <el-option v-for="(item, index) in props.channelList" :key="index" :label="item.name" :value="item.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('orderList.poolName')">
            <el-input clearable v-model="form.poolName" class="ele-fluid" :placeholder="t('payPool.enterPoolName')" />
          </el-form-item>
        </el-col>
        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('common.accountStatusText')">
            <el-select clearable v-model="form.status" :empty-values="[null, undefined]" :placeholder="t('common.selectEnableStatusText')" class="ele-fluid">
              <el-option v-for="(item, index) in statusOptions" :key="index" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('common.quickFilterText')">
            <el-select clearable v-model="form.timeType" :empty-values="[null, undefined]" :placeholder="t('common.selectTime')" class="ele-fluid" @change="resetDateRange">
              <el-option v-for="(item, index) in timeTypeOptions" :key="index" :label="item.label" :value="item.value" />
            </el-select>
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
              @change="form.timeType = ''"
            />
          </el-form-item>
        </el-col>

        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item label-width="16px">
            <el-button type="primary" @click="search">{{ t('common.selectText') }}</el-button>
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
    channelList: {
      type: Array,
      default: () => []
    }
  });
  const emit = defineEmits(['search']);

  /** 表单数据 */
  const [form, resetFields] = useFormData({
    status: '',
    cPayConfigId: null,
    poolName: '',
    timeType: 3
  });

  const statusOptions = computed(() => [
    { label: t('common.all'), value: '' },
    { label: t('common.enable'), value: 1 },
    { label: t('merchant.disabled'), value: 2 }
  ]);

  const timeTypeOptions = computed(() => [
    { label: t('common.all'), value: '' },
    { label: t('common.halfAnHour'), value: 1 },
    { label: t('common.oneHour'), value: 2 },
    { label: t('common.today'), value: 3 }
  ]);

  // 生成个数组0-23
  const hoursList = Array.from({ length: 24 }, (_, i) => i);

  const resetDateRange = () => {
    dateRange.value = ['', ''];
  }

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
