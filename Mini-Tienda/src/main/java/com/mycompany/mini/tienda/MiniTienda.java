package com.mycompany.mini.tienda;

import java.util.*;
import javax.swing.JOptionPane;

public class MiniTienda {

    // Estructuras de datos
    private static ArrayList<String> nombres = new ArrayList<>();
    private static double[] precios = new double[0];
    private static HashMap<String, Integer> stock = new HashMap<>();
    private static double totalCompras = 0.0;

    // Métodos utilitarios
    private static void addProducto(String nombre, double precio, int cantidad) {
        if (nombres.contains(nombre)) {
            JOptionPane.showMessageDialog(null, "El producto ya existe.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        nombres.add(nombre);
        expandPrecios(precio);
        stock.put(nombre, cantidad);
    }

    private static void expandPrecios(double nuevoPrecio) {
        double[] nuevoArray = Arrays.copyOf(precios, precios.length + 1);
        nuevoArray[nuevoArray.length - 1] = nuevoPrecio;
        precios = nuevoArray;
    }

    private static int indexOfNombre(String nombre) {
        return nombres.indexOf(nombre);
    }
}
