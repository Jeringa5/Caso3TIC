package Caso3;

import java.util.Random;

public class ServidorEntrega extends Thread {
    private final BuzonLimitado buzonEntrega;
    private final Random azar = new Random();

    public ServidorEntrega(int id, BuzonLimitado entrega) {
        super("Servidor-" + id);
        this.buzonEntrega = entrega;
    }

    @Override public void run() {
        // Tomar mensajes del buzón de entrega e "entregarlos"
        while (true) {
            Mensaje m;
            // Intentar tomar un mensaje
            while ((m = buzonEntrega.intentarTomar(getName())) == null) {
                Thread.yield();
            }
            if ("FIN".equals(m.getTipo())) break;

            try { Thread.sleep(500 + azar.nextInt(1500)); } catch (InterruptedException e) { interrupt(); }
            System.out.println("[" + getName() + "] entregó " + m);
        }
    }
}
