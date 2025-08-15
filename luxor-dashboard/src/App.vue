<template>
  <div id="app" class="min-h-screen bg-gray-50">
    <div class="flex h-screen">
      <!-- Sidebar -->
      <Sidebar :is-open="sidebarOpen" @toggle="toggleSidebar" />
      
      <!-- Main Content -->
      <div class="flex-1 flex flex-col overflow-hidden">
        <!-- Header -->
        <Header @toggle-sidebar="toggleSidebar" />
        
        <!-- Main Content Area -->
        <main class="flex-1 overflow-x-hidden overflow-y-auto bg-gray-50 p-6">
          <router-view />
        </main>
      </div>
    </div>
  </div>
</template>

<script>
import Sidebar from './components/layout/Sidebar.vue'
import Header from './components/layout/Header.vue'

export default {
  name: 'App',
  components: {
    Sidebar,
    Header
  },
  data() {
    return {
      sidebarOpen: false
    }
  },
  methods: {
    toggleSidebar() {
      this.sidebarOpen = !this.sidebarOpen
    }
  }
}
</script>