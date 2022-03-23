# Travel-Planner
This project allows a user to plan travel routes that meet their needs. Speciically, users can load a travel graph, then request routes according to one of three priorities:
- Price, the route that costs the lowest amount of money
- Time, the route with the lowest total travel time
- Directness, the route with the fewest number of connections
Users will load graphs and request routes through text prompts in the console. This program computes the routes and will print the result back to users. 

The two main algorithms that were implemented in this program were the Breadth-first search (BFS) algorithm and Dijkstra's algorithm. BFS was implemented to find the most direct route from one city to the next, and Dijkstra's algorithm was used with weighted graphs that found the cheapest and the fastest route from one city to the next. 
