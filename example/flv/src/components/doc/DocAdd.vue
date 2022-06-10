<template>
  <div class="modal fade" id="docadd" tabindex="-1" role="dialog" 
    aria-labelledby="loginLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" 
                data-dismiss="modal" aria-hidden="true">
                    &times;
            </button>
              <h4 class="modal-title" id="loginLabel">
                文章管理 / 添加文章
              </h4>
          </div>
          <div class="modal-body">
              <form class="form-horizontal" role="form">
                <div class="form-group">
                    <label for="doctitle" class="col-md-2 control-label">标题</label>
                    <div class="col-md-9">
                      <input type="text" class="form-control" id="doctitle" 
                          placeholder="请输入标题">
                    </div>
                </div>

                <div class="form-group">
                  <label for="doccontent" class="col-md-2 control-label">内容</label>
                  <div class="col-md-9">
                    <input type="text" class="form-control" id="doccontent" 
                      placeholder="请输入内容">
                  </div>
                </div>
              </form>
          </div>
          <div class="modal-footer">
              <button type="button" class="btn btn-default" @click="reset()">重置</button>
              <button type="button" class="btn btn-primary" @click="login()">保存</button>
          </div>
        </div>
      </div>
  </div>
</template>

<script>
export default {
    data () {
      return {
        msg: 'docadd'
      }
    },
    methods: {
      reset() {
        $("#doctitle").val("");
        $("#doccontent").val("");
      }
        
    },
    mounted() {
        //模板编译后执行
        $(function () { 
          $('#docadd').modal({
            keyboard: true
          })
        });
    }
}
</script>

<style lang="scss">

</style>