<template>
  <div id="container" style="height: 100%"></div>
</template>

<script>
var echarts = require('echarts')
export default {
    data () {
      return {
        msg: 'echarts'
      }
    },
    methods: {
      init(){
        var dom = document.getElementById("container");
        var myChart = echarts.init(dom);
        var option = {
            xAxis: {
                type: 'category',
                data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
            },
            yAxis: {
                type: 'value'
            },
            series: [{
                data: [820, 932, 901, 934, 1290, 1330, 1320],
                type: 'line'
            }]
        };
        if (option && typeof option === "object") {
            myChart.setOption(option, true);
        }
      }
    },
    mounted() {
        this.init();
    }
}
</script>

<style lang="scss">

</style>