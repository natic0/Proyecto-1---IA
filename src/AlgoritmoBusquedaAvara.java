/*
    Algoritmo de Busqueda informada Avara

 * Heuristica: Greedy Best Firts
 * consiste en escoger los nodos con menor distancia al destino del pasajero
 * En esta heuristica no se considera la penalizacion por el tráfico para escoger el camino
*/

import java.util.*;


public class AlgoritmoBusquedaAvara{

    private static int expandedNodes;  // Variable para contar nodos expandidos
    private static int maxDepth;       // Variable para almacenar la profundidad máxima alcanzada

    // Clase para representar los nodos
    static class Node implements Comparable<Node> {

        // Coordenadas del nodo
        int x, y;  

        // Valor heurístico 
        int h;     

        // Para reconstruir el camino
        Node parent;  

        // Profundidad del nodo
        int depth;  

        public Node(int x, int y, int h, Node parent) {
            this.x = x;
            this.y = y;
            this.h = h;
            this.parent = parent;
            this.depth = parent != null ? parent.depth + 1 : 0; // Establece la profundidad
        }

        // Comparar solo por la heurística
        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.h, other.h);  
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return x == node.x && y == node.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    // Heurística para considerar en el camino mas corto a la meta (distancia mannhattan)
    public static int heuristic(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);  
    }

    // Función de busqueda Greedy Best Firts
    public static List<Node> greedyBestFirstSearch(int[][] grid, int[] start, int[] goal) {
        int rows = grid.length;
        int cols = grid[0].length;

        // Selecciona el nodo con menor heurística
        PriorityQueue<Node> openList = new PriorityQueue<>();
        
        //Almacena los nodos ya explorados
        Set<Node> closedList = new HashSet<>();
        
        // Nodo inicial
        Node startNode = new Node(start[0], start[1], heuristic(start[0], start[1], goal[0], goal[1]), null);
        openList.add(startNode);
        
        // Movimientos posibles
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        expandedNodes = 0; // Inicializa el contador de nodos expandidos
        maxDepth = 0; // Inicializa la profundidad máxima
        
        while (!openList.isEmpty()) {
            // Nodo con menor h(n)
            Node current = openList.poll();  
            expandedNodes++; // Incrementa el contador de nodos expandidos
            maxDepth = Math.max(maxDepth, current.depth); // Actualiza la profundidad máxima

            // Si hemos llegado al objetivo
            if (current.x == goal[0] && current.y == goal[1]) {
                return reconstructPath(current);
            }
            
            closedList.add(current);

            // Expandir los nodos vecinos
            for (int[] direction : directions) {
                int newX = current.x + direction[0];
                int newY = current.y + direction[1];
                
                //verifca si el vecino está dentro del rango y no es un muro
                if (newX >= 0 && newX < rows && newY >= 0 && newY < cols && grid[newX][newY] != 1) {
                    Node neighbor = new Node(newX, newY, heuristic(newX, newY, goal[0], goal[1]), current);
                    
                    // Si ya hemos pasado por ese nodo
                    if (closedList.contains(neighbor)) {
                        continue;  
                    }
                    
                    // Si el vecino no está en la openList, agregarlo
                    if (!openList.contains(neighbor)) {
                        openList.add(neighbor);
                    }
                }
            }
        }
        
         // Si no se encuentra solución
        return null; 
    }

    // Función para reconstruir el camino desde el objetivo hasta el inicio
    public static List<Node> reconstructPath(Node goal) {
        List<Node> path = new ArrayList<>();
        Node current = goal;
        while (current != null) {
            path.add(current);
            current = current.parent;
        }
        // El camino se construye al revés, así que lo invertimos
        Collections.reverse(path);  
        return path;
    }

    // Función para imprimir el camino
    public static void printPath(List<Node> path) {
        System.out.println("Camino encontrado:");
        for (Node node : path) {
            System.out.println("[" + node.x + ", " + node.y + "]");
        }
    }// Métodos para obtener información sobre los nodos expandidos y la profundidad
    public static int getExpandedNodes() {
        return expandedNodes;
    }

    public static int getDepth() {
        return maxDepth;
    }

}

