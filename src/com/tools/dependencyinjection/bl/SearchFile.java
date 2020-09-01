package com.tools.dependencyinjection.bl;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class SearchFile {
	private static final String keyword="Taaa";
	static List<String> fileList=new ArrayList<String>();
	public static void main(String[] args) {
		File file=new File("src");
		traversePath(file);
		Iterator<String> iterator=null;
		iterator=fileList.iterator();
		while(iterator.hasNext()){
			System.out.println(iterator.next());
			searchData(iterator.next());
		}
			
	}


	public static void traversePath(File file){
		if(file.isDirectory()){
			File allfiles[]=file.listFiles();
			for(File file1:allfiles){
				traversePath(file1);
			}
		}
		else{
			//System.out.println(file.getAbsolutePath());
			fileList.add(file.getAbsolutePath());
			
		}
	}
	
	private static void searchData(String path) {
		
	}

}
