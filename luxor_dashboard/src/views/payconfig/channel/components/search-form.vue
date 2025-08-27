<!-- 搜索表单 -->
<template>
  <ele-card :body-style="{ paddingBottom: '2px' }">
    <el-form label-width="110px" @keyup.enter="search" @submit.prevent="">
      <el-row :gutter="8">
        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('merchantChannel.channelName')">
            <el-input clearable v-model="form.cPayConfigName" class="ele-fluid" :placeholder="t('merchantChannel.enterChannelName')" />
          </el-form-item>
        </el-col>
        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('channelConfig.thirdCode')">
            <el-input clearable v-model="form.cPayConfigCode" class="ele-fluid" :placeholder="t('channelConfig.enterThirdCode')" />
          </el-form-item>
        </el-col>

        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('channelConfig.sourceType')">
            <el-select clearable v-model="form.channelType" :empty-values="[null, undefined]" :placeholder="t('channelConfig.selectSourceType')" class="ele-fluid">
              <el-option v-for="(item, index) in channelTypeOptions" :key="index" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('channelConfig.channelType')">
            <el-select clearable v-model="form.type" :empty-values="[null, undefined]" :placeholder="t('channelConfig.selectChannelType')" class="ele-fluid">
              <el-option v-for="(item, index) in typeOptions" :key="index" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('channelConfig.channelPayType')">
            <el-select clearable v-model="form.payType" :empty-values="[null, undefined]" :placeholder="t('channelConfig.selectPayType')" class="ele-fluid">
              <el-option v-for="(item, index) in payTypeOptions" :key="index" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('common.enableStatusText')">
            <el-select clearable v-model="form.status" :empty-values="[null, undefined]" :placeholder="t('common.selectEnableStatusText')" class="ele-fluid">
              <el-option v-for="(item, index) in statusOptions" :key="index" :label="item.label" :value="item.value" />
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

  const props = defineProps({});
  const emit = defineEmits(['search']);

  /** 表单数据 */
  const [form, resetFields] = useFormData({
    status: '',
    cPayConfigName: '',
    cPayConfigCode: '',
    channelType: '',
    type: '',
    payType: '',
  });

  const statusOptions = ref([
    { label: t('common.all'), value: '' },
    { label: t('common.enable'), value: 1 },
    { label: t('merchant.disabled'), value: 2 }
  ]);

  const channelTypeOptions = ref([
    { label: t('common.all'), value: '' },
    { label: t('common.selfDevelop'), value: 1 },
    { label: t('common.thirdParty'), value: 2 }
  ])

  const typeOptions = ref([
    { label: t('common.all'), value: '' },
    { label: t('channelConfig.payText'), value: 1 },
    { label: t('channelConfig.payOutText'), value: 2 }
  ])

  const payTypeOptions = ref([
    { label: t('common.all'), value: '' },
    { label: t('payMethod.qrCode'), value: 1 },
    { label: t('payMethod.bankCard'), value: 2 }
  ])

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
