import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import './assets/css/main.css'

// Import Heroicons
import {
  HomeIcon,
  UserIcon,
  CogIcon,
  ChartBarIcon,
  BellIcon,
  MenuIcon,
  XIcon
} from '@heroicons/vue/24/outline'

const app = createApp(App)

// Register global components
app.component('HomeIcon', HomeIcon)
app.component('UserIcon', UserIcon)
app.component('CogIcon', CogIcon)
app.component('ChartBarIcon', ChartBarIcon)
app.component('BellIcon', BellIcon)
app.component('MenuIcon', MenuIcon)
app.component('XIcon', XIcon)

app.use(store).use(router).mount('#app')