<template>
  <ele-page>
    <ele-card>
      <div>
        <ele-pro-table
          ref="tableRef"
          row-key="ID"
          :columns="columns"
          :datasource="datasource"
          v-model:current="current"
          v-model:selections="selections"
          :show-overflow-tooltip="true"
          :border="bordered"
          :toolbar="{ theme: 'default' }"
          :default-sort="{ prop: 'createTime', order: 'descending' }"
          :footer-style="{ paddingBottom: '12px' }"
          :tableStyle="{ paddingBottom: '0', color: 'rgba(0, 0, 0, 0.65)' }"
        >
          <template #toolbar>
            <el-button type="primary" icon="plus" @click="addMenu(0)">{{ t('menu.addRootMenu') }}</el-button>
          </template>
          <template #title="{ row }">
            <span>{{ row.meta.title }}</span>
          </template>
          <template #icon="{ row }">
            <div v-if="row.meta.icon" class="icon-column">
              <el-icon>
                <component :is="row.meta.icon" />
              </el-icon>
              <span>{{ row.meta.icon }}</span>
            </div>
          </template>

          <template #action="{ row }">
            <el-button type="primary" link icon="plus" @click="addMenu(row.ID)">{{ t('menu.addSubmenu') }}</el-button>
            <el-button type="primary" link icon="edit" @click="editMenu(row.ID)">{{ t('common.edit') }}</el-button>
            <el-button type="primary" link icon="delete" @click="deleteMenu(row.ID)">{{ t('common.deleted') }}</el-button>
          </template>
        </ele-pro-table>
        <!-- 菜单抽屉 -->
        <el-drawer v-model="dialogFormVisible" size="60%" :before-close="handleClose" :show-close="false">
          <template #header>
            <div class="flex justify-between items-center">
              <span class="text-lg" style="margin-bottom: 10px">{{ dialogTitle }}</span>
              <div>
                <el-button @click="closeDialog">{{ t('common.cancelText') }}</el-button>
                <el-button type="primary" @click="enterDialog">{{ t('common.confirm') }}</el-button>
              </div>
            </div>
          </template>

          <el-form v-if="dialogFormVisible" ref="menuForm" :inline="true" :model="form" :rules="rules" label-width="92px" label-position="top">
            <el-row style="width: 100%">
              <el-col :sm="12" :xs="24">
                <el-form-item :label="t('menu.parentMenuText')" prop="parentId">
                  <menu-select v-model="form.parentId" :placeholder="t('menu.selectParentMenu')" />
                </el-form-item>
              </el-col>
              <el-col :sm="12" :xs="24">
                <el-form-item :label="t('menu.displayName')" prop="meta.title">
                  <el-input v-model="form.meta.title" :placeholder="t('menu.enterDisplayName')" autocomplete="off" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="16" style="width: 100%">
              <el-col :sm="8" :xs="24">
                <el-form-item :label="t('menu.routeName')" prop="path">
                  <el-input v-model="form.name" autocomplete="off" :placeholder="t('menu.enterRouteName')" @change="changeName" />
                </el-form-item>
              </el-col>
              <el-col :sm="8" :xs="24">
                <el-form-item prop="path">
                  <template #label>
                    <span style="display: inline-flex; align-items: center">
                      <span>{{ t('menu.routePath') }}</span>
                      <el-checkbox v-model="checkFlag" style="margin-left: 12px; height: auto">{{ t('menu.addParameterText') }}</el-checkbox>
                    </span>
                  </template>

                  <el-input v-model="form.path" :disabled="!checkFlag" autocomplete="off" :placeholder="t('menu.enterRoutePath')" />
                </el-form-item>
              </el-col>
              <el-col :sm="8" :xs="24">
                <el-form-item :label="t('menu.isHidden')">
                  <el-select v-model="form.hidden" style="width: 100%" :placeholder="t('menu.isHiddenText')">
                    <el-option :value="false" :label="t('menu.no')" />
                    <el-option :value="true" :label="t('menu.yes')" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="16" style="width: 100%">
              <el-col :sm="8" :xs="24">
                <el-form-item :label="t('menu.componentPath')" prop="component">
                  <el-input v-model.number="form.component" :placeholder="t('menu.enterComponentPath')" autocomplete="off" />
                </el-form-item>
              </el-col>
              <el-col :sm="8" :xs="24">
                <el-form-item :label="t('menu.sortMark')" prop="sort">
                  <el-input v-model.number="form.sort" :placeholder="t('menu.enterSortMark')" autocomplete="off" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="16" style="width: 100%">
              <el-col :sm="24" :xs="24">
                <el-form-item :label="t('menu.metaText')" prop="meta.lang">
                  <el-input v-model="form.meta.lang" type="textarea" :rows="3" :placeholder="t('menu.metaText')" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <div>
            <div class="flex items-center gap-2">
              <el-button type="primary" icon="edit" @click="addParameter(form)">{{ t('menu.addMenuParamsText') }}</el-button>
            </div>
            <el-table :data="form.parameters" style="width: 100%; margin-top: 12px">
              <el-table-column align="left" prop="type" :label="t('menu.paramsTypeText')" width="180">
                <template #default="scope">
                  <el-select v-model="scope.row.type" :placeholder="t('menu.selectParamsType')">
                    <el-option key="query" value="query" label="query" />
                    <el-option key="params" value="params" label="params" />
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column align="left" prop="key" :label="t('menu.paramsKey')" width="180">
                <template #default="scope">
                  <div>
                    <el-input v-model="scope.row.key" :placeholder="t('menu.enterParamsKey')" />
                  </div>
                </template>
              </el-table-column>
              <el-table-column align="left" prop="value" :label="t('menu.paramsValue')">
                <template #default="scope">
                  <div>
                    <el-input v-model="scope.row.value" :placeholder="t('menu.enterParamsValue')" />
                  </div>
                </template>
              </el-table-column>
              <el-table-column align="left">
                <template #default="scope">
                  <div>
                    <el-button type="danger" icon="delete" @click="deleteParameter(form.parameters, scope.$index)">{{ t('common.deleted') }}</el-button>
                  </div>
                </template>
              </el-table-column>
            </el-table>

            <div style="display: flex; align-items: center; margin-top: 12px">
              <el-button type="primary" icon="edit" @click="addBtn(form)">{{ t('menu.addControlButton') }}</el-button>
              <!--          <el-icon-->
              <!--              class="cursor-pointer"-->
              <!--              @click="-->
              <!--              toDoc('https://www.gin-vue-admin.com/guide/web/button-auth.html')-->
              <!--            "-->
              <!--          >-->
              <!--            <QuestionFilled />-->
              <!--          </el-icon>-->
              <el-tooltip style="margin-left: 15px" content="测试、暂未使用" placement="top" effect="light">
                <el-icon>
                  <QuestionFilled />
                </el-icon>
              </el-tooltip>
            </div>

            <el-table :data="form.menuBtn" style="width: 100%; margin-top: 12px">
              <el-table-column align="left" prop="name" :label="t('menu.buttonName')" width="180">
                <template #default="scope">
                  <div>
                    <el-input v-model="scope.row.name" :placeholder="t('menu.enterButtonName')" />
                  </div>
                </template>
              </el-table-column>
              <el-table-column align="left" prop="name" :label="t('common.notes')" width="180">
                <template #default="scope">
                  <div>
                    <el-input v-model="scope.row.desc" :placeholder="t('common.enterNotes')" />
                  </div>
                </template>
              </el-table-column>
              <el-table-column align="left">
                <template #default="scope">
                  <div>
                    <el-button type="danger" icon="delete" @click="deleteBtn(form.menuBtn, scope.$index)">{{ t('common.deleted') }}</el-button>
                  </div>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-drawer>
      </div>
    </ele-card>
  </ele-page>
