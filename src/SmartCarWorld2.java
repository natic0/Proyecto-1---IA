import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;


public class SmartCarWorld2 extends JFrame {
    private int[][] grid;
    private final int cellSize = 50;  // Tamaño de cada celda en la cuadrícula
    private JComboBox<String> searchTypeComboBox;  // Combo para el tipo de búsqueda
    private JComboBox<String> algorithmComboBox;   // Combo dinámico para algoritmos
    private JButton startButton;                   // Botón para iniciar búsqueda
    private JPanel controlPanel;                   // Panel de control
    private int vehicleX = -1;        // Posición inicial X del vehículo
    private int vehicleY = -1;        // Posición inicial Y del vehículo
    private Timer timer;              // Timer para animación
    private String filePath;
    private VehicleAnimation vehicleAnimation;
    public SmartCarWorld2(int[][] grid,String filePath) {
        this.grid = grid;
        this.filePath = filePath;
        vehicleAnimation = new VehicleAnimation(this);
        setTitle("Smart Car World");
        setSize(grid[0].length * cellSize + 200, grid.length * cellSize); // Ajustar ancho para el panel de control
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (grid[y][x] == 2) {
                    vehicleX = x;
                    vehicleY = y;
                }
            }
        };
        // Inicializar elementos de la interfaz
        initializeUI();
    }

    private void initializeUI() {
        controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setPreferredSize(new Dimension(177, getHeight()));

        // ComboBox para seleccionar entre búsqueda informada y no informada
        searchTypeComboBox = new JComboBox<>(new String[]{"No informada", "Informada"});
        searchTypeComboBox.addActionListener(e -> updateAlgorithmOptions());

        // ComboBox dinámico para los algoritmos, se actualiza según la selección
        algorithmComboBox = new JComboBox<>();
        updateAlgorithmOptions();  // Inicializa las opciones según la selección inicial

        // Botón para iniciar la búsqueda
        startButton = new JButton("Búsqueda");
        startButton.addActionListener(e -> startSearch());

        // Agregar componentes al panel de control
        controlPanel.add(new JLabel("Tipo de búsqueda:"));
        controlPanel.add(searchTypeComboBox);
        controlPanel.add(new JLabel("Algoritmo:"));
        controlPanel.add(algorithmComboBox);
        controlPanel.add(startButton);

        // Añadir el panel de control a la ventana
        getContentPane().add(controlPanel, BorderLayout.EAST);
    }

    // Método para actualizar las opciones del segundo comboBox dependiendo de la selección
    private void updateAlgorithmOptions() {
        algorithmComboBox.removeAllItems();
        if (searchTypeComboBox.getSelectedItem().equals("No informada")) {
            algorithmComboBox.addItem("Amplitud");
            algorithmComboBox.addItem("Costo uniforme");
            algorithmComboBox.addItem("Profundidad evitando ciclos");
        } else {
            algorithmComboBox.addItem("Avara");
            algorithmComboBox.addItem("A*");
        }
    }

    // Método para iniciar la búsqueda cuando se hace clic en el botón
    private void startSearch() {
        String searchType = (String) searchTypeComboBox.getSelectedItem();
        String algorithm = (String) algorithmComboBox.getSelectedItem();
        System.out.println("Tipo de búsqueda: " + searchType);
        System.out.println("Algoritmo seleccionado: " + algorithm);

        //String filePath = "C:\\Users\\Admin Arandinni\\Downloads\\Copia de Proyecto 1 - IA\\Copia de Proyecto 1 - IA\\src\\world.txt";
        Object[] resultado = loadWorldFromFile(filePath);
        // Asigna los valores a variables específicas
        int[][] grid = (int[][]) resultado[0]; // Matriz del mundo
        int[] car = (int[]) resultado[1];      // Posición del número 2  (Carro)
        int[] pasa = (int[]) resultado[2];      // Posición del número 5 (pasajero)
        int[] goal = (int[]) resultado[3];      // Posición del número 6 (destino final)
        
        switch (algorithm) {
            case "Amplitud":
                System.out.println("Ejecutando búsqueda por amplitud");
                List<AlgoritmoBusquedaAmplitud.Node> pathToPassengerAmp = AlgoritmoBusquedaAmplitud.breadthFirstSearch(grid, car, pasa);
                List<AlgoritmoBusquedaAmplitud.Node> pathToGoalAmp = AlgoritmoBusquedaAmplitud.breadthFirstSearch(grid, pasa, goal);
                vehicleAnimation.animatePathAmp(pathToPassengerAmp, pathToGoalAmp);
                break;
                
            case "Costo uniforme":
                System.out.println("Ejecutando búsqueda de costo uniforme");
                List<AlgoritmoBusquedaCostoUniforme.Node> pathToPassengerCos = AlgoritmoBusquedaCostoUniforme.uniformCostSearch(grid, car, goal);
                List<AlgoritmoBusquedaCostoUniforme.Node> pathToGoalCos = AlgoritmoBusquedaCostoUniforme.uniformCostSearch(grid, car, goal);
                vehicleAnimation.animatePathCos(pathToPassengerCos, pathToGoalCos);
                break;
                
            case "Profundidad evitando ciclos":
                System.out.println("Ejecutando búsqueda en profundidad evitando ciclos");
                List<AlgoritmoBusquedaProfundidad.Node> pathToPassengerPro = AlgoritmoBusquedaProfundidad.depthFirstSearch(grid, car, pasa);
                List<AlgoritmoBusquedaProfundidad.Node> pathToGoalPro = AlgoritmoBusquedaProfundidad.depthFirstSearch(grid, pasa, goal);
                vehicleAnimation.animatePathPro(pathToPassengerPro, pathToGoalPro);
                break;
                
            case "Avara":
                System.out.println("Ejecutando búsqueda Avara");
                // Implementar la lógica de búsqueda Avara aquí
                List<AlgoritmoBusquedaAvara.Node> pathToPassengerAva = AlgoritmoBusquedaAvara.greedyBestFirstSearch(grid, car, pasa);
                List<AlgoritmoBusquedaAvara.Node> pathToGoalAva = AlgoritmoBusquedaAvara.greedyBestFirstSearch(grid, pasa, goal);
                vehicleAnimation.animatePathAvara(pathToPassengerAva, pathToGoalAva);
                
                break;
                
            case "A*":
                System.out.println("Ejecutando A*");
                // Ejecutar A*
                List<AlgoritmoBusquedaA.Node> pathToPassengerAstar = AlgoritmoBusquedaA.AStar(grid, car, pasa);
                List<AlgoritmoBusquedaA.Node> pathToGoalAstar = AlgoritmoBusquedaA.AStar(grid, pasa, goal);
                //animatePath(pathToPassengerAstar, pathToGoalAstar);   // Animar el movimiento
                vehicleAnimation.animatePathA(pathToPassengerAstar, pathToGoalAstar);
                break;
                
            default:
                System.out.println("Algoritmo no reconocido");
                break;
        }
    }
    
    // Ejemplo de animación de los movimientos del vehículo
    private void animatePath(List<AlgoritmoBusquedaA.Node> pathToPassenger, List<AlgoritmoBusquedaA.Node> pathToGoal) {
        // Camino hacia el pasajero
        System.out.println("Camino al pasajero:");
        int[][] pathToPassengerArray = convertPathToArray(pathToPassenger);
        moveVehicle(pathToPassengerArray, () -> {
            // Cuando termine el camino al pasajero, mover al destino
            System.out.println("Camino al destino:");
            int[][] pathToGoalArray = convertPathToArray(pathToGoal);
            moveVehicle(pathToGoalArray, null);
        });
    }
    // Método para convertir una lista de nodos a un array bidimensional de enteros
private int[][] convertPathToArray(List<AlgoritmoBusquedaA.Node> path) {
    int[][] pathArray = new int[path.size()][2]; // Cada nodo tiene dos valores: x e y
    for (int i = 0; i < path.size(); i++) {
        AlgoritmoBusquedaA.Node node = path.get(i);
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
                    repaint();  // Redibujar la cuadrícula con la nueva posición del vehículo
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
     // Método para actualizar la posición del vehículo
     public void updateVehiclePosition(int x, int y) {
        this.vehicleX = x;  // Actualiza la posición X del vehículo
        this.vehicleY = y;  // Actualiza la posición Y del vehículo
        repaint();  // Redibuja la cuadrícula
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                Color color;
                switch (grid[y][x]) {
                    case 0: color = Color.GREEN; break; // Tráfico liviano
                    case 1: color = Color.GRAY; break;  // Muro
                    case 2: color = Color.BLUE; break;  // Vehículo
                    case 3: color = Color.YELLOW; break; // Tráfico medio
                    case 4: color = Color.RED; break;   // Tráfico pesado
                    case 5: color = Color.ORANGE; break; // Pasajero
                    case 6: color = Color.CYAN; break;  // Destino
                    default: color = Color.WHITE; break; // Espacio vacío
                }
                g.setColor(color);
                g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                g.setColor(Color.BLACK);
                g.drawRect(x * cellSize, y * cellSize, cellSize, cellSize);
            }
        }

        // Dibujar el vehículo en su nueva posición
        g.setColor(Color.BLUE);
        g.fillRect(vehicleX * cellSize, vehicleY * cellSize, cellSize, cellSize);
    }

    // Método para cargar el mundo desde un archivo
    public static Object[] loadWorldFromFile(String filePath) {
        int[][] world = new int[10][10]; // Asumimos que el mundo siempre es de 10x10
        int[] var2 = null; // Posición del número 2
        int[] var3 = null; // Posición del número 5 (pasajero)
        int[] var4 = null; // Posición del número 6 (destino final)
    
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int row = 0;
    
            while ((line = br.readLine()) != null && row < 10) {
                String[] values = line.split(" ");
                for (int col = 0; col < values.length && col < 10; col++) {
                    world[row][col] = Integer.parseInt(values[col]);
    
                    // Buscar las posiciones de los números deseados
                    if (world[row][col] == 2 && var2 == null) {
                        var2 = new int[]{row, col}; // Almacena la posición del número 2
                    } else if (world[row][col] == 5 && var3 == null) {
                        var3 = new int[]{row, col}; // Almacena la posición del número 5
                    } else if (world[row][col] == 6 && var4 == null) {
                        var4 = new int[]{row, col}; // Almacena la posición del número 6
                    }
                }
                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        // Retorna el mundo y las posiciones encontradas
        return new Object[]{world, var2, var3, var4};
    }
    
    public static void main(String[] args) {
        String filePath = "world.txt"; // Cambia a la ruta de tu archivo
        Object[] resultado = loadWorldFromFile(filePath);

        // Asigna los valores a variables específicas
        int[][] grid = (int[][]) resultado[0]; // Matriz del mundo
        if (grid != null) {
            SwingUtilities.invokeLater(() -> {
                SmartCarWorld2 world = new SmartCarWorld2(grid,filePath);
                world.setVisible(true);
            });
        } else {
            System.out.println("Error al cargar el mundo.");
        }
    }
}