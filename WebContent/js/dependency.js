var response = "";
var lastgridsel;
function displayGrid() {


	myGrid = $("#dependencyIdentGrid")
        .jqGrid({
            datatype: "jsonstring",
            colNames: ["Name", "Version", "IsDeprecatedJar"],
            colModel: [{
                name: "jarName",
                index: "jarName",
                width: 350,
                align: "center",
                sortable: false
            }, {
                name: "jarVersion",
                index: "jarVersion",
                width: 270,
                align: "center",
                sortable: false,
                editable: true,
                editoptions: {
                    size: 20,
                    maxlength: 10,
	                dataEvents: [
			                        {
			                         type: 'keyup',
			                         fn: function(e) {
			                                           // to support all browsers
			                                           //something on key press by manipulating above value
			                                          
			                                           var c=$(this).attr('id');
						                            	console.log(c);
						                            	 jQuery('#dependencyIdentGrid').jqGrid('saveRow',c,false,'clientArray');
						                                 jQuery('#dependencyIdentGrid').jqGrid('editRow',c, true,{color:'red'});
	
			                                }
			                        }
			                       ]
                }
            }, {
                name: "flag",
                index: "flag",
                align: "center",
                width: 350,
                sortable: false
            }],
            height: 350,
            viewrecords: true,
            jsonReader: {
                repeatitems: false,
                root: function(obj) {
                    return obj;
                },
                page: function(obj) {
                    return 1;
                },
                total: function(obj) {
                    return 1;
                },
                records: function(obj) {
                    return obj.length;
                }
            },
            scroll:1,
            pager: "#pagerdependencyIdentifier",
            forceFit: true,
            autowidth: true,
            scrollrows: true,
            width: null,
            shrinkToFit: true,
            cellEdit: true,
            //rowNum: 10,
            loadComplete: function() { 
                var $this = $(this),
                    ids = $this
                    .jqGrid('getDataIDs'),
                    i, l = ids.length;
                for (i = 0; i < l; i++) {
                    $this.jqGrid('editRow', ids[i], true);
                }
              

            },
            gridComplete: function(){
            	  if (response != "") {
            		  var $this = $(this),
                      ids = $this
                      .jqGrid('getDataIDs'),
                      i, l = ids.length;
                  for (i = 0; i < l; i++) {
                      
                          //alert('#'+i);
                          var value = null;
                          value =$this.jqGrid('getCell',ids[i],'flag');
                          console.log(ids[i]);
                          console.log("Value is " +value); 
                          if (value === 'true') { 
                              $('#'+ ids[i]).css({ 
                                  'background-color': 'red'  
                              });
                          }
                          // console.log("Value is "+value);

                      }
                  }
            }
        });

}
$(document)
    .ready(
        function() {
        	 $("#view-source")
             .click( function() {
            	 reset();
             }
             );
        	
        	 var changedDataList=[];
            var globalresponse;
            displayGrid();
            $("#GenerateXmlFilebutton")
                .click(
                    function() {
                    	  $("#StatusReport").fadeOut();
                          $("#ivyText").fadeOut();
                          $("#jarscard").fadeOut();
                     	
                     	$("#ivyarea").val("");
                     	var dataIds="";
                    	var dataidlength="";
                    	var rowData="";
                    	var ids="";
                    	changedDataList=[];
                    	var  idslength="";var dataState="";
                    	dataIds = jQuery("#dependencyIdentGrid").getDataIDs();
                    	dataidlength=dataIds.length;
                    	console.log(dataidlength)
                    	   for (var i = 0; i < dataidlength ; i++) {
                    	 $('#dependencyIdentGrid').jqGrid('saveRow',dataIds[i],false,'clientArray');
                    	rowData = $("#dependencyIdentGrid").jqGrid('getRowData',dataIds[i]);
                    	 changedDataList.push(rowData);
                    	 
                    	   }
                    	   console.log(JSON.stringify(changedDataList)); 
                    	   ids = $('#dependencyIdentGrid').jqGrid('getDataIDs'); 
                    	    idslength=ids.length;
                    		for (i = 0; i < idslength; i++) {
                    			$('#dependencyIdentGrid').jqGrid('editRow', ids[i], true); 
                    		}
                    		 dataState=JSON.stringify(changedDataList); 
                    		 console.log(changedDataList);
                    		 $.blockUI({
                                 message: '<img src="css/images/loading.gif" height="100" width="100"/>'
                             });
                    		 $.ajax({
                    				type : 'POST',
                    				url : "rest/di/getIvy",
                    				contentType : 'text/json',
                    				cache : false,
                    				data : {jarlist : dataState},   
                    				// - when passing form parameters you have to use this 'data' field to
                    				// pass data
                    				// to the RESTful service method

                    				success : function(response) {
                    						console.log(response);
                    						  var trHTML = '';
                    						  var data=jQuery.parseJSON(response.LinkedHashMap.jarinfo); 
                    						  
                    						  console.log(data);
                    						  console.log(data.length);

                    					        $.each(data, function (i, item) {
                    					        var styleclass="";
                    					           if(item.jarinfo === "Jar Not found in arfifactory"){
                    					        	   styleclass='#f9bdbb';
                    					           }
                    					            trHTML += '<tr style="background-color:'+styleclass+'"><td class="mdl-data-table__cell--non-numeric">' + item.jarname + '</td><td>' + item.jarinfo + '</td></tr>';
                    					        });
                    					        console.log(trHTML);
                    					        $("#Statustable tbody tr").remove();   
                    					        $('#Statustable').append(trHTML);
                    					        $("#ivyarea").val(response.LinkedHashMap.ivydata);
                    					        $.unblockUI();
                    					        $("#StatusReport").fadeIn();
                    					        $("#ivyText").fadeIn(); 
                    					        $("#jarscard").fadeIn();
                    					        $("#totaljars").text("Total no of Jars : "+response.LinkedHashMap.totalNoOfJars);
                    					        $("#jarsfound").text("No of Jars Found : "+response.LinkedHashMap.presentInArtifactory);
                    					        $("#jarsnotfound").text("No of Jars Not Found : "+response.LinkedHashMap.notPresentInArtifactory);
                    				},
                    				error : function(jqXHR, textStatus, errorThrown) {   
 
                    					$.unblockUI();
                    				}
                    			});
                        //alert("File name " + globalresponse.DependencyInjectionResponse.fileName);
                        //var url = 'rest/di/exportData?fileName=' + globalresponse.DependencyInjectionResponse.fileName;
                        //window.open(url, '_blank');
                    });

            $("#button")
                .click(
                    function() {
                        $("#form").show();
                        $("#xmlStringDiv").hide();
                      
                        jQuery.support.cors = true;
                        $.blockUI({
                            message: '<img src="css/images/loading.gif" height="100" width="100"/>'
                        });
                        $
                            .ajax({
                                type: "POST", 
                                url: 'rest/di/getDependencies',
                                data: getFileName(),
                                contentType: "application/json; charset=utf-8",
                                dataType: "json",
                                success: function(data) {
                                    globalresponse = data;
                                    var msg = JSON
                                        .parse((JSON
                                            .stringify(data)));
                                    console
                                        .log(JSON
                                            .stringify(data));
                                    if(data.DependencyInjectionResponse.read === "content grid not used"){
                                    	 $('#responseInfo')
                                         .html(
                                             '<button style="background-color: forestgreen;" class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent">'+data.DependencyInjectionResponse.read+'</button>');
                                    }else{
                                    	 $('#responseInfo')
                                         .html(
                                         '<button style="background-color: red;" class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent">'+data.DependencyInjectionResponse.read+'</button>');
                                    } 
                                   

                                    response = data;
                                    $(
                                            "#dependencyIdentGrid")
                                        .jqGrid(
                                            'setGridParam', {
                                                datatype: 'jsonstring',
                                                datastr: response.DependencyInjectionResponse.dependencyDetailLists
                                            })
                                        .trigger(
                                            'reloadGrid'); 
                                    $.unblockUI();
                                    $("#NameVersion").fadeIn(); 
                                },
                                error: function(xhr) {
                                    alert(xhr.responseText);
                                    $.unblockUI(); 
                                }
                            });
                    });
        });
var request = {};
var req = {};
var myObject = new Object();
var myString;

function getFileName() {
    var projectPath = document.getElementById("projectPath").value; 
    //var excelfile=document.getElementById("deprecatedJarListPath").value;
    req.appName = "ESVS";
    req.projectPath = projectPath;
    //req.ivyXMLPath=projectPath;
    //req.deprecatedJarListPath=excelfile;
    request.DependencyInjectionRequest = req;
    myString = JSON.stringify(request);
    alert(myString);
    return myString;
}
function reset(){
	 $("#NameVersion").fadeOut(); 
	 $("#StatusReport").fadeOut();
     $("#ivyText").fadeOut();
     $("#jarscard").fadeOut();
	$("#Statustable tbody tr").remove();   
	$("#ivyarea").val("");
	jQuery('#dependencyIdentGrid').jqGrid('clearGridData');
}

