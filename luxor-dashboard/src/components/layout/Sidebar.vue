<template>
  <div class="flex">
    <!-- Mobile sidebar overlay -->
    <div
      v-if="isOpen"
      class="fixed inset-0 z-40 lg:hidden"
      @click="$emit('toggle')"
    >
      <div class="fixed inset-0 bg-gray-600 bg-opacity-75"></div>
    </div>

    <!-- Sidebar -->
    <div
      :class="[
        'fixed inset-y-0 left-0 z-50 w-64 bg-white shadow-lg transform transition-transform duration-300 ease-in-out lg:translate-x-0 lg:static lg:inset-0',
        isOpen ? 'translate-x-0' : '-translate-x-full'
      ]"
    >
      <!-- Logo -->
      <div class="flex items-center justify-center h-16 bg-gradient-luxor">
        <h1 class="text-2xl font-bold text-white">Luxor</h1>
      </div>

      <!-- Navigation -->
      <nav class="mt-8 px-4">
        <div class="space-y-2">
          <router-link
            v-for="item in navigation"
            :key="item.name"
            :to="item.href"
            :class="[
              'sidebar-link',
              $route.name === item.name ? 'active' : ''
            ]"
          >
            <component :is="item.icon" class="w-5 h-5 mr-3" />
            {{ item.name }}
          </router-link>
        </div>
      </nav>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Sidebar',
  props: {
    isOpen: {
      type: Boolean,
      default: false
    }
  },
  emits: ['toggle'],
  data() {
    return {
      navigation: [
        { name: 'Dashboard', href: '/', icon: 'HomeIcon' },
        { name: 'Users', href: '/users', icon: 'UserIcon' },
        { name: 'Analytics', href: '/analytics', icon: 'ChartBarIcon' },
        { name: 'Settings', href: '/settings', icon: 'CogIcon' }
      ]
    }
  }
}
</script>