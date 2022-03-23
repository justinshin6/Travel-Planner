package sol;

import src.*;

import java.io.IOException;
import java.util.function.Function;
import java.util.Map;

import java.util.List;

public class TravelController implements ITravelController<City, Transport> {

    private TravelGraph graph;

    public TravelController() {
    }

    /**
     * Loads CSVs into the app.
     *
     * @param citiesFile - the filename of the cities csv
     * @param transportFile - the filename of the transportations csv
     * @return - an informative message to be printed in the REPL
     */
    @Override
    public String load(String citiesFile, String transportFile) {
        this.graph = new TravelGraph();
        TravelCSVParser csvParser = new TravelCSVParser();

        Function<Map<String, String>, Void> addVertex = map -> {
            this.graph.addVertex(new City(map.get("name")));
            return null;
        };

        Function<Map<String, String>, Void> buildEdge = map -> {
            this.addEdgeHelper(this.graph, map.get("origin"), map.get("destination"), map.get("type"),
                    map.get("price"), map.get("duration"));
            return null;
        };

        try {
            csvParser.parseLocations(citiesFile, addVertex);
        } catch (IOException e) {
            return "Error trying to parse " + citiesFile;
        }
        try {
            csvParser.parseTransportation(transportFile, buildEdge);
        } catch (IOException e) {
            return "Error trying to parse " + transportFile;
        }
        return "Successfully loaded cities and transportation files.";
    }

    /**
     * Finds the fastest route in between two cities
     *
     * @param source - the name of the source city
     * @param destination - the name of the destination city
     * @return - the path starting from the source to the destination,
     * or empty if there is none
     */
    @Override
    public List<Transport> fastestRoute(String source, String destination) {
        Dijkstra djk = new Dijkstra();
        Function<Transport, Double> edgeWeightCalculation = e -> e.getMinutes();
        Function<String, City> stringToCity = string -> this.graph.getCityByName(string);

        return djk.getShortestPath(this.graph, stringToCity.apply(source), stringToCity.apply(destination),
                edgeWeightCalculation);
    }

    /**
     * Finds the cheapest route in between two cities
     *
     * @param source - the name of the source city
     * @param destination - the name of the destination city
     * @return - the path starting from the source to the destination,
     * or empty if there is none
     */
    @Override
    public List<Transport> cheapestRoute(String source, String destination) {
        Dijkstra djk = new Dijkstra();
        Function<Transport, Double> edgeWeightCalculation = e -> e.getPrice();
        Function<String, City> stringToCity = string -> this.graph.getCityByName(string);

        return djk.getShortestPath(this.graph, stringToCity.apply(source), stringToCity.apply(destination),
                edgeWeightCalculation);
    }

    /**
     * Finds the most direct route in between two cities
     *
     * @param source - the name of the source city
     * @param destination - the name of the destination city
     * @return - the path starting from the source to the destination,
     * or empty if there is none
     */
    @Override
    public List<Transport> mostDirectRoute(String source, String destination) {
        BFS bfs = new BFS();
        Function<String, City> stringToCity = string -> this.graph.getCityByName(string);

        return bfs.getPath(this.graph, stringToCity.apply(source), stringToCity.apply(destination));
    }

    /**
     * helper method that adds an edge to the travel graph
     * @param graph - current travel graph
     * @param origin - origin city
     * @param destination - destination city
     * @param type - type of Transport
     * @param price - price of Transport
     * @param duration - duration of Transport
     */
    private void addEdgeHelper(TravelGraph graph, String origin, String destination, String type,
                               String price, String duration) {
        TransportType transportType = TransportType.fromString(type);
        double mapPrice = Double.parseDouble(price);
        double mapDuration = Double.parseDouble(duration);
        graph.addEdge(graph.getCityByName(origin), new
                Transport(graph.getCityByName(origin),
                graph.getCityByName(destination), transportType, mapPrice, mapDuration));
    }
}
