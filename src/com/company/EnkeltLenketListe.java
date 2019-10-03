package com.company;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public abstract class EnkeltLenketListe<T> implements Liste<T> {



    private static final class Node<T>       // en indre nodeklasse
    {
        private T verdi;                       // nodens verdi
        private Node<T> neste;                 // den neste noden

        private Node(T verdi,Node<T> neste)    // konstruktør
        {
            this.verdi = verdi;
            this.neste = neste;
        }
    }  // Node

    private Node<T> hode, hale;  // pekere til første og siste node

    private int antall;
    private int endringer;
    // antall verdier/noder i listen

    private Node<T> finnNode(int indeks)
    {
        Node<T> p = hode;
        for (int i = 0; i < indeks; i++) p = p.neste;
        return p;
    }

    public EnkeltLenketListe()   // standardkonstruktør
    {
        hode = hale = null;        // hode og hale til null
        antall = 0;

        // ingen verdier - listen er tom
    }

    public EnkeltLenketListe(T[] a)
    {
        this();

        if (a.length == 0) return;  // ingen verdier - tom liste

        hode = hale = new Node<>(a[a.length-1], null);  // den siste noden

        for (int i = a.length - 2; i >= 0; i--)  // resten av verdiene
        {
            hode = new Node<>(a[i], hode);
        }

        antall = a.length;
    }

    @Override
    public boolean leggInn(T verdi)   // verdi legges bakerst
    {
        Objects.requireNonNull(verdi, "Ikke tillatt med null-verdier!");

        if (antall == 0)  hode = hale = new Node<>(verdi, null);  // tom liste
        else hale = hale.neste = new Node<>(verdi, null);         // legges bakerst

        antall++;        // en mer i listen
        return true;     // vellykket innlegging
    }

    @Override
    public void leggInn(int indeks, T verdi)    // verdi til posisjon indeks
    {
        Objects.requireNonNull(verdi, "Ikke tillatt med null-verdier!");

        indeksKontroll(indeks, true);        // true: indeks = antall er lovlig

        if (indeks == 0)                     // ny verdi skal ligge først
        {
            hode = new Node<>(verdi, hode);    // legges først
            if (antall == 0) hale = hode;      // hode og hale peker på samme node
        }
        else if (indeks == antall)           // ny verdi skal ligge bakerst
        {
            hale = hale.neste = new Node<>(verdi, null);  // legges bakerst
        }
        else
        {
            Node<T> p = hode;                  // p flyttes indeks - 1 ganger
            for (int i = 1; i < indeks; i++) p = p.neste;

            p.neste = new Node<>(verdi, p.neste);  // verdi settes inn i listen
        }

        antall++;                            // listen har fått en ny verdi
    }

    @Override
    public int indeksTil(T verdi)
    {
        if (verdi == null) return -1;

        Node<T> p = hode;

        for (int indeks = 0; indeks < antall ; indeks++)
        {
            if (p.verdi.equals(verdi)) return indeks;
            p = p.neste;
        }
        return -1;
    }

    @Override
    public boolean inneholder(T verdi)
    {
        return indeksTil(verdi) != -1;
    }

    @Override
    public T hent(int indeks)
    {
        indeksKontroll(indeks, false);  // false: indeks = antall er ulovlig
        return finnNode(indeks).verdi;
    }

    @Override
    public T oppdater(int indeks, T verdi)
    {
        Objects.requireNonNull(verdi, "Ikke tillatt med null-verdier!");

        indeksKontroll(indeks, false);  // false: indeks = antall er ulovlig

        Node<T> p = finnNode(indeks);
        T gammelVerdi = p.verdi;

        p.verdi = verdi;
        return gammelVerdi;
    }

    @Override
    public T fjern(int indeks)
    {
        indeksKontroll(indeks, false);  // false: indeks = antall er ulovlig

        T temp;                              // hjelpevariabel

        if (indeks == 0)                     // skal første verdi fjernes?
        {
            temp = hode.verdi;                 // tar vare på verdien som skal fjernes
            hode = hode.neste;                 // hode flyttes til neste node

            if (antall == 1) hale = null;      // det var kun en verdi i listen
        }
        else
        {
            Node<T> p = finnNode(indeks - 1);  // p er noden foran den som skal fjernes
            Node<T> q = p.neste;               // q skal fjernes

            temp = q.verdi;                    // tar vare på verdien som skal fjernes

            if (q == hale) hale = p;           // q er siste node

            p.neste = q.neste;                 // "hopper over" q
        }

        antall--;                            // reduserer antallet

        return temp;                         // returner fjernet verdi
    }

    @Override
    public boolean fjern(T verdi)               // verdi skal fjernes
    {
        if (verdi == null) return false;          // ingen nullverdier i listen

        Node<T> q = hode, p = null;               // hjelpepekere

        while (q != null)                         // q skal finne verdien t
        {
            if (q.verdi.equals(verdi)) break;       // verdien funnet
            p = q; q = q.neste;

            // p er forgjengeren til q

            ////
        }

        if (q == null) return false;              // fant ikke verdi
        else if (q == hode) hode = hode.neste;    // går forbi q
        else p.neste = q.neste;                   // går forbi q

        if (q == hale) hale = p;                  // oppdaterer hale

        q.verdi = null;                           // nuller verdien til q
        q.neste = null;                           // nuller nestepeker

        antall--;                                 // en node mindre i listen

        return true;                              // vellykket fjerning
    }

    @Override
    public int antall()
    {
        return antall;
    }

    @Override
    public boolean tom()
    {
        return antall == 0;
    }

    @Override
    public void nullstill()
    {
        Node<T> p = hode, q;

        while (p != null)
        {
            q = p.neste;
            p.neste = null;
            p.verdi = null;
            p = q;
        }

        hode = hale = null;
        antall = 0;
    }

    @Override
    public Iterator<T> iterator()
    {
        return new EnkeltLenketListeIterator();
    }

    private class EnkeltLenketListeIterator implements Iterator<T>
    {
        private Node<T> p = hode;         // p starter på den første i listen
        private boolean fjernOK = false;  // blir sann når next() kalles
        private int iteratorendringer = endringer;  // startverdi

        @Override
        public boolean hasNext()
        {
            return p != null;  // p er ute av listen hvis den har blitt null
        }

        @Override
        public T next()
        {
            if (endringer != iteratorendringer)
                throw new ConcurrentModificationException("Listen er endret!");

            if (!hasNext()) throw new
                    NoSuchElementException("Tomt eller ingen verdier igjen!");

            fjernOK = true;            // nå kan remove() kalles

            T denneVerdi = p.verdi;    // tar vare på verdien i p
            p = p.neste;               // flytter p til den neste noden

            return denneVerdi;         // returnerer verdien
        }

        @Override
        public void remove()
        {
            if (endringer != iteratorendringer)
                throw new ConcurrentModificationException("Listen er endret!");


            if (!fjernOK) throw new IllegalStateException("Ulovlig tilstand!");

            fjernOK = false;               // remove() kan ikke kalles på nytt
            Node<T> q = hode;              // hjelpepeker

            if (hode.neste == p)           // skal den første fjernes?
            {
                hode = hode.neste;           // den første fjernes
                if (p == null) hale = null;  // dette var den eneste noden
            }
            else
            {
                Node<T> r = hode;            // må finne forgjengeren
                // til forgjengeren til p
                while (r.neste.neste != p)
                {
                    r = r.neste;               // flytter r
                }

                q = r.neste;                 // det er q som skal fjernes
                r.neste = p;                 // "hopper" over q
                if (p == null) hale = r;     // q var den siste
            }

            q.verdi = null;                // nuller verdien i noden
            q.neste = null;                // nuller nestepeker

            endringer++;             // en endring i listen
            iteratorendringer++;    // en endring av denne iteratoren
            antall--;                      // en node mindre i listen
        }
    } // EnkeltLenketListeIterator

  // EnkeltLenketListe


    @Override
    public String toString()
    {
        StringBuilder s = new StringBuilder();

        s.append('[');

        if (!tom())
        {
            Node<T> p = hode;
            s.append(p.verdi);

            p = p.neste;

            while (p != null)  // tar med resten hvis det er noe mer
            {
                s.append(',').append(' ').append(p.verdi);
                p = p.neste;
            }
        }

        s.append(']');

        return s.toString();
    }





}