import java.util.*;

public class AlgoritmoBusquedaProfundidad {

    private static int expandedNodes;  // Variable para contar nodos expandidos
    private static int maxDepth;  // Variable para almacenar la profundidad máxima alcanzada

    static class Node {
        int x, y; // Coordenadas del nodo en el grid
        Node parent; // Nodo padre para reconstruir el camino
        int depth; // Profundidad del nodo en el árbol
    
        // Constructor que inicializa las coordenadas y el nodo padre
        public Node(int x, int y, Node parent, int depth) {
            this.x = x;
            this.y = y;
            this.parent = parent;
            this.depth = depth; // Se inicializa la profundidad del nodo
        }

        // Método que compara nodos para verificar si son iguales
        @Override
        public boolean equals(Object o) {
            if (this == o) return true; // Comparación por referencia
            if (o == null || getClass() != o.getClass()) return false; // Verifica tipo
            Node node = (Node) o; // Conversión segura
            return x == node.x && y == node.y; // Compara coordenadas
        }

        // Método que genera un hash para el nodo, necesario para usarlo en colecciones
        @Override
        public int hashCode() {
            return Objects.hash(x, y); // Genera un hash basado en las coordenadas
        }
    }

    // Método que implementa la búsqueda en profundidad
    public static List<Node> depthFirstSearch(int[][] grid, int[] start, int[] goal) {
        int rows = grid.length; // Número de filas en el grid
        int cols = grid[0].length; // Número de columnas en el grid

        Stack<Node> stack = new Stack<>(); // Pila para la búsqueda
        Set<Node> visited = new HashSet<>(); // Conjunto para nodos visitados

       // Inicializa el nodo de inicio
       Node startNode = new Node(start[0], start[1], null, 0);
       stack.push(startNode); // Agrega el nodo de inicio a la pila

        // Direcciones posibles para mover (arriba, abajo, izquierda, derecha)
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        expandedNodes = 0; // Inicializa el contador de nodos expandidos
        maxDepth = 0; // Inicializa la profundidad máxima

        // Bucle principal de la búsqueda
        while (!stack.isEmpty()) {
            Node current = stack.pop(); // Extrae el nodo en la parte superior de la pila
            // Verifica si se ha llegado al nodo objetivo
            if (current.x == goal[0] && current.y == goal[1]) {
                return reconstructPath(current); // Reconstruye y devuelve el camino
            }

            visited.add(current); // Marca el nodo como visitado
            expandedNodes++; // Incrementa el contador de nodos expandidos
            maxDepth = Math.max(maxDepth, current.depth); // Actualiza la profundidad máxima

            // Explora los nodos vecinos
            for (int[] direction : directions) {
                int newX = current.x + direction[0]; // Nueva coordenada X
                int newY = current.y + direction[1]; // Nueva coordenada Y

                // Verifica si la nueva posición es válida y no es un obstáculo
                if (newX >= 0 && newX < rows && newY >= 0 && newY < cols && grid[newX][newY] != 1) {
                    Node neighbor = new Node(newX, newY, current, current.depth + 1); // Crea un nodo vecino

                    // Si el vecino ya fue visitado, lo ignora
                    if (visited.contains(neighbor)) {
                        continue;
                    }
                    
                    stack.push(neighbor); // Agrega el vecino a la pila para exploración
                }
            }
        }
        
        return null; // Retorna null si no se encuentra un camino
    }

    // Método para reconstruir el camino desde el nodo objetivo
    public static List<Node> reconstructPath(Node goal) {
        List<Node> path = new ArrayList<>(); // Lista para almacenar el camino
        Node current = goal; // Comienza desde el nodo objetivo
        // Recorre los nodos padres hasta llegar al inicio
        while (current != null) {
            path.add(current); // Agrega el nodo actual al camino
            current = current.parent; // Se mueve al nodo padre
        }
        Collections.reverse(path); // Invierte el camino para que esté en el orden correcto
        return path; // Retorna el camino reconstruido
    }
    
     // Métodos para obtener información sobre los nodos expandidos y la profundidad
     public static int getExpandedNodes() {
        return expandedNodes;
    }

    public static int getDepth() {
        return maxDepth;
    }
    
}
