<template>
   <div>
    <section id="container" class="">
        <!--main content start-->
        <section class="wrapper">
            <!-- page start-->
            <div class="row">
                <div class="col-lg-12">
                    <section class="panel">
                        <header class="panel-heading">
                            用户管理 / 用户列表&nbsp;&nbsp;<span class="glyphicon glyphicon-user"></span>
                        </header>
                        <table class="table table-striped border-top" id="sample_1">
                            <thead>
                                <tr>
                                    <th style="width:8px;"><input type="checkbox" class="group-checkable" data-set="#sample_1 .checkboxes" /></th>
                                    <th class="hidden-phone">用户ID</th>
                                    <th class="hidden-phone">用户名</th>
                                    <th class="hidden-phone">密码</th>
                                    <th class="hidden-phone">操作</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr class="odd gradeX" v-for="(item, index) in users" :key="index">
                                    <td><input v-if="item.id && item.id.length > 1" type="checkbox" class="checkboxes" value="item.id" /></td>
                                    <td>{{item.id}}</td>
                                    <td class="hidden-phone"><a href="mailto:jhone-doe@gmail.com">{{item.username}}</a></td>
                                    <td class="hidden-phone"></td>
                                    <td class="hidden-phone">
                                        <button v-if="item.id && item.id.length > 1" class="btn label label-danger" @click="deleteById(item.id)">删除</button>
                                        <button v-if="item.id && item.id.length > 1" class="btn label label-warning" @click="updateFindById(item.id)">修改</button>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                        <v-pageable :current_page="current_page" :pages="pages" @jumpPage="jumpPage"></v-pageable>
                    </section>
                </div>
            </div>
            <!-- page end-->
        </section>
    </section>

    <div class="modal fade" id="userupdate" tabindex="-1" role="dialog" 
        aria-labelledby="userLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" 
                    data-dismiss="modal" aria-hidden="true">
                        &times;
                </button>
                <h4 class="modal-title" id="userLabel">
                    用户管理 / 修改用户
                </h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                    <div class="form-group">
                        <label for="userid" class="col-md-2 control-label">用户ID</label>
                        <div class="col-md-9">
                        <input type="text" disabled="disabled" class="form-control" id="updateuserid" v-bind:value="updateuserid">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="username" class="col-md-2 control-label">用户名</label>
                        <div class="col-md-9">
                        <input type="text" class="form-control" id="updateusername" v-bind:value="updateusername" 
                            placeholder="请输入用户名">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="password" class="col-md-2 control-label">密&nbsp;&nbsp;&nbsp;码</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" id="updatepassword" v-bind:value="updatepassword" 
                            placeholder="请输入密码">
                        </div>
                    </div>

                    <div class="form-group">
                    <label for="role" class="col-md-2 control-label">角&nbsp;&nbsp;&nbsp;色</label>
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
                <button type="button" class="btn btn-primary" @click="update()">保存</button>
            </div>
            </div>
        </div>
    </div>
   </div>
</template>

<script>
import Pageable from '../common/Pageable.vue'
export default {
   data () {
      return {
         msg: 'userlist',
         users: [],
         updateuserid:"",
         updateusername:"",
         updaterole:"",
         updatepassword:"",
         rolelist:[],
         current_page: 1, //当前页 
         pages: 5 //总页数 
      }
   },
   methods: {
       findByPage() {
            var userQueryVO = {};
            userQueryVO.pageNum = this.current_page-1;
            userQueryVO.pageSize = 10;
            this.$axios({
            method: "post",
            url: this.global.baseURL + "/user/findByPage",
            data: this.$qs.stringify(userQueryVO)
            }).then(res => {
                if (res.status == '200' || res.status == 200) {
                    if (res.data.code == '200') {
                        this.users = res.data.data.content;
                        this.pages = res.data.data.totalPages;
                        if (res.data.data.content.length != 8) {
                            //填充
                            var length = res.data.data.content.length;
                            for (var i = 7;i >= length; i--) {
                                this.users.push({id:'~'});
                            }
                        }
                    } else {
                        alert(res.data.message);
                    }
                } else {
                    alert("查询失败");
                }
            });
       },
       jumpPage: function(current_page) { 
            this.current_page = current_page;
            this.findByPage();
            this.selectlist();
       },
       findOne(id) {
          
       },
       deleteById(id) {
        if(confirm("确定删除该用户?")){
          var ids = [];
          ids.push(id)
          this.$axios({
          method: "delete",
          url: this.global.baseURL + "/user/delete/"+ ids
          }).then(res => {
              if (res.status == '200' || res.status == 200) {
                  if (res.data.code == '200') {
                      this.findByPage()
                  } else {
                      alert(res.data.message);
                  }
              } else {
                  alert("删除异常");
              }
          });
        }
       },
       updateFindById(id) {
            
            this.$axios({
            method: "get",
            url: this.global.baseURL + "/user/findOne/"+ id
            }).then(res => {
                if (res.status == '200' || res.status == 200) {
                    if (res.data.code == '200') {
                        var userDO = res.data.data
                        this.updateuserid = userDO.id;
                        this.updateusername = userDO.username;
                        this.updatepassword = userDO.password;
                        this.updatepassword = "";
                        var rolelistids = [];
                        for(var i = 0;i < userDO.roleVOs.length;i++){
                            rolelistids.push(userDO.roleVOs[i].id)
                        }
                        $("#multiple_choice").val(rolelistids).trigger('change');
                        $('#userupdate').modal('show')
                    } else {
                        alert(res.message);
                    }
                } else {
                    alert("查询异常");
                }
            });
       },
       update() {
            var userVO = {};
            userVO.id = $("#updateuserid").val()
            userVO.username = $("#updateusername").val()
            userVO.password = $("#updatepassword").val()
            var checkedRoles = $("#multiple_choice").val();
            for(var i = 0; i<checkedRoles.length;i++ ){
                userVO["roleVOs["+ i +"].id"] = checkedRoles[i]
            }
            
            this.$axios({
            method: "put",
            url: this.global.baseURL + "/user/update",
            data: this.$qs.stringify(userVO)
            }).then(res => {
                if (res.status == '200' || res.status == 200) {
                    if (res.data.code == '200') {
                        $('#userupdate').modal('hide')
                        this.findByPage()
                    } else {
                        alert(res.data.message);
                    }
                } else {
                    alert("更新异常");
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
   components: {
      'v-pageable': Pageable
   },
    mounted() {
        //模板编译后执行
        $(function () {

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

        this.findByPage();
        this.selectlist();
    }
}
</script>

<style lang="scss">
  
</style>