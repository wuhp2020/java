<template>
   <div>
      <div class="row" style="padding:0px 0px 3px">
         <div class="col-md-12">
            <nav class="navbar navbar-default" role="navigation" style="margin:0px;padding:0px;background-color:#8b9199 !important">
               <div class="navbar-header">
                  <a class="navbar-brand" href="#"><font size="5" color="#797979">&nbsp;&nbsp;&nbsp;&nbsp;管理系统&nbsp;&nbsp;<span class="glyphicon glyphicon-folder-open"></span></font></a>
               </div>
               <div>
                  <ul class="nav navbar-nav pull-right">
                     <li><a href="#"><font color="#FFFFFF">当前用户：{{username}}</font></a></li>
                     <li><router-link to="/login"><a href="#"><font color="#FFFFFF">退出</font></a></router-link></li>
                  </ul>
               </div>
            </nav>
            <ul class="nav nav-tabs nav-justified">
                <li class="dropdown active">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">文章管理<span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li data-toggle="tab"><router-link to="/index/docadd">添加文章</router-link></li>
                        <li data-toggle="tab"><router-link to="/index/doclist">文章列表</router-link></li>
                        <li data-toggle="tab"><router-link to="/index/docstatistics">文章统计</router-link></li>
                    </ul>
                </li>
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">账户管理<span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li data-toggle="tab"><router-link to="/index/useradd">添加用户</router-link></li>
                        <li data-toggle="tab"><router-link to="/index/userlist">用户列表</router-link></li>
                        <li class="divider"></li>
                        <li data-toggle="tab"><router-link to="/index/roleadd">添加角色</router-link></li>
                        <li data-toggle="tab"><router-link to="/index/rolelist">角色列表</router-link></li>
                    </ul>
                </li>
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">字典管理<span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li data-toggle="tab"><router-link to="/index/dictadd">添加字典</router-link></li>
                        <li data-toggle="tab"><router-link to="/index/dictlist">字典列表</router-link></li>
                    </ul>
                </li>

                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">日志管理<span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li data-toggle="tab"><router-link to="/index/operationloglist">日志列表</router-link></li>
                        <li data-toggle="tab"><router-link to="/index/operationlogsetup">日志设置</router-link></li>
                    </ul>
                </li>
            </ul>
         </div>
      </div>

      <div class="row">
         <div class="col-md-12" style="margin:0px;padding:5px 30px 5px 30px">
            <div class="panel panel-default">
               <div class="panel-body" style="height:500px">
                  <router-view></router-view>
               </div>
            </div>
         </div>
      </div>
   </div>
</template>

<script>
export default {
   data () {
      return {
         msg: 'index',
         username: localStorage.getItem('username')
      }
   },
   methods: {

   },
    mounted() {
        //模板编译后执行
        $(function () { 
            
        });
    }
}
</script>

<style lang="scss">
  notline {
      text-decoration:none;
  }
   navbar-default {
      background-color: #8b9199 !important;
   }
</style>