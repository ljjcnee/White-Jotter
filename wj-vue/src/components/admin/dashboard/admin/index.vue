<template>
  <div class="dashboard-editor-container">

    <panel-group :info="dashboardInfo" />

    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <line-chart :chart-data="lineChartData" />
    </el-row>

    <el-row :gutter="32">
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <raddar-chart :value="radarData" />
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

export default {
  name: 'DashboardAdmin',
  components: {
    PanelGroup,
    RaddarChart,
    PieChart,
    LineChart,
    BarChart
  },
  data () {
    return {
      // 1. 卡片数据
      dashboardInfo: { visitCount: 0, userCount: 0, bookCount: 0, borrowCount: 0 },
      // 2. 饼图数据
      pieData: [],
      // 3. 柱状图数据
      barData: {},
      // 4. 折线图数据 (新增)
      lineChartData: { expectedData: [], actualData: [], dates: [] },
      // 5. 雷达图数据 (新增)
      radarData: { indicators: [], stock: [], borrows: [] }
    }
  },
  mounted () {
    this.loadAllData()
  },
  methods: {
    loadAllData () {
      var _this = this

      // 请求卡片数据
      this.$axios.get('/admin/dashboard/status').then(resp => {
        if (resp && resp.data.code === 200) _this.dashboardInfo = resp.data.result
      })
      // 请求饼图数据
      this.$axios.get('/admin/dashboard/pie').then(resp => {
        if (resp && resp.data.code === 200) _this.pieData = resp.data.result
      })
      // 请求柱状图数据
      this.$axios.get('/admin/dashboard/bar').then(resp => {
        if (resp && resp.data.code === 200) _this.barData = resp.data.result
      })
      // 请求折线图数据
      this.$axios.get('/admin/dashboard/line').then(resp => {
        if (resp && resp.data.code === 200) _this.lineChartData = resp.data.result
      })
      // 请求雷达图数据
      this.$axios.get('/admin/dashboard/radar').then(resp => {
        if (resp && resp.data.code === 200) _this.radarData = resp.data.result
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
