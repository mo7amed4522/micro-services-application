<template>
  <ele-page>
    <ele-card>
      <el-form ref="settingFormRef" label-width="100px" @submit.prevent="">
        <el-row :gutter="8">
          <el-form-item label="商户名称：">
            <div class="title">万博</div>
          </el-form-item>
        </el-row>
        <el-row :gutter="8">
          <el-col :lg="24" :md="12" :sm="12" :xs="24">
            <el-form-item label="赠送方式：">
              <el-input clearable :disabled="isWbDisabled" v-model="wanBoForm.immediatePercentage" placeholder="请输入" style="width: 180px; margin-right: 20px">
                <!-- 前面追加 -->
                <template #prepend>即时</template>
                <template #append>%</template>
              </el-input>
              <el-input clearable :disabled="isWbDisabled" v-model="wanBoForm.secondDayPercentage" placeholder="请输入" style="width: 180px">
                <template #prepend>第二天</template>
                <template #append>%</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="8">
          <el-col :lg="24" :md="12" :sm="12" :xs="24">
            <el-form-item>
              <el-button :type="isWbDisabled ? 'primary' : 'default'" @click="isWbDisabled = !isWbDisabled">{{ isWbDisabled ? '修改' : '取消' }}</el-button>
              <el-button :type="isWbDisabled ? 'default' : 'primary'" :disabled="isWbDisabled" @click="updateConfigUser('paymer01')">确定</el-button>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="8">
          <el-form-item label="商户名称：">
            <div class="title">亚投</div>
          </el-form-item>
        </el-row>
        <el-row :gutter="8">
          <el-col :lg="24" :md="12" :sm="12" :xs="24">
            <el-form-item label="赠送方式：">
              <el-input clearable :disabled="isYtDisabled" v-model="yaTouForm.immediatePercentage" placeholder="请输入" style="width: 180px; margin-right: 20px">
                <!-- 前面追加 -->
                <template #prepend>即时</template>
                <template #append>%</template>
              </el-input>
              <el-input clearable :disabled="isYtDisabled" v-model="yaTouForm.secondDayPercentage" placeholder="请输入" style="width: 180px">
                <template #prepend>第二天</template>
                <template #append>%</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="8">
          <el-col :lg="24" :md="12" :sm="12" :xs="24">
            <el-form-item>
              <el-button :type="isYtDisabled ? 'primary' : 'default'" @click="isYtDisabled = !isYtDisabled">{{ isYtDisabled ? '修改' : '取消' }}</el-button>
              <el-button :type="isYtDisabled ? 'default' : 'primary'" :disabled="isYtDisabled" @click="updateConfigUser('paymer02')">确定</el-button>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="8">
          <el-form-item label="商户名称：">
            <div class="title">亚冠</div>
          </el-form-item>
        </el-row>
        <el-row :gutter="8">
          <el-col :lg="24" :md="12" :sm="12" :xs="24">
            <el-form-item label="赠送方式：">
              <el-input clearable :disabled="isYgDisabled" v-model="yaGuanForm.immediatePercentage" placeholder="请输入" style="width: 180px; margin-right: 20px">
                <!-- 前面追加 -->
                <template #prepend>即时</template>
                <template #append>%</template>
              </el-input>
              <el-input clearable :disabled="isYgDisabled" v-model="yaGuanForm.secondDayPercentage" placeholder="请输入" style="width: 180px">
                <template #prepend>第二天</template>
                <template #append>%</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="8">
          <el-col :lg="24" :md="12" :sm="12" :xs="24">
            <el-form-item>
              <el-button :type="isYgDisabled ? 'primary' : 'default'" @click="isYgDisabled = !isYgDisabled">{{ isYgDisabled ? '修改' : '取消' }}</el-button>
              <el-button :type="isYgDisabled ? 'default' : 'primary'" :disabled="isYgDisabled" @click="updateConfigUser('paymer03')">确定</el-button>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="8">
          <el-col :lg="24" :md="12" :sm="12" :xs="24">
            <div class="tips">
              <div class="tips-title">说明</div>
              <div class="tips-content">
                <div>1，两个选项都有值的情况下，则即时发放一部分，剩下的比例则第二天指定时间发放。</div>
                <div>2，如果发放比例为0，则当前选项不应用。</div>
              </div>
            </div>
          </el-col>
        </el-row>
      </el-form>
    </ele-card>
  </ele-page>
</template>
<script setup>
  import { ref, onMounted } from 'vue';
  import { EleMessage } from 'ele-admin-plus';
  import { configMUserApi, getConfigMUserApi } from '@/api/merchant';
  import { useMessageBox } from 'ele-admin-plus/es';

  const messageBox = useMessageBox();

  const isWbDisabled = ref(true);

  const isYtDisabled = ref(true);

  const isYgDisabled = ref(true);

  const wanBoForm = ref({
    immediatePercentage: 0,
    secondDayPercentage: 0,
    merName: ''
  });

  const yaTouForm = ref({
    immediatePercentage: 0,
    secondDayPercentage: 0
  });

  const yaGuanForm = ref({
    immediatePercentage: 0,
    secondDayPercentage: 0,
    merName: ''
  });

  // 获取商户配置
  const getConfigMUser = async () => {
    try {
      const res = await getConfigMUserApi();
      wanBoForm.value = {
        ...res.paymer01,
        immediatePercentage: res.paymer01.immediatePercentage * 100,
        secondDayPercentage: res.paymer01.secondDayPercentage * 100
      }
      yaTouForm.value = {
        ...res.paymer02,
        immediatePercentage: res.paymer02.immediatePercentage * 100,
        secondDayPercentage: res.paymer02.secondDayPercentage * 100
      }
      yaGuanForm.value = {
        ...res.paymer03,
        immediatePercentage: res.paymer03.immediatePercentage * 100,
        secondDayPercentage: res.paymer03.secondDayPercentage * 100
      };
    } catch (e) {
      EleMessage.error(e.message);
    }
  };

  const getFormData = (merName) => {
    if (merName === 'paymer01') {
      return wanBoForm.value;
    } else if (merName === 'paymer02') {
      return yaTouForm.value;
    } else if (merName === 'paymer03') {
      return yaGuanForm.value;
    }
    return null;
  };

  const setDisabledState = (merName) => {
    if (merName === 'paymer01') {
      isWbDisabled.value = true;
    } else if (merName === 'paymer02') {
      isYtDisabled.value = true;
    } else if (merName === 'paymer03') {
      isYgDisabled.value = true;
    }
  }

  const getMerName = (merName) => {
    if (merName === 'paymer01') {
      return '万博';
    } else if (merName === 'paymer02') {
      return '亚投';
    } else if (merName === 'paymer03') {
      return '亚冠';
    }
  }

  // 赠送方式配置
  const updateConfigUser = async (merName) => {
    messageBox
      .confirm(`确认修改${getMerName(merName)}商户赠送方式配置吗? `, '操作确认', {
        type: 'warning',
        draggable: true
      })
      .then(async () => {
        try {
          const formData = getFormData(merName);
          const res = await configMUserApi({
            immediatePercentage: Math.max(Number(formData.immediatePercentage) / 100, 0),
            secondDayPercentage: Math.max(Number(formData.secondDayPercentage) / 100, 0),
            merName: merName
          });

          // 处理 disabled 状态
          setDisabledState(merName);

          EleMessage.success(res);
          getConfigMUser();
        } catch (error) {
          EleMessage.error(error.message);
        }
      })
      .catch(() => {});
  };

  onMounted(() => {
    getConfigMUser();
  });
</script>
<style lang="scss" scoped>
  .title {
    font-size: 14px;
    font-weight: bold;
  }
  .tips {
    display: flex;
    font-size: 12px;
    color: #999;
    margin-bottom: 10px;
    &-title {
      margin-right: 5px;
    }
  }
</style>
