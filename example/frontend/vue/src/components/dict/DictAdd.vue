<template>
  <div class="modal fade" id="dictadd" tabindex="-1" role="dialog" 
    aria-labelledby="dictLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" 
                data-dismiss="modal" aria-hidden="true">
                    &times;
            </button>
              <h4 class="modal-title" id="loginLabel">
                字典管理 / 添加字典
              </h4>
          </div>
          <div class="modal-body">
              <form class="form-horizontal" role="form">
                <div class="form-group">
                    <label for="dicttype" class="col-md-2 control-label">类型</label>
                    <div class="col-md-9">
                      <input type="text" class="form-control" id="dicttype" 
                          placeholder="请输入类型">
                    </div>
                </div>

                <div class="form-group">
                  <label for="dictcode" class="col-md-2 control-label">编码</label>
                  <div class="col-md-9">
                    <input type="text" class="form-control" id="dictcode" 
                      placeholder="请输入编码">
                  </div>
                </div>

                <div class="form-group">
                  <label for="dictrelation" class="col-md-2 control-label">级联字段</label>
                  <div class="col-md-9">
                    <input type="text" class="form-control" id="dictrelation" 
                      placeholder="级联字段值">
                  </div>
                </div>

                <div class="form-group">
                  <label for="dictlabel" class="col-md-2 control-label">说明</label>
                  <div class="col-md-9">
                    <input type="text" class="form-control" id="dictlabel" 
                      placeholder="请输入说明">
                  </div>
                </div>
              </form>
          </div>
          <div class="modal-footer">
              <button type="button" class="btn btn-default" @click="reset()">重置</button>
              <button type="button" class="btn btn-primary" @click="save()">保存</button>
          </div>
        </div>
      </div>
  </div>
</template>

<script>
export default {
    data () {
      return {
        msg: 'dictadd'
      }
    },
    methods: {
      reset() {
        $("#dictcode").val("");
        $("#dicttype").val("");
        $("#dictlabel").val("");
        $("#dictrelation").val("");
      },
      save() {
          var dictVO = {};
          dictVO.code = $("#dictcode").val()
          dictVO.type = $("#dicttype").val()
          dictVO.name = $("#dictlabel").val()
          dictVO.relation = $("#dictrelation").val()
          this.$axios({
          method: "post",
          url: this.global.baseURL + "/dict/save",
          data: this.$qs.stringify(dictVO)
          }).then(res => {
              if (res.status == '200' || res.status == 200) {
                  if (res.data.code == '200') {
                      $('#dictadd').click();
                      this.$router.push({path:'/index/dictlist'})
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
        $(function () { 
          $('#dictadd').modal({
            keyboard: true
          })
        });
    }
}
</script>

<style lang="scss">

</style>