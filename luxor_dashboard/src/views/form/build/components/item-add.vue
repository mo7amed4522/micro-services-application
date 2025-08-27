<template>
  <EleDrawer
    :size="980"
    title="添加表单项"
    :appendToBody="true"
    style="max-width: 100%"
    :modelValue="modelValue"
    :bodyStyle="{
      padding: 0,
      height: '100%',
      display: 'flex',
      flexDirection: 'column'
    }"
    @update:modelValue="updateModelValue"
  >
    <div class="cover-wrapper">
      <div class="item-group-label">表单类型组件</div>
      <EleCheckCard
        :items="items"
        v-model="form.type"
        :row="{ gutter: 20 }"
        itemClass="item-cover-wrap"
        :style="{ padding: '0 20px' }"
      >
        <template #item="{ item }">
          <div class="item-cover">
            <component :is="item.cover" />
          </div>
          <div>{{ item.label }}</div>
        </template>
      </EleCheckCard>
      <div class="item-group-label">展示类型组件</div>
      <EleCheckCard
        :items="viewItems"
        v-model="form.type"
        :row="{ gutter: 20 }"
        itemClass="item-cover-wrap"
        :style="{ padding: '0 20px' }"
      >
        <template #item="{ item }">
          <div class="item-cover">
            <component :is="item.cover" />
          </div>
          <div>{{ item.label }}</div>
        </template>
      </EleCheckCard>
      <div class="item-group-label">容器类型组件</div>
      <EleCheckCard
        :items="containerItems"
        v-model="form.type"
        :row="{ gutter: 20 }"
        itemClass="item-cover-wrap"
        :style="{ padding: '0 20px' }"
      >
        <template #item="{ item }">
          <div class="item-cover">
            <GridSelect v-model="tableGrid" v-if="item.value === 'eleTable'" />
            <TabsSelect v-model="tabSize" v-else-if="item.value === 'tabs'" />
            <component v-else :is="item.cover" />
          </div>
          <div>{{ item.label }}</div>
        </template>
      </EleCheckCard>
    </div>
    <div class="footer-bar">
      <div style="flex-shrink: 0">
        {{ form.type && viewTypes.includes(form.type) ? '文本：' : '标题：' }}
      </div>
      <div style="flex: 1">
        <ElInput
          clearable
          v-model="form.label"
          placeholder="请输入标题"
          :disabled="form.type && ['tabs', 'eleTable'].includes(form.type)"
        />
      </div>
      <div style="flex-shrink: 0; width: 70px; text-align: right">
        {{
          form.type &&
          (viewTypes.includes(form.type) ||
            containerTypes.includes(form.type) ||
            form.type === 'eleTable')
            ? 'key：'
            : '字段名：'
        }}
      </div>
      <div style="flex: 1">
        <ElInput clearable v-model="form.prop" placeholder="请输入字段名" />
      </div>
    </div>
    <template #footer>
      <ElButton @click="updateModelValue(false)">取消</ElButton>
      <ElButton type="primary" @click="save">确定</ElButton>
    </template>
  </EleDrawer>
</template>

