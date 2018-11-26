var mainPanelVm = new Vue({
	el : "#mainPanel",
	data : {
		currentDataSource : null,
		tableNameList : [],
		displayTableNameList : [],
		search : {
			tableName : ""
		},
		delaySearch : null,
		basicConfig : {
			total : 1
		},
		fieldConfigs : [],
		task : {
			ticketId : 0,
			progress : 0
		}
	},
	methods : {
		setCurrentDataSource : function(dataSource){
			this.currentDataSource = dataSource;
			if(this.currentDataSource != null){
				this.showTables();
			}
		},
		showTables : function(){
			this.tableName = "";
			this.tableNameList = [];
			this.displayTableNameList = [];
			this.search.tableName = "";
			this.$http.post("/datasource/show/tables", this.currentDataSource).then(
				(response)=>{
					console.log(response);
					var nameList = [];
					$.each(response.body, function(idx, elem){
						for(var i in elem){
							nameList.push(elem[i]);
							break;
						}
					});
					this.tableNameList = nameList;
					this.displayTableNameList = nameList;
				},
				this.errorHandler
			);
		},
		filterTableName : function(){
			clearTimeout(this.delaySearch);
			this.delaySearch = setTimeout(this.searchTableName, 300);
		},
		searchTableName : function(){
			this.displayTableNameList = [];
			var nameList = [];
			var tableName;
			for(var idx in this.tableNameList){
				tableName = this.tableNameList[idx];
				if(tableName.indexOf(this.search.tableName)>-1){
					this.displayTableNameList.push(tableName);
				}
			}
		},
		showTableDesc : function(tableName){
			this.basicConfig = {
				total : 1,
				tableName : tableName,
				dataSource : this.currentDataSource
			}
			this.fieldConfigs = [];
			this.$http.get("/datasource/table/desc/" + tableName).then(
				(response)=>{
					console.log(response);
					var fieldConfigList = [];
					$.each(response.body, function(idx, elem){
						var fieldConfig = {};
						fieldConfig.fieldName = elem.Field;
						fieldConfig.type = elem.Type;
						fieldConfig.pattern = mainPanelVm.getPattern(elem.Type);
						fieldConfig.value = mainPanelVm.getDefaultValue(elem);
						fieldConfig.rule = "null";
						fieldConfigList.push(fieldConfig);
					});
					this.fieldConfigs = fieldConfigList;
					$("#generatorConfigModal").modal("show");
				},
				this.errorHandler
			);
		},
		getDefaultValue : function(field) {
			if(!field.Default){
				return "";
			}
			if(field.Default.startsWith("CURRENT_TIMESTAMP")){
				return this.getCurrentDateTime();
			}
			return field.Default;
		},
		getCurrentDateTime : function() {
			var now = new Date();
			var result = now.getFullYear() + "-";
			result += ((now.getMonth()+1)<10? "0":"") + (now.getMonth()+1) + "-";
			result += (now.getDate()<10? "0":"") + now.getDate() + " ";
			result += (now.getHours()<10? "0":"") + now.getHours() + ":";
			result += (now.getMinutes()<10? "0":"") + now.getMinutes() + ":";
			result += (now.getSeconds()<10? "0":"") + now.getSeconds() + ".000";
			return result;
		},
		getPattern : function(type){
			if(type.startsWith("bigint") || type.startsWith("int")){
				return "\\d+";
			} else if (type.startsWith("timestamp")){
				return "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}(\\.\\d{3})?";
			}
			return ".?";
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
		tempSaveGenerationConfig : function(){
			if(window.localStorage){
				var generationConfig = {
					basicConfig : this.basicConfig,
					fieldConfigs : this.fieldConfigs
				};
				var templateName = 'template_' + generationConfig.basicConfig.tableName;
				window.localStorage[templateName] = JSON.stringify(generationConfig);
				this.$alert("message.save.success");
			}else{
				alert('This browser does NOT support localStorage');
			}
		},
		tempLoadGenerationConfig : function(){
			if(window.localStorage){
				var templateName = 'template_' + this.basicConfig.tableName;
				var generationConfig = JSON.parse(window.localStorage[templateName]);
				this.basicConfig.total = generationConfig.basicConfig.total;
				for(var i in this.fieldConfigs){
					for(var j in generationConfig.fieldConfigs){
						var fieldConfig = generationConfig.fieldConfigs[j];
						if(this.fieldConfigs[i].fieldName == fieldConfig.fieldName){
							this.fieldConfigs[i].value = fieldConfig.value;
							this.fieldConfigs[i].rule = fieldConfig.rule;
							this.fieldConfigs[i].expression = fieldConfig.expression;
							break;
						}
					}
				}
			}else{
				alert('This browser does NOT support localStorage');
			}
		},
		errorHandler : function(response){
			console.log(response);
			alert(response.body.error + "\n" + response.body.message);
		}
	}
});