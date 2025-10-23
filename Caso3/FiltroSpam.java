package Caso3;

import java.util.Random;

public class FiltroSpam extends Thread {
    private final BuzonLimitado buzonEntrada;      // limitado (pasiva)
    private final BuzonCuarentena buzonCuarentena; // ilimitado
    private final BuzonLimitado buzonEntrega;      // limitado (semi-activa para escribir)
    private final Coordinador coord;
    private final Random azar = new Random();

    public FiltroSpam(int id, BuzonLimitado entrada, BuzonCuarentena cuarentena,
                      BuzonLimitado entrega, Coordinador coord) {
        super("Filtro-" + id);
        this.buzonEntrada = entrada;
        this.buzonCuarentena = cuarentena;
        this.buzonEntrega = entrega;
        this.coord = coord;
    }

    // Filtro actua como consumidor de entrada y productor de entrega
    @Override public void run() {
        while (true) {
            Mensaje m = buzonEntrada.tomarBloqueante(getName());
            if (m == null) break;

            // Procesamiento del mensaje
            switch (m.getTipo()) {
                case "INICIO" -> { }
                case "DATO" -> {
                    if (m.esSpam()) {
                        int segundos = 10 + azar.nextInt(11); // 10..20
                        buzonCuarentena.poner(m, segundos, getName());
                    } else {
                        while (!buzonEntrega.intentarPoner(m, getName())) {
                            Thread.yield();
                        }
                    }
                }
                case "FIN" -> { }
            }

            // Verificaciones de fin de trabajo
            if (coord.puedeEnviarFinEntrega(buzonEntrada, buzonCuarentena)) {
                buzonEntrega.ponerBloqueante(new Mensaje("FIN", -1, 0, false), getName());
                coord.marcarFinEntregaEnviado();
            }
            if (coord.puedeEnviarFinCuarentena(buzonEntrada)) {
                buzonCuarentena.poner(new Mensaje("FIN", -1, 0, false), 1, getName());
                coord.marcarFinCuarentenaEnviado();
            }

            if (coord.todosClientesFinalizaron() && buzonEntrada.estaVacio()) break;
        }
    }
}
