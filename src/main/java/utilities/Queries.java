package utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Queries {
	private static final Logger logger = LogManager.getLogger(Queries.class);

	private static final String RESOURCE_DIR = "src/main/resources";
	private static final String QUERY_DIR = RESOURCE_DIR + "/queries";

	private static final Map<String, String> QUERY_MAP = buildQueryMap();
	

	public static String getQuery(String name) {
		return QUERY_MAP.get(name);
	}
	

	private static Map<String,String> buildQueryMap(){
		Map<String, String> queryMap = new HashMap<>();
		File queryDirectory = new File(QUERY_DIR);
		Set<File> queryFiles = listFiles(queryDirectory, true);

		for(File f : queryFiles) {
			String fileName = f.getName();
			if(!fileName.endsWith(".txt")) {
				continue;
			}
			String key = fileName.substring(0, fileName.length() - 4);
			String value = readContent(f);
			queryMap.put(key, value);
		}
		return  Collections.unmodifiableMap(queryMap);
	}
	
	private static Set<File> listFiles(File dir, boolean includeSubdir) {
		Set<File> fileTree = new HashSet<>();
		if (dir == null || !dir.exists()) {
			return fileTree;
		}
		File[] children = dir.listFiles();
		if (children == null) {
			fileTree.add(dir);
		} 
		else {
			for (File entry : children) {
				if (entry.isFile())
					fileTree.add(entry);
				else if(includeSubdir)
					fileTree.addAll(listFiles(entry, includeSubdir));
			}
		}
		return fileTree;
	}
	
	private static final String readContent(File f) {
		try {
			return Files.readAllLines(f.toPath())
				.stream()
				.collect(Collectors.joining());
		} catch (IOException e) {
			//we should know this at compile time.
			logger.error("error reading file content", e);
			throw new IllegalStateException(e);
		}
	}
	
	private Queries() {
		//no constructor for util class
	}
}
