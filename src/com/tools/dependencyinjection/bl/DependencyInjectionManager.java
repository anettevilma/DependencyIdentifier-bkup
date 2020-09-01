package com.tools.dependencyinjection.bl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;

import javax.servlet.ServletContext;
import javax.xml.bind.JAXBException;

import com.tools.dependencyinjection.dto.DependencyDetail;
import com.tools.dependencyinjection.dto.DependencyInjectionRequest;
import com.tools.dependencyinjection.dto.DependencyInjectionResponse;
import com.tools.dependencyinjection.xsd.XmlToJavaObject;



//import java.nio.file.Path;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.NumberUtils;

public class DependencyInjectionManager {
	// private static Logger LOGGER =
	// Logger.getLogger(DependencyInjectionService.class);
	static String prj_path, depjarpath;

	public static DependencyInjectionResponse getXml(
			DependencyInjectionRequest request,ServletContext context) throws IOException {

		System.out.println(request.getProjectPath());

		String path = (request.getProjectPath()).toString();

		path = path.replace('\\', '/');
		prj_path = path;
		//path = (request.getDeprecatedJarListPath()).toString();
		//path = path.replace('\\', '/');
		depjarpath = context.getRealPath("DeprecatedJarList.xls");

		DependencyInjectionResponse response = new DependencyInjectionResponse();
		DependencyInjectionResponse ivyJarNameList = new DependencyInjectionParser()
				.getResponse(request);
		
		List<DependencyDetail> webInfoLibJarList = findWebInfoLibJarList(request
				.getProjectPath());
		// generating Xml File having jar list content of Web/Inf->Lib
		// ..........
		try {
			response.setXmlString(XmlToJavaObject.marshal(webInfoLibJarList,request));

			String fileName = null;
			fileName = "search" + System.currentTimeMillis() + ".xml";
			String fileLocation = System.getProperty("os.name");
			if (fileLocation.contains("Win")) {
				fileLocation = "C:\\opt\\isv\\";
			} else {
				fileLocation = "/var/opt/tomcat/tmp/";
			}
			File file = new File(fileLocation + fileName);

			try (FileOutputStream fop = new FileOutputStream(file)) {

				// if file doesn't exists, then create it
				if (!file.exists()) {
					file.createNewFile();
				}

				// get the content in bytes
				byte[] contentInBytes = response.getXmlString().getBytes();

				fop.write(contentInBytes);
				fop.flush();
				fop.close();
				response.setFileName(fileName);
				System.out.println("Done");

			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		// webInfoLibJarList.addAll(ivyJarNameList.getDependencyDetailLists());
		ivyJarNameList.getDependencyDetailLists().addAll(webInfoLibJarList);
		List<DependencyDetail> finalJarList = findDeprecatedJarDetails(
				depjarpath,
				ivyJarNameList.getDependencyDetailLists());

		response.getDependencyDetailLists().addAll(finalJarList);
		response.setRead(getData(path));
		return response;

	}

	public static List<DependencyDetail> findWebInfoLibJarList(
			String projectPath) {

		// Paths.get("D:\\geetha\\DependencyIdentifier\\DependencyIdentifier");
		Path startDir = Paths.get(projectPath);

		String pattern = "**\\\\WEB-INF\\\\lib\\\\*.{jar}";

		FileSystem fs = FileSystems.getDefault();
		final PathMatcher matcher = fs.getPathMatcher("glob:" + pattern);
		final List<DependencyDetail> jarDetailList = new ArrayList<DependencyDetail>();
		FileVisitor<Path> matcherVisitor = new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file,
					BasicFileAttributes attribs) {
				Path name = file.getFileName();

				if (matcher.matches(file)) {
					DependencyDetail jarDetail = processJarFile(file.toFile());
					jarDetailList.add(jarDetail);

				}
				return FileVisitResult.CONTINUE;
			}
		};

		try {
			Files.walkFileTree(startDir, matcherVisitor);
		} catch (IOException e) {

			e.printStackTrace();
		}
		return jarDetailList;
	}

	protected static DependencyDetail processJarFile(File file) {
		System.out.println(file);
		try (JarFile jf = new JarFile(file, true, 1)) {
			ZipEntry ze = jf.getEntry(JarFile.MANIFEST_NAME);
			DependencyDetail webJarDetail = new DependencyDetail();

			Manifest mf = jf.getManifest();
			Attributes at=null;
			if(mf != null){
				Map<String, Attributes> attr = mf.getEntries();
				at = mf.getMainAttributes();
				if (at.getValue(Attributes.Name.IMPLEMENTATION_VERSION.toString()) == null) {
					if (at.getValue(Attributes.Name.SPECIFICATION_VERSION
							.toString()) != null) {
						// System.out.println(file.getName() +
						// "=======SPEC=========="+at.getValue(Attributes.Name.SPECIFICATION_VERSION.toString())
						// );
						webJarDetail.setJarVersion(at
								.getValue(Attributes.Name.SPECIFICATION_VERSION
										.toString()));
					} else if (at.getValue("Bundle-Version") != null) {// Bundle-Version
						// System.out.println(file.getName() +
						// "=======BUILD=========="+at.getValue("Bundle-Version") );
						webJarDetail.setJarVersion(at.getValue("Bundle-Version"));
					} else {
						String ver = "";
						if (file.getName().lastIndexOf("-") > -1) {
							ver = file.getName().substring(
									file.getName().lastIndexOf("-"),
									FilenameUtils.indexOfExtension(file.getName()));
							System.out.println(file.getName()
									+ "=======NOOOOOOOOOOOT FILEEEEEE =========="
									+ ver);
							String version=ver.replace("-", "");
							webJarDetail.setJarVersion(version);
							webJarDetail.setJarName(file.getName());
						} else {
							System.out.println(file.getName()
									+ "=======NOOOOOOOOOOOT ==========" + ver);
							String version=ver.replace("-", "");
							webJarDetail.setJarVersion(version);
							webJarDetail.setJarName(file.getName());
						}

					}

				} else {
					System.out.println(file.getName()
							+ "=======IMPL=========="
							+ at.getValue(Attributes.Name.IMPLEMENTATION_VERSION
									.toString()));
					webJarDetail.setJarVersion(at
							.getValue(Attributes.Name.IMPLEMENTATION_VERSION
									.toString()));

				}
				if (file.getName() != null && file.getName().length() > 0) {
					webJarDetail.setJarName(removeNumerics(file.getName()));
				}
			}else{
				System.out.println(" Unable to find manifest file: "+ file.getName());
				webJarDetail.setJarName(file.getName());
			}
			
			
			
			// webJarDetail.setJarName(file.getName());
			return webJarDetail;
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}

	}

	private static String removeNumerics(String str) {
		String result = null;
		if (str.matches(".*[\\d].*")) {

			String theStringArray[] = str.split("\\d");
			result = theStringArray[0];
			result = result.substring(0, result.length() - 1);
			System.out.println("Position of first numeric position "
					+ (theStringArray.length == 0 ? 0 : theStringArray[0]
							.length()));
		} else {
			
			result = str;
			System.out.println("No numeric character in the string");
		}

		return result;

	}

	public static List<DependencyDetail> findDeprecatedJarDetails(
			String excelPath, List<DependencyDetail> consolidatedProjJarList) {
		DependencyInjectionRequest request = new DependencyInjectionRequest();
		List<DependencyDetail> deprecatedJarList = null;
		try {

			FileInputStream x = new FileInputStream(excelPath);

			// Create Workbook instance holding reference to .xls file.....
			HSSFWorkbook workbook = new HSSFWorkbook(x);

			// Get first/desired sheet from the workbook.....
			HSSFSheet sheet = workbook.getSheetAt(0);

			Integer columnNo1 = 0;
			Integer columnNo2 = 1;
			// Integer colu
			// output all not null values to the list
			// List<Cell> cells = new ArrayList<Cell>();

			/*
			 * Row firstRow = sheet.getRow(0);
			 * 
			 * for(Cell cell:firstRow){ if
			 * (cell.getStringCellValue().equals(columnWanted)){ columnNo1 =
			 * cell.getColumnIndex(); } }
			 */

			deprecatedJarList = new ArrayList<DependencyDetail>();

			for (Row row : sheet) {
				DependencyDetail jarDetail = new DependencyDetail();
				// Cell jarName = row.getCell(columnNo1);

				String jarName = row.getCell(columnNo1).toString();
				// Cell jarVer = row.getCell(columnNo2);
				String jarVer = row.getCell(columnNo2).toString();

				if (jarName != null && !jarName.isEmpty()) {
					jarName = jarName.substring(1, jarName.length());
					jarDetail.setJarName(jarName.trim());
				}

				if (jarVer != null && !jarVer.isEmpty()) {
					jarVer = jarVer.substring(1, jarVer.length());
					jarDetail.setJarVersion(jarVer.trim());

				}
				if (jarDetail.getJarName() != null
						&& jarDetail.getJarVersion() != null) {
					deprecatedJarList.add(jarDetail);
				}

				/*
				 * if (c == null || c.getCellType() == Cell.CELL_TYPE_BLANK) {
				 * // Nothing in the cell in this row, skip it } else {
				 * cells.add(c); }
				 */
			}
			x.close();

			List<DependencyDetail> finalDeprecatedJarList = new ArrayList<DependencyDetail>();

			for (DependencyDetail jarDetail : consolidatedProjJarList) {
				for (DependencyDetail deprecatedJarDetail : deprecatedJarList) {
					if (jarDetail.getJarName() != null
							&& deprecatedJarDetail.getJarName() != null
							&& deprecatedJarDetail.getJarName()
									.equalsIgnoreCase(jarDetail.getJarName()))

					{
						if (deprecatedJarDetail.getJarVersion() != null
								&& !deprecatedJarDetail.getJarVersion()
										.isEmpty()
								&& deprecatedJarDetail.getJarVersion() != "*"
								&& NumberUtils.isNumber(deprecatedJarDetail
										.getJarVersion())) {
							if (jarDetail.getJarVersion() != null
									&& !jarDetail.getJarVersion().isEmpty()
									&& Float.valueOf(getValue(jarDetail
											.getJarVersion())) < Float
											.valueOf(deprecatedJarDetail
													.getJarVersion())) {
								jarDetail.setFlag(true);
								finalDeprecatedJarList.add(jarDetail);
							}
						} else {
							jarDetail.setFlag(true);
							finalDeprecatedJarList.add(jarDetail);
						}
					}

					/*
					 * if(jarDetail.getJarName().trim().equalsIgnoreCase(
					 * deprecatedJarDetail.getJarName().trim())) {
					 * jarDetail.setFlag(true);
					 * finalDeprecatedJarList.add(jarDetail); }
					 */

					/*
					 * if(jarDetail.getJarName() != null &&
					 * deprecatedJarDetail.getJarName() != null &&
					 * deprecatedJarDetail.getJarName() != "" &&
					 * jarDetail.getJarName
					 * ().equals(deprecatedJarDetail.getJarName())); {
					 * jarDetail.setFlag(true);
					 * finalDeprecatedJarList.add(jarDetail); }
					 */
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return consolidatedProjJarList;
	}
 public static String getValue(String value){
	 
	 String [] values=value.split("\\.");
	 String val="";
	 if(values.length>2){
		 val=values[0]+"."+values[1];
	 }else{
		 val=value;
	 }
	 return val;
 }
	static String data = "content grid not used";

	public static String getData(String path) throws IOException, FileNotFoundException {
		//File currDir = new File(".");
		//String path = currDir.getAbsolutePath();
		//path = (path.substring(0, path.length() - 1)).replace('\\', '/');
		System.out.println("given path"+path);
		File folder = new File(path);
		Properties properties = new Properties();
		InputStream is = DependencyInjectionManager.class
				.getResourceAsStream("/com/tools/dependencyinjection/dto/property.properties");
		DependencyInjectionResponse response = new DependencyInjectionResponse();
		List<DependencyInjectionResponse> searchall1 = new ArrayList<DependencyInjectionResponse>();

		// InputStream is=new
		// FileInputStream("D:\\workspace\\DependencyIdentifier\\src\\com\\tools\\dependencyinjection\\dto\\property.properties");
		properties.load(is);

		Enumeration<?> e = properties.propertyNames(); 
		while (e.hasMoreElements()) {
			List<DependencyInjectionResponse> searchList = new ArrayList<DependencyInjectionResponse>();
			String tempWord = properties.getProperty((String) e.nextElement());
			String afterSplit[] = tempWord.split(",");
			initiateSearch(folder, afterSplit[0], afterSplit[1], searchList);
		}
		return data;
	}

	private static List<DependencyInjectionResponse> initiateSearch(
			File folder, String word, String fileType,
			List<DependencyInjectionResponse> searchList) {

		DependencyInjectionResponse search = null;
		for (File file : folder.listFiles()) {
			// to find file extension
			// to search file
			if (file.isFile()
					&& (file.getName().substring(
							file.getName().lastIndexOf(".") + 1,
							file.getName().length()).equals(fileType))) {

				search = searchFile(file, word);
				if (null != search) {
					searchList.add(search);
				}

			} else if (file.isDirectory()) {
				File newFile = file.getAbsoluteFile();
				initiateSearch(newFile, word, fileType, searchList);

			}

		}

		return searchList;

	}

	private static DependencyInjectionResponse searchFile(File file, String word) {

		List<Integer> line = findWord(DependencyInjectionResponse(file), word);

		DependencyInjectionResponse search = null;

		if (!line.isEmpty()) {

			search = new DependencyInjectionResponse();
			data = "content grid  used";
			System.out.println("reached"+data );
		} else {

			System.out.println("not found");
		}
		return search;
	}

	private static String DependencyInjectionResponse(File fileName) {

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
		List<Integer> c = new ArrayList<Integer>();
		String a[] = sb.split("\n");
		for (i = 0; i < a.length; i++) {
			if (a[i].contains(word)) {
				c.add(i + 1);
			}
		}

		return c;
	}

}

/*
 * private static List<DependencyDetail> findVersionDetails() {
 * 
 * Path startDir = (javax.ws.rs.Path) Paths
 * .get("C:/Users/PP5024112/Desktop/pp12.xls");
 * 
 * String pattern = "**\\\\WEB-INF\\\\lib\\\\*.{jar}";
 * 
 * FileSystem fs = FileSystems.getDefault(); final PathMatcher matcher =
 * fs.getPathMatcher("glob:" + pattern);
 * 
 * FileVisitor<Path> matcherVisitor = new SimpleFileVisitor<Path>() {
 * 
 * @Override public FileVisitResult visitFile(Path file, BasicFileAttributes
 * attribs) { Path name = file.getFileName();
 * 
 * if (matcher.matches(file)) { processJarFile(((java.nio.file.Path)
 * file).toFile()); } return FileVisitResult.CONTINUE; } };
 * Files.walkFileTree(startDir, matcherVisitor);
 * 
 * }
 */

/*
 * protected static void processJarFile(File file) { System.out.println(file);
 * try (JarFile jf = new JarFile(file, true, 1)) { ZipEntry ze =
 * jf.getEntry(JarFile.MANIFEST_NAME);
 * 
 * Manifest mf = jf.getManifest(); Map<String, Attributes> attr =
 * mf.getEntries(); Attributes at = mf.getMainAttributes(); if
 * (at.getValue(Attributes.Name.IMPLEMENTATION_VERSION.toString()) == null) { if
 * (at.getValue(Attributes.Name.SPECIFICATION_VERSION .toString()) != null) {
 * System.out.println(file.getName() + "=======SPEC==========" +
 * at.getValue(Attributes.Name.SPECIFICATION_VERSION .toString())); } else if
 * (at.getValue("Bundle-Version") != null) {// Bundle-Version
 * System.out.println(file.getName() + "=======BUILD==========" +
 * at.getValue("Bundle-Version")); } else { String ver = ""; if
 * (file.getName().lastIndexOf("-") > -1) { ver = file.getName().substring(
 * file.getName().lastIndexOf("-"),
 * FilenameUtils.indexOfExtension(file.getName()));
 * System.out.println(file.getName() +
 * "=======NOOOOOOOOOOOT FILEEEEEE ==========" + ver); } else {
 * System.out.println(file.getName() + "=======NOOOOOOOOOOOT ==========" + ver);
 * }
 * 
 * } } else { System.out.println(file.getName() + "=======IMPL==========" +
 * at.getValue(Attributes.Name.IMPLEMENTATION_VERSION .toString())); }
 * 
 * } catch (IOException e) { ex.printStackTrace(); } return; }
 */
/*
 * @SuppressWarnings("unused") private static List<DeprecatedDependencyDetail>
 * findDeprecatedDetails( String excelPath, List<DeprecatedDependencyDetail>
 * dependencyList) { List<DeprecatedDependencyDetail> dependencyFinalList = new
 * ArrayList<DeprecatedDependencyDetail>(); List<DeprecatedDependencyDetail>
 * deprecatedDepenList = new ArrayList<DeprecatedDependencyDetail>();
 * 
 * try { FileInputStream x = new FileInputStream(new File(
 * "C:/Users/PP5024112/Desktop/pp12.xls"));
 * 
 * // Create Workbook instance holding reference to .xls file..... HSSFWorkbook
 * workbook = new HSSFWorkbook(x);
 * 
 * // Get first/desired sheet from the workbook..... HSSFSheet sheet =
 * workbook.getSheetAt(0);
 * 
 * // Iterate through each rows one by one..... for (Iterator<Row> iterator =
 * sheet.iterator(); iterator.hasNext();) { Row row = (Row) iterator.next(); for
 * (Iterator<Cell> iterator2 = row.iterator(); iterator2 .hasNext();) { Cell
 * cell = (Cell) iterator2.next();
 * System.out.println(cell.getStringCellValue()); DeprecatedDependencyDetail
 * depDepnedDetail = new DeprecatedDependencyDetail();
 * depDepnedDetail.setDeprecatedJarName(cell .getStringCellValue());
 * depDepnedDetail.setDeprecatedJarVersion(excelPath); deprecatedDepenList
 * .addAll((Collection<? extends DeprecatedDependencyDetail>) depDepnedDetail);
 * }
 * 
 * }
 * 
 * x.close();
 * 
 * }
 * 
 * catch (Exception e)
 * 
 * { e.printStackTrace(); }
 * 
 * return deprecatedDepenList;
 */

// flag for identify DependencyDetail are deprecated or not.......
/*
 * boolean flag = true;
 * 
 * for (DeprecatedDependencyDetail depDetail : dependencyList) {
 * 
 * if (deprecatedDepenList.contains(depDetail)) { flag = true;
 * 
 * } else {
 * 
 * flag = false; } dependencyFinalList.add((DeprecatedDependencyDetail)
 * depDetail); }
 * 
 * return dependencyFinalList;
 * 
 * } }
 */
