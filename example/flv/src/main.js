import Vue from 'vue'
import App from './App.vue'

// 引入官方插件
import VueResource from 'vue-resource'
import VueRouter from 'vue-router'
// 第三方插件
import $ from 'jquery'
import './assets/bootstrap-3.3.7/css/bootstrap.min.css'
import './assets/bootstrap-3.3.7/js/bootstrap.min.js'
import './assets/select2/css/select2.min.css'
import './assets/select2/js/select2.full.min.js'
import './assets/css/style.css'
import echarts from "echarts"
import Global from './components/common/Global.vue'

//全局更改
import Axios from 'axios'
//配合qs模块转化post请求的参数
import qs from "qs"
Vue.prototype.$axios = Axios
Vue.prototype.$qs = qs

// 使用
Vue.use(VueResource);
Vue.use(VueRouter);
Vue.prototype.$echarts = echarts
Vue.prototype.global = Global

// 1. 创建组件
import Login from './components/Login.vue'
import Index from './components/Index-col.vue'
import DocList from './components/doc/DocList.vue'
import DocAdd from './components/doc/DocAdd.vue'
import DocStatistics from './components/doc/DocStatistics.vue'
import DictList from './components/dict/DictList.vue'
import DictAdd from './components/dict/DictAdd.vue'
import OperationLogList from './components/operationlog/OperationLogList.vue'
import OperationLogSetup from './components/operationlog/OperationLogSetup.vue'
import RoleList from './components/role/RoleList.vue'
import RoleAdd from './components/role/RoleAdd.vue'
import UserList from './components/user/UserList.vue'
import UserAdd from './components/user/UserAdd.vue'
// 2. 配置路由
const baseURL = '/'
const routes = [
  {path: '/login', component: Login},
  {
    path: '/index', 
    component: Index,
    children:[
      {path: 'doclist', component: DocList},
      {path: 'docadd', component: DocAdd},
      {path: 'dictlist', component: DictList},
      {path: 'docstatistics', component: DocStatistics},
      {path: 'dictadd', component: DictAdd},
      {path: 'operationloglist', component: OperationLogList},
      {path: 'operationlogsetup', component: OperationLogSetup},
      {path: 'rolelist', component: RoleList},
      {path: 'roleadd', component: RoleAdd},
      {path: 'userlist', component: UserList},
      {path: 'useradd', component: UserAdd}
    ]
  },
  {path: '*', redirect: '/login'}
]
// 3. 实例化VueRouter
const router = new VueRouter({
  routes
})
// 4. 挂载路由
new Vue({
  el: '#app',
  router,
  baseURL,
  render: h => h(App)
})
// 5. 动态在根组件引入

