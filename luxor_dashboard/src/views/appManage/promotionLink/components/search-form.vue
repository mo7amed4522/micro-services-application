<!-- 搜索表单 -->
<template>
  <ele-card :body-style="{ paddingBottom: '2px' }">
    <el-form label-width="100px" @keyup.enter="search" @submit.prevent="">
      <el-row :gutter="8">
        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item label="所属应用">
            <el-select clearable v-model="form.appId" :empty-values="[null, undefined]" placeholder="请选择" class="ele-fluid">
              <el-option v-for="item in appList" :key="item.id" :label="item.appName" :value="item.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item label="链接名称">
            <el-input clearable v-model="form.name" placeholder="请输入" class="ele-fluid" />
          </el-form-item>
        </el-col>
        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item label="所属域名">
            <el-select clearable v-model="form.domainId" :empty-values="[null, undefined]" placeholder="请选择" class="ele-fluid">
              <el-option v-for="item in domainList" :key="item.id" :label="item.domain" :value="item.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item label="所属模版">
            <el-select clearable v-model="form.templateId" :empty-values="[null, undefined]" placeholder="请选择" class="ele-fluid">
              <el-option v-for="item in templateList" :key="item.id" :label="item.name" :value="item.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item label="是否允许修改url" label-width="120px">
            <el-select clearable v-model="form.isAllowEditUrl" :empty-values="[null, undefined]" placeholder="请选择" class="ele-fluid">
              <el-option v-for="item in isAllowEditUrlOptions" :key="item.value" :label="item.label" :value="item.value" />
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

  const props = defineProps({
    appList: {
      type: Array,
      default: () => []
    },
    domainList: {
      type: Array,
      default: () => []
    },
    templateList: {
      type: Array,
      default: () => []
    }
  });
  const emit = defineEmits(['search']);

  /** 表单数据 */
  const [form, resetFields] = useFormData({
    name: '',
    appId: null,
    domainId: null,
    templateId: null,
    isAllowEditUrl: null, // 是否允许修改url 1 允许 -1 不允许 
  });

  const isAllowEditUrlOptions = computed(() => [
    { label: '允许', value: 1 },
    { label: '不允许', value: -1 }
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
