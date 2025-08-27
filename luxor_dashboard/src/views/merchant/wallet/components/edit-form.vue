<template>
  <ele-modal form :width="550" :model-value="modelValue" :title="isUpdate ? '修改' : '新增'" @update:modelValue="updateModelValue">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="120px" @submit.prevent="">
      <el-form-item label="账户余额" prop="balance">
        <el-input v-model="form.balance" placeholder="请输入账户余额" clearable />
      </el-form-item>
      <el-form-item label="是否启用" prop="status">
        <el-switch v-model="form.status" :active-value="1" :inactive-value="0" active-text="正常" inactive-text="冻结" inline-prompt />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="updateModelValue(false)">取消</el-button>
      <el-button type="primary" :loading="loading" @click="save">保存</el-button>
    </template>
  </ele-modal>
</template>

<script setup>
  import { ref, reactive, watch } from 'vue';
  import { EleMessage } from 'ele-admin-plus/es';
  import { useFormData } from '@/utils/use-form-data';
  import { QuestionCircleOutlined } from '@/components/icons/index.js';
  import { createMUserWalletApi, updateMUserWalletApi } from '@/api/merchant';

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
    id: void 0,
    balance: '',
    status: 1
  });

  /** 表单验证规则 */
  const rules = reactive({
    remark: [{ required: true, message: '请输入标签名称', trigger: 'blur' }],
    valid: [{ required: true, message: '请选择状态', trigger: ['change', 'blur'] }]
  });

  /** 保存编辑 */
  const save = () => {
    formRef.value?.validate?.((valid) => {
      if (!valid) {
        return;
      }
      loading.value = true;
      const saveOrUpdate = isUpdate.value ? updateMUserWalletApi : createMUserWalletApi;
      saveOrUpdate({ ...form })
        .then((msg) => {
          loading.value = false;
          EleMessage.success(msg);
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

  watch(
    () => props.modelValue,
    (modelValue) => {
      if (modelValue) {
        if (props.data) {
          assignFields({
            ...props.data
          });
          isUpdate.value = true;
        } else {
          isUpdate.value = false;
        }
      } else {
        resetFields();
        formRef.value?.clearValidate?.();
      }
    }
  );
</script>
