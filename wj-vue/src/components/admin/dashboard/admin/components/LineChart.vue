<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
// 1. Import 必须在最前
import echarts from 'echarts'
import resize from './mixins/resize'

// 2. Require 在后
require('echarts/theme/macarons') // echarts theme

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
      default: '350px'
    },
    autoResize: {
      type: Boolean,
      default: true
    },
    chartData: {
      type: Object,
      required: true
    }
  },
  data () {
    return {
      chart: null
    }
  },
  watch: {
    chartData: {
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
      this.setOptions(this.chartData)
    },
    setOptions ({ expectedData, actualData, dates } = {}) {
      this.chart.setOption({
        title: {
          text: '近七日借还趋势',
          textStyle: {
            fontSize: 14,
            color: '#666'
          },
          left: '10',
          top: '10'
        },
        xAxis: {
          data: dates,
          boundaryGap: false,
          axisTick: {
            show: false
          }
        },
        grid: {
          left: 10,
          right: 10,
          bottom: 20,
          top: 60, // 把图表往下推，留出头部空间
          containLabel: true
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          },
          padding: [5, 10]
        },
        yAxis: {
          axisTick: {
            show: false
          }
        },
        legend: {
          data: ['借书量', '还书量'],
          right: 10, // 图例放在右上角
          top: 10
        },
        series: [
          {
            name: '还书量',
            // 严格换行以通过 ESLint
            itemStyle: {
              normal: {
                color: '#FF005A',
                lineStyle: {
                  color: '#FF005A',
                  width: 2
                }
              }
            },
            smooth: true,
            type: 'line',
            data: expectedData,
            animationDuration: 2800,
            animationEasing: 'cubicInOut'
          },
          {
            name: '借书量',
            smooth: true,
            type: 'line',
            // 严格换行以通过 ESLint
            itemStyle: {
              normal: {
                color: '#3888fa',
                lineStyle: {
                  color: '#3888fa',
                  width: 2
                },
                areaStyle: {
                  color: '#f3f8ff'
                }
              }
            },
            data: actualData,
            animationDuration: 2800,
            animationEasing: 'quadraticOut'
          }
        ]
      })
    }
  }
}
</script>
