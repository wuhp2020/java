<template>
  <div class="modal fade" id="logsetupadd" tabindex="-1" role="dialog" 
    aria-labelledby="logsetupLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" 
                data-dismiss="modal" aria-hidden="true">
                    &times;
            </button>
              <h4 class="modal-title" id="logsetupLabel">
                日志管理 / 日志设置
              </h4>
          </div>
          <div class="modal-body">
              <form class="form-horizontal" role="form">
                <div class="form-group">
                    <label for="dictrelation" class="col-md-2 control-label">保留天数</label>
                    <div class="col-md-6">
                        <input list="test" type="text" class="form-control" id="days" v-bind:value="days">
                        <datalist id="test">
                            <option value="1"></option>
                            <option value="2"></option>
                            <option value="5"></option>
                            <option value="10"></option>
                            <option value="20"></option>
                            <option value="30"></option>
                            <option value="60"></option>
                        </datalist>
                    </div>
                    <label for="dictrelation" class="col-md-2 control-label">天</label>
                </div>
              </form>
          </div>
          <div class="modal-footer">
              <button type="button" class="btn btn-default" @click="reset()">重置</button>
              <button type="button" class="btn btn-primary" @click="saveLogSetup()">保存</button>
          </div>
        </div>
      </div>
  </div>
</template>

<script>
export default {
    data () {
      return {
        msg: 'logsetupadd',
        days: ''
      }
    },
    methods: {
      reset() {
        $("#days").val("");
      },
      saveLogSetup() {
          var operationLogSetupVO = {};
          operationLogSetupVO.days = $("#days").val()
          this.$axios({
          method: "post",
          url: this.global.baseURL + "/operationlog/saveLogSetup",
          data: this.$qs.stringify(operationLogSetupVO)
          }).then(res => {
              if (res.status == '200' || res.status == 200) {
                  if (res.data.code == '200') {
                      $('#logsetupadd').click();
                      this.$router.push({path:'/index/operationloglist'})
                  } else {
                      alert(res.data.message);
                  }
              } else {
                  alert("保存异常");
              }
          });
      },
      findLogSetup() {
          this.$axios({
          method: "get",
          url: this.global.baseURL + "/operationlog/findLogSetup"
          }).then(res => {
              if (res.status == '200' || res.status == 200) {
                  if (res.data.code == '200') {
                      var operationLogSetupVO = res.data.data
                      this.days = operationLogSetupVO.days;
                  } else {
                      alert(res.data.message);
                  }
              } else {
                  alert("保存异常");
              }
          });
      }
        
    },
    mounted() {
        //模板编译后执行
        this.findLogSetup()
        $(function () { 
          $('#logsetupadd').modal({
            keyboard: true
          })
        });
    }
}
</script>

<style lang="scss">

</style>