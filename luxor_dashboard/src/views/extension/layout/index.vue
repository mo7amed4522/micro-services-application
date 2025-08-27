<template>
  <ele-page>
    <demo-basic />
    <demo-side />
    <demo-top />
  </ele-page>
</template>

<script setup>
  import DemoBasic from './components/demo-basic.vue';
  import DemoSide from './components/demo-side.vue';
  import DemoTop from './components/demo-top.vue';

  defineOptions({ name: 'ExtensionLayout' });
</script>
