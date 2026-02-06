<template>
  <el-menu
    :default-active="currentPath"
    class="el-menu-admin"
    router
    mode="vertical"
    background-color="#545c64"
    text-color="#fff"
    active-text-color="#ffd04b"
    :collapse="isCollapse">
    <div style="height: 80px;"></div>

      <el-submenu v-for="(item,i) in navList" :key="i" :index="(i).toString()" style="text-align: left">
        <span slot="title" style="font-size: 17px;">
          <i :class="item.iconCls"></i>
          {{item.nameZh}}
        </span>
        <el-menu-item v-for="child in item.children" :key="child.path" :index="child.path">
          <i :class="child.icon"></i>
          {{ child.nameZh }}
        </el-menu-item>
      </el-submenu>

  </el-menu>
</template>

<script>
  export default {
    name: 'AdminMenu',
    data () {
      return {
        isCollapse: false,
        // ↓↓↓↓↓↓ 核心修改：手动定义精简后的菜单列表 ↓↓↓↓↓↓
        navList: [
          {
            nameZh: '运行情况',
            iconCls: 'el-icon-monitor',
            children: [
              { nameZh: '后台首页', icon: 'el-icon-s-data', path: '/admin/dashboard' }
            ]
          },
          {
            nameZh: '人员管理',
            iconCls: 'el-icon-user',
            children: [
              { nameZh: '用户列表', icon: 'el-icon-user-solid', path: '/admin/user/profile' },
              { nameZh: '角色配置', icon: 'el-icon-s-custom', path: '/admin/user/role' }
            ]
          },
          {
            nameZh: '内容管理', // 这里只保留图书，去掉了文章和广告
            iconCls: 'el-icon-document-copy',
            children: [
              { nameZh: '图书管理', icon: 'el-icon-notebook-2', path: '/admin/content/book' }
            ]
          }
        ]
        // ↑↑↑↑↑↑ 修改结束 ↑↑↑↑↑↑
      }
    },
    computed: {
      // 这里的 adminMenus 已经不需要了，我们直接用上面的 navList
      currentPath () {
        return this.$route.path
      }
    }
  }
</script>

<style scoped>
  .el-menu-admin {
    border-radius: 5px;
    height: 100%;
  }
</style>
