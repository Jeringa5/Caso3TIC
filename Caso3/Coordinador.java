package Caso3;

public class Coordinador {
    private final int numeroClientes;
    private int iniciosVistos = 0;
    private int finesVistos = 0;
    private boolean finEntregaEnviado = false;
    private boolean finCuarentenaEnviado = false;

    public Coordinador(int numeroClientes) { this.numeroClientes = numeroClientes; }

    // Sincronización para el registro de inicios y fines
    public synchronized void registrarInicio() { iniciosVistos++; }
    public synchronized void registrarFin() { finesVistos++; }

    // Verificación de si todos los clientes han finalizado
    public synchronized boolean todosClientesFinalizaron() { return finesVistos >= numeroClientes; }

    // Verificación y marcado de envío de mensajes de fin
    public synchronized boolean puedeEnviarFinEntrega(BuzonLimitado entrada, BuzonCuarentena cuarentena) {
        return !finEntregaEnviado && todosClientesFinalizaron() && entrada.estaVacio() && cuarentena.estaVacio();
    }

    // Marcar que se ha enviado el mensaje de fin de entrega
    public synchronized void marcarFinEntregaEnviado() { finEntregaEnviado = true; }

    // Verificación y marcado de envío de mensajes de fin de cuarentena
    public synchronized boolean puedeEnviarFinCuarentena(BuzonLimitado entrada) {
        return !finCuarentenaEnviado && todosClientesFinalizaron() && entrada.estaVacio();
    }

    // Marcar que se ha enviado el mensaje de fin de cuarentena
    public synchronized void marcarFinCuarentenaEnviado() { finCuarentenaEnviado = true; }
}
