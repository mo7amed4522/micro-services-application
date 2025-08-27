<template>
  <EleCard
    header="预览"
    shadow="always"
    :bordered="true"
    :bodyStyle="{ padding: 0, display: 'flex', flexDirection: 'column' }"
    style="min-height: 100%; display: flex; flex-direction: column"
  >
    <ProForm
      v-bind="proFormProps"
      ref="proFormRef"
      :model="form"
      :items="formItems"
      :validateOnRuleChange="false"
      :submitText="showFooterExpand ? '搜索' : void 0"
      @updateValue="setFieldValue"
      @submit="handleSubmit"
      @reset="handleReset"
    >
      <template v-if="showFooterExpand" #footerExtra>
        <ElLink
          type="primary"
          :underline="false"
          @click="isSearchExpand = !isSearchExpand"
          style="margin-left: 12px"
        >
          <template v-if="isSearchExpand">
            <span>收起</span>
            <ElIcon style="vertical-align: -1px">
              <ArrowUp />
            </ElIcon>
          </template>
          <template v-else>
            <span>展开</span>
            <ElIcon style="vertical-align: -2px">
              <ArrowDown />
            </ElIcon>
          </template>
        </ElLink>
      </template>
    </ProForm>
  </EleCard>
</template>

<script setup>
  import { ref, onBeforeUnmount, onMounted, nextTick, computed } from 'vue';
  import { EleMessage } from 'ele-admin-plus/es';
  import { useFormData } from '@/utils/use-form-data';
  import { ArrowDown, ArrowUp } from '@/components/icons';
  import ProForm from '@/components/ProForm/index.vue';
  import { getFormInitValue } from './util';
  import { transformJsVar } from './code-js-var';

  const props = defineProps({
    /** 表单项 */
    items: {
      type: Array,
      required: true
    },
    /** 表单其它属性 */
    formProps: {
      type: Object,
      required: true
    },
    /** 表单缓存数据 */
    data: Object
  });

  const emit = defineEmits(['unmount']);

  /** 表单实例 */
  const proFormRef = ref(null);

  /** 表单项数据 */
  const formItems = ref([]);

  /** 获取表单初始值 */
  const init = getFormInitValue(props.items);

  /** 表单数据 */
  const [form, resetFields, assignFields, setFieldValue] = useFormData(init);

  /** 表单属性 */
  const proFormProps = computed(() => {
    return JSON.parse(JSON.stringify(props.formProps || {}), (_jk, jv) => {
      const jsVar = transformJsVar(jv);
      if (jsVar != null && jsVar.jsCode != null) {
        return jsVar.jsCode(
          form,
          formItems,
          props.formProps || {},
          isSearchExpand
        );
      }
      return jv;
    });
  });

  /** 是否需要展开收起按钮 */
  const showFooterExpand = ref(false);

  /** 提交 */
  const handleSubmit = () => {
    EleMessage.success('表单验证通过');
    console.log('form:', JSON.parse(JSON.stringify(form)));
  };

  /** 重置 */
  const handleReset = () => {
    resetFields();
    nextTick(() => {
      proFormRef.value?.clearValidate?.();
      nextTick(() => {
        proFormRef.value?.clearValidate?.();
      });
    });
  };

  /** 销毁事件 */
  onBeforeUnmount(() => {
    emit('unmount', form);
  });

  onMounted(() => {
    // 回显缓存数据
    if (props.data != null) {
      assignFields(
        props.data,
        Object.keys(props.data).filter((k) => props.data?.[k] == null)
      );
    }
    // 赋值表单项数据
    let footerExpand = false;
    formItems.value = JSON.parse(JSON.stringify(props.items), (_jk, jv) => {
      const jsVar = transformJsVar(jv);
      if (jsVar != null && jsVar.jsCode != null) {
        if (jsVar.name === 'isSearchExpand') {
          footerExpand = true;
        }
        return jsVar.jsCode(
          form,
          formItems,
          props.formProps || {},
          isSearchExpand
        );
      }
      return jv;
    });
    showFooterExpand.value = footerExpand;
  });

  defineExpose({ setFieldValue });

  /** 折叠的表单项是否展开 */
  const isSearchExpand = ref(false);
</script>
