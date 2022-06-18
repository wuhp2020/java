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
                            字典管理 / 字典列表&nbsp;&nbsp;<span class="glyphicon glyphicon-book"></span>
                        </header>
                        <table class="table table-striped border-top" id="sample_1">
                            <thead>
                                <tr>
                                    <th style="width:8px;"><input type="checkbox" class="group-checkable" data-set="#sample_1 .checkboxes" /></th>
                                    <th class="hidden-phone">字典ID</th>
                                    <th class="hidden-phone">字典类型</th>
                                    <th class="hidden-phone">字典编码</th>
                                    <th class="hidden-phone">级联字段值</th>
                                    <th class="hidden-phone">字典说明</th>
                                    <th class="hidden-phone">操作</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr class="odd gradeX" v-for="(item, index) in dicts" :key="index">
                                    <td><input v-if="item.id && item.id.length > 1" type="checkbox" class="checkboxes" value="item.id" /></td>
                                    <td>{{item.id}}</td>
                                    <td class="hidden-phone">{{item.type}}</td>
                                    <td class="hidden-phone">{{item.code}}</td>
                                    <td class="hidden-phone">{{item.relation}}</td>
                                    <td class="hidden-phone">{{item.name}}</td>
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

    <div class="modal fade" id="dictupdate" tabindex="-1" role="dialog" 
        aria-labelledby="dictLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" 
                    data-dismiss="modal" aria-hidden="true">
                        &times;
                </button>
                <h4 class="modal-title" id="dictLabel">
                    字典管理 / 修改字典
                </h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                    <div class="form-group">
                        <label for="userid" class="col-md-2 control-label">字典ID</label>
                        <div class="col-md-9">
                        <input type="text" disabled="disabled" class="form-control" id="updatedictid" v-bind:value="updatedictid">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="dicttype" class="col-md-2 control-label">字典类型</label>
                        <div class="col-md-9">
                        <input type="text" class="form-control" id="updatedicttype" v-bind:value="updatedicttype" 
                            placeholder="请输入字典类型">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="dictcode" class="col-md-2 control-label">字典编码</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" id="updatedictcode" v-bind:value="updatedictcode" 
                            placeholder="请输入字典编码">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="dictrelation" class="col-md-2 control-label">级联字段值</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" id="updatedictrelation" v-bind:value="updatedictrelation" 
                            placeholder="请输入级联字段值">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="dictlabel" class="col-md-2 control-label">字典说明</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" id="updatedictlabel" v-bind:value="updatedictlabel" 
                            placeholder="请输入字典说明">
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
         msg: 'dictlist',
         dicts: [],
         updatedictid:"",
         updatedicttype:"",
         updatedictcode:"",
         updatedictlabel:"",
         updatedictrelation:"",
         current_page: 1, //当前页 
         pages: 5 //总页数
      }
   },
   methods: {
       findByPage() {
            var dictQueryVO = {};
            dictQueryVO.pageNum = this.current_page-1;
            dictQueryVO.pageSize = 8;
            this.$axios({
            method: "post",
            url: this.global.baseURL + "/dict/findByPage",
            data: this.$qs.stringify(dictQueryVO)
            }).then(res => {
                if (res.status == '200' || res.status == 200) {
                    if (res.data.code == '200') {
                        this.dicts = res.data.data.content;
                        this.pages = res.data.data.totalPages;
                        if (res.data.data.content.length != 8) {
                            //填充
                            var length = res.data.data.content.length;
                            for (var i = 7;i >= length; i--) {
                                this.dicts.push({id:'~'});
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
            this.findByPage()
       },
       findOne(id) {
          
       },
       deleteById(id) {
        if(confirm("确定删除该字典?")){
          var ids = [];
          ids.push(id)
          this.$axios({
          method: "delete",
          url: this.global.baseURL + "/dict/delete/"+ ids
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
            url: this.global.baseURL + "/dict/findOne/"+ id
            }).then(res => {
                if (res.status == '200' || res.status == 200) {
                    if (res.data.code == '200') {
                        var dictDO = res.data.data
                        this.updatedictid = dictDO.id;
                        this.updatedicttype = dictDO.type;
                        this.updatedictcode = dictDO.code;
                        this.updatedictrelation = dictDO.relation;
                        this.updatedictlabel = dictDO.name;
                        $('#dictupdate').modal('show')
                    } else {
                        alert(res.data.message);
                    }
                } else {
                    alert("查询异常");
                }
            });
       },
       update() {
            var dictVO = {};
            dictVO.id = $("#updatedictid").val()
            dictVO.type = $("#updatedicttype").val()
            dictVO.code = $("#updatedictcode").val()
            dictVO.relation = $("#updatedictrelation").val()
            dictVO.name = $("#updatedictlabel").val()
            this.$axios({
            method: "put",
            url: this.global.baseURL + "/dict/update",
            data: this.$qs.stringify(dictVO)
            }).then(res => {
                if (res.status == '200' || res.status == 200) {
                    if (res.data.code == '200') {
                        $('#dictupdate').modal('hide')
                        this.findByPage()
                    } else {
                        alert(res.data.message);
                    }
                } else {
                    alert("更新异常");
                }
            });
       }
   },
   components: {
      'v-pageable': Pageable
   },
    mounted() {
        this.findByPage();
    }
}
</script>

<style lang="scss">
  
</style>