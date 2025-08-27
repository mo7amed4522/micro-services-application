import { menuToRoutes, eachTree } from 'ele-admin-plus/es';
import { HOME_PATH, LAYOUT_PATH, REDIRECT_PATH } from '@/config/setting';
import Layout from '@/layout/index.vue';
import RedirectLayout from '@/components/RedirectLayout/index.vue';
const modules = import.meta.glob('/src/views/**/index.vue');

/**
 * 静态路由
 */
export const routes = [
  {
    path: '/login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录' }
  },
  // 404
  {
    path: '/:path(.*)*',
    component: () => import('@/views/exception/404/index.vue')
  }

  // {
  //   path: '/finance',
  //   component: Layout,
  //   children: [
  //     {
  //       path: '/deposit/index',
  //       component: () => import('@/views/finance/deposit/index.vue'),
  //       meta: { title: '充值审核' }
  //     },
  //     {
  //       path: '/withdrawal/index',
  //       component: () => import('@/views/finance/withdrawal/index.vue'),
  //       meta: { title: '提现管理' }
  //     }
  //   ]
  // },
  //
  // {
  //   path: '/member',
  //   component: Layout,
  //   children: [
  //     {
  //       path: '/member/index',
  //       component: () => import('@/views/member/index/index.vue'),
  //       meta: { title: '会员列表' }
  //     },
  //     {
  //       path: '/risk/index',
  //       component: () => import('@/views/member/risk/index.vue'),
  //       meta: { title: '风险用户' }
  //     },
  //     {
  //       path: '/member/grade',
  //       component: () => import('@/views/member/grade/index.vue'),
  //       meta: { title: '会员分层' }
  //     },
  //     {
  //       path: '/member/level',
  //       component: () => import('@/views/member/level/index.vue'),
  //       meta: { title: '等级管理' }
  //     },
  //     {
  //       path: '/member/demoAccount',
  //       component: () => import('@/views/member/demoAccount/index.vue'),
  //       meta: { title: '陪玩账号' }
  //     }
  //   ]
  // }
];

/**
 * 根据菜单生成动态路由
 * @param menus 菜单数据
 * @param homePath 主页地址
 */
export function getMenuRoutes(menus, homePath) {
  const childRoutes = [
    // 用于刷新的路由
    {
      path: REDIRECT_PATH + '/:path(.*)',
      component: RedirectLayout,
      meta: { hideFooter: true }
    }
  ];
  const layoutRoutes = [
    {
      path: LAYOUT_PATH,
      component: Layout,
      redirect: HOME_PATH ?? homePath,
      children: childRoutes
    }
  ];
  // 路由铺平处理
  eachTree(menuToRoutes(menus, getComponent, routes), (route) => {
    const temp = Object.assign({}, route, { children: void 0 });
    if (route.meta?.layout === false) {
      layoutRoutes.push(temp); // 不需要外层布局的路由
    } else {
      childRoutes.push(temp); // 需要外层布局的路由
    }
  });
  return layoutRoutes;
}

/**
 * 解析路由组件
 * @param component 组件名称
 */
function getComponent(component) {
  if (component) {
    const module = modules[`/src/views${component}.vue`];
    if (!module) {
      return modules[`/src/views${component}/index.vue`];
    }
    return module;
  }
}
