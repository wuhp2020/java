<template>
  <div> 
    <div class="page"> 
      <div class="pagelist"> 
        <span class="jump" v-show="current_page > 1" @click="jumpPage(current_page - 1)">上一页</span> 
        <span v-show="current_page > 5" class="jump" @click="jumpPage(1)">1</span> 
        <span class="ellipsis"  v-show="current_page > 5">...</span> 
        
        <span class="jump" v-for="num in indexs" :class="{bgprimary:current_page==num}" @click="jumpPage(num)">{{num}}</span> 
        
        <span class="ellipsis"  v-show="current_page < pages-4">...</span> 
        <span v-show="current_page < pages-4" class="jump" @click="jumpPage(pages)">{{pages}}</span>
        <span class="jump" v-if="pages-current_page>0" @click="jumpPage(parseInt(current_page) + 1)">下一页</span>
 
        <span class="jumppoint">跳转到：</span> 
        <span class="jumpinp"><input type="text" v-model="changePage"></span> 
        <span class="jump gobtn" @click="jumpPage(changePage)">GO</span> 
      </div> 
    </div> 
  </div>
</template>

<script>
export default {
    data () {
      return {
          
      }
    },
    props : { //通过props来接受从父组件传递过来的值
        current_page: {//当前页 
            type : Number,
            default : 0
        }, 
        pages: {//总页数 
            type : Number,
            default : 5
        }, 
        changePage:{//跳转页
            type : Number,
            default : 1
        }
    },
    methods: {
        jumpPage: function(id) {
            if (this.pages < id){
                alert("共 "+ this.pages +" 页")
                return;
            }
            //父组件通过jumpPage方法来接受当前的页码
            this.$emit('jumpPage', id) 
        }
    },
    computed:{
        indexs: function() {
            var left = 1, 
            right = this.pages, 
            ar = []; 
            if (this.pages >= 7) { 
                if (this.current_page > 5 && this.current_page < this.pages - 4) { 
                    left = Number(this.current_page) - 3; 
                    right = Number(this.current_page) + 3; 
                } else { 
                    if (this.current_page <= 5) { 
                        left = 1; 
                        right = 7; 
                    } else { 
                        right = this.pages; 
                        left = this.pages - 6; 
                    } 
                } 
            } 
            while (left <= right) { 
                ar.push(left); 
                left++; 
            } 
            return ar; 
     }
    }
}
</script>

<style lang="scss">
    .page { 
    font-weight: 900; 
    height: 40px; 
    text-align: center; 
    color: #888; 
    margin: 20px auto 0; 
    background: #f2f2f2; 
    } 
    
    .pagelist { 
    font-size: 0; 
    background: #fff; 
    height: 50px; 
    line-height: 50px; 
    } 
    
    .pagelist span { 
    font-size: 14px; 
    } 
    
    .pagelist .jump { 
    border: 1px solid #ccc; 
    padding: 5px 8px; 
    -webkit-border-radius: 4px; 
    -moz-border-radius: 4px; 
    border-radius: 4px; 
    cursor: pointer; 
    margin-left: 5px; 
    } 
    
    .pagelist .bgprimary { 
    cursor: default; 
    color: #fff; 
    background: #337ab7; 
    border-color: #337ab7; 
    } 
    
    .jumpinp input { 
    width: 55px; 
    height: 26px; 
    font-size: 13px; 
    border: 1px solid #ccc; 
    -webkit-border-radius: 4px; 
    -moz-border-radius: 4px; 
    border-radius: 4px; 
    text-align: center; 
    } 
    
    .ellipsis { 
    padding: 0px 8px; 
    } 
    
    .jumppoint { 
    margin-left: 30px; 
    } 
    
    .pagelist .gobtn {} 
    
    .bgprimary { 
    cursor: default; 
    color: #fff; 
    background: #337ab7; 
    border-color: #337ab7; 
    }
</style>