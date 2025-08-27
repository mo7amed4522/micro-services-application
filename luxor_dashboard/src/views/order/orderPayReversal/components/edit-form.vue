<template>
  <ele-modal form :width="900" :model-value="modelValue" :title="t('utrOrder.addUtrOrder')" @update:modelValue="updateModelValue">
    <el-form ref="formRef" :model="form" label-width="140px" @submit.prevent="">
      <el-row :gutter="10" v-for="(item, index) in form" :key="index">
        <el-col :lg="12" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('utrOrder.channelAndPoolText')" :prop="'[' + index + '].cPayPoolId'">
            <el-select v-model="item.cPayPoolId" clearable :empty-values="[null, undefined]" :placeholder="t('utrOrder.selectChannelAndPool')" @change="getPayPoolAccountList($event, index)">
              <el-option v-for="(item, pIndex) in payPoolListOptions" :key="pIndex" :label="item.cPayPoolName + '(' + item.name + ')'" :value="item.cPayPoolId" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('common.accountText')" :prop="'[' + index + '].cPayPoolAccountId'">
            <el-select v-model="item.cPayPoolAccountId" filterable clearable :empty-values="[null, undefined]" :placeholder="t('utrOrder.selectBankCard')">
              <el-option v-for="(item, cardIndex) in bankCardOptions[index]" :key="cardIndex" :label="item.cardNumber + '(' + item.cPoolAccountName + ')'" :value="item.id" />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :lg="12" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('utrOrder.paySource')" :prop="'[' + index + '].sourceType'">
            <el-select clearable v-model="item.sourceType" :empty-values="[null, undefined]" :placeholder="t('utrOrder.selectPaySource')">
              <el-option v-for="(item, optIndex) in sourceTypeOptions" :key="optIndex" :label="item.name" :value="item.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('utrOrder.utrCode')" :prop="'[' + index + '].utr'">
            <el-input v-model="item.utr" :placeholder="t('utrOrder.enterUtrCode')" clearable />
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('utrOrder.utrAmount')" :prop="'[' + index + '].amount'">
            <el-input v-model="item.amount" :placeholder="t('utrOrder.enterUtrAmount')" clearable />
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="12" :xs="24">
          <el-form-item :label="t('common.notes')" :prop="'[' + index + '].remark'">
            <el-input v-model="item.remark" :placeholder="t('common.enterNotes')" clearable />
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="12" :xs="24">
          <el-form-item label="">
            <el-button type="primary" :loading="loading" @click="save(index)">{{ t('common.submitText') }}</el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button @click="updateModelValue(false)">{{ t('common.close') }}</el-button>
    </template>
  </ele-modal>
</template>

<script setup>
  import { ref, reactive, watch } from 'vue';
  import { EleMessage } from 'ele-admin-plus/es';
  import { getConfigAndPayPoolListApi, getPayPoolAccountListApi, createOrderPayReversalApi } from '@/api/order';
  import { useI18n } from 'vue-i18n';

  const { t } = useI18n();

  const emit = defineEmits(['done', 'update:modelValue']);

  const props = defineProps({
    modelValue: Boolean,
    data: Object
  });

  const sourceTypeOptions = ref([
    {
      id: 1,
      name: 'upi-QR'
    },
    {
      id: 2,
      name: 'upi-bank'
    },
    {
      id: 3,
      name: 'bank-bank'
    }
  ]);
  const loading = ref(false);
  const formRef = ref(null);

  // 初始化表单数据
  const form = ref(
    Array(8)
      .fill()
      .map(() => ({
        cPayPoolId: null,
        sourceType: null,
        amount: '',
        utr: '',
        remark: '',
        cPayPoolAccountId: null
      }))
  );

  const validateUTR = (rule, value, callback) => {
    if (!value) {
      callback(new Error(t('utrOrder.enterUtrCode')));
    } else if (!/^\d{12}$/.test(value)) {
      callback(new Error(t('utrOrder.utrCodeRule')));
    } else {
      callback(); // 验证通过
    }
  };

  /** 保存编辑 */
  const save = (index) => {
    // 校验当前行的所有字段
    const currentRow = form.value[index];
    const fieldProps = ['cPayPoolId', 'sourceType', 'amount', 'remark', 'utr', 'cPayPoolAccountId'];

    // 校验当前行的每个字段
    const validatePromises = fieldProps.map(
      (field) =>
        new Promise((resolve, reject) => {
          formRef.value?.validateField(`[${index}].${field}`, (valid) => {
            if (valid) {
              resolve();
            } else {
              reject(`${field} is invalid`);
            }
          });
        })
    );

    // 等待所有字段的验证
    Promise.all(validatePromises)
      .then(() => {
        currentRow.amount = currentRow.amount ? parseFloat(currentRow.amount) : 0;
        loading.value = true;
        // 提交当前行的数据
        createOrderPayReversalApi({
          ...currentRow // 提交当前行的数据
        })
          .then((msg) => {
            loading.value = false;
            EleMessage.success(msg);
            // updateModelValue(false);
            // 清空对应字段
            currentRow.utr = '';
            currentRow.amount = '';
            currentRow.remark = '';
            // 将form.value 存入缓存
            localStorage.setItem('utrForm', JSON.stringify(form.value));
            emit('done');
          })
          .catch((e) => {
            loading.value = false;
            EleMessage.error(e.message);
          });
      })
      .catch((error) => {
        console.log(error);
        // EleMessage.error(`验证失败: ${error}`);
      });
  };

  /** 更新modelValue */
  const updateModelValue = (value) => {
    emit('update:modelValue', value);
  };

  const payPoolListOptions = ref([]);

  const getConfigAndPayPoolList = async () => {
    const res = await getConfigAndPayPoolListApi();
    payPoolListOptions.value = res;
  };

  // bankCardOptions 初始化8个空数组
  const bankCardOptions = ref(Array(8).fill([]));

  const getPayPoolAccountList = async (cPayPoolId, index) => {
    form.value[index].cPayPoolAccountId = null;
    bankCardOptions.value[index] = [];
    const res = await getPayPoolAccountListApi({ cPayPoolId });
    bankCardOptions.value[index] = res;
  };

  watch(
    () => props.modelValue,
    async (modelValue) => {
      if (modelValue) {
        await getConfigAndPayPoolList();
        // 从缓存中获取utrForm, 如果存在则赋值给form
        const utrForm = localStorage.getItem('utrForm');
        if (utrForm) {
          form.value = JSON.parse(utrForm);
        }
      } else {
        // 重置表单
        form.value = Array(8)
          .fill()
          .map(() => ({
            cPayPoolId: null,
            sourceType: null,
            utr: '',
            amount: '',
            remark: '',
            cPayPoolAccountId: null
          }));
      }
    }
  );
</script>

<style>
  .el-input-number .el-input-number__decrease {
    left: 1px !important;
  }
  .small-width {
    width: 110px;
  }

  .large-width {
    width: 100%;
  }
</style>
