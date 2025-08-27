<!-- 编辑弹窗 -->
<template>
  <ele-modal form :width="540" :model-value="modelValue" :title="t('merchant.tags')" @update:modelValue="updateModelValue">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" @submit.prevent="">
      <el-form-item :label="t('merchant.tagName')" prop="labelId">
        <el-select v-model="form.labelId" :empty-values="[null, undefined]" :placeholder="t('merchant.selectTag')">
          <el-option v-for="item in payLabelList" :key="item.id" :value="item.id" :label="item.name" />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('common.notes')" prop="remark">
        <el-input v-model="form.remark" type="textarea" :placeholder="t('common.enterNotes')" :rows="3" clearable />
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
  import { getPayLabelListApi, createCPayPoolLabelApi } from '@/api/payConfig/index.js';
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
    cPayPoolId: null,
    labelId: null,
    remark: null
  });

  /** 表单验证规则 */
  const rules = reactive({
    labelId: [{ required: true, message: t('merchant.selectTag'), trigger: 'change' }]
  });

  /** 保存编辑 */
  const save = () => {
    formRef.value?.validate?.((valid) => {
      if (!valid) {
        return;
      }
      loading.value = true;
      createCPayPoolLabelApi({
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

  const payLabelList = ref([]);

  const getCPayConfigList = async () => {
    const res = await getPayLabelListApi({
      page: 1,
      pageSize: 9999
    });
    payLabelList.value = res.list;
  };

  watch(
    () => props.modelValue,
    (modelValue) => {
      if (modelValue) {
        if (props.data) {
          getCPayConfigList();
          form.cPayPoolId = props.data.id;
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
