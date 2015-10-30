package br.com.filescanner;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import br.com.filescanner.utils.FileUtils;
import br.com.filescanner.visitor.DefaultFileVisitor;

public class LocalizarArquivosNaoUtilizados {
	
	public static void main(String [] args) throws Exception {
		Path path = Paths.get(args[0]);
		FileUtils.setRelativePath(path);
		varrerArquivos(Files.newDirectoryStream(path), path);
		printUnusedFile();
	}
	
	private static void printUnusedFile() {
		for(Path path : FileUtils.getUnusedFiles()) {
			System.out.println(path.toAbsolutePath().toString());
		}
	}
	
	private static void varrerArquivos(DirectoryStream<Path> directoryStream, Path path) throws IOException {
		for(Path directory : directoryStream) {
			if(Files.isDirectory(directory)) {
				varrerArquivos(Files.newDirectoryStream(directory), path);
			} else {
				Files.walkFileTree(path, new DefaultFileVisitor(directory));
			}
		}
	}
}