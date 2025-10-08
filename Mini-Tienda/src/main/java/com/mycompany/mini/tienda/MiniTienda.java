package com.mycompany.mini.tienda;

import java.util.*;
import javax.swing.JOptionPane;

public class MiniTienda {

   // Estructuras de datos
    private static ArrayList<String> nombres = new ArrayList<>();
    private static double[] precios = new double[0];
    private static HashMap<String, Integer> stock = new HashMap<>();
    private static double totalCompras = 0.0;

    public static void main(String[] args) {
        String[] opciones = {
            "Agregar producto",
            "Listar inventario",
            "Comprar producto",
            "Mostrar estadísticas (más barato y más caro)",
            "Buscar producto por nombre",
            "Salir con ticket final"
        };

        int opcion;
        do {
            String seleccion = (String) JOptionPane.showInputDialog(
                null,
                "Selecciona una opción:",
                "Mini-Tienda - Menú Principal",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
            );

            if (seleccion == null) {
                // Usuario cerró la ventana → salir
                mostrarTicketYTerminar();
                return;
            }

            opcion = Arrays.asList(opciones).indexOf(seleccion);

            switch (opcion) {
                case 0 -> agregarProducto();
                case 1 -> listarInventario();
                case 2 -> comprarProducto();
                case 3 -> mostrarEstadisticas();
                case 4 -> buscarProducto();
                case 5 -> {
                    mostrarTicketYTerminar();
                    return;
                }
                default -> JOptionPane.showMessageDialog(null, "Opción no válida.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } while (true);
    }

    // === TASK 1: Modelo de datos ===
    private static void addProducto(String nombre, double precio, int cantidad) {
        if (nombres.contains(nombre)) {
            JOptionPane.showMessageDialog(null, "El producto ya existe.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        nombres.add(nombre);
        expandPrecios(precio);
        stock.put(nombre, cantidad);
        JOptionPane.showMessageDialog(null, "Producto agregado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void expandPrecios(double nuevoPrecio) {
        double[] nuevoArray = Arrays.copyOf(precios, precios.length + 1);
        nuevoArray[nuevoArray.length - 1] = nuevoPrecio;
        precios = nuevoArray;
    }

    private static int indexOfNombre(String nombre) {
        return nombres.indexOf(nombre);
    }

    // === TASK 2 + 3 + 4: Funcionalidades completas con validaciones ===
    private static void agregarProducto() {
        try {
            String nombre = JOptionPane.showInputDialog("Ingresa el nombre del producto:");
            if (nombre == null || nombre.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nombre inválido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            nombre = nombre.trim();

            String precioStr = JOptionPane.showInputDialog("Ingresa el precio del producto:");
            if (precioStr == null || precioStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Precio inválido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double precio = Double.parseDouble(precioStr.trim());
            if (precio <= 0) {
                JOptionPane.showMessageDialog(null, "El precio debe ser mayor a 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String stockStr = JOptionPane.showInputDialog("Ingresa la cantidad en stock:");
            if (stockStr == null || stockStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Stock inválido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int cantidad = Integer.parseInt(stockStr.trim());
            if (cantidad < 0) {
                JOptionPane.showMessageDialog(null, "El stock no puede ser negativo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            addProducto(nombre, precio, cantidad);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Formato numérico inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void listarInventario() {
        if (nombres.isEmpty()) {
            JOptionPane.showMessageDialog(null, "El inventario está vacío.", "Inventario", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("Inventario:\n\n");
        for (int i = 0; i < nombres.size(); i++) {
            String nombre = nombres.get(i);
            double precio = precios[i];
            int cantidad = stock.get(nombre);
            sb.append(String.format("- %s | Precio: $%.2f | Stock: %d\n", nombre, precio, cantidad));
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Inventario", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void comprarProducto() {
        if (nombres.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay productos en el inventario.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nombre = JOptionPane.showInputDialog("Ingresa el nombre del producto a comprar:");
        if (nombre == null || nombre.trim().isEmpty()) {
            return;
        }
        nombre = nombre.trim();

        int index = indexOfNombre(nombre);
        if (index == -1) {
            JOptionPane.showMessageDialog(null, "Producto no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String cantidadStr = JOptionPane.showInputDialog("¿Cuántas unidades deseas comprar?");
            if (cantidadStr == null || cantidadStr.trim().isEmpty()) {
                return;
            }
            int cantidad = Integer.parseInt(cantidadStr.trim());
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(null, "La cantidad debe ser mayor a 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int stockActual = stock.get(nombre);
            if (cantidad > stockActual) {
                JOptionPane.showMessageDialog(null,
                    String.format("Stock insuficiente. Solo hay %d unidades disponibles.", stockActual),
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double precioTotal = precios[index] * cantidad;
            int confirm = JOptionPane.showConfirmDialog(
                null,
                String.format("¿Confirmar compra de %d unidades de %s por $%.2f?", cantidad, nombre, precioTotal),
                "Confirmar Compra",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                stock.put(nombre, stockActual - cantidad);
                totalCompras += precioTotal;
                JOptionPane.showMessageDialog(null,
                    String.format("Compra realizada con éxito.\nTotal: $%.2f", precioTotal),
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Cantidad inválida.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void mostrarEstadisticas() {
        if (precios.length == 0) {
            JOptionPane.showMessageDialog(null, "No hay productos para mostrar estadísticas.", "Estadísticas", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        double min = precios[0], max = precios[0];
        String nombreMin = nombres.get(0), nombreMax = nombres.get(0);

        for (int i = 1; i < precios.length; i++) {
            if (precios[i] < min) {
                min = precios[i];
                nombreMin = nombres.get(i);
            }
            if (precios[i] > max) {
                max = precios[i];
                nombreMax = nombres.get(i);
            }
        }

        String mensaje = String.format(
            "Producto más barato: %s - $%.2f\nProducto más caro: %s - $%.2f",
            nombreMin, min, nombreMax, max
        );
        JOptionPane.showMessageDialog(null, mensaje, "Estadísticas", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void buscarProducto() {
        String busqueda = JOptionPane.showInputDialog("Ingresa parte del nombre del producto:");
        if (busqueda == null || busqueda.trim().isEmpty()) {
            return;
        }
        busqueda = busqueda.trim().toLowerCase();

        List<String> coincidencias = new ArrayList<>();
        for (String nombre : nombres) {
            if (nombre.toLowerCase().contains(busqueda)) {
                coincidencias.add(nombre);
            }
        }

        if (coincidencias.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se encontraron productos.", "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder sb = new StringBuilder("Productos encontrados:\n\n");
            for (String nombre : coincidencias) {
                int idx = indexOfNombre(nombre);
                sb.append(String.format("- %s | Precio: $%.2f | Stock: %d\n", nombre, precios[idx], stock.get(nombre)));
            }
            JOptionPane.showMessageDialog(null, sb.toString(), "Resultados de Búsqueda", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static void mostrarTicketYTerminar() {
        String mensaje = totalCompras > 0
            ? String.format("Gracias por tu compra.\nTotal gastado en esta sesión: $%.2f", totalCompras)
            : "No realizaste ninguna compra. ¡Vuelve pronto!";
        JOptionPane.showMessageDialog(null, mensaje, "Ticket Final", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }
}
