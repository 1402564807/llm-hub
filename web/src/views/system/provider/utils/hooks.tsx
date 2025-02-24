import type { Ref } from "vue";
import { computed, h, onMounted, reactive, ref, toRaw } from "vue";
import type { PaginationProps } from "@pureadmin/table";
import { usePublicHooks } from "@/views/system/hooks";
import dayjs from "dayjs";
import {
  addProvider,
  getProviderList,
  getProviders,
  removeProvider,
  updateProvider
} from "@/api/system";
import { addDialog } from "@/components/ReDialog/index";
import { deviceDetection } from "@pureadmin/utils";
import editForm from "@/views/system/provider/form/index.vue";
import { message } from "@/utils/message";
import type { FormItemProps } from "@/views/system/provider/utils/types";
import { ElMessageBox, ElTag } from "element-plus";

export function useProvider(tableRef: Ref) {
  const formRef = ref();
  const dataList = ref([]);
  const providers = ref([]);
  const loading = ref(true);
  const switchLoadMap = ref({});
  const { switchStyle } = usePublicHooks();
  const pagination = reactive<PaginationProps>({
    total: 0,
    pageSize: 10,
    currentPage: 1,
    background: true
  });

  const form = reactive({
    name: "",
    type: null,
    group: [],
    model: [],
    pageIndex: pagination.currentPage - 1,
    pageSize: pagination.pageSize,
    status: true
  });

  const buttonClass = computed(() => {
    return [
      "!h-[20px]",
      "reset-margin",
      "!text-gray-500",
      "dark:!text-white",
      "dark:hover:!text-primary"
    ];
  });

  const columns: TableColumnList = [
    {
      label: "编号",
      width: 70,
      cellRenderer: scope => {
        return <span>{scope.$index + 1}</span>;
      }
    },
    {
      label: "名称",
      prop: "name",
      minWidth: 130
    },
    {
      label: "供应商",
      prop: "type",
      minWidth: 130,
      cellRenderer: ({ row }) => (
        <ElTag round>
          {providers.value.findLast(it => it.key === row.type).label}
        </ElTag>
      )
    },
    {
      label: "分组",
      prop: "group",
      minWidth: 100,
      cellRenderer: ({ row }) =>
        row.group.map((it: string) => (
          <ElTag type="info" round>
            {it}
          </ElTag>
        ))
    },
    {
      label: "已用额度",
      prop: "usedQuota",
      minWidth: 100
    },
    {
      label: "优先级",
      prop: "priority",
      minWidth: 80
    },
    {
      label: "权重",
      prop: "weight",
      minWidth: 80
    },
    {
      label: "状态",
      minWidth: 130,
      cellRenderer: scope => (
        <el-switch
          size={scope.props.size === "small" ? "small" : "default"}
          loading={switchLoadMap.value[scope.index]?.loading}
          v-model={scope.row.status}
          active-value={true}
          inactive-value={false}
          active-text="已启用"
          inactive-text="已停用"
          inline-prompt
          style={switchStyle.value}
          onChange={() => onChange(scope as any)}
        />
      )
    },
    {
      label: "创建时间",
      minWidth: 130,
      formatter: ({ createdTime }) =>
        dayjs(createdTime).format("YYYY-MM-DD HH:mm:ss")
    },
    {
      label: "操作",
      fixed: "right",
      width: 150,
      slot: "operation"
    }
  ];

  const onChange = ({ row, index }) => {
    ElMessageBox.confirm(
      `确认要<strong>${
        row.status ? "启用" : "停用"
      }</strong><strong style='color:var(--el-color-primary)'>${
        row.name
      }</strong>服务商吗?`,
      "系统提示",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        dangerouslyUseHTMLString: true,
        draggable: true
      }
    )
      .then(() => {
        switchLoadMap.value[index] = Object.assign(
          {},
          switchLoadMap.value[index],
          {
            loading: true
          }
        );
        updateProvider({
          id: row.id,
          status: row.status
        }).then(({ success }) => {
          if (success) {
            setTimeout(() => {
              switchLoadMap.value[index] = Object.assign(
                {},
                switchLoadMap.value[index],
                {
                  loading: false
                }
              );
              message("已成功修改状态", {
                type: "success"
              });
            }, 300);
          }
        });
      })
      .catch(() => {
        row.status === 0 ? (row.status = 1) : (row.status = 0);
      });
  };

  const onSearch = async () => {
    loading.value = true;
    const { data } = await getProviderList(toRaw(form));
    dataList.value = data.rows;
    pagination.total = data.totalRowCount;
    // pagination.pageSize = data.pageSize;
    // pagination.currentPage = data.currentPage;

    setTimeout(() => {
      loading.value = false;
    }, 500);
  };

  const resetForm = formEl => {
    if (!formEl) return;
    formEl.resetFields();
    onSearch().then();
  };

  function handleDelete(row) {
    loading.value = true;
    removeProvider(row.id)
      .then(({ success }) => {
        if (success) {
          message(`您删除了编号为${row.id}的这条数据`, { type: "success" });
          onSearch().then();
        }
      })
      .finally(() => {
        setTimeout(() => {
          loading.value = false;
        }, 500);
      });
  }

  function handleSizeChange(val: number) {
    console.log(`${val} items per page`);
    pagination.pageSize = val;
    onSearch().then();
  }

  function handleCurrentChange(val: number) {
    console.log(`current page: ${val}`);
    pagination.currentPage = val;
    onSearch().then();
  }

  function openDialog(title = "新增", row?: FormItemProps) {
    const domains = [];
    if (row?.modelMap) {
      const keys = Object.keys(row.modelMap);
      for (let key of keys) {
        domains.push({
          key: Math.random() * 1000,
          value: key,
          target: row.modelMap[key]
        });
      }
    }

    addDialog({
      title: `${title}服务商`,
      props: {
        formInline: {
          title,
          providers: providers.value,
          id: row?.id ?? null,
          type: row?.type ?? null,
          name: row?.name ?? "",
          key: row?.key ?? "",
          baseUrl: row?.baseUrl ?? "",
          models: row?.models ?? [],
          group: row?.group ?? ["default"],
          domains: row?.domains ?? [],
          weight: row?.weight ?? 1,
          priority: row?.priority ?? 1,
          status: row?.status ?? true
        }
      },
      width: "46%",
      draggable: true,
      fullscreen: deviceDetection(),
      fullscreenIcon: true,
      closeOnClickModal: false,
      contentRenderer: () => h(editForm, { ref: formRef, formInline: null }),
      beforeSure: (done, { options }) => {
        const FormRef = formRef.value.getRef();
        const curData = options.props.formInline as FormItemProps;

        function chores() {
          message(`您${title}了服务商名为${curData.name}的这条数据`, {
            type: "success"
          });
          done(); // 关闭弹框
          onSearch().then(); // 刷新表格数据
        }

        FormRef.validate((valid: boolean) => {
          if (valid) {
            console.log("curData", curData);
            // 表单规则校验通过
            const modelMap = {};
            for (let domain of curData.domains) {
              modelMap[domain.value] = domain.target;
            }
            curData.modelMap = modelMap;
            if (title === "新增") {
              addProvider(curData).then(({ success }) => {
                if (success) {
                  chores();
                  return;
                }
                message(`${title}服务商${curData.name}失败`, {
                  type: "error"
                });
              });
            } else {
              updateProvider(curData).then(({ success }) => {
                if (success) {
                  chores();
                  return;
                }
                message(`${title}服务商${curData.name}失败`, {
                  type: "error"
                });
              });
            }
          }
        });
      }
    });
  }

  const findProvides = async () => {
    getProviders().then(({ success, data }) => {
      if (success) {
        providers.value = data;
      }
    });
  };

  onMounted(async () => {
    onSearch().then();
    findProvides().then();
  });

  return {
    formRef,
    loading,
    dataList,
    switchLoadMap,
    pagination,
    form,
    buttonClass,
    columns,
    onChange,
    onSearch,
    resetForm,
    openDialog,
    handleDelete,
    handleSizeChange,
    handleCurrentChange
  };
}
