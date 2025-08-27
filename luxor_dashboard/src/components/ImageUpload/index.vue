<template>
  <CommonUpload
    v-model="images"
    :fileLimit="fileLimit"
    :readonly="readonly"
    :disabled="disabled"
    :preview="preview"
    :limit="limit"
    :multiple="multiple"
    :drag="drag"
    :accept="accept"
    :itemStyle="itemStyle"
    :buttonStyle="buttonStyle"
    :sortable="sortable"
    :imageProps="imageProps"
    :progressProps="progressProps"
    :previewProps="previewProps"
    :tools="tools"
    :listType="listType"
    :beforeUploadClick="beforeUploadClick"
    :beforeItemEdit="beforeItemEdit"
    :locale="locale"
    :type="type"
    :name="name"
  />
</template>

<script setup>
  import { ref, watch } from 'vue';
  import CommonUpload from '../CommonUpload/index.vue';

  defineOptions({ name: 'ImageUpload' });

  const props = defineProps({
    type: {
      type: Number,
      default: 1
    },
    name: {
      type: String,
      default: 'icon'
    },
    /** 绑定值 */
    modelValue: String,
    /** 文件大小限制, 单位MB */
    fileLimit: {
      type: Number,
      default: 100
    },
    /** 是否只读 */
    readonly: Boolean,
    /** 是否禁用 */
    disabled: Boolean,
    /** 是否支持点击预览 */
    preview: {
      type: Boolean,
      default: true
    },
    /** 最大上传数量 */
    limit: Number,
    /** 是否支持多选文件 */
    multiple: Boolean,
    /** 是否启用拖拽上传 */
    drag: {
      type: Boolean,
      default: true
    },
    /** 接受上传的文件类型 */
    accept: {
      type: String,
      default: 'image/*'
    },
    /** 自定义样式 */
    itemStyle: Object,
    /** 自定义上传按钮样式 */
    buttonStyle: Object,
    /** 是否开启拖拽排序 */
    sortable: {
      type: [Boolean, Object],
      default: () => {
        return { forceFallback: true };
      }
    },
    /** 自定义图片属性 */
    imageProps: Object,
    /** 自定义进度条属性 */
    progressProps: Object,
    /** 自定义图片预览属性 */
    previewProps: Object,
    /** 是否开启底部预览和修改的操作按钮 */
    tools: {
      type: Boolean,
      default: true
    },
    /** 列表显示样式 */
    listType: String,
    /** 上传按钮点击前的钩子 */
    beforeUploadClick: Function,
    /** 修改按钮点击前的钩子 */
    beforeItemEdit: Function,
    /** 国际化 */
    locale: Object
  });

  const emit = defineEmits(['update:modelValue', 'change']);

  /** 上传组件数据 */
  const images = ref([]);

  /** 更新绑定值 */
  const updateModelValue = (value) => {
    const oldValue = props.modelValue ?? '';
    const newValue = value ?? '';
    if (oldValue !== newValue) {
      emit('update:modelValue', newValue);
      emit('change', value);
    }
  };

  /** 清空上传组件数据 */
  const clearData = () => {
    images.value = [];
  };

  /** 判断是否全部上传完成 */
  const isDone = () => {
    return !images.value.some((item) => item.status !== 'done');
  };

  /** 获取上传数据对应的值 */
  const getDataValue = (imagesValue) => {
    const urls = [];
    imagesValue.forEach((item) => {
      if (item.status === 'done' && (item.originalUrl || item.url) != null) {
        // 优先使用原始URL，如果没有则使用处理后的URL
        urls.push(item.originalUrl || item.url);
      }
    });
    return urls;
  };

  /** 处理图片URL，确保可以正确访问 */
  const processImageUrl = (url) => {
    if (!url) return url;
    
    // 如果是完整URL（包含http/https），直接返回
    if (url.startsWith('http://') || url.startsWith('https://')) {
      return url;
    }
    
    // 如果是相对路径，转换为API路径
    if (url.startsWith('uploads/')) {
      return `/api/${url}`;
    }
    
    return url;
  };

  /** 获取值对应的上传数据 */
  const getImagesValue = (dataValue, imagesValue) => {
    if (!dataValue) {
      return imagesValue.filter((item) => item.status !== 'done');
    }
    const result = [];
    if (props.limit === 1) {
      result.push({ 
        key: `0-${dataValue}`, 
        originalUrl: dataValue, // 存储原始URL
        url: processImageUrl(dataValue), // 存储处理后的URL用于显示
        status: 'done' 
      });
      return result;
    }
    try {
      const urls = JSON.parse(dataValue);
      imagesValue.forEach((item) => {
        if ((item.originalUrl && urls.includes(item.originalUrl)) || 
            (item.url && urls.includes(item.url)) || 
            item.status !== 'done') {
          result.push(item);
        }
      });
      urls.forEach((url, index) => {
        if (url && !result.some((item) => item.originalUrl === url || item.url === url)) {
          const key = `${index}-${url}`;
          const oldIndex = result.findIndex((item) => item.key === key);
          if (oldIndex !== -1) {
            result.splice(oldIndex, 1);
          }
          result.push({ 
            key, 
            originalUrl: url, // 存储原始URL
            url: processImageUrl(url), // 存储处理后的URL用于显示
            status: 'done' 
          });
        }
      });
      if (props.limit != null && result.length > props.limit) {
        const temp = result.filter((item) => item.status !== 'done');
        for (const t of temp) {
          const i = result.indexOf(t);
          result.splice(i, 1);
          if (result.length <= props.limit) {
            break;
          }
        }
      }
    } catch (e) {
      console.error(e);
      imagesValue.forEach((item) => {
        if (item.status !== 'done') {
          result.push(item);
        }
      });
    }
    return result;
  };

  /** 判断上传数据是否改变 */
  const imagesIsChanged = (newImages, oldImages) => {
    if (newImages.length !== oldImages.length) {
      return true;
    }
    for (let i = 0; i < newImages.length; i++) {
      const newItem = newImages[i];
      const oldItem = oldImages[i];
      if (
        newItem.key !== oldItem.key ||
        (newItem.originalUrl || newItem.url) !== (oldItem.originalUrl || oldItem.url) ||
        newItem.status !== oldItem.status
      ) {
        return true;
      }
    }
    return false;
  };

  /** 同步绑定值 */
  watch(
    images,
    (imagesValue) => {
      const dataValue = getDataValue(imagesValue);
      if (props.limit === 1) {
        updateModelValue(dataValue.join());
        return;
      }
      updateModelValue(dataValue.length ? JSON.stringify(dataValue) : '');
    },
    { deep: true }
  );

  watch(
    () => props.modelValue,
    (dataValue) => {
      const imagesValue = getImagesValue(dataValue, images.value);
      if (imagesIsChanged(imagesValue, images.value)) {
        images.value = imagesValue;
      }
    },
    { immediate: true }
  );

  defineExpose({ clearData, isDone });
</script>
