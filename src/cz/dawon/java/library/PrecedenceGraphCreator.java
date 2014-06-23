package cz.dawon.java.library;

import java.security.InvalidAlgorithmParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import cz.dawon.java.library.graphConnectors.IGraphConnector;

/**
 * Creates graph (its edges) from Actions
 * @author Jakub Zacek
 * @version 1.3
 *
 * @param <I> datatype for Action's identifier
 * @param <D> datatype for Action's data
 */
public class PrecedenceGraphCreator<I, D> {

	/**
	 * Exactly represents {@link Action} Reference
	 * @author Jakub Zacek
	 * @version 1.1
	 */
	public static class ReferenceIdentificator<I> {
		/**
		 * first vertex
		 */		
		private I from;
		/**
		 * second vertex
		 */		
		private I to;
		/**
		 * reference type
		 */
		private int type;
		
		public ReferenceIdentificator(int type, I from, I to) {
			this.type = type;
			this.from = from;
			this.to = to;
		}

		/**
		 * Gets first vertex
		 * @return first vertex
		 */
		public I getFrom() {
			return from;
		}

		/**
		 * Gets second vertex
		 * @return second vertex
		 */		
		public I getTo() {
			return to;
		}

		/**
		 * Gets type of Reference
		 * @return type of reference
		 */
		public int getType() {
			return type;
		}
		
		@Override
		public String toString() {
			return Action.REFERENCES_NAMES[type] + ": " + from.toString() + " -> " + to.toString();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((from == null) ? 0 : from.hashCode());
			result = prime * result + ((to == null) ? 0 : to.hashCode());
			result = prime * result + type;
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			ReferenceIdentificator<I> other = (ReferenceIdentificator<I>) obj;
			if (obj == null || getClass() != obj.getClass()) {
				return false;
			}
			return this.type == other.getType() && this.from.equals(other.getFrom()) && this.to.equals(other.getTo());
		}
		
	}
	
	
	/**
	 * Represents Virtual Edge in graph
	 * @author Jakub Zacek
	 * @version 1.0
	 */
	public class VirtualEdge {
		/**
		 * first vertex
		 */
		private I from;
		/**
		 * second vertex
		 */
		private I to;

		/**
		 * constructor
		 * @param from first vertex
		 * @param to second vertex
		 */
		public VirtualEdge(I from, I to) {
			this.from = from;
			this.to = to;
		}

		/**
		 * Gets first vertex
		 * @return first vertex
		 */
		public I getFrom() {
			return from;
		}

		/**
		 * Gets second vertex
		 * @return second vertex
		 */
		public I getTo() {
			return to;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((from == null) ? 0 : from.hashCode());
			result = prime * result + ((to == null) ? 0 : to.hashCode());
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null || !(obj instanceof PrecedenceGraphCreator.VirtualEdge)) {
				return false;
			}
			VirtualEdge c = (VirtualEdge) obj;
			return c.getFrom().equals(from) && c.getTo().equals(to);
		}

		/**
		 * Gets Outer class
		 * @return outer class
		 */
		private PrecedenceGraphCreator<I, D> getOuterType() {
			return PrecedenceGraphCreator.this;
		}

	}


	/**
	 * Represents all actions accessible from parents of the action (and parents of parents...)
	 * @author Jakub Zacek
	 * @version 1.0
	 */
	public class Accessibles {
		/**
		 * this {@link Action}'s identifier
		 */
		private I thisA = null;
		/**
		 * {@link Set} of parent {@link Action}s
		 */
		private Set<Accessibles> parents = new HashSet<Accessibles>();

		/**
		 * constructor
		 * @param action for which {@link Action}
		 */
		public Accessibles(I action) {
			thisA = action;
		}

		/**
		 * Does add parent
		 * @param a parent {@link Accessibles} instance
		 */
		public void inherit(Accessibles a) {			
			parents.add(a);
		}

		/**
		 * Returns true if {@link Action} with specified id is in some of the parents (or parents of parents...)
		 * @param a {@link Action} ID
		 * @return true if it is accessible
		 */
		public boolean isAccessible(I a) {
			return getAccessibles().contains(a);
		}

		/**
		 * Returns {@link Set} of {@link Action} IDs from parents
		 * @return {@link Set}
		 */
		public Set<I> getAccessibles() {
			Set<I> accessibles = new HashSet<I>();
			accessibles.add(thisA);
			Accessibles ac;
			for (Iterator<Accessibles> iterator = parents.iterator(); iterator.hasNext();) {
				ac = iterator.next();
				accessibles.addAll(ac.getAccessibles());
			}
			return accessibles;
		}

	}

	/**
	 * {@link Map} containing {@link Action}s
	 */
	private Map<I, Action<I, D>> actions;
	/**
	 * {@link Map} containing {@link Action}'s {@link Accessibles}
	 */
	private Map<Action<I, D>, Accessibles> acc = new HashMap<Action<I, D>, Accessibles>();
	/**
	 * {@link Map} containing Prerequisities (with transformed ones from Followers)
	 */
	private Map<I, Set<I>> prerequisities = new HashMap<I, Set<I>>();
	/**
	 * {@link Map} containing Tight Prerequisities (with transformed ones from Tights Followers)
	 */	
	private Map<I, Set<I>> tightPrerequisities = new HashMap<I, Set<I>>();

	/**
	 * {@link Set} containing all edges 
	 */
	private Set<VirtualEdge> edges = new HashSet<VirtualEdge>();


	/**
	 * constructor
	 * @param actions {@link Map} of {@link Accessibles}s
	 */
	public PrecedenceGraphCreator(Map<I, Action<I, D>> actions) {
		this.actions = actions;
		Action<I, D> a;
		for (Iterator<I> iterator = actions.keySet().iterator(); iterator.hasNext();) {
			a = actions.get(iterator.next());
			acc.put(a, new Accessibles(a.getId()));

			prerequisities.put(a.getId(), new HashSet<I>());
			tightPrerequisities.put(a.getId(), new HashSet<I>());	
		}
	}


	/**
	 * Checks whether connections to actions exist
	 * @param actionIds {@link Set} of {@link Action} IDs
	 * @throws NoSuchElementException when connection does not exist
	 */
	private void checkIndexes(Set<I> actionIds) throws NoSuchElementException {
		I id;
		for (Iterator<I> iterator = actionIds.iterator(); iterator.hasNext();) {
			id = iterator.next();
			if (actions.get(id) == null) {
				throw new NoSuchElementException("Can't find Action with ID '"+id.toString()+"'!");
			}
		}
	}

	/**
	 * Transforms Followers to Prerequisities
	 * @param action {@link Action}
	 * @param actionIds {@link Set} of {@link Action} IDs
	 * @param tight is it tight follower or follower?
	 */
	private void followersToPrerequisities(Action<I, D> action, Set<I> actionIds, boolean tight) {
		I id;
		for (Iterator<I> iterator = actionIds.iterator(); iterator.hasNext();) {
			id = iterator.next();
			if (tight) {
				tightPrerequisities.get(id).add(action.getId());
			} else {
				prerequisities.get(id).add(action.getId());
			}
		}
	}

	/**
	 * Connects tight prereqisities int graph
	 * @param action {@link Action}
	 */
	private void connectTights(Action<I, D> action) {
		I id;
		for (Iterator<I> iterator = action.getTightPrerequisities().iterator(); iterator.hasNext();) {
			id = iterator.next();
			edges.add(new VirtualEdge(id, action.getId()));
			acc.get(action).inherit(acc.get(actions.get(id)));
		}		
		for (Iterator<I> iterator = action.getTightFollowers().iterator(); iterator.hasNext();) {
			id = iterator.next();
			edges.add(new VirtualEdge(action.getId(), id));
			acc.get(actions.get(id)).inherit(acc.get(action));
		}			
	}
	
	/**
	 * Creates graph from actions - not result graph, only to find cycles...
	 * @param graph {@link IGraphConnector} instance
	 */
	private void createGraphFromSet(IGraphConnector<I> graph) {
		Action<I, D> a;
		I sc, eid;
		for (Iterator<I> iterator = actions.keySet().iterator(); iterator.hasNext();) {
			a = actions.get(iterator.next());
			graph.addVertex(a.getId());			
		}
		for (Iterator<I> iterator = actions.keySet().iterator(); iterator.hasNext();) {
			a = actions.get(iterator.next());
			for (int i = 0; i < Action.REFERENCES_NAMES.length; i++) {
				for (Iterator<I> iterator2 = a.getRawReferences().get(i).iterator(); iterator2.hasNext();) {
					sc = iterator2.next();
					if (i < 2) {
						eid = graph.createEdgeId(sc, a.getId());
						graph.addEdge(eid, sc, a.getId(), true);
					} else {
						eid = graph.createEdgeId(a.getId(), sc);
						graph.addEdge(eid, a.getId(), sc, true);
					}
					graph.addEdgeData(eid, "type", i);
				}
			}	
		}		
	}

	/**
	 * Finds cycles in Graph recursively
	 * @param graph {@link IGraphConnector} instance
	 * @param edge processed edge
	 * @param vertex processed vertex
	 * @param visitedVertices visited vertices
	 * @param completedVertices completed vertices
	 * @param badReferences found bad references (cycles)
	 */
    private void findCycles(IGraphConnector<I> graph, I edge, I vertex, Set<I> visitedVertices, Set<I> completedVertices, List<ReferenceIdentificator<I>> badReferences) {    	
        if (edge != null && visitedVertices.contains(vertex)) {
            if (completedVertices.contains(vertex)) {
            	return;
            }
            badReferences.add(new ReferenceIdentificator<I>((int)graph.getEdgeData(edge, "type"), graph.getEdgeFrom(edge), graph.getEdgeTo(edge)));
            return;
        }

        visitedVertices.add(vertex);

        for (Iterator<I> iterator = graph.getEdgesFromVertex(vertex).iterator(); iterator.hasNext(); ) {
            I e = iterator.next();
        	findCycles(graph, e, graph.getEdgeTo(e), visitedVertices, completedVertices, badReferences);
        }

        completedVertices.add(vertex);
    }	
    
    /**
     * Finds cycles in Precedence Graph and tries to fix them
     * @param graph {@link IGraphConnector} instance
     * @return {@link List} of problematic references ({@link ReferenceIdentificator})
     * @throws NoSuchElementException when connection to {@link Action} does not exist
     */
    public List<ReferenceIdentificator<I>> findAndFixCycles(IGraphConnector<I> graph, boolean fix) throws NoSuchElementException {
        Set<I> visitedVertices = new HashSet<I>();
        Set<I> completedVertices = new HashSet<I>();
    	List<ReferenceIdentificator<I>> refs = new ArrayList<ReferenceIdentificator<I>>();
    	
    	prepare(false);
    	
    	createGraphFromSet(graph);
    	
    	for (Iterator<I> iterator = actions.keySet().iterator(); iterator.hasNext();) {
			findCycles(graph, null, iterator.next(), visitedVertices, completedVertices, refs);
		}
    	
    	if (fix) {
    		for (Iterator<ReferenceIdentificator<I>> iterator = refs.iterator(); iterator.hasNext();) {
    			ReferenceIdentificator<I> id = iterator.next();
				if (id.getType() < 2) {
					actions.get(id.getTo()).getRawReferences().get(id.getType()).remove(id.getFrom());
				} else {
					actions.get(id.getFrom()).getRawReferences().get(id.getType()).remove(id.getTo());
				}
     		}
    	}
    	return refs;
    }
	
	/**
	 * Creates comma-separated {@link String} from {@link Set}
	 * @param s {@link Set}
	 * @return Comma-separated {@link String}
	 */
	private String getStringFromSet(Set<Action<I, D>> s) {
		StringBuilder result = new StringBuilder();
		for(Action<I, D> a : s) {
			result.append(a.getId().toString());
			result.append(",");
		}
		return result.length() > 0 ? result.substring(0, result.length() - 1): "";
	}

	/**
	 * Processes the data loaded from loaded {@link Action}s
	 * @throws NoSuchElementException when connection to {@link Action} does not exist
	 * @throws InvalidAlgorithmParameterException when solution does not exist
	 */
	public void process() throws NoSuchElementException, InvalidAlgorithmParameterException {
		Map<Action<I, D>, Integer> alg = new HashMap<Action<I, D>, Integer>();
		int depth = 0;
		Set<Action<I, D>> acts = new HashSet<Action<I, D>>(actions.values());
		
		prepare(true);		

		boolean change;
		while (!acts.isEmpty()) {
			change = false;
			a : for (Iterator<Action<I, D>> iterator2 = acts.iterator(); iterator2.hasNext();) {
				Action<I, D> action = iterator2.next();
				if (depth == 0) {
					if (prerequisities.get(action.getId()).isEmpty() && tightPrerequisities.get(action.getId()).isEmpty()) {
						alg.put(action, depth);
						iterator2.remove();
						change = true;
					}
				} else {
					for (Iterator<I> iterator = prerequisities.get(action.getId()).iterator(); iterator.hasNext();) {
						Integer ag = alg.get(actions.get(iterator.next()));
						if (ag == null || ag >= depth) {
							continue a;
						}						
					}				
					for (Iterator<I> iterator = tightPrerequisities.get(action.getId()).iterator(); iterator.hasNext();) {
						Integer ag = alg.get(actions.get(iterator.next()));
						if (ag == null || ag >= depth) {
							continue a;
						}						
					}						
					alg.put(action, depth);
					iterator2.remove();
					change = true;
				}				
			}
			depth++;
			if (!change) {
				throw new InvalidAlgorithmParameterException("Solution does not exist! (cyclic dependencies?) Unable to connect these Actions: " + getStringFromSet(acts));
			}
		}
		connectNormals(alg);

	}


	/**
	 * prepares actions and does simple task before actual process
	 * @throws NoSuchElementException when connection to {@link Action} does not exist
	 */
	private void prepare(boolean full) throws NoSuchElementException {
		Action<I, D> a;
		for (Iterator<I> iterator = actions.keySet().iterator(); iterator.hasNext();) {
			a = actions.get(iterator.next());
			checkIndexes(a.getPrerequisities());
			checkIndexes(a.getTightPrerequisities());
			checkIndexes(a.getFollowers());
			checkIndexes(a.getTightFollowers());

			if (full) {
				followersToPrerequisities(a, a.getFollowers(), false);
				followersToPrerequisities(a, a.getTightFollowers(), true);			
				prerequisities.get(a.getId()).addAll(a.getPrerequisities());
				tightPrerequisities.get(a.getId()).addAll(a.getTightPrerequisities());
			}
		}
		if (!full) {
			return;
		}
		for (Iterator<I> iterator = actions.keySet().iterator(); iterator.hasNext();) {		
			connectTights(actions.get(iterator.next()));		
		}
	}

	/**
	 * Connects all {@link Action}s based on their Prereequisities
	 * @param alg preprocessed data from process() methods ({@link Map} depth - {@link Action})
	 */
	private void connectNormals(Map<Action<I, D>, Integer> alg) {
		Map<Integer, Set<Action<I, D>>> algInv = new HashMap<Integer, Set<Action<I, D>>>();
		for (Iterator<Action<I, D>> iterator = alg.keySet().iterator(); iterator.hasNext();) {
			Action<I, D> action = iterator.next();

			if (algInv.get(alg.get(action)) == null) {
				algInv.put(alg.get(action), new HashSet<Action<I, D>>());
			}
			algInv.get(alg.get(action)).add(action);
		}


		for (int i = 1; i < algInv.keySet().size(); i++) {
			for (Iterator<Action<I, D>> iterator = algInv.get(i).iterator(); iterator.hasNext();) {
				Action<I, D> action = iterator.next();
				for (Iterator<I> iterator1 = prerequisities.get(action.getId()).iterator(); iterator1.hasNext();) {
					I p = iterator1.next();
					if (!acc.get(action).isAccessible(p)) {
						connectNormalsTo(algInv, action, p, i-1);
					}
				}
			}
		}
	}

	/**
	 * Does the connection of Prerequisity {@link Action} itself
	 * @param algInv {@link Map} {@link Action} - {@link Set} of depths
	 * @param action {@link Action} to be connected
	 * @param pre Prerequisity demanded
	 * @param depth depth
	 */
	private void connectNormalsTo(Map<Integer, Set<Action<I, D>>> algInv, Action<I, D> action, I pre, int depth) {
		if (depth < 0) {
			return;
		}
		for (Iterator<Action<I, D>> iterator2 = algInv.get(depth).iterator(); iterator2.hasNext();) {
			Action<I, D> action2 = iterator2.next();
			if (acc.get(action2).isAccessible(pre)) {
				edges.add(new VirtualEdge(action2.getId(), action.getId()));
				acc.get(action).inherit(acc.get(action2));								
				return;
			}
		}
		connectNormalsTo(algInv, action, pre, depth-1);
	}

	/**
	 * Gets {@link Set} of {@link VirtualEdge}s
	 * @return {@link Set} of {@link VirtualEdge}s
	 */
	public Set<VirtualEdge> getEdges() {
		return edges;
	}


}
