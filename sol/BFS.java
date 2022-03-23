package sol;

import src.IBFS;
import src.IGraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BFS<V, E> implements IBFS<V, E> {

    /**
     * Returns the path from start to end.
     *
     * @param graph - the graph including the vertices
     * @param start - the start vertex
     * @param end - the end vertex
     * @return - a list of edges starting from the start to the end
     */
    @Override
    public List<E> getPath(IGraph<V, E> graph, V start, V end) {
        if (start == null || end == null) {
            return new LinkedList<>();
        }

        Map<E, V> pathMap = this.getPathHelper(graph, start, end);
        return this.getEdgePath(graph, pathMap, start, end);
    }

    /**
     * getPath() helper method that returns a path HashMap<E, V> which will be used as an argument for
     * getEdgePath() to find the most direct path from the start to the end.
     * @param graph - the graph including the vertices
     * @param start - the start vertex
     * @param end - the end vertex
     * @return - the path HashMap with edges and vertices as the key and values respectivelly
     */
    public Map<E, V> getPathHelper(IGraph<V, E> graph, V start, V end) {
        Set<E> checked = new HashSet<>();
        Map<E, V> path = new HashMap<>();
        LinkedList<E> toCheck = new LinkedList<>(graph.getOutgoingEdges(start));

        while (!toCheck.isEmpty()) {
            //element is popped from top of stack
            E e = toCheck.pop();
            V target = graph.getEdgeTarget(e);
            //if we've already checked e, continue looping
            if (checked.contains(e)) {
                continue;
            }
            //add element to checked HashSet
            checked.add(e);

            //if path doesn't contain target, add edge and target to path
            if (!path.containsValue(target)) {
                path.put(e, target);
            }
            //if target equals the end, we've reached the destination so return path
            if (target.equals(end)) {
                return path;
            }
            //add all edges of target vertex to toCheck
            else {
                toCheck.addAll(graph.getOutgoingEdges(target));
            }
        }
        return new HashMap<>();
    }

    /**
     * helper method that retrieves most direct path from the HashMap from getPathHelper()
     * @param graph - the graph including the vertices
     * @param pathMap - the HashMap where the edge path is retrieved from
     * @param start - the start vertex
     * @param end - the end vertex
     * @return - the most direct path from the start to the end
     */
    public List<E> getEdgePath(IGraph<V, E> graph, Map<E, V> pathMap, V start, V end) {
        LinkedList<E> edgeList = new LinkedList<>();
        //if there is no connection, return an empty list
        if (pathMap.isEmpty()) {
            return new LinkedList<>();
        }
        //while loop that loops until start = end since we traverse from the end to the start
        while (!start.equals(end)) {
            //loop through each edge key in pathMap
            for (E edge : pathMap.keySet()) {
                //if target of edge equals end, addFirst edge to edgeList and make the new end the source of that edge
                if (graph.getEdgeTarget(edge).equals(end)) {
                    edgeList.addFirst(edge);
                    end = graph.getEdgeSource(edge);
                }
            }
        }
        return edgeList;
    }
}
