<template>
  <div class="modal fade" id="roleadd" tabindex="-1" role="dialog" 
    aria-labelledby="loginLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" 
                data-dismiss="modal" aria-hidden="true">
                    &times;
            </button>
              <h4 class="modal-title" id="loginLabel">
                角色管理 / 添加角色
              </h4>
          </div>
          <div class="modal-body">
              <form class="form-horizontal" role="form">
                <div class="form-group">
                    <label for="rolename" class="col-md-2 control-label">角色名</label>
                    <div class="col-md-9">
                      <input type="text" class="form-control" id="rolename" 
                          placeholder="请输入角色名">
                    </div>
                </div>

                <div class="form-group">
                  <label for="resource" class="col-md-2 control-label">资&nbsp;&nbsp;&nbsp;源</label>
                  <div class="col-md-9">
                      <select class="form-control select2" id="multiple_choice" multiple="multiple">
                        <option v-for="item in resourcelist" :value="item.id">{{item.resourceName}}</option>
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
        msg: 'roleadd',
        resourcelist: []
      }
    },
    methods: {
      reset() {
        $("#rolename").val("");
        $("#resource").val("");
      },
      save() {
          var checkedresources = $("#multiple_choice").val();
          var roleVO = {};
          for(var i = 0; i<checkedresources.length;i++ ){
             roleVO["resourceVOs["+ i +"].id"] = checkedresources[i]
          }
          roleVO.roleName = $("#rolename").val()
          this.$axios({
          method: "post",
          dataType:"json",
          url: this.global.baseURL + "/role/save",
          data: this.$qs.stringify(roleVO)
          }).then(res => {
              if (res.status == '200' || res.status == 200) {
                  if (res.data.code == '200') {
                      $('#roleadd').click();
                      this.$router.push({path:'/index/rolelist'})
                  } else {
                      alert(res.data.message);
                  }
              } else {
                  alert("保存异常");
              }
          });
      },
      selectlist() {
        this.$axios({
          method: "get",
          dataType: 'json',
          url: this.global.baseURL + "/resource/findResource"
          }).then(res => {
              if (res.status == '200' || res.status == 200) {
                  if (res.data.code == '200') {
                      this.resourcelist = res.data.data;
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
          $('#roleadd').modal({
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