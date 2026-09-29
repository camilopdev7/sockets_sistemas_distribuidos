import java.io.*;
import java.net.*;
import java.sql.*;

public class SocketServer {

	// credenciales usuario mysql 
	static final String URL = "jdbc:mysql://localhost:3306/sockets_exercise";
    static final String USUARIO = "javauser";
    static final String CLAVE = "Java123*";

    public static void main(String[] args) {

        int port = 5000;

        try (ServerSocket servidor = new ServerSocket(port)) {

            System.out.println("Servidor iniciado en el puerto " + port);

            while (true) {  
                System.out.println("\nEsperando conexión del cliente...");

                try (Socket cliente = servidor.accept();
                     BufferedReader entrada = new BufferedReader(
                             new InputStreamReader(cliente.getInputStream()));
                     PrintWriter salida = new PrintWriter(
                             cliente.getOutputStream(), true)) {

                    System.out.println("Cliente conectado: " + cliente.getInetAddress());

                    String telefono;
 
                    while ((telefono = entrada.readLine()) != null) {
                        System.out.println("Teléfono recibido: " + telefono);
                        String respuesta = buscarPersona(telefono.trim());
                        System.out.println("Respuesta enviada: " + respuesta);
                        salida.println(respuesta);
                    }

                    System.out.println("Cliente cerró la transacción.");

                } catch (IOException e) {
                    System.out.println("Error atendiendo cliente: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.out.println("Error en el servidor: " + e.getMessage());
        }
    }

    static String buscarPersona(String telefono) {

        String sql = "SELECT p.dir_tel, p.dir_nombre, p.dir_direccion, c.ciud_nombre "
                   + "FROM personas p "
                   + "INNER JOIN ciudades c ON p.dir_ciud_id = c.ciud_id "
                   + "WHERE p.dir_tel = ?";

        try (Connection con = DriverManager.getConnection(URL, USUARIO, CLAVE);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, telefono);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return "Teléfono: " + rs.getString("dir_tel")
                         + " | Nombre: " + rs.getString("dir_nombre")
                         + " | Dirección: " + rs.getString("dir_direccion")
                         + " | Ciudad: " + rs.getString("ciud_nombre");
                } else {
                    return "Persona dueña de ese número telefónico no existe";
                }
            }

        } catch (SQLException e) {
            System.out.println("Error MySQL: " + e.getMessage());
            return "Error consultando la base de datos";
        }
    }
}