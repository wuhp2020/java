(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([[22],{"4f99":function(e,t,n){"use strict";n.r(t);var a=function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"q-pa-md q-gutter-sm bg-page"},[n("q-breadcrumbs",[n("q-breadcrumbs-el",{attrs:{label:"序列号管理"}}),n("q-breadcrumbs-el",{attrs:{label:"列表"}})],1),n("q-separator"),n("div",{staticClass:"q-pb-md bg-white"},[n("div",{staticClass:"row justify-start items-baseline content-center items-center"},[n("div",{staticClass:"row justify-start items-baseline content-center items-center"},[n("div",{staticClass:"q-px-md"},[n("q-item-label",{staticClass:"query-cond"},[e._v("关键字:")])],1),n("div",{staticClass:"q-pt-md"},[n("q-input",{attrs:{outlined:"",placeholder:"请输入关键字"},on:{keyup:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.onQueryClickAction.apply(null,arguments)}},model:{value:e.search,callback:function(t){e.search=t},expression:"search"}})],1)]),e._l(e.queryColumns,(function(t){return n("div",{key:t.name,staticClass:"row justify-start items-baseline content-center items-center"},[n("div",{staticClass:"q-px-md"},[n("q-item-label",{staticClass:"query-cond"},[e._v(e._s(t.label)+":")])],1),n("div",{staticClass:"q-pt-md"},[n("q-input",{attrs:{outlined:"",placeholder:""},on:{keyup:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.onQueryClickAction.apply(null,arguments)}},model:{value:t.value,callback:function(n){e.$set(t,"value",n)},expression:"item.value"}})],1)])}))],2),n("div",{staticClass:"q-pt-md row justify-start items-center"},[n("div",{staticClass:"q-px-md"},[n("q-btn",{attrs:{unelevated:"",color:"primary",label:"查询"},on:{click:e.onQueryClickAction}})],1),n("div",{staticClass:"q-px-md"},[n("q-btn",{attrs:{unelevated:"",color:"grey",label:"重置"},on:{click:function(t){return e.onResetClickAction()}}})],1)])]),n("div",{staticClass:"bg-table-list"},[n("q-banner",{staticClass:"text-black bg-listcolor",attrs:{"inline-actions":""},scopedSlots:e._u([{key:"action",fn:function(){return[n("q-btn",{attrs:{unelevated:"",color:"purple",label:"导出全部"},on:{click:function(t){return e.onExportClickAction()}}}),n("p",{staticClass:"q-px-sm"}),n("q-btn",{attrs:{disable:0==e.selected.length,unelevated:"",color:"negative",label:"批量删除"},on:{click:function(t){return e.onDeleteClickAction()}}}),n("p",{staticClass:"q-px-sm"}),n("q-btn",{attrs:{unelevated:"",color:"primary",label:"添加"},on:{click:function(t){return e.onNewClickAction()}}})]},proxy:!0}])}),n("q-table",{attrs:{data:e.data,columns:e.columns,"row-key":"id",selection:"multiple",selected:e.selected,"visible-columns":e.visibleColumns,pagination:e.tablePagination,loading:e.loading,"hide-bottom":"",flat:""},on:{"update:selected":function(t){e.selected=t},"update:pagination":function(t){e.tablePagination=t}},scopedSlots:e._u([{key:"body",fn:function(t){return[n("q-tr",{attrs:{props:t}},[n("q-td",[n("q-checkbox",{model:{value:t.selected,callback:function(n){e.$set(t,"selected",n)},expression:"props.selected"}})],1),n("q-td",{key:"dataClickAction",attrs:{props:t}},[n("q-btn",{attrs:{unelevated:"",color:"negative",label:"删除",flat:"",dense:""},on:{click:function(n){return e.onDeleteClickAction(t.row.id)}}}),n("q-btn",{attrs:{unelevated:"",color:"primary",label:"编辑",flat:"",dense:""},on:{click:function(n){return e.onEditClickAction(t.row.id)}}})],1),n("q-td",{key:"id",attrs:{props:t}},[e._v(e._s(t.row.id))]),n("q-td",{key:"name",attrs:{props:t}},[n("span",[e._v(e._s(t.row.name))])]),n("q-td",{key:"caption",attrs:{props:t}},[n("span",[e._v(e._s(t.row.caption))])]),n("q-td",{key:"sequenceType",attrs:{props:t}},[n("span",[e._v(e._s(e._f("seqTypeFormat")(t.row.sequenceType)))])]),n("q-td",{key:"currentTime",attrs:{props:t}},[null!=t.row.currentTime?n("q-toggle",{attrs:{disable:""},model:{value:t.row.currentTime,callback:function(n){e.$set(t.row,"currentTime",n)},expression:"props.row.currentTime"}}):e._e()],1),n("q-td",{key:"format",attrs:{props:t}},[n("span",[e._v(e._s(t.row.format))])]),n("q-td",{key:"minValue",attrs:{props:t}},[n("span",[e._v(e._s(t.row.minValue))])]),n("q-td",{key:"maxValue",attrs:{props:t}},[n("span",[e._v(e._s(t.row.maxValue))])]),n("q-td",{key:"nextValue",attrs:{props:t}},[n("span",[e._v(e._s(t.row.nextValue))])]),n("q-td",{key:"incrementBy",attrs:{props:t}},[n("span",[e._v(e._s(t.row.incrementBy))])]),n("q-td",{key:"createdDate",attrs:{props:t}},[n("span",[e._v(e._s(e._f("dateFormat")(t.row.createdDate)))])]),n("q-td",{key:"lastModifiedDate",attrs:{props:t}},[n("span",[e._v(e._s(e._f("dateFormat")(t.row.lastModifiedDate)))])])],1)]}}])}),e.data.length>0?n("q-separator"):e._e(),n("div",{staticClass:"q-py-md"},[n("CPage",{on:{input:e.onRequestAction},model:{value:e.pagination,callback:function(t){e.pagination=t},expression:"pagination"}})],1)],1)],1)},r=[],o=n("7ec2"),i=n.n(o),c=n("c973"),s=n.n(c),l=(n("b0c0"),n("498a"),n("ac1f"),n("841c"),n("d3b7"),n("159b"),n("e9c4"),n("8e44")),u=n("ed08"),d={data:function(){return{data:[],loading:!0,selected:[],search:"",queryColumns:[],pagination:{page:1,rowsPerPage:10,count:0},tablePagination:{rowsPerPage:10},visibleColumns:["id","name","caption","sequenceType","currentTime","format","minValue","maxValue","nextValue","incrementBy","createdDate","lastModifiedDate","dataClickAction"],columns:[{name:"dataClickAction",align:"center",label:"操作",field:"dataClickAction",sortable:!0},{name:"id",label:"编号",align:"left",field:function(e){return e.id},format:function(e){return"".concat(e)},sortable:!0},{name:"name",required:!0,label:"编码",align:"left",field:function(e){return e.name},format:function(e){return"".concat(e)},sortable:!0},{name:"caption",required:!0,label:"名称",align:"left",field:function(e){return e.caption},format:function(e){return"".concat(e)},sortable:!0},{name:"sequenceType",required:!0,label:"类型",align:"left",field:function(e){return e.sequenceType},format:function(e){return"".concat(e)},sortable:!0},{name:"currentTime",required:!0,label:"时间戳",align:"left",field:function(e){return e.currentTime},format:function(e){return"".concat(e)},sortable:!0},{name:"format",required:!0,label:"格式",align:"left",field:function(e){return e.format},format:function(e){return"".concat(e)},sortable:!0},{name:"minValue",required:!0,label:"最小值",align:"left",field:function(e){return e.minValue},format:function(e){return"".concat(e)},sortable:!0},{name:"maxValue",required:!0,label:"最大值",align:"left",field:function(e){return e.maxValue},format:function(e){return"".concat(e)},sortable:!0},{name:"nextValue",required:!0,label:"下一个值",align:"left",field:function(e){return e.nextValue},format:function(e){return"".concat(e)},sortable:!0},{name:"incrementBy",required:!0,label:"步长",align:"left",field:function(e){return e.incrementBy},format:function(e){return"".concat(e)},sortable:!0},{name:"createdDate",required:!0,label:"创建时间",align:"left",field:function(e){return e.createdDate},format:function(e){return"".concat(e)},sortable:!0},{name:"lastModifiedDate",required:!0,label:"修改时间",align:"left",field:function(e){return e.lastModifiedDate},format:function(e){return"".concat(e)},sortable:!0}]}},created:function(){var e=this;return s()(i()().mark((function t(){return i()().wrap((function(t){while(1)switch(t.prev=t.next){case 0:return t.next=2,e.init();case 2:return t.next=4,e.onRefresh();case 4:case"end":return t.stop()}}),t)})))()},mounted:function(){console.info("mounted")},activated:function(){console.info("activated")},deactivated:function(){console.info("deactivated")},updated:function(){console.info("updated")},destroyed:function(){console.info("destroyed")},beforeRouteUpdate:function(e,t,n){var a=this;return s()(i()().mark((function e(){return i()().wrap((function(e){while(1)switch(e.prev=e.next){case 0:return console.info("beforeRouteUpdate"),e.next=3,a.init();case 3:return e.next=5,a.onRefresh();case 5:n();case 6:case"end":return e.stop()}}),e)})))()},filters:{dateFormat:function(e){return u["a"].dateTimeFormat(e)},seqTypeFormat:function(e){return"STRING"===e?"字符串":"LONG"===e?"数字":e}},methods:{onRefresh:function(){var e=this;return s()(i()().mark((function t(){return i()().wrap((function(t){while(1)switch(t.prev=t.next){case 0:return e.selected=[],t.next=3,e.fetchFromServer();case 3:case"end":return t.stop()}}),t)})))()},onRequestAction:function(e){console.info("onRequestAction"),console.info(e),this.tablePagination.rowsPerPage=e.rowsPerPage,this.fetchFromServer()},getQuery:function(){for(var e={},t=0;t<this.queryColumns.length;t++){var n=this.queryColumns[t];n.value&&""!==n.value.trim()&&(e[n.name]=n.value)}return console.info(e),e},onQueryClickAction:function(){this.onRefresh()},onResetClickAction:function(){console.info("onResetClickAction"),this.search="";for(var e=0;e<this.queryColumns.length;e++)this.queryColumns[e].value="";this.onRefresh()},onNewClickAction:function(){this.$router.push("/metadata/sequences/new")},onEditClickAction:function(e){this.$router.push("/metadata/sequences/"+e)},onExportClickAction:function(e){window.open("/api/metadata/sequences/export","_blank")},onDeleteClickAction:function(e){var t=this;return s()(i()().mark((function n(){var a;return i()().wrap((function(n){while(1)switch(n.prev=n.next){case 0:a=[],t.selected.forEach((function(e){a.push(e.id)})),console.info(JSON.stringify(a));try{t.$q.dialog({title:"删除",message:"确认删除吗？",ok:{unelevated:!0},cancel:{color:"negative",unelevated:!0},persistent:!1}).onOk(s()(i()().mark((function n(){return i()().wrap((function(n){while(1)switch(n.prev=n.next){case 0:if(!e){n.next=5;break}return n.next=3,l["d"].delete(e);case 3:n.next=7;break;case 5:return n.next=7,l["d"].batchDelete(a);case 7:t.$q.notify("删除成功"),t.onRefresh();case 9:case"end":return n.stop()}}),n)})))).onCancel((function(){})).onDismiss((function(){console.info("I am triggered on both OK and Cancel")}))}catch(r){t.$q.notify("删除失败")}case 4:case"end":return n.stop()}}),n)})))()},fetchFromServer:function(){var e=this;return s()(i()().mark((function t(){var n,a;return i()().wrap((function(t){while(1)switch(t.prev=t.next){case 0:return e.loading=!0,e.data=[],t.prev=2,n=e.getQuery(),console.info("query"+JSON.stringify(n)),t.next=7,l["d"].count(e.search,n);case 7:return e.pagination.count=t.sent,t.next=10,l["d"].list(e.pagination.page,e.pagination.rowsPerPage,e.search,n);case 10:a=t.sent,e.data=a,e.loading=!1,t.next=19;break;case 15:t.prev=15,t.t0=t["catch"](2),e.loading=!1,console.error(t.t0);case 19:case"end":return t.stop()}}),t,null,[[2,15]])})))()},init:function(){var e=this;return s()(i()().mark((function t(){return i()().wrap((function(t){while(1)switch(t.prev=t.next){case 0:console.info("init"),e.$store.commit("config/updateIsAllowBack",e.$route.meta.isAllowBack),e.selected=[],e.search="";case 4:case"end":return t.stop()}}),t)})))()}}},f=d,p=n("2877"),m=n("ead5"),b=n("079e"),q=n("eb85"),v=n("0170"),k=n("27f9"),y=n("9c40"),h=n("54e1"),g=n("eaac"),w=n("bd08"),C=n("db86"),x=n("8f8e"),_=n("9564"),A=n("eebe"),T=n.n(A),Q=Object(p["a"])(f,a,r,!1,null,null,null);t["default"]=Q.exports;T()(Q,"components",{QBreadcrumbs:m["a"],QBreadcrumbsEl:b["a"],QSeparator:q["a"],QItemLabel:v["a"],QInput:k["a"],QBtn:y["a"],QBanner:h["a"],QTable:g["a"],QTr:w["a"],QTd:C["a"],QCheckbox:x["a"],QToggle:_["a"]})}}]);