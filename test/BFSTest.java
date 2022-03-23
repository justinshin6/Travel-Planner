package test;

import org.junit.Test;
import sol.BFS;
import sol.TravelController;
import test.simple.SimpleEdge;
import test.simple.SimpleGraph;
import test.simple.SimpleVertex;
import src.Transport;
import org.junit.Before;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Your BFS tests should all go in this class!
 * The test we've given you will pass if you've implemented BFS correctly, but we still expect
 * you to write more tests using the City and Transport classes.
 * You are welcome to write more tests using the Simple classes, but you will not be graded on
 * those.
 *
 */
public class BFSTest {

    private static final double DELTA = 0.001;

    private SimpleVertex a;
    private SimpleVertex b;
    private SimpleVertex c;
    private SimpleVertex d;
    private SimpleVertex e;
    private SimpleVertex f;
    private SimpleVertex z;
    private SimpleVertex y;
    private SimpleVertex x;
    private SimpleGraph graph;

    /**
     * Creates a simple graph.
     * You'll find a similar method in each of the Test files.
     * Normally, we'd like to use @Before, but because each test may require a different setup,
     * we manually call the setup method at the top of the test.
     *
     *
     */
    @Before
    public void makeSimpleGraph() {
        this.graph = new SimpleGraph();

        this.a = new SimpleVertex("a");
        this.b = new SimpleVertex("b");
        this.c = new SimpleVertex("c");
        this.d = new SimpleVertex("d");
        this.e = new SimpleVertex("e");
        this.f = new SimpleVertex("f");
        this.z = new SimpleVertex("z");
        this.y = new SimpleVertex("y");
        this.x = new SimpleVertex("x");

        this.graph.addVertex(this.a);
        this.graph.addVertex(this.b);
        this.graph.addVertex(this.c);
        this.graph.addVertex(this.d);
        this.graph.addVertex(this.e);
        this.graph.addVertex(this.f);
        this.graph.addVertex(this.z);
        this.graph.addVertex(this.y);
        this.graph.addVertex(this.x);


        this.graph.addEdge(this.a, new SimpleEdge(1, this.a, this.b));
        this.graph.addEdge(this.b, new SimpleEdge(1, this.b, this.c));
        this.graph.addEdge(this.c, new SimpleEdge(1, this.c, this.e));
        this.graph.addEdge(this.d, new SimpleEdge(1, this.d, this.e));
        this.graph.addEdge(this.a, new SimpleEdge(100, this.a, this.f));
        this.graph.addEdge(this.f, new SimpleEdge(100, this.f, this.e));
        this.graph.addEdge(this.z, new SimpleEdge(1, this.z, this.y));
        this.graph.addEdge(this.y, new SimpleEdge(1, this.y, this.x));
    }

    /**
     * Testing for the SimpleVertices and SimpleEdges
     */
    @Test
    public void testBasicBFS() {
        this.makeSimpleGraph();
        BFS<SimpleVertex, SimpleEdge> bfs = new BFS<>();
        List<SimpleEdge> path1 = bfs.getPath(this.graph, this.a, this.e);
        assertEquals(SimpleGraph.getTotalEdgeWeight(path1), 200.0, DELTA);
        assertEquals(path1.size(), 2);
        //test when there is no path
        List<SimpleEdge> path2 = bfs.getPath(this.graph, this.a, this.z);
        assertEquals(path2.size(), 0);

        this.graph.addEdge(this.c, new SimpleEdge(15, this.c, this.z));
        List<SimpleEdge> path3 = bfs.getPath(this.graph, this.a, this.z);
        assertEquals(path3.size(), 3);

        List<SimpleEdge> path4 = bfs.getPath(this.graph, this.a, this.x);
        assertEquals(path4.size(), 5);
    }

    /**
     * Testing for the most direct route for the cities1.csv
     */
    @Test
    public void testCities1BFS() {
        TravelController trc = new TravelController();
        trc.load("data/cities1.csv", "data/transport1.csv");

        //check if source equals the destination which should return size 0
        assertEquals(trc.mostDirectRoute("Boston", "Boston").size(), 0);

        List<Transport> directRouteBostonToProvidence = trc.mostDirectRoute("Boston", "Providence");
        assertEquals(directRouteBostonToProvidence.size(), 1);

        List<Transport> directRouteBostonToNYC = trc.mostDirectRoute("Boston", "New York City");
        assertEquals(directRouteBostonToNYC.size(), 1);

        List<Transport> directRouteProvidenceToNYC = trc.mostDirectRoute("Providence", "New York City");
        assertEquals(directRouteProvidenceToNYC.size(), 2);

        List<Transport> directRouteNYCToProvidence = trc.mostDirectRoute("New York City",
                "Providence");
        assertEquals(directRouteNYCToProvidence.size(), 1);
    }

    /**
     * Testing for the most direct route for the cities2.csv
     */
    @Test
    public void testCities2BFS() {
        TravelController trc = new TravelController();
        trc.load("data/cities2.csv", "data/transport2.csv");

        //basic BFS
        List<Transport> directRouteWashingtonToBoston = trc.mostDirectRoute("Washington", "Boston");
        assertEquals(directRouteWashingtonToBoston.size(), 3);
        List<Transport> directRouteDurhamToProvidence = trc.mostDirectRoute("Durham", "Providence");
        assertEquals(directRouteDurhamToProvidence.size(), 1);

        //testing multiple paths for BFS
        //This is a multiple path because there is a path from Boston -> Durham -> Chicago and there is also a path
        //from Boston -> Washington -> Chicago. Regardless the path.size() is 2.
        List<Transport> directRouteBostonToChicago = trc.mostDirectRoute("Boston", "Chicago");
        assertEquals(directRouteBostonToChicago.size(), 2);

        //testing no connection BFS
        List<Transport> noRouteOne = trc.mostDirectRoute("Boston", "Las Vegas");
        assertEquals(noRouteOne.size(), 0);
        List<Transport> noRouteTwo = trc.mostDirectRoute("Seattle", "Providence");
        assertEquals(noRouteTwo.size(), 0);
        List<Transport> noRouteThree = trc.mostDirectRoute("Las Vegas", "Seattle");
        assertEquals(noRouteThree.size(), 0);

        //testing sameLocation and Cycle BFS
        List<Transport> sameLocationOne = trc.mostDirectRoute("Boston", "Boston");
        assertEquals(sameLocationOne.size(), 0);
        List<Transport> sameLocationTwo = trc.mostDirectRoute("Providence", "Providence");
        assertEquals(sameLocationTwo.size(), 0);
        List<Transport> sameLocationThree = trc.mostDirectRoute("Durham", "Durham");
        assertEquals(sameLocationThree.size(), 0);
    }

    /**
     * Testing for the most direct route for the ourCities.csv
     */
    @Test
    public void testOurCitiesBFS() {
        TravelController trc = new TravelController();
        trc.load("data/ourCities.csv", "data/ourTransport.csv");

        //basic BFS
        List<Transport> directHomeToJos = trc.mostDirectRoute("Home", "Jos");
        assertEquals(directHomeToJos.size(), 1);
        List<Transport> directRattyAndrews = trc.mostDirectRoute("Ratty", "Andrews");
        assertEquals(directRattyAndrews.size(), 1);
        List<Transport> directVWtoAndrews = trc.mostDirectRoute("VW", "Andrews");
        assertEquals(directVWtoAndrews.size(), 2);

        //testing multiple paths BFS
        //This is a multiple path because there are two possible paths. First, Ivy Room -> Jos -> Ratty, and second
        //Ivy Room -> Andrews -> Ratty. Regardless, the size of both is two.
        List<Transport> directIvyRoomRatty = trc.mostDirectRoute("Ivy Room", "Ratty");
        assertEquals(directIvyRoomRatty.size(), 2);

        //testing no connection BFS
        List<Transport> noRouteOne = trc.mostDirectRoute("Jos", "Chipotle");
        assertEquals(noRouteOne.size(), 0);
        List<Transport> noRouteTwo = trc.mostDirectRoute("East side Pockets", "Jos");
        assertEquals(noRouteTwo.size(), 0);
        List<Transport> noRouteThree = trc.mostDirectRoute("East side Pockets", "Chipotle");
        assertEquals(noRouteThree.size(), 0);
        List<Transport> noRouteFour = trc.mostDirectRoute("Jos", "Home");
        assertEquals(noRouteFour.size(), 0);

        //testing sameLocation and Cycle BFS
        List<Transport> sameLocationOne = trc.mostDirectRoute("Home", "Home");
        assertEquals(sameLocationOne.size(), 0);
        List<Transport> sameLocationTwo = trc.mostDirectRoute("Ivy Room", "Ivy Room");
        assertEquals(sameLocationTwo.size(), 0);
        List<Transport> sameLocationThree = trc.mostDirectRoute("Jos", "Jos");
        assertEquals(sameLocationThree.size(), 0);
    }
}
