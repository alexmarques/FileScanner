package br.com.filescanner.provider;

import java.nio.file.Path;

public abstract class FileTypeProvider implements Iterable<Path> {
	
	public abstract void parseFile();

	public abstract boolean hasReferenceTo(Path currentFile);
	
	public abstract String[] acceptFile();

}
