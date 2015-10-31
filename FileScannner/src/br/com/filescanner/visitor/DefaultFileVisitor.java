package br.com.filescanner.visitor;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import br.com.filescanner.factory.FileTypeFactory;
import br.com.filescanner.provider.FileTypeProvider;
import br.com.filescanner.utils.FileUtils;

public class DefaultFileVisitor implements FileVisitor<Path> {
	
	private Path currentFile;
	
	public DefaultFileVisitor(Path currentFile) {
		this.currentFile = currentFile;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		try {
			FileTypeProvider provider = FileTypeFactory.getInstance().getFileTypeProvider(file);
			if(provider != null && provider.hasReferenceTo(currentFile)) {
				FileUtils.fileFound = true;
			} 
		} catch (RuntimeException e) {
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
		return FileVisitResult.CONTINUE;
	}
}	