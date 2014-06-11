package cz.dawon.java.library.parsers;

import java.io.IOException;
import java.text.ParseException;
import java.util.Set;

import cz.dawon.java.library.Action;

/**
 * Interface, that describes universal File Parser
 * @author Jakub Zacek
 * @version 1.0
 *
 * @param <I> datatype for Action's identifier
 * @param <D> datatype for Action's data
 */
public interface IFileParser<I, D> {
	
	/**
	 * Parses a single file and returns set of parsed Actions
	 * @param fileName name of the file to parse
	 * @return set of parsed Actions
	 * @throws IOException thrown when unable to load the file
	 * @throws ParseException thrown when problem occurs during parsing file
	 */
	public Set<Action<I, D>> parse(String fileName) throws IOException, ParseException;
	
}
