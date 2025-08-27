<!-- 搜索表单 -->
<template>
  <el-form label-width="100px" @keyup.enter="search" @submit.prevent="">
    <el-row :gutter="8">
      <el-col :lg="5" :md="12" :sm="12" :xs="24">
        <el-form-item :label="t('adminUser.userName')">
          <el-input clearable v-model="form.username" :placeholder="t('adminUser.enterUserName')" class="ele-fluid" />
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
</template>

<script setup>
  import { ref, onMounted } from 'vue';
  import { useFormData } from '@/utils/use-form-data';
  import { useI18n } from 'vue-i18n';

  const { t } = useI18n();

  const emit = defineEmits(['search']);

  /** 表单数据 */
  const [form, resetFields] = useFormData({
    username: ''
  });

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
