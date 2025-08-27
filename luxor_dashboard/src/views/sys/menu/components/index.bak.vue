<template>
  <ele-page>
    <ele-card :body-style="{ paddingTop: '8px' }">
      <div>
        <div class="gva-table-box">
          <div class="gva-btn-list">
            <el-button type="primary" icon="plus" @click="addMenu(0)">新增根菜单</el-button>
          </div>

          <!-- 由于此处菜单跟左侧列表一一对应所以不需要分页 pageSize默认999 -->
          <el-table :data="tableData" row-key="ID">
            <el-table-column align="left" label="ID" min-width="100" prop="ID" />
            <el-table-column align="left" label="展示名称" min-width="120" prop="authorityName">
              <template #default="scope">
                <span>{{ scope.row.meta.title }}</span>
              </template>
            </el-table-column>
            <el-table-column align="left" label="图标" min-width="140" prop="authorityName">
              <template #default="scope">
                <div v-if="scope.row.meta.icon" class="icon-column">
                  <el-icon>
                    <component :is="scope.row.meta.icon" />
                  </el-icon>
                  <span>{{ scope.row.meta.icon }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column align="left" label="路由Name" show-overflow-tooltip min-width="160" prop="name" />
            <el-table-column align="left" label="路由Path" show-overflow-tooltip min-width="160" prop="path" />
            <el-table-column align="left" label="是否隐藏" min-width="100" prop="hidden">
              <template #default="scope">
                <span>{{ scope.row.hidden ? '隐藏' : '显示' }}</span>
              </template>
            </el-table-column>
            <el-table-column align="left" label="父节点" min-width="90" prop="parentId" />
            <el-table-column align="left" label="排序" min-width="70" prop="sort" />
            <el-table-column align="left" label="文件路径" min-width="360" prop="component" />
            <el-table-column align="left" fixed="right" label="操作" width="300">
              <template #default="scope">
                <el-button type="primary" link icon="plus" @click="addMenu(scope.row.ID)">添加子菜单</el-button>
                <el-button type="primary" link icon="edit" @click="editMenu(scope.row.ID)">编辑</el-button>
                <el-button type="primary" link icon="delete" @click="deleteMenu(scope.row.ID)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <el-drawer v-model="dialogFormVisible" size="60%" :before-close="handleClose" :show-close="false">
          <template #header>
            <div class="flex justify-between items-center">
              <span class="text-lg">{{ dialogTitle }}</span>
              <div>
                <el-button @click="closeDialog">取 消</el-button>
                <el-button type="primary" @click="enterDialog">确 定</el-button>
              </div>
            </div>
          </template>

          <el-form v-if="dialogFormVisible" ref="menuForm" :inline="true" :model="form" :rules="rules" label-width="92px" label-position="top">
            <el-row style="width: 100%">
              <el-col :sm="12" :xs="24">
                <el-form-item label="上级菜单" prop="parentId">
                  <menu-select v-model="form.parentId" />
                </el-form-item>
              </el-col>
              <el-col :sm="12" :xs="24">
                <el-form-item label="展示名称" prop="meta.title">
                  <el-input v-model="form.meta.title" autocomplete="off" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="16" style="width: 100%">
              <el-col :sm="8" :xs="24">
                <el-form-item label="路由Name" prop="path">
                  <el-input v-model="form.name" autocomplete="off" placeholder="唯一英文字符串" @change="changeName" />
                </el-form-item>
              </el-col>
              <el-col :sm="8" :xs="24">
                <el-form-item prop="path">
                  <template #label>
                    <span style="display: inline-flex; align-items: center">
                      <span>路由Path</span>
                      <el-checkbox v-model="checkFlag" style="margin-left: 12px; height: auto">添加参数</el-checkbox>
                    </span>
                  </template>

                  <el-input v-model="form.path" :disabled="!checkFlag" autocomplete="off" placeholder="建议只在后方拼接参数" />
                </el-form-item>
              </el-col>
              <el-col :sm="8" :xs="24">
                <el-form-item label="是否隐藏">
                  <el-select v-model="form.hidden" style="width: 100%" placeholder="是否在列表隐藏">
                    <el-option :value="false" label="否" />
                    <el-option :value="true" label="是" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="16" style="width: 100%">
              <el-col :sm="8" :xs="24">
                <el-form-item label="组件地址" prop="component">
                  <el-input v-model.number="form.component" autocomplete="off" />
                </el-form-item>
              </el-col>
              <el-col :sm="8" :xs="24">
                <el-form-item label="排序标记" prop="sort">
                  <el-input v-model.number="form.sort" autocomplete="off" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <div>
            <div class="flex items-center gap-2">
              <el-button type="primary" icon="edit" @click="addParameter(form)">新增菜单参数</el-button>
            </div>
            <el-table :data="form.parameters" style="width: 100%; margin-top: 12px">
              <el-table-column align="left" prop="type" label="参数类型" width="180">
                <template #default="scope">
                  <el-select v-model="scope.row.type" placeholder="请选择">
                    <el-option key="query" value="query" label="query" />
                    <el-option key="params" value="params" label="params" />
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column align="left" prop="key" label="参数key" width="180">
                <template #default="scope">
                  <div>
                    <el-input v-model="scope.row.key" />
                  </div>
                </template>
              </el-table-column>
              <el-table-column align="left" prop="value" label="参数值">
                <template #default="scope">
                  <div>
                    <el-input v-model="scope.row.value" />
                  </div>
                </template>
              </el-table-column>
              <el-table-column align="left">
                <template #default="scope">
                  <div>
                    <el-button type="danger" icon="delete" @click="deleteParameter(form.parameters, scope.$index)">删除</el-button>
                  </div>
                </template>
              </el-table-column>
            </el-table>

            <div style="display: flex; align-items: center">
              <el-button type="primary" icon="edit" @click="addBtn(form)">新增可控按钮</el-button>
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
              <el-table-column align="left" prop="name" label="按钮名称" width="180">
                <template #default="scope">
                  <div>
                    <el-input v-model="scope.row.name" />
                  </div>
                </template>
              </el-table-column>
              <el-table-column align="left" prop="name" label="备注" width="180">
                <template #default="scope">
                  <div>
                    <el-input v-model="scope.row.desc" />
                  </div>
                </template>
              </el-table-column>
              <el-table-column align="left">
                <template #default="scope">
                  <div>
                    <el-button type="danger" icon="delete" @click="deleteBtn(form.menuBtn, scope.$index)">删除</el-button>
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
  import { updateBaseMenu, getMenuList, addBaseMenu, getBaseMenuById, deleteBaseMenu, canRemoveAuthorityBtnApi } from '@/api/sys/menu/index.js';
  import icon from '../icon.vue';
  import WarningBar from './warningBar.vue';
  import { reactive, ref } from 'vue';
  import { ElMessage, ElMessageBox } from 'element-plus';
  import { QuestionFilled } from '@element-plus/icons-vue';
  import { toLowerCase } from '@/utils/stringFun.js';
  import MenuSelect from '@/views/sys/menu/components/menu-select.vue';
  // import ComponentsCascader from "./components/components-cascader.vue"

  defineOptions({
    name: 'Menus'
  });

  const rules = reactive({
    path: [{ required: true, message: '请输入菜单name', trigger: 'blur' }],
    // component: [{ required: true, message: '请输入文件路径', trigger: 'blur' }],
    'meta.title': [{ required: true, message: '请输入菜单展示名称', trigger: 'blur' }]
  });

  const tableData = ref([]);
  // 查询
  const getTableData = async () => {
    const table = await getMenuList();
    tableData.value = table;
  };

  getTableData();

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
      keepAlive: false
    },
    parameters: [],
    menuBtn: []
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
    ElMessageBox.confirm('此操作将永久删除所有角色下该菜单, 是否继续?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
      .then(async () => {
        const res = await deleteBaseMenu({ ID });
        ElMessage({
          type: 'success',
          message: '删除成功!'
        });
        await getTableData();
      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: '已取消删除'
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
        keepAlive: false
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
          getTableData();
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
  const dialogTitle = ref('新增菜单');
  const addMenu = (id) => {
    dialogTitle.value = '新增菜单';
    form.value.parentId = id;
    isEdit.value = false;
    setOptions();
    dialogFormVisible.value = true;
  };
  // 修改菜单方法
  const editMenu = async (id) => {
    dialogTitle.value = '编辑菜单';
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
