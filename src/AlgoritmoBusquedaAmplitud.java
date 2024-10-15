import java.util.*;

import org.w3c.dom.Node;

public class AlgoritmoBusquedaAmplitud {

    private static int expandedNodes;  // Variable para contar nodos expandidos
    private static int maxDepth;       // Variable para almacenar la profundidad máxima alcanzada

    static class Node {
        int x, y;           // Coordenadas del nodo en el grid
        Node parent;        // Nodo padre desde el cual se llegó a este nodo
        int depth;          // Profundidad del nodo en el árbol

        // Constructor que inicializa las coordenadas y el nodo padre
        public Node(int x, int y, Node parent, int depth) {
            this.x = x;
            this.y = y;
            this.parent = parent;
            this.depth = depth; // Inicializa la profundidad
        }

        // Método para comparar nodos (necesario para almacenar en un conjunto)
        @Override
        public boolean equals(Object o) {
            if (this == o) return true; // Comparación por referencia
            if (o == null || getClass() != o.getClass()) return false; // Comprobación de tipo
            Node node = (Node) o; // Conversión a tipo Node
            return x == node.x && y == node.y; // Comparación de coordenadas
        }

        // Método para generar un hashCode (necesario para usar en un conjunto)
        @Override
        public int hashCode() {
            return Objects.hash(x, y); // Generación del hash con las coordenadas
        }
    }

    // Método que implementa la búsqueda por amplitud
    public static List<Node> breadthFirstSearch(int[][] grid, int[] start, int[] goal) {
        int rows = grid.length; // Número de filas en el grid
        int cols = grid[0].length; // Número de columnas en el grid

        // Cola para explorar los nodos en amplitud
        Queue<Node> queue = new LinkedList<>();
        
        // Conjunto para almacenar los nodos ya visitados
        Set<Node> visited = new HashSet<>();
        
        // Nodo inicial basado en las coordenadas de inicio
        Node startNode = new Node(start[0], start[1], null, 0);
        queue.add(startNode); // Agregar el nodo inicial a la cola
        visited.add(startNode); // Marcar el nodo inicial como visitado
        
        // Movimientos posibles (arriba, abajo, izquierda, derecha)
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        expandedNodes = 0; // Inicializa el contador de nodos expandidos
        maxDepth = 0; // Inicializa la profundidad máxima


        // Bucle de búsqueda
        while (!queue.isEmpty()) {
            // Extraer el nodo de la cola
            Node current = queue.poll();
            expandedNodes++; // Incrementa el contador de nodos expandidos
            maxDepth = Math.max(maxDepth, current.depth); // Actualiza la profundidad máxima

            // Si hemos llegado al objetivo
            if (current.x == goal[0] && current.y == goal[1]) {
                return reconstructPath(current); // Reconstruir y devolver el camino
            }

            // Expandir los vecinos
            for (int[] direction : directions) {
                // Calcular las nuevas coordenadas del vecino
                int newX = current.x + direction[0];
                int newY = current.y + direction[1];

                // Verificar si el vecino está dentro del rango y no es un muro
                if (newX >= 0 && newX < rows && newY >= 0 && newY < cols && grid[newX][newY] != 1) {
                    Node neighbor = new Node(newX, newY, current, current.depth + 1); // Crear un nuevo nodo vecino

                    // Si no ha sido visitado, agregarlo a la cola
                    if (!visited.contains(neighbor)) {
                        queue.add(neighbor); // Agregar el vecino a la cola
                        visited.add(neighbor); // Marcar como visitado
                    }
                }
            }
        }

        // Si no se encuentra solución, devolver null
        return null;
    }

    // Función para reconstruir el camino desde el objetivo hasta el inicio
    public static List<Node> reconstructPath(Node goal) {
        List<Node> path = new ArrayList<>(); // Lista para almacenar el camino
        Node current = goal; // Comenzar desde el nodo objetivo
        while (current != null) {
            path.add(current); // Agregar el nodo actual al camino
            current = current.parent; // Mover al nodo padre
        }
        // Invertir el camino porque se construye de atrás hacia adelante
        Collections.reverse(path);
        return path; // Devolver el camino reconstruido
    }

     // Métodos para obtener información sobre los nodos expandidos y la profundidad
     public static int getExpandedNodes() {
        return expandedNodes;
    }

    public static int getDepth() {
        return maxDepth;
    }
}
