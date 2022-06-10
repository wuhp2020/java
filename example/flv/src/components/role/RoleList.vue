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
                            角色管理 / 角色列表&nbsp;&nbsp;<span class="glyphicon glyphicon-eye-open"></span>
                        </header>
                        <table class="table table-striped border-top" id="sample_1">
                            <thead>
                                <tr>
                                    <th style="width:8px;"><input type="checkbox" class="group-checkable" data-set="#sample_1 .checkboxes" /></th>
                                    <th class="hidden-phone">角色ID</th>
                                    <th class="hidden-phone">角色名</th>
                                    <th class="hidden-phone">操作</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr class="odd gradeX" v-for="(item, index) in roles" :key="index">
                                    <td><input v-if="item.id && item.id.length > 1" type="checkbox" class="checkboxes" value="item.id" /></td>
                                    <td>{{item.id}}</td>
                                    <td class="hidden-phone">{{item.roleName}}</td>
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

    <div class="modal fade" id="roleupdate" tabindex="-1" role="dialog" 
        aria-labelledby="roleLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" 
                    data-dismiss="modal" aria-hidden="true">
                        &times;
                </button>
                <h4 class="modal-title" id="roleLabel">
                    角色管理 / 修改角色
                </h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                    <div class="form-group">
                        <label for="roleid" class="col-md-2 control-label">角色ID</label>
                        <div class="col-md-9">
                        <input type="text" disabled="disabled" class="form-control" id="updateroleid" v-bind:value="updateroleid">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="rolename" class="col-md-2 control-label">角色名</label>
                        <div class="col-md-9">
                        <input type="text" class="form-control" id="updaterolename" v-bind:value="updaterolename" 
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
         msg: 'rolelist',
         roles: [],
         updateroleid:"",
         updaterolename:"",
         resourcelist: [],
         current_page: 1, //当前页 
         pages: 5 //总页数 
      }
   },
   methods: {
        findByPage() {
            var roleQueryVO = {};
            // roleQueryVO.roleName = '';
            roleQueryVO.pageNum = this.current_page-1;
            roleQueryVO.pageSize = 8;
            this.$axios({
            method: "post",
            url: this.global.baseURL + "/role/findByPage",
            data: this.$qs.stringify(roleQueryVO)
            }).then(res => {
                if (res.status == '200' || res.status == 200) {
                    if (res.data.code == '200') {
                        this.roles = res.data.data.content;
                        this.pages = res.data.data.totalPages;
                        if (res.data.data.content.length != 8) {
                            //填充
                            var length = res.data.data.content.length;
                            for (var i = 7;i >= length; i--) {
                                this.roles.push({id:'~'});
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
        if(confirm("确定删除该角色?")){
          var ids = [];
          ids.push(id)
          this.$axios({
          method: "delete",
          url: this.global.baseURL + "/role/delete/"+ ids
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
            url: this.global.baseURL + "/role/findOne/"+ id
            }).then(res => {
                if (res.status == '200' || res.status == 200) {
                    if (res.data.code == '200') {
                        var roleDO = res.data.data
                        this.updateroleid = roleDO.id;
                        this.updaterolename = roleDO.roleName;
                        var resourcelistids = [];
                        for(var i = 0;i < roleDO.resourceVOs.length;i++){
                            resourcelistids.push(roleDO.resourceVOs[i].id)
                        }
                        $("#multiple_choice").val(resourcelistids).trigger('change');
                        $('#roleupdate').modal('show')
                    } else {
                        alert(res.data.message);
                    }
                } else {
                    alert("查询异常");
                }
            });
       },
       update() {
            var roleVO = {};
            var checkedresources = $("#multiple_choice").val();
            for(var i = 0; i<checkedresources.length;i++ ){
                roleVO["resourceVOs["+ i +"].id"] = checkedresources[i]
            }
            roleVO.id = $("#updateroleid").val()
            roleVO.roleName = $("#updaterolename").val()
            this.$axios({
            method: "put",
            url: this.global.baseURL + "/role/update",
            data: this.$qs.stringify(roleVO)
            }).then(res => {
                if (res.status == '200' || res.status == 200) {
                    if (res.data.code == '200') {
                        $('#roleupdate').modal('hide')
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