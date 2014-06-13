package cz.dawon.java.library.parsers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
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
 * @version 1.1
 */
public class XMLParser implements IFileParser<String, String> {

	/**
	 * Used to exactly specify Node or it's data in XML document
	 * @author Jakub Zacek
	 * @version 1.2
	 */
	public static class NodeSelector {
		/**
		 * Name of the node or null to use actual Node.
		 */
		public String nodeName;

		/**
		 * Attribute name of desired value. Null when inner value of element is desired
		 */
		public String attributeName = null;

		/**
		 * Dot separated list of parent elements, or null when there are not parent Nodes
		 */
		public String parents;

		/**
		 * constructor method
		 */
		public NodeSelector() {

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
			return parents + "." + nodeName + " ["+attributeName+"]";
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
			throw new ParseException("", 0);
		} catch (SAXException e) {
			throw new ParseException("", 0);
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
	 * @return
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
			return n.getAttributes().getNamedItem(sel.attributeName).getNodeValue();
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
