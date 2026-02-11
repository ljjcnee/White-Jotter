<template>
  <el-card class="admin-header" body-style="padding: 0 20px; display: flex; align-items: center; height: 100%; box-sizing: border-box;">

    <span style="font-size: 20px; font-weight: bold; margin-left: -5px; color: #333;">
      后台管理
    </span>

    <div style="flex: 1;"></div>

    <el-tooltip content="返回前台首页" placement="bottom" effect="light">
      <i class="el-icon-s-home"
         style="font-size: 24px; cursor: pointer; margin-right: 25px; color: #555;"
         @click="goHome">
      </i>
    </el-tooltip>

    <span
      v-on:click="logout"
      style="font-size: 14px; color: #555; cursor: pointer; font-weight: bold; letter-spacing: 1px;">
      退出登录
    </span>

  </el-card>
</template>

<script>
  import {createRouter} from '../../router'

  export default {
    name: 'Header',
    methods: {
      // 核心修复：将 '/index' 改为 '/library'
      goHome () {
        this.$router.push('/library')
      },
      logout () {
        var _this = this
        this.$axios.get('/logout').then(resp => {
          if (resp && resp.data.code === 200) {
            _this.$store.commit('logout')
            // 退出登录也建议跳回登录页或前台，这里设为登录页更合理，或者 /library
            _this.$router.replace('/login')
            // 清空路由
            const newRouter = createRouter()
            _this.$router.matcher = newRouter.matcher
          }
        }).catch(failResponse => {})
      }
    }
  }
</script>

<style scoped>
  .admin-header {
    height: 60px;
    opacity: 1;
    min-width: 900px;
    background-color: #fff;
    border-radius: 0;
    box-shadow: 0 1px 4px rgba(0,21,41,.08);
    border-bottom: none;
    margin-bottom: 0;
  }

  .el-icon-s-home:hover {
    color: #409EFF !important;
  }
</style>
