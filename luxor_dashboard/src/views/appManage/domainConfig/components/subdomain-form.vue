<template>
  <ele-modal form :width="550" :model-value="modelValue" :title="isView ? '查看子域名' : '添加子域名'" @update:modelValue="updateModelValue">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" @submit.prevent="">
      <div class="form-notice">
        <el-icon class="warning-icon"><QuestionCircleOutlined /></el-icon>
        <span class="notice-text">注意:前缀请勿输入大写字母,以防不过审!</span>
      </div>
      <el-form-item label="子域名前缀" prop="subDomains" required>
        <div class="subdomain-form">
          <div class="subdomain-list">
            <div v-for="(item, index) in form.subDomains" :key="index" class="subdomain-item">
              <el-input 
                v-model="form.subDomains[index]" 
                placeholder="请输入" 
                :rules="subdomainRules" 
                @blur="validateSubdomain(index)"
                :disabled="props.isView"
              />
              <div class="item-actions" v-if="!props.isView">
                <el-button 
                  v-if="form.subDomains.length < 3"
                  type="primary" 
                  :icon="Plus" 
                  circle 
                  size="small" 
                  @click="addSubdomain" 
                  title="添加" 
                />
                <el-button 
                  v-if="form.subDomains.length > 1" 
                  type="danger" 
                  :icon="Delete" 
                  circle 
                  size="small" 
                  @click="removeSubdomain(index)" 
                  title="删除" 
                />
              </div>
            </div>
          </div>

          <div v-if="form.subDomains.length === 0 && !isView" class="empty-state">
            <el-button type="primary" :icon="Plus" @click="addSubdomain">添加子域名前缀</el-button>
          </div>
          
          <!-- 查看模式下的空状态 -->
          <div v-if="form.subDomains.length === 0 && isView" class="empty-state">
            <span class="no-data">暂无子域名前缀</span>
          </div>
        </div>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="updateModelValue(false)">{{ isView ? '关闭' : '取消' }}</el-button>
      <el-button v-if="!isView" type="primary" :loading="loading" @click="save">保存</el-button>
    </template>
  </ele-modal>
</template>

<script setup>
  import { ref, reactive, watch } from 'vue';
  import { EleMessage } from 'ele-admin-plus/es';
  import { useFormData } from '@/utils/use-form-data';
  import { QuestionCircleOutlined } from '@/components/icons/index.js';
  import { createCSubDominManageApi } from '@/api/app';
  import { Plus, Delete } from '@element-plus/icons-vue';

  const emit = defineEmits(['done', 'update:modelValue']);

  const props = defineProps({
    /** 弹窗是否打开 */
    modelValue: Boolean,
    /** 修改回显的数据 */
    data: Object, 
    /** 是否是查看 */
    isView: Boolean
  });

  /** 提交状态 */
  const loading = ref(false);

  /** 表单实例 */
  const formRef = ref(null);

  /** 表单数据 */
  const [form, resetFields, assignFields] = useFormData({
    cDominId: null,
    subDomains: []
  });

  /** 子域名验证规则 */
  const subdomainRules = [
    { required: true, message: '请输入子域名前缀', trigger: 'blur' },
    {
      pattern: /^[a-z0-9-]+$/,
      message: '子域名前缀只能包含小写字母、数字和连字符',
      trigger: 'blur'
    },
    {
      validator: (rule, value, callback) => {
        if (value && value.length < 2) {
          callback(new Error('子域名前缀至少2个字符'));
        } else if (value && value.length > 63) {
          callback(new Error('子域名前缀不能超过63个字符'));
        } else {
          callback();
        }
      },
      trigger: 'blur'
    }
  ];

  /** 表单验证规则 */
  const rules = reactive({
    subDomains: [
      {
        validator: (rule, value, callback) => {
          if (!value || value.length === 0) {
            callback(new Error('请至少添加一个子域名前缀'));
            return;
          }

          // 检查是否有重复
          const uniqueValues = new Set(value.filter((item) => item && item.trim()));
          if (uniqueValues.size !== value.filter((item) => item && item.trim()).length) {
            callback(new Error('子域名前缀不能重复'));
            return;
          }

          // 检查每个子域名是否有效
          for (let i = 0; i < value.length; i++) {
            const item = value[i];
            if (!item || !item.trim()) {
              callback(new Error(`第${i + 1}个子域名前缀不能为空`));
              return;
            }

            if (!/^[a-z0-9-]+$/.test(item)) {
              callback(new Error(`第${i + 1}个子域名前缀只能包含小写字母、数字和连字符`));
              return;
            }

            if (item.length < 2) {
              callback(new Error(`第${i + 1}个子域名前缀至少2个字符`));
              return;
            }

            if (item.length > 63) {
              callback(new Error(`第${i + 1}个子域名前缀不能超过63个字符`));
              return;
            }
          }

          callback();
        },
        trigger: 'blur'
      }
    ]
  });

  /** 添加子域名 */
  const addSubdomain = () => {
    form.subDomains.push('');
  };

  /** 删除子域名 */
  const removeSubdomain = (index) => {
    form.subDomains.splice(index, 1);
  };

  /** 验证单个子域名 */
  const validateSubdomain = (index) => {
    // 查看模式下不进行验证和转换
    if (props.isView) {
      return;
    }
    
    const value = form.subDomains[index];
    if (!value || !value.trim()) {
      return;
    }
    
    // 自动转换为小写
    if (value !== value.toLowerCase()) {
      form.subDomains[index] = value.toLowerCase();
    }
  };

  /** 保存编辑 */
  const save = () => {
    // 查看模式下不执行保存操作
    if (props.isView) {
      return;
    }
    
    formRef.value?.validate?.((valid) => {
      if (!valid) {
        return;
      }
      
      // 过滤空值并转换为小写
      const cleanSubDomains = form.subDomains.filter((item) => item && item.trim()).map((item) => item.toLowerCase().trim());
      
      const submitData = {
        ...form,
        subDomains: cleanSubDomains
      };
      
      loading.value = true;
      createCSubDominManageApi(submitData)
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
        assignFields({
          cDominId: props.data.id,
          subDomains: props.data.subDomainList.length > 0 ? props.data.subDomainList.map((item) => item.subDomain) : ['']
        });
      } else {
        resetFields();
        formRef.value?.clearValidate?.();
      }
    }
  );
</script>

<style scoped>
  .subdomain-form {
    width: 100%;
  }

  .form-notice {
    display: flex;
    align-items: center;
    margin-bottom: 16px;
    padding: 8px 12px;
    background-color: #fff2e8;
    border: 1px solid #ffd591;
    border-radius: 4px;
  }

  .warning-icon {
    color: #fa8c16;
    margin-right: 8px;
    font-size: 14px;
  }

  .notice-text {
    color: #d46b08;
    font-size: 12px;
  }

  .subdomain-list {
    display: flex;
    flex-direction: column;
    gap: 12px;
  }

  .subdomain-item {
    display: flex;
    align-items: center;
    gap: 8px;
  }

  .subdomain-item .el-input {
    flex: 1;
  }

  .item-actions {
    display: flex;
    gap: 4px;
  }

  .empty-state {
    display: flex;
    justify-content: center;
    padding: 20px 0;
  }

  .no-data {
    color: #909399;
    font-size: 14px;
  }
</style>
