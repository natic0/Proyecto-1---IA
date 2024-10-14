/*
    Algoritmo de Busqueda informada A*
    @author Natalia
    @author LauraMurillas
 
 * Heuristica: La distancia Manhattan desde el nodo actual al objetivo,
 * pero penalizando la distancia a las casillas con tráfico medio (3) y pesado (4) 
 * para que el algoritmo prefiera rutas con tráfico liviano (0).
 * 
 * Formula: h|(n)= ∣ X actual​ − X objetivo ​∣ + ∣ Y actual​ − Y objetivo ​∣
*/

import java.util.*;

public class AlgoritmoBusquedaA {

    // funcion para representar los nodos (coordenadas) y el costo acumulado
    static class Node implements Comparable<Node> {
        int x, y;  // Estas son las coordenadas del nodo
        int g;     // Costo acumulado contando el inicio
        int f;     // Costo estimado total (g + h)
        
        Node parent;  // Para reconstruir el camino
        
        public Node(int x, int y, int g, int f, Node parent) {
            this.x = x;
            this.y = y;
            this.g = g;
            this.f = f;
            this.parent = parent;
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
        Node startNode = new Node(start[0], start[1], 0, 0, null);
        openList.add(startNode);
        
        // Movimientos posibles (arriba, abajo, izquierda, derecha)
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        
        
        while (!openList.isEmpty()) {
            // Nodo con menor f(n)
            Node current = openList.poll();  

            // Si se ha llegado al objetivo
            if (current.x == goal[0] && current.y == goal[1]) {
                return reconstructPath(current);
            }
            
            closedList.add(current);

            // Expandir los vecinos (sucesores)
            for (int[] direction : directions) {
                int newX = current.x + direction[0];
                int newY = current.y + direction[1];
                
                // Verificar si el vecino está dentro del rango y no es un muro
                if (newX >= 0 && newX < rows && newY >= 0 && newY < cols && grid[newX][newY] != 1) {
                    // Calcular el costo de moverse al vecino
                    int movementCost = getMovementCost(grid[newX][newY]);
                    int tentativeG = current.g + movementCost;
                    
                    Node neighbor = new Node(newX, newY, tentativeG, 0, current);
                    
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
        List<Node> path = new ArrayList<>();
        Node current = goal;
        while (current != null) {
            path.add(current);
            current = current.parent;
        }
        //Se invierte el camino pq se construyó al revés
        Collections.reverse(path);
        return path;
    }

    // Función principal para probar el algoritmo
    public static void main(String[] args) {
        // Definición del grid (10x10)
        int[][] grid = {
            {0, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {0, 1, 1, 0, 0, 0, 4, 0, 0, 0},
            {2, 1, 1, 0, 1, 0, 1, 0, 1, 0},
            {0, 3, 3, 0, 4, 0, 0, 0, 4, 0},
            {0, 1, 1, 0, 1, 1, 1, 1, 1, 0},
            {0, 0, 0, 0, 1, 1, 0, 0, 0, 6},
            {5, 1, 1, 1, 1, 1, 0, 1, 1, 1},
            {0, 1, 0, 0, 0, 1, 0, 0, 0, 1},
            {0, 1, 0, 1, 0, 1, 1, 1, 0, 1},
            {0, 0, 0, 1, 0, 0, 0, 0, 0, 1}
        };
        
        // Punto de partida del vehículo
        int[] start = {2, 0};
        
        // Posición del pasajero
        int[] passenger = {6, 0};
        
        // Destino del pasajero
        int[] goal = {5, 9};

        // 1. Buscar el mejor camino desde el vehículo hasta el pasajero
        List<Node> pathToPassenger = AStar(grid, start, passenger);
        
        if (pathToPassenger != null) {
            System.out.println("Camino al pasajero:");
            for (Node node : pathToPassenger) {
                System.out.println("[" + node.x + ", " + node.y + "]");
            }
        } else {
            System.out.println("No se encontró un camino al pasajero.");
            return;
        }
        
        // 2. Buscar el mejor camino desde el pasajero hasta el destino
        List<Node> pathToGoal = AStar(grid, passenger, goal);
        
        if (pathToGoal != null) {
            System.out.println("Camino al destino:");
            for (Node node : pathToGoal) {
                System.out.println("[" + node.x + ", " + node.y + "]");
            }
        } else {
            System.out.println("No se encontró un camino al destino.");
        }
    }
}
