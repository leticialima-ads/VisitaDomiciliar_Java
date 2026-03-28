import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RegistroPA {
    private LocalDate data;
    private int pressaoSistolica;
    private int pressaoDiastolica;
    private String observacoes;

    public RegistroPA(LocalDate data, int sistolica, int diastolica, String observacoes) {
        this.data = data;
        this.pressaoSistolica = sistolica;
        this.pressaoDiastolica = diastolica;
        this.observacoes = observacoes;
    }

    public LocalDate getData() { return data; }
    public int getPressaoSistolica() { return pressaoSistolica; }
    public int getPressaoDiastolica() { return pressaoDiastolica; }
    public String getObservacoes() { return observacoes; }

    public String getDataFormatada() {
        return data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String getClassificacao() {
        if (pressaoSistolica < 120 && pressaoDiastolica < 80) return "Normal";
        if (pressaoSistolica >= 120 && pressaoSistolica <= 129 && pressaoDiastolica < 80) return "Elevada";
        if (pressaoSistolica >= 130 && pressaoSistolica <= 139 || pressaoDiastolica >= 80 && pressaoDiastolica <= 89) return "Hipertensao Estagio 1";
        if (pressaoSistolica >= 140 && pressaoSistolica <= 159 || pressaoDiastolica >= 90 && pressaoDiastolica <= 99) return "Hipertensao Estagio 2";
        return "Hipertensao Estagio 3 (Crise)";
    }

    public boolean isAlta() {
        return pressaoSistolica >= 140 || pressaoDiastolica >= 90;
    }

    @Override
    public String toString() {
        return getDataFormatada() + " - " + pressaoSistolica + "/" + pressaoDiastolica + " mmHg (" + getClassificacao() + ")";
    }
}