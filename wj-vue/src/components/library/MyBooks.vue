<template>
  <div style="width: 990px; margin: 0 auto; padding-top: 30px;">
    <el-card>
      <div slot="header">
        <span style="font-weight: bold; font-size: 20px;">📖 我的借阅书架</span>
      </div>
      <el-table :data="books" style="width: 100%">
        <el-table-column label="封面" width="120">
          <template slot-scope="scope">
            <img :src="scope.row.book.cover" style="width: 60px; height: 90px; border-radius: 4px;">
          </template>
        </el-table-column>
        <el-table-column label="书名" width="250">
          <template slot-scope="scope">
            <span style="font-weight: bold">{{ scope.row.book.title }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="book.author" label="作者" width="180"></el-table-column>
        <el-table-column prop="borrowTime" label="借阅时间" width="200"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button type="danger" size="small" @click="returnBook(scope.row.id)">归还图书</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
  export default {
    name: 'MyBooks',
    data () {
      return {
        books: []
      }
    },
    mounted () {
      this.loadMyBooks()
    },
    methods: {
      loadMyBooks () {
        // ⚠️ 修正：防止刷新页面后 user 为空导致报错
        // 如果 store 里没数据，我们就默认用用户 1 来测试
        let uid = (this.$store.state.user && this.$store.state.user.id) ? this.$store.state.user.id : 1

        this.$axios.get('/mybooks?uid=' + uid).then(resp => {
          if (resp && resp.data.code === 200) {
            this.books = resp.data.result
          }
        })
      },
      returnBook (recordId) {
        this.$confirm('确定要归还这本书吗?', '提示', { type: 'warning' }).then(() => {
          this.$axios.post('/return', { id: recordId }).then(resp => {
            if (resp && resp.data.code === 200) {
              this.$message.success('归还成功！')
              this.loadMyBooks() // 刷新列表
            }
          })
        })
      }
    }
  }
</script>
