<template>
  <ele-page>
    <div class="title">{{ t('common.paymentSystemText') }}</div>
  </ele-page>
</template>

<script setup></script>

<script setup>
  import { useI18n } from 'vue-i18n';

  const { t } = useI18n();
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
