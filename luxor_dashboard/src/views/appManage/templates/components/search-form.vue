<!-- 搜索表单 -->
<template>
  <ele-card :body-style="{ paddingBottom: '2px' }">
    <el-form label-width="100px" @keyup.enter="search" @submit.prevent="">
      <el-row :gutter="8">
        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item label="ID">
            <el-input clearable v-model="form.id" placeholder="请输入" class="ele-fluid" />
          </el-form-item>
        </el-col>
        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item label="模版名称">
            <el-input clearable v-model="form.name" placeholder="请输入" class="ele-fluid" />
          </el-form-item>
        </el-col>
        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item label="模版编码">
            <el-input clearable v-model="form.code" placeholder="请输入" class="ele-fluid" />
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

  const props = defineProps({});
  const emit = defineEmits(['search']);

  /** 表单数据 */
  const [form, resetFields] = useFormData({
    name: '',
    code: ''
  });

  const statusOptions = computed(() => [
    { label: '全部', value: '' },
    { label: '匹配中', value: 1 },
    { label: '正确匹配', value: 2 },
    { label: '匹配失败', value: 3 }
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
