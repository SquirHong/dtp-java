(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-126806af"],{"2cbf":function(e,t,a){"use strict";a("653b")},"333d":function(e,t,a){"use strict";var i=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"pagination-container",class:{hidden:e.hidden}},[a("el-pagination",e._b({attrs:{background:e.background,"current-page":e.currentPage,"page-size":e.pageSize,layout:e.layout,"page-sizes":e.pageSizes,total:e.total},on:{"update:currentPage":function(t){e.currentPage=t},"update:current-page":function(t){e.currentPage=t},"update:pageSize":function(t){e.pageSize=t},"update:page-size":function(t){e.pageSize=t},"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange}},"el-pagination",e.$attrs,!1))],1)},n=[];a("c5f6");Math.easeInOutQuad=function(e,t,a,i){return e/=i/2,e<1?a/2*e*e+t:(e--,-a/2*(e*(e-2)-1)+t)};var l=function(){return window.requestAnimationFrame||window.webkitRequestAnimationFrame||window.mozRequestAnimationFrame||function(e){window.setTimeout(e,1e3/60)}}();function r(e){document.documentElement.scrollTop=e,document.body.parentNode.scrollTop=e,document.body.scrollTop=e}function s(){return document.documentElement.scrollTop||document.body.parentNode.scrollTop||document.body.scrollTop}function o(e,t,a){var i=s(),n=e-i,o=20,u=0;t="undefined"===typeof t?500:t;var c=function(){u+=o;var e=Math.easeInOutQuad(u,i,n,t);r(e),u<t?l(c):a&&"function"===typeof a&&a()};c()}var u={name:"Pagination",props:{total:{required:!0,type:Number},page:{type:Number,default:1},limit:{type:Number,default:20},pageSizes:{type:Array,default:function(){return[10,20,30,50]}},layout:{type:String,default:"total, sizes, prev, pager, next, jumper"},background:{type:Boolean,default:!0},autoScroll:{type:Boolean,default:!0},hidden:{type:Boolean,default:!1}},computed:{currentPage:{get:function(){return this.page},set:function(e){this.$emit("update:page",e)}},pageSize:{get:function(){return this.limit},set:function(e){this.$emit("update:limit",e)}}},methods:{handleSizeChange:function(e){this.$emit("pagination",{page:this.currentPage,limit:e}),this.autoScroll&&o(0,800)},handleCurrentChange:function(e){this.$emit("pagination",{page:e,limit:this.pageSize}),this.autoScroll&&o(0,800)}}},c=u,d=(a("2cbf"),a("2877")),p=Object(d["a"])(c,i,n,!1,null,"6af373ef",null);t["a"]=p.exports},"36fb":function(e,t,a){"use strict";a.d(t,"c",(function(){return n})),a.d(t,"d",(function(){return l})),a.d(t,"a",(function(){return r})),a.d(t,"b",(function(){return s}));var i=a("b775");function n(e){return Object(i["a"])({url:"/v1/cs/tenant/query/page",method:"post",data:e})}function l(e){return Object(i["a"])({url:"/v1/cs/tenant/update",method:"post",data:e})}function r(e){return Object(i["a"])({url:"/v1/cs/tenant/save",method:"post",data:e})}function s(e){return Object(i["a"])({url:"/v1/cs/tenant/delete/"+e,method:"delete"})}},"653b":function(e,t,a){},6724:function(e,t,a){"use strict";a("8d41");var i="@@wavesContext";function n(e,t){function a(a){var i=Object.assign({},t.value),n=Object.assign({ele:e,type:"hit",color:"rgba(0, 0, 0, 0.15)"},i),l=n.ele;if(l){l.style.position="relative",l.style.overflow="hidden";var r=l.getBoundingClientRect(),s=l.querySelector(".waves-ripple");switch(s?s.className="waves-ripple":(s=document.createElement("span"),s.className="waves-ripple",s.style.height=s.style.width=Math.max(r.width,r.height)+"px",l.appendChild(s)),n.type){case"center":s.style.top=r.height/2-s.offsetHeight/2+"px",s.style.left=r.width/2-s.offsetWidth/2+"px";break;default:s.style.top=(a.pageY-r.top-s.offsetHeight/2-document.documentElement.scrollTop||document.body.scrollTop)+"px",s.style.left=(a.pageX-r.left-s.offsetWidth/2-document.documentElement.scrollLeft||document.body.scrollLeft)+"px"}return s.style.backgroundColor=n.color,s.className="waves-ripple z-active",!1}}return e[i]?e[i].removeHandle=a:e[i]={removeHandle:a},a}var l={bind:function(e,t){e.addEventListener("click",n(e,t),!1)},update:function(e,t){e.removeEventListener("click",e[i].removeHandle,!1),e.addEventListener("click",n(e,t),!1)},unbind:function(e){e.removeEventListener("click",e[i].removeHandle,!1),e[i]=null,delete e[i]}},r=function(e){e.directive("waves",l)};window.Vue&&(window.waves=l,Vue.use(r)),l.install=r;t["a"]=l},"76da":function(e,t,a){"use strict";a.d(t,"d",(function(){return n})),a.d(t,"e",(function(){return l})),a.d(t,"b",(function(){return r})),a.d(t,"c",(function(){return s})),a.d(t,"a",(function(){return o}));var i=a("b775");function n(e){return Object(i["a"])({url:"/v1/cs/thread/pool/query/page",method:"post",data:e})}function l(e){return Object(i["a"])({url:"/v1/cs/thread/pool/save_or_update",method:"post",data:e})}function r(e){return Object(i["a"])({url:"/v1/cs/thread/pool/save_or_update",method:"post",data:e})}function s(e){return Object(i["a"])({url:"/v1/cs/thread/pool/delete",method:"delete",data:e})}function o(e){return Object(i["a"])({url:"/v1/cs/thread/pool/alarm/enable/"+e.id+"/"+e.isAlarm,method:"post"})}},"8d41":function(e,t,a){},fa7e:function(e,t,a){"use strict";a.r(t);var i=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"app-container"},[a("div",{staticClass:"filter-container"},[a("el-select",{staticClass:"filter-item",staticStyle:{width:"220px"},attrs:{placeholder:"租户ID"},model:{value:e.listQuery.tenantId,callback:function(t){e.$set(e.listQuery,"tenantId",t)},expression:"listQuery.tenantId"}},e._l(e.tenantOptions,(function(e){return a("el-option",{key:e.key,attrs:{label:e.display_name,value:e.key}})})),1),e._v(" "),a("el-select",{staticClass:"filter-item",staticStyle:{width:"220px"},attrs:{placeholder:"项目ID"},model:{value:e.listQuery.itemId,callback:function(t){e.$set(e.listQuery,"itemId",t)},expression:"listQuery.itemId"}},e._l(e.itemOptions,(function(e){return a("el-option",{key:e.key,attrs:{label:e.display_name,value:e.key}})})),1),e._v(" "),a("el-select",{staticClass:"filter-item",staticStyle:{width:"220px"},attrs:{placeholder:"线程池ID"},model:{value:e.listQuery.tpId,callback:function(t){e.$set(e.listQuery,"tpId",t)},expression:"listQuery.tpId"}},e._l(e.threadPoolOptions,(function(e){return a("el-option",{key:e.key,attrs:{label:e.display_name,value:e.key}})})),1),e._v(" "),a("el-button",{directives:[{name:"waves",rawName:"v-waves"}],staticClass:"filter-item",attrs:{type:"primary",icon:"el-icon-search"},on:{click:e.fetchData}},[e._v("\n      搜索\n    ")]),e._v(" "),a("el-button",{staticClass:"filter-item",staticStyle:{"margin-left":"10px"},attrs:{type:"primary",icon:"el-icon-edit"},on:{click:e.handleCreate}},[e._v("\n      添加\n    ")])],1),e._v(" "),a("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.listLoading,expression:"listLoading"}],attrs:{data:e.list,"element-loading-text":"Loading",border:"",fit:"","highlight-current-row":""}},[a("el-table-column",{attrs:{align:"center",label:"序号",width:"95"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(e._s(t.$index+1))]}}])}),e._v(" "),a("el-table-column",{attrs:{label:"租户ID",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(e._s(t.row.tenantId))]}}])}),e._v(" "),a("el-table-column",{attrs:{label:"项目ID",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(e._s(t.row.itemId))]}}])}),e._v(" "),a("el-table-column",{attrs:{label:"线程池ID",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(e._s(t.row.tpId))]}}])}),e._v(" "),a("el-table-column",{attrs:{label:"核心线程",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(e._s(t.row.coreSize))]}}])}),e._v(" "),a("el-table-column",{attrs:{label:"最大线程",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(e._s(t.row.maxSize))]}}])}),e._v(" "),a("el-table-column",{attrs:{label:"队列类型",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(e._s(e._f("queueFilter")(t.row.queueType)))]}}])}),e._v(" "),a("el-table-column",{attrs:{label:"队列容量",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(e._s(t.row.capacity))]}}])}),e._v(" "),a("el-table-column",{attrs:{label:"是否报警",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-switch",{attrs:{"active-color":"#00A854","active-text":"报警","active-value":0,"inactive-color":"#F04134","inactive-text":"忽略","inactive-value":1},on:{change:function(a){return e.changeAlarm(t.row)}},model:{value:t.row.isAlarm,callback:function(a){e.$set(t.row,"isAlarm",a)},expression:"scope.row.isAlarm"}})]}}])}),e._v(" "),a("el-table-column",{attrs:{label:"操作",align:"center",width:"200","class-name":"small-padding fixed-width"},scopedSlots:e._u([{key:"default",fn:function(t){var i=t.row;return[a("el-button",{attrs:{type:"primary",size:"mini"},on:{click:function(t){return e.handleUpdate(i)}}},[e._v("\n          编辑\n        ")]),e._v(" "),a("el-button",{attrs:{size:"mini",type:"danger"},on:{click:function(t){return e.handleDelete(i)}}},[e._v("\n          删除\n        ")])]}}])})],1),e._v(" "),a("pagination",{directives:[{name:"show",rawName:"v-show",value:e.total>0,expression:"total>0"}],attrs:{total:e.total,page:e.listQuery.current,limit:e.listQuery.size},on:{"update:page":function(t){return e.$set(e.listQuery,"current",t)},"update:limit":function(t){return e.$set(e.listQuery,"size",t)},pagination:e.fetchData}}),e._v(" "),a("el-dialog",{attrs:{title:e.textMap[e.dialogStatus],visible:e.dialogFormVisible,width:"1000px"},on:{"update:visible":function(t){e.dialogFormVisible=t}}},[a("el-form",{ref:"dataForm",attrs:{rules:e.rules,model:e.temp,"label-position":"left","label-width":"110px"}},[a("el-row",{attrs:{gutter:20}},[a("el-col",{attrs:{span:12}},[a("el-form-item",{attrs:{label:"《基本信息》"}})],1)],1),e._v(" "),a("el-row",{attrs:{gutter:20}},[a("el-col",{attrs:{span:12}},[a("el-form-item",{attrs:{label:"租户ID",prop:"tenantId"}},[a("el-select",{staticStyle:{display:"block"},attrs:{placeholder:"请选择租户",disabled:"create"!==e.dialogStatus},model:{value:e.temp.tenantId,callback:function(t){e.$set(e.temp,"tenantId",t)},expression:"temp.tenantId"}},e._l(e.tenantOptions,(function(e){return a("el-option",{key:e.key,attrs:{label:e.display_name,value:e.key}})})),1)],1)],1),e._v(" "),a("el-col",{attrs:{span:12}},[a("el-form-item",{attrs:{label:"核心线程",prop:"coreSize"}},[a("el-input-number",{attrs:{placeholder:"核心线程",min:1,max:999},model:{value:e.temp.coreSize,callback:function(t){e.$set(e.temp,"coreSize",t)},expression:"temp.coreSize"}})],1)],1)],1),e._v(" "),a("el-row",{attrs:{gutter:20}},[a("el-col",{attrs:{span:12}},[a("el-form-item",{attrs:{label:"项目ID",prop:"itemId"}},[a("el-select",{staticStyle:{display:"block"},attrs:{placeholder:"请选择项目",disabled:"create"!==e.dialogStatus},model:{value:e.temp.itemId,callback:function(t){e.$set(e.temp,"itemId",t)},expression:"temp.itemId"}},e._l(e.itemOptions,(function(e){return a("el-option",{key:e.key,attrs:{label:e.display_name,value:e.key}})})),1)],1)],1),e._v(" "),a("el-col",{attrs:{span:12}},[a("el-form-item",{attrs:{label:"最大线程",prop:"maxSize"}},[a("el-input-number",{attrs:{placeholder:"最大线程",min:1,max:999},model:{value:e.temp.maxSize,callback:function(t){e.$set(e.temp,"maxSize",t)},expression:"temp.maxSize"}})],1)],1)],1),e._v(" "),a("el-row",{attrs:{gutter:20}},[a("el-col",{attrs:{span:12}},[a("el-form-item",{attrs:{label:"线程池ID",prop:"tpId"}},[a("el-input",{attrs:{size:"medium",placeholder:"请输入线程池ID",disabled:"create"!==e.dialogStatus},model:{value:e.temp.tpId,callback:function(t){e.$set(e.temp,"tpId",t)},expression:"temp.tpId"}})],1)],1),e._v(" "),a("el-col",{attrs:{span:12}},[a("el-form-item",{attrs:{label:"KVTime",prop:"keepAliveTime"}},[a("el-input-number",{attrs:{placeholder:"Time / S",min:20,max:90},model:{value:e.temp.keepAliveTime,callback:function(t){e.$set(e.temp,"keepAliveTime",t)},expression:"temp.keepAliveTime"}})],1)],1)],1),e._v(" "),a("el-row",{attrs:{gutter:20}},[a("el-col",{attrs:{span:12}},[a("el-form-item",{attrs:{label:"《扩展信息》"}})],1)],1),e._v(" "),a("el-row",{attrs:{gutter:20}},[a("el-col",{attrs:{span:12}},[a("el-form-item",{attrs:{label:"队列类型",prop:"queueType"}},[a("el-select",{staticStyle:{display:"block"},attrs:{placeholder:"队列类型"},on:{change:e.selectQueueType},model:{value:e.temp.queueType,callback:function(t){e.$set(e.temp,"queueType",t)},expression:"temp.queueType"}},e._l(e.queueTypeOptions,(function(e){return a("el-option",{key:e.key,attrs:{label:e.display_name,value:e.key}})})),1)],1)],1),e._v(" "),a("el-col",{attrs:{span:12}},[a("el-form-item",{attrs:{label:"队列容量",prop:"capacity"}},[a("el-input-number",{attrs:{placeholder:"队列容量",min:0,max:2147483647,disabled:4===e.temp.queueType||5===e.temp.queueType},model:{value:e.temp.capacity,callback:function(t){e.$set(e.temp,"capacity",t)},expression:"temp.capacity"}})],1)],1)],1),e._v(" "),a("el-row",{attrs:{gutter:20}},[a("el-col",{attrs:{span:12}},[a("el-form-item",{attrs:{label:"拒绝策略",prop:"rejectedType"}},[a("el-select",{staticStyle:{display:"block"},attrs:{placeholder:"拒绝策略"},model:{value:e.temp.rejectedType,callback:function(t){e.$set(e.temp,"rejectedType",t)},expression:"temp.rejectedType"}},e._l(e.rejectedOptions,(function(e){return a("el-option",{key:e.key,attrs:{label:e.display_name,value:e.key}})})),1)],1)],1),e._v(" "),a("el-col",{attrs:{span:12}},[a("el-form-item",{attrs:{label:"活跃度报警",prop:"livenessAlarm"}},[a("el-input-number",{attrs:{placeholder:"活跃度报警",min:30,max:90},model:{value:e.temp.livenessAlarm,callback:function(t){e.$set(e.temp,"livenessAlarm",t)},expression:"temp.livenessAlarm"}})],1)],1)],1),e._v(" "),a("el-row",{attrs:{gutter:20}},[a("el-col",{attrs:{span:12}},[a("el-form-item",{attrs:{label:"是否报警",prop:"isAlarm"}},[a("el-select",{staticStyle:{display:"block"},attrs:{placeholder:"是否报警"},model:{value:e.temp.isAlarm,callback:function(t){e.$set(e.temp,"isAlarm",t)},expression:"temp.isAlarm"}},e._l(e.alarmTypes,(function(e){return a("el-option",{key:e.key,attrs:{label:e.display_name,value:e.key}})})),1)],1)],1),e._v(" "),a("el-col",{attrs:{span:12}},[a("el-form-item",{attrs:{label:"容量报警",prop:"capacityAlarm"}},[a("el-input-number",{attrs:{placeholder:"容量报警",min:30,max:90},model:{value:e.temp.capacityAlarm,callback:function(t){e.$set(e.temp,"capacityAlarm",t)},expression:"temp.capacityAlarm"}})],1)],1)],1),e._v(" "),a("el-row",{attrs:{gutter:20}})],1),e._v(" "),a("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(t){e.dialogFormVisible=!1}}},[e._v("\n        取消\n      ")]),e._v(" "),a("el-button",{attrs:{type:"primary"},on:{click:function(t){"create"===e.dialogStatus?e.createData():e.updateData()}}},[e._v("\n        确认\n      ")])],1)],1),e._v(" "),a("el-dialog",{attrs:{visible:e.dialogPluginVisible,title:"Reading statistics"},on:{"update:visible":function(t){e.dialogPluginVisible=t}}},[a("el-table",{staticStyle:{width:"100%"},attrs:{data:e.pluginData,border:"",fit:"","highlight-current-row":""}},[a("el-table-column",{attrs:{prop:"key",label:"Channel"}}),e._v(" "),a("el-table-column",{attrs:{prop:"pv",label:"Pv"}})],1),e._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{attrs:{type:"primary"},on:{click:function(t){e.dialogPvVisible=!1}}},[e._v("Confirm")])],1)],1)],1)},n=[],l=a("bd86"),r=a("fbcd"),s=a("36fb"),o=a("76da"),u=a("6724"),c=a("333d"),d=Object(l["a"])(Object(l["a"])(Object(l["a"])(Object(l["a"])({name:"JobProject",components:{Pagination:c["a"]},directives:{waves:u["a"]},filters:{statusFilter:function(e){var t={published:"success",draft:"gray",deleted:"danger"};return t[e]}}},"filters",{queueFilter:function(e){return"1"==e?"ArrayBlockingQueue":"2"==e?"LinkedBlockingQueue":"3"==e?"LinkedBlockingDeque":"4"==e?"SynchronousQueue":"5"==e?"LinkedTransferQueue":"6"==e?"PriorityBlockingQueue":"9"==e?"ResizableLinkedBlockingQueue":void 0}}),"data",(function(){return{list:null,listLoading:!0,total:0,listQuery:{current:1,size:10,itemId:""},pluginTypeOptions:["reader","writer"],dialogPluginVisible:!1,pluginData:[],dialogFormVisible:!1,tenantOptions:[],threadPoolOptions:[],itemOptions:[],queueTypeOptions:[{key:1,display_name:"ArrayBlockingQueue"},{key:2,display_name:"LinkedBlockingQueue"},{key:3,display_name:"LinkedBlockingDeque"},{key:4,display_name:"SynchronousQueue"},{key:5,display_name:"LinkedTransferQueue"},{key:6,display_name:"PriorityBlockingQueue"},{key:9,display_name:"ResizableLinkedBlockingQueue (支持动态修改队列大小)"}],rejectedOptions:[{key:1,display_name:"CallerRunsPolicy"},{key:2,display_name:"AbortPolicy"},{key:3,display_name:"DiscardPolicy"},{key:4,display_name:"DiscardOldestPolicy"}],alarmTypes:[{key:0,display_name:"报警"},{key:1,display_name:"不报警"}],dialogStatus:"",textMap:{update:"Edit",create:"Create"},rules:{tenantId:[{required:!0,message:"this is required",trigger:"blur"}],itemId:[{required:!0,message:"this is required",trigger:"blur"}],tpId:[{required:!0,message:"this is required",trigger:"blur"}],coreSize:[{required:!0,message:"this is required",trigger:"blur"}],maxSize:[{required:!0,message:"this is required",trigger:"blur"}],queueType:[{required:!0,message:"this is required",trigger:"blur"}],keepAliveTime:[{required:!0,message:"this is required",trigger:"blur"}],isAlarm:[{required:!0,message:"this is required",trigger:"blur"}],capacityAlarm:[{required:!0,message:"this is required",trigger:"blur"}],livenessAlarm:[{required:!0,message:"this is required",trigger:"blur"}],rejectedType:[{required:!0,message:"this is required",trigger:"blur"}]},temp:{id:void 0,tenantId:""},visible:!0}})),"created",(function(){this.fetchData(),this.initSelect()})),"methods",{fetchData:function(){var e=this;this.listLoading=!0,o["d"](this.listQuery).then((function(t){var a=t.records,i=t.total;e.total=i,e.list=a,e.listLoading=!1}))},changeAlarm:function(e){var t=this;o["a"](e).then((function(){t.fetchData(),t.$notify({title:"Success",message:"Update Successfully",type:"success",duration:2e3})}))},initSelect:function(){var e=this;s["c"]({}).then((function(t){for(var a=t.records,i=0;i<a.length;i++)e.tenantOptions.push({key:a[i].tenantId,display_name:a[i].tenantId+" "+a[i].tenantName})})),r["c"]({}).then((function(t){for(var a=t.records,i=0;i<a.length;i++)e.itemOptions.push({key:a[i].itemId,display_name:a[i].itemId+" "+a[i].itemName})})),o["d"]({}).then((function(t){for(var a=t.records,i=0;i<a.length;i++)e.threadPoolOptions.push({key:a[i].tpId,display_name:a[i].tpId})}))},resetTemp:function(){this.temp={id:void 0,tenantName:"",tenantDesc:""}},handleCreate:function(){var e=this;this.resetTemp(),this.dialogStatus="create",this.dialogFormVisible=!0,this.$nextTick((function(){e.$refs["dataForm"].clearValidate()}))},createData:function(){var e=this;this.$refs["dataForm"].validate((function(t){t&&o["b"](e.temp).then((function(){e.fetchData(),e.dialogFormVisible=!1,e.$notify({title:"Success",message:"Created Successfully",type:"success",duration:2e3})}))}))},handleUpdate:function(e){var t=this;this.temp=Object.assign({},e),this.dialogStatus="update",this.dialogFormVisible=!0,this.$nextTick((function(){t.$refs["dataForm"].clearValidate()}))},updateData:function(){var e=this;this.$refs["dataForm"].validate((function(t){if(t){var a=Object.assign({},e.temp);o["e"](a).then((function(){e.fetchData(),e.dialogFormVisible=!1,e.$notify({title:"Success",message:"Update Successfully",type:"success",duration:2e3})}))}}))},handleDelete:function(e){var t=this;o["c"](e).then((function(e){t.fetchData(),t.$notify({title:"Success",message:"Delete Successfully",type:"success",duration:2e3})}))},selectQueueType:function(e){4===e?this.temp.capacity=0:5===e&&(this.temp.capacity=2147483647)}}),p=d,m=a("2877"),f=Object(m["a"])(p,i,n,!1,null,null,null);t["default"]=f.exports},fbcd:function(e,t,a){"use strict";a.d(t,"c",(function(){return n})),a.d(t,"d",(function(){return l})),a.d(t,"a",(function(){return r})),a.d(t,"b",(function(){return s}));var i=a("b775");function n(e){return Object(i["a"])({url:"/v1/cs/item/query/page",method:"post",data:e})}function l(e){return Object(i["a"])({url:"/v1/cs/item/update",method:"post",data:e})}function r(e){return Object(i["a"])({url:"/v1/cs/item/save",method:"post",data:e})}function s(e){return Object(i["a"])({url:"/v1/cs/item/delete/"+e[0]+"/"+e[1],method:"delete"})}}}]);