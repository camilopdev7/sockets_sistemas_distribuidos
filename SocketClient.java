import java.io.*;
import java.net.*;
import java.util.Scanner;

public class SocketClient {

    public static void main(String[] args) {

        String ipServer = "192.168.10.10";
        int port = 5000;
        int consultas = 3;

        Scanner teclado = new Scanner(System.in);

        try (Socket socket = new Socket(ipServer, port);
             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader entrada = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Conectado al servidor " + ipServer + ":" + port);

            for (int i = 1; i <= consultas; i++) {
                System.out.print("\nConsulta " + i + " - Ingrese el número de teléfono: ");
                String telefono = teclado.nextLine();

                salida.println(telefono);

                String respuesta = entrada.readLine();
                System.out.println("Resultado: " + respuesta);
            }

            System.out.println("\nTransacción finalizada. Cerrando cliente.");

        } catch (IOException e) {
            System.out.println("Error en el cliente: " + e.getMessage());
        }

        teclado.close();
    }
}