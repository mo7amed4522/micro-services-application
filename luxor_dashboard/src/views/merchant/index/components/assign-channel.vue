<!-- 编辑弹窗 -->
<template>
  <ele-modal form :width="540" :model-value="modelValue" :title="t('merchant.assignChannel')" @update:modelValue="updateModelValue">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="120px" @submit.prevent="">
      <el-form-item :label="t('merchant.channelName')" prop="cPayConfigId">
        <el-select v-model="form.cPayConfigId" :empty-values="[null, undefined]" :placeholder="t('merchant.selectChannel')">
          <el-option v-for="item in cPayConfigList" :key="item.id" :value="item.id" :label="item.name" />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="updateModelValue(false)">{{ t('common.cancelText') }}</el-button>
      <el-button type="primary" :loading="loading" @click="save">{{ t('common.saveText') }}</el-button>
    </template>
  </ele-modal>
</template>

<script setup>
  import { ref, reactive, watch, computed } from 'vue';
  import { EleMessage } from 'ele-admin-plus/es';
  import { useFormData } from '@/utils/use-form-data';
  import { getCPayConfigListApi } from '@/api/payConfig/index.js';
  import { createMPayChannelConfigApi } from '@/api/merchant'
  import { useI18n } from 'vue-i18n';

  const { t } = useI18n();

  const emit = defineEmits(['done', 'update:modelValue']);

  const props = defineProps({
    /** 弹窗是否打开 */
    modelValue: Boolean,
    /** 修改回显的数据 */
    data: Object
  });

  /** 是否是修改 */
  const isUpdate = ref(false);

  /** 提交状态 */
  const loading = ref(false);

  /** 表单实例 */
  const formRef = ref(null);

  /** 表单数据 */
  const [form, resetFields, assignFields] = useFormData({
    mUserId: null,
    cPayConfigId: null
  });

  /** 表单验证规则 */
  const rules = reactive({
    cPayConfigId: [{ required: true, message: t('merchant.selectChannel'), trigger: 'change' }]
  });

  /** 保存编辑 */
  const save = () => {
    formRef.value?.validate?.((valid) => {
      if (!valid) {
        return;
      }
      loading.value = true;
      const saveOrUpdate = isUpdate.value ? createMPayChannelConfigApi : createMPayChannelConfigApi;
      saveOrUpdate({
        ...form
      })
        .then((res) => {
          loading.value = false;
          EleMessage.success(res);
          updateModelValue(false);
          emit('done');
        })
        .catch((e) => {
          loading.value = false;
          EleMessage.error(e.message);
        });
    });
  };

  /** 更新modelValue */
  const updateModelValue = (value) => {
    emit('update:modelValue', value);
  };

  const cPayConfigList = ref([]);

  const getCPayConfigList = async () => {
    const res = await getCPayConfigListApi({
      page: 1,
      pageSize: 9999
    });
    cPayConfigList.value = res.list;
  };

  watch(
    () => props.modelValue,
    (modelValue) => {
      if (modelValue) {
        if (props.data) {
          getCPayConfigList();
          // assignFields(props.data);
          form.mUserId = props.data.id;
          isUpdate.value = true;
        } else {
          isUpdate.value = false;
        }
      } else {
        resetFields();
        formRef.value?.resetFields?.();
      }
    }
  );
</script>
