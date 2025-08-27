import { markRaw } from 'vue';
import { ElCarousel, ElCarouselItem, ElIcon } from 'element-plus/es';
import { EleAdminLayout, EleSteps } from 'ele-admin-plus/es';
import { UserOutlined, CheckCircleOutlined } from '@/components/icons';

/**
 * 标识 js 变量名
 */
export class JsVar {
  __isJsVar = true;

  constructor(props) {
    /** 生成 ElForm 代码时显示的变量名称 */
    this.name = props.name;
    /** 生成 ProForm 代码时显示的变量名称 */
    this.proName = props.proName;
    /** 生成 ElForm 代码时添加的声明代码 */
    this.code = props.code;
    /** 生成 ProForm 代码时添加的声明代码 */
    this.proCode = props.proCode;
    /** 生成 ElForm 代码时添加的导入代码 */
    this.imports = props.imports;
    /** 生成 ProForm 代码时添加的导入代码 */
    this.proImports = props.proImports;
    /** ProForm 预览界面获取对应的 js 变量 */
    this.jsCode = props.jsCode;
  }
}

/**
 * 模板库使用到的 js 变量
 */
export const templateJsVar = [
  new JsVar({
    name: 'ElCarousel',
    proName: 'markRaw(ElCarousel)',
    imports: [],
    proImports: [
      `import { markRaw } from 'vue';`,
      `import { ElCarousel } from 'element-plus/es';`
    ],
    jsCode: () => markRaw(ElCarousel)
  }),
  new JsVar({
    name: 'ElCarouselItem',
    proName: 'markRaw(ElCarouselItem)',
    imports: [],
    proImports: [
      `import { markRaw } from 'vue';`,
      `import { ElCarouselItem } from 'element-plus/es';`
    ],
    jsCode: () => markRaw(ElCarouselItem)
  }),
  new JsVar({
    name: 'EleAdminLayout',
    proName: 'markRaw(EleAdminLayout)',
    imports: [],
    proImports: [
      `import { markRaw } from 'vue';`,
      `import { EleAdminLayout } from 'ele-admin-plus/es';`
    ],
    jsCode: () => markRaw(EleAdminLayout)
  }),
  new JsVar({
    name: 'UserOutlined',
    proName: 'markRaw(UserOutlined)',
    imports: [`import { UserOutlined } from '@/components/icons';`],
    proImports: [
      `import { markRaw } from 'vue';`,
      `import { UserOutlined } from '@/components/icons';`
    ],
    jsCode: () => markRaw(UserOutlined)
  }),
  new JsVar({
    name: 'EleSteps',
    proName: 'markRaw(EleSteps)',
    imports: [],
    proImports: [
      `import { markRaw } from 'vue';`,
      `import { EleSteps } from 'ele-admin-plus/es';`
    ],
    jsCode: () => markRaw(EleSteps)
  }),
  new JsVar({
    name: 'demoStepsActive',
    proName: '0',
    code: `/** 步骤条选中步骤 */
const demoStepsActive = ref(0);`,
    proCode: '',
    jsCode: () => 0
  }),
  new JsVar({
    name: 'prevDemoSteps',
    proName: 'prevDemoSteps',
    code: `/** 步骤条上一步 */
const prevDemoSteps = () => {
  if (demoStepsActive.value > 0) {
    demoStepsActive.value--;
  }
};`,
    proCode: `/** 步骤条上一步 */
const prevDemoSteps = () => {
  const item = formItems.value?.find?.((d) => d.prop === 'demoSteps');
  if (item && item.props && item.props.active > 0) {
    item.props.active--;
  }
};`,
    jsCode: (_form, formItems) => {
      return () => {
        if (formItems && formItems.value) {
          const item = formItems.value.find((d) => d.prop === 'demoSteps');
          if (item && item.props && item.props.active > 0) {
            item.props.active--;
          }
        }
      };
    }
  }),
  new JsVar({
    name: 'nextDemoSteps',
    proName: 'nextDemoSteps',
    code: `/** 步骤条下一步 */
const nextDemoSteps = () => {
  if (demoStepsActive.value < 3) {
    demoStepsActive.value++;
  }
};`,
    proCode: `/** 步骤条下一步 */
const nextDemoSteps = () => {
  const item = formItems.value?.find?.((d) => d.prop === 'demoSteps');
  if (item && item.props && item.props.active < (item.props.items?.length ?? 2)) {
    item.props.active++;
  }
};`,
    jsCode: (_form, formItems) => {
      return () => {
        if (formItems && formItems.value) {
          const item = formItems.value.find((d) => d.prop === 'demoSteps');
          const sizes = item?.props?.items?.length ?? 3;
          if (item && item.props && item.props.active < sizes) {
            item.props.active++;
          }
        }
      };
    }
  }),
  new JsVar({
    name: 'isSearchExpand',
    proName: '() => isSearchExpand.value',
    code: `/** 是否展开全部搜索表单项 */
const isSearchExpand = ref(false);`,
    proCode: `/** 是否展开全部搜索表单项 */
const isSearchExpand = ref(false);`,
    imports: [`import { ArrowDown, ArrowUp } from '@/components/icons';`],
    proImports: [`import { ArrowDown, ArrowUp } from '@/components/icons';`],
    jsCode: (_form, _formItems, _formProps, isSearchExpand) => {
      return () => isSearchExpand?.value;
    }
  }),
  new JsVar({
    name: `isSearchExpand ? 'flex-end' : 'flex-start'`,
    proName: `isSearchExpand ? 'flex-end' : 'flex-start'`,
    code: `/** 是否展开全部搜索表单项 */
const isSearchExpand = ref(false);`,
    proCode: `/** 是否展开全部搜索表单项 */
const isSearchExpand = ref(false);`,
    jsCode: (_form, _formItems, _formProps, isSearchExpand) => {
      return isSearchExpand?.value ? 'flex-end' : 'flex-start';
    }
  }),
  new JsVar({
    name: 'isSearchExpand ? 2 * 8 : 8',
    proName: 'isSearchExpand ? (3 - (formItems.length % 3)) * 8 : 8',
    code: `/** 是否展开全部搜索表单项 */
const isSearchExpand = ref(false);`,
    proCode: `/** 是否展开全部搜索表单项 */
const isSearchExpand = ref(false);`,
    jsCode: (_form, formItems, formProps, isSearchExpand) => {
      const grid = formProps?.grid;
      const span = (grid ? (grid === true ? 12 : grid?.span) : void 0) ?? 24;
      if (!isSearchExpand?.value) {
        return span;
      }
      const gridNum = 24 / span;
      return (gridNum - ((formItems?.value?.length || 0) % gridNum)) * span;
    }
  }),
  new JsVar({
    name: 'CheckCircleOutlined',
    proName: 'markRaw(CheckCircleOutlined)',
    imports: [`import { CheckCircleOutlined } from '@/components/icons';`],
    proImports: [
      `import { markRaw } from 'vue';`,
      `import { CheckCircleOutlined } from '@/components/icons';`
    ],
    jsCode: () => markRaw(CheckCircleOutlined)
  }),
  new JsVar({
    name: 'ElIcon',
    proName: 'markRaw(ElIcon)',
    imports: [],
    proImports: [
      `import { markRaw } from 'vue';`,
      `import { ElIcon } from 'element-plus/es';`
    ],
    jsCode: () => markRaw(ElIcon)
  })
];

/**
 * 根据名字获取模板 JsVar
 * @param name 名字
 */
export function getTplJsVar(name) {
  return templateJsVar.find((v) => v.name == name);
}

/**
 * JSON 数据转换 JsVar 对象
 * @param jv JSON 解析后的值
 */
export function transformJsVar(jv) {
  if (jv != null && typeof jv === 'object') {
    if (jv instanceof JsVar) {
      return jv;
    }
    if (jv.__isJsVar === true && (jv.name != null || jv.proName != null)) {
      const tplVar = templateJsVar.find(
        (v) =>
          v.name == jv.name &&
          v.proName == jv.proName &&
          v.code == jv.code &&
          v.proCode == jv.proCode
      );
      if (tplVar != null) {
        return tplVar;
      }
      return new JsVar(jv);
    }
  }
}

/**
 * 生成 ProForm 代码时的 JSON 数据转换 JsVar 对象
 * @param jv JSON 解析后的值
 */
export function transformProJsVar(jv) {
  const jsVar = transformJsVar(jv);
  if (jsVar != null) {
    return new JsVar({ ...jsVar, name: jsVar.proName });
  }
  return jsVar;
}
