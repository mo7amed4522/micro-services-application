<!-- 搜索表单 -->
<template>
  <el-form label-width="72px" @keyup.enter="search" @submit.prevent="">
    <el-row :gutter="8">
      <el-col :lg="5" :md="12" :sm="12" :xs="24">
        <el-form-item label="路径">
          <el-input clearable v-model="form.path" placeholder="请输入" class="ele-fluid" />
        </el-form-item>
      </el-col>
      <el-col :lg="5" :md="12" :sm="12" :xs="24">
        <el-form-item label="api分组">
          <el-select v-model="form.apiGroup">
            <el-option v-for="item in apiGroup" :key="item.key" :value="item.value" :label="item.value"></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :lg="4" :md="12" :sm="12" :xs="24">
        <el-form-item label="创建时间">
          <el-date-picker unlink-panels type="daterange" v-model="dateRange" range-separator="-" value-format="YYYY-MM-DD" start-placeholder="开始日期" end-placeholder="结束日期" class="ele-fluid" />
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
</template>

<script setup>
  import { ref, onMounted } from 'vue';
  import { useFormData } from '@/utils/use-form-data';
  import { useI18n } from 'vue-i18n';

  const { t } = useI18n();

  const props = defineProps({
    apiGroup: {
      type: Array,
      required: true
    }
  });
  const emit = defineEmits(['search']);

  /** 表单数据 */
  const [form, resetFields] = useFormData({
    title: ''
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
