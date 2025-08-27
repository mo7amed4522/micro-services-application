<!-- 搜索表单 -->
<template>
  <ele-card :body-style="{ paddingBottom: '2px' }">
    <el-form label-width="100px" @keyup.enter="search" @submit.prevent="">
      <el-row :gutter="8">
        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('operatorOrderReplenish.operatorText')">
            <el-select clearable v-model="form.operateID" :empty-values="[null, undefined]" :placeholder="t('operatorOrderReplenish.selectOperator')" class="ele-fluid">
              <el-option v-for="(item, index) in props.userList" :key="index" :label="item.userName" :value="item.ID" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :lg="4" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('common.quickFilterText')">
            <el-select clearable v-model="form.classes" :empty-values="[null, undefined]" :placeholder="t('mockOrder.selectText')" class="ele-fluid">
              <el-option v-for="(item, index) in timeOptions" :key="index" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :lg="6" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('common.createTimeText')">
            <el-date-picker
              unlink-panels
              type="daterange"
              v-model="dateRange"
              range-separator="-"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"
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
  import { ref, onMounted, computed } from 'vue';
  import { useFormData } from '@/utils/use-form-data';
  import { useI18n } from 'vue-i18n';

  const { t } = useI18n();

  const props = defineProps({
    userList: {
      type: Array,
      default: () => []
    }
  });
  const emit = defineEmits(['search']);

  /** 表单数据 */
  const [form, resetFields] = useFormData({
    operateID: null,
    classes: null
  });

  const timeOptions = computed(() => [
    { label: t('common.whiteShift'), value: 1 },
    { label: t('common.nightShift'), value: 2 }
  ]);

  /** 日期范围 */
  const dateRange = ref(['', '']);

  /** 搜索 */
  const search = () => {
    const [d1, d2] = dateRange.value ?? [];
    emit('search', {
      ...form,
      startTime: d1 ? `${d1} 00:00` : '',
      endTime: d2 ? `${d2} 23:59` : ''
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
