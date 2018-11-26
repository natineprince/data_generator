var leftPanelVm = new Vue({
	el : "#leftPanel",
	data : {
		currentIndex : 0,
		editMode : "",
		dataSourceList : [],
		currentDataSource : {}
	},
	methods : {
		loadAll : function(){
			this.$http.get("/datasource/list/all").then(
				(response)=>{
					console.log(response);
					this.dataSourceList = response.body;
				},
				this.errorHandler
			);
		},
		selectDataSource : function(index){
			mainPanelVm.setCurrentDataSource(this.dataSourceList[index]);
		},
		newDataSource : function(){
			this.currentDataSource = {};
			this.editMode = 'create';
		},
		createDataSource : function(){
			this.$http.post("/datasource/create", this.currentDataSource).then(
				(response)=>{
					console.log(response);
					this.currentDataSource = response.body;
					this.dataSourceList.push(response.body);
					$("#editDataSourceModal").modal("hide");
				},
				this.errorHandler
			);
		},
		deleteDataSource : function(index){
			this.$http.post("/datasource/delete", this.dataSourceList[index]).then(
				(response)=>{
					console.log(response);
					this.dataSourceList.splice(index, 1);
				},
				this.errorHandler
			);
		},
		showDataSource : function(index){
			this.currentDataSource = this.dataSourceList[index];
			this.editMode = "show";
			$("#editDataSourceModal").modal("show");
		},
		editDataSource : function(index){
			this.currentDataSource = $.extend({}, this.dataSourceList[index]);
			this.currentIndex = index;
			this.editMode = "edit";
			$("#editDataSourceModal").modal("show");
		},
		updateDataSource : function(){
			this.$http.post("/datasource/update", this.currentDataSource).then(
				(response)=>{
					console.log(response);
					this.currentDataSource = response.body;
					this.dataSourceList[this.currentIndex] = response.body;
					$("#editDataSourceModal").modal("hide");
				},
				this.errorHandler
			);
		},
		testDataSource : function(){
			this.$http.post("/datasource/test", this.currentDataSource).then(
				(response)=>{
					console.log(response);
					alert(response.bodyText);
				},
				this.errorHandler
			);
		},
		errorHandler : function(response){
			console.log(response);
			alert(response.body.error + "\n" + response.body.message);
		}
	}
})
leftPanelVm.loadAll();