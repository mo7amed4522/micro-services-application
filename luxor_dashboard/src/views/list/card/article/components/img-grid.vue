<template>
  <div class="demo-img-grid">
    <el-image
      v-for="(item, index) in images"
      :key="item"
      fit="cover"
      :src="item"
      style="width: 100%; height: 100%; border-radius: 4px; cursor: zoom-in"
      @click="openPreview(index)"
    />
    <div style="display: none">
      <el-image
        ref="previewRef"
        :src="images[0]"
        :preview-src-list="images"
        :initial-index="previewIndex"
        :hide-on-click-modal="true"
        :preview-teleported="true"
      />
    </div>
  </div>
</template>

<script setup>
  import { ref } from 'vue';

  defineProps({
    images: Array
  });

  const previewRef = ref(null);

  const previewIndex = ref(0);

  const openPreview = (index) => {
    previewIndex.value = index;
    previewRef.value?.$el?.querySelector?.('img')?.click?.();
  };
</script>

<style lang="scss" scoped>
  .demo-img-grid {
    display: grid;
    grid-gap: 6px 6px;
    grid-template-columns: repeat(3, 1fr);
    height: 100%;
    padding: 8px;
    box-sizing: border-box;
    border-radius: 6px;
    background: #eee;
    background: var(--el-fill-color-light);
  }
</style>
