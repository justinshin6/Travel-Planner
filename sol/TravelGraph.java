package sol;

import src.City;
import src.IGraph;
import src.Transport;

import java.util.HashMap;
import java.util.Set;
import java.util.Map;


public class TravelGraph implements IGraph<City, Transport> {

    private Map<City, Set<Transport>> toCities;

    public TravelGraph() {
        this.toCities = new HashMap<>();
    }

    /**
     * Adds a vertex to the graph.
     *
     * @param vertex - the vertex
     */
    @Override
    public void addVertex(City vertex) {
        this.toCities.put(vertex, vertex.getOutgoing());
    }

    /**
     * Adds an edge to the graph.
     *
     * @param origin - the origin of the edge.
     * @param edge - edge to be added
     */
    @Override
    public void addEdge(City origin, Transport edge) {
        origin.addOut(edge);
    }

    /**
     * Gets a set of vertices in the graph.
     *
     * @return - the set of vertices
     */
    @Override
    public Set<City> getVertices() {
        return this.toCities.keySet();
    }

    /**
     * Gets the source of an edge.
     *
     * @param edge - the edge
     * @return - the source of the edge
     */
    @Override
    public City getEdgeSource(Transport edge) {
        return edge.getSource();
    }

    /**
     * Gets the target of an edge.
     *
     * @param edge - the edge
     * @return - the target of the edge
     */
    @Override
    public City getEdgeTarget(Transport edge) {
        return edge.getTarget();
    }

    /**
     * Gets the outgoing edges of a vertex.
     *
     * @param fromVertex - the vertex
     * @return - the outgoing edges from that vertex
     */
    @Override
    public Set<Transport> getOutgoingEdges(City fromVertex) {
        return fromVertex.getOutgoing();
    }

    /**
     * method that returns the city with a specific name
     * @param name - the name of the city that we want to return
     * @return - the city with the name parameter, null if city not found
     */
    public City getCityByName(String name) {
        Set<City> cities = this.getVertices();
        for (City city : cities) {
            if (city != null && city.toString().equals(name)) {
                return city;
            }
        }
        return null;
    }
}