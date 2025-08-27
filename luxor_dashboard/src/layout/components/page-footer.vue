<!-- 全局页脚 -->
<template>
  <ele-text
    type="placeholder"
    class="ele-footer"
    style="padding: 16px 0; text-align: center"
  >
    <el-space
      :size="24"
      style="--el-text-color-regular: var(--el-text-color-placeholder)"
    >
      <el-link :underline="false" href="#" target="_blank">
        xx
      </el-link>
      <el-link
        :underline="false"
        href="#"
        target="_blank"
      >
        xx
      </el-link>
      <el-link
        :underline="false"
        href="#"
        target="_blank"
      >
        xx
      </el-link>
    </el-space>
    <div style="margin-top: 8px">
      Copyright © 2024 xxx
    </div>
  </ele-text>
</template>

<script setup>
  import { useI18n } from 'vue-i18n';

  const { t } = useI18n();
</script>
