import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class SistemaMenu {
    private RepositorioPacientesArquivo repositorio;
    private Scanner scanner;
    private DateTimeFormatter formatter;

    public SistemaMenu() {
        this.repositorio = new RepositorioPacientesArquivo();
        this.scanner = new Scanner(System.in);
        this.formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }

    public void executar() {
        int opcao = 0;
        do {
            System.out.println("\n==========================================");
            System.out.println("  SISTEMA DE ACOMPANHAMENTO TERRITORIAL");
            System.out.println("  Busca Ativa em Saude Publica");
            System.out.println("==========================================");
            System.out.println("1. Cadastrar paciente");
            System.out.println("2. Listar pacientes");
            System.out.println("3. Registrar visita domiciliar");
            System.out.println("4. Relatorios");
            System.out.println("5. Excluir paciente");
            System.out.println("6. Sair");
            System.out.println("==========================================");
            System.out.print("Opcao: ");
            
            if (!scanner.hasNextInt()) {
                System.out.println("Digite um numero valido (1 a 6)!");
                scanner.next();
                continue;
            }
            
            opcao = scanner.nextInt();
            scanner.nextLine();
            
            switch(opcao) {
                case 1: cadastrarPaciente(); break;
                case 2: listarPacientes(); break;
                case 3: registrarVisita(); break;
                case 4: menuRelatorios(); break;
                case 5: excluirPaciente(); break;
                case 6: System.out.println("Saindo..."); break;
                default: System.out.println("Opcao invalida! Digite 1 a 6.");
            }
        } while(opcao != 6);
        scanner.close();
    }

    private void cadastrarPaciente() {
        System.out.println("\n--- CADASTRO DE PACIENTE ---");
        
        System.out.print("Nome completo: ");
        String nome = scanner.nextLine();
        
        System.out.print("Data nascimento (dd/MM/aaaa): ");
        LocalDate dataNasc = null;
        while (dataNasc == null) {
            try {
                dataNasc = LocalDate.parse(scanner.nextLine(), formatter);
            } catch (Exception e) {
                System.out.print("Data invalida! Digite novamente: ");
            }
        }
        
        System.out.print("Sexo (MASCULINO/FEMININO): ");
        String sexo = scanner.nextLine().toUpperCase();
        
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();
        if (cpf.isEmpty()) {
            cpf = "NAO POSSUI";
        }
        
        System.out.print("Microarea (ex: A1, B2): ");
        String micro = scanner.nextLine();
        
        System.out.print("Endereco completo: ");
        String end = scanner.nextLine();
        
        Paciente p = new Paciente(nome, dataNasc, sexo, cpf, micro, end);
        repositorio.cadastrar(p);
        System.out.println("\nPaciente cadastrado com sucesso!");
    }

    private void listarPacientes() {
        List<Paciente> lista = repositorio.listarTodos();
        System.out.println("\n--- PACIENTES CADASTRADOS (" + lista.size() + ") ---");
        for (int i = 0; i < lista.size(); i++) {
            Paciente p = lista.get(i);
            System.out.println((i+1) + ". " + p);
        }
        System.out.print("\nPressione ENTER para continuar...");
        scanner.nextLine();
    }

    private void excluirPaciente() {
        System.out.println("\n--- EXCLUIR PACIENTE ---");
        
        List<Paciente> lista = repositorio.listarTodos();
        if (lista.isEmpty()) {
            System.out.println("Nenhum paciente cadastrado.");
            return;
        }
        
        System.out.println("\nPacientes cadastrados:");
        for (int i = 0; i < lista.size(); i++) {
            Paciente p = lista.get(i);
            System.out.println((i+1) + ". " + p.getNome() + " | " + p.getCpf() + " | " + p.getMicroarea());
        }
        
        System.out.print("\nDigite o numero do paciente para excluir (0 para cancelar): ");
        int escolha = scanner.nextInt();
        scanner.nextLine();
        
        if (escolha == 0) {
            System.out.println("Operacao cancelada.");
            return;
        }
        
        if (escolha < 1 || escolha > lista.size()) {
            System.out.println("Numero invalido!");
            return;
        }
        
        Paciente paciente = lista.get(escolha - 1);
        
        System.out.print("\nTem certeza que deseja excluir " + paciente.getNome() + "? (S/N): ");
        String confirmacao = scanner.nextLine();
        
        if (confirmacao.equalsIgnoreCase("S")) {
            repositorio.excluirPaciente(paciente);
            System.out.println("\nPaciente excluido com sucesso!");
        } else {
            System.out.println("Exclusao cancelada.");
        }
    }
        private void registrarVisita() {
        System.out.println("\n--- REGISTRO DE VISITA DOMICILIAR ---");
        
        System.out.print("Digite nome ou CPF do paciente: ");
        String busca = scanner.nextLine().toLowerCase();
        
        List<Paciente> encontrados = repositorio.listarTodos().stream()
            .filter(p -> p.getNome().toLowerCase().contains(busca) || 
                         p.getCpf().toLowerCase().contains(busca))
            .collect(Collectors.toList());
        
        if (encontrados.isEmpty()) {
            System.out.println("Nenhum paciente encontrado!");
            return;
        }
        
        Paciente paciente;
        if (encontrados.size() == 1) {
            paciente = encontrados.get(0);
        } else {
            System.out.println("Varios pacientes encontrados:");
            for (int i = 0; i < encontrados.size(); i++) {
                System.out.println((i+1) + ". " + encontrados.get(i).getNome() + " (" + encontrados.get(i).getCpf() + ")");
            }
            System.out.print("Escolha o numero: ");
            int escolha = scanner.nextInt();
            scanner.nextLine();
            if (escolha < 1 || escolha > encontrados.size()) {
                System.out.println("Opcao invalida!");
                return;
            }
            paciente = encontrados.get(escolha - 1);
        }
        
        System.out.println("\nPaciente selecionado: " + paciente.getNome() + " (" + paciente.getIdade() + " anos)");
        
        System.out.print("Data da visita (dd/MM/aaaa) ou ENTER para hoje: ");
        String dataStr = scanner.nextLine();
        LocalDate data = LocalDate.now();
        if (!dataStr.isEmpty()) {
            try {
                data = LocalDate.parse(dataStr, formatter);
            } catch (Exception e) {
                System.out.println("Data invalida, usando hoje.");
            }
        }
        
        System.out.print("Motivo da visita: ");
        String motivo = scanner.nextLine();
        
        System.out.print("Observacoes: ");
        String obs = scanner.nextLine();
        
        System.out.print("Paciente esta acamado? (S/N): ");
        boolean acamado = scanner.nextLine().equalsIgnoreCase("S");
        
        VisitaDomiciliar visita = new VisitaDomiciliar(data, motivo, obs, acamado);
        
        System.out.println("\n--- INFORMACOES DE SAUDE ---");
        
        // DIABETES (para todos)
        System.out.print("Paciente tem diabetes? (S/N): ");
        visita.setDiabetes(scanner.nextLine().equalsIgnoreCase("S"));
        if (visita.isDiabetes()) {
            System.out.print("Data da ultima consulta (dd/MM/aaaa): ");
            try {
                visita.setDataConsultaDiabetes(LocalDate.parse(scanner.nextLine(), formatter));
            } catch (Exception e) {}
        }
        
        // HIPERTENSAO (para todos)
        System.out.print("Paciente tem hipertensao? (S/N): ");
        visita.setHipertensao(scanner.nextLine().equalsIgnoreCase("S"));
        if (visita.isHipertensao()) {
            System.out.print("Data da ultima consulta (dd/MM/aaaa): ");
            try {
                visita.setDataConsultaHipertensao(LocalDate.parse(scanner.nextLine(), formatter));
            } catch (Exception e) {}
            
            System.out.print("Registrar Pressao Arterial? (S/N): ");
            if (scanner.nextLine().equalsIgnoreCase("S")) {
                System.out.print("Pressao Sistolica (maxima): ");
                int sis = scanner.nextInt();
                System.out.print("Pressao Diastolica (minima): ");
                int dias = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Observacoes PA: ");
                String obsPA = scanner.nextLine();
                RegistroPA pa = new RegistroPA(data, sis, dias, obsPA);
                visita.setRegistroPA(pa);
                System.out.println("PA registrada: " + pa.getClassificacao());
                if (pa.isAlta()) System.out.println("ALERTA: Pressao elevada!");
            }
        }
        
        // SAUDE DA CRIANCA (apenas menores de 6 anos)
        if (paciente.isMenorDeSeis()) {
            System.out.println("\n--- SAUDE DA CRIANCA ---");
            System.out.print("Caderneta de vacinacao em dia? (S/N): ");
            visita.setCadernetaEmDia(scanner.nextLine().equalsIgnoreCase("S"));
        } else {
            visita.setCadernetaEmDia(true);
        }
        
        // SAUDE DA MULHER (apenas mulheres 25-64 anos)
        if (paciente.isMulherIdadeFertil()) {
            System.out.println("\n--- SAUDE DA MULHER ---");
            System.out.print("Preventivo em dia? (S/N): ");
            visita.setPreventivoEmDia(scanner.nextLine().equalsIgnoreCase("S"));
            if (!visita.isPreventivoEmDia()) {
                System.out.print("Data do ultimo preventivo (dd/MM/aaaa): ");
                try {
                    visita.setDataUltimoPreventivo(LocalDate.parse(scanner.nextLine(), formatter));
                } catch (Exception e) {}
                
                System.out.print("Metodo contraceptivo (Nenhum, Pilula, Injetavel, DIU, Preservativo): ");
                visita.setMetodoContraceptivo(scanner.nextLine());
            }
        } else {
            visita.setPreventivoEmDia(true);
        }
        
        // PLANEJAMENTO FAMILIAR (apenas homens de 25-64 anos)
        if (paciente.isHomem() && paciente.getIdade() >= 25 && paciente.getIdade() <= 64) {
            System.out.println("\n--- PLANEJAMENTO FAMILIAR ---");
            System.out.print("Interesse em vasectomia? (S/N): ");
            visita.setInteresseVasectomia(scanner.nextLine().equalsIgnoreCase("S"));
        } else {
            visita.setInteresseVasectomia(false);
        }
        
        paciente.adicionarVisita(visita);
        repositorio.salvarTodos();
        
        System.out.println("\nVisita registrada com sucesso!");
        if (visita.isPrioritario() || acamado) {
            System.out.println("ATENCAO: Paciente PRIORITARIO para busca ativa!");
            System.out.println("Motivo(s): " + visita.getPrioridadeDescricao());
            if (acamado) System.out.println("   - Paciente acamado");
        }
        
        System.out.print("\nPressione ENTER para continuar...");
        scanner.nextLine();
    }

        private void menuRelatorios() {
        int opcao = 0;
        do {
            System.out.println("\n--- RELATORIOS ---");
            System.out.println("1. Diabeticos");
            System.out.println("2. Hipertensos");
            System.out.println("3. Mulheres sem preventivo (25-64 anos)");
            System.out.println("4. Criancas com caderneta atrasada (menor de 6 anos)");
            System.out.println("5. Homens interessados em vasectomia (25-64 anos)");
            System.out.println("6. Pacientes acamados");
            System.out.println("7. Todos os prioritarios");
            System.out.println("8. Voltar");
            System.out.print("Escolha: ");
            
            if (!scanner.hasNextInt()) {
                System.out.println("Digite um numero valido!");
                scanner.next();
                continue;
            }
            
            opcao = scanner.nextInt();
            scanner.nextLine();
            
            List<Paciente> resultado = null;
            String titulo = "";
            
            switch(opcao) {
                case 1: 
                    resultado = repositorio.listarTodos().stream()
                        .filter(p -> p.isDiabetes())
                        .collect(Collectors.toList());
                    titulo = "DIABETICOS";
                    break;
                case 2:
                    resultado = repositorio.listarTodos().stream()
                        .filter(p -> p.isHipertensao())
                        .collect(Collectors.toList());
                    titulo = "HIPERTENSOS";
                    break;
                case 3:
                    resultado = repositorio.listarTodos().stream()
                        .filter(p -> p.isMulherIdadeFertil() && p.isPreventivoAtrasado())
                        .collect(Collectors.toList());
                    titulo = "MULHERES SEM PREVENTIVO (25-64 anos)";
                    break;
                case 4:
                    resultado = repositorio.listarTodos().stream()
                        .filter(p -> p.isMenorDeSeis() && p.isCadernetaAtrasada())
                        .collect(Collectors.toList());
                    titulo = "CRIANCAS COM CADERNETA ATRASADA (menor de 6 anos)";
                    break;
                case 5:
                    resultado = repositorio.listarTodos().stream()
                        .filter(p -> p.isHomem() && p.getIdade() >= 25 && p.getIdade() <= 64 && p.isInteresseVasectomia())
                        .collect(Collectors.toList());
                    titulo = "HOMENS INTERESSADOS EM VASECTOMIA (25-64 anos)";
                    break;
                case 6:
                    resultado = repositorio.listarTodos().stream()
                        .filter(p -> p.isAcamado())
                        .collect(Collectors.toList());
                    titulo = "PACIENTES ACAMADOS";
                    break;
                case 7:
                    resultado = repositorio.listarTodos().stream()
                        .filter(Paciente::isPrioritario)
                        .collect(Collectors.toList());
                    titulo = "TODOS OS PRIORITARIOS";
                    break;
                case 8:
                    return;
                default:
                    System.out.println("Opcao invalida!");
                    continue;
            }
            
            System.out.println("\n========== " + titulo + " (" + resultado.size() + ") ==========");
            if (resultado.isEmpty()) {
                System.out.println("Nenhum paciente encontrado.");
            } else {
                for (int i = 0; i < resultado.size(); i++) {
                    Paciente p = resultado.get(i);
                    System.out.println("\n" + (i+1) + ". " + p.getNome());
                    System.out.println("   CPF: " + p.getCpf());
                    System.out.println("   Idade: " + p.getIdade() + " anos");
                    System.out.println("   Microarea: " + p.getMicroarea());
                    System.out.println("   Endereco: " + p.getEndereco());
                    System.out.println("   Motivo: " + p.getPrioridadeDescricao());
                    System.out.println("   ---");
                    System.out.print("   Deseja ver detalhes? (S/N): ");
                    if (scanner.nextLine().equalsIgnoreCase("S")) {
                        verDetalhesPaciente(p);
                    }
                }
            }
            System.out.print("\nPressione ENTER para continuar...");
            scanner.nextLine();
        } while(opcao != 8);
    }

    private void verDetalhesPaciente(Paciente p) {
        System.out.println("\n--- DETALHES DO PACIENTE ---");
        System.out.println("Nome: " + p.getNome());
        System.out.println("CPF: " + p.getCpf());
        System.out.println("Idade: " + p.getIdade() + " anos");
        System.out.println("Sexo: " + p.getSexo());
        System.out.println("Microarea: " + p.getMicroarea());
        System.out.println("Endereco: " + p.getEndereco());
        
        VisitaDomiciliar ultima = p.getUltimaVisita();
        if (ultima != null) {
            System.out.println("\n--- ULTIMA VISITA ---");
            System.out.println("Data: " + ultima.getDataFormatada());
            System.out.println("Motivo: " + ultima.getMotivo());
            System.out.println("Observacoes: " + ultima.getObservacoes());
            System.out.println("Acamado: " + (ultima.isAcamado() ? "Sim" : "Nao"));
            
            if (ultima.isDiabetes()) {
                System.out.println("Diabetes: Sim");
                if (ultima.getDataConsultaDiabetes() != null)
                    System.out.println("  Ultima consulta: " + ultima.getDataConsultaDiabetes().format(formatter));
            }
            if (ultima.isHipertensao()) {
                System.out.println("Hipertensao: Sim");
                if (ultima.getDataConsultaHipertensao() != null)
                    System.out.println("  Ultima consulta: " + ultima.getDataConsultaHipertensao().format(formatter));
                if (ultima.getRegistroPA() != null)
                    System.out.println("  Ultima PA: " + ultima.getRegistroPA());
            }
            if (p.isMulherIdadeFertil()) {
                System.out.println("Preventivo em dia: " + (ultima.isPreventivoEmDia() ? "Sim" : "Nao"));
                if (ultima.getDataUltimoPreventivo() != null)
                    System.out.println("  Data ultimo preventivo: " + ultima.getDataUltimoPreventivo().format(formatter));
                if (ultima.getMetodoContraceptivo() != null)
                    System.out.println("  Metodo contraceptivo: " + ultima.getMetodoContraceptivo());
            }
            if (p.isMenorDeSeis()) {
                System.out.println("Caderneta em dia: " + (ultima.isCadernetaEmDia() ? "Sim" : "Nao"));
            }
            if (p.isHomem() && p.getIdade() >= 25 && p.getIdade() <= 64 && ultima.isInteresseVasectomia()) {
                System.out.println("Interesse vasectomia: Sim");
            }
        }
        
        System.out.print("\nPressione ENTER para continuar...");
        scanner.nextLine();
    }

    public static void main(String[] args) {
        new SistemaMenu().executar();
    }
}