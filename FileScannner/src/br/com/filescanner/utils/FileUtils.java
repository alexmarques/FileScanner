package br.com.filescanner.utils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class FileUtils {
	
	private static Set<Path> unusedFiles = new HashSet<>();
	private static Set<Path> inexistedFiles = new HashSet<Path>();
	private static Path relativePath;
	public static boolean fileFound = false;
	
	public static void setRelativePath(Path relativePath) {
		FileUtils.relativePath = relativePath;
	}
	
	public static void addUnusedFile(Path path) {
		unusedFiles.add(path);
	}
	
	public static Set<Path> getUnusedFiles() {
		return unusedFiles;
	}
	
	public static void addInexistedFile(Path currentFile, Path fileDeclaredInsideCurrentFile) {
		inexistedFiles.add(fileDeclaredInsideCurrentFile);
	}
	
	public static Path resolve(Path path, String fileName) {
		if(path.toString().indexOf("Modal.js") != -1 || fileName.indexOf("Modal.js") != -1) {
			//System.out.println(fileName);
		}
		if(!fileName.startsWith("\\") && !fileName.startsWith("/")) {
			return path.resolveSibling(fileName).normalize();
		} else {
			return Paths.get(relativePath.toString(), fileName);
		}
	}
}