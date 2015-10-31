package br.com.filescanner.provider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import br.com.filescanner.utils.FileUtils;
import br.com.filescanner.utils.StringUtils;

public class XSLFileTypeProvider extends FileTypeProvider implements Iterable<Path> {
	
	private Path path = null;
	private Set<Path> fileEntries = new HashSet<Path>();
	
	public XSLFileTypeProvider(Path path) {
		this.path = path;
		parseFile();
	}
	
	public XSLFileTypeProvider(String path) {
		this(Paths.get(path));
	}
	
	public void parseFile() {
		try {
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			builder = builderFactory.newDocumentBuilder();
			Document document = builder.parse(path.toFile());
			document.getDocumentElement().normalize();
			lookForFileEntries(document.getChildNodes());
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void lookForFileEntries(NodeList nodeList) {
		for(int count = 0; count < nodeList.getLength(); count++) {
			Node item = nodeList.item(count);
			String nodeValue = StringUtils.trim(item.getNodeValue());
			if(isSupportedFile(nodeValue)) {
				this.fileEntries.add(FileUtils.resolve(this.path, nodeValue));
			} else {
				if(item.hasAttributes()) {
					NamedNodeMap attributes = item.getAttributes(); 
					for(int countAttrs = 0; countAttrs < attributes.getLength(); countAttrs++) {
						Node attr = attributes.item(countAttrs);
						String attrNodeValue = StringUtils.trim(attr.getNodeValue());
						if(isSupportedFile(attr.getNodeValue())) {
							this.fileEntries.add(FileUtils.resolve(path, attrNodeValue));
						}
					}
				}
			}
			if(item.hasChildNodes()) {
				lookForFileEntries(item.getChildNodes());
			}
		}
	}
	
	private boolean isSupportedFile(String fileName) {
		return fileName != null && (fileName.endsWith(".xsl") || fileName.endsWith(".js") || fileName.endsWith(".css"));
	}

	@Override
	public Iterator<Path> iterator() {
		return this.fileEntries.iterator();
	}

	@Override
	public boolean hasReferenceTo(Path currentFile) {
		Iterator<Path> iterator = this.iterator();
		while(iterator.hasNext()) {
			Path next = iterator.next();
			try {
				if(Files.exists(next)) {
					return Files.isSameFile(next, currentFile);
				} else {
					FileUtils.addInexistedFile(null, next);
					return false;
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return false;
	}

	@Override
	public String[] acceptFile() {
		return new String[]{"xml","xslt","xsl"};
		
	}
}