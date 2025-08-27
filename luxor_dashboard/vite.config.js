import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import { resolve } from 'path';
import Compression from 'vite-plugin-compression';
import Components from 'unplugin-vue-components/vite';
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers';
import { EleAdminResolver } from 'ele-admin-plus/es/utils/resolvers';

export default defineConfig(({ command, mode }) => {
  const isBuild = command === 'build';
  const alias = {
    '@/': resolve('src') + '/',
    'vue-i18n': 'vue-i18n/dist/vue-i18n.cjs.js'
  };
  const plugins = [vue()];

  if (isBuild) {
    // 组件按需引入
    plugins.push(
      Components({
        dts: false,
        resolvers: [
          ElementPlusResolver({
            importStyle: 'sass'
          }),
          EleAdminResolver({
            importStyle: 'sass'
          })
        ]
      })
    );

    // gzip压缩
    plugins.push(
      Compression({
        disable: !isBuild,
        threshold: 10240,
        algorithm: 'gzip',
        ext: '.gz'
      })
    );
  } else {
    // 开发环境全局安装
    alias['./as-needed'] = './global-import';
  }

  // 用于开发环境代理配置
  const serverConfig =
    mode === 'development'
      ? {
          port: 5180,
          proxy: {
            '/api': {
              target: 'http://172.232.96.159:13340/api/',
              // target: 'https://newpayn.indpy.com/api/',
              // target: 'https://knympy.com/api/',
              changeOrigin: true,
              // rewrite: (path) => path.replace(/^\/api/, '')
              rewrite: (path) => path.replace(new RegExp('^' + '/api'), '')
            }
          }
        }
      : {};

  return {
    resolve: { alias },
    plugins,
    css: {
      preprocessorOptions: {
        scss: {
          additionalData: `@use "@/styles/variables.scss" as *;`
        }
      }
    },
    optimizeDeps: {
      include: ['echarts/core', 'echarts/charts', 'echarts/renderers', 'echarts/components', 'vue-echarts']
    },
    build: {
      target: 'chrome63',
      chunkSizeWarningLimit: 2000
    },
    server: serverConfig
  };
});
