import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private static List<Funcionario> funcionarios;

    public static void main(String[] args) {
        System.out.println("Inserir funcionários na lista... ");
        inicializarFuncionarios();
        System.out.println("Funcionários inseridos com sucesso!");

        System.out.println("Remover funcionário João... ");
        removerFuncionarioPorNome("João");
        System.out.println("Funcionário removido com sucesso!");

        System.out.println("Imprimindo funcionários...");
        imprimirFuncionarios();
        System.out.println("Fim da impressão dos funcionários");

        System.out.println("Aplicando aumento de 10%...");
        aplicarAumentoGeral(new BigDecimal("1.10"));
        System.out.println("Aumento aplicado com sucesso!");

        System.out.println("Agrupando funcionários por função...");
        Map<String, List<Funcionario>> funcionariosPorFuncao = agruparPorFuncao();
        System.out.println("Funcionários agrupados com sucesso!");

        System.out.println("Imprimindo funcionários por função...");
        imprimirFuncionariosPorFuncao(funcionariosPorFuncao);
        System.out.println("Fim da impressão dos funcionários por função");

        System.out.println("Aniversariantes em outubro e dezembro...");
        imprimirAniversariantesPorMes(10, 12);
        System.out.println("Fim da impressão dos Aniversariantes");

    }

    public static void inicializarFuncionarios(){
        funcionarios = new ArrayList<>(Arrays.asList(
                new Funcionario("Maria",   LocalDate.of(2000, 10, 18), new BigDecimal("2009.44"), "Operador"),
                new Funcionario("João",    LocalDate.of(1990,  5, 12), new BigDecimal("2284.38"), "Operador"),
                new Funcionario("Caio",    LocalDate.of(1961,  5,  2), new BigDecimal("9836.14"), "Coordenador"),
                new Funcionario("Miguel",  LocalDate.of(1988, 10, 14), new BigDecimal("19119.88"), "Diretor"),
                new Funcionario("Alice",   LocalDate.of(1995,  1,  5), new BigDecimal("2234.68"), "Recepcionista"),
                new Funcionario("Heitor",  LocalDate.of(1999, 11, 19), new BigDecimal("1582.72"), "Operador"),
                new Funcionario("Arthur",  LocalDate.of(1993,  3, 31), new BigDecimal("4071.84"), "Contador"),
                new Funcionario("Laura",   LocalDate.of(1994,  7,  8), new BigDecimal("3017.45"), "Gerente"),
                new Funcionario("Heloísa", LocalDate.of(2003,  5, 24), new BigDecimal("1606.85"), "Eletricista"),
                new Funcionario("Helena",  LocalDate.of(1996,  9,  2), new BigDecimal("2799.93"), "Gerente")
        ));
    }

    public static void removerFuncionarioPorNome(String nome) {
        boolean removido = funcionarios.removeIf(f -> f.getNome().equals(nome));
        if (!removido) {
            System.out.println("Funcionário '" + nome + "' não encontrado.");
        }
    }

    public static void imprimirFuncionarios() {
        DateTimeFormatter fmtData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        NumberFormat fmtSalario = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
        fmtSalario.setMinimumFractionDigits(2);
        fmtSalario.setMaximumFractionDigits(2);

        funcionarios.forEach(f -> System.out.printf(
                "Nome: %s | Nascimento: %s | Salário: %s | Função: %s%n",
                f.getNome(),
                f.getDataNascimento().format(fmtData),
                fmtSalario.format(f.getSalario()),
                f.getFuncao()
        ));
    }

    public static void aplicarAumentoGeral(BigDecimal percentual) {
        funcionarios.forEach(f ->
                f.setSalario(f.getSalario().multiply(percentual).setScale(2, RoundingMode.HALF_UP))
        );
    }

    public static Map<String, List<Funcionario>> agruparPorFuncao() {
        return funcionarios.stream()
                .collect(Collectors.groupingBy(Funcionario::getFuncao));
    }

    public static void imprimirFuncionariosPorFuncao(Map<String, List<Funcionario>> funcionariosPorFuncao) {
        funcionariosPorFuncao.forEach((funcao, lista) -> {
            System.out.println("\n[ " + funcao + " ]");
            lista.forEach(f -> System.out.printf(
                    "  Nome: %s | Nascimento: %s | Salário: %s%n",
                    f.getNome(),
                    f.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    NumberFormat.getNumberInstance(new Locale("pt", "BR")).format(f.getSalario())
            ));
        });
    }

    public static void imprimirAniversariantesPorMes(int... meses) {
        Set<Integer> mesesFiltro = Arrays.stream(meses)
                .boxed()
                .collect(Collectors.toSet());

        funcionarios.stream()
                .filter(f -> mesesFiltro.contains(f.getDataNascimento().getMonthValue()))
                .forEach(f -> System.out.printf(
                        "Nome: %s | Nascimento: %s%n",
                        f.getNome(),
                        f.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                ));
    }

}
