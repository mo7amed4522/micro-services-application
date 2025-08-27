<!-- 编辑弹窗 -->
<template>
  <div>
    <ele-drawer
      :model-value="modelValue"
      :title="isUpdate ? t('common.edit') : t('common.add')"
      :append-to-body="true"
      :size="825"
      style="max-width: 100%; background-color: #f5f7fa"
      @update:modelValue="updateModelValue"
    >
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="110px">
        <!-- APP基本信息卡片 -->
        <el-card class="form-card mb-6">
          <template #header>
            <div class="card-header">
              <div class="card-title">APP基本信息</div>
              <el-button type="success" size="small">
                <el-icon class="mr-1"><Download /></el-icon>
                导入已有APP配置
              </el-button>
            </div>
          </template>

          <div class="card-content">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="APP名称" prop="appName" required class="el-form-item--label-top">
                  <el-input v-model="form.appName" placeholder="请输入APP名称" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="备注" prop="appRemark" class="el-form-item--label-top">
                  <el-input v-model="form.appRemark" placeholder="请输入备注" />
                </el-form-item>
              </el-col>
            </el-row>
            <!-- APP图标上传 -->
            <el-col :span="12">
              <el-form-item label="APP图标" prop="appIcon" class="el-form-item--label-top">
                <image-upload :limit="1" v-model="form.appIcon" :item-style="{ margin: 0 }" :button-style="{ margin: 0 }" />
              </el-form-item>
            </el-col>
          </div>
        </el-card>

        <!-- 商店语言卡片 -->
        <el-card class="form-card mb-6">
          <template #header>
            <div class="card-header">
              <div class="card-title">商店语言</div>
            </div>
          </template>

          <div class="card-content">
            <div class="language-grid">
              <el-checkbox v-model="form.languages.english" label="英语" :disabled="true" />
              <el-checkbox v-model="form.languages.thai" label="泰语" />
              <el-checkbox v-model="form.languages.chinese" label="中文" />
            </div>
          </div>
        </el-card>

        <!-- 商店信息卡片 -->
        <el-card class="form-card mb-6">
          <template #header>
            <div class="card-header">
              <div class="card-title">商店信息</div>
            </div>
          </template>

          <div class="card-content">
            <!-- 语言切换标签 -->
            <div class="mb-4">
              <div class="language-tabs">
                <el-tag
                  v-for="lang in getAvailableLanguages()"
                  :key="lang"
                  :type="currentLanguage === lang ? 'success' : 'info'"
                  size="large"
                  :class="{ 'language-tab': true, active: currentLanguage === lang }"
                  @click="switchLanguage(lang)"
                >
                  {{ getLanguageName(lang) }}
                  <span v-if="lang === 'english'" class="ml-1 text-xs">(默认)</span>
                </el-tag>
              </div>
              <div class="mt-2 text-sm text-gray-500">
                <el-icon class="mr-1"><InfoFilled /></el-icon>
                点击切换编辑语言
              </div>
            </div>

            <!-- APP名称 -->
            <el-form-item label="APP名称" :prop="`languageData.${currentLanguage}.appName`" required class="el-form-item--label-top">
              <el-input v-model="form.languageData[currentLanguage].appName" placeholder="请输入APP名称" />
            </el-form-item>

            <!-- APP图标上传 -->
            <el-form-item label="APP图标" :prop="`languageData.${currentLanguage}.appIcon`" class="el-form-item--label-top">
              <image-upload :limit="1" v-model="form.languageData[currentLanguage].appIcon" :item-style="{ margin: 0 }" :button-style="{ margin: 0 }" />
            </el-form-item>

            <!-- 公司名称和APP下载数 -->
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="公司名称" :prop="`languageData.${currentLanguage}.companyName`" required class="el-form-item--label-top">
                  <el-input v-model="form.languageData[currentLanguage].companyName" placeholder="请输入商店的公司名称" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="APP下载数" :prop="`languageData.${currentLanguage}.appDownloads`" required class="el-form-item--label-top">
                  <el-input v-model="form.languageData[currentLanguage].appDownloads" placeholder="请输入该APP在商店的下载数" />
                </el-form-item>
              </el-col>
            </el-row>

            <!-- APP评论数和适用年龄 -->
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="APP评论数" :prop="`languageData.${currentLanguage}.appRating`" required class="el-form-item--label-top">
                  <el-input v-model="form.languageData[currentLanguage].appRating" placeholder="请输入商店评论数" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="适用年龄" :prop="`languageData.${currentLanguage}.ageRating`" class="el-form-item--label-top">
                  <el-select v-model="form.languageData[currentLanguage].ageRating" placeholder="暂不设置" style="width: 100%">
                    <el-option label="暂不设置" value="" />
                    <el-option label="3+" value="3+" />
                    <el-option label="7+" value="7+" />
                    <el-option label="12+" value="12+" />
                    <el-option label="17+" value="17+" />
                    <el-option label="18+" value="18+" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <!-- APP简介 -->
            <el-form-item label="APP简介" :prop="`languageData.${currentLanguage}.appDescription`" required class="el-form-item--label-top">
              <el-input v-model="form.languageData[currentLanguage].appDescription" type="textarea" :rows="6" placeholder="请输入APP简介" maxlength="5000" show-word-limit />
            </el-form-item>

            <!-- APP详情图上传 -->
            <el-form-item label="请上传APP详情图" class="el-form-item--label-top" label-width="130px">
              <image-upload :limit="10" v-model="form.languageData[currentLanguage].appDetailImages" :item-style="{ margin: '0 8px 8px 0' }" :button-style="{ margin: 0 }" />
            </el-form-item>

            <!-- 添加APP标签 -->
            <div class="app-tags-section">
              <div class="tags-header">
                <span class="tags-label">
                  <span class="required-star">*</span>
                  添加APP标签
                </span>
              </div>

              <div class="tag-input-container">
                <el-input v-model="tagInput" placeholder="请输入标签" @keyup.enter="addTag" class="tag-input" />
                <el-button type="success" @click="addTag">添加</el-button>
                <el-button @click="clearTags">清空</el-button>
              </div>

              <div class="tag-display-area">
                <el-tag v-for="(tag, index) in form.languageData[currentLanguage].appTags" :key="index" closable @close="removeTag(index)" class="tag-item" type="info">
                  {{ tag }}
                </el-tag>
                <div v-if="form.languageData[currentLanguage].appTags.length === 0" class="tag-empty-tip">暂无标签，请在上方输入并添加标签</div>
                <div class="tag-count">{{ form.languageData[currentLanguage].appTags.length }}/20</div>
              </div>
            </div>

            <!-- 打分设置 -->
            <div class="rating-section">
              <div class="rating-header">
                <span class="rating-label">
                  <span class="required-star">*</span>
                  打分设置
                </span>
              </div>

              <div class="rating-container">
                <div class="final-score">
                  <div class="score-number">{{ finalScore }}</div>
                  <div class="score-label">最终得分</div>
                </div>
                <div class="rating-sliders">
                  <div v-for="score in [5, 4, 3, 2, 1]" :key="score" class="rating-row">
                    <span class="rating-score">{{ score }}</span>
                    <el-slider v-model="form.languageData[currentLanguage].ratingDistribution[score]" :min="0" :max="100" :step="1" class="rating-slider" @change="updateFinalScore" />
                  </div>
                </div>
              </div>
            </div>

            <!-- 评论区设置 -->
            <div class="comment-section-wrapper">
              <div class="comment-header">
                <span class="comment-label">
                  <span class="required-star">*</span>
                  评论区设置
                </span>
              </div>

              <div class="comment-section">
                <!-- 已添加的评论列表 -->
                <div v-if="form.languageData[currentLanguage].comments.length > 0" class="comments-list">
                  <div v-for="(comment, index) in form.languageData[currentLanguage].comments" :key="index" class="comment-item">
                    <div class="comment-row">
                      <div class="comment-avatar-display">
                        <image-upload :limit="1" v-model="comment.avatar" :multiple="false" :item-style="{ margin: '0 8px 8px 0' }" :button-style="{ margin: 0 }" />
                      </div>
                      <div class="comment-right">
                        <div class="comment-inputs-display">
                          <el-input v-model="comment.reviewerName" placeholder="请输入评论者名称" />
                          <el-input v-model="comment.rating" placeholder="5" style="width: 80px" />
                        </div>
                        <el-input v-model="comment.content" type="textarea" :rows="4" placeholder="请输入评论内容" />
                      </div>
                      <div class="comment-actions">
                        <el-button type="danger" size="small" @click="removeComment(index)" class="delete-btn">×</el-button>
                      </div>
                    </div>
                  </div>
                </div>

                <!-- 添加评论按钮 - 始终在最下面 -->
                <div class="add-comment-section">
                  <el-button type="success" @click="addComment" class="add-comment-btn">
                    <el-icon><Plus /></el-icon>
                    添加评论
                  </el-button>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </el-form>
      <template #footer>
        <el-button @click="updateModelValue(false)">{{ t('common.cancelText') }}</el-button>
        <el-button type="primary" :loading="loading" @click="save">{{ t('common.saveText') }}</el-button>
      </template>
    </ele-drawer>
  </div>
