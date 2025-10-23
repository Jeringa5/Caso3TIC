package Caso3;

import java.util.List;

public class ManejadorCuarentena extends Thread {
    private final BuzonCuarentena cuarentena;
    private final BuzonLimitado entrega;

    public ManejadorCuarentena(BuzonCuarentena cuarentena, BuzonLimitado entrega) {
        super("Cuarentena");
        this.cuarentena = cuarentena;
        this.entrega = entrega;
    }

    @Override public void run() {
        boolean viFin = false;
        while (true) {
            try { Thread.sleep(1000); } catch (InterruptedException e) { interrupt(); }

            // Recolectar mensajes listos de la cuarentena
            List<Mensaje> listos = cuarentena.ticYRecolectarListos(getName());
            for (Mensaje m : listos) {
                if ("FIN".equals(m.getTipo())) { viFin = true; continue; }
                while (!entrega.intentarPoner(m, getName())) {
                    Thread.yield();
                }
            }

            if (viFin && cuarentena.estaVacio()) break;
        }
    }
}
