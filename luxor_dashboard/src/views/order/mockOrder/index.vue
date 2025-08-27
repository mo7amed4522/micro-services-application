<template>
  <ele-page>
    <ele-card :body-style="{ paddingBottom: '2px' }">
      <ele-tabs ref="tabRef" :items="items" v-model="active" :mousewheel="true" :class="{ 'demo-card-button': type === 'button' }">
        <template #paymentOrder>
          <PaymentOrder ref="paymentOrder" />
        </template>
        <template #payoutOrder>
          <keep-alive>
            <PayoutOrder v-if="active === 'payoutOrder'" />
          </keep-alive>
        </template>
        <template #paymentOrderOnline>
          <keep-alive>
            <PaymentOrderOnline v-if="active === 'paymentOrderOnline'" />
          </keep-alive>
        </template>
      </ele-tabs>
    </ele-card>
  </ele-page>
</template>
<script setup>
  import { ref } from 'vue';
  import PaymentOrder from './components/paymentOrder/index.vue';
  import PayoutOrder from './components/payoutOrder/index.vue';
  import PaymentOrderOnline from './components/paymentOrderOnline/index.vue'
  import { useI18n } from 'vue-i18n';

  const { t } = useI18n();

  // 风格
  const type = ref('default');

  const active = ref('paymentOrder');

  const items = ref([
    {
      name: 'paymentOrder',
      label: t('common.payOrderText')
    },
    {
      name: 'payoutOrder',
      label: t('common.payoutOrderText')
    },
    {
      name: 'paymentOrderOnline',
      label: t('common.paymentOrderOpsText')
    }
  ]);
</script>
<style lang="scss" scoped></style>
