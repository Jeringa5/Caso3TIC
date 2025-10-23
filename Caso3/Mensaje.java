package Caso3;

public class Mensaje {
    private final String tipo;
    private final int idCliente;
    private final int secuencial;
    private final boolean esSpam;

    public Mensaje(String tipo, int idCliente, int secuencial, boolean esSpam) {
        this.tipo = tipo;
        this.idCliente = idCliente;
        this.secuencial = secuencial;
        this.esSpam = esSpam;
    }

    public String getTipo() { return tipo; }
    public int getIdCliente() { return idCliente; }
    public int getSecuencial() { return secuencial; }
    public boolean esSpam() { return esSpam; }

    /*
     * Representación en cadena del mensaje
     */
    @Override public String toString() {
        return switch (tipo) {
            case "INICIO" -> "INICIO(cliente=" + idCliente + ")";
            case "FIN"    -> "FIN(cliente=" + idCliente + ")";
            default       -> "DATO(cliente=" + idCliente + ", #" + secuencial + ", spam=" + esSpam + ")";
        };
    }
}