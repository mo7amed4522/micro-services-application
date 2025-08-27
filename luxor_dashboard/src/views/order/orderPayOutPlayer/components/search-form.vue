<!-- 搜索表单 -->
<template>
  <el-form label-width="72px" @keyup.enter="search" @submit.prevent="">
    <el-row :gutter="8">
      <el-col :lg="5" :md="12" :sm="12" :xs="24">
        <el-form-item label="关键字">
          <el-input clearable v-model="form.keyword" placeholder="请输入" class="ele-fluid" />
        </el-form-item>
      </el-col>

      <el-col :lg="4" :md="12" :sm="12" :xs="24">
        <el-form-item label-width="16px">
          <el-button type="primary" @click="search">查询</el-button>
          <el-button @click="reset">重置</el-button>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>

<script setup>
  import { ref, onMounted } from 'vue';
  import { useFormData } from '@/utils/use-form-data';

  const props = defineProps({});
  const emit = defineEmits(['search']);

  /** 表单数据 */
  const [form, resetFields] = useFormData({
    keyword: ''
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
