package com.tools.dependencyinjection.bl;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.tools.dependencyinjection.dto.DependencyDetail;
import com.tools.dependencyinjection.dto.DependencyInjectionRequest;
import com.tools.dependencyinjection.dto.DependencyInjectionResponse;

public class DependencyInjectionParser extends DefaultHandler {
	DependencyInjectionResponse response = new DependencyInjectionResponse();

	public DependencyInjectionResponse getResponse(
			DependencyInjectionRequest request) throws FileNotFoundException, IOException {
		parseDocument(request.getProjectPath() + "\\\\ivy.xml");
	
		
		return response;

	}

	void parseDocument(String xmlPath) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			parser.parse(xmlPath, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	DependencyDetail details;

	@Override
	public void startElement(String s, String s1, String elementName,
			Attributes attributes) throws SAXException {

		if (elementName.equalsIgnoreCase("dependency")) {
			details = new DependencyDetail();
			details.setJarName(attributes.getValue("name"));
			details.setJarVersion(attributes.getValue("rev"));

		}

	}

	@Override
	public void endElement(String uri, String localName, String element)
			throws SAXException {
		if (element.equalsIgnoreCase("dependency")) {
			response.getDependencyDetailLists().add(details);
		}
	}

}
