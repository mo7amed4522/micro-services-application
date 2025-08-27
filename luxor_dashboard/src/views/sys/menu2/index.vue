<template>
  <ele-page>
    <menu-search @search="reload" />
    <ele-card :body-style="{ paddingTop: '8px' }">
      <ele-pro-table
        sticky
        ref="tableRef"
        row-key="ID"
        :columns="columns"
        :datasource="datasource"
        :show-overflow-tooltip="true"
        :highlight-current-row="true"
        :export-config="{ fileName: '菜单数据' }"
        :default-expand-all="false"
        :pagination="false"
      >
        <template #toolbar>
          <el-button type="primary" class="ele-btn-icon" :icon="PlusOutlined" @click="openEdit()">新建</el-button>
          <el-button class="ele-btn-icon" :icon="ColumnHeightOutlined" @click="expandAll">展开全部</el-button>
          <el-button class="ele-btn-icon" :icon="VerticalAlignMiddleOutlined" @click="foldAll">折叠全部</el-button>
        </template>
        <template #title="{ row }">
          <el-icon v-if="row.icon && row.icon.trim()" :size="15" style="margin-right: 8px; vertical-align: -2px">
            <component v-if="row.icon !== null" :is="row.icon" />
          </el-icon>
          <span>{{ row.meta.title }}</span>
        </template>
        <template #menu_type="{ row }">
          <el-tag v-if="isExternalLink(row.href)" size="small" type="danger" :disable-transitions="true">外链</el-tag>
          <el-tag v-else-if="isExternalLink(row.component)" size="small" type="warning" :disable-transitions="true">内链</el-tag>
          <el-tag v-else-if="isDirectory(row)" size="small" :disable-transitions="true">目录</el-tag>
          <el-tag v-else-if="isMenu(row)" size="small" type="success" :disable-transitions="true">菜单</el-tag>
          <el-tag v-else-if="row.menu_type === 1" size="small" type="info" :disable-transitions="true">按钮</el-tag>
        </template>
        <template #action="{ row }">
          <el-link type="primary" :underline="false" @click="openEdit(null, row.parentID)">添加</el-link>
          <el-divider direction="vertical" />
          <el-link type="primary" link icon="edit" :underline="false" @click="openEdit(row)">修改</el-link>
          <el-divider direction="vertical" />
          <el-link type="danger" link icon="delete" :underline="false" @click="remove(row)">删除</el-link>
        </template>
      </ele-pro-table>
    </ele-card>
    <menu-edit v-model="showEdit" :data="current" :parent-id="parentId" @done="reload" />
  </ele-page>
</template>

<script setup>
  import { ref } from 'vue';
  import { ElMessageBox } from 'element-plus/es';
  import { EleMessage, isExternalLink, toTree } from 'ele-admin-plus/es';
  import { Agent, Activity, CustomerService, Discount, Finance, Game, Provider, Risk, Report, PlusOutlined, ColumnHeightOutlined, VerticalAlignMiddleOutlined } from '@/components/icons';
  import MenuSearch from './components/menu-search.vue';
  import MenuEdit from './components/menu-edit.vue';
  import { listMenus } from '@/api/sys/menu';

  /** 表格实例 */
  const tableRef = ref(null);

  /** 表格列配置 */
  const columns = ref([
    {
      type: 'index',
      columnKey: 'index',
      width: 50,
      align: 'center',
      fixed: 'left'
    },
    {
      label: '菜单名称',
      slot: 'title',
      minWidth: 160
    },
    {
      prop: 'path',
      label: '路由地址',
      minWidth: 160
    },
    {
      prop: 'sort',
      label: '排序',
      width: 100,
      align: 'center'
    },
    {
      prop: 'hidden',
      label: '可见',
      width: 100,
      align: 'center',
      formatter: (row) => (row.hidden ? '否' : '是')
    },
    {
      prop: 'component',
      label: '组件路径',
      align: 'center'
    },
    // {
    //   prop: 'menu_type',
    //   label: '类型',
    //   width: 100,
    //   align: 'center',
    //   slot: 'menu_type',
    //   formatter: (row) =>
    //     ['菜单', '按钮', '外链', '内链', '目录'][
    //       isExternalLink(row.href)
    //         ? 2
    //         : isExternalLink(row.component)
    //           ? 3
    //           : isDirectory(row)
    //             ? 4
    //             : row.menu_type
    //     ]
    // },
    {
      columnKey: 'action',
      label: '操作',
      width: 180,
      align: 'center',
      slot: 'action',
      hideInPrint: true,
      hideInExport: true
    }
  ]);

  /** 当前编辑数据 */
  const current = ref(null);

  /** 是否显示编辑弹窗 */
  const showEdit = ref(false);

  /** 上级菜单id */
  const parentId = ref();

  /** 表格数据源 */
  const datasource = async ({ where }) => {
    try {
      const data = await listMenus({ ...where });
      return data;
      let tt = toTree({
        data,
        idField: 'ID',
        parentIdField: 'parentId'
      });
      console.log(tt, 11111111111);
      return tt;
    } catch (e) {
      EleMessage.error(e.message);
    }
  };

  /** 刷新表格 */
  const reload = (where) => {
    tableRef.value?.reload?.({ where });
  };

  /** 打开编辑弹窗 */
  const openEdit = (row, id) => {
    current.value = row ?? null;
    parentId.value = id;
    showEdit.value = true;
  };

  /** 删除单个 */
  const remove = (row) => {
    if (row.children?.length) {
      EleMessage.error('请先删除子节点');
      return;
    }
    ElMessageBox.confirm('确定要删除“' + row.title + '”吗?', '系统提示', {
      type: 'warning',
      draggable: true
    })
      .then(() => {
        const loading = EleMessage.loading({
          message: '请求中..',
          plain: true
        });
        removeMenu(row.menuId)
          .then((msg) => {
            loading.close();
            EleMessage.success(msg);
            reload();
          })
          .catch((e) => {
            loading.close();
            EleMessage.error(e.message);
          });
      })
      .catch(() => {});
  };

  /** 展开全部 */
  const expandAll = () => {
    tableRef.value?.toggleRowExpansionAll?.(true);
  };

  /** 折叠全部 */
  const foldAll = () => {
    tableRef.value?.toggleRowExpansionAll?.(false);
  };

  /** 判断是否是目录 */
  const isDirectory = (d) => {
    return d.parent_id === 0 && d.menu_type === 0;
  };
  const isMenu = (d) => {
    return d.parent_id !== 0 && d.menu_type === 0;
  };
</script>

<script>
  import * as MenuIcons from '@/layout/menu-icons';

  export default {
    name: 'SystemMenu',
    components: MenuIcons
  };
</script>
