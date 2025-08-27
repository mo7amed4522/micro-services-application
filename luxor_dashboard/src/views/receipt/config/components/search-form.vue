<!-- 搜索表单 -->
<template>
  <ele-card :body-style="{ paddingBottom: '2px' }">
    <el-form label-width="110px" @keyup.enter="search" @submit.prevent="">
      <el-row :gutter="8">
        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('common.configKey')" prop="configKey">
            <el-input v-model="form.configKey" :placeholder="t('common.enterConfigKey')" clearable />
          </el-form-item>
        </el-col>

        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('common.configValue')" prop="configValue">
            <el-input v-model="form.configValue" :placeholder="t('common.enterConfigValue')" clearable />
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

  /** 表单数据 */
  const [form, resetFields] = useFormData({
    configValue: '',
    configKey: ''
  });

  const statusOptions = ref([
    { label: t('common.all'), value: '' },
    { label: t('common.online'), value: 1 },
    { label: t('common.offline'), value: -1 }
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
