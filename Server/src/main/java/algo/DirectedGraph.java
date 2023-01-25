package algo;

import java.util.ArrayList;
import java.util.Stack;

/**
 *  it was used initially to generate the csvs with the largest path from the graph
 *  which are located now in the client
 *  class for finding single source longest distances in a DAG
 *  Graph is represented using adjacency list. Every
 *  node of adjacency list contains vertex number of
 *  the vertex to which edge connects. It also
 *  contains weight of the edge
 */

class DirectedGraph {
    static class AdjacencyListNode {
        int vertex;
        int weight;

        AdjacencyListNode(int vertex, int weight) {
            this.vertex = vertex;
            this.weight = weight;
        }

        int getVertex() {
            return vertex;
        }

        int getWeight() {
            return weight;
        }
    }


    /**
     * Class to represent a graph using adjacency list representation
     * - we have Pointer to an array containing adjacency lists
     */
    static class Graph {
        int numberOfVertices;
        ArrayList<ArrayList<AdjacencyListNode>> adj;
        Graph(int nrOfVertices) // Constructor
        {
            this.numberOfVertices = nrOfVertices;
            adj = new ArrayList<ArrayList<AdjacencyListNode>>(nrOfVertices);
            for (int i = 0; i < nrOfVertices; i++) {
                adj.add(new ArrayList<AdjacencyListNode>());
            }
        }

        void addEdge(int u, int v, int weight) {
            AdjacencyListNode node = new AdjacencyListNode(v, weight);
            adj.get(u).add(node); // Add v to u's list
        }

        /**
         * - Mark the current node as visited
         * - Recur for all the vertices adjacent to this vertex
         * - Push current vertex to stack which stores topological sort
         * A recursive function used by longestPath
         * @param vertice the node
         * @param visited visited node
         * @param stack the stack
         */
        void topologicalSorting(int vertice, boolean visited[],
                                Stack<Integer> stack) {
            visited[vertice] = true;
            for (int i = 0; i < adj.get(vertice).size(); i++) {
                AdjacencyListNode node = adj.get(vertice).get(i);
                if (!visited[node.getVertex()])
                    topologicalSorting(node.getVertex(), visited, stack);
            }
            stack.push(vertice);
        }

        /**
         * The function to find longest distances from a given vertex.
         * It uses recursive topologicalSortUtil() to get topological sorting
         *
         * - Mark all the vertices as not visited
         * - Call the recursive helper function to store Topological
         * Sort starting from all vertices one by one
         * - Initialize distances to all vertices as infinite and
         * - Process vertices in topological order
         * - Get the next vertex from topological order
         * - update distance of all adjacent vertices
         * distance to source as 0
         * @param source the source node
         */
        void longestPath(int source) {
            Stack<Integer> stack = new Stack<Integer>();
            int[] distance = new int[numberOfVertices];
            boolean[] visited = new boolean[numberOfVertices];
            for (int i = 0; i < numberOfVertices; i++)
                visited[i] = false;
            for (int i = 0; i < numberOfVertices; i++)
                if (!visited[i])
                    topologicalSorting(i, visited, stack);
            for (int i = 0; i < numberOfVertices; i++)
                distance[i] = Integer.MIN_VALUE;

            distance[source] = 0;
            while (!stack.isEmpty()) {
                int u = stack.peek();
                stack.pop();

                if (distance[u] != Integer.MIN_VALUE) {
                    for (int i = 0; i < adj.get(u).size(); i++) {
                        AdjacencyListNode node = adj.get(u).get(i);
                        if (distance[node.getVertex()] < distance[u] + node.getWeight())
                            distance[node.getVertex()] = distance[u] + node.getWeight();
                    }
                }
            }
        }
    }



    public static void main(String args[]) {
        Graph graph = new Graph(6);
        // read from the csv of Beverly Hills with the street graph
        int s = 1;
        graph.longestPath(s);
    }
}