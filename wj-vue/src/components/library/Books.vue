<template>
  <div>
    <el-row style="height: 80px; display: flex; align-items: center; width: 990px; margin: 0 auto;">
      <search-bar @onSearch="searchResult" ref="searchBar"></search-bar>
      <view-switch class="switch" style="margin-left: auto;"></view-switch>
    </el-row>

    <el-row>
      <div class="books-area">
        <el-tooltip effect="dark" placement="right"
                    v-for="item in books.slice((currentPage-1)*pagesize,currentPage*pagesize)"
                    :key="item.id">

          <p slot="content" style="font-size: 14px;margin-bottom: 6px;">{{item.title}}</p>
          <p slot="content" style="font-size: 13px;margin-bottom: 6px">
            <span>{{item.author}}</span> /
            <span>{{item.date}}</span> /
            <span>{{item.press}}</span>
          </p>
          <p slot="content" style="width: 300px" class="abstract">{{item.abs}}</p>

          <el-card class="book-card" bodyStyle="padding:10px" shadow="hover">

            <div class="cover">
              <img :src="item.cover" alt="封面">
            </div>

            <div class="info">
              <div class="title">
                <a href="">{{item.title}}</a>
              </div>
              <div class="author-row">
                <span class="author-name">{{item.author}}</span>
                <span class="stock-info">
                  <i class="el-icon-box" style="margin-right: 2px;"></i>{{item.nums}}
                </span>
              </div>
            </div>

            <div class="action-row">
              <el-button
                size="small"
                type="primary"
                round
                class="borrow-btn"
                @click="borrowBook(item)">
                立即借阅
              </el-button>
            </div>

          </el-card>
        </el-tooltip>
      </div>
    </el-row>

    <el-row style="margin-top: 20px;">
      <el-pagination
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-size="pagesize"
        :total="books.length">
      </el-pagination>
    </el-row>
  </div>
</template>

<script>
  import SearchBar from './SearchBar'
  import ViewSwitch from './ViewSwitch'

  export default {
    name: 'Books',
    components: {SearchBar, ViewSwitch},
    data () {
      return {
        books: [],
        currentPage: 1,
        pagesize: 18
      }
    },
    mounted: function () {
      this.loadBooks()
    },
    methods: {
      loadBooks () {
        var _this = this
        this.$axios.get('/books').then(resp => {
          if (resp && resp.data.code === 200) {
            _this.books = resp.data.result
          }
        })
      },
      handleCurrentChange: function (currentPage) {
        this.currentPage = currentPage
      },
      searchResult () {
        var _this = this
        this.$axios
          .get('/search?keywords=' + this.$refs.searchBar.keywords, {
          }).then(resp => {
          if (resp && resp.data.code === 200) {
            _this.books = resp.data.result
          }
        })
      },
      borrowBook (item) {
        if (item.nums <= 0) {
          this.$message.error('手慢了，库存不足！')
          return
        }

        // 获取用户ID，如果获取不到则默认为1
        let uid = (this.$store.state.user && this.$store.state.user.id) ? this.$store.state.user.id : 1

        this.$confirm('确定要借阅 《' + item.title + '》 吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$axios.post('/borrow', {
            uid: uid, // 修复点：这里只留一个空格
            bid: item.id // 修复点：这里只留一个空格
          }).then(resp => {
            if (resp && resp.data.code === 200) {
              this.$message({
                type: 'success',
                message: '借阅成功！库存已自动扣减'
              })
              this.loadBooks()
            } else {
              this.$message.error(resp.data.message)
            }
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消借阅'
          })
        })
      }
    }
  }
</script>

<style scoped>
  /* 核心修复点：使用固定宽度 990px */
  /* 这样在 1366px 的屏幕上，左边会自动留出 (1366-990)/2 = 188px 的空间 */
  /* 这个空间足够放下左边的侧边栏（通常侧边栏只有 150px 左右） */
  .books-area {
    width: 990px;
    margin-left: auto;
    margin-right: auto;
    display: flex;
    flex-wrap: wrap;
    justify-content: flex-start; /* 改回 flex-start，让书从左往右排 */
    gap: 20px; /* 卡片间距 */
  }

  .book-card {
    width: 230px; /* 略微调整宽度配合间距 */
    height: 380px;
    border-radius: 12px;
    border: none;
    transition: all 0.3s ease;
    position: relative;
    overflow: hidden;
  }

  .book-card:hover {
    transform: translateY(-6px);
    box-shadow: 0 12px 24px rgba(0, 0, 0, 0.15);
  }

  .cover {
    width: 100%;
    height: 220px;
    overflow: hidden;
    border-radius: 6px;
    margin-bottom: 12px;
  }

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: transform 0.5s;
  }

  .book-card:hover img {
    transform: scale(1.05);
  }

  .title {
    font-size: 15px;
    font-weight: bold;
    color: #333;
    text-align: left;
    height: 22px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    margin-bottom: 6px;
  }

  .title a {
    color: #2c3e50;
    text-decoration: none;
  }

  .author-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;
    font-size: 13px;
  }

  .author-name {
    color: #666;
    max-width: 120px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .stock-info {
    color: #13ce66;
    font-weight: bold;
    background: #f0f9eb;
    padding: 2px 6px;
    border-radius: 4px;
    font-size: 12px;
  }

  .action-row {
    text-align: center;
  }

  .borrow-btn {
    width: 80%;
    font-weight: bold;
    box-shadow: 0 4px 6px rgba(64, 158, 255, 0.3);
  }

  .abstract {
    display: block;
    line-height: 17px;
  }
</style>
