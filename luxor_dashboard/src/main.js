import { createApp } from 'vue';
import App from './App.vue';
import store from './store';
import router from './router';
import permission from './utils/permission';
import DictData from '@/components/DictData/index.vue';
import i18n from './i18n';
import installer from './as-needed';
import 'element-plus/theme-chalk/display.css';
import 'ele-admin-plus/es/style/nprogress.scss';
import './styles/themes/rounded.scss';
import './styles/themes/dark.scss';
import './styles/index.scss';
import * as ElementPlusIconsVue from '@element-plus/icons-vue';

const app = createApp(App);
// 全局注册所有 Element Plus 图标
Object.keys(ElementPlusIconsVue).forEach((key) => {
  app.component(key, ElementPlusIconsVue[key]);
});
app.use(store);
app.use(router);
app.use(permission);
app.use(i18n);
app.use(installer);
app.component('DictData', DictData);

// if (import.meta.env.MODE === 'development') {
//   import('./mocks/mock.js')
//     .then(() => {
//       console.log('Mock Service Worker initialized');
//     })
//     .catch((err) => {
//       console.error('Failed to initialize MSW:', err);
//     });
// }

app.mount('#app');
