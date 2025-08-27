<!-- 路由出口 -->
<template>
  <router-view v-slot="{ route, Component }">
    <transition :name="transitionName" mode="out-in" appear>
      <div>
        <!-- 包裹一个 div 来保持单一根节点 -->
        <keep-alive v-if="TAB_KEEP_ALIVE && tabBar" :include="keepAliveInclude" :max="10">
          <component :key="route.path" :is="Component" />
        </keep-alive>
        <component v-else :key="route.path" :is="Component" />
      </div>
    </transition>
  </router-view>
</template>

<script setup>
  import { storeToRefs } from 'pinia';
  import { useThemeStore } from '@/store/modules/theme';
  import { TAB_KEEP_ALIVE } from '@/config/setting';

  const themeStore = useThemeStore();
  const { keepAliveInclude, transitionName, tabBar } = storeToRefs(themeStore);
</script>

<script>
  export default {
    name: 'RouterLayout'
  };
</script>
