var mainPanelVm = new Vue({
	el : "#templateManagerPanel",
	data : {
		refreshFlag : 0,
		search : {
			templateName : ""
		},
		currentTemplateName : "",
		basicConfig : {
			total : 1,
			dataSource : {}
		},
		fieldConfigs : [],
		task : {
			ticketId : 0,
			progress : 0
		},
		databaseOptions : []
	},
	computed : {
		displayTemplateNameList : function() {
			console.log(this.refreshFlag);
			var nameList = [];
			for(var templateName in window.localStorage){
				if(templateName.startsWith('template_') && templateName.indexOf(this.search.templateName)>-1){
					nameList.push(templateName);
				}
			}
			return nameList;
		}
	},
	methods : {
		showTemplate : function(templateName){
			this.currentTemplateName = templateName;
			this.tempLoadGenerationConfig();
			this.loadDatabases();
		},
		loadDatabases : function(){
			this.databaseOptions = [];
			this.$http.post("/datasource/show/databases", this.basicConfig.dataSource).then(
				(response)=>{
					console.log(response);
					for(var idx in response.body){
						this.databaseOptions.push(response.body[idx].Database);
					}
				},
				this.errorHandler
			);
		},
		tempSaveGenerationConfig : function(){
			if(window.localStorage){
				var generationConfig = {
					basicConfig : this.basicConfig,
					fieldConfigs : this.fieldConfigs
				};
				window.localStorage[this.currentTemplateName] = JSON.stringify(generationConfig);
				this.$alert("message.save.success");
			}else{
				alert('This browser does NOT support localStorage');
			}
			$("#generatorConfigModal").modal("hide");
		},
		tempLoadGenerationConfig : function(){
			if(window.localStorage){
				var generationConfig = JSON.parse(window.localStorage[this.currentTemplateName]);
				this.basicConfig = generationConfig.basicConfig;
				this.fieldConfigs = generationConfig.fieldConfigs;
			}else{
				alert('This browser does NOT support localStorage');
			}
			$("#generatorConfigModal").modal("show");
		},
		deleteTemplate : function(templateName){
			if(confirm("Are you sure?")){
				delete window.localStorage[templateName];
				this.refreshFlag++;
			}
		},
		submitGenerationTask : function(){
			var generationConfig = {
				basicConfig : this.basicConfig,
				fieldConfigs : []
			};
			$.each(this.fieldConfigs, function(idx, elem){
				if(elem.rule!="null"){
					generationConfig.fieldConfigs.push(elem);
				}
			})
			console.log(generationConfig);
			
			this.$http.post("/data/generation/submit", generationConfig).then(
				(response)=>{
					console.log(response);
					this.task.ticketId = response.bodyText;
					this.task.progress = 0;
					$("#generatorConfigModal").modal("hide");
					this.showGenerationProgress();
				},
				this.errorHandler
			);
		},
		showGenerationProgress : function() {
			this.$http.get("/data/generation/progress/"+this.task.ticketId).then(
				(response)=>{
					console.log(response);
					var progressNumber = (response.bodyText * 100).toFixed(0);
					this.task.progress = progressNumber;
					$("#generationProgress").css("width", progressNumber + "%");
					$("#progressViewModal").modal("show");
					if(progressNumber<100){
						setTimeout(this.showGenerationProgress, 1000);
					} else if (progressNumber==100) {
						setTimeout(this.closeGenerationProgress, 2000);
					} else {
						alert("Progress Error!");
						setTimeout(this.closeGenerationProgress, 1000);
					}
				},
				this.errorHandler
			);
		},
		closeGenerationProgress : function(){
			this.$http.delete("/data/generation/clear/progress/"+this.task.ticketId).then(
				(response)=>{
					console.log(response);
					$("#progressViewModal").modal("hide");
				},
				this.errorHandler
			);
		},
		errorHandler : function(response){
			console.log(response);
			alert(response.body.error + "\n" + response.body.message);
		}
	}
});