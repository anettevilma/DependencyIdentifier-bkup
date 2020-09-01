var excelData;
$(document).ready(function(){
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

						});