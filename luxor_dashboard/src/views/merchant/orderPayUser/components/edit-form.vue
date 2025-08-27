<template>
  <ele-modal form :width="550" :model-value="modelValue" :title="isUpdate ? t('common.edit') : t('common.add')" @update:modelValue="updateModelValue">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="140px" @submit.prevent="">
      <el-form-item label="商户号" prop="mUserNo">
        <el-input v-model="form.mUserNo" placeholder="请输入商户号" clearable />
      </el-form-item>
      <el-form-item label="历史账号" prop="hisAccount">
        <el-input v-model="form.hisAccount" placeholder="请输入历史账号" clearable />
      </el-form-item>
      <el-form-item label="商户下用户id" prop="subUserId">
        <el-input v-model="form.subUserId" placeholder="请输入商户下用户id" clearable />
      </el-form-item>
      <el-form-item label="用户标识名称/描述" prop="subUserMsg">
        <el-input v-model="form.subUserMsg" placeholder="请输入用户标识名称/描述" clearable />
      </el-form-item>
      <el-form-item label="是否启用" prop="valid">
        <!-- radio -->
        <el-radio-group v-model="form.valid">
          <el-radio :value="1">启用</el-radio>
          <el-radio :value="0">停用</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="updateModelValue(false)">{{ t('common.cancelText') }}</el-button>
      <el-button type="primary" :loading="loading" @click="save">{{ t('common.saveText') }}</el-button>
    </template>
  </ele-modal>
</template>

<script setup>
  import { ref, reactive, watch } from 'vue';
  import { EleMessage } from 'ele-admin-plus/es';
  import { useFormData } from '@/utils/use-form-data';
  import { QuestionCircleOutlined } from '@/components/icons/index.js';
  import { createOrderPayUserApi, updateOrderPayUserApi } from '@/api/merchant';
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
    id: void 0,
    hisAccount: '',
    mUserNo: '',
    subUserId: '',
    subUserMsg: '',
    valid: 1
  });

  /** 表单验证规则 */
  const rules = reactive({
    mUserNo: [{ required: true, message: '请输入商户号', trigger: 'blur' }],
    hisAccount: [{ required: true, message: '请输入历史账号', trigger: 'blur' }],
    subUserId: [{ required: true, message: '请输入商户下用户id', trigger: 'blur' }],
    subUserMsg: [{ required: true, message: '请输入用户标识名称/描述', trigger: 'blur' }],
    valid: [{ required: true, message: '请选择状态', trigger: ['change', 'blur'] }]
  });

  /** 保存编辑 */
  const save = () => {
    formRef.value?.validate?.((valid) => {
      if (!valid) {
        return;
      }
      loading.value = true;
      const saveOrUpdate = isUpdate.value ? updateOrderPayUserApi : createOrderPayUserApi;
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
