import type { FormRules } from "element-plus";
import { reactive } from "vue";

/** 自定义表单规则校验 */
export const formRules = reactive(<FormRules>{
  name: [{ required: true, message: "名称为必填项", trigger: "blur" }],
  key: [{ required: true, message: "密钥为必填项", trigger: "blur" }],
  type: [{ required: true, message: "类型必填项", trigger: "blur" }]
});
