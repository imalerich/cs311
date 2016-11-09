
package cs311.hw7.graphalgorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cs311.hw7.graph.IGraph;
import cs311.hw7.graph.IGraph.Vertex;


public class GraphAlgorithms {

    /* ----------------------------------------------------------------------------
     * Topological Sort(...)
     * ---------------------------------------------------------------------------- */
	
	/**
	 * Potential statuses a vertex may be in during a Topological Sort.
	 * @author Ian Malerich
	 */
	private enum Status {
		VISITED, TEMPORARY, UNVISITED
	}

	/**
	 * Perform a topological sort on the given graph. If the input graph is not a DAG
	 * null will be returned, else a sorted array of vertices will be returned to the
	 * caller.
	 * @param g Graph whose vertices we wish to sort.
	 * @return Sorted list of vertices.
	 */
    public static <V, E> List<Vertex<V>> TopologicalSort(IGraph<V, E> g) {
    	ArrayList<Vertex<V>> l = new ArrayList<Vertex<V>>();
    	
    	// Create the HashMap representing the marks of our vertices.
    	HashMap<String, Status> status = new HashMap<String, Status>();
    	for (Vertex<V> v : g.getVertices()) {
    		// All vertices are initially 'UNVISITED'.
    		status.put(v.getVertexName(), Status.UNVISITED);
    	}
    	
    	// n will be our current unmarked node.
    	for (Vertex<V> n = selectUnmarked(status, g); n != null; n = selectUnmarked(status, g)) {
    		try {
				visitVertex(n, status, g, l);
    		} catch (RuntimeException e) {
    			// Graph is not a DAG, return null.
    			return null;
    		}
    	}

        return l;
    }
    
    /**
     * Process a vertex in our depth first search based algorithm (recursive implementation).
     * @param n The current node to parse.
     * @param s List of current statuses for all nodes in the input graph.
     * @param g Graph containing all vertices to parse.
     * @param l List subset to the topologically sorted list of vertices.
     */
    private static <V, E> void visitVertex(Vertex<V> n, HashMap<String, Status> s, IGraph<V, E> g, List<Vertex<V>> l) {
		if (s.get(n.getVertexName()) == Status.TEMPORARY) {
			// The graph is not a DAG, return null for failure.
			throw new RuntimeException();
		}
		
		if (s.get(n.getVertexName()) == Status.UNVISITED) {
			s.put(n.getVertexName(), Status.TEMPORARY);
			for (Vertex<V> m : g.getNeighbors(n.getVertexName())) {
				visitVertex(m, s, g, l);
			}
			s.put(n.getVertexName(), Status.VISITED);
			l.add(0, n);
		}
    }
    
    /**
     * Select an unmarked node from the given graph.
     * @param v List of current status for all vertices in the input graph.
     * @param g Graph of vertices to pick from.
     * @return An unvisited vertex, null if no such vertices exist.
     */
    private static <V, E> Vertex<V> selectUnmarked(HashMap<String, Status> v, IGraph<V, E> g) {
    	for (String key : v.keySet()) {
    		// If the vertex denoted by 'key' has not been visited, return it.
    		if (v.get(key) == Status.UNVISITED) {
    			return g.getVertex(key);
    		}
    	}

    	// All nodes have been visited return false.
    	return null;
    }
    
    /* ----------------------------------------------------------------------------
     * All Topological Sort(...)
     * ---------------------------------------------------------------------------- */
    
    public static <V, E> List<List<Vertex<V>>> AllTopologicalSort(IGraph<V, E> g) {
        return null;
    }
    
    /* ----------------------------------------------------------------------------
     * Kruscal(...)
     * ---------------------------------------------------------------------------- */
    
    public static <V, E extends IWeight> IGraph<V, E> Kruscal(IGraph<V, E> g) {
        return null;
    }
}
