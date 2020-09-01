package com.tools.dependencyinjection.xsd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;

import com.tools.dependencyinjection.bl.DependencyInjectionManager;
import com.tools.dependencyinjection.dto.DependencyDetail;
import com.tools.dependencyinjection.dto.DependencyInjectionRequest;
import com.tools.dependencyinjection.dto.DependencyInjectionResponse;
import com.tools.dependencyinjection.ivy.dto.IvyModule;
import com.tools.dependencyinjection.ivy.dto.IvyModule.Dependencies.Dependency;


public class XmlToJavaObject {
	
	//private static Logger LOGGER = Logger.getLogger(DependencyInjectionService.class);
	public static String marshal(List<DependencyDetail> webInfoLibJarList,DependencyInjectionRequest request) throws IOException,JAXBException,
	FileNotFoundException {
		StringWriter writer= new StringWriter();
		try{
			// XML and Java binding
		JAXBContext jaxbContext = JAXBContext.newInstance(IvyModule.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		
		// XML and Java main class
		File xml = new File(request.getProjectPath()+"/ivy.xml");
		IvyModule module = (IvyModule) jaxbUnmarshaller.unmarshal(xml);
		
		// print the response for debugging
        System.out.println("ssssssss  "+writer.toString());
		System.out.println("module :" + module);
		System.out.println("WebInfoJarList --->");
		System.out.println("info: " + module.getInfo());
		System.out.println("configurations: " + module.getConfigurations());
		System.out.println("publications " + module.getPublications());
		System.out.println("dependencies: " + module.getDependencies());
		System.out.println("conflicts: " + module.getConflicts());
		System.out.println("version: " + module.getVersion());
		System.out.println(" ");
		

		// print the XML contains to java object
		IvyModule.Dependencies dependency = module.getDependencies();
		List<IvyModule.Dependencies.Dependency> Depdependency = (List<IvyModule.Dependencies.Dependency>) dependency
				.getDependency();
		
		
		{
			Iterator<Dependency> itr = Depdependency.iterator();

			while (itr.hasNext()) {

				IvyModule.Dependencies.Dependency Webdependency = (IvyModule.Dependencies.Dependency) itr
						.next();
				System.out.println("org: " + Webdependency.getOrg());
				System.out.println("name: " + Webdependency.getName());
				System.out.println("rev: " + Webdependency.getRev());
				System.out.println("conf: " + Webdependency.getConf());
				System.out.println(" ");

			}

		}
		
		
		//List<DependencyDetail> dependencyDetailList=DependencyInjectionManager.findWebInfoLibJarList("D:\\Workspace_Java\\DFWebServices");
		Dependency dependency1=null;
		for (DependencyDetail dependencyDetail : webInfoLibJarList) {
			dependency1=new Dependency();
			dependency1.setName(dependencyDetail.getJarName());
			dependency1.setRev(dependencyDetail.getJarVersion());
			System.out.println("<------------------Jar list inside WebContent->WEB-INF->lib-------------------> " );
			module.getDependencies().getDependency().add(dependency1);
			System.out.println("name: " +dependencyDetail.getJarName());
			System.out.println("rev: " +dependencyDetail.getJarVersion());
			
		}
		Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(module, writer);
	
	} catch (UnmarshalException ue) {
		System.out.println("Caught UnmarshalException");
	} catch (JAXBException je) {
		je.printStackTrace();
	}
		return writer.toString();
		
	

}
}