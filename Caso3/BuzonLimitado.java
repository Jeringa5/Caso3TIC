package Caso3;

import java.util.ArrayDeque;
import java.util.Queue;

public class BuzonLimitado {
    private final int capacidad;
    private final Queue<Mensaje> cola = new ArrayDeque<>();

    public BuzonLimitado(int capacidad) { this.capacidad = capacidad; }

    /**
     * Inserta un mensaje en el búfer usando espera pasiva.
     * Si el búfer está lleno, el hilo se bloquea con wait() hasta que haya espacio disponible.
     */
    public synchronized void ponerBloqueante(Mensaje m, String quien) {
        while (cola.size() == capacidad) {
            log(quien + " espera: búfer lleno");
            try { wait(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); return; }
        }
        cola.add(m);
        log(quien + " puso " + m);
        notifyAll();
    }

    /**
     * Extrae un mensaje del búfer usando espera pasiva.
     * Si el búfer está vacío, el hilo se bloquea hasta que otro hilo coloque un mensaje.
     */
    public synchronized Mensaje tomarBloqueante(String quien) {
        while (cola.isEmpty()) {
            try { wait(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); return null; }
        }
        Mensaje m = cola.remove();
        log(quien + " tomó " + m);
        notifyAll();
        return m;
    }

    /**
     * Inserta un mensaje de manera no bloqueante (espera semi-activa).
     * Si el búfer está lleno, devuelve false y el hilo puede ceder CPU con yield().
     */
    public synchronized boolean intentarPoner(Mensaje m, String quien) {
        if (cola.size() == capacidad) return false;
        cola.add(m);
        log(quien + " puso " + m + " (tryPut)");
        notifyAll();
        return true;
    }

    /**
     * Extrae un mensaje de manera no bloqueante (espera semi-activa).
     * Si el búfer está vacío, devuelve null y el hilo puede ceder CPU con yield().
     */
    public synchronized Mensaje intentarTomar(String quien) {
        if (cola.isEmpty()) return null;
        Mensaje m = cola.remove();
        log(quien + " tomó " + m + " (tryTake)");
        notifyAll();
        return m;
    }

    /**
     * Devuelve true si el búfer no contiene ningún mensaje.
     */
    public synchronized boolean estaVacio() { return cola.isEmpty(); }

    /**
     * Retorna la capacidad máxima del búfer.
     */
    public int capacidad() { return capacidad; }

    /**
     * Muestra mensajes de depuración con el nombre del hilo actual.
     */
    private static void log(String s) { System.out.println("[" + Thread.currentThread().getName() + "] " + s); }
}
