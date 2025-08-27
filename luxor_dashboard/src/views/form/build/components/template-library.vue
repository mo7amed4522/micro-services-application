<template>
  <EleDrawer
    :size="980"
    title="模板库"
    :appendToBody="true"
    style="max-width: 100%"
    :modelValue="modelValue"
    :bodyStyle="{
      height: '100%',
      overflow: 'auto',
      padding: '20px 20px 0 20px',
      background: 'var(--el-bg-color-page)'
    }"
    @update:modelValue="updateModelValue"
  >
    <ElRow :gutter="20">
      <ElCol v-for="item in items" :key="item.name" :md="8" :sm="12" :xs="24">
        <EleCard
          :body-style="{ padding: '10px 16px 0 16px' }"
          class="library-item"
          @click="handleSelect(item)"
        >
          <div>{{ item.name }}</div>
          <div style="height: 228px; padding-top: 14px; box-sizing: border-box">
            <component :is="item.cover" />
          </div>
        </EleCard>
      </ElCol>
    </ElRow>
  </EleDrawer>
</template>

<script setup>
  import { mapTree } from 'ele-admin-plus/es';
  import {
    formSingleConfig,
    formMultipleConfig,
    formGroupConfig,
    formQuestionConfig,
    formResumeConfig,
    formResumeViewConfig,
    formTableConfig,
    formTableViewConfig,
    formTabsConfig,
    formSearchConfig,
    formDivProConfig
  } from './template-config';
  import CoverSingle from './covers/templates/cover-single.vue';
  import CoverMultiple from './covers/templates/cover-multiple.vue';
  import CoverGroup from './covers/templates/cover-group.vue';
  import CoverQuestion from './covers/templates/cover-question.vue';
  import CoverResume from './covers/templates/cover-resume.vue';
  import CoverResumeView from './covers/templates/cover-resume-view.vue';
  import CoverTable from './covers/templates/cover-table.vue';
  import CoverTableView from './covers/templates/cover-table-view.vue';
  import CoverTabs from './covers/templates/cover-tabs.vue';
  import CoverSearch from './covers/templates/cover-search.vue';
  import CoverDiv from './covers/templates/cover-div.vue';

  const emit = defineEmits(['update:modelValue', 'select']);

  defineProps({
    /** 弹窗是否打开 */
    modelValue: {
      type: Boolean,
      required: true
    }
  });

  /** 数据 */
  const items = [
    {
      name: '单列表单',
      cover: CoverSingle,
      config: formSingleConfig
    },
    {
      name: '多列表单',
      cover: CoverMultiple,
      config: formMultipleConfig
    },
    {
      name: '分组表单',
      cover: CoverGroup,
      config: formGroupConfig
    },
    {
      name: '问卷表单',
      cover: CoverQuestion,
      config: formQuestionConfig
    },
    {
      name: '简历表单',
      cover: CoverResume,
      config: formResumeConfig
    },
    {
      name: '只读简历表单',
      cover: CoverResumeView,
      config: formResumeViewConfig
    },
    {
      name: '表格表单',
      cover: CoverTable,
      config: formTableConfig
    },
    {
      name: '只读表格表单',
      cover: CoverTableView,
      config: formTableViewConfig
    },
    {
      name: '选项卡表单',
      cover: CoverTabs,
      config: formTabsConfig
    },
    {
      name: '搜索表单',
      cover: CoverSearch,
      config: formSearchConfig
    },
    {
      name: 'div高级示例',
      cover: CoverDiv,
      config: formDivProConfig
    }
  ];

  /** 更新modelValue */
  const updateModelValue = (value) => {
    emit('update:modelValue', value);
  };

  /** 选择模板 */
  const handleSelect = (template) => {
    updateModelValue(false);
    let i = 0;
    const config = JSON.parse(JSON.stringify(template.config));
    config.items = mapTree(config.items, (item) => {
      i++;
      return { ...item, key: `${template.name}-i-${i}` };
    });
    emit('select', config);
  };

  handleSelect(items[0]);
</script>

<style lang="scss" scoped>
  .library-item {
    margin: 0 0 20px 0;
    border: 2px solid transparent;
    transition: all 0.24s;
    cursor: pointer;

    &:hover {
      transform: translateY(-8px);
      border-color: var(--el-color-primary);
      box-shadow: var(--el-box-shadow-light);
    }
  }
</style>
