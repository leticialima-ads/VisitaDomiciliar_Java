import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Paciente {
    private String nome;
    private LocalDate dataNascimento;
    private String sexo;
    private String cpf;
    private String microarea;
    private String endereco;
    private List<VisitaDomiciliar> historicoVisitas;
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Paciente(String nome, LocalDate dataNascimento, String sexo, String cpf, String microarea, String endereco) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.cpf = cpf;
        this.microarea = microarea;
        this.endereco = endereco;
        this.historicoVisitas = new ArrayList<>();
    }

    // Getters
    public String getNome() { return nome; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public String getSexo() { return sexo; }
    public String getCpf() { return cpf; }
    public String getMicroarea() { return microarea; }
    public String getEndereco() { return endereco; }
    public List<VisitaDomiciliar> getHistoricoVisitas() { return historicoVisitas; }
    public void adicionarVisita(VisitaDomiciliar visita) { this.historicoVisitas.add(visita); }

    public int getIdade() {
        return Period.between(dataNascimento, LocalDate.now()).getYears();
    }

    public VisitaDomiciliar getUltimaVisita() {
        if (historicoVisitas.isEmpty()) return null;
        return historicoVisitas.get(historicoVisitas.size() - 1);
    }

    // Condicoes de saude baseadas na ultima visita
    public boolean isDiabetes() {
        VisitaDomiciliar ultima = getUltimaVisita();
        return ultima != null && ultima.isDiabetes();
    }

    public boolean isHipertensao() {
        VisitaDomiciliar ultima = getUltimaVisita();
        return ultima != null && ultima.isHipertensao();
    }

    public boolean isConsultaDiabetesAtrasada() {
        VisitaDomiciliar ultima = getUltimaVisita();
        if (ultima == null || !ultima.isDiabetes()) return false;
        return ultima.isConsultaDiabetesAtrasada();
    }

    public boolean isConsultaHipertensaoAtrasada() {
        VisitaDomiciliar ultima = getUltimaVisita();
        if (ultima == null || !ultima.isHipertensao()) return false;
        return ultima.isConsultaHipertensaoAtrasada();
    }

    public boolean isAcamado() {
        VisitaDomiciliar ultima = getUltimaVisita();
        return ultima != null && ultima.isAcamado();
    }

    // Verificacoes demograficas
    public boolean isMenorDeSeis() {
        return getIdade() < 6;
    }

    public boolean isMulherIdadeFertil() {
        return sexo.equalsIgnoreCase("FEMININO") && getIdade() >= 25 && getIdade() <= 64;
    }

    public boolean isHomem() {
        return sexo.equalsIgnoreCase("MASCULINO");
    }

    public boolean isHomemIdadeVasectomia() {
        return isHomem() && getIdade() >= 25 && getIdade() <= 64;
    }

    // Prioridades condicionais
    public boolean isCadernetaAtrasada() {
        if (!isMenorDeSeis()) return false;
        VisitaDomiciliar ultima = getUltimaVisita();
        if (ultima == null) return false;
        return ultima.isCadernetaAtrasada();
    }

    public boolean isPreventivoAtrasado() {
        if (!isMulherIdadeFertil()) return false;
        VisitaDomiciliar ultima = getUltimaVisita();
        if (ultima == null) return false;
        return ultima.isPreventivoAtrasado();
    }

    public boolean isInteresseVasectomia() {
        if (!isHomemIdadeVasectomia()) return false;
        VisitaDomiciliar ultima = getUltimaVisita();
        return ultima != null && ultima.isInteresseVasectomia();
    }

    // PRIORIZACAO COMPLETA
    public boolean isPrioritario() {
        // Condicoes que valem para TODOS os pacientes
        boolean diabetesPrioritario = isDiabetes() && isConsultaDiabetesAtrasada();
        boolean hipertensaoPrioritario = isHipertensao() && isConsultaHipertensaoAtrasada();
        boolean acamadoPrioritario = isAcamado();
        
        // Condicoes especificas para CRIANCAS (menor de 6 anos)
        boolean criancaPrioritario = isMenorDeSeis() && isCadernetaAtrasada();
        
        // Condicoes especificas para MULHERES (25-64 anos)
        boolean mulherPrioritario = isMulherIdadeFertil() && isPreventivoAtrasado();
        
        // Condicoes especificas para HOMENS (25-64 anos)
        boolean homemPrioritario = isHomemIdadeVasectomia() && isInteresseVasectomia();
        
        return diabetesPrioritario || hipertensaoPrioritario || acamadoPrioritario || 
               criancaPrioritario || mulherPrioritario || homemPrioritario;
    }

    public String getPrioridadeDescricao() {
        StringBuilder desc = new StringBuilder();
        VisitaDomiciliar ultima = getUltimaVisita();
        if (ultima == null) return "";
        
        // Condicoes que valem para TODOS
        if (isDiabetes() && isConsultaDiabetesAtrasada()) desc.append("Diabetico sem consulta. ");
        if (isHipertensao() && isConsultaHipertensaoAtrasada()) desc.append("Hipertenso sem consulta. ");
        if (isAcamado()) desc.append("Acamado. ");
        
        // Condicoes para CRIANCAS
        if (isMenorDeSeis() && isCadernetaAtrasada()) desc.append("Crianca com caderneta atrasada. ");
        
        // Condicoes para MULHERES
        if (isMulherIdadeFertil() && isPreventivoAtrasado()) desc.append("Mulher com preventivo atrasado. ");
        
        // Condicoes para HOMENS
        if (isHomemIdadeVasectomia() && isInteresseVasectomia()) desc.append("Homem com interesse em vasectomia. ");
        
        return desc.toString();
    }

    public String toFileString() {
        StringBuilder sb = new StringBuilder();
        sb.append(nome).append(";");
        sb.append(dataNascimento.format(FORMATTER)).append(";");
        sb.append(sexo).append(";");
        sb.append(cpf).append(";");
        sb.append(microarea).append(";");
        sb.append(endereco);
        return sb.toString();
    }

    public static Paciente fromFileString(String linha) {
        String[] partes = linha.split(";");
        LocalDate dataNasc = LocalDate.parse(partes[1], FORMATTER);
        return new Paciente(partes[0], dataNasc, partes[2], partes[3], partes[4], partes[5]);
    }

    @Override
    public String toString() {
        return nome + " | " + getIdade() + " anos | " + microarea + " | " + cpf;
    }
}