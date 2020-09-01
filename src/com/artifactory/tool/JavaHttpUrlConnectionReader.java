package com.artifactory.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

 
/**
 *
 * A complete Java class that shows how to open a URL, then read data (text) from that URL,
 * HttpURLConnection class (in combination with an InputStreamReader and BufferedReader).
 *
 *
 */
public class JavaHttpUrlConnectionReader
{
  public static String JavaHttpUrlConnectionReaders(JSONObject quickRequest) throws IOException
  {
	  String url = "https://maven.artifactory.homedepot.com/artifactory/ui/artifactsearch/quick";
      HttpURLConnection urlConn = null;
      BufferedReader reader = null;
      OutputStream ouputStream = null;
      //String jsonInput = "{\"search\":\"quick\",\"query\":\"asm-3.0.jar\"}";
      String jsonInput = quickRequest.toString();
      System.out.println("jsonInput----------------"+jsonInput);
      JsonElement repoType = null;
      JsonElement jarPath = null;
      String repokey = null;
      String relpath = null;
      String S1 = null;
    try
    {
    	URL urlObj = new URL(url);
        urlConn = (HttpURLConnection) urlObj.openConnection();
        urlConn.setDoOutput(true);
        urlConn.setRequestMethod("POST");
        urlConn.setRequestProperty("Content-Type", "application/json");
        urlConn.setConnectTimeout(5000);
        urlConn.setReadTimeout(5000);
        urlConn.setRequestProperty("Accept", "application/json");
        // send json input request
        ouputStream = urlConn.getOutputStream();
        ouputStream.write(jsonInput.getBytes());
        ouputStream.flush();
        if (urlConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            System.err.println("Unable to connect to the URL...");
        }
        System.out.println("Connected to the server...");
        InputStream is = urlConn.getInputStream();
        reader = new BufferedReader(new InputStreamReader((is)));
        String result = null;
        while((result = reader.readLine()) != null){
            System.out.println(result);
            JsonObject json = new JsonParser().parse(result).getAsJsonObject();	
            JsonArray resultdata = json.getAsJsonArray("results");
            System.out.println(resultdata);
            if(resultdata != null && resultdata.size() > 0 ){
            	System.out.println("Inside If");
            	for (int i = 0; i < 1; i++) {
                    JsonElement finalresult = resultdata.get(i);
                    System.out.println(finalresult);
                    JsonObject jsonObject = finalresult.getAsJsonObject();
                    repoType = jsonObject.get("repoKey");
                    jarPath = jsonObject.get("relativePath");
                }
            }else{
            	System.out.println("Inside Else");
            	System.out.println("Jar Not found in arfifactory");
            	return S1 = "";
            }
        }
        repokey = repoType.toString();
        relpath = jarPath.toString();
        relpath = relpath.replace("/", "%2F");
    }
    catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        try {
            if(reader != null) reader.close();
            if(urlConn != null) urlConn.disconnect();
        } catch(Exception ex){
             }
    }
    if(repokey!= null && relpath!= null){
    	String geturl = "https://maven.artifactory.homedepot.com/artifactory/ui/dependencydeclaration?buildtool=maven&path="+relpath.replace("\"", "")+"&repoKey="+repokey.replace("\"", "");
    	URL obj = new URL(geturl);
    	System.out.println("Connection BEGIN");
    	HttpURLConnection con = (HttpURLConnection) obj.openConnection();
    	System.out.println("Connection END");
    	con.setRequestMethod("GET");
    	System.out.println("\nSending 'GET' request to URL : " + geturl);

    	BufferedReader bufReader = new BufferedReader(
    			new InputStreamReader(con.getInputStream()));
    	String inputLine;
    	StringBuffer strBuf = new StringBuffer();
    	while ((inputLine = bufReader.readLine()) != null) {
    		strBuf.append(inputLine);
    	}
    	bufReader.close();
    	System.out.println("Result-->" + strBuf.toString());
    	S1 = strBuf.toString();
    }
	return S1;
	
  }
}
  