<template>
  <ele-modal form :width="650" :model-value="modelValue" :title="isUpdate ? t('common.edit') : t('common.add')" @update:modelValue="updateModelValue">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="140px" @submit.prevent="">
      <el-form-item :label="t('payPool.selectMerchant')" prop="mUserId">
        <el-select clearable v-model="form.mUserId" :empty-values="[null, undefined]" :placeholder="t('payPool.enterMerchant')" class="ele-fluid">
          <el-option v-for="(item, index) in mUserList" :key="index" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('payPool.selectChannel')" prop="cPayConfigId">
        <el-select clearable v-model="form.cPayConfigId" :placeholder="t('payPool.enterChannel')" class="ele-fluid">
          <el-option v-for="(item, index) in cPayConfigList" :key="index" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('payPool.poolName')" prop="name">
        <el-input v-model="form.name" :placeholder="t('payPool.enterPoolName')" clearable />
      </el-form-item>
      <el-form-item :label="t('payPool.poolCode')" prop="code">
        <el-input v-model="form.code" :placeholder="t('payPool.enterPoolCode')" clearable />
      </el-form-item>
      <el-form-item :label="t('payPool.singleMinAmount')" prop="limitAmountMin">
        <el-input-number v-model="form.limitAmountMin" :precision="2" :step="0.1" />
      </el-form-item>
      <el-form-item :label="t('payPool.singleMaxAmount')" prop="limitAmountMax">
        <el-input-number v-model="form.limitAmountMax" :precision="2" :step="0.1" />
      </el-form-item>
      <!-- <el-form-item label="限制交易总金额" prop="limitAmount">
        <el-input-number v-model="form.limitAmount" :precision="2" :step="0.1" />
      </el-form-item>
      <el-form-item label="限制每天交易金额" prop="limitAmountDay">
        <el-input-number v-model="form.limitAmountDay" :precision="2" :step="0.1" />
      </el-form-item>
      
      <el-form-item label="每天请求次数" prop="limitCountRequestDay">
        <el-input-number v-model="form.limitCountRequestDay" :step="1" />
      </el-form-item> -->
      <!-- <el-form-item label="每天实际完成次数" prop="limitCountUsedDay">
        <el-input-number v-model="form.limitCountUsedDay" :step="1" />
      </el-form-item>
      <el-form-item label="已经支付总金额" prop="totalAmount">
        <el-input-number v-model="form.totalAmount" :precision="2" :step="0.1" />
      </el-form-item> -->
      <el-form-item :label="t('common.notes')" prop="remark">
        <el-input v-model="form.remark" type="textarea" :rows="3" :placeholder="t('common.enterNotes')" clearable />
      </el-form-item>
      <el-form-item :label="t('common.status')" prop="valid">
        <el-radio-group v-model="form.valid">
          <el-radio :value="0">{{ t('common.close') }}</el-radio>
          <el-radio :value="1">{{ t('common.open') }}</el-radio>
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
  import { createCPayPoolApi, updateCPayPoolApi, getMUserList } from '@/api/merchant';
  import { getCPayConfigListApi } from '@/api/payConfig';
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
    cPayConfigId: null,
    mUserId: null,
    name: null,
    code: null,
    limitAmount: 0,
    limitAmountDay: 0,
    limitAmountMax: 0,
    limitAmountMin: 0,
    limitCountRequestDay: 0,
    limitCountUsedDay: 0,
    limitAmountMax: 0,
    limitAmountMin: 0,
    remark: null,
    totalAmount: 0,
    valid: 1
  });

  /** 表单验证规则 */
  const rules = reactive({
    // mUserId: [{ required: true, message: '请选择商户', trigger: 'change' }],
    cPayConfigId: [{ required: true, message: t('payPool.enterChannel'), trigger: 'change' }],
    name: [{ required: true, message: t('payPool.enterPoolName'), trigger: 'blur' }],
    code: [{ required: true, message: t('payPool.enterPoolCode'), trigger: 'blur' }],
    limitAmount: [{ required: true, message: '请输入限制交易总金额', trigger: 'blur' }],
    limitAmountDay: [{ required: true, message: '请输入限制每天交易金额', trigger: 'blur' }],
    limitAmountMin: [{ required: true, message: '请输入限制单笔最小金额', trigger: 'blur' }],
    limitAmountMax: [{ required: true, message: '请输入限制单笔最大金额', trigger: 'blur' }],
    limitCountRequestDay: [{ required: true, message: '请输入限制每天请求次数', trigger: 'blur' }],
    limitCountUsedDay: [{ required: true, message: '请输入限制每天使用次数', trigger: 'blur' }],
    totalAmount: [{ required: true, message: '请输入已经支付总金额', trigger: 'blur' }]
  });

  /** 保存编辑 */
  const save = () => {
    formRef.value?.validate?.((valid) => {
      if (!valid) {
        return;
      }
      loading.value = true;
      const saveOrUpdate = isUpdate.value ? updateCPayPoolApi : createCPayPoolApi;
      saveOrUpdate({
        ...form
      })
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

  const cPayConfigList = ref([]);

  const getCPayConfigList = async () => {
    const res = await getCPayConfigListApi({
      status: 1,
      type: 1,
      chanelType: 1,
      page: 1,
      pageSize: 9999
    });
    cPayConfigList.value = res.list;
  };

  const mUserList = ref([]);

  const getMUserListData = async () => {
    const res = await getMUserList({
      page: 1,
      pageSize: 9999
    });
    mUserList.value = res.list;
    mUserList.value = [{ id: 0, name: '取消绑定' }, ...res.list];
    console.log(mUserList.value);
  };

  watch(
    () => props.modelValue,
    (modelValue) => {
      if (modelValue) {
        getCPayConfigList();
        getMUserListData();
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
<style>
  .el-input-number .el-input-number__decrease {
    left: 1px !important;
  }
</style>
