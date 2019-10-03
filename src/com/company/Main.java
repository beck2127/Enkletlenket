package com.company;

import java.util.*;




public class Main {

    public static void main(String[] args) {


        Liste<Integer> liste = new EnkeltLenketListe<>() {


        };

        System.out.println("Antall i listen: " + liste.antall());
        System.out.println("Listen inneholder: " + liste);

        for (int i = 0; i < 10; i++) liste.leggInn(i + 1);

        System.out.println("Antall i listen: " + liste.antall());
        System.out.println("Listen inneholder: " + liste);

        liste.nullstill();
        System.out.println("Antall i listen: " + liste.antall());

        for (int i = 0; i < 10; i++) liste.leggInn(liste.antall() / 2, i + 1);

        System.out.println("Antall i listen: " + liste.antall());
        System.out.println("Listen inneholder: " + liste);


    }



}



