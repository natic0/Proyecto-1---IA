/*
 * Interfaz de la ciudad inteligente
 * 
 * @author Laura Murillas
 * @author Natalia
 */


import java.util.Scanner;


public class CiudadInteligente {

    private static final int[][] ciudad = {
        {0,1,1,1,1,1,1,1,1,1},
        {0,1,1,0,0,0,4,0,0,0},
        {2,1,1,0,1,0,1,0,1,0},
        {0,3,3,0,4,0,0,0,4,0},
        {0,1,1,0,1,1,1,1,1,0},
        {0,0,0,0,1,1,0,0,0,6},
        {5,1,1,1,1,1,0,1,1,1},
        {0,1,0,0,0,1,0,0,0,1},
        {0,1,0,1,0,1,1,1,0,1},
        {0,0,0,1,0,0,0,0,0,1}
    };



    public static void mostrarCiudad() {
        System.out.println("Estado inicial de la ciudad: ");
        for (int[] fila : ciudad){
            for (int celda : fila){
                System.out.print(celda + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        mostrarCiudad();

        //llamado a los algoritmos de búsqueda
        System.out.println("Seleccione un tipo de busqueda: ");
        System.out.println("1. Busqueda no informada");
        System.out.println("2. Busqueda informada");

        Scanner scanner = new Scanner(System.in);
        int opcion = scanner.nextInt();

        switch (opcion) {
            case 1:
                System.out.println("Busqueda no informada");
                //función de busqueda no informada
                break;
            case 2:
                //AStarSearch()
            default:
                System.out.println("Opción inválida");
                break;
        }
        scanner.close();
    }
}