const Layout = () => import("@/layout/index.vue");

export default {
  path: "/system",
  name: "System",
  redirect: "/system/user",
  component: Layout,
  meta: {
    icon: "ri:information-line",
    showLink: true,
    title: "系统管理",
    rank: 8
  },
  children: [
    {
      path: "/system/user",
      name: "User",
      component: () => import("@/views/system/user/index.vue"),
      meta: {
        title: "用户管理"
      }
    },
    {
      path: "/system/price",
      name: "Price",
      component: () => import("@/views/system/price/index.vue"),
      meta: {
        title: "价格管理"
      }
    },
    {
      path: "/system/provider",
      name: "Provider",
      component: () => import("@/views/system/provider/index.vue"),
      meta: {
        title: "供应商管理"
      }
    }
  ]
} satisfies RouteConfigsTable;
