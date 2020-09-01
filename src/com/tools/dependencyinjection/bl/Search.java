package com.tools.dependencyinjection.bl;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;






import org.apache.log4j.Logger;

import com.tools.dependencyinjection.dto.ReadFile;
import com.tools.dependencyinjection.dto.ReadFileResponse;
import com.tools.dependencyinjection.service.DependencyInjectionService;

public class Search {
	private static Logger LOGGER = Logger
			.getLogger(Search.class);
		static String data=null;
	 public static String getData() throws IOException ,FileNotFoundException {
			File folder = new File("D:\\workspace\\DependencyIdentifier\\src");
			Properties properties=new Properties();
			
			ReadFileResponse response=new ReadFileResponse();
			List<ReadFile> searchall1=new ArrayList<ReadFile>();
			
			InputStream is=new FileInputStream("D:\\workspace\\DependencyIdentifier\\src\\com\\tools\\dependencyinjection\\dto\\property.properties");
			properties.load(is);
			
			Enumeration<?> e = properties.propertyNames();
			
			
			
			while (e.hasMoreElements()) {
				List<ReadFile> searchList= new ArrayList<ReadFile>();
				String tempWord = properties.getProperty((String) e.nextElement());
				String afterSplit[] = tempWord.split(",");
				List<ReadFile> searchList1=initiateSearch(folder,afterSplit[0],afterSplit[1],searchList);
				if(!searchList.isEmpty()){
					
				searchall1.addAll(searchList1);
				System.out.println("k"+searchall1);
				System.out.println("k"+searchList);
				System.out.println("k"+searchList1);
				}
				//response.setReadFileLists(searchall1);
			}
			
			//exportDataToExcel(outFile, searchall1);
			LOGGER.info(searchall1);
		return data;
		}

		private static List<ReadFile> initiateSearch(File folder, String word, String fileType, List<ReadFile> searchList) {
					
			ReadFile search = null;
			for (File file : folder.listFiles()) {
				//to find file extension
				//to search file
				if (file.isFile() && (file.getName().substring(file.getName().lastIndexOf(".") + 1,file.getName().length()).equals(fileType))) {
				
					search=searchFile(file,word);
					 if(null != search){
						 searchList.add(search);
					}

				}else if(file.isDirectory()){
					File newFile= file.getAbsoluteFile();
					initiateSearch(newFile,word,fileType,searchList);
					
				}
				
				

			}
			
			return searchList;
			
		}

		private static ReadFile searchFile(File file, String word) {
			
			List<Integer> line = findWord(readFile(file), word);
			
			ReadFile search= null;
			int wordCounter = 0;
			String s="";

			if (!line.isEmpty()) {
				
				search=new ReadFile();
				
				search.setFileName(file.getName());
				
				search.setKeyword(word);
				
								
				for (Integer integer : line) {
					s=s+integer;
					wordCounter++;
					if(line.indexOf(integer) != line.size()-1){
					s=s+",";
					}
				}
				search.setLineNumber(s);
				
				//search.setOccurences(wordCounter);
									
				System.out.println(word + " found in " + file.getName()+ " at line: " + s);
				System.out.println("No. of occurences is "+wordCounter);
				data="content grid  used";
			}
			else{
				data="content grid not used";
				System.out.println("not found");
			}
			return search;
		}

		private static String readFile(File fileName) {
			
			String LS = System.getProperty("line.separator");
			StringBuffer fileContent = new StringBuffer();
			BufferedReader reader = null;
			try {
				FileReader fr = new FileReader(fileName);
				reader = new BufferedReader(fr);
				String line;
				while ((line = reader.readLine()) != null) {
					fileContent.append(line).append(LS);
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return fileContent.toString();
		}

		private static List<Integer> findWord(String sb, String word) {
			int i;
			System.out.println("4");
			List<Integer> c = new ArrayList<Integer>();
			String a[] = sb.split("\n");
			for (i = 0; i < a.length; i++) {
				if (a[i].contains(word)) {
					c.add(i + 1);
				}
			}
			return c;
		}
		
	/*public static void exportDataToExcel(FileOutputStream outFile,List<ReadFile> searchList) throws IOException {
			
			HSSFWorkbook workbook = new HSSFWorkbook();
			List<String> columnNamesList = new ArrayList<String>();
			
			// Add excel header row to the list
			columnNamesList.add("Keyword");
			columnNamesList.add("File Name");
			columnNamesList.add("Line Number");
			columnNamesList.add("No Of Occurence");
			
			createSheetWithData(workbook, searchList,columnNamesList );
			
			workbook.write(outFile);
			outFile.flush();
			outFile.close();
			System.out.println("successfully on disk."); 
		
		}
		
		*//**
		 * This method creates the actual sheet with data in the excel.
		 *
		 * @param workbook the workbook
		 * @param ReadFile the search dto
		 * @param columnNamesList the column names list
		 *//*
		private static void createSheetWithData(HSSFWorkbook workbook,List<ReadFile> searchList,	List<String> columnNamesList) {
			
			//** To create a sheet based on sheetName param **//*
			HSSFSheet worksheet = workbook.createSheet("Result Sheet");

			//** To create static Header Row based on the columnNamesList param **//*
			HSSFRow row1 = worksheet.createRow(0);
			
			HSSFCellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			//** To create cell-0 **//*
			HSSFCell cellA1 = row1.createCell(0);
			cellA1.setCellValue(new HSSFRichTextString(columnNamesList.get(0)));
			cellA1.setCellStyle(cellStyle);

			//** To create cell-1 **//*
			HSSFCell cellB1 = row1.createCell(1);
			cellB1.setCellValue(new HSSFRichTextString(columnNamesList.get(1)));
			cellB1.setCellStyle(cellStyle);

			//** To create cell-2 **//*
			HSSFCell cellC1 = row1.createCell(2);
			cellC1.setCellValue(new HSSFRichTextString(columnNamesList.get(2)));
			cellC1.setCellStyle(cellStyle);
			//** To create cell-3 **//*
			HSSFCell cellD1 = row1.createCell(3);
			cellD1.setCellValue(new HSSFRichTextString(columnNamesList.get(3)));
			cellD1.setCellStyle(cellStyle);

			createDataRows(searchList, worksheet);
			
		}
		
		
		private static void createDataRows(List<ReadFile> searchList,HSSFSheet worksheet) {
			
			HSSFRow dataRows;
			int rowCount = 1;
			
			for (ReadFile ReadFile : searchList)
			{
				
				dataRows = worksheet.createRow((short) rowCount);

				//** To create cell-0 **//*
				HSSFCell cellA1 = dataRows.createCell(0);
				cellA1.setCellValue(new HSSFRichTextString(ReadFile.getKeyword()));

				//** To create cell-1 **//*
				HSSFCell cellB1 = dataRows.createCell(1);
				cellB1.setCellValue(new HSSFRichTextString(ReadFile.getFileName()));

				//** To create cell-2 **//*
				HSSFCell cellC1 = dataRows.createCell(2);
				cellC1.setCellValue(new HSSFRichTextString(ReadFile.getLineNumber()));
				
				//** To create cell-3 **//*
				HSSFCell cellD1 = dataRows.createCell(3);
				cellD1.setCellValue(new HSSFRichTextString(String.valueOf(ReadFile.getOccurences())));

				rowCount++;
			}
			
		}*/
}
