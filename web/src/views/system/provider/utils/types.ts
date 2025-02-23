interface FormItemProps {
  id?: number /** 用于判断是`新增`还是`修改` */;
  providers: Array<{ key: number; label: string }> /** 服务商列表 */;
  title: string;
  type: number;
  name: string;
  key: string;
  baseUrl: string;
  models: Array<string>;
  group: Array<string>;
  modelMap?: object;
  domains: Array<DomainItem>;
  weight: number;
  priority: number;
  status: boolean;
}

interface FormProps {
  formInline: FormItemProps;
}

interface DomainItem {
  key: number;
  value: string;
  target: string;
}

export type { FormItemProps, FormProps, DomainItem };