<script setup>
  import { watch, ref } from 'vue';
  import { useFormData } from '@/utils/use-form-data';
  import { viewTypes, containerTypes } from '@/components/ProForm/util';
  import { itemTypes } from './item-types';
  import { getOptionsAndProps } from './util';
  import { covers } from './covers';
  import GridSelect from './grid-select.vue';
  import TabsSelect from './tabs-select.vue';
  let id = 1;

  const emit = defineEmits(['done', 'update:modelValue']);

  const props = defineProps({
    /** 弹窗是否打开 */
    modelValue: {
      type: Boolean,
      required: true
    }
  });

  /** 获取卡片数据 */
  function getCardItems(items) {
    return items.map((d) => {
      return {
        ...d,
        col: { md: 6, sm: 8, xs: 12 },
        cover: covers[d.value]
      };
    });
  }

  /** 表单类型组件列表 */
  const items = getCardItems(
    itemTypes.filter(
      (d) => !viewTypes.includes(d.value) && !containerTypes.includes(d.value)
    )
  );

  /** 展示类型组件列表 */
  const viewItems = getCardItems(
    itemTypes.filter((d) => viewTypes.includes(d.value))
  );

  /** 容器类型组件列表 */
  const containerItems = getCardItems([
    { value: 'card', label: '卡片' },
    { value: 'tabs', label: '选项卡' },
    { value: 'eleTable', label: '表格' },
    { value: 'div', label: 'div' }
  ]);

  /** 配置项表单数据 */
  const [form, resetFields, assignFields] = useFormData({
    key: void 0,
    label: '',
    prop: '',
    type: void 0
  });

  /** 表格添加的行列数 */
  const tableGrid = ref([1, 1]);

  /** 选项卡添加的数量 */
  const tabSize = ref(1);

  /** 更新modelValue */
  const updateModelValue = (value) => {
    emit('update:modelValue', value);
  };

  /** 保存表单项配置 */
  const save = () => {
    id++;
    const prop = form.prop || 'f' + String(id).padStart(2, '0');
    const { options, props } = getOptionsAndProps(form.type);
    const isView = form.type != null && viewTypes.includes(form.type);
    const isContainer = form.type != null && containerTypes.includes(form.type);
    const item = {
      ...form,
      prop,
      props: props ? JSON.parse(props) : void 0,
      options: options ? JSON.parse(options) : void 0,
      required: isView || isContainer || form.type === 'text' ? void 0 : true
    };
    // 选项卡添加子级
    if (item.type === 'tabs') {
      item.label = '';
      item.children = Array.from({ length: tabSize.value || 0 }).map((_, i) => {
        const cId = String(i + 1).padStart(2, '0');
        const key = '_tab_' + cId;
        const child = {
          key: item.key + key,
          label: `选项卡${cId}`,
          prop: prop + key,
          type: 'div'
        };
        return child;
      });
    } else if (item.type === 'eleTable') {
      // 添加表格子级
      item.label = '';
      item.type = 'div';
      item.props = {
        is: 'ele-table',
        border: true,
        hasHeader: false,
        style: { tableLayout: 'fixed' }
      };
      item.children = Array.from({ length: tableGrid.value[0] || 0 }).map(
        (_, i) => {
          const iId = String(i + 1).padStart(2, '0');
          const childs = Array.from({ length: tableGrid.value[1] || 0 }).map(
            (_, j) => {
              const k = '_td_' + iId + '_' + String(j + 1).padStart(2, '0');
              const child = {
                key: item.key + k,
                label: '',
                prop: prop + k,
                type: 'div',
                props: { is: 'td' }
              };
              return child;
            }
          );
          const key = '_tr_' + iId;
          const childItem = {
            key: item.key + key,
            label: '',
            prop: prop + key,
            type: 'div',
            props: { is: 'tr' },
            children: childs
          };
          return childItem;
        }
      );
    }
    emit('done', item);
  };

  watch(
    () => props.modelValue,
    (modelValue) => {
      if (!modelValue) {
        resetFields();
        return;
      }
      const key = String(id).padStart(2, '0');
      assignFields({
        key,
        prop: 'f' + key,
        label: '字段' + key,
        type: 'input'
      });
    }
  );
</script>

<style lang="scss" scoped>
  .cover-wrapper {
    flex: 1;
    overflow: auto;
    padding-top: 12px;
    background: var(--el-bg-color-page);

    :deep(.ele-check-card-group .item-cover-wrap.ele-check-card) {
      font-size: 13px;
      padding: 6px 12px;
      margin: 0 0 20px 0;
      background: var(--el-bg-color);
      border: 2px solid transparent;
      transition: all 0.24s;

      &:hover {
        transform: translateY(-8px);
        box-shadow: var(--el-box-shadow-light);
      }

      &.is-checked {
        border-color: var(--el-color-primary);
      }
    }

    .item-cover {
      display: flex;
      flex-direction: column;
      justify-content: center;
      max-width: 174px;
      height: 120px;
      margin: 0 auto;
    }
  }

  .footer-bar {
    display: flex;
    align-items: center;
    padding: 16px 20px;
    box-sizing: border-box;
    box-shadow: 0 -1px 5px rgba(0, 0, 0, 0.08);
    position: relative;
    z-index: 6;
  }

  .item-group-label {
    font-size: 12px;
    font-weight: 600;
    color: var(--el-color-primary);
    padding: 0 0 12px 22px;
    position: relative;

    &::after {
      content: '';
      position: absolute;
      left: 10px;
      top: 50%;
      width: 4px;
      height: 12px;
      margin-top: -12px;
      border-radius: 2px;
      background: var(--el-color-primary);
    }
  }
</style>