</template>

<script setup>
  import { ref, reactive, watch, computed, nextTick, onMounted } from 'vue';
  import { EleMessage, isExternalLink } from 'ele-admin-plus/es';
  import { useFormData } from '@/utils/use-form-data';
  import { createApp, updateApp } from '@/api/manage';
  import { ElMessage } from 'element-plus';
  import { useI18n } from 'vue-i18n';
  import { Plus, Download, InfoFilled } from '@element-plus/icons-vue';
  import ImageUpload from '@/components/ImageUpload/index.vue';

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

  const isEnable = ref(false);

  /** 表单实例 */
  const formRef = ref(null);

  /** 表单数据 */
  const [form, resetFields, assignFields] = useFormData({
    id: void 0,
    enable: 1,
    appIcon: '',
    appName: '',
    appRemark: '',
    // 语言设置
    defaultLanguage: 'english',
    languages: {
      english: true,
      thai: false,
      chinese: false
    },
    // 多语言数据结构
    languageData: {
      english: {
        appName: '',
        appIcon: '',
        companyName: '',
        appDownloads: '',
        appRating: '',
        ageRating: '',
        appDescription: '',
        appDetailImages: '',
        appTags: [],
        ratingDistribution: {
          5: 80,
          4: 20,
          3: 5,
          2: 3,
          1: 10
        },
        comments: [
          {
            reviewerName: '',
            rating: '5',
            content: '',
            avatar: '',
            id: Date.now()
          }
        ]
      },
      thai: {
        appName: '',
        appIcon: '',
        companyName: '',
        appDownloads: '',
        appRating: '',
        ageRating: '',
        appDescription: '',
        appDetailImages: '',
        appTags: [],
        ratingDistribution: {
          5: 80,
          4: 20,
          3: 5,
          2: 3,
          1: 10
        },
        comments: [
          {
            reviewerName: '',
            rating: '5',
            content: '',
            avatar: '',
            id: Date.now()
          }
        ]
      },
      chinese: {
        appName: '',
        appIcon: '',
        companyName: '',
        appDownloads: '',
        appRating: '',
        ageRating: '',
        appDescription: '',
        appDetailImages: '',
        appTags: [],
        ratingDistribution: {
          5: 80,
          4: 20,
          3: 5,
          2: 3,
          1: 10
        },
        comments: [
          {
            reviewerName: '',
            rating: '5',
            content: '',
            avatar: '',
            id: Date.now()
          }
        ]
      }
    }
  });

  // 标签输入
  const tagInput = ref('');

  // 当前编辑语言
  const currentLanguage = ref('english');

  // 语言名称映射
  const languageNames = {
    english: '英语',
    thai: '泰语',
    chinese: '中文'
  };

  // 计算最终得分
  const finalScore = ref('3.6');

  // 动态验证规则 - 只验证当前语言
  const getFormRules = () => {
    const rules = {};
    const currentLang = currentLanguage.value;

    // 只为当前编辑的语言添加验证规则
    rules[`languageData.${currentLang}.appName`] = [{ required: true, message: '请输入APP名称', trigger: 'blur' }];
    rules[`languageData.${currentLang}.companyName`] = [{ required: true, message: '请输入公司名称', trigger: 'blur' }];
    rules[`languageData.${currentLang}.appDownloads`] = [{ required: true, message: '请输入APP下载数', trigger: 'blur' }];
    rules[`languageData.${currentLang}.appRating`] = [{ required: true, message: '请输入APP评论数', trigger: 'blur' }];
    rules[`languageData.${currentLang}.appDescription`] = [{ required: true, message: '请输入APP简介', trigger: 'blur' }];

    return rules;
  };

  // 使用ref存储当前的验证规则
  const formRules = ref(getFormRules());

  // 更新最终得分
  const updateFinalScore = () => {
    const distribution = form.languageData[currentLanguage.value].ratingDistribution;
    const total = Object.values(distribution).reduce((sum, val) => sum + val, 0);

    if (total === 0) {
      finalScore.value = '0.0';
      return;
    }

    const weightedSum = Object.entries(distribution).reduce((sum, [score, percentage]) => {
      return sum + parseInt(score) * percentage;
    }, 0);

    finalScore.value = (weightedSum / total).toFixed(1);
  };

  // 初始化时计算一次
  nextTick(() => {
    updateFinalScore();
  });

  // 语言切换相关方法
  const getAvailableLanguages = () => {
    const languages = ['english'];
    if (form.languages.thai) languages.push('thai');
    if (form.languages.chinese) languages.push('chinese');
    return languages;
  };

  const getLanguageName = (langKey) => {
    return languageNames[langKey];
  };

  const switchLanguage = (langKey) => {
    if (currentLanguage.value === langKey) return; // 如果点击的是当前语言，不做任何操作

    currentLanguage.value = langKey;
    // 更新验证规则
    formRules.value = getFormRules();
    // 切换语言后重新计算最终得分
    updateFinalScore();
    // 清除验证状态，让新的验证规则生效
    nextTick(() => {
      if (formRef.value?.clearValidate) {
        formRef.value.clearValidate();
      }
    });
  };

  // 标签操作方法
  const addTag = () => {
    const trimmedTag = tagInput.value.trim();
    const currentTags = form.languageData[currentLanguage.value].appTags;
    if (trimmedTag && currentTags.length < 20 && !currentTags.includes(trimmedTag)) {
      currentTags.push(trimmedTag);
      tagInput.value = '';
    } else if (!trimmedTag) {
      ElMessage.warning('请输入标签内容');
    } else if (currentTags.length >= 20) {
      ElMessage.warning('最多只能添加20个标签');
    } else if (currentTags.includes(trimmedTag)) {
      ElMessage.warning('该标签已存在');
    }
  };

  const removeTag = (index) => {
    const currentTags = form.languageData[currentLanguage.value].appTags;
    currentTags.splice(index, 1);
  };

  const clearTags = () => {
    const currentTags = form.languageData[currentLanguage.value].appTags;
    if (currentTags.length > 0) {
      form.languageData[currentLanguage.value].appTags = [];
    }
  };

  // 评论操作方法
  const addComment = () => {
    form.languageData[currentLanguage.value].comments.push({
      reviewerName: '',
      rating: '5',
      content: '',
      avatar: '',
      id: Date.now()
    });
  };

  const removeComment = (index) => {
    const currentComments = form.languageData[currentLanguage.value].comments;
    currentComments.splice(index, 1);
  };

  /** 保存编辑 */
  const save = () => {
    // 验证所有启用的语言
    const enabledLanguages = Object.keys(form.languages).filter((lang) => form.languages[lang]);
    let hasError = false;
    let errorMessage = '';

    for (const lang of enabledLanguages) {
      const langData = form.languageData[lang];
      const langName = languageNames[lang];

      if (!langData.appName?.trim()) {
        hasError = true;
        errorMessage = `请完善${langName}的APP名称`;
        break;
      }
      if (!langData.companyName?.trim()) {
        hasError = true;
        errorMessage = `请完善${langName}的公司名称`;
        break;
      }
      if (!langData.appDownloads?.trim()) {
        hasError = true;
        errorMessage = `请完善${langName}的APP下载数`;
        break;
      }
      if (!langData.appRating?.trim()) {
        hasError = true;
        errorMessage = `请完善${langName}的APP评论数`;
        break;
      }
      if (!langData.appDescription?.trim()) {
        hasError = true;
        errorMessage = `请完善${langName}的APP简介`;
        break;
      }
    }

    if (hasError) {
      ElMessage.error(errorMessage);
      return;
    }

    formRef.value?.validate?.((valid) => {
      if (!valid) {
        console.log('当前语言验证失败');
        return;
      }
      loading.value = true;

      const saveData = {
        ...form,
        languageJson: JSON.stringify(form.languageData)
      };

      console.log('保存数据:', saveData);

      const saveOrUpdate = isUpdate.value ? updateApp : createApp;
      saveOrUpdate(saveData)
        .then((res) => {
          loading.value = false;
          EleMessage.success(res.msg);
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
    if (!value) {
      isUpdate.value = false; // 关闭弹窗时重置 isUpdate
    }
  };

  watch(
    () => props.modelValue,
    (modelValue) => {
      if (modelValue) {
        if (props.data) {
          // 确保字段正确初始化
          const dataWithDefaults = {
            ...props.data,
            languages: {
              english: true,
              thai: false,
              chinese: false,
              ...props.data.languages
            },
            languageData: JSON.parse(props.data.languageJson)
          };
          assignFields(dataWithDefaults);

          Object.keys(form.languageData).forEach((langKey) => {
            if (form.languageData[langKey].appName) {
              form.languages[langKey] = true;
            }
          });
          isUpdate.value = true;
          // 数据更新后重新计算最终得分
          nextTick(() => {
            updateFinalScore();
          });

          // 设置当前语言
          currentLanguage.value = 'english';
        } else {
          isUpdate.value = false;
          // 确保新增模式下也有默认评论
          Object.keys(form.languageData).forEach((langKey) => {
            if (form.languageData[langKey].comments.length === 0) {
              form.languageData[langKey].comments.push({
                reviewerName: '',
                rating: '5',
                content: '',
                avatar: '',
                id: Date.now()
              });
            }
          });

          // 设置当前语言
          currentLanguage.value = 'english';
        }
      } else {
        resetFields();
        formRef.value?.clearValidate?.();
        currentLanguage.value = 'english';
      }
    },
    { immediate: true }
  );

  // 监听语言切换
  watch(
    () => currentLanguage.value,
    () => {
      // 更新验证规则
      formRules.value = getFormRules();
      updateFinalScore();
      // 延迟清除验证状态，避免频繁触发
      nextTick(() => {
        if (formRef.value?.clearValidate) {
          formRef.value.clearValidate();
        }
      });
    }
  );

  // 监听语言设置，确保英语永远选中
  watch(
    () => form.languages.english,
    (newVal) => {
      if (!newVal) {
        // 使用nextTick避免循环触发
        nextTick(() => {
          form.languages.english = true;
        });
      }
    }
  );
</script>

<style scoped>
  .el-card.is-always-shadow {
    box-shadow: none;
  }

  /* 卡片样式 */
  .form-card {
    margin-bottom: 24px;
    background: #fff;
  }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0;
  }

  .card-title {
    font-size: 18px;
    font-weight: 600;
    color: #303133;
  }

  .card-content {
    padding: 20px 0 0 0;
  }

  /* 语言网格布局 */
  .language-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 16px 24px;
    margin-top: 10px;
  }

  /* 工具类 */
  .flex {
    display: flex;
  }

  .justify-between {
    justify-content: space-between;
  }

  .items-center {
    align-items: center;
  }

  .flex-col {
    flex-direction: column;
  }

  .mb-4 {
    margin-bottom: 1rem;
  }

  .mb-6 {
    margin-bottom: 1.5rem;
  }

  .ml-1 {
    margin-left: 0.25rem;
  }

  .ml-2 {
    margin-left: 0.5rem;
  }

  .mr-1 {
    margin-right: 0.25rem;
  }

  .mr-2 {
    margin-right: 0.5rem;
  }

  .mt-2 {
    margin-top: 0.5rem;
  }

  .text-sm {
    font-size: 0.875rem;
  }

  .text-gray-500 {
    color: #6b7280;
  }

  .text-center {
    text-align: center;
  }

  /* 表单项样式 */
  :deep(.el-form-item--label-top .el-form-item__label) {
    padding: 0 0 8px 0;
    font-weight: 500;
    color: #606266;
  }

  /* 卡片头部样式覆盖 */
  :deep(.el-card__header) {
    padding: 20px 24px;
    border-bottom: 1px solid #f0f0f0;
    background: #fafafa;
  }

  :deep(.el-card__body) {
    padding: 24px;
  }

  /* 上传组件样式 */
  .image-uploader :deep(.el-upload) {
    border: 1px dashed var(--el-border-color);
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: var(--el-transition-duration-fast);
    width: 80px;
    height: 80px;
  }

  .image-uploader :deep(.el-upload:hover) {
    border-color: var(--el-color-primary);
  }

  .detail-image-uploader :deep(.el-upload) {
    border: 1px dashed var(--el-border-color);
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: var(--el-transition-duration-fast);
    width: 80px;
    height: 80px;
  }

  .detail-image-uploader :deep(.el-upload:hover) {
    border-color: var(--el-color-primary);
  }

  /* 调整上传组件内部元素大小 */
  .image-uploader :deep(.flex) {
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
  }

  .image-uploader :deep(.el-icon) {
    font-size: 16px !important;
  }

  .image-uploader :deep(.mt-2) {
    margin-top: 4px !important;
    font-size: 10px !important;
    line-height: 12px !important;
  }

  .detail-image-uploader :deep(.flex) {
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
  }

  .detail-image-uploader :deep(.el-icon) {
    font-size: 16px !important;
  }

  /* 复选框样式优化 */
  :deep(.el-checkbox) {
    margin-right: 0;
    white-space: nowrap;
  }

  :deep(.el-checkbox__label) {
    color: #606266;
    font-size: 14px;
  }

  :deep(.el-checkbox__input.is-checked .el-checkbox__inner) {
    background-color: #67c23a;
    border-color: #67c23a;
  }

  /* 标签相关样式 */
  .app-tags-section {
    margin-bottom: 20px;
  }

  .tags-header {
    margin-bottom: 8px;
  }

  .tags-label {
    font-size: 14px;
    color: #606266;
    font-weight: 500;
    line-height: 1.5;
  }

  .required-star {
    color: #f56565;
    margin-right: 4px;
  }

  .tag-input-container {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-bottom: 12px;
  }

  .tag-input {
    flex: 1;
  }

  .tag-input-container .el-button {
    height: 32px;
    padding: 8px 15px;
  }

  .tag-display-area {
    min-height: 120px;
    background: #f5f7fa;
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    padding: 12px 12px 30px 12px;
    position: relative;
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
    align-content: flex-start;
    overflow: hidden;
  }

  .tag-item {
    margin: 0;
    background: #fff;
    border: 1px solid #e4e7ed;
  }

  .tag-empty-tip {
    color: #c0c4cc;
    font-size: 14px;
    text-align: center;
    width: 100%;
    line-height: 1.8;
    margin-top: 20px;
  }

  .tag-count {
    position: absolute;
    bottom: 8px;
    right: 12px;
    color: #909399;
    font-size: 12px;
    font-weight: 500;
  }

  /* 评分设置样式 */
  .rating-section {
    margin-bottom: 20px;
  }

  .rating-header {
    margin-bottom: 20px;
  }

  .rating-label {
    font-size: 14px;
    color: #606266;
    font-weight: 500;
    line-height: 1.5;
  }

  .rating-container {
    display: flex;
    gap: 60px;
    align-items: center;
  }

  .final-score {
    text-align: center;
    min-width: 120px;
  }

  .score-number {
    font-size: 68px;
    color: #303133;
    line-height: 0.9;
    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Helvetica Neue', Arial, sans-serif;
    letter-spacing: -0.02em;
    transition: all 0.3s ease;
  }

  .score-label {
    font-size: 14px;
    color: #606266;
    margin-top: 12px;
    font-weight: 500;
  }

  .rating-sliders {
    flex: 1;
    max-width: 300px;
  }

  .rating-row {
    display: flex;
    align-items: center;
    margin-bottom: 5px;
    gap: 20px;
  }

  .rating-row:last-child {
    margin-bottom: 0;
  }

  .rating-score {
    width: 0px;
    text-align: center;
    font-weight: 600;
    font-size: 14px;
    color: #303133;
  }

  .rating-slider {
    flex: 1;
  }

  /* 自定义滑块样式 */
  .rating-slider :deep(.el-slider__runway) {
    background-color: #e4e7ed;
    height: 6px;
    border-radius: 3px;
  }

  .rating-slider :deep(.el-slider__bar) {
    background-color: #67c23a;
    height: 6px;
    border-radius: 3px;
  }

  .rating-slider :deep(.el-slider__button) {
    border: 4px solid #67c23a;
    background-color: #fff;
    width: 24px;
    height: 24px;
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.12);
    transition: all 0.2s ease;
  }

  .rating-slider :deep(.el-slider__button:hover) {
    transform: scale(1.15);
    border-color: #5daf34;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  }

  .rating-slider :deep(.el-slider__button-wrapper) {
    top: -12px;
  }

  .rating-slider :deep(.el-slider__button-wrapper:hover) {
    cursor: grab;
  }

  .rating-slider :deep(.el-slider__button-wrapper:active) {
    cursor: grabbing;
  }

  .rating-slider :deep(.el-slider) {
    height: 24px;
    display: flex;
    align-items: center;
  }

  /* 评论区样式 */
  .comment-section-wrapper {
    margin-bottom: 20px;
  }

  .comment-header {
    margin-bottom: 20px;
  }

  .comment-label {
    font-size: 14px;
    color: #606266;
    font-weight: 500;
    line-height: 1.5;
  }

  .comment-section {
    background: #f5f7fa;
    border-radius: 8px;
    padding: 20px;
  }

  .comments-list {
    margin-bottom: 20px;
  }

  .add-comment-section {
    padding: 20px;
    text-align: center;
    border-top: 1px solid #ebeef5;
  }

  .add-comment-btn {
    padding: 12px 24px;
    font-size: 14px;
    font-weight: 500;
  }

  .language-tabs {
    display: flex;
    gap: 12px;
    align-items: center;
  }

  .language-tab {
    cursor: pointer;
    transition: all 0.3s ease;
    padding: 8px 16px;
    border-radius: 20px;
    user-select: none;
    font-weight: 500;
    position: relative;
  }

  .language-tab:hover {
    transform: translateY(-1px);
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  }

  .language-tab.active {
    box-shadow: 0 4px 12px rgba(103, 194, 58, 0.3);
  }

  .language-tab:not(.active) {
    background-color: #f0f2f5;
    color: #606266;
    border-color: #e4e7ed;
  }

  .language-tab:not(.active):hover {
    background-color: #e8f5e8;
    color: #67c23a;
    border-color: #67c23a;
  }

  /* 覆盖Element Plus的默认样式 */
  .language-tabs :deep(.el-tag) {
    margin-right: 0;
  }

  .language-tabs :deep(.el-tag--success) {
    border-color: #67c23a;
  }

  .language-tabs :deep(.el-tag--info) {
    border-color: #e4e7ed;
  }

  .comment-item {
    background: white;
    border-radius: 6px;
    padding: 15px;
    margin-bottom: 12px;
    border: 1px solid #e4e7ed;
  }

  .comment-row {
    display: flex;
    gap: 12px;
    align-items: flex-start;
  }

  .comment-avatar-display {
    flex-shrink: 0;
  }

  .avatar-uploader-small :deep(.el-upload) {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    width: 60px;
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .avatar-placeholder-small {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    color: #8c939d;
    font-size: 10px;
    text-align: center;
  }

  .avatar-placeholder-small .el-icon {
    font-size: 16px;
    margin-bottom: 2px;
  }

  .comment-right {
    flex: 1;
  }

  .comment-inputs-display {
    display: flex;
    gap: 12px;
    align-items: center;
    margin-bottom: 12px;
  }

  .comment-actions {
    flex-shrink: 0;
    display: flex;
    align-items: center;
    margin-top: 4px;
  }

  .delete-btn {
    width: 24px;
    height: 24px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 0;
    font-size: 16px;
    font-weight: bold;
    background: #f56c6c;
    border: none;
    color: white;
  }

  .delete-btn:hover {
    background: #f78989;
  }
</style>
