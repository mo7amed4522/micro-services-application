<template>
  <ele-modal form :width="550" :model-value="modelValue" :title="isUpdate ? '修改模版' : '新增模版'" @update:modelValue="updateModelValue">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="120px" @submit.prevent="">
      <el-form-item label="所属应用" prop="appId">
        <el-select v-model="form.appId" :empty-values="[null, undefined]" placeholder="请选择" class="ele-fluid">
          <el-option v-for="item in appList" :key="item.id" :label="item.appName" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="所属域名" prop="domainId">
        <el-select v-model="form.domainId" :empty-values="[null, undefined]" placeholder="请选择" class="ele-fluid">
          <el-option v-for="item in domainList" :key="item.id" :label="item.domain" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="所属模版" prop="templateId">
        <el-select v-model="form.templateId" :empty-values="[null, undefined]" placeholder="请选择" class="ele-fluid">
          <el-option v-for="item in templateList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="链接名称" prop="name">
        <el-input v-model="form.name" placeholder="请输入" clearable />
      </el-form-item>
      <el-form-item label="ios跳转url" prop="iosRedirectUrl">
        <el-input v-model="form.iosRedirectUrl" placeholder="请输入" clearable />
      </el-form-item>
      <el-form-item label="包网下载地址" prop="url">
        <el-input v-model="form.url" placeholder="请输入" clearable />
      </el-form-item>
      <el-form-item label="允许修改url" prop="isAllowEditUrl">
        <el-radio-group v-model="form.isAllowEditUrl">
          <el-radio :value="1" label="允许"></el-radio>
          <el-radio :value="-1" label="不允许"></el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input v-model="form.remark" type="textarea" placeholder="请输入" clearable />
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
  import { createCPromotionLinkApi, updateCPromotionLinkApi } from '@/api/app';

  const emit = defineEmits(['done', 'update:modelValue']);

  const props = defineProps({
    /** 弹窗是否打开 */
    modelValue: Boolean,
    /** 修改回显的数据 */
    data: Object,
    appList: {
      type: Array,
      default: () => []
    },
    domainList: {
      type: Array,
      default: () => []
    },
    templateList: {
      type: Array,
      default: () => []
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
    id: void 0,
    name: null,
    appId: null,
    templateId: null,
    url: null,
    isAllowEditUrl: 1,
    iosRedirectUrl: null,
    domainId: null,
    remark: null
  });

  /** 表单验证规则 */
  const rules = reactive({
    name: [{ required: true, message: '请输入链接名称', trigger: 'blur' }],
    appId: [{ required: true, message: '请选择所属应用', trigger: 'change' }],
    templateId: [{ required: true, message: '请选择所属模版', trigger: 'change' }],
    url: [{ required: true, message: '请输入包网下载地址', trigger: 'blur' }],
    isAllowEditUrl: [{ required: true, message: '请选择是否允许修改url', trigger: 'blur' }],
    iosRedirectUrl: [{ required: true, message: '请输入ios跳转url', trigger: 'blur' }],
    domainId: [{ required: true, message: '请选择所属域名', trigger: 'change' }],
  });

  /** 保存编辑 */
  const save = () => {
    formRef.value?.validate?.((valid) => {
      if (!valid) {
        return;
      }
      loading.value = true;
      const saveOrUpdate = isUpdate.value ? updateCPromotionLinkApi : createCPromotionLinkApi;
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
