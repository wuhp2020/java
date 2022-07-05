<template>
   <div>
    <table class="table table-striped">
        <caption>文章管理 / 文章列表&nbsp;&nbsp;<span class="glyphicon glyphicon-folder-open"></span></caption>
        <thead>
        <tr>
            <th>名称</th>
            <th>城市</th>
            <th>密码</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>Tanmay</td>
            <td>Bangalore</td>
            <td>560001</td>
        </tr>
        <tr>
            <td>Sachin</td>
            <td>Mumbai</td>
            <td>400003</td>
        </tr>
        <tr>
            <td>Uma</td>
            <td>Pune</td>
            <td>411027</td>
        </tr>
        <tr>
            <td>Uma</td>
            <td>Pune</td>
            <td>411027</td>
        </tr>
        <tr>
            <td>Uma</td>
            <td>Pune</td>
            <td>411027</td>
        </tr>
        <tr>
            <td>Uma</td>
            <td>Pune</td>
            <td>411027</td>
        </tr>
        <tr>
            <td>Uma</td>
            <td>Pune</td>
            <td>411027</td>
        </tr>
        <tr>
            <td>Uma</td>
            <td>Pune</td>
            <td>411027</td>
        </tr>
        
        </tbody>
    </table>
    <v-pageable></v-pageable>
   </div>
</template>

<script>
import Pageable from '../common/Pageable.vue'
export default {
   data () {
      return {
         msg: 'doclist'
      }
   },
   methods: {

   },
   components: {
      'v-pageable': Pageable
   }
}
</script>

<style lang="scss">
  
</style>