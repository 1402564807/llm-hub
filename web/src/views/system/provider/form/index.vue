<script setup lang="ts">
import { FormProps, DomainItem } from "@/views/system/provider/utils/types";
import { ref } from "vue";
import { usePublicHooks } from "@/views/system/hooks";
import { formRules } from "@/views/system/provider/utils/rule";
import ReCol from "@/components/ReCol";

const props = withDefaults(defineProps<FormProps>(), {
  formInline: () => ({
    title: "新增",
    providers: [],
    name: "",
    type: null,
    key: "",
    baseUrl: "",
    weight: 1,
    priority: 1,
    status: true,
    group: ["default"],
    models: [],
    domains: []
  })
});

const ruleFormRef = ref();
const { switchStyle } = usePublicHooks();
const newFormInline = ref(props.formInline);

function getRef() {
  return ruleFormRef.value;
}

const removeDomain = (item: DomainItem) => {
  const index = newFormInline.value.domains.indexOf(item);
  if (index !== -1) {
    newFormInline.value.domains.splice(index, 1);
  }
};

const addDomain = () => {
  newFormInline.value.domains.push({
    key: Date.now(),
    value: "",
    target: ""
  });
};

defineExpose({ getRef });
</script>

<template>
  <el-form
    ref="ruleFormRef"
    :model="newFormInline"
    :rules="formRules"
    label-width="82px"
  >
    <el-row :gutter="30">
      <re-col :value="12" :xs="24" :sm="24">
        <el-form-item label="名称" prop="name">
          <el-input
            v-model="newFormInline.name"
            clearable
            placeholder="请输入名称"
          />
        </el-form-item>
      </re-col>
      <re-col
        v-if="newFormInline.title === '新增'"
        :value="12"
        :xs="24"
        :sm="24"
      >
        <el-form-item label="服务商" prop="type">
          <el-select v-model="newFormInline.type" placeholder="请选择服务商">
            <el-option
              v-for="provider in newFormInline.providers"
              :key="provider.key"
              :value="provider.key"
              :label="provider.label"
            />
          </el-select>
        </el-form-item>
      </re-col>

      <re-col :value="24" :xs="24" :sm="24">
        <el-form-item
          label="密钥"
          prop="key"
          :required="newFormInline.title === '新增'"
        >
          <el-input
            v-model="newFormInline.key"
            clearable
            placeholder="请输入服务商API密钥"
          />
        </el-form-item>
      </re-col>
      <re-col :value="24" :xs="24" :sm="24">
        <el-form-item label="端点" prop="baseUrl">
          <el-input
            v-model="newFormInline.baseUrl"
            clearable
            placeholder="请输入端点 Base Url"
          />
        </el-form-item>
      </re-col>

      <re-col :value="24" :xs="24" :sm="24">
        <el-form-item label="可用模型" prop="models">
          <el-input-tag
            v-model="newFormInline.models"
            placeholder="请输入模型"
            aria-label="请在输入后单击回车键"
          />
        </el-form-item>
      </re-col>

      <re-col :value="12" :xs="24" :sm="24">
        <el-form-item label="权重" prop="weight">
          <el-input-number
            v-model="newFormInline.weight"
            clearable
            :min="1"
            placeholder="请输入权重"
          />
        </el-form-item>
      </re-col>

      <re-col :value="12" :xs="24" :sm="24">
        <el-form-item label="优先级" prop="priority">
          <el-input-number
            v-model="newFormInline.priority"
            clearable
            :min="1"
            placeholder="请输入优先级"
          />
        </el-form-item>
      </re-col>

      <re-col :value="24" :xs="24" :sm="24">
        <div class="flex justify-between">
          <div class="w-full">
            <el-form-item
              v-for="(domain, index) in newFormInline.domains"
              :key="domain.key"
              :label="'映射' + (index + 1)"
              :prop="'domains[' + index + '].value'"
              :rules="{
                required: true,
                message: '映射名称为空',
                trigger: 'blur'
              }"
            >
              <div class="flex justify-between w-full">
                <el-select v-model="domain.value" placeholder="选择模型">
                  <el-option
                    v-for="model in newFormInline.models"
                    :key="model"
                    :value="model"
                    :label="model"
                  />
                </el-select>
                <el-input
                  v-model="domain.target"
                  placeholder="服务商模型名"
                  class="ml-2"
                />
                <el-button
                  type="danger"
                  class="ml-2 mr-2"
                  @click.prevent="removeDomain(domain)"
                >
                  删 除
                </el-button>
              </div>
            </el-form-item>
          </div>
          <el-button class="right-0 mb-[18px]" @click="addDomain">
            添加模型映射
          </el-button>
        </div>
      </re-col>

      <re-col
        v-if="newFormInline.title === '新增'"
        :value="12"
        :xs="24"
        :sm="24"
      >
        <el-form-item label="状态">
          <el-switch
            v-model="newFormInline.status"
            inline-prompt
            :active-value="true"
            :inactive-value="false"
            active-text="启用"
            inactive-text="停用"
            :style="switchStyle"
          />
        </el-form-item>
      </re-col>
    </el-row>
  </el-form>
</template>

<style scoped lang="scss"></style>
