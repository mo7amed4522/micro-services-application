<!-- 上级菜单选择下拉框 -->
<template>
  <el-tree-select
    clearable
    filterable
    :data="menuData"
    check-strictly
    default-expand-all
    node-key="ID"
    :props="{label: (node) => node.meta.title}"
    :placeholder="placeholder"
    :model-value="modelValue || void 0"
    class="ele-fluid"
    :popper-options="{ strategy: 'fixed' }"
    @update:modelValue="updateValue"
  >
    <template #default="{ data }">
      <el-icon
        v-if="data.icon"
        style="margin-right: 4px; transform: translateY(-1px)"
      >
<!--        <component :is="data.icon" />-->
      </el-icon>
      <span>{{ data.meta.title }}</span>
    </template>
    <template v-if="selectedIcon" #prefix>
      <el-icon color="var(--el-text-color-regular)" style="margin-right: 6px">
        <component :is="selectedIcon" />
      </el-icon>
    </template>
  </el-tree-select>
</template>

<script setup>
  import { ref, computed } from 'vue';
  import { EleMessage, toTree, findTree } from 'ele-admin-plus/es';
  import { listMenus } from '@/api/sys/menu';
  import * as MenuIcons from '@/layout/menu-icons';

  defineOptions({
    components: MenuIcons
  });

  const emit = defineEmits(['update:modelValue']);

  const props = defineProps({
    /** 选中的菜单 */
    modelValue: [Number, String],
    /** 提示信息 */
    placeholder: {
      type: String,
      default: '请选择上级菜单'
    }
  });

  /** 菜单数据 */
  const menuData = ref([]);

  /** 更新选中数据 */
  const updateValue = (value) => {
    emit('update:modelValue', value || 0);
  };

  /** 选中菜单的图标 */
  const selectedIcon = computed(() => {
    if (!props.modelValue) {
      return;
    }
    return findTree(menuData.value, (d) => d.ID == props.modelValue)?.icon;
  });

  /** 获取菜单数据 */
  listMenus()
    .then((list) => {
      menuData.value = toTree({
        data: list,
        idField: 'ID',
        parentIdField: 'parentId'
      });
    })
    .catch((e) => {
      EleMessage.error(e.message);
    });
</script>
