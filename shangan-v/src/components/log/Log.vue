<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>日志管理</el-breadcrumb-item>
      <el-breadcrumb-item>日志查看</el-breadcrumb-item>
    </el-breadcrumb>
    <div style="margin-bottom: 15px; width: 100%">
      <div id="echartsBar" style="width: 60%;height: 500px;margin-bottom: 10px; display: inline-block"></div>
      <div style="width: 30%; height: 500px; display: inline-block;float: right; margin-right: 40px">
        <el-card style="width:100%; height: 140px;margin-bottom: 20px; float: right">
          <div class="grid-content grid-con-1">
            <i class="el-icon-s-custom grid-con-icon"></i>
            <div class="grid-cont-right">
              <div class="grid-num">1005</div>
              <div>商城用户量</div>
            </div>
          </div>
        </el-card>
        <el-card style="width:100%; height: 140px;margin-bottom: 20px; float: right">
          <div class="grid-content grid-con-2">
            <i class="el-icon-bell grid-con-icon"></i>
            <div class="grid-cont-right">
              <div class="grid-num">575</div>
              <div>商品数量</div>
            </div>
          </div>
        </el-card>
        <el-card style="width:100%; height: 140px;margin-bottom: 20px; float: right">
          <div class="grid-content grid-con-3">
            <i class="el-icon-shopping-bag-1 grid-con-icon"></i>
            <div class="grid-cont-right">
              <div class="grid-num">5113606</div>
              <div>商品交易额</div>
            </div>
          </div>
        </el-card>
      </div>
    </div>
    <div style="margin-bottom: 15px">
      <div id="echartsLine" style="width: 60%;height: 500px;margin-bottom: 10px; display: inline-block"></div>
      <div id="echartsPie" style="width: 30%;height: 500px;margin-bottom: 10px; display: inline-block"></div>
    </div>

  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  data() {
    return {
      queryInfo: {
        query: '',
        pagenum: 1,
        pagesize: 10
      },
      lastSevenSale: [],
      Logslist: [],
      total: 0,
      data: 50,
      barChart: '',
      optionBar: {
        title: {
          text: '近7天交易量'
        },
        xAxis: {
          type: 'category',
          data: []
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          data: [],
          type: 'bar',
          color: '#337ab7'
        }]
      },
      optionPie: {
        title: {
          text: '各订单状态数量'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          top: '10%',
          left: 'center'
        },
        series: [
          {
            name: '访问来源',
            type: 'pie',
            radius: ['30%', '60%'],
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '40',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: [
              { value: 40, name: '下单未支付' },
              { value: 20, name: '已支付' },
              { value: 50, name: '已发货' },
              { value: 30, name: '已出库' },
              { value: 25, name: '交易成功' }
            ]
          }
        ]
      },
      optionLine: {
        title: {
          text: '近7天访客量'
        },
        xAxis: {
          type: 'category',
          data: []
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          data: [10, 9, 15, 12, 13, 17, 16],
          type: 'line'
        }]
      }
    }
  },
  created() {
    this.getLastSevenDays()
  },
  mounted() {
    this.barChart = echarts.init(document.getElementById('echartsBar'))
    var myLineChart = echarts.init(document.getElementById('echartsLine'))
    var myPieChart = echarts.init(document.getElementById('echartsPie'))
    this.optionBar.xAxis.data = this.getLastSevenDays()
    this.optionLine.xAxis.data = this.getLastSevenDays()
    this.getLastSevenSale()
    this.optionBar && this.barChart.setOption(this.optionBar)
    this.optionPie && myPieChart.setOption(this.optionPie)
    this.optionLine && myLineChart.setOption(this.optionLine)
  },
  methods: {
    async getLastSevenSale() {
      const { data: res } = await this.$http.get('logs/total')
      if (res.resultCode !== 200) {
        return this.$message.error('获取日志列表失败！')
      }
      console.log(res.data)
      this.optionBar.series[0].data = res.data
    },
    handleSizeChange(newSize) {
      this.queryInfo.pagesize = newSize
      this.getGoodsList()
    },
    handleCurrentChange(newPage) {
      this.queryInfo.pagenum = newPage
      this.getGoodsList()
    },
    async getBarOption() {
      const { data: res } = await this.$http.get('/logs/total')

      if (res.resultCode !== 200) {
        return this.$message.error('获取日志统计失败！')
      }
    },
    getLastSevenDays() {
      var myDate = new Date() // 获取今天日期
      myDate.setDate(myDate.getDate() - 6)
      var dateArray = []
      var dateTemp
      var flag = 1
      for (var i = 0; i < 7; i++) {
        dateTemp = (myDate.getMonth() + 1) + '/' + myDate.getDate()
        dateArray.push(dateTemp)
        myDate.setDate(myDate.getDate() + flag)
      }
      return dateArray
    }
  },
  watch: {
    optionBar: {
      handler(newVal, oldVal) {
        if (this.barChart) {
          if (newVal) {
            this.barChart.setOption(newVal)
          } else {
            this.barChart.setOption(oldVal)
          }
        } else {}
      },
      deep: true // 对象内部属性的监听，关键。
    }
  }
}
</script>

<style lang="less" scoped>
  .grid-content {
    display: flex;
    align-items: center;
    height: 100px;
  }
  .grid-cont-right {
    flex: 1;
    text-align: center;
    font-size: 14px;
    color: #999;
  }

  .grid-num {
    font-size: 30px;
    font-weight: bold;
  }
  .grid-con-icon {
    font-size: 50px;
    width: 100px;
    height: 100px;
    text-align: center;
    line-height: 100px;
    color: #fff;
  }
  .grid-con-1 .grid-con-icon {
    background: rgb(45, 140, 240);
  }

  .grid-con-1 .grid-num {
    color: rgb(45, 140, 240);
  }
  .grid-con-2 .grid-con-icon {
    background: rgb(100, 213, 114);
  }
  .grid-con-2 .grid-num {
    color: rgb(45, 140, 240);
  }

  .grid-con-3 .grid-con-icon {
    background: rgb(242, 94, 67);
  }

  .grid-con-3 .grid-num {
    color: rgb(242, 94, 67);
  }
  //
  //[v-cloak] { display: none; }
</style>
