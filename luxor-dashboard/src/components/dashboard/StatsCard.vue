<template>
  <div class="card hover:shadow-lg transition-shadow duration-200">
    <div class="flex items-center">
      <div class="flex-shrink-0">
        <div :class="iconClasses">
          <component :is="icon" class="w-6 h-6" />
        </div>
      </div>
      <div class="ml-4 flex-1">
        <p class="text-sm font-medium text-gray-500">{{ title }}</p>
        <p class="text-2xl font-semibold text-gray-900">{{ value }}</p>
        <p :class="changeClasses">
          {{ change }}
        </p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'StatsCard',
  props: {
    title: {
      type: String,
      required: true
    },
    value: {
      type: String,
      required: true
    },
    change: {
      type: String,
      required: true
    },
    icon: {
      type: String,
      required: true
    },
    color: {
      type: String,
      default: 'primary'
    }
  },
  computed: {
    iconClasses() {
      const baseClasses = 'w-12 h-12 rounded-lg flex items-center justify-center'
      const colorClasses = {
        primary: 'bg-primary-100 text-primary-600',
        secondary: 'bg-secondary-100 text-secondary-600',
        luxor: 'bg-yellow-100 text-yellow-600',
        green: 'bg-green-100 text-green-600'
      }
      return `${baseClasses} ${colorClasses[this.color] || colorClasses.primary}`
    },
    changeClasses() {
      const isPositive = this.change.startsWith('+')
      return `text-sm font-medium ${
        isPositive ? 'text-green-600' : 'text-red-600'
      }`
    }
  }
}
</script>