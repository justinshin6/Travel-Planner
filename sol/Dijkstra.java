package sol;

import src.IDijkstra;
import src.IGraph;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.Function;

public class Dijkstra<V, E> implements IDijkstra<V, E> {

    /**
     * Finds the lowest cost path from source to destination.
     *
     * @param graph - the graph including the vertices
     * @param source - the source vertex
     * @param destination - the destination vertex
     * @param edgeWeight - Function that determines which weight the algorithm should focus on
     * @return - a list of edges from source to destination
     */
    @Override
    public List<E> getShortestPath(IGraph<V, E> graph, V source, V destination,
                                   Function<E, Double> edgeWeight) {
        double defaultValue = Double.POSITIVE_INFINITY;
        HashMap<V, Double> distances = new HashMap<>();
        HashMap<E, V> parents = new HashMap<>();
        Comparator<V> weight = (vertex1, vertex2) -> {
            return Double.compare(distances.get(vertex1), distances.get(vertex2));
        };
        PriorityQueue<V> toCheck = new PriorityQueue<V>(weight);

        if (graph.getVertices().isEmpty()) {
            return new LinkedList<>();
        }
        //initializes source dest to 0 and everything else to inf
        for (V vertex : graph.getVertices()) {
            if (vertex.equals(source)) {
                distances.put(vertex, 0.0);
            } else {
                distances.put(vertex, defaultValue);
            }
        }

        //add all distance vertex keys to toCheck PriorityQueue
        toCheck.addAll(distances.keySet());

        while (!toCheck.isEmpty()) {
            V curr = toCheck.poll(); // vertex with first priority, deletes from PQ
            for (E edge : graph.getOutgoingEdges(curr)) {
                V edgeTarget = graph.getEdgeTarget(edge);
                //existing distance between curr and edgeTarget
                Double existingDistance = distances.get(edgeTarget);
                //combine the distance of the curr vertex plus the edgeWeight between curr and edgeTarget
                Double newDistance = distances.get(curr) + edgeWeight.apply(edge);

                //if the existing distance is greater than the newDistance, replace the distance with newDistance
                if (existingDistance > newDistance) {
                    distances.replace(edgeTarget, newDistance);
                    //add curr to toCheck and add edge, edgeTarget to parents
                    toCheck.add(curr);
                    parents.put(edge, edgeTarget);
                }
            }
        }
        return this.getShortestPathHelper(graph, parents, source, destination, edgeWeight);
    }

    /**
     * helper method that retrieves the lowest cost path from parents HashMap.
     * @param graph - the graph including the vertices
     * @param parents - HashMap that lowest cost path is retrieved from
     * @param source - the source vertex
     * @param destination - the destination vertex
     * @param edgeWeight - Function that determines which weight the algorithm should focus on
     * @return - the lowest cost path from source to destination
     */
    private List<E> getShortestPathHelper(IGraph<V, E> graph, HashMap<E, V> parents, V source, V destination,
                                          Function<E, Double> edgeWeight) {
        //if there is no connection, return empty list
        if (parents.isEmpty()) {
            return new LinkedList<>();
        }
        LinkedList<E> edgeList = new LinkedList<>();
        Comparator<E> edgeWeightComparator = (edge1, edge2) -> {
            return Double.compare(edgeWeight.apply(edge1), edgeWeight.apply(edge2));
        };

        try {
            PriorityQueue<E> edgeQueue = new PriorityQueue<E>(edgeWeightComparator);
            //while loop that loops until destination equals source
            while (!destination.equals(source)) {
                //for each key edge in parents, check if edgeTarget equals destination
                for (E edge : parents.keySet()) {
                    //if true, add to edgeQueue which orders based off lowest edgeWeight
                    if (graph.getEdgeTarget(edge).equals(destination)) {
                        edgeQueue.add(edge);
                    }
                }
                //retrieve edge with the lowest edgeWeight and addFirst to edgeList
                E edge = edgeQueue.poll();
                edgeList.addFirst(edge);
                //make destination the source of the edge with the lowest edgeWeight
                destination = graph.getEdgeSource(edge);
            }
            return edgeList;
        } catch (NullPointerException e) {
            //if null exception is found, something doesn't exist so return no path
            return new LinkedList<>();
        }
    }
}

