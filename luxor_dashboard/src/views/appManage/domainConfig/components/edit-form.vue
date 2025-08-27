<template>
  <ele-modal form :width="550" :model-value="modelValue" :title="isUpdate ? '修改' : '新增'" @update:modelValue="updateModelValue">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="120px" @submit.prevent="">
      <el-form-item label="域名地址" prop="domain">
        <el-input v-model="form.domain" placeholder="请输入域名地址" clearable />
      </el-form-item>
      <el-form-item label="解析IP" prop="resolveIp">
        <el-input v-model="form.resolveIp" placeholder="请输入解析IP" clearable />
      </el-form-item>
      <el-form-item label="接入方式" prop="accessType">
        <el-select v-model="form.accessType" placeholder="请选择接入方式">
          <el-option label="域名托管" :value="1" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="form.status" placeholder="请选择状态">
          <el-option label="匹配中" :value="1" />
          <el-option label="正确匹配" :value="2" />
          <el-option label="匹配失败" :value="3" />
        </el-select>
      </el-form-item>
      <el-form-item label="显示状态" prop="visible">
        <el-radio-group v-model="form.visible">
          <el-radio :value="1">显示</el-radio>
          <el-radio :value="-1">隐藏</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" clearable />
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
  import { createCDominManageApi } from '@/api/app';

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
    domain: '',
    status: 1,
    visible: 1,
    accessType: 1,
    resolveIp: '',
    domainSource: 1,
    remark: ''
  });

  /** 表单验证规则 */
  const rules = reactive({
    domain: [{ required: true, message: '请输入域名地址', trigger: 'blur' }],
    resolveIp: [{ required: true, message: '请输入解析IP', trigger: 'blur' }],
    accessType: [{ required: true, message: '请选择接入方式', trigger: 'blur' }],
    status: [{ required: true, message: '请选择状态', trigger: 'blur' }],
    visible: [{ required: true, message: '请选择显示状态', trigger: 'blur' }],
    remark: [{ required: true, message: '请输入备注', trigger: 'blur' }]
  });

  /** 保存编辑 */
  const save = () => {
    formRef.value?.validate?.((valid) => {
      if (!valid) {
        return;
      }
      loading.value = true;
      const saveOrUpdate = isUpdate.value ? createCDominManageApi : createCDominManageApi;
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
