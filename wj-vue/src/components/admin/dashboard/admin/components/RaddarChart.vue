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
      default: '300px'
    },
    value: {
      type: Object,
      default: () => ({ indicators: [], stock: [], borrows: [] })
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
    setOptions ({ indicators, stock, borrows } = {}) {
      this.chart.setOption({
        title: {
          text: '分类馆藏与热度分析',
          left: 'center',
          top: '10',
          textStyle: {
            color: '#666',
            fontSize: 14
          }
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        radar: {
          radius: '55%', // 缩小一点半径
          center: ['50%', '40%'], // 核心修改：大幅向上移动中心点 (从 55% -> 40%)
          splitNumber: 8,
          indicator: indicators,
          name: {
            textStyle: {
              color: '#999'
            }
          }
        },
        legend: {
          left: 'center',
          bottom: '10', // 图例依然在底部，但因为图上移了，所以不会重叠
          data: ['馆藏数量', '借阅热度']
        },
        series: [{
          type: 'radar',
          symbolSize: 0,
          // ESLint 格式：多行属性
          areaStyle: {
            normal: {
              shadowBlur: 13,
              shadowColor: 'rgba(0,0,0,.2)',
              shadowOffsetX: 0,
              shadowOffsetY: 10,
              opacity: 1
            }
          },
          data: [
            {
              value: stock,
              name: '馆藏数量'
            },
            {
              value: borrows,
              name: '借阅热度'
            }
          ],
          animationDuration: 3000
        }]
      })
    }
  }
}
</script>
