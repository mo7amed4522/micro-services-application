<!-- 机构编辑弹窗 -->
<template>
  <ele-modal form :width="620" :model-value="modelValue" :title="isUpdate ? '新增' : '修改'" @update:modelValue="updateModelValue">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="80px" @submit.prevent="">
      <el-row :gutter="16">
        <el-col :sm="18" :xs="24">
          <el-form-item label="渠道名称" prop="name">
            <el-input clearable :maxlength="20" v-model="form.name" />
          </el-form-item>
          <el-form-item label="渠道code" prop="channelCode">
            <el-input clearable :maxlength="20" v-model="form.channelCode" />
          </el-form-item>
          <el-form-item label="商户号" prop="thirdMerchantNo">
            <el-input clearable :maxlength="20" v-model="form.thirdMerchantNo" />
          </el-form-item>
          <el-form-item label="请求地址" prop="thirdMerchantNo">
            <el-input clearable :maxlength="20" v-model="form.thirdUrl" />
          </el-form-item>
          <el-form-item label="状态" required>
            <el-switch v-model="form.valid" :active-value="1" :inactive-value="0" />
          </el-form-item>
          <el-form-item label="渠道类型" required>
            <el-radio-group v-model="form.channelType">
              <el-radio :value="1">自研</el-radio>
              <el-radio :value="2">三方</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="备注" required>
            <el-input v-model="form.remark" clearable />
          </el-form-item>
        </el-col>
      </el-row>

      <ele-card header="参数">
        <div style="overflow: auto">
          <ele-table style="table-layout: fixed">
            <colgroup>
              <col />
              <col />
              <col />
            </colgroup>
            <thead>
              <tr>
                <th style="width: 100%">key</th>
                <th style="width: 100%">value</th>
                <th style="width: 100%">操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(row, index) in form.params" :key="row.index">
                <td>
                  <el-form-item
                    label-width="0px"
                    :prop="'params.' + index + '.name'"
                    :rules="{
                      required: true,
                      message: '请输入值',
                      type: 'string'
                    }"
                    class="pro-form-error-popper"
                    style="margin-bottom: 0"
                  >
                    <el-input clearable v-model="row.name" placeholder="请输入值" />
                  </el-form-item>
                </td>
                <td>
                  <el-form-item
                    label-width="0px"
                    :prop="'params.' + index + '.value'"
                    :rules="{
                      required: true,
                      message: '请输入值',
                      type: 'string'
                    }"
                    class="pro-form-error-popper"
                    style="margin-bottom: 0"
                  >
                    <el-input clearable v-model="row.value" placeholder="请输入值" />
                  </el-form-item>
                </td>
                <td
                  :style="{
                    textAlign: 'center',
                    position: 'sticky',
                    right: 0,
                    zIndex: 98
                  }"
                >
                  <el-link type="danger" :underline="false" @click="removeUser(row, index)">删除</el-link>
                </td>
              </tr>
              <tr v-if="!form.params || !form.params.length">
                <td colspan="5" style="text-align: center">
                  <ele-text style="padding: 4px 0" type="secondary">暂无数据</ele-text>
                </td>
              </tr>
            </tbody>
          </ele-table>
        </div>
        <el-button :icon="PlusOutlined" style="margin-top: 16px; width: 100%" @click="addUser">
          <span style="padding-top: 1px">新增参数</span>
        </el-button>
      </ele-card>
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
  import { PlusOutlined } from '@/components/icons/index.js';

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
    name: '',
    channelCode: '',
    valid: 1,
    channelType: 1,
    thirdMerchantNo: '',
    third_merchant_no: '',
    third_public_key: '',
    third_url: '',
    params: []
  });

  /** 保存编辑 */
  const save = () => {
    console.log(form, 'form数据-------------------');
    formRef.value?.validate?.((valid) => {
      if (!valid) {
        return;
      }
      loading.value = true;
      const saveOrUpdate = isUpdate.value ? updateOrganization : addOrganization;
      saveOrUpdate({ ...form, parentId: form.parentId || 0 })
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

  /** 添加 */
  const addUser = () => {
    form.params.push({
      key: Date.now() + '-' + form.params.length,
      number: '',
      name: '',
      department: void 0
    });
  };

  /** 删除 */
  const removeUser = (_row, index) => {
    form.params.splice(index, 1);
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
            ...props.data,
            parentId: props.data.parentId || void 0
          });
          isUpdate.value = true;
        } else {
          form.parentId = props.organizationId;
          isUpdate.value = false;
        }
      } else {
        resetFields();
        formRef.value?.clearValidate?.();
      }
    }
  );
</script>
