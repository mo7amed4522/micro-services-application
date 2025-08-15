<template>
  <div class="space-y-6">
    <!-- Stats Cards -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
      <StatsCard
        v-for="stat in stats"
        :key="stat.title"
        :title="stat.title"
        :value="stat.value"
        :change="stat.change"
        :icon="stat.icon"
        :color="stat.color"
      />
    </div>

    <!-- Charts Section -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
      <div class="card">
        <h3 class="text-lg font-semibold mb-4">User Growth</h3>
        <div class="h-64 bg-gray-100 rounded-lg flex items-center justify-center">
          <p class="text-gray-500">Chart Component Here</p>
        </div>
      </div>
      
      <div class="card">
        <h3 class="text-lg font-semibold mb-4">Revenue Analytics</h3>
        <div class="h-64 bg-gray-100 rounded-lg flex items-center justify-center">
          <p class="text-gray-500">Chart Component Here</p>
        </div>
      </div>
    </div>

    <!-- Recent Activity -->
    <div class="card">
      <h3 class="text-lg font-semibold mb-4">Recent Activity</h3>
      <div class="space-y-4">
        <div
          v-for="activity in recentActivity"
          :key="activity.id"
          class="flex items-center space-x-4 p-4 bg-gray-50 rounded-lg"
        >
          <div class="flex-shrink-0">
            <div class="w-10 h-10 bg-primary-100 rounded-full flex items-center justify-center">
              <UserIcon class="w-5 h-5 text-primary-600" />
            </div>
          </div>
          <div class="flex-1">
            <p class="text-sm font-medium text-gray-900">{{ activity.user }}</p>
            <p class="text-sm text-gray-500">{{ activity.action }}</p>
          </div>
          <div class="text-sm text-gray-500">
            {{ activity.time }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import StatsCard from '../components/dashboard/StatsCard.vue'

export default {
  name: 'Dashboard',
  components: {
    StatsCard
  },
  data() {
    return {
      stats: [
        {
          title: 'Total Users',
          value: '12,345',
          change: '+12%',
          icon: 'UserIcon',
          color: 'primary'
        },
        {
          title: 'Revenue',
          value: '$45,678',
          change: '+8%',
          icon: 'ChartBarIcon',
          color: 'secondary'
        },
        {
          title: 'Orders',
          value: '1,234',
          change: '+15%',
          icon: 'HomeIcon',
          color: 'luxor'
        },
        {
          title: 'Growth',
          value: '23%',
          change: '+5%',
          icon: 'CogIcon',
          color: 'green'
        }
      ],
      recentActivity: [
        {
          id: 1,
          user: 'John Doe',
          action: 'Created a new account',
          time: '2 minutes ago'
        },
        {
          id: 2,
          user: 'Jane Smith',
          action: 'Updated profile information',
          time: '5 minutes ago'
        },
        {
          id: 3,
          user: 'Mike Johnson',
          action: 'Made a purchase',
          time: '10 minutes ago'
        }
      ]
    }
  }
}
</script>