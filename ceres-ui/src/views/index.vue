<template>
  <div class="dashboard-container">
    <!-- 数据总览区 -->
    <el-row :gutter="24">
      <el-col
          :xs="24"
          :sm="12"
          :md="8"
          v-for="(card, index) in overviewCards"
          :key="index"
      >
        <el-card class="overview-card" shadow="hover">
          <div class="card-header">
            <span class="title">{{ card.title }}</span>
            <el-icon>
              <More/>
            </el-icon>
          </div>
          <div class="card-value">{{
              card.title === '回款金额' ? (card.value / 10000).toFixed(4) + '万' : card.value
            }}
          </div>
          <div
              class="trend-info"
              :class="{ 'up': card.trend > 0, 'down': card.trend < 0 }"
          >
            <el-icon>
              <ArrowUp v-if="card.trend > 0"/>
              <ArrowDown v-else/>
            </el-icon>
            {{ Math.abs(card.trend).toFixed(2) }}%
          </div>
          <div class="chart-container">
            <div ref="charts" class="chart"></div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 销售转化区 -->
    <el-row :gutter="24" class="analysis-section">
      <el-col :span="8">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>销售漏斗分析</span>
            </div>
          </template>
          <div ref="funnelChart" class="funnel-chart"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>执行中合同金额统计</span>
            </div>
          </template>
          <div ref="pieContractChart" class="pie-chart"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>客户来源分布</span>
            </div>
          </template>
          <div ref="pieChart" class="pie-chart"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 趋势分析区 -->
    <el-card class="trend-card">
      <template #header>
        <div class="trend-header">
          <span>趋势分析</span>
          <div class="trend-controls">
            <el-select v-model="queryYearValue" style="width: 120px" @change="updateData">
              <el-option label="本年" :value="new Date().getFullYear()"></el-option>
              <el-option label="去年" :value="new Date().getFullYear() - 1"></el-option>
              <el-option label="前年" :value="new Date().getFullYear() - 2"></el-option>
            </el-select>
            <el-radio-group v-model="trendMetric" style="margin-left: 16px" @change="handleTrendMetric">
              <el-radio-button label="contract">合同金额</el-radio-button>
              <el-radio-button label="payment">回款金额</el-radio-button>
              <!--              <el-radio-button label="customers">新增客户</el-radio-button>-->
            </el-radio-group>
          </div>
        </div>
      </template>
      <el-row>
        <el-col :span="18">
          <div ref="trendChart" class="trend-chart"></div>
        </el-col>
        <el-col :span="6">
          <div ref="yearChart" class="year-chart"></div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup>
import {ref, onMounted} from 'vue';
import * as echarts from 'echarts';
import {More, ArrowUp, ArrowDown, Open} from '@element-plus/icons-vue';
import {getDashboard} from "@/api/dashboard"

const trendMetric = ref('contract');
const queryYearValue = ref(new Date().getFullYear())
const charts = ref([]);
const funnelChart = ref(null);
const pieChart = ref(null);
const trendChart = ref(null);
const yearChart = ref(null);
const pieContractChart = ref(null);
let trendInstance = null

const overviewCards = ref([
  {title: '线索总数', value: '2,846', trend: 12.5, data: []},
  {title: '商机总数', value: '1,268', trend: 8.2, data: []},
  {title: '客户总数', value: '856', trend: -3.1, data: []},
  {title: '合同总数', value: '426', trend: 15.4, data: []},
  {title: '回款金额', value: '￥3,642,500', trend: 5.7, data: []},
  {title: '产品销量', value: '1,286', trend: -2.3, data: []},
]);
const contractAmountData = ref({})
const customerData = ref({})
const receivePaymentData = ref({})
const contractAmountTotalData = ref({})

const handleTrendMetric = (value) => {
  const customer = customerData.value
  let secondData = {};
  let legend = ['合同金额', '新增客户'];

  if (value === 'contract') {
    secondData = contractAmountData.value;
    legend[0] = '合同金额';
  } else {
    secondData = receivePaymentData.value
    legend[0] = '回款金额';
  }

  if (!trendInstance) {
    trendInstance = echarts.init(trendChart.value);
  }
  trendInstance.clear(); // 清空所有内容
  trendInstance.setOption({
    animation: true,
    tooltip: {
      trigger: 'axis',
      formatter: params => {
        const lines = params.map(p => {
          if (p.seriesType === 'bar') {
            // 金额数据，转万元并保留4位小数
            return `${p.seriesName}：￥${(p.value / 10000).toFixed(4)} 万`;
          } else {
            // 数量不处理
            return `${p.seriesName}：${p.value}`;
          }
        });
        return lines.join('<br/>');
      }
    },
    legend: {
      data: legend
    },
    xAxis: {
      type: 'category',
      data: customer.data.map(d => d.name)
    },
    yAxis: [
      {
        type: 'value',
        name: '金额',
        axisLabel: {
          formatter: value => `￥${(value / 10000).toFixed(4)} 万`
        }
      },
      {
        type: 'value',
        name: '数量',
        splitLine: {show: false}
      }
    ],
    series: [
      {
        name: legend[0],
        type: 'bar',
        data: secondData.data.map(d => d.value)
      },
      {
        name: legend[1],
        type: 'line',
        yAxisIndex: 1,
        data: customer.data.map(d => d.value)
      }
    ]
  }, true);

  window.addEventListener('resize', () => {
    trendInstance.resize()
  })
};

const updateData = () => {
  getDashboard(queryYearValue.value).then(res => {
    const countData = res.data.countData
    const yearAmountData = res.data.yearAmountData
    contractAmountData.value = res.data.contractAmountData
    customerData.value = res.data.customerData
    receivePaymentData.value = res.data.receivePaymentData
    contractAmountTotalData.value = res.data.contractAmountTotalData
    overviewCards.value.length = 0
    // 初始化迷你趋势图
    charts.value.forEach((chart, index) => {
      overviewCards.value.push({
        title: countData[index].title,
        value: countData[index].value,
        trend: countData[index].trend,
        data: countData[index].data
      })

      const chartInstance = echarts.init(chart);
      chartInstance.setOption({
        animation: false,
        grid: {top: 0, right: 0, bottom: 0, left: 0},
        xAxis: {show: false, type: 'category'},
        yAxis: {show: false},
        series: [{
          type: 'line',
          data: overviewCards.value[index].data.map(d => d.value),
          smooth: true,
          symbol: 'none',
          lineStyle: {color: '#1890FF'},
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              {offset: 0, color: 'rgba(24,144,255,0.3)'},
              {offset: 1, color: 'rgba(24,144,255,0)'}
            ])
          }
        }]
      });
      window.addEventListener('resize', () => {
        chartInstance.resize()
      })
    });

    // 初始化漏斗图
    const funnelInstance = echarts.init(funnelChart.value);
    // 计算最大值，确保至少为1，防止除零错误
    const funnelData = res.data.funnelData || []
    const maxValue = Math.max(...funnelData.map(item => item.value || 0), 1)
    funnelInstance.setOption({
      animation: false,
      tooltip: {trigger: 'item', formatter: '{b}: {c}'},
      series: [{
        name: '销售漏斗',
        type: 'funnel',
        left: '5%',
        top: 0,
        bottom: 0,
        width: '80%',
        min: 0,
        max: maxValue,  // 动态设置最大值
        minSize: '0%',
        maxSize: '100%',
        sort: 'descending',
        gap: 2,
        label: {
          show: true,
          position: 'right',
          formatter: function (params) {
            return params.name + ': ' + params.value;
          }
        },
        labelLine: {
          length: 10,
          lineStyle: {
            width: 1,
            type: 'solid'
          }
        },
        itemStyle: {
          borderColor: '#fff',
          borderWidth: 1
        },
        data: funnelData
      }]
    })
    window.addEventListener('resize', () => {
      funnelInstance.resize()
    })

    // 初始化饼图
    const pieInstance = echarts.init(pieChart.value);
    pieInstance.setOption({
      animation: false,
      tooltip: {trigger: 'item'},
      series: [{
        name: '客户来源',
        type: 'pie',
        radius: ['40%', '70%'],
        center: ['50%', '50%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: true,
          position: 'outside',
          formatter: function (params) {
            const percent = ((params.value / params.percent) * 100).toFixed(1); // 计算原始百分比
            return `{name|${params.name}}：{value|${params.value}个}\n{percent|(${percent}%)}`;
          },
          rich: {
            name: {
              fontSize: 12,
              fontWeight: 'bold'
            },
            value: {
              fontSize: 12,
              color: '#666'
            },
            percent: {
              fontSize: 12,
              color: '#999'
            }
          }
        },
        labelLine: {
          show: true,
          length: 10,
          length2: 15,
          lineStyle: {
            width: 1,
            type: 'solid'
          }
        },
        data: res.data.customerFromData
      }]
    });
    window.addEventListener('resize', () => {
      pieInstance.resize()
    })

    const yearPieInstance = echarts.init(yearChart.value);
    yearPieInstance.setOption({
      title: {
        text: '本年合同收款统计',
        subtext: `合同总额：¥${(yearAmountData.value / 10000).toFixed(4)} 万`,
        left: 'center',
        top: '0%',
        textStyle: {
          fontSize: 16,
          fontWeight: 'bold'
        },
        subtextStyle: {
          fontSize: 12,
          color: '#666'
        }
      },
      tooltip: {
        trigger: 'item',
        formatter: params => {
          const valueWan = (params.value / 10000).toFixed(4);
          return `${params.name}：¥${valueWan} 万 (${params.percent}%)`;
        }
      },
      legend: {
        orient: 'vertical',
        right: '0%',
        top: 'middle',
        itemGap: 10,
        textStyle: {
          fontSize: 12
        }
      },
      series: [
        {
          name: '年回款金额分布',
          type: 'pie',
          radius: ['30%', '60%'],
          center: ['35%', '55%'],  // 左移中心，为右侧标签腾出空间
          itemStyle: {
            borderRadius: 10,
            borderColor: '#fff',
            borderWidth: 2
          },
          label: {
            show: true,
            position: 'inside',
            formatter: params => {
              const valueWan = (params.value / 10000).toFixed(4);
              return `${params.name}\n¥${valueWan} 万\n(${params.percent}%)`;
            },
            fontSize: 12
          },
          labelLine: {
            show: true,
            length: 20,
            length2: 10
          },
          data: yearAmountData.data
        }
      ]
    });
    window.addEventListener('resize', () => {
      yearPieInstance.resize()
    })

    const pieContractInstance = echarts.init(pieContractChart.value);
    const formatWan = value => (value / 10000).toFixed(4)
    const pieContractData = contractAmountTotalData.value
    pieContractInstance.setOption({
      tooltip: {
        trigger: 'axis',
        axisPointer: {type: 'shadow'},
        formatter: params => {
          const lines = params.map(p =>
              `${p.seriesName}: ￥${formatWan(p.value)} 万`
          )
          return lines.join('<br/>')
        }
      },
      legend: {
        bottom: '0%',
        data: ['总额', '已回款', '未回款']
      },
      grid: {
        top: '5%',
        left: '3%',
        right: '3%',
        bottom: "10%",
        containLabel: true
      },
      xAxis: {
        type: 'category',
        data: ['金额情况']
      },
      yAxis: {
        type: 'value',
        axisLabel: {
          formatter: value => `￥${(value / 10000).toFixed(0)} 万`
        }
      },
      series: [
        {
          name: '总额',
          type: 'bar',
          data: [pieContractData.contractTotalAmount],
          label: {
            show: true,
            position: 'top',
            formatter: value => `￥${formatWan(pieContractData.contractTotalAmount)} 万`
          },
          itemStyle: {color: '#5470c6'}
        },
        {
          name: '已回款',
          type: 'bar',
          data: [pieContractData.receivedTotalAmount],
          label: {
            show: true,
            position: 'top',
            formatter: value => `￥${formatWan(pieContractData.receivedTotalAmount)} 万`
          },
          itemStyle: {color: '#91cc75'}
        },
        {
          name: '未回款',
          type: 'bar',
          data: [pieContractData.unreceivedTotalAmount],
          label: {
            show: true,
            position: 'top',
            formatter: value => `￥${formatWan(pieContractData.unreceivedTotalAmount)} 万`
          },
          itemStyle: {color: '#fac858'}
        }
      ]
    })
    window.addEventListener('resize', () => {
      pieContractInstance.resize()
    })

    handleTrendMetric(trendMetric.value)
  })
};

onMounted(() => {
  updateData()
});
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.overview-card {
  margin-bottom: 24px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.title {
  color: #606266;
  font-size: 14px;
}

.card-value {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 12px;
}

.trend-info {
  display: flex;
  align-items: center;
  font-size: 14px;
  margin-bottom: 12px;
}

.trend-info.up {
  color: #67c23a;
}

.trend-info.down {
  color: #f56c6c;
}

.trend-info .el-icon {
  margin-right: 4px;
}

.chart-container {
  height: 48px;
}

.chart {
  width: 100%;
  height: 100%;
}

.analysis-section {
  margin-bottom: 24px;
}

.chart-card {
  height: 100%;
}

.funnel-chart,
.pie-chart {
  height: 320px;
}

.trend-card {
  margin-bottom: 24px;
}

.trend-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.trend-controls {
  display: flex;
  align-items: center;
  gap: 16px;
}

.trend-chart {
  height: 320px;
}

.year-chart {
  height: 320px;
}
</style>