import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
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
    private JPanel reportPanel;                    // Panel para mostrar el reporte
    private JTextArea reportArea;                  // Área de texto para el reporte
    private JButton loadWorldButton;
    public SmartCarWorld2(int[][] grid,String filePath) {
        this.grid = grid;
        this.filePath = filePath;
        vehicleAnimation = new VehicleAnimation(this);
        setTitle("Smart Car World");
        setSize(grid[0].length * cellSize + 200, grid.length * cellSize+200); // Ajustar ancho para el panel de control
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

        // Panel para el label y el comboBox de tipo de búsqueda
        JPanel searchTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Panel horizontal para label y comboBox
        JLabel searchTypeLabel = new JLabel("Tipo de búsqueda:");

        // ComboBox para seleccionar entre búsqueda informada y no informada
        searchTypeComboBox = new JComboBox<>(new String[]{"No informada", "Informada"});
        searchTypeComboBox.addActionListener(e -> updateAlgorithmOptions());

        searchTypePanel.add(searchTypeLabel); // Añadir el label al panel
        searchTypePanel.add(searchTypeComboBox); // Añadir el comboBox al panel


        // Panel para el label y el comboBox de algoritmo
        JPanel algorithmPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Panel horizontal para label y comboBox
        JLabel algorithmLabel = new JLabel("Algoritmo:");

        // ComboBox dinámico para los algoritmos, se actualiza según la selección
        algorithmComboBox = new JComboBox<>();
        updateAlgorithmOptions();  // Inicializa las opciones según la selección inicial

        algorithmPanel.add(algorithmLabel); // Añadir el label al panel
        algorithmPanel.add(algorithmComboBox); // Añadir el comboBox al panel

        // Botón para iniciar la búsqueda
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Panel horizontal para botones
        // Botón para iniciar la búsqueda
        startButton = new JButton("Búsqueda");
        startButton.addActionListener(e -> startSearch());
        
        // Botón para cargar un archivo nuevo
        loadWorldButton = new JButton("(+) Mundo");
        loadWorldButton.addActionListener(e -> loadWorldFromFileChooser());

        buttonPanel.add(startButton); // Añadir botón de búsqueda al panel
        buttonPanel.add(loadWorldButton); // Añadir botón de cargar mundo al panel

        // Agregar componentes al panel de control
        controlPanel.add(searchTypePanel); // Añadir el panel del tipo de búsqueda
        controlPanel.add(algorithmPanel);  // Añadir el panel del algoritmo
        controlPanel.add(buttonPanel);     // Añadir el panel de botones

        // Panel para el reporte
        reportPanel = new JPanel();
        reportPanel.setLayout(new BorderLayout());
        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setPreferredSize(new Dimension(200, 80));
        reportPanel.add(new JScrollPane(reportArea), BorderLayout.CENTER);
    
        // Añadir el panel de control y el de reporte a la ventana
        getContentPane().setLayout(new BorderLayout()); // Cambiar la disposición del JFrame
        getContentPane().add(controlPanel, BorderLayout.EAST);
        getContentPane().add(reportPanel, BorderLayout.SOUTH); // Colocar el panel de reporte en la parte inferior
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

    // Método para cargar el archivo desde un JFileChooser
    private void loadWorldFromFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            filePath = selectedFile.getAbsolutePath();
            
            // Cargar el mundo desde el archivo seleccionado
            Object[] resultado = loadWorldFromFile(filePath);
    
            if (resultado == null) {
                reportArea.setText("Error: No se pudo cargar el archivo.");
                return;
            }
    
            grid = (int[][]) resultado[0]; // Actualizar la matriz del mundo
            int[] car = (int[]) resultado[1];
    
            // Verificar que se haya encontrado el vehículo (número 2)
            if (car != null) {
                vehicleX = car[1]; // Actualizar la posición del vehículo
                vehicleY = car[0];
            } else {
                reportArea.setText("Error: No se encontró el vehículo en el archivo.");
                return;
            }
    
            // Redibujar la ventana con el nuevo mundo
            repaint();  // Redibuja el componente gráfico con el nuevo mundo cargado
            reportArea.setText("Mundo cargado exitosamente desde: " + filePath);
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

        // Inicializar variables para el reporte
        int expandedNodes = 0;
        int depth = 0;
        long startTime = System.currentTimeMillis();
        int solutionCost = 0;  // Solo aplicable a Costo uniforme y A*

        switch (algorithm) {
            case "Amplitud":
                System.out.println("Ejecutando búsqueda por amplitud");
                List<AlgoritmoBusquedaAmplitud.Node> pathToPassengerAmp = AlgoritmoBusquedaAmplitud.breadthFirstSearch(grid, car, pasa);
                List<AlgoritmoBusquedaAmplitud.Node> pathToGoalAmp = AlgoritmoBusquedaAmplitud.breadthFirstSearch(grid, pasa, goal);
                vehicleAnimation.animatePathAmp(pathToPassengerAmp, pathToGoalAmp);
                expandedNodes = AlgoritmoBusquedaAmplitud.getExpandedNodes(); // Método para obtener la cantidad de nodos expandidos
                depth = AlgoritmoBusquedaAmplitud.getDepth(); // Método para obtener la profundidad del árbol
                break;
                
            case "Costo uniforme":
                System.out.println("Ejecutando búsqueda de costo uniforme");
                List<AlgoritmoBusquedaCostoUniforme.Node> pathToPassengerCos = AlgoritmoBusquedaCostoUniforme.uniformCostSearch(grid, car, goal);
                List<AlgoritmoBusquedaCostoUniforme.Node> pathToGoalCos = AlgoritmoBusquedaCostoUniforme.uniformCostSearch(grid, car, goal);
                vehicleAnimation.animatePathCos(pathToPassengerCos, pathToGoalCos);
                // Obtener datos para el reporte
                expandedNodes = AlgoritmoBusquedaCostoUniforme.getExpandedNodes(); // Método para obtener la cantidad de nodos expandidos
                depth = AlgoritmoBusquedaCostoUniforme.getDepth(); // Método para obtener la profundidad del árbol
                solutionCost =AlgoritmoBusquedaCostoUniforme.getCost();
                break;
                
            case "Profundidad evitando ciclos":
                System.out.println("Ejecutando búsqueda en profundidad evitando ciclos");
                List<AlgoritmoBusquedaProfundidad.Node> pathToPassengerPro = AlgoritmoBusquedaProfundidad.depthFirstSearch(grid, car, pasa);
                List<AlgoritmoBusquedaProfundidad.Node> pathToGoalPro = AlgoritmoBusquedaProfundidad.depthFirstSearch(grid, pasa, goal);
                vehicleAnimation.animatePathPro(pathToPassengerPro, pathToGoalPro);
                expandedNodes = AlgoritmoBusquedaProfundidad.getExpandedNodes(); // Método para obtener la cantidad de nodos expandidos
                depth = AlgoritmoBusquedaProfundidad.getDepth(); // Método para obtener la profundidad del árbol
                break;
                
            case "Avara":
                System.out.println("Ejecutando búsqueda Avara");
                // Implementar la lógica de búsqueda Avara aquí
                List<AlgoritmoBusquedaAvara.Node> pathToPassengerAva = AlgoritmoBusquedaAvara.greedyBestFirstSearch(grid, car, pasa);
                List<AlgoritmoBusquedaAvara.Node> pathToGoalAva = AlgoritmoBusquedaAvara.greedyBestFirstSearch(grid, pasa, goal);
                vehicleAnimation.animatePathAvara(pathToPassengerAva, pathToGoalAva);
                expandedNodes = AlgoritmoBusquedaAvara.getExpandedNodes(); // Método para obtener la cantidad de nodos expandidos
                depth = AlgoritmoBusquedaAvara.getDepth(); // Método para obtener la profundidad del árbol
                
                break;
                
            case "A*":
                System.out.println("Ejecutando A*");
                // Ejecutar A*
                List<AlgoritmoBusquedaA.Node> pathToPassengerAstar = AlgoritmoBusquedaA.AStar(grid, car, pasa);
                List<AlgoritmoBusquedaA.Node> pathToGoalAstar = AlgoritmoBusquedaA.AStar(grid, pasa, goal);
                //animatePath(pathToPassengerAstar, pathToGoalAstar);   // Animar el movimiento
                vehicleAnimation.animatePathA(pathToPassengerAstar, pathToGoalAstar);
                expandedNodes = AlgoritmoBusquedaA.getExpandedNodes(); // Método para obtener la cantidad de nodos expandidos
                depth = AlgoritmoBusquedaA.getDepth(); // Método para obtener la profundidad del árbol
                solutionCost =AlgoritmoBusquedaA.getCost();
                break;
                
            default:
                System.out.println("Algoritmo no reconocido");
                break;
        }

        
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        // Mostrar el reporte en el área de texto
        reportArea.setText("Reporte de búsqueda:\n");
        reportArea.append("Nodos expandidos: " + expandedNodes + "\n");
        reportArea.append("Profundidad del árbol: " + depth + "\n");
        reportArea.append("Tiempo de cómputo: " + elapsedTime + " ms\n");
        if (solutionCost > 0) {
            reportArea.append("Costo de la solución: " + solutionCost + "\n");
        }
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