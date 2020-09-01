package com.tools.dependencyinjection.json;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

/*
 * SXD8442 Configure objectMapper to account for the root node in JSON request
 * */
@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class JsonMessageBodyReader implements MessageBodyReader<Object> {

	@Override
	public boolean isReadable(Class<?> paramClass, Type paramType,
			Annotation[] paramArrayOfAnnotation, MediaType paramMediaType) {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object readFrom(@SuppressWarnings("rawtypes") Class paramClass, Type paramType,
			Annotation[] paramArrayOfAnnotation, MediaType paramMediaType,
			@SuppressWarnings("rawtypes") MultivaluedMap paramMultivaluedMap, InputStream paramInputStream)
			throws IOException {
		ObjectMapper obj = new ObjectMapper();
		obj.configure(DeserializationConfig.Feature.UNWRAP_ROOT_VALUE, true);
		obj.configure(DeserializationConfig.Feature.READ_ENUMS_USING_TO_STRING,
				true);
		return obj.readValue(paramInputStream, paramClass);
	}
}
