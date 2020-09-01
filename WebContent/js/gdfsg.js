$(document).ready(function(){
	
	$("#button").click(function(){
	
		jQuery.support.cors = true;
		$.ajax({	
				type: "POST",
				url: 'http://localhost:8085/DependencyIdentifier/rest/di/getDependencies',				
				data:getFileName(),
				contentType: "application/json; charset=utf-8",
				dataType: "json",
				success: function (data){
					displayGrid(data);
				/* 	var datalength=data.DependencyInjectionResponse.dependencyDetailLists;
					console.log(JSON.stringify(datalength));
					 var datalength1 = datalength.length;
			         console.log(datalength1);
			         */
					
					
				/* 	
					for(var i=1;i<datalength1;i++){
					//alert('#'+i);
					var value=null;
						value=$('#'+i).find('td:nth-child(3)').text();
						if(value==='true'){
							$('#'+i).css({'background':'red'});
						}
						//console.log("Value is "+value);
						
						}
				 */
				
				

			},error: function (xhr){
				alert(xhr.responseText);
			}	
	});
		/* $("#button").click(function(){
			

			$.ajax(
				{	
					type: "POST",
					url: 'http://localhost:8082/DependencyIdentifier/rest/di/xmlToJavaObject',				
					contentType: "application/json",
					dataType: "json",
					success: function (data){
					var url = 'http://localhost:8082/DependencyIdentifier/rest/di/xmlToJavaObject?fileName='+data;
                     window.open(url,'_blank');
					},
					error: function (xhr){
						var url = 'http://localhost:8085/DependencyIdentifier/rest/di/xmlToJavaObject?fileName='+data;
	                     window.open(url,'_blank');
					}

				});
			}); */
	
	 function displayGrid(response)
	{
		
		
		// alert(JSON.stringify(response.DependencyInjectionResponse.dependencyDetailLists));
			
			$("#dependencyIdentGrid")
					.jqGrid(
							{
								datatype : "jsonstring",
								datastr : response.DependencyInjectionResponse.dependencyDetailLists,
								autowidth : true,
								scrollrows : true,
								width: null,
								shrinkToFit: true,
								gridview : true,
								loadonce : true,
								rowNum : 10,							
								colNames : [ "Name", "Version","IsDeprecatedJar"],
								colModel : [
										{
											name : "jarName",
											index : "jarName",
											width : 350,
											align : "center",
											sortable : false
										}, {
											name : "jarVersion",
											index : "jarVersion",
											width : 270,
											align : "center",
											sortable : false
										}, {
											name : "flag",
											index : "flag",
											align : "center",
											width : 350,
											sortable : false
										}		
		
									//	{

											/* 
											loadComplete: function() {
												
											//Get all grid row IDs 
											var rowIDs = jQuery("#dependencyIdentGrid").getDataIDs();   
											for (var i = 0; i < rowIDs.length; i++) {    
												//enumerate rows in the grid
											
											var rowData = $("#dependencyIdentGrid").jqGrid('getRowData', rowIDs[i]);   
											 //If condition is met (update condition as req)
														    // if (rowData["COLNAME_CHECKED"] == "N") {          
															if (deprecatedDepenList.contains(depDetail) != "true") {  
														         //set cell color if other cell value is matching condition
														         $("#dependencyIdentGrid").jqGrid('setCell', rowIDs[i], 
														        		 "COLNAMEModified", "", { color: 'red' });
														        
														     }
														 }

													 */
													],

													height : 350,
													viewrecords : true,
													jsonReader : {
														repeatitems : false,
														root : function(obj) {
															return obj;
														},
														page : function(obj) {
															return 1;
														},
														total : function(obj) {
															return 1;
														},
														records : function(obj) {
															return obj.length;
														}
													},
													
													pager : "#pagerdependencyIdentifier",

													forceFit : true,
											loadComplete:function(){
												var datalength=response.DependencyInjectionResponse.dependencyDetailLists;
												console.log(JSON.stringify(datalength));
												 var datalength1 = datalength.length;
										         console.log(datalength1);
										        
												
												
												
												for(var i=1;i<datalength1;i++){
												//alert('#'+i);
												var value=null;
													value=$('#'+i).find('td:nth-child(3)').text();
													if(value==='true'){
														$('#'+i).css({'background':'red'});
													}
													//console.log("Value is "+value);
													
													}
											}
														
												}).navGrid(
												'#pagerdependencyIdentifier', {
													edit : false,
													add : false,
													del : false,
													search : false,
													refresh : false
												});

							}

						});
	 $("#analyzeFile").click(function(){
		downloadExcelGrid();	
		$.ajax(
			{	
				type: "GET",
				url: 'http://localhost:8085/DependencyIdentifier/rest/di/getData',				
				contentType: "application/json; charset=utf-8",
				dataType: "json",
				success: function (data){
					
					
					excelData=data;
					$("#downloadExcelGrid").jqGrid("setGridParam", 
                            {
                      datatype : 'jsonstring',
                      datastr : data
                   }).trigger("reloadGrid");

				},
				error: function (xhr){
				
					alert(xhr.responseText);
				}
			});
		}
	); 
	
	 
	});


function downloadExcelGrid(){
	
	
	// alert(JSON.stringify(response.DependencyInjectionResponse.dependencyDetailLists));
		
		$("#downloadExcelGrid").jqGrid({
							datatype : "json",
							mtype : "get",
							autowidth : true,
							scrollrows : false,
							//height:$("#box").height(),
							height:'auto',
							shrinkToFit: true,
							gridview : true,
							loadonce : true,
							rowNum : 10,	
							colNames : ["Keyword","FileName","LineNumber","Occurence"],
							colModel : [
									{
										name : "Keyword",
										index : "keyword",
										width : 100,
										align : "left",
										sortable : false,
										jsonmap: "keyword" 
									}, {
										name : "FileName",
										index : "filename",
										width : 100,
										align : "left",
										sortable : false,
										jsonmap: "filename"
									}, {
										name : "LineNumber",
										index : "linenumber",
										align : "left",
										width : 100,
										sortable : false,
										jsonmap: "linenumber"
									},	
									{
										name : "Occurence",
										index : "occurence",
										align : "center",
										width : 40,
										sortable : false,
										jsonmap: "occurence"
									}],

												
												viewrecords : true,
												jsonReader:
													{
			                                          root:'readFile',
			                                          repeatitems : false
													},
												pager : "#pagerdownloadExcel",
												forceFit : true
											}).navGrid(
											'#pagerdownloadExcel', {
												edit : false,
												add : false,
												del : false,
												search : false,
												refresh : false
											});

						}
