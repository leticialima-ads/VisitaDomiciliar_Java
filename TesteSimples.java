import java.sql.*;

public class TesteSimples {
    public static void main(String[] args) {
        System.out.println("Testando driver SQLite...");
        
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("Driver encontrado!");
            
            Connection conn = DriverManager.getConnection("jdbc:sqlite:teste.db");
            System.out.println("Conexao ok!");
            
            conn.close();
            System.out.println("SUCESSO!");
            
        } catch (ClassNotFoundException e) {
            System.out.println("Driver nao encontrado!");
        } catch (SQLException e) {
            System.out.println("Erro SQL: " + e.getMessage());
        }
    }
}