/*
    Algoritmo de Busqueda informada A*

 * Heuristica: La distancia Manhattan desde el nodo actual al objetivo,
 * pero penalizando la distancia a las casillas con tráfico medio (3) y pesado (4) 
 * para que el algoritmo prefiera rutas con tráfico liviano (0).
 * 
 * Formula: h|(n)= ∣ X actual​ − X objetivo ​∣ + ∣ Y actual​ − Y objetivo ​∣
*/

import java.util.*;

public class AlgoritmoBusquedaA {

    private static int expandedNodes;  // Variable para contar nodos expandidos
    private static int maxDepth;  // Profundidad máxima del árbol
    private static int cost;  // Costo de la solución

    // funcion para representar los nodos (coordenadas) y el costo acumulado
    static class Node implements Comparable<Node> {
        int x, y;  // Estas son las coordenadas del nodo
        int g;     // Costo acumulado contando el inicio
        int f;     // Costo estimado total (g + h)
        
        Node parent;  // Para reconstruir el camino

        int depth; // Profundidad del nodo en el árbol
        
        public Node(int x, int y, int g, int f, Node parent, int depth) {
            this.x = x;
            this.y = y;
            this.g = g;
            this.f = f;
            this.parent = parent;
            this.depth = depth; // Establecer profundidad del nodo
        }


        
        // Para comparar por el costo total f(n)
        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.f, other.f);  
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

    // Heurística: Distancia Manhattan
    public static int heuristic(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    // Función para ejecutar A* entre dos puntos
    public static List<Node> AStar(int[][] grid, int[] start, int[] goal) {
        int rows = grid.length;
        int cols = grid[0].length;

        // Lista de los nodos a expandir
        PriorityQueue<Node> openList = new PriorityQueue<>();
        
        // Nodos ya explorados
        Set<Node> closedList = new HashSet<>();
        
        // Nodo inicial
        Node startNode = new Node(start[0], start[1], 0, 0, null, 0); // Inicializa el nodo de inicio con costo 0 y profundidad 0
        openList.add(startNode);  // Añade el nodo inicial a la lista abierta
        
        // Movimientos posibles (arriba, abajo, izquierda, derecha)
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        expandedNodes = 0; // Contador de nodos expandidos

        long startTime = System.nanoTime(); // Iniciar el temporizador
        
        
        while (!openList.isEmpty()) {
            // Nodo con menor f(n)
            Node current = openList.poll();  
            expandedNodes++; // Incrementar el contador de nodos expandidos

            // Si se ha llegado al objetivo
            if (current.x == goal[0] && current.y == goal[1]) {
                long endTime = System.nanoTime(); // Finalizar el temporizador
                long duration = endTime - startTime; // Calcular duración en nanosegundos
                maxDepth = current.depth; // Actualiza la profundidad máxima
                cost = current.g; // Actualiza el costo de la solución

                // Mostrar reporte
                System.out.println("Reporte:");
                System.out.println("Cantidad de nodos expandidos: " + expandedNodes);
                System.out.println("Profundidad del árbol: " + maxDepth);
                System.out.println("Tiempo de cómputo: " + (duration / 1_000_000) + " ms"); // Convertir a milisegundos
                System.out.println("Costo de la solución encontrada: " + current.g);

                return reconstructPath(current);  // Reconstruir el camino desde el nodo objetivo
            }
            
            closedList.add(current); // Añadir el nodo actual a la lista cerrada

            // Expandir los vecinos (sucesores)
            for (int[] direction : directions) {
                int newX = current.x + direction[0];
                int newY = current.y + direction[1];
                
                // Verificar si el vecino está dentro del rango y no es un muro
                if (newX >= 0 && newX < rows && newY >= 0 && newY < cols && grid[newX][newY] != 1) {
                    // Calcular el costo de moverse al vecino
                    int movementCost = getMovementCost(grid[newX][newY]);
                    int tentativeG = current.g + movementCost;
                    
                    Node neighbor = new Node(newX, newY, tentativeG, 0, current,  current.depth + 1);
                    
                    if (closedList.contains(neighbor)) {
                        continue;  // Ya hemos explorado este nodo
                    }
                    
                    // Calcular f(n) = g(n) + h(n)
                    neighbor.f = tentativeG + heuristic(newX, newY, goal[0], goal[1]);

                    // Si el vecino no está en la openList o encontramos un camino mejor
                    if (!openList.contains(neighbor)) {
                        openList.add(neighbor);
                    }
                }
            }
        }
        
        // Si no se encuentra solución
        return null;  
    }

    public static int getExpandedNodes() {
        return expandedNodes;  // Retorna el número de nodos expandidos
    }

    public static int getDepth() {
        return maxDepth;  // Retorna la profundidad máxima
    }

    public static int getCost() {
        return cost;  // Retorna el costo de la solución
    }

    // Función auxiliar para obtener el costo de moverse a una casilla
    public static int getMovementCost(int cellType) {
        switch (cellType) {
            case 0: return 1;  
            case 3: return 4;  
            case 4: return 7;  
            default: return 1;  
        }
    }

    // Función para reconstruir el camino desde el objetivo hasta el inicio
    public static List<Node> reconstructPath(Node goal) {
        List<Node> path = new ArrayList<>();//Lista para almacenar el camino
        Node current = goal; //Comienza desde el nodo objetivo
        while (current != null) {
            path.add(current); // Añade el nodo actual al camino
            current = current.parent; // Mueve al nodo padre
        }
        //Se invierte el camino pq se construyó al revés
        Collections.reverse(path);
        return path; // Retorna el camino reconstruido
    }
}
