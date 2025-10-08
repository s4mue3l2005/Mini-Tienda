package com.mycompany.mini.tienda;

public class Alimento extends Producto {
    public Alimento(String nombre, double precio) {
        super(nombre, precio);
    }

    @Override
    public String getDescripcion() {
        return "Alimento perecedero. Consumir preferentemente antes de la fecha indicada.";
    }
}