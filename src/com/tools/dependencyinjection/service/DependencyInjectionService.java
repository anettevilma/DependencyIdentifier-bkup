package com.tools.dependencyinjection.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import com.artifactory.tool.JavaHttpUrlConnectionReader;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.tools.dependencyinjection.bl.DependencyInjectionManager;
import com.tools.dependencyinjection.dto.DependencyInjectionRequest;
import com.tools.dependencyinjection.dto.DependencyInjectionResponse;

@Path("/di")
public class DependencyInjectionService {
	private static Logger LOGGER = Logger
			.getLogger(DependencyInjectionService.class);
	@Context
	ServletContext context;
	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/getDependencies")
	public DependencyInjectionResponse getDependencies(
			DependencyInjectionRequest request) throws IOException {
		LOGGER.debug("request start " + request);
		DependencyInjectionResponse response = new DependencyInjectionResponse();
		LOGGER.debug("Request:" + request);

		if (request != null) {
			LOGGER.trace("Ivy XML Path" + request.getProjectPath());
			/*LOGGER.trace("Deprecated Jar List Path"
					+ request.getDeprecatedJarListPath());*/
			response = DependencyInjectionManager.getXml(request,context);
			LOGGER.info("Info Message!");
			LOGGER.warn("Warn Message!");
			LOGGER.error("Error Message!");
			LOGGER.fatal("Fatal Message!");
		}
		return response;
	}

	// as download button will be clicked it come to this part to search file
	// with the given filename
	@GET
	@Path("/exportData")
	@Produces({ "application/xml" })
	public javax.ws.rs.core.Response exportData(
			@QueryParam("fileName") String fileName) {
		String fileLocation = System.getProperty("os.name");

		if (fileLocation.contains("Win")) {
			fileLocation = "C:\\opt\\isv\\";
		} else {
			fileLocation = "/var/opt/tomcat/tmp/";
		}

		File file = new File(fileLocation + fileName);
		ResponseBuilder response = null;
		response = javax.ws.rs.core.Response.ok((Object) file).type(
				"application/xml");

		response.header("Content-Disposition", "attachment; filename="
				+ fileName);

		// Delete the file from server before creation of new file.
		if (file.exists()) {
			file.deleteOnExit();
		}

		return response.build();

	}

@POST
@Produces({  MediaType.APPLICATION_JSON })
@Path("/getIvy")
public Map<String, String> getVersions(@FormParam("jarlist") String jarlist) throws Exception {
	String k1="";
	  int inArtifactory=0;
      int notInArtifactory=0;
	//System.out.println(jarlist);
	//System.out.println(jarlist.length());;
	jarlist="{\"jarslist\""+jarlist+"}";
    System.out.println(jarlist);
    JSONObject json = (JSONObject) new JSONParser().parse(jarlist.toString());
    System.out.println(json);  
    JSONArray jsar= (JSONArray) json.get("jarslist");
    System.out.println(jsar);
    System.out.println(jsar.size());
    int jararraysize=jsar.size();
    JSONArray infodata=new JSONArray();
    for(int i=0;i<jararraysize;i++){
    	Map<String, String> info =new LinkedHashMap<String, String>();
    	JSONObject  object = (JSONObject) jsar.get(i);
     String jarname =(String) object.get("jarName");
     String jarversion =(String) object.get("jarVersion");
     jarname = jarname.replace(".jar","");
     String jarNameToSend="";
     String searchType="quick";
     JSONObject quickRequest = new JSONObject();
     System.out.println("jarversion"+jarversion);
     if(jarversion != null && !jarversion.isEmpty()){
    	 jarNameToSend=jarname+"-"+jarversion+".jar*";
    	 quickRequest.put("search", searchType);
    	 quickRequest.put("query", jarNameToSend);
     }else{
    	 jarNameToSend=jarname+".jar*";
    	 quickRequest.put("search", searchType);
    	 quickRequest.put("query", jarNameToSend);
     }
        System.out.println(jarNameToSend);
        String returnIvy=JavaHttpUrlConnectionReader.JavaHttpUrlConnectionReaders(quickRequest);
      
        if(returnIvy.equals("")){
    	   info.put("jarname" , jarname);
    	   info.put("jarinfo","Jar Not found in arfifactory");
    	   notInArtifactory+=1;
       }else{
        k1=k1+returnIvy;
        info.put("jarname" , jarname);
        info.put("jarinfo","Jar found Ivy generated");
        inArtifactory+=1;
       }
       infodata.add(info);
       }
    Map<String, String> infosend=new LinkedHashMap<String, String>();
    infosend.put("jarinfo", infodata.toString());
    infosend.put("ivydata", k1);
    String totalJars=Integer.toString(jsar.size());
    infosend.put("totalNoOfJars",totalJars);
    infosend.put("presentInArtifactory", Integer.toString(inArtifactory));
    infosend.put("notPresentInArtifactory", Integer.toString(notInArtifactory));
    System.out.println(k1);  
    System.out.println(infodata); 
	return infosend; 
}
}