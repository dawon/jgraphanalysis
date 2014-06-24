package cz.dawon.java.library.parsers;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cz.dawon.java.library.Action;

/**
 * Parser for XML documents. Typed to String ID and String Data
 * @author Jakub Zacek
 * @version 1.3.1
 */
public class XMLParser implements IFileParser<String, String> {

	/**
	 * Used to exactly specify Node or it's data in XML document
	 * @author Jakub Zacek
	 * @version 1.4.1
	 */
	public static class NodeSelector {

		/**
		 * Delimiter character for string representation of {@link NodeSelector}
		 */
		public static final char DELIMITER = '/';

		/**
		 * Name of the node or null to use actual Node.
		 */
		public String nodeName = null;

		/**
		 * Attribute name of desired value. Null when inner value of element is desired
		 */
		public String attributeName = null;

		/**
		 * Dot separated list of parent elements, or null when there are not parent Nodes
		 */
		public String parents = null;

		/**
		 * constructor method
		 */
		public NodeSelector() {
		}

		/**
		 * constructor - creates {@link NodeSelector} from String (ie. node/attribute/parent1.parent2)
		 * @param in source string
		 * @throws ParseException when string is not in valid format
		 */
		public NodeSelector(String in) throws ParseException {
			String[] data = in.trim().split("\\"+DELIMITER, -1);
			if (data.length != 3) {
				throw new ParseException("Unable to parse NodeSelector string ("+in+")!", 0);
			}
			if (data[0] != null && !data[0].isEmpty()) {
				nodeName = new String(data[0].trim());
			}
			if (data[1] != null && !data[1].isEmpty()) {
				attributeName = new String(data[1].trim());
			}
			if (data[2] != null && !data[2].isEmpty()) {
				parents = new String(data[2].trim());
			}			
		}		

		/**
		 * constructor method
		 * @param elementName element name
		 * @param attributeName attribute name
		 * @param parents dot separated list of parent node names
		 */
		public NodeSelector(String elementName, String attributeName, String parents) {
			this.nodeName = elementName;
			this.attributeName = attributeName;
			this.parents = parents;
		}

		@Override
		public String toString() {
			String res = "";
			if (nodeName != null) {
				res += nodeName.trim();
			}
			res += DELIMITER;
			if (attributeName != null) {
				res += attributeName.trim();
			}
			res += DELIMITER;
			if (parents != null) {
				res += parents.trim();
			}
			return res;
		}

	}

	/**
	 * Action selector
	 */
	private NodeSelector action;

	/**
	 * Action ID selector
	 */
	private NodeSelector actionId;
	/**
	 * Action Data selector
	 */
	private NodeSelector actionData;

	/**
	 * Action prerequisities selector
	 */
	private NodeSelector prerequisity;
	/**
	 * Action tight prerequisities selector
	 */
	private NodeSelector tightPrerequisity;
	/**
	 * Action followers selector
	 */
	private NodeSelector follower;
	/**
	 * Action tight followers selector
	 */
	private NodeSelector tightFollower;


	/**
	 * Join array elements into one string using specified separator
	 * @param separator separator used to join array
	 * @param data array to be joined
	 * @return result string
	 */
	public static String implode(String separator, String... data) {
		if (data.length == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < data.length - 1; i++) {
			//data.length - 1 => to not add separator at the end
			if (!data[i].matches(" *")) {//empty string are ""; " "; "  "; and so on
				sb.append(data[i]);
				sb.append(separator);
			}
		}
		sb.append(data[data.length - 1]);
		return sb.toString();
	}	


	/**
	 * Loads a Parses XML file using SAX
	 * @param fileName name of the file to be parsed
	 * @return Document containing parsed XML file
	 * @throws IOException occurs when there is any problem while reading the file
	 * @throws ParseException occurs when there is any problem while parsing the file
	 */
	public static Document loadXMLFile(String fileName) throws IOException, ParseException {
		Document doc = null;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setValidating(false);
			DocumentBuilder builder = dbf.newDocumentBuilder();
			builder.setErrorHandler(new XMLParserErrorHandler());
			doc = builder.parse(new ByteArrayInputStream(Files.readAllBytes(Paths.get(fileName))));
		} catch (ParserConfigurationException e) {
			throw new ParseException(e.getMessage(), 0);
		} catch (SAXException e) {
			throw new ParseException(e.getMessage(), 0);
		}	
		return doc;
	}

	/**
	 * Recursively finds specific node in NodeList using NodeSelector
	 * @param nodes source NodeList
	 * @param sel Node Selector
	 * @param skip How many times skip desired node. When less than 1, first occurance is taken. 1 - second occurance is taken. 2 third etc.
	 * @return found Node or null if there is not any other occurance.
	 */
	private static Node findNode(NodeList nodes, NodeSelector sel, int[] skip) {
		if (sel == null) {
			return null;
		}
		int i = 0;	
		Node n;
		if (sel.parents == null || sel.parents.trim().isEmpty()) {
			while ((n = nodes.item(i)) != null) {
				if (n.getNodeName().equals(sel.nodeName)) {
					if (skip[0] <= 0) {
						return n;
					} else {
						skip[0]--;
					}
				}
				i++;
			}
			return null;
		}

		String[] path = sel.parents.split("\\.");
		String parents = null;
		if (path.length >= 1) {
			parents = implode(".", Arrays.copyOfRange(path, 1, path.length));
		}

		i = 0;
		Node n1 = null;
		while ((n = nodes.item(i)) != null) {
			if (n.getNodeName().equals(path[0])) {
				n1 = findNode(n.getChildNodes(), new NodeSelector(sel.nodeName, sel.attributeName, parents), skip);
				if (n1 != null) {
					break;
				}
			}
			i++;
		}					
		return n1;	
	}

	/**
	 * Recursively finds specific node in NodeList using NodeSelector
	 * @param nodes source NodeList
	 * @param sel Node Selector
	 * @param skip How many times skip desired node. When less than 1, first occurance is taken. 1 - second occurance is taken. 2 third etc.
	 * @return found Node or null if there is not any other occurance.
	 */	
	public static Node findNode(NodeList nodes, NodeSelector sel, int skip) {
		return findNode(nodes, sel, new int[] {skip});
	}

	/**
	 * Recursively finds specific node data in Node (and subNodes) using NodeSelector
	 * @param node source Node
	 * @param sel Node Selector
	 * @param skip How many times skip desired node data. When less than 1, first occurance is taken. 1 - second occurance is taken. 2 third etc.
	 * @return Node data
	 */
	public static String getNodeData(Node node, NodeSelector sel, int skip) {
		if (sel == null) {
			return null;
		}

		if (sel.nodeName == null) {
			if (sel.attributeName == null) {
				return node.getTextContent();
			} else {
				return node.getAttributes().getNamedItem(sel.attributeName).getNodeValue();
			}			
		}

		Node n;

		if (sel.nodeName != null && sel.attributeName != null && sel.nodeName.equals(node.getNodeName())) {
			n = node;
		} else {
			n = findNode(node.getChildNodes(), sel, skip);
		}


		if (n == null) {
			return null;
		}

		if (sel.attributeName == null) {
			return n.getTextContent();
		} else {
			if (n.getAttributes() != null && n.getAttributes().getNamedItem(sel.attributeName) != null) { 
				return n.getAttributes().getNamedItem(sel.attributeName).getNodeValue();
			} else {
				return null;
			}
		}
	}

	/**
	 * Parses Action from Node
	 * @param action Action to be parsed
	 * @return parsed Action
	 * @throws ParseException When there is not set Action ID
	 */
	private Action<String, String> parseAction(Node action) throws ParseException {
		String id = getNodeData(action, this.actionId, 0);
		if (id == null) {
			throw new ParseException("Unable to find ID of Action!", 0);
		}
		Action<String, String> a = new Action<String, String>(id);

		String data = getNodeData(action, this.actionData, 0);
		a.setData(data);

		int skip = 0;
		String val;
		while ((val = getNodeData(action, this.prerequisity, skip)) != null) {
			a.addPrerequisity(val);
			skip++;
		}

		skip = 0;
		while ((val = getNodeData(action, this.tightPrerequisity, skip)) != null) {
			a.addTightPrerequisity(val);
			skip++;
		}		

		skip = 0;
		while ((val = getNodeData(action, this.follower, skip)) != null) {
			a.addFollower(val);
			skip++;
		}

		skip = 0;
		while ((val = getNodeData(action, this.tightFollower, skip)) != null) {
			a.addTightFollower(val);
			skip++;
		}		

		return a;
	}

	@Override
	public Set<Action<String, String>> parse(String fileName) throws IOException, ParseException {
		Document doc = loadXMLFile(fileName);

		Set<Action<String, String>> actions = new HashSet<Action<String,String>>();

		int skip = 0;
		Node n;
		while ((n = findNode(doc.getChildNodes(), this.action, skip)) != null) {
			actions.add(parseAction(n));
			skip++;
		}

		return actions;
	}


	/**
	 * Reads line and creates {@link NodeSelector} instance
	 * @param str line to be parsed
	 * @param mandatory when true throws {@link ParseException} when is not set up
	 * @return {@link NodeSelector}
	 * @throws ParseException when problem with parsing
	 */
	private NodeSelector importLine(String str, boolean mandatory) throws ParseException {
		if (str.trim().equals("-")) {
			if (mandatory) {
				throw new ParseException("This NodeSelector is mandatory!", 0);
			}
			return null;
		}
		return new NodeSelector(str);
	}

	/**
	 * Imports parser settings from the file
	 * @param path file to be imported from
	 * @throws ParseException problems with parsing
	 * @throws FileNotFoundException file was not found
	 */
	public void importFromFile(String path) throws ParseException, FileNotFoundException {
		if (path == null) {
			throw new IllegalArgumentException("Path must not be null!");
		}
		File f = new File(path);
		Scanner sc = null;
		try {
			sc = new Scanner(new FileReader(f));

			this.setActionSelector(importLine(sc.nextLine(), true));
			this.setActionIdSelector(importLine(sc.nextLine(), true));
			this.setActionDataSelector(importLine(sc.nextLine(), false));
			this.setPrerequisitySelector(importLine(sc.nextLine(), false));
			this.setTightPrerequisitySelector(importLine(sc.nextLine(), false));
			this.setFollowerSelector(importLine(sc.nextLine(), false));
			this.setTightFollowerSelector(importLine(sc.nextLine(), false));

		} catch (NoSuchElementException e) {
			throw new ParseException(e.getMessage(), 0);
		} finally {
			if (sc != null) {
				sc.close();
			}
		}
	}	
	
	
	/**
	 * Creates string from {@link NodeSelector}
	 * @param ns {@link NodeSelector}
	 * @return output string
	 */
	private String exportLine(NodeSelector ns) {
		if (ns == null) {
			return "-";
		}
		return ns.toString();
	}
	
	/**
	 * Exports parser settings to the file
	 * @param path file path
	 * @throws IOException on write problems 
	 */
	public void exportToFile(String path) throws IOException {
		if (path == null) {
			throw new IllegalArgumentException("Path must not be null!");
		}
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(path));
			
			out.write(exportLine(this.getActionSelector())); out.newLine();
			out.write(exportLine(this.getActionIdSelector())); out.newLine();
			out.write(exportLine(this.getActionDataSelector())); out.newLine();
			out.write(exportLine(this.getPrerequisitySelector())); out.newLine();
			out.write(exportLine(this.getTightPrerequisitySelector())); out.newLine();
			out.write(exportLine(this.getFollowerSelector())); out.newLine();
			out.write(exportLine(this.getTightFollowerSelector())); out.newLine();
		
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}		


	/**
	 * Gets Action Selector
	 * @return Action Selector
	 */
	public NodeSelector getActionSelector() {
		return action;
	}


	/**
	 * Sets Action Selector
	 * @param action Action Selector
	 */
	public void setActionSelector(NodeSelector action) {
		this.action = action;
	}


	/**
	 * Gets Action ID Selector
	 * @return Action ID Selector
	 */
	public NodeSelector getActionIdSelector() {
		return actionId;
	}


	/**
	 * Sets Action ID Selector
	 * @param actionId Action ID Selector
	 */
	public void setActionIdSelector(NodeSelector actionId) {
		this.actionId = actionId;
	}


	/**
	 * Gets Action Data Selector
	 * @return Action Data Selector
	 */
	public NodeSelector getActionDataSelector() {
		return actionData;
	}


	/**
	 * Sets Action Data Selector
	 * @param actionData Action Data Selector
	 */
	public void setActionDataSelector(NodeSelector actionData) {
		this.actionData = actionData;
	}


	/**
	 * Gets Action Prerequisity Selector
	 * @return Action Prerequisity Selector
	 */
	public NodeSelector getPrerequisitySelector() {
		return prerequisity;
	}


	/**
	 * Sets Action Prerequisity Selector
	 * @param prerequisity Action Prerequisity Selector
	 */
	public void setPrerequisitySelector(NodeSelector prerequisity) {
		this.prerequisity = prerequisity;
	}

	/**
	 * Gets Action Tight Prerequisity Selector
	 * @return Action Tight Prerequisity Selector
	 */
	public NodeSelector getTightPrerequisitySelector() {
		return tightPrerequisity;
	}


	/**
	 * Sets Action Tight Prerequisity Selector
	 * @param tightPrerequisity Action Tight Prerequisity Selector
	 */
	public void setTightPrerequisitySelector(NodeSelector tightPrerequisity) {
		this.tightPrerequisity = tightPrerequisity;
	}


	/**
	 * Gets Action Follower Selector
	 * @return Action Follower Selector
	 */
	public NodeSelector getFollowerSelector() {
		return follower;
	}


	/**
	 * Sets Action Follower Selector
	 * @param follower Action Follower Selector
	 */
	public void setFollowerSelector(NodeSelector follower) {
		this.follower = follower;
	}


	/**
	 * Gets Action Tight Follower Selector
	 * @return Action Tight Follower Selector
	 */
	public NodeSelector getTightFollowerSelector() {
		return tightFollower;
	}


	/**
	 * Sets Action Tight Follower Selector
	 * @param tightFollower Action Tight Follower Selector
	 */
	public void setTightFollowerSelector(NodeSelector tightFollower) {
		this.tightFollower = tightFollower;
	}



}
