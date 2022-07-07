<template>
  <div class="modal fade" id="useradd" tabindex="-1" role="dialog" 
    aria-labelledby="loginLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" 
                data-dismiss="modal" aria-hidden="true">
                    &times;
            </button>
              <h4 class="modal-title" id="loginLabel">
                用户管理 / 添加用户
              </h4>
          </div>
          <div class="modal-body">
              <form class="form-horizontal" role="form">
                <div class="form-group">
                    <label for="username" class="col-md-2 control-label">用户名</label>
                    <div class="col-md-9">
                      <input type="text" class="form-control" id="username" 
                          placeholder="请输入用户名">
                    </div>
                </div>

                <div class="form-group">
                  <label for="password" class="col-md-2 control-label">密&nbsp;&nbsp;&nbsp;码</label>
                  <div class="col-md-9">
                    <input type="text" class="form-control" id="password" 
                      placeholder="请输入密码">
                  </div>
                </div>

                <div class="form-group">
                  <label for="rolelist" class="col-md-2 control-label">角&nbsp;&nbsp;&nbsp;色</label>
                  <div class="col-md-9">
                      <select class="form-control select2" id="multiple_choice" multiple="multiple">
                        <option v-for="item in rolelist" :value="item.id">{{item.roleName}}</option>
                      </select>
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
        msg: 'useradd',
        rolelist:[]
      }
    },
    methods: {
      reset() {
        $("#username").val("");
        $("#password").val("");
      },
      save() {
          var userVO = {};
          var checkedRoles = $("#multiple_choice").val();
          for(var i = 0; i<checkedRoles.length;i++ ){
             userVO["roleVOs["+ i +"].id"] = checkedRoles[i]
          }
          userVO.username = $("#username").val()
          userVO.password = $("#password").val()
          this.$axios({
          method: "post",
          url: this.global.baseURL + "/user/save",
          data: this.$qs.stringify(userVO)
          }).then(res => {
              if (res.status == '200' || res.status == 200) {
                  if (res.data.code == '200') {
                      $('#useradd').click();
                      this.$router.push({path:'/index/userlist'})
                  } else {
                      alert(res.data.message);
                  }
              } else {
                  alert("保存异常");
              }
          });
      },
      selectlist() {
        var roleQueryVO = {};
        roleQueryVO.pageNum = 0;
        roleQueryVO.pageSize = 10000;
        this.$axios({
          method: "post",
          dataType: 'json',
          url: this.global.baseURL + "/role/findByPage",
          data: this.$qs.stringify(roleQueryVO)
          }).then(res => {
              if (res.status == '200' || res.status == 200) {
                  if (res.data.code == '200') {
                      this.rolelist = res.data.data.content;
                  } else {
                      //alert(res.data.message);
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
          $('#useradd').modal({
            keyboard: true
          });

          // 初始化
          var multiple = $("#multiple_choice").select2({
            placeholder: "  请选择资源",
            width: "418.5px",
            allowClear: true
          });

          $('#multiple_all').click(function(event){
              event.preventDefault();
              var res = [];
              $(this).next('select').find('option').each(function(i,ele){
                  res.push($(ele).val())
              });
              $(multiple).val(res).trigger('change');
          });
        });

        this.selectlist()
    }
}
</script>

<style lang="scss">

</style>