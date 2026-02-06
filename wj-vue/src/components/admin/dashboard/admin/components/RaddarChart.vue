<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
import resize from './mixins/resize'
require('echarts/theme/macarons') // echarts theme

const animationDuration = 3000

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
    }
  },
  data () {
    return {
      chart: null
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

      this.chart.setOption({
        title: {
          text: '各类图书借阅热度',
          left: 'center',
          top: '5',
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
          radius: '60%',
          center: ['50%', '55%'],
          splitNumber: 8,
          splitArea: {
            areaStyle: {
              color: 'rgba(127,95,132,.3)',
              opacity: 1,
              shadowBlur: 45,
              shadowColor: 'rgba(0,0,0,.5)',
              shadowOffsetX: 0,
              shadowOffsetY: 15
            }
          },
          // 修改点：指标改为中文图书分类
          indicator: [
            { name: '文学', max: 10000 },
            { name: '流行', max: 20000 },
            { name: '文化', max: 20000 },
            { name: '生活', max: 20000 },
            { name: '经管', max: 20000 },
            { name: '科技', max: 20000 }
          ]
        },
        legend: {
          left: 'center',
          bottom: '10',
          // 修改点：改为中文含义
          data: ['总库存', '已借出', '热度指数']
        },
        series: [{
          type: 'radar',
          symbolSize: 0,
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
              value: [5000, 7000, 12000, 11000, 15000, 14000],
              name: '总库存'
            },
            {
              value: [4000, 9000, 15000, 15000, 13000, 11000],
              name: '已借出'
            },
            {
              value: [5500, 11000, 12000, 15000, 12000, 12000],
              name: '热度指数'
            }
          ],
          animationDuration: animationDuration
        }]
      })
    }
  }
}
</script>
