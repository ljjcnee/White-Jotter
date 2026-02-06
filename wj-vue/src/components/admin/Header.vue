<template>
  <el-card class="admin-header" body-style="padding: 0 20px; display: flex; align-items: center; height: 100%; box-sizing: border-box;">

    <span style="font-size: 20px; font-weight: bold; margin-left: -5px; color: #333;">
      智慧图书馆管理系统
    </span>

    <div style="flex: 1;"></div>

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
      logout () {
        var _this = this
        this.$axios.get('/logout').then(resp => {
          if (resp && resp.data.code === 200) {
            _this.$store.commit('logout')

            // ↓↓↓ 修改这里：退出后回到图书馆大厅 (首页) ↓↓↓
            _this.$router.replace('/library')

            // 清空路由，防止权限残留
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
    height: 60px; /* 稍微改矮一点，80px对于纯文字有点太高了，60px更显精致 */
    opacity: 1; /* 改回不透明，看起来更清晰 */
    min-width: 900px;
    background-color: #fff;
    border-radius: 0; /* 去掉圆角，让它跟浏览器顶部贴合更紧密 */
    box-shadow: 0 1px 4px rgba(0,21,41,.08); /* 调整阴影，更柔和 */
    border-bottom: none;
    margin-bottom: 0;
  }
</style>
