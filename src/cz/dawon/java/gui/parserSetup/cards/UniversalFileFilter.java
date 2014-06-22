package cz.dawon.java.gui.parserSetup.cards;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.swing.filechooser.FileFilter;

/**
 * Universal file filter
 * @author Jakub Zacek
 * @version 1.0
 */
public class UniversalFileFilter extends FileFilter {

	/**
	 * description
	 */
	private String description;
	/**
	 * acceptedExtensions {@link Set}
	 */
	private Set<String> acceptedExtensions = new HashSet<String>();
	/**
	 * should it accept folders?
	 */
	private boolean acceptFolders = true;
	
	/**
	 * should it accept all files?
	 */
	private boolean acceptAllFiles = false;
	
	/**
	 * constructor
	 * @param description description
	 * @param acceptFolders should it accept folders?
	 */
	public UniversalFileFilter(String description, boolean acceptFolders) {
		this.description = description;
		this.acceptFolders = acceptFolders;
	}
	
	/**
	 * Gets extension of a file
	 * @param f file
	 * @return extension of the file
	 */
	public static String getExtension(File f) {
	    String extension = null;
	    String fileName = f.getName();
	    int i = fileName.lastIndexOf('.');

	    if (i > 0 &&  i < fileName.length() - 1) {
	        extension = fileName.substring(i+1).toLowerCase();
	    }
	    return extension;
	}	
	
	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			if (acceptFolders) {
				return true;
			}
			return false;
		}
		if (acceptAllFiles) {
			return true;
		}
		String ext = getExtension(f);
		if (ext == null) {
			return false;
		}
		return acceptedExtensions.contains(ext.toLowerCase());
	}
	
	/**
	 * Adds new accepted extension
	 * @param extension extension
	 * @return {@link UniversalFileFilter} for chaining
	 */
	public UniversalFileFilter addExtension(String extension) {
		acceptedExtensions.add(extension.toLowerCase());
		return this;
	}
	
	/**
	 * Sets whether to accept all files
	 * @param allow allow all files?
	 * @return {@link UniversalFileFilter} for chaining
	 */
	public UniversalFileFilter setAllowAllFiles(boolean allow) {
		this.acceptAllFiles = allow;
		return this;
	}

	@Override
	public String getDescription() {
		return description;
	}

}
