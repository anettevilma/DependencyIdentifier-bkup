package com.tools.dependencyinjection.transformer;

import com.google.gson.Gson;

public final class GSONEncoder {

	private static final Gson GSON = new Gson();
	private  GSONEncoder(){
		
		
	}
	/**
	 * 
	 * @param obj
	 * @param t
	
	
	 * @return String */
	@SuppressWarnings("rawtypes")
	public static String toJson(Object obj, Class t) {
		return GSON.toJson(obj, t);
	}

	/**
	 * 
	
	 * @param jsonStr
	 * @param classT
	
	
	 * @return T */
	public static <T> T fromJson(String jsonStr, Class<T> classT) {
		return GSON.fromJson(jsonStr, classT);
	}

}



