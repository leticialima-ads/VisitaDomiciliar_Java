import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:saude.db";
    
    public static Connection connect() {
        try {
            Connection conn = DriverManager.getConnection(URL);
            criarTabela(conn);
            return conn;
        } catch (SQLException e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
            return null;
        }
    }
    
    private static void criarTabela(Connection conn) {
        String sql = "CREATE TABLE IF NOT EXISTS pacientes (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nome TEXT NOT NULL," +
                    "data_nascimento TEXT NOT NULL," +
                    "sexo TEXT NOT NULL," +
                    "microarea TEXT NOT NULL," +
                    "endereco TEXT NOT NULL," +
                    "acamado INTEGER," +
                    "diabetes INTEGER," +
                    "data_consulta_diabetes TEXT," +
                    "hipertensao INTEGER," +
                    "data_consulta_hipertensao TEXT," +
                    "preventivo_em_dia INTEGER," +
                    "data_ultimo_preventivo TEXT," +
                    "metodo_contraceptivo TEXT," +
                    "caderneta_em_dia INTEGER," +
                    "data_ultima_vacina TEXT," +
                    "interesse_vasectomia INTEGER)";
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabela verificada/criada com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabela: " + e.getMessage());
        }
    }
}