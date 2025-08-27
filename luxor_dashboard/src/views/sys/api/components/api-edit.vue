<!-- 机构编辑弹窗 -->
<template>
  <ele-modal form :width="620" :model-value="modelValue" :title="isUpdate ? '修改' : '新增'" @update:modelValue="updateModelValue">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="80px" @submit.prevent="">
      <el-row :gutter="16">
        <el-col :sm="18" :xs="24">
          <el-form-item label="路径" prop="path">
            <el-input clearable :maxlength="200" v-model="form.path" placeholder="请输入路径名称" />
          </el-form-item>
          <el-form-item label="api分组">
            <el-select v-model="form.apiGroup" allow-create filterable default-first-option>
              <el-option v-for="item in apiGroup" :key="item.key" :value="item.value" :label="item.value"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="请求类型">
            <el-select v-model="form.method">
              <el-option value="POST" label="创建(POST)" />
              <el-option value="GET" label="查看(GET)" />
              <el-option value="PUT" label="更新(PUT)" />
              <el-option value="DELETE" label="删除(DELETE)" />
            </el-select>
          </el-form-item>
          <el-form-item label="api简介" prop="description">
            <el-input clearable :maxlength="100" v-model="form.description" placeholder="请输入机构全称" />
          </el-form-item>
        </el-col>
      </el-row>
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
  import { addOrganization, updateOrganization } from '@/api/system/organization';
  import { createApi, updateApi } from '@/api/sys/api/index.js';

  const emit = defineEmits(['done', 'update:modelValue']);

  const props = defineProps({
    /** 弹窗是否打开 */
    modelValue: Boolean,
    /** 修改回显的数据 */
    data: Object,
    apiGroup: {
      type: Array,
      required: true
    }
  });

  /** 是否是修改 */
  const isUpdate = ref(false);

  /** 提交状态 */
  const loading = ref(false);

  /** 表单实例 */
  const formRef = ref(null);

  /** 表单数据 */
  const [form, resetFields, assignFields] = useFormData({
    ID: void 0,
    path: '',
    method: '',
    description: '',
    apiGroup: '',
    sortNumber: void 0
  });

  /** 表单验证规则 */
  const rules = reactive({
    organizationName: [
      {
        required: true,
        message: '请输入机构名称',
        type: 'string',
        trigger: 'blur'
      }
    ],
    organizationFullName: [
      {
        required: true,
        message: '请输入机构全称',
        type: 'string',
        trigger: 'blur'
      }
    ],
    organizationType: [
      {
        required: true,
        message: '请选择机构类型',
        type: 'string',
        trigger: 'blur'
      }
    ],
    sortNumber: [
      {
        required: true,
        message: '请输入排序号',
        type: 'number',
        trigger: 'blur'
      }
    ]
  });

  /** 保存编辑 */
  const save = () => {
    formRef.value?.validate?.((valid) => {
      if (!valid) {
        return;
      }
      loading.value = true;
      const saveOrUpdate = isUpdate.value ? updateApi : createApi;
      saveOrUpdate({ ...form })
        .then((msg) => {
          loading.value = false;
          EleMessage.success('新增成功');
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
        console.log(isUpdate.value)
      } else {
        resetFields();
        formRef.value?.clearValidate?.();
      }
    }
  );
</script>
