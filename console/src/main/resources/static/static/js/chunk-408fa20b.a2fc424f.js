(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-408fa20b"],{"231b":function(e,t,n){"use strict";n.r(t);var a=function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"app-container"},[n("div",{staticClass:"filter-container"},[n("el-input",{staticClass:"filter-item",staticStyle:{width:"200px"},attrs:{clearable:"",placeholder:"租户ID"},model:{value:e.listQuery.tenantId,callback:function(t){e.$set(e.listQuery,"tenantId",t)},expression:"listQuery.tenantId"}}),e._v(" "),n("el-button",{directives:[{name:"waves",rawName:"v-waves"}],staticClass:"filter-item",attrs:{type:"primary",icon:"el-icon-search"},on:{click:e.fetchData}},[e._v("\n        搜索\n      ")]),e._v(" "),"admin"==this.$cookie.get("userName")?n("el-button",{staticClass:"filter-item",staticStyle:{"margin-left":"10px"},attrs:{type:"primary",icon:"el-icon-edit"},on:{click:e.handleCreate}},[e._v("\n        添加\n      ")]):e._e()],1),e._v(" "),n("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.listLoading,expression:"listLoading"}],attrs:{data:e.list,"element-loading-text":"Loading",border:"",fit:"","highlight-current-row":""}},[n("el-table-column",{attrs:{align:"center",label:"序号",width:"95"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(e._s(t.$index+1))]}}])}),e._v(" "),n("el-table-column",{attrs:{label:"租户ID",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(e._s(t.row.tenantId))]}}])}),e._v(" "),n("el-table-column",{attrs:{label:"租户名称",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(e._s(t.row.tenantName))]}}])}),e._v(" "),n("el-table-column",{attrs:{label:"OWNER",width:"200",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(e._s(t.row.owner)+"\n        ")]}}])}),e._v(" "),n("el-table-column",{attrs:{label:"创建时间",width:"200",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(e._s(t.row.gmtCreate))]}}])}),e._v(" "),n("el-table-column",{attrs:{label:"修改时间",width:"200",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(e._s(t.row.gmtModified))]}}])}),e._v(" "),"admin"==this.$cookie.get("userName")?n("el-table-column",{attrs:{label:"操作",align:"center",width:"230","class-name":"small-padding fixed-width"},scopedSlots:e._u([{key:"default",fn:function(t){var a=t.row;return[n("el-button",{attrs:{type:"primary",size:"mini"},on:{click:function(t){return e.handleUpdate(a)}}},[e._v("\n            编辑\n          ")]),e._v(" "),n("el-button",{attrs:{size:"mini",type:"danger"},on:{click:function(t){return e.handleDelete(a)}}},[e._v("\n            删除\n          ")])]}}],null,!1,3226574922)}):e._e()],1),e._v(" "),n("pagination",{directives:[{name:"show",rawName:"v-show",value:e.total>0,expression:"total>0"}],attrs:{total:e.total,page:e.listQuery.current,limit:e.listQuery.size},on:{"update:page":function(t){return e.$set(e.listQuery,"current",t)},"update:limit":function(t){return e.$set(e.listQuery,"size",t)},pagination:e.fetchData}}),e._v(" "),n("el-dialog",{attrs:{title:e.textMap[e.dialogStatus],visible:e.dialogFormVisible,width:"800px"},on:{"update:visible":function(t){e.dialogFormVisible=t}}},[n("el-form",{ref:"dataForm",attrs:{rules:e.rules,model:e.temp,"label-position":"left","label-width":"100px"}},[n("el-form-item",{attrs:{label:"租户ID",prop:"tenantId"}},[n("el-input",{staticStyle:{width:"40%"},attrs:{disabled:"create"!==e.dialogStatus,placeholder:"租户ID"},model:{value:e.temp.tenantId,callback:function(t){e.$set(e.temp,"tenantId",t)},expression:"temp.tenantId"}})],1),e._v(" "),n("el-form-item",{attrs:{label:"租户名称",prop:"tenantName"}},[n("el-input",{staticStyle:{width:"40%"},attrs:{placeholder:"租户名称"},model:{value:e.temp.tenantName,callback:function(t){e.$set(e.temp,"tenantName",t)},expression:"temp.tenantName"}})],1),e._v(" "),n("el-form-item",{attrs:{label:"OWNER",prop:"owner"}},[n("el-input",{staticStyle:{width:"40%"},attrs:{placeholder:"OWNER"},model:{value:e.temp.owner,callback:function(t){e.$set(e.temp,"owner",t)},expression:"temp.owner"}})],1),e._v(" "),n("el-form-item",{attrs:{label:"租户描述",prop:"tenantDesc"}},[n("el-input",{staticStyle:{width:"40%"},attrs:{autosize:{minRows:3,maxRows:6},type:"textarea",placeholder:"租户描述"},model:{value:e.temp.tenantDesc,callback:function(t){e.$set(e.temp,"tenantDesc",t)},expression:"temp.tenantDesc"}})],1)],1),e._v(" "),n("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[n("el-button",{on:{click:function(t){e.dialogFormVisible=!1}}},[e._v("\n          取消\n        ")]),e._v(" "),n("el-button",{attrs:{type:"primary"},on:{click:function(t){"create"===e.dialogStatus?e.createData():e.updateData()}}},[e._v("\n          确认\n        ")])],1)],1),e._v(" "),n("el-dialog",{attrs:{visible:e.dialogPluginVisible,title:"Reading statistics"},on:{"update:visible":function(t){e.dialogPluginVisible=t}}},[n("el-table",{staticStyle:{width:"100%"},attrs:{data:e.pluginData,border:"",fit:"","highlight-current-row":""}},[n("el-table-column",{attrs:{prop:"key",label:"Channel"}}),e._v(" "),n("el-table-column",{attrs:{prop:"pv",label:"Pv"}})],1),e._v(" "),n("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[n("el-button",{attrs:{type:"primary"},on:{click:function(t){e.dialogPvVisible=!1}}},[e._v("Confirm")])],1)],1)],1)},i=[],l=n("36fb"),r=n("6724"),o=n("333d"),s={name:"JobProject",components:{Pagination:o["a"]},directives:{waves:r["a"]},filters:{statusFilter:function(e){var t={published:"success",draft:"gray",deleted:"danger"};return t[e]},ellipsis:function(e){return e?e.length>26?e.slice(0,26)+"...":e:""}},data:function(){return{list:null,listLoading:!0,total:0,listQuery:{current:1,size:10,tenantId:""},pluginTypeOptions:["reader","writer"],dialogPluginVisible:!1,pluginData:[],dialogFormVisible:!1,dialogStatus:"",textMap:{update:"Edit",create:"Create"},rules:{tenantId:[{required:!0,message:"this is required",trigger:"blur"}],tenantName:[{required:!0,message:"this is required",trigger:"blur"}],owner:[{required:!0,message:"this is required",trigger:"blur"}],tenantDesc:[{required:!0,message:"this is required",trigger:"blur"}]},temp:{id:void 0,tenantId:"",tenantName:"",owner:"",tenantDesc:""},visible:!0}},created:function(){this.fetchData()},methods:{fetchData:function(){var e=this;this.listLoading=!0,l["c"](this.listQuery).then((function(t){var n=t.records,a=t.total;e.total=a,e.list=n,e.listLoading=!1}))},resetTemp:function(){this.temp={id:void 0,tenantName:"",tenantDesc:""}},handleCreate:function(){var e=this;this.resetTemp(),this.dialogStatus="create",this.dialogFormVisible=!0,this.$nextTick((function(){e.$refs["dataForm"].clearValidate()}))},createData:function(){var e=this;this.$refs["dataForm"].validate((function(t){t&&l["a"](e.temp).then((function(){e.fetchData(),e.dialogFormVisible=!1,e.$notify({title:"Success",message:"Created Successfully",type:"success",duration:2e3})}))}))},handleUpdate:function(e){var t=this;this.temp=Object.assign({},e),this.dialogStatus="update",this.dialogFormVisible=!0,this.$nextTick((function(){t.$refs["dataForm"].clearValidate()}))},updateData:function(){var e=this;this.$refs["dataForm"].validate((function(t){if(t){var n=Object.assign({},e.temp);l["d"](n).then((function(){e.fetchData(),e.dialogFormVisible=!1,e.$notify({title:"Success",message:"Update Successfully",type:"success",duration:2e3})}))}}))},handleDelete:function(e){var t=this;console.log("删除"),l["b"](e.tenantId).then((function(e){t.fetchData(),t.$notify({title:"Success",message:"Delete Successfully",type:"success",duration:2e3})}))}}},u=s,c=n("2877"),d=Object(c["a"])(u,a,i,!1,null,null,null);t["default"]=d.exports},"2cbf":function(e,t,n){"use strict";n("653b")},"333d":function(e,t,n){"use strict";var a=function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"pagination-container",class:{hidden:e.hidden}},[n("el-pagination",e._b({attrs:{background:e.background,"current-page":e.currentPage,"page-size":e.pageSize,layout:e.layout,"page-sizes":e.pageSizes,total:e.total},on:{"update:currentPage":function(t){e.currentPage=t},"update:current-page":function(t){e.currentPage=t},"update:pageSize":function(t){e.pageSize=t},"update:page-size":function(t){e.pageSize=t},"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange}},"el-pagination",e.$attrs,!1))],1)},i=[];n("c5f6");Math.easeInOutQuad=function(e,t,n,a){return e/=a/2,e<1?n/2*e*e+t:(e--,-n/2*(e*(e-2)-1)+t)};var l=function(){return window.requestAnimationFrame||window.webkitRequestAnimationFrame||window.mozRequestAnimationFrame||function(e){window.setTimeout(e,1e3/60)}}();function r(e){document.documentElement.scrollTop=e,document.body.parentNode.scrollTop=e,document.body.scrollTop=e}function o(){return document.documentElement.scrollTop||document.body.parentNode.scrollTop||document.body.scrollTop}function s(e,t,n){var a=o(),i=e-a,s=20,u=0;t="undefined"===typeof t?500:t;var c=function(){u+=s;var e=Math.easeInOutQuad(u,a,i,t);r(e),u<t?l(c):n&&"function"===typeof n&&n()};c()}var u={name:"Pagination",props:{total:{required:!0,type:Number},page:{type:Number,default:1},limit:{type:Number,default:20},pageSizes:{type:Array,default:function(){return[10,20,30,50]}},layout:{type:String,default:"total, sizes, prev, pager, next, jumper"},background:{type:Boolean,default:!0},autoScroll:{type:Boolean,default:!0},hidden:{type:Boolean,default:!1}},computed:{currentPage:{get:function(){return this.page},set:function(e){this.$emit("update:page",e)}},pageSize:{get:function(){return this.limit},set:function(e){this.$emit("update:limit",e)}}},methods:{handleSizeChange:function(e){this.$emit("pagination",{page:this.currentPage,limit:e}),this.autoScroll&&s(0,800)},handleCurrentChange:function(e){this.$emit("pagination",{page:e,limit:this.pageSize}),this.autoScroll&&s(0,800)}}},c=u,d=(n("2cbf"),n("2877")),p=Object(d["a"])(c,a,i,!1,null,"6af373ef",null);t["a"]=p.exports},"36fb":function(e,t,n){"use strict";n.d(t,"c",(function(){return i})),n.d(t,"d",(function(){return l})),n.d(t,"a",(function(){return r})),n.d(t,"b",(function(){return o}));var a=n("b775");function i(e){return Object(a["a"])({url:"/v1/cs/tenant/query/page",method:"post",data:e})}function l(e){return Object(a["a"])({url:"/v1/cs/tenant/update",method:"post",data:e})}function r(e){return Object(a["a"])({url:"/v1/cs/tenant/save",method:"post",data:e})}function o(e){return Object(a["a"])({url:"/v1/cs/tenant/delete/"+e,method:"delete"})}},"653b":function(e,t,n){},6724:function(e,t,n){"use strict";n("8d41");var a="@@wavesContext";function i(e,t){function n(n){var a=Object.assign({},t.value),i=Object.assign({ele:e,type:"hit",color:"rgba(0, 0, 0, 0.15)"},a),l=i.ele;if(l){l.style.position="relative",l.style.overflow="hidden";var r=l.getBoundingClientRect(),o=l.querySelector(".waves-ripple");switch(o?o.className="waves-ripple":(o=document.createElement("span"),o.className="waves-ripple",o.style.height=o.style.width=Math.max(r.width,r.height)+"px",l.appendChild(o)),i.type){case"center":o.style.top=r.height/2-o.offsetHeight/2+"px",o.style.left=r.width/2-o.offsetWidth/2+"px";break;default:o.style.top=(n.pageY-r.top-o.offsetHeight/2-document.documentElement.scrollTop||document.body.scrollTop)+"px",o.style.left=(n.pageX-r.left-o.offsetWidth/2-document.documentElement.scrollLeft||document.body.scrollLeft)+"px"}return o.style.backgroundColor=i.color,o.className="waves-ripple z-active",!1}}return e[a]?e[a].removeHandle=n:e[a]={removeHandle:n},n}var l={bind:function(e,t){e.addEventListener("click",i(e,t),!1)},update:function(e,t){e.removeEventListener("click",e[a].removeHandle,!1),e.addEventListener("click",i(e,t),!1)},unbind:function(e){e.removeEventListener("click",e[a].removeHandle,!1),e[a]=null,delete e[a]}},r=function(e){e.directive("waves",l)};window.Vue&&(window.waves=l,Vue.use(r)),l.install=r;t["a"]=l},"8d41":function(e,t,n){}}]);