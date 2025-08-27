<!-- 搜索表单 -->
<template>
  <ele-card :body-style="{ paddingBottom: '2px' }">
    <el-form label-width="110px" @keyup.enter="search" @submit.prevent="">
      <el-row :gutter="8">
        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('common.deviceId')">
            <el-input clearable v-model="form.deviceId" class="ele-fluid" :placeholder="t('common.enterDeviceId')" />
          </el-form-item>
        </el-col>

        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('common.loginUserId')">
            <el-input clearable v-model="form.loginUserId" class="ele-fluid" :placeholder="t('common.enterLoginUserId')" />
          </el-form-item>
        </el-col>

        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('common.loginUserName')">
            <el-input clearable v-model="form.loginUserName" class="ele-fluid" :placeholder="t('common.enterLoginUserName')" />
          </el-form-item>
        </el-col>

        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('payMethod.bankName')">
            <el-input clearable v-model="form.bankName" class="ele-fluid" :placeholder="t('payMethod.enterBankName')" />
          </el-form-item>
        </el-col>

        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('common.statusText')">
            <el-select clearable v-model="form.status" :empty-values="[null, undefined]" :placeholder="t('common.selectEnableStatusText')" class="ele-fluid">
              <el-option v-for="(item, index) in statusOptions" :key="index" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
        </el-col>
        <!-- <el-col :lg="6" :md="12" :sm="12" :xs="24">
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
        </el-col> -->

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

  /** 表单数据 */
  const [form, resetFields] = useFormData({
    status: '',
    deviceId: '',
    bankName: '',
    loginUserName: '',
    loginUserId: '',
  });

  const statusOptions = computed(() => [
    { label: t('common.all'), value: '' },
    { label: t('common.manualOnline'), value: 1 },
    { label: t('common.manualOffline'), value: -1 },
    { label: t('common.waitOffline'), value: 2 },
    { label: t('common.manualMaintain'), value: 3 },
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