</template>

<script setup>
  import { addBaseMenu, canRemoveAuthorityBtnApi, deleteBaseMenu, getBaseMenuById, getMenuList, updateBaseMenu } from '@/api/sys/menu';
  import icon from './icon.vue';
  import { reactive, ref } from 'vue';
  import { ElMessage, ElMessageBox } from 'element-plus';
  import { QuestionFilled } from '@element-plus/icons-vue';
  import { toLowerCase } from '@/utils/stringFun';
  import MenuSelect from '@/views/sys/menu/components/menu-select.vue';
  import { EleMessage } from 'ele-admin-plus';
  import { PlusOutlined } from '@/components/icons/index.js';
  // import ComponentsCascader from "./components/components-cascader.vue"
  import { useI18n } from 'vue-i18n';

  const { t } = useI18n();

  defineOptions({
    name: 'Menus'
  });
  /** 表格实例 */
  const tableRef = ref(null);
  const bordered = ref(true);
  const selections = ref([]);
  const current = ref(null);

  const rules = reactive({
    path: [{ required: true, message: '请输入菜单name', trigger: 'blur' }],
    // component: [{ required: true, message: '请输入文件路径', trigger: 'blur' }],
    'meta.title': [{ required: true, message: '请输入菜单展示名称', trigger: 'blur' }]
  });

  /** 表格列配置 */
  const columns = ref([
    {
      prop: 'tt',
      label: t('menu.nameText'),
      align: 'left',
      slot: 'title',
      minWidth: 140
    },
    {
      prop: 'tt1',
      label: t('menu.iconText'),
      slot: 'icon'
    },
    {
      prop: 'name',
      label: t('menu.routeName'),
      minWidth: 160
    },
    {
      prop: 'path',
      label: t('menu.routePath'),
      minWidth: 160
    },
    {
      prop: 'hidden',
      label: t('menu.visibleText'),
      width: 100,
      align: 'center',
      formatter: (row) => (row.hidden ? t('menu.hiddenText') : t('menu.showText'))
    },
    {
      prop: 'sort',
      label: t('menu.sortText'),
      align: 'center'
    },
    {
      prop: 'component',
      label: t('menu.filePathText'),
      align: 'center'
    },
    {
      columnKey: 'action',
      label: t('common.action'),
      width: 320,
      align: 'center',
      slot: 'action',
      hideInPrint: true,
      hideInExport: true,
      fixed: 'right'
    }
  ]);
  const tableData = ref([]);
  const datasource = async () => {
    try {
      const res = await getMenuList();
      tableData.value = res;
      return res;
    } catch (e) {
      EleMessage.error(e.message);
    }
  };

  // 新增参数
  const addParameter = (form) => {
    if (!form.parameters) {
      form.parameters = [];
    }
    form.parameters.push({
      type: 'query',
      key: '',
      value: ''
    });
  };

  const fmtComponent = (component) => {
    form.value.component = component.replace(/\\/g, '/');
    form.value.name = toLowerCase(pathInfo['/src/' + component]);
    form.value.path = form.value.name;
  };

  // 删除参数
  const deleteParameter = (parameters, index) => {
    parameters.splice(index, 1);
  };

  // 新增可控按钮
  const addBtn = (form) => {
    if (!form.menuBtn) {
      form.menuBtn = [];
    }
    form.menuBtn.push({
      name: '',
      desc: ''
    });
  };
  // 删除可控按钮
  const deleteBtn = async (btns, index) => {
    const btn = btns[index];
    if (btn.ID === 0) {
      btns.splice(index, 1);
      return;
    }
    const res = await canRemoveAuthorityBtnApi({ id: btn.ID });
    if (res.code === 0) {
      btns.splice(index, 1);
    }
  };

  const form = ref({
    ID: 0,
    path: '',
    name: '',
    hidden: false,
    parentId: 0,
    component: '',
    meta: {
      activeName: '',
      title: '',
      icon: '',
      defaultMenu: false,
      closeTab: false,
      keepAlive: false,
      lang: ''
    },
    parameters: [],
    menuBtn: [],
  });
  const changeName = () => {
    form.value.path = form.value.name;
  };

  const handleClose = (done) => {
    initForm();
    done();
  };
  // 删除菜单
  const deleteMenu = (ID) => {
    ElMessageBox.confirm(t('menu.deleteMenuTips'), t('menu.tipsText'), {
      confirmButtonText: t('common.confirm'),
      cancelButtonText: t('common.cancelText'),
      type: 'warning'
    })
      .then(async () => {
        const res = await deleteBaseMenu({ ID });
        ElMessage({
          type: 'success',
          message: t('menu.deleteSuccess')
        });
        reload();
      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: t('menu.deleteCancel')
        });
      });
  };
  // 初始化弹窗内表格方法
  const menuForm = ref(null);
  const checkFlag = ref(false);
  const initForm = () => {
    checkFlag.value = false;
    menuForm.value.resetFields();
    form.value = {
      ID: 0,
      path: '',
      name: '',
      hidden: false,
      parentId: 0,
      component: '',
      meta: {
        title: '',
        icon: '',
        defaultMenu: false,
        closeTab: false,
        keepAlive: false,
        lang: ''
      }
    };
  };
  // 关闭弹窗

  const dialogFormVisible = ref(false);
  const closeDialog = () => {
    initForm();
    dialogFormVisible.value = false;
  };
  // 添加menu
  const enterDialog = async () => {
    menuForm.value.validate(async (valid) => {
      try {
        if (valid) {
          let res;
          if (isEdit.value) {
            await updateBaseMenu(form.value);
          } else {
            await addBaseMenu(form.value);
          }
          ElMessage({
            type: 'success',
            message: isEdit.value ? '编辑成功' : '添加成功!'
          });
          reload();
          initForm();
          dialogFormVisible.value = false;
        }
      } catch (e) {
        ElMessage({
          type: 'error',
          message: isEdit.value ? '编辑失败' : '添加失败!'
        });
      }
    });
  };

  const reload = (where) => {
    tableRef.value?.reload?.();
  };

  const menuOption = ref([
    {
      ID: '0',
      title: '根菜单'
    }
  ]);
  const setOptions = () => {
    menuOption.value = [
      {
        ID: 0,
        title: '根目录'
      }
    ];
    setMenuOptions(tableData.value, menuOption.value, false);
  };
  const setMenuOptions = (menuData, optionsData, disabled) => {
    menuData &&
      menuData.forEach((item) => {
        if (item.children && item.children.length) {
          const option = {
            title: item.meta.title,
            ID: item.ID,
            disabled: disabled || item.ID === form.value.ID,
            children: []
          };
          setMenuOptions(item.children, option.children, disabled || item.ID === form.value.ID);
          optionsData.push(option);
        } else {
          const option = {
            title: item.meta.title,
            ID: item.ID,
            disabled: disabled || item.ID === form.value.ID
          };
          optionsData.push(option);
        }
      });
  };

  // 添加菜单方法，id为 0则为添加根菜单
  const isEdit = ref(false);
  const dialogTitle = ref(t('menu.addMenuText'));
  const addMenu = (id) => {
    dialogTitle.value = t('menu.addMenuText');
    form.value.parentId = id;
    isEdit.value = false;
    setOptions();
    dialogFormVisible.value = true;
  };
  // 修改菜单方法
  const editMenu = async (id) => {
    dialogTitle.value = t('menu.editMenuText');
    const res = await getBaseMenuById({ id });
    form.value.parentId = id;
    form.value = res.menu;
    isEdit.value = true;
    setOptions();
    dialogFormVisible.value = true;
  };
</script>

<style scoped lang="scss">
  //.warning {
  //  color: #dc143c;
  //}
  //.icon-column {
  //  display: flex;
  //  align-items: center;
  //  .el-icon {
  //    margin-right: 8px;
  //  }
  //}
</style>
