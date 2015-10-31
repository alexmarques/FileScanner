package br.com.filescanner.factory;

import java.nio.file.Path;
import java.nio.file.Paths;

import br.com.filescanner.provider.FileTypeProvider;
import br.com.filescanner.provider.XSLFileTypeProvider;

public class FileTypeFactory {
	
	private static FileTypeFactory INSTANCE;
	
	private FileTypeFactory(){}
	
    public static FileTypeFactory getInstance() {
    	if(INSTANCE == null) {
    		INSTANCE = new FileTypeFactory();
    	}
    	return INSTANCE;
    }
    
    public FileTypeProvider getFileTypeProvider(String file) {
    	return getFileTypeProvider(Paths.get(file));
    }
    
    public FileTypeProvider getFileTypeProvider(Path file) {
    	String name = file.toString();
    	if(name.endsWith(".xsl") || name.endsWith(".xslt") || name.endsWith(".xml")) {
    		return new XSLFileTypeProvider(file);
    	}
    	return null;
    }
}