package test;

import org.junit.Test;
import sol.Dijkstra;
import sol.TravelController;
import src.IDijkstra;
import src.Transport;
import test.simple.SimpleEdge;
import test.simple.SimpleGraph;
import test.simple.SimpleVertex;

import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Your Dijkstra's tests should all go in this class!
 * The test we've given you will pass if you've implemented Dijkstra's correctly, but we still
 * expect you to write more tests using the City and Transport classes.
 * You are welcome to write more tests using the Simple classes, but you will not be graded on
 * those.
 */
public class DijkstraTest {

    private static final double DELTA = 0.001;

    private SimpleGraph graph;
    private SimpleVertex a;
    private SimpleVertex b;
    private SimpleVertex c;
    private SimpleVertex d;
    private SimpleVertex e;

    /**
     * Creates a simple graph.
     * You'll find a similar method in each of the Test files.
     * Normally, we'd like to use @Before, but because each test may require a different setup,
     * we manually call the setup method at the top of the test.
     *
     */

    public void createSimpleGraph() {
        this.graph = new SimpleGraph();

        this.a = new SimpleVertex("a");
        this.b = new SimpleVertex("b");
        this.c = new SimpleVertex("c");
        this.d = new SimpleVertex("d");
        this.e = new SimpleVertex("e");

        this.graph.addVertex(this.a);
        this.graph.addVertex(this.b);
        this.graph.addVertex(this.c);
        this.graph.addVertex(this.d);
        this.graph.addVertex(this.e);

        this.graph.addEdge(this.a, new SimpleEdge(100, this.a, this.b));
        this.graph.addEdge(this.a, new SimpleEdge(3, this.a, this.c));
        this.graph.addEdge(this.a, new SimpleEdge(1, this.a, this.e));
        this.graph.addEdge(this.c, new SimpleEdge(6, this.c, this.b));
        this.graph.addEdge(this.c, new SimpleEdge(2, this.c, this.d));
        this.graph.addEdge(this.d, new SimpleEdge(1, this.d, this.b));
        this.graph.addEdge(this.d, new SimpleEdge(5, this.e, this.d));
    }

    /**
     * Test with SimpleVertices and SimpleEdges
     */
    @Test
    public void testSimple() {
        this.createSimpleGraph();

        IDijkstra<SimpleVertex, SimpleEdge> dijkstra = new Dijkstra<>();
        Function<SimpleEdge, Double> edgeWeightCalculation = e -> e.weight;
        // a -> c -> d -> b
        List<SimpleEdge> path =
            dijkstra.getShortestPath(this.graph, this.a, this.b, edgeWeightCalculation);
        assertEquals(6, SimpleGraph.getTotalEdgeWeight(path), DELTA);
        assertEquals(3, path.size());

        // c -> d -> b
        path = dijkstra.getShortestPath(this.graph, this.c, this.b, edgeWeightCalculation);
        assertEquals(3, SimpleGraph.getTotalEdgeWeight(path), DELTA);
        assertEquals(2, path.size());
    }

    /**
     * Testing the cheapest route in between cities in cities1.csv and cities2.csv
     */
    @Test
    public void testCheapSimple() {
        TravelController tc = new TravelController();
        tc.load("data/cities2.csv", "data/transport2.csv");
        TravelController tc2 = new TravelController();
        tc2.load("data/cities1.csv", "data/transport1.csv");

        List<Transport> path = tc.cheapestRoute("Boston", "Providence");
        assertEquals(3, path.size());

        List<Transport> path2 = tc.cheapestRoute("Boston", "Chicago");
        assertEquals(2, path2.size());

        List<Transport> path3 = tc.cheapestRoute("Boston", "Durham");
        assertEquals(1, path3.size());

        List<Transport> path4 = tc.cheapestRoute("Durham", "Providence");
        assertEquals(2, path4.size());

        List<Transport> path5 = tc2.cheapestRoute("Boston", "Providence");
        assertEquals(1, path5.size());
        assertTrue(path5.toString().contains("Type: bus"));

        List<Transport> path6 = tc2.cheapestRoute("Providence", "Boston");
        assertEquals(1, path6.size());
        assertTrue(path6.toString().contains("Type: bus"));
    }

    /**
     * Testing for the cheapest route in ourCities.csv
     */
    @Test
    public void testCheap() {
        TravelController tc = new TravelController();
        tc.load("data/ourCities.csv", "data/ourTransport.csv");

        List<Transport> path1 = tc.cheapestRoute("Home", "Ratty");
        assertEquals(3, path1.size());

        List<Transport> path2 = tc.cheapestRoute("Home", "Jos");
        assertEquals(4, path2.size());

        List<Transport> path3 = tc.cheapestRoute("Home", "Ivy Room");
        assertEquals(1, path3.size());

        List<Transport> path4 = tc.cheapestRoute("Home", "VW");
        assertEquals(5, path4.size());

        List<Transport> path5 = tc.cheapestRoute("VW", "Jos");
        assertEquals(2, path5.size());

        // tests when there are multiple best paths for cost, will return the most direct
        List<Transport> path6 = tc.cheapestRoute("Home", "Blue Room");
        assertEquals(1, path6.size());

        // when there is a double edge, return the cheaper edge
        List<Transport> path7 = tc.cheapestRoute("Ratty", "Andrews");
        assertEquals(1, path7.size());
        assertTrue(path7.toString().contains("Type: bus"));

        // tests that if source and destination are the same, returns an empty list
        List<Transport> path8 = tc.cheapestRoute("VW", "VW");
        assertEquals(0, path8.size());

        // tests that if destination doesn't exist, returns an empty list
        List<Transport> path9 = tc.cheapestRoute("Ivy Room", "Calientes");
        assertEquals(0, path9.size());

        // tests that if source doesn't exist, returns an empty list
        List<Transport> path10 = tc.cheapestRoute("Calientes", "Ivy Room");
        assertEquals(0, path10.size());

        // test that if route doesn't exist, returns an empty list
        List<Transport> path11 = tc.cheapestRoute("Andrews", "Blue Room");
        assertEquals(0, path11.size());
    }

    /**
     * Testing for the fastest route in cities1.csv and cities2.csv
     */
    @Test
    public void testFastSimple() {
        TravelController tc = new TravelController();
        tc.load("data/cities2.csv", "data/transport2.csv");
        TravelController tc2 = new TravelController();
        tc2.load("data/cities1.csv", "data/transport1.csv");

        List<Transport> path = tc.fastestRoute("Boston", "Providence");
        assertEquals(1, path.size());

        List<Transport> path6 = tc.fastestRoute("Boston", "Chicago");
        assertEquals(2, path6.size());

        List<Transport> path2 = tc.fastestRoute("Providence", "Washington");
        assertEquals(2, path2.size());

        List<Transport> path3 = tc2.fastestRoute("Boston", "Providence");
        assertEquals(1, path3.size());
        assertTrue(path3.toString().contains("Type: train"));

        List<Transport> path4 = tc2.fastestRoute("Providence", "Boston");
        assertEquals(1, path4.size());
        assertTrue(path4.toString().contains("Type: train"));

        List<Transport> path5 = tc2.fastestRoute("New York City", "Boston");
        assertEquals(1, path5.size());
    }

    /**
     * Testing for the fastest route in ourCities.csv
     */
    @Test
    public void testFast() {
        TravelController tc = new TravelController();
        tc.load("data/ourCities.csv", "data/ourTransport.csv");

        List<Transport> path = tc.fastestRoute("Home", "Andrews");
        assertEquals(2, path.size());

        List<Transport> path2 = tc.fastestRoute("Jos", "Andrews");
        assertEquals(1, path2.size());

        List<Transport> path3 = tc.fastestRoute("Home", "Ivy Room");
        assertEquals(1, path3.size());

        List<Transport> path4 = tc.fastestRoute("VW", "Andrews");
        assertEquals(2, path4.size());

        List<Transport> path5 = tc.fastestRoute("Andrews", "VW");
        assertEquals(3, path5.size());

        List<Transport> path6 = tc.fastestRoute("Home", "Jos");
        assertEquals(2, path6.size());

        // tests when there are multiple best paths for time, will return the most direct
        List<Transport> path8 = tc.fastestRoute("Home", "Blue Room");
        assertEquals(1, path8.size());

        //when there is a double edge, return the faster edge
        List<Transport> path9 = tc.fastestRoute("Home", "Ratty");
        assertEquals(1, path9.size());
        assertTrue(path9.toString().contains("Type: plane"));

        // tests that if source and destination are the same, returns an empty list
        List<Transport> path10 = tc.fastestRoute("VW", "VW");
        assertEquals(0, path10.size());

        // tests that if destination doesn't exist, returns an empty list
        List<Transport> path11 = tc.fastestRoute("Ivy Room", "Calientes");
        assertEquals(0, path11.size());

        //tests that if source doesn't exist, returns an empty list
        List<Transport> path12 = tc.fastestRoute("Calientes", "Ivy Room");
        assertEquals(0, path12.size());

        //tests that if path doesn't exist, returns an empty list
        List<Transport> path13 = tc.fastestRoute("Andrews", "Blue Room");
        assertEquals(0, path13.size());
    }
}
