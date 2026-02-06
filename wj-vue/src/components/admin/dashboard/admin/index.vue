<template>
  <div class="dashboard-editor-container">

    <panel-group @handleSetLineChartData="handleSetLineChartData" :info="dashboardInfo" />

    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <line-chart :chart-data="lineChartData" />
    </el-row>

    <el-row :gutter="32">
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <raddar-chart />
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <pie-chart :value="pieData" />
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <bar-chart :value="barData" />
        </div>
      </el-col>
    </el-row>

  </div>
</template>

<script>
import PanelGroup from './components/PanelGroup'
import RaddarChart from './components/RaddarChart'
import PieChart from './components/PieChart'
import LineChart from './components/LineChart'
import BarChart from './components/BarChart'

const lineChartData = {
  newVisitis: {
    expectedData: [100, 120, 161, 134, 105, 160, 165],
    actualData: [120, 82, 91, 154, 162, 140, 145]
  },
  messages: {
    expectedData: [200, 192, 120, 144, 160, 130, 140],
    actualData: [180, 160, 151, 106, 145, 150, 130]
  },
  purchases: {
    expectedData: [80, 100, 121, 104, 105, 90, 100],
    actualData: [120, 90, 100, 138, 142, 130, 130]
  },
  shoppings: {
    expectedData: [130, 140, 141, 142, 145, 150, 160],
    actualData: [120, 82, 91, 154, 162, 140, 130]
  }
}

export default {
  name: 'DashboardAdmin',
  components: {
    PanelGroup,
    RaddarChart,
    LineChart,
    PieChart,
    BarChart
  },
  data () {
    return {
      lineChartData: lineChartData.newVisitis,
      dashboardInfo: {
        visitCount: 0,
        userCount: 0,
        bookCount: 0,
        borrowCount: 0
      },
      // ↓↓↓ 新增：用于存储饼图数据 ↓↓↓
      pieData: [],
      barData: {} // 新增这个对象
    }
  },
  mounted () {
    this.loadStatus()
    // ↓↓↓ 新增：页面加载时请求饼图数据 ↓↓↓
    this.loadPieData()
    this.loadBarData()
  },
  methods: {
    handleSetLineChartData (type) {
      this.lineChartData = lineChartData[type]
    },
    loadStatus () {
      var _this = this
      this.$axios.get('/admin/dashboard/status').then(resp => {
        if (resp && resp.data.code === 200) {
          _this.dashboardInfo = resp.data.result
        }
      })
    },
    // ↓↓↓ 新增：请求后端饼图接口的方法 ↓↓↓
    loadPieData () {
      var _this = this
      this.$axios.get('/admin/dashboard/pie').then(resp => {
        if (resp && resp.data.code === 200) {
          _this.pieData = resp.data.result
        }
      })
    },
    loadBarData () {
      var _this = this
      this.$axios.get('/admin/dashboard/bar').then(resp => {
        if (resp && resp.data.code === 200) {
          _this.barData = resp.data.result
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
  padding: 32px;
  background-color: #f6f6f6;
  min-height: 100vh;
  position: relative;

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
  }
}

@media (max-width:1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}
</style>
