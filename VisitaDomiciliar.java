import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class VisitaDomiciliar {
    private LocalDate data;
    private String motivo;
    private String observacoes;
    private boolean acamado;
    
    private boolean diabetes;
    private LocalDate dataConsultaDiabetes;
    private boolean hipertensao;
    private LocalDate dataConsultaHipertensao;
    private RegistroPA registroPA;
    
    private boolean preventivoEmDia;
    private LocalDate dataUltimoPreventivo;
    private String metodoContraceptivo;
    
    private boolean cadernetaEmDia;
    
    private boolean interesseVasectomia;
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public VisitaDomiciliar(LocalDate data, String motivo, String observacoes, boolean acamado) {
        this.data = data;
        this.motivo = motivo;
        this.observacoes = observacoes;
        this.acamado = acamado;
        this.cadernetaEmDia = true;  // Padrao
        this.preventivoEmDia = true; // Padrao
    }

    // Getters
    public LocalDate getData() { return data; }
    public String getMotivo() { return motivo; }
    public String getObservacoes() { return observacoes; }
    public boolean isAcamado() { return acamado; }
    
    public boolean isDiabetes() { return diabetes; }
    public void setDiabetes(boolean diabetes) { this.diabetes = diabetes; }
    public LocalDate getDataConsultaDiabetes() { return dataConsultaDiabetes; }
    public void setDataConsultaDiabetes(LocalDate data) { this.dataConsultaDiabetes = data; }
    
    public boolean isHipertensao() { return hipertensao; }
    public void setHipertensao(boolean hipertensao) { this.hipertensao = hipertensao; }
    public LocalDate getDataConsultaHipertensao() { return dataConsultaHipertensao; }
    public void setDataConsultaHipertensao(LocalDate data) { this.dataConsultaHipertensao = data; }
    public RegistroPA getRegistroPA() { return registroPA; }
    public void setRegistroPA(RegistroPA registroPA) { this.registroPA = registroPA; }
    
    public boolean isPreventivoEmDia() { return preventivoEmDia; }
    public void setPreventivoEmDia(boolean preventivo) { this.preventivoEmDia = preventivo; }
    public LocalDate getDataUltimoPreventivo() { return dataUltimoPreventivo; }
    public void setDataUltimoPreventivo(LocalDate data) { this.dataUltimoPreventivo = data; }
    public String getMetodoContraceptivo() { return metodoContraceptivo; }
    public void setMetodoContraceptivo(String metodo) { this.metodoContraceptivo = metodo; }
    
    public boolean isCadernetaEmDia() { return cadernetaEmDia; }
    public void setCadernetaEmDia(boolean caderneta) { this.cadernetaEmDia = caderneta; }
    
    public boolean isInteresseVasectomia() { return interesseVasectomia; }
    public void setInteresseVasectomia(boolean interesse) { this.interesseVasectomia = interesse; }

    // Metodos de atraso
    public boolean isConsultaDiabetesAtrasada() {
        if (!diabetes || dataConsultaDiabetes == null) return false;
        return Period.between(dataConsultaDiabetes, LocalDate.now()).getMonths() >= 1;
    }

    public boolean isConsultaHipertensaoAtrasada() {
        if (!hipertensao || dataConsultaHipertensao == null) return false;
        return Period.between(dataConsultaHipertensao, LocalDate.now()).getMonths() >= 1;
    }

    public boolean isPreventivoAtrasado() {
        if (preventivoEmDia) return false;
        if (dataUltimoPreventivo == null) return true;
        return Period.between(dataUltimoPreventivo, LocalDate.now()).getYears() >= 1;
    }

    public boolean isCadernetaAtrasada() {
        return !cadernetaEmDia;
    }

    public boolean isPrioritario() {
        return (diabetes && isConsultaDiabetesAtrasada()) ||
               (hipertensao && isConsultaHipertensaoAtrasada()) ||
               (!preventivoEmDia && isPreventivoAtrasado()) ||
               (!cadernetaEmDia) ||
               interesseVasectomia;
    }

    public String getPrioridadeDescricao() {
    StringBuilder desc = new StringBuilder();
    if (diabetes && isConsultaDiabetesAtrasada()) desc.append("Diabetico sem consulta. ");
    if (hipertensao && isConsultaHipertensaoAtrasada()) desc.append("Hipertenso sem consulta. ");
    if (!preventivoEmDia && isPreventivoAtrasado()) desc.append("Preventivo atrasado. ");
    if (!cadernetaEmDia) desc.append("Caderneta atrasada. ");
    if (interesseVasectomia) desc.append("Interesse vasectomia. ");
    return desc.toString();
}

    public String getDataFormatada() {
        return data.format(FORMATTER);
    }

    @Override
    public String toString() {
        return getDataFormatada() + " - " + motivo;
    }
}