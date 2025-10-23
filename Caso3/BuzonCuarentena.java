package Caso3;

import java.util.*;

public class BuzonCuarentena {
    private static class Entrada {
        Mensaje mensaje; int segundosRestantes;
        Entrada(Mensaje m, int s) { mensaje = m; segundosRestantes = s; }
    }
    private final List<Entrada> lista = new ArrayList<>();
    private final Random azar = new Random();

    // Pone un mensaje en cuarentena por el tiempo especificado en segundos.
    public synchronized void poner(Mensaje m, int segundos, String quien) {
        lista.add(new Entrada(m, segundos));
        log(quien + " envió a CUARENTENA " + m + " (" + segundos + " s)");
    }

    /*
     * Decrementa en 1 segundo el tiempo restante de todos los mensajes en cuarentena.
     */
    public synchronized List<Mensaje> ticYRecolectarListos(String quien) {
        List<Mensaje> listos = new ArrayList<>();
        Iterator<Entrada> it = lista.iterator();
        // Recorremos la lista de entradas en cuarentena
        while (it.hasNext()) {
            Entrada e = it.next();
            e.segundosRestantes = Math.max(0, e.segundosRestantes - 1);
            if (e.segundosRestantes == 0) {
                if ("FIN".equals(e.mensaje.getTipo())) {
                    listos.add(e.mensaje);
                    it.remove();
                    continue;
                }
                // Simular posible descarte por malicioso
                int k = 1 + azar.nextInt(21);
                if (k % 7 == 0) {
                    log(quien + " DESCARTA por malicioso " + e.mensaje + " (k=" + k + ")");
                } else {
                    listos.add(e.mensaje);
                }
                it.remove();
            }
        }
        return listos;
    }

    public synchronized boolean estaVacio() { return lista.isEmpty(); }

    private static void log(String s) { System.out.println("[" + Thread.currentThread().getName() + "] " + s); }
}
