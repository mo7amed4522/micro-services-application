<template>
  <ele-modal form :width="750" :model-value="modelValue" :title="t('common.detailText')" @update:modelValue="updateModelValue">
    <el-form ref="formRef" :model="form" label-width="140px" @submit.prevent="">
      <el-form-item :label="t('merchant.channelName') + '：'">
        <span>{{ form.cPayConfigName }}</span>
      </el-form-item>
      <el-form-item :label="t('payMethod.bankName') + '：'">
        <span>{{ form.cardBank }}</span>
      </el-form-item>
      <el-form-item :label="t('payMethod.ifscCode') + '：'">
        <span>{{ form.ifsc }}</span>
      </el-form-item>
      <el-form-item :label="t('outOrder.payeeName') + '：'">
        <span>{{ form.cardName }}</span>
      </el-form-item>
      <el-form-item :label="t('outOrder.payeeCardNumber') + '：'">
        <span>{{ form.cardNumber }}</span>
      </el-form-item>
      <el-form-item :label="t('outOrder.actualTradeAmount') + '：'">
        <span>{{ form.actualAmount }}</span>
      </el-form-item>
      <el-form-item :label="t('channelConfig.channelType') + '：'">
        <el-tag v-if="form.cType == 1" type="success">{{ t('common.selfDevelop') }}</el-tag>
        <el-tag v-if="form.cType == 2" type="danger">{{ t('common.thirdParty') }}</el-tag>
      </el-form-item>
      <!-- <el-form-item :label="t('orderList.userName') + '：'">
        <span>{{ form.merSubUser }}</span>
      </el-form-item> -->
      <el-form-item :label="t('outOrder.currency') + '：'">
        <span>{{ form.currency }}</span>
      </el-form-item>
      <el-form-item :label="t('outOrder.orderRequestText') + '：'">
        <span style="white-space: pre-wrap; word-break: break-all">{{ form.orderBody }}</span>
      </el-form-item>
      <!-- <el-form-item :label="t('outOrder.orderDesc') + '：'">
        <span style="white-space: pre-wrap; word-break: break-all">{{ form.orderDesc }}</span>
      </el-form-item> -->
      <el-form-item :label="t('orderList.orderRemark') + '：'">
        <span style="white-space: pre-wrap; word-break: break-all">{{ form.remark }}</span>
      </el-form-item>
      <el-form-item :label="t('payMethod.type') + '：'">
        <span>{{ form.type == 0 ? t('outOrder.systemPayText') : t('outOrder.merchantPayText') }}</span>
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
    cPayConfigName: '',
    cardBank: '',
    ifsc: '',
    cardName: '',
    cardNumber: '',
    actualAmount: '',
    cType: '',
    currency: '',
    orderBody: '',
    orderDesc: '',
    remark: '',
    type: '',
    merSubUser: '',
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
