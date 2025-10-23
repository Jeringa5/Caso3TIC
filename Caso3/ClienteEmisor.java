package Caso3;

import java.util.Random;

public class ClienteEmisor extends Thread {
    private final int idCliente;
    private final int mensajesPorGenerar;
    private final BuzonLimitado buzonEntrada;
    private final Coordinador coord;
    private final Random azar = new Random();

    public ClienteEmisor(int idCliente, int mensajesPorGenerar, BuzonLimitado buzonEntrada, Coordinador coord) {
        super("Cliente-" + idCliente);
        this.idCliente = idCliente;
        this.mensajesPorGenerar = mensajesPorGenerar;
        this.buzonEntrada = buzonEntrada;
        this.coord = coord;
    }

    @Override public void run() {
        // Enviar mensaje de inicio
        buzonEntrada.ponerBloqueante(new Mensaje("INICIO", idCliente, 0, false), getName());
        coord.registrarInicio();

        // Random spam
        for (int i = 1; i <= mensajesPorGenerar; i++) {
            boolean esSpam = azar.nextBoolean();
            buzonEntrada.ponerBloqueante(new Mensaje("DATO", idCliente, i, esSpam), getName());
        }

        buzonEntrada.ponerBloqueante(new Mensaje("FIN", idCliente, 0, false), getName());
        coord.registrarFin();
    }
}
