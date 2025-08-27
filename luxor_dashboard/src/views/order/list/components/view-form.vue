<template>
  <ele-modal form :width="750" :model-value="modelValue" :title="t('common.detailText')" @update:modelValue="updateModelValue">
    <el-form ref="formRef" :model="form" label-width="140px" @submit.prevent="">
      <el-form-item :label="t('orderList.poolName') + '：'">
        <span>{{ form.cPayPoolName }}</span>
      </el-form-item>
      <el-form-item :label="t('orderList.channelName') + '：'">
        <span>{{ form.cPayConfigName }}</span>
      </el-form-item>
      <el-form-item :label="t('orderList.userName') + '：'">
        <span>{{ form.merSubUser }}</span>
      </el-form-item>
      <el-form-item :label="t('utrOrder.paySource') + '：'">
        <span v-if="form.payType == 1">upi</span>
        <span v-if="form.payType == 2">bank</span>
        <span v-if="form.payType == 3">imps</span>
      </el-form-item>
      <el-form-item :label="t('channelConfig.channelType') + '：'">
        <el-tag v-if="form.cType == 1" type="success">{{ t('common.selfDevelop') }}</el-tag>
        <el-tag v-if="form.cType == 2" type="danger">{{ t('common.thirdParty') }}</el-tag>
      </el-form-item>
      <el-form-item :label="t('orderList.orderRemark') + '：'">
        <span style="white-space: pre-wrap; word-break: break-all">{{ form.remark }}</span>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="updateModelValue(false)">{{ t('common.close') }}</el-button>
    </template>
  </ele-modal>
</template>

<script setup>
  import { ref, reactive, watch } from 'vue';
  import { EleMessage } from 'ele-admin-plus/es';
  import { useFormData } from '@/utils/use-form-data';
  import { useI18n } from 'vue-i18n';

  const { t } = useI18n();

  const emit = defineEmits(['done', 'update:modelValue']);

  const props = defineProps({
    /** 弹窗是否打开 */
    modelValue: Boolean,
    /** 修改回显的数据 */
    data: Object
  });

  /** 表单实例 */
  const formRef = ref(null);

  /** 表单数据 */
  const [form, resetFields, assignFields] = useFormData({
    cPayPoolName: '',
    cPayConfigName: '',
    merSubUser: '',
    payType: '',
    cType: '',
    remark: ''
  });

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
        }
      } else {
        resetFields();
        formRef.value?.clearValidate?.();
      }
    }
  );
</script>
<style>
  .el-input-number .el-input-number__decrease {
    left: 1px !important;
  }
</style>
