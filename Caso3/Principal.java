package Caso3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Principal {
    public static void main(String[] args) {
        String rutaArchivo = "Caso3/configuracion.txt";

        int numeroClientes = 0;
        int mensajesPorCliente = 0;
        int numeroFiltros = 0;
        int numeroServidores = 0;
        int capacidadEntrada = 0;
        int capacidadEntrega = 0;

        try (Scanner lector = new Scanner(new File(rutaArchivo))) {
            numeroClientes = lector.nextInt();
            mensajesPorCliente = lector.nextInt();
            numeroFiltros = lector.nextInt();
            numeroServidores = lector.nextInt();
            capacidadEntrada = lector.nextInt();
            capacidadEntrega = lector.nextInt();
        } catch (FileNotFoundException e) {
            System.err.println("No se encontró el archivo de configuración: " + rutaArchivo);
            return;
        } catch (Exception e) {
            System.err.println("Error al leer el archivo de configuración. Verifica el formato.");
            e.printStackTrace();
            return;
        }

        System.out.println("=== INICIO SISTEMA DE MENSAJERÍA ===\n");
        System.out.printf(
            "Clientes=%d, msgs/cliente=%d, Filtros=%d, Servidores=%d, CapEntrada=%d, CapEntrega=%d%n%n",
            numeroClientes, mensajesPorCliente, numeroFiltros, numeroServidores, capacidadEntrada, capacidadEntrega);

        Coordinador coord = new Coordinador(numeroClientes);

        BuzonLimitado buzonEntrada = new BuzonLimitado(capacidadEntrada);
        BuzonCuarentena buzonCuarentena = new BuzonCuarentena();
        BuzonLimitado buzonEntrega = new BuzonLimitado(capacidadEntrega);

        Thread[] clientes = new Thread[numeroClientes];
        for (int i = 0; i < numeroClientes; i++) {
            clientes[i] = new ClienteEmisor(i+1, mensajesPorCliente, buzonEntrada, coord);
            clientes[i].start();
        }

        Thread[] filtros = new Thread[numeroFiltros];
        for (int i = 0; i < numeroFiltros; i++) {
            filtros[i] = new FiltroSpam(i+1, buzonEntrada, buzonCuarentena, buzonEntrega, coord);
            filtros[i].start();
        }

        Thread manejador = new ManejadorCuarentena(buzonCuarentena, buzonEntrega);
        manejador.start();

        Thread[] servidores = new Thread[numeroServidores];
        for (int i = 0; i < numeroServidores; i++) {
            servidores[i] = new ServidorEntrega(i+1, buzonEntrega);
            servidores[i].start();
        }

        try {
            for (Thread c : clientes) c.join();
            for (Thread f : filtros) f.join();
            manejador.join();

            for (int i = 0; i < numeroServidores; i++) {
                buzonEntrega.ponerBloqueante(new Mensaje("FIN", -1, 0, false), "Principal");
            }
            for (Thread s : servidores) s.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n=== PROGRAMA FINALIZADO ===");
    }
}
