<template>
  <body class="login-body">
    <div class="container">
      <div class="form-signin">
        <h2 class="form-signin-heading">管理系统</h2>
        <div class="login-wrap">
            <input type="text" class="form-control" placeholder="用户名" autofocus id="username">
            <input type="password" class="form-control" placeholder="密码" id="password">
            <label class="checkbox">
                <span class="pull-right"> <a href="#">忘记密码?</a></span>
            </label>
            <button class="btn btn-lg btn-login btn-block" @click="login()">登录</button>
            <p>welcome use manage system</p>       
        </div>
      </div>
    </div>
  </body>
</template>

<script>
export default {
    data () {
      return {
        msg: 'login'
      }
    },
    methods: {

      login() {
        var user = {};
        user.username = $("#username").val();
        user.password = $("#password").val();

        //localStorage.setItem('username', user.username);
        //this.$router.push('/index')

        this.$axios({
          method: "post",
          url: this.global.baseURL + "/login",
          data: this.$qs.stringify(user)
        }).then(res => {
          if (res.data.status == '200' || res.data.status == 200) {
            localStorage.setItem('username', user.username);
            this.$router.push({path:'/index/userlist'})
          } else {
            alert(res.data.message)
            this.$router.push(this.$baseURL+ '/login')
            //this.$router.push({path:'/index/userlist'})
            this.$router.go(0)
          }
        });
      },
      reset() {
        $("#username").val("");
        $("#password").val("");
      }
        
    },
    mounted() {
        //模板编译后执行
        $(function () { 
          
        });
    }
}
</script>

<style lang="scss">

</style>