<template>
  <ele-page>
    <demo-modal />
    <demo-multiple />
    <demo-drawer />
    <demo-message-box />
  </ele-page>
</template>

<script setup>
  import DemoModal from './components/demo-modal.vue';
  import DemoMultiple from './components/demo-multiple.vue';
  import DemoDrawer from './components/demo-drawer.vue';
  import DemoMessageBox from './components/demo-message-box.vue';
</script>

<script>
  export default {
    name: 'ExtensionDialog'
  };
</script>
