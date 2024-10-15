import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class VehicleAnimation {
    private int vehicleX;
    private int vehicleY;
    private Timer timer;
    private SmartCarWorld2 world; // Instancia de SmartCarWorld2 para acceder a los métodos de dibujo

    public VehicleAnimation(SmartCarWorld2 world) {
        this.world = world;
    }
    //A*
    public void animatePathA(List<AlgoritmoBusquedaA.Node> pathToPassenger, List<AlgoritmoBusquedaA.Node> pathToGoal) {
        // Camino hacia el pasajero
        System.out.println("Camino al pasajero:");
        int[][] pathToPassengerArray = convertPathToArrayA(pathToPassenger);
        moveVehicle(pathToPassengerArray, () -> {
            // Cuando termine el camino al pasajero, mover al destino
            System.out.println("Camino al destino:");
            int[][] pathToGoalArray = convertPathToArrayA(pathToGoal);
            moveVehicle(pathToGoalArray, null);
        });
    }
    // Método para convertir una lista de nodos a un array bidimensional de enteros
    private int[][] convertPathToArrayA(List<AlgoritmoBusquedaA.Node> path) {
        int[][] pathArray = new int[path.size()][2]; // Cada nodo tiene dos valores: x e y
        for (int i = 0; i < path.size(); i++) {
            AlgoritmoBusquedaA.Node node = path.get(i);
            pathArray[i][0] = node.x;
            pathArray[i][1] = node.y;
        }
        return pathArray;
    }

    //AVARA
    public void animatePathAvara(List<AlgoritmoBusquedaAvara.Node> pathToPassenger, List<AlgoritmoBusquedaAvara.Node> pathToGoal) {
        // Camino hacia el pasajero
        System.out.println("Camino al pasajero:");
        int[][] pathToPassengerArray = convertPathToArrayAvara(pathToPassenger);
        moveVehicle(pathToPassengerArray, () -> {
            // Cuando termine el camino al pasajero, mover al destino
            System.out.println("Camino al destino:");
            int[][] pathToGoalArray = convertPathToArrayAvara(pathToGoal);
            moveVehicle(pathToGoalArray, null);
        });
    }
    // Método para convertir una lista de nodos a un array bidimensional de enteros
    private int[][] convertPathToArrayAvara(List<AlgoritmoBusquedaAvara.Node> path) {
        int[][] pathArray = new int[path.size()][2]; // Cada nodo tiene dos valores: x e y
        for (int i = 0; i < path.size(); i++) {
            AlgoritmoBusquedaAvara.Node node = path.get(i);
            pathArray[i][0] = node.x;
            pathArray[i][1] = node.y;
        }
        return pathArray;
    }
     //PROFUNDIDAD
     public void animatePathPro(List<AlgoritmoBusquedaProfundidad.Node> pathToPassenger, List<AlgoritmoBusquedaProfundidad.Node> pathToGoal) {
        // Camino hacia el pasajero
        System.out.println("Camino al pasajero:");
        int[][] pathToPassengerArray = convertPathToArrayPro(pathToPassenger);
        moveVehicle(pathToPassengerArray, () -> {
            // Cuando termine el camino al pasajero, mover al destino
            System.out.println("Camino al destino:");
            int[][] pathToGoalArray = convertPathToArrayPro(pathToGoal);
            moveVehicle(pathToGoalArray, null);
        });
    }
    // Método para convertir una lista de nodos a un array bidimensional de enteros
    private int[][] convertPathToArrayPro(List<AlgoritmoBusquedaProfundidad.Node> path) {
        int[][] pathArray = new int[path.size()][2]; // Cada nodo tiene dos valores: x e y
        for (int i = 0; i < path.size(); i++) {
            AlgoritmoBusquedaProfundidad.Node node = path.get(i);
            pathArray[i][0] = node.x;
            pathArray[i][1] = node.y;
        }
        return pathArray;
    }
    // COSTO 
    public void animatePathCos(List<AlgoritmoBusquedaCostoUniforme.Node> pathToPassenger, List<AlgoritmoBusquedaCostoUniforme.Node> pathToGoal) {
        // Camino hacia el pasajero
        System.out.println("Camino al pasajero:");
        int[][] pathToPassengerArray = convertPathToArrayCos(pathToPassenger);
        moveVehicle(pathToPassengerArray, () -> {
            // Cuando termine el camino al pasajero, mover al destino
            System.out.println("Camino al destino:");
            int[][] pathToGoalArray = convertPathToArrayCos(pathToGoal);
            moveVehicle(pathToGoalArray, null);
        });
    }
    // Método para convertir una lista de nodos a un array bidimensional de enteros
    private int[][] convertPathToArrayCos(List<AlgoritmoBusquedaCostoUniforme.Node> path) {
        int[][] pathArray = new int[path.size()][2]; // Cada nodo tiene dos valores: x e y
        for (int i = 0; i < path.size(); i++) {
            AlgoritmoBusquedaCostoUniforme.Node node = path.get(i);
            pathArray[i][0] = node.x;
            pathArray[i][1] = node.y;
        }
        return pathArray;
    }
    //AMPLITUD
    public void animatePathAmp(List<AlgoritmoBusquedaAmplitud.Node> pathToPassenger, List<AlgoritmoBusquedaAmplitud.Node> pathToGoal) {
        // Camino hacia el pasajero
        System.out.println("Camino al pasajero:");
        int[][] pathToPassengerArray = convertPathToArrayAmp(pathToPassenger);
        moveVehicle(pathToPassengerArray, () -> {
            // Cuando termine el camino al pasajero, mover al destino
            System.out.println("Camino al destino:");
            int[][] pathToGoalArray = convertPathToArrayAmp(pathToGoal);
            moveVehicle(pathToGoalArray, null);
        });
    }
    // Método para convertir una lista de nodos a un array bidimensional de enteros
    private int[][] convertPathToArrayAmp(List<AlgoritmoBusquedaAmplitud.Node> path) {
        int[][] pathArray = new int[path.size()][2]; // Cada nodo tiene dos valores: x e y
        for (int i = 0; i < path.size(); i++) {
            AlgoritmoBusquedaAmplitud.Node node = path.get(i);
            pathArray[i][0] = node.x;
            pathArray[i][1] = node.y;
        }
        return pathArray;
    }



    // Método que mueve el vehículo paso a paso en el camino dado
    private void moveVehicle(int[][] path, Runnable onComplete) {
        timer = new Timer();
        TimerTask task = new TimerTask() {
            int stepIndex = 0;

            @Override
            public void run() {
                if (stepIndex < path.length) {
                    vehicleX = path[stepIndex][1];  // Actualizar coordenada X del vehículo
                    vehicleY = path[stepIndex][0];  // Actualizar coordenada Y del vehículo
                    world.updateVehiclePosition(vehicleX, vehicleY);  // Llama a un método en SmartCarWorld2
                    stepIndex++;
                } else {
                    timer.cancel();  // Detener el timer cuando terminamos
                    if (onComplete != null) {
                        onComplete.run();  // Ejecutar acción cuando se completa el movimiento
                    }
                }
            }
        };
        timer.schedule(task, 0, 500);  // Ejecutar cada 500 ms
    }
}