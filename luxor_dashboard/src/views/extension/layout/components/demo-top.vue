<template>
  <ele-card header="用于前台网站" :body-style="{ padding: '12px' }">
    <frame-card>
      <div
        :style="{
          height: '520px',
          overflow: 'auto',
          background: 'var(--ele-layout-bg)'
        }"
      >
        <ele-admin-layout
          layout="top"
          :header-menus="headerMenus"
          :fixed-header="true"
          :header-custom-style="{
            background: 'rgba(255, 255, 255, 0.1)',
            backdropFilter: 'blur(12px)',
            boxShadow: 'none',
            borderBottom: '1px solid rgba(0, 0, 0, 0.04)',
            paddingRight: '12px'
          }"
          :header-menu-props="{
            style: {
              marginLeft: '20px',
              '--el-color-primary-light-9': 'rgba(22, 119, 255, 0.1)'
            }
          }"
          :menu-scroll-to-active="false"
          :style="{ background: 'none' }"
        >
          <template #logo>
            <img src="@/assets/logo.svg" />
          </template>
          <template #logoTitle>
            <h1 class="hidden-xs-only">Ele Admin</h1>
          </template>
          <template #right>
            <layout-tool>登录</layout-tool>
            <layout-tool class="hidden-xs-only">注册</layout-tool>
          </template>
          <div :style="{ flex: 'auto' }">
            <ElCarousel height="320px" arrow="never" style="margin-top: -55px">
              <ElCarouselItem v-for="item in banner" :key="item">
                <img
                  :src="item"
                  :style="{
                    width: '100%',
                    height: '100%',
                    objectFit: 'cover',
                    display: 'block',
                    pointerEvents: 'none'
                  }"
                  class="demo-banner-img"
                />
              </ElCarouselItem>
            </ElCarousel>
            <div
              :style="{
                maxWidth: '820px',
                margin: '0 auto',
                padding: '0 12px 22px 12px',
                overflow: 'hidden'
              }"
            >
              <div style="text-align: center; padding: 28px 0 20px 0">
                <ele-text size="lg" type="heading">标题标题标题</ele-text>
                <ele-text size="sm" type="secondary">描述描述描述</ele-text>
              </div>
              <el-row :gutter="16">
                <el-col
                  v-for="index in 8"
                  :key="index"
                  :md="6"
                  :sm="8"
                  :xs="12"
                >
                  <ele-card style="border-radius: 0; min-height: 140px" />
                </el-col>
              </el-row>
            </div>
            <div
              :style="{
                textAlign: 'center',
                padding: '26px 0',
                backgroundImage:
                  'url(https://cdn2.weimob.com/saas/@assets/saas-fe-website-web-stc/_next/static/media/ZhuceBg.d0ce1f1c.png)',
                backgroundSize: '100% 100%'
              }"
            >
              <div style="color: #fff; font-size: 16px">
                文本文本文本文本文本文本文本文本
              </div>
              <div
                :style="{
                  width: '88px',
                  margin: '0 auto',
                  padding: '6px 0',
                  borderRadius: '4px',
                  color: 'var(--el-color-primary)',
                  marginTop: '12px',
                  background: '#fff',
                  cursor: 'pointer'
                }"
              >
                按钮按钮
              </div>
            </div>
          </div>
          <div
            :style="{
              color: '#999',
              fontSize: '13px',
              background: '#17181B',
              padding: '22px 0 12px 0'
            }"
          >
            <div
              :style="{
                maxWidth: '820px',
                margin: '0 auto',
                padding: '0 12px',
                overflow: 'hidden'
              }"
            >
              <el-row :gutter="16">
                <el-col v-for="index in 4" :key="index" :sm="6" :xs="12">
                  <div style="width: max-content; margin: 12px auto">
                    <div style="color: #fff; font-size: 14px">标题标题</div>
                    <div style="margin-top: 8px; line-height: 2">
                      <div>文字文字文字</div>
                      <div>文字文字文字</div>
                      <div>文字文字文字</div>
                    </div>
                  </div>
                </el-col>
              </el-row>
              <div
                :style="{
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  borderTop: '1px solid #333',
                  paddingTop: '12px',
                  marginTop: '22px'
                }"
              >
                <template v-for="index in 4" :key="index">
                  <div>文字文字</div>
                  <div
                    v-if="index !== 4"
                    :style="{
                      borderLeft: '1px solid #444',
                      height: '12px',
                      margin: '0 12px'
                    }"
                  ></div>
                </template>
              </div>
            </div>
          </div>
        </ele-admin-layout>
      </div>
    </frame-card>
  </ele-card>
</template>

<script setup>
  import { ref, shallowRef } from 'vue';
  import { LayoutTool } from 'ele-admin-plus/es';
  import FrameCard from './frame-card.vue';

  const headerMenus = shallowRef([
    { index: '/index', title: '首页' },
    { index: '/service', title: '产品和服务' },
    { index: '/product', title: '解决方案' },
    { index: '/cases', title: '客户案例' }
  ]);

  const banner = ref([
    'https://cdn2.weimob.com/saas/@assets/saas-fe-website-web-stc/_next/static/media/L101.0a6f1b83.jpg',
    'https://cdn2.weimob.com/saas/@assets/saas-fe-website-web-stc/_next/static/media/L108.d7c6f061.png',
    'https://cdn2.weimob.com/saas/@assets/saas-fe-website-web-stc/_next/static/media/L100.98e3daa4.jpg'
  ]);
</script>

<style lang="scss">
  html.dark .demo-banner-img {
    filter: brightness(78%);
  }
</style>
