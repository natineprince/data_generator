(function() {
	var VueAlert = {};
	VueAlert.install = function(Vue, options) {
		Vue.prototype.$alert = function(code, level, title, args) {
			this.$http.get("/message/get/" + code, args).then(
				(response)=>{
					console.log(response);
					alertVm.title = title;
					alertVm.level = (level==null)?"info":level;
					alertVm.message = response.bodyText;
					$("#alertModal").modal("show");
				},
				(response)=>{
					console.log(response);
					alert(response.body.error + "\n" + response.body.message);
				}
			);
		}
	}
	if (typeof window !== 'undefined' && window.Vue) {
		window.Vue.use(VueAlert);
	}
})();

var alertVm = new Vue({
	el : "#alert",
	data : {
		title : "",
		level : "info",
		message : ""
	}
});