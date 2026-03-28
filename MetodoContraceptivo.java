public enum MetodoContraceptivo {
    NAO_SE_APLICA("Nao se aplica"),
    NENHUM("Nenhum"),
    PILULA("Pilula anticoncepcional"),
    INJETAVEL("Injetavel"),
    DIU("DIU"),
    IMPLANTE("Implante"),
    ADESIVO("Adesivo"),
    ANEL_VAGINAL("Anel vaginal"),
    PRESERVATIVO_MASCULINO("Preservativo masculino"),
    PRESERVATIVO_FEMININO("Preservativo feminino"),
    LAQUEADURA("Laqueadura"),
    VASECTOMIA("Vasectomia"),
    OUTROS("Outros");

    private final String descricao;

    MetodoContraceptivo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static void listarOpcoes() {
        System.out.println("\nMetodos contraceptivos disponiveis:");
        for (MetodoContraceptivo metodo : values()) {
            System.out.println(metodo.ordinal() + " - " + metodo.getDescricao());
        }
    }

    public static MetodoContraceptivo fromOrdinal(int ordinal) {
        return values()[ordinal];
    }
}