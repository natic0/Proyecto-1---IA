# Proyecto: SMART CAR WORLD

## Autores
Angie Natalia Cordoba Collazos - 2124366
Laura Murillas Andrade - 201944153

## Descripción

Este proyecto fue desarrollado como parte de la materia de **Inteligencia Artificial**, donde se implementaron diferentes algoritmos de búsqueda tanto informada como no informada. El objetivo del proyecto es simular un vehículo inteligente que debe recoger a un pasajero y llevarlo a su destino, utilizando diferentes estrategias de búsqueda.

## Algoritmos Implementados

### Búsqueda No Informada
- **Búsqueda en Amplitud**: Recorre todos los nodos a un nivel antes de pasar al siguiente.
- **Búsqueda en Profundidad evitando ciclos**: Explora tanto como sea posible antes de retroceder, pero previene ciclos para evitar caminos repetidos.
- **Búsqueda de Costo Uniforme**: Elige siempre el nodo con el menor costo acumulado hasta el momento.

### Búsqueda Informada
- **A***: Utiliza una función heurística y el costo real acumulado para buscar el camino óptimo.
- **Búsqueda Avara**: Usa solo la función heurística, guiándose por la estimación de cuán cerca está del destino.

## Funcionamiento del Proyecto

Para ejecutar el proyecto, simplemente sigue estos pasos:

1. Ejecuta el archivo `SmartCarWorld.java`. Este archivo contiene la simulación del mundo en el que opera el vehículo inteligente.
2. Selecciona el tipo de búsqueda que deseas utilizar:
   - **Búsqueda No Informada** (Amplitud, Profundidad evitando ciclos, Costo Uniforme).
   - **Búsqueda Informada** (A*, Avara).
3. Puedes personalizar el "mundo" del vehículo, donde se definirán las posiciones iniciales del vehículo, del pasajero y del destino. Esto se puede hacer cargando el mapa desde la interfaz de `SmartCarWorld.java`.

## Requisitos

- Java 8 o superior.
- IDE compatible con proyectos Java o un entorno que permita ejecutar archivos `.java`.

## Cómo Ejecutar el Proyecto

1. Clona el repositorio:

   ```bash
   git clone https://github.com/tu-usuario/nombre-del-repo.git

2. Compila el archivo SmartCarWorld.java:
    
    ```bash
    javac SmartCarWorld.java

3. Ejecuta el programa:

    ```bash
    java SmartCarWorld

