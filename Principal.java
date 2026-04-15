import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private static List<Funcionario> funcionarios;

    public static void main(String[] args) {
        System.out.println("\n=== 3.1 - Inserindo funcionários ===");
        inicializarFuncionarios();

        System.out.println("\n=== 3.2 - Removendo funcionário 'João' ===");
        removerFuncionarioPorNome("João");

        System.out.println("\n=== 3.3 - Lista de funcionários ===");
        imprimirFuncionarios();

        System.out.println("\n=== 3.4 - Aplicando aumento de 10% ===");
        aplicarAumentoGeral(new BigDecimal("1.10"));

        System.out.println("\n=== 3.5 / 3.6 - Funcionários agrupados por função ===");
        Map<String, List<Funcionario>> funcionariosPorFuncao = agruparPorFuncao();
        imprimirFuncionariosPorFuncao(funcionariosPorFuncao);

        System.out.println("\n=== 3.8 - Aniversariantes em outubro e dezembro ===");
        imprimirAniversariantesPorMes(10, 12);

        System.out.println("\n=== 3.9 - Funcionário com maior idade ===");
        imprimirFuncionarioMaisVelho();

        System.out.println("\n=== 3.10 - Funcionários em ordem alfabética ===");
        imprimirFuncionariosOrdemAlfabetica();

        System.out.println("\n=== 3.11 - Total dos salários ===");
        totalSalariosPagos();

        System.out.println("\n=== 3.12 - Salários mínimos por funcionário ===");
        imprimirFuncionariosSalarioMinimo();


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

    public static void imprimirFuncionarioMaisVelho(){
        funcionarios.stream()
                .min(Comparator.comparing(Funcionario::getDataNascimento))
                .ifPresent(f -> {
                    int idade = Period.between(f.getDataNascimento(), LocalDate.now()).getYears();
                    System.out.printf("Nome: %s | Idade: %d anos%n", f.getNome(), idade);
                });

    }

    public static void imprimirFuncionariosOrdemAlfabetica(){
        funcionarios.stream()
                .sorted(Comparator.comparing(Funcionario::getNome))
                .forEach(f -> System.out.printf(
                        "Nome: %s | Nascimento: %s | Salário: %s | Função: %s%n",
                        f.getNome(),
                        f.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        NumberFormat.getNumberInstance(new Locale("pt", "BR")).format(f.getSalario()),
                        f.getFuncao()
                ));
    }

    public static void totalSalariosPagos(){
        BigDecimal total = funcionarios.stream()
                .map(Funcionario::getSalario)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        NumberFormat fmt = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
        fmt.setMinimumFractionDigits(2);
        fmt.setMaximumFractionDigits(2);

        System.out.println("Total dos salarios: " + fmt.format(total));
    }

    public static void imprimirFuncionariosSalarioMinimo(){
        DateTimeFormatter fmtData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        funcionarios.forEach(f -> System.out.printf(
                "Nome: %s | Nascimento: %s | Função: %s | QT salário minimo: %s%n ",
                f.getNome(),
                f.getDataNascimento().format(fmtData),
                f.getFuncao(),
                f.getSalario().divide(BigDecimal.valueOf(1212),2, RoundingMode.HALF_UP)
        ));
    }

}
