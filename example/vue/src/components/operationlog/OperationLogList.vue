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
                                    <th class="hidden-phone">日志ID</th>
                                    <th class="hidden-phone">请求IP</th>
                                    <th class="hidden-phone">请求时间</th>
                                    <th class="hidden-phone">请求路径</th>
                                    <th class="hidden-phone">用户名</th>
                                    <th class="hidden-phone">操作</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr class="odd gradeX" v-for="(item, index) in operationLogs" :key="index">
                                    <td><input v-if="item.id && item.id.length > 1" type="checkbox" class="checkboxes" value="item.id" /></td>
                                    <td>{{item.id}}</td>
                                    <td class="hidden-phone">{{item.clientIP}}</td>
                                    <td class="hidden-phone">{{item.requestTime}}</td>
                                    <td class="hidden-phone">{{item.requestURL}}</td>
                                    <td class="hidden-phone">{{item.username}}</td>
                                    <td class="hidden-phone">
                                        <button v-if="item.id && item.id.length > 1" class="btn label label-warning" @click="findById(item.id)">详情</button>
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

    <div class="modal fade" id="logDetail" tabindex="-1" role="dialog" 
        aria-labelledby="logLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" 
                    data-dismiss="modal" aria-hidden="true">
                        &times;
                </button>
                <h4 class="modal-title" id="logLabel">
                    日志管理 / 日志详情
                </h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                    <div class="form-group">
                        <label for="logid" class="col-md-2 control-label">日志ID</label>
                        <div class="col-md-9">
                        <input type="text" disabled="disabled" class="form-control" id="logid" v-bind:value="logid">
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label for="logClientIP" class="col-md-2 control-label">请求IP</label>
                        <div class="col-md-9">
                        <input type="text" disabled="disabled" class="form-control" id="logClientIP" v-bind:value="logClientIP">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="logRequestTime" class="col-md-2 control-label">请求时间</label>
                        <div class="col-md-9">
                        <input type="text" disabled="disabled" class="form-control" id="logRequestTime" v-bind:value="logRequestTime">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="logRequestURL" class="col-md-2 control-label">请求路径</label>
                        <div class="col-md-9">
                        <input type="text" disabled="disabled" class="form-control" id="logRequestURL" v-bind:value="logRequestURL">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="logusername" class="col-md-2 control-label">用户名</label>
                        <div class="col-md-9">
                        <input type="text" disabled="disabled" class="form-control" id="logusername" v-bind:value="logusername">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="logInputParams" class="col-md-2 control-label">入参</label>
                        <div class="col-md-9">
                        <textarea disabled="disabled" class="form-control" rows="5" id="logInputParams">{{logInputParams}}</textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="logOutputParams" class="col-md-2 control-label">出参</label>
                        <div class="col-md-9">
                        <textarea disabled="disabled" class="form-control" rows="5" id="logOutputParams">{{logOutputParams}}</textarea>
                        </div>
                    </div>
                </form>
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
         msg: 'operationlog',
         logid: '',
         logClientIP: '',
         logRequestTime: '',
         logRequestURL: '',
         logusername: '',
         logInputParams: '',
         logOutputParams: '',
         operationLogs: [],
         current_page: 1, //当前页 
         pages: 5 //总页数 
      }
   },
   methods: {
       findByPage() {
            var operationLogQueryVO = {};
            operationLogQueryVO.pageNum = this.current_page-1;
            operationLogQueryVO.pageSize = 8;
            this.$axios({
            method: "post",
            url: this.global.baseURL + "/operationlog/findByPage",
            data: this.$qs.stringify(operationLogQueryVO)
            }).then(res => {
                if (res.status == '200' || res.status == 200) {
                    if (res.data.code == '200') {
                        this.operationLogs = res.data.data.content;
                        this.pages = res.data.data.totalPages;
                        if (res.data.data.content.length != 8) {
                            //填充
                            var length = res.data.data.content.length;
                            for (var i = 7;i >= length; i--) {
                                this.operationLogs.push({id:'~'});
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
       findById(id) {
          this.$axios({
            method: "get",
            url: this.global.baseURL + "/operationlog/findOne/"+ id
            }).then(res => {
                if (res.status == '200' || res.status == 200) {
                    if (res.data.code == '200') {
                        var operationLogVO = res.data.data
                        this.logid = operationLogVO.id;
                        this.logClientIP = operationLogVO.clientIP;
                        this.logRequestTime = operationLogVO.requestTime;
                        this.logRequestURL = operationLogVO.requestURL;
                        this.logusername = operationLogVO.username;
                        this.logInputParams = operationLogVO.inputParams;
                        this.logOutputParams = operationLogVO.outputParams;
                        $('#logDetail').modal('show')
                    } else {
                        alert(res.data.message);
                    }
                } else {
                    alert("查询异常");
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