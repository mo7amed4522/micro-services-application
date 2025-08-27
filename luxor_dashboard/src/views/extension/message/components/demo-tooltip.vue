<template>
  <ele-card header="文本提示">
    <option-item label="使用">
      <ele-tooltip content="EleAdminPlus" placement="top" :offset="8">
        <el-button>Dark</el-button>
      </ele-tooltip>
      <ele-tooltip
        content="EleAdminPlus"
        placement="top"
        effect="light"
        :offset="8"
      >
        <el-button>Light</el-button>
      </ele-tooltip>
    </option-item>
    <option-item label="主题">
      <ele-tooltip
        content="EleAdminPlus"
        placement="top"
        effect="danger"
        :offset="8"
      >
        <el-button class="ele-btn-icon">错误</el-button>
      </ele-tooltip>
      <ele-tooltip
        content="EleAdminPlus"
        placement="top"
        effect="warning"
        :offset="8"
      >
        <el-button class="ele-btn-icon">警告</el-button>
      </ele-tooltip>
      <ele-tooltip
        content="EleAdminPlus"
        placement="top"
        effect="success"
        :offset="8"
      >
        <el-button class="ele-btn-icon">成功</el-button>
      </ele-tooltip>
      <ele-tooltip
        content="EleAdminPlus"
        placement="top"
        bg="linear-gradient( 135deg, #43CBFF 10%, #9708CC 100%)"
        arrow-bg="#7556E0"
        :offset="8"
      >
        <el-button class="ele-btn-icon">Custom</el-button>
      </ele-tooltip>
    </option-item>
    <option-item label="气泡">
      <ele-popover
        :width="180"
        trigger="click"
        title="这是标题"
        :offset="8"
        :popper-options="{ strategy: 'fixed' }"
      >
        <template #reference>
          <el-button class="ele-btn-icon">Popover</el-button>
        </template>
        <div>This is content.</div>
        <div>This is content.</div>
      </ele-popover>
      <ele-popconfirm
        :width="190"
        :offset="8"
        title="确定要删除此用户吗?"
        :popper-options="{ strategy: 'fixed' }"
      >
        <template #reference>
          <el-button class="ele-btn-icon">PopConfirm</el-button>
        </template>
      </ele-popconfirm>
      <ele-popconfirm
        :width="258"
        :offset="8"
        title="用户满意度调研"
        content="您在使用中是否对本产品还满意?"
        cancel-button-text="关闭"
        confirm-button-text="满意"
        :cancel-button-props="{ style: { order: -1, margin: '0 auto 0 0' } }"
        :popper-options="{ strategy: 'fixed' }"
      >
        <template #reference>
          <el-button class="ele-btn-icon">PopConfirm</el-button>
        </template>
        <template #action="{ cancel }">
          <el-button size="small" plain type="danger" @click="cancel()">
            投诉
          </el-button>
        </template>
      </ele-popconfirm>
    </option-item>
  </ele-card>
</template>

<script setup>
  import OptionItem from '@/views/extension/avatar/components/option-item.vue';
</script>
