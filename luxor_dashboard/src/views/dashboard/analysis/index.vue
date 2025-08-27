<template>
  <ele-page>
    <div class="title">{{ t('common.paymentSystemText') }}</div>

    <!-- <statistics-card />
    <sale-card />
    <el-row :gutter="16">
      <el-col :md="16" :sm="14" :xs="24">
        <visit-hour />
      </el-col>
      <el-col :md="8" :sm="10" :xs="24">
        <hot-search />
      </el-col>
    </el-row> -->
  </ele-page>
</template>

<script setup>
  import StatisticsCard from './components/statistics-card.vue';
  import { useI18n } from 'vue-i18n';

  const { t } = useI18n();
  // import SaleCard from './components/sale-card.vue';
  // import VisitHour from './components/visit-hour.vue';
  // import HotSearch from './components/hot-search.vue';
</script>

<script>
  export default {
    name: 'DashboardAnalysis'
  };
</script>
<style lang="scss" scoped>
  .title {
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 24px;
    font-weight: bold;
    margin-top: 100px;
    margin-bottom: 100px;
  }
</style>
