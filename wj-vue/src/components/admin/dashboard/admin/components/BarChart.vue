<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
import resize from './mixins/resize'
require('echarts/theme/macarons') // echarts theme

const animationDuration = 6000

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '300px'
    },
    // 接收父组件数据
    value: {
      type: Object,
      default: () => {
        return {
          dates: [],
          values: []
        }
      }
    }
  },
  data () {
    return {
      chart: null
    }
  },
  watch: {
    value: {
      deep: true,
      handler (val) {
        this.setOptions(val)
      }
    }
  },
  mounted () {
    this.$nextTick(() => {
      this.initChart()
    })
  },
  beforeDestroy () {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    initChart () {
      this.chart = echarts.init(this.$el, 'macarons')
      this.setOptions(this.value)
    },
    setOptions ({ dates, values } = {}) {
      this.chart.setOption({
        title: {
          text: '近七日借阅趋势',
          left: 'center',
          top: '5',
          textStyle: { color: '#666', fontSize: 14 }
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'shadow' }
        },
        grid: {
          top: 40,
          left: '2%',
          right: '2%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: [{
          type: 'category',
          data: dates, // 使用真实日期
          axisTick: { alignWithLabel: true }
        }],
        yAxis: [{
          type: 'value',
          axisTick: { show: false }
        }],
        series: [{
          name: '借阅量',
          type: 'bar',
          stack: 'vistors',
          barWidth: '60%',
          data: values, // 使用真实数据
          animationDuration
        }]
      })
    }
  }
}
</script>
