import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RepositorioPacientesArquivo {
    private List<Paciente> pacientes;
    private static final String ARQUIVO = "pacientes.txt";
    
    public RepositorioPacientesArquivo() {
        this.pacientes = new ArrayList<>();
        carregar();
    }
    
    private void carregar() {
        File file = new File(ARQUIVO);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO))) {
                String linha;
                while ((linha = reader.readLine()) != null) {
                    if (!linha.trim().isEmpty()) {
                        Paciente p = Paciente.fromFileString(linha);
                        pacientes.add(p);
                    }
                }
            } catch (IOException e) {
                System.out.println("Erro ao carregar: " + e.getMessage());
            }
        }
        System.out.println("Carregados " + pacientes.size() + " pacientes");
    }
    
    public void cadastrar(Paciente paciente) {
        pacientes.add(paciente);
        salvarTodos();
    }
    
    public void excluirPaciente(Paciente paciente) {
        pacientes.remove(paciente);
        salvarTodos();
    }
    
    public void salvarTodos() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO))) {
            for (Paciente p : pacientes) {
                writer.write(p.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar: " + e.getMessage());
        }
    }
    
    public List<Paciente> listarTodos() {
        return new ArrayList<>(pacientes);
    }
}