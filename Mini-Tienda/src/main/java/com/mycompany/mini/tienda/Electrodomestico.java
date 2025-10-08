/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mini.tienda;

/**
 *
 * @author samya
 */
public class Electrodomestico extends Producto {
    public Electrodomestico(String nombre, double precio) {
        super(nombre, precio);
    }

    @Override
    public String getDescripcion() {
        return "Electrodoméstico con garantía de 1 año.";
    }
}
