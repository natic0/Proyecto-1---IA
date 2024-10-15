import java.util.*;

public class AlgoritmoBusquedaCostoUniforme {

    static class Node implements Comparable<Node> {
        int x, y;  // Coordenadas del nodo
        int g;     // Costo acumulado desde el nodo inicial
        Node parent;  // Nodo desde el cual se llegó a este nodo

        // Constructor que inicializa las coordenadas, costo y el nodo padre
        public Node(int x, int y, int g, Node parent) {
            this.x = x;
            this.y = y;
            this.g = g;
            this.parent = parent;
        }

        // Método para comparar los nodos según el costo acumulado (g)
        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.g, other.g);  // Compara el costo de este nodo con otro
        }

        @Override
        public boolean equals(Object o) {
            // Compara si dos nodos son iguales basándose en sus coordenadas
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return x == node.x && y == node.y;
        }

        @Override
        public int hashCode() {
            // Genera un hashcode basado en las coordenadas del nodo
            return Objects.hash(x, y);
        }
    }

    // Función de búsqueda de costo uniforme
    public static List<Node> uniformCostSearch(int[][] grid, int[] start, int[] goal) {
        int rows = grid.length;  // Número de filas en el grid
        int cols = grid[0].length;  // Número de columnas en el grid

        // Cola de prioridad para explorar los nodos según el costo acumulado
        PriorityQueue<Node> openList = new PriorityQueue<>();
        // Conjunto para almacenar los nodos visitados
        Set<Node> closedList = new HashSet<>();

        // Nodo inicial
        Node startNode = new Node(start[0], start[1], 0, null);  // Inicializa el nodo de inicio con costo 0
        openList.add(startNode);  // Añade el nodo inicial a la lista abierta

        // Movimientos posibles (arriba, abajo, izquierda, derecha)
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        // Mientras haya nodos por explorar
        while (!openList.isEmpty()) {
            // Extraer el nodo con menor costo acumulado
            Node current = openList.poll();

            // Si se ha llegado al objetivo
            if (current.x == goal[0] && current.y == goal[1]) {
                return reconstructPath(current);  // Reconstruir el camino desde el nodo objetivo
            }

            closedList.add(current);  // Añadir el nodo actual a la lista cerrada

            // Expandir los vecinos del nodo actual
            for (int[] direction : directions) {
                int newX = current.x + direction[0];  // Nueva coordenada X
                int newY = current.y + direction[1];  // Nueva coordenada Y

                // Verificar si el vecino está dentro del rango y no es un muro
                if (newX >= 0 && newX < rows && newY >= 0 && newY < cols && grid[newX][newY] != 1) {
                    int movementCost = getMovementCost(grid[newX][newY]);  // Obtener el costo de moverse a la celda
                    int tentativeG = current.g + movementCost;  // Calcular el costo acumulado

                    Node neighbor = new Node(newX, newY, tentativeG, current);  // Crear el nodo vecino

                    // Si ya se visitó el nodo, continuar
                    if (closedList.contains(neighbor)) continue;

                    // Agregar el vecino a la cola si no está o si se encuentra un mejor camino
                    if (!openList.contains(neighbor)) {
                        openList.add(neighbor);  // Añadir el vecino a la lista abierta
                    }
                }
            }
        }

        // Si no se encuentra solución, retornar null
        return null;
    }

    // Función auxiliar para obtener el costo de moverse a una casilla
    public static int getMovementCost(int cellType) {
        switch (cellType) {
            case 0: return 1;  // Camino libre
            case 3: return 4;  // Tráfico medio
            case 4: return 7;  // Tráfico pesado
            default: return 1;  // Otros casos
        }
    }

    // Función para reconstruir el camino desde el objetivo hasta el inicio
    public static List<Node> reconstructPath(Node goal) {
        List<Node> path = new ArrayList<>();  // Lista para almacenar el camino
        Node current = goal;  // Comienza desde el nodo objetivo
        while (current != null) {
            path.add(current);  // Añade el nodo actual al camino
            current = current.parent;  // Mueve al nodo padre
        }
        // Invertir el camino porque se construyó al revés
        Collections.reverse(path);
        return path;  // Retorna el camino reconstruido
    }

    // Main de prueba
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

        // Buscar el camino desde el vehículo hasta el pasajero
        List<Node> pathToPassenger = uniformCostSearch(grid, start, passenger);

        if (pathToPassenger != null) {
            System.out.println("Camino al pasajero:");
            for (Node node : pathToPassenger) {
                System.out.println("[" + node.x + ", " + node.y + "]");  // Imprimir coordenadas del camino
            }
        } else {
            System.out.println("No se encontró un camino al pasajero.");
            return;
        }

        // Buscar el camino desde el pasajero hasta el destino
        List<Node> pathToGoal = uniformCostSearch(grid, passenger, goal);

        if (pathToGoal != null) {
            System.out.println("Camino al destino:");
            for (Node node : pathToGoal) {
                System.out.println("[" + node.x + ", " + node.y + "]");  // Imprimir coordenadas del camino
            }
        } else {
            System.out.println("No se encontró un camino al destino.");
        }
    }
}
