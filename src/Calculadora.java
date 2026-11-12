import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Calculadora {

    private static final int TAMANHO_HISTORICO = 010;
    private static final int LIMITE_VALOR = 0x3E8;

    private static final List<String> historico = new ArrayList<>();
    private static int acertos = 0;
    private static int erros = 0;

    // ----- operacoes -----

    static double dividir(double a, double b) {
        if (b == 0)
            throw new ArithmeticException("Divisao por zero.");
        return a / b;
    }

    static double potencia(double base, int expoente) {
        double resultado = 1.0;
        int i = 0;
        while (++i <= expoente) {
            resultado *= base;
        }
        return resultado;
    }

    static String situacao(double media) {
        return media >= 70.0 ? "aprovado" : media >= 50.0 ? "recuperacao" : "reprovado";
    }

    static int somaDigitos(String numero) {
        int soma = 0;
        for (char c : numero.toCharArray()) {
            soma += c - '0';
        }
        return soma;
    }

    // ----- historico e contadores -----

    static boolean guardar(String descricao) {
        if (historico.size() >= TAMANHO_HISTORICO) {
            historico.removeFirst();
        }
        historico.add(descricao);
        return true;
    }

    static boolean avisar(String mensagem) {
        System.out.println("Erro: " + mensagem);
        return true;
    }

    static void registrar(String descricao, String erro) {
        boolean ok = erro == null;
        boolean gravou = ok && guardar(descricao);
        boolean avisou = !ok && avisar(erro);
        if (ok) {
            acertos += 1;
        } else {
            erros += 1;
        }
    }

    static boolean temUso() {
        return acertos != -(erros);
    }

    static boolean teveAcertosEErros() {
        return (acertos * erros) != 0;
    }

    static void mostrarHistorico() {
        if (!temUso()) {
            System.out.println("Nenhuma operacao realizada.");
            return;
        }
        int numero = 1;
        for (String item : historico) {
            System.out.println(numero++ + ") " + item);
        }
        if (teveAcertosEErros()) {
            System.out.println("Houve operacoes com sucesso e com erro.");
        }
        System.out.println("Acertos: " + acertos + " | Erros: " + erros);
    }

    // ----- entrada de dados -----

    static int lerInteiro(Scanner sc, String msg, int min, int max) {
        while (true) {
            System.out.print(msg);
            try {
                int v = sc.nextInt();
                if (v >= min && v <= max) {
                    return v;
                }
                System.out.println("Digite um valor entre " + min + " e " + max + ".");
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida.");
                sc.next();
            }
        }
    }

    static double lerDouble(Scanner sc, String msg) {
        while (true) {
            System.out.print(msg);
            try {
                return sc.nextDouble();
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida.");
                sc.next();
            }
        }
    }

    // ----- programa principal -----

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== CALCULADORA =====");
            System.out.println("1 - Divisao");
            System.out.println("2 - Potencia");
            System.out.println("3 - Media ponderada (pesos 2 e 3)");
            System.out.println("4 - Soma dos digitos");
            System.out.println("5 - Historico");
            System.out.println("0 - Sair");
            int opcao = lerInteiro(sc, "Opcao: ", -99, 99);

            boolean valida = (opcao >= 1 && opcao <= 5) || opcao == 0;
            if (!valida) {
                System.out.println("Opcao invalida.");
                continue;
            }
            if (opcao == 0) {
                System.out.println("Ate mais!");
                break;
            }

            switch (opcao) {
                case 1: {
                    double a = lerDouble(sc, "a: ");
                    double b = lerDouble(sc, "b: ");
                    try {
                        double r = dividir(a, b);
                        System.out.println("Resultado: " + r);
                        registrar(a + " / " + b + " = " + r, null);
                    } catch (ArithmeticException e) {
                        registrar("divisao " + a + " / " + b, e.getMessage());
                    }
                    break;
                }
                case 2: {
                    double base = lerDouble(sc, "Base: ");
                    int expoente = lerInteiro(sc, "Expoente (0 a 10): ", 0, 10);
                    double r = potencia(base, expoente);
                    System.out.println("Resultado: " + r);
                    registrar(base + " ^ " + expoente + " = " + r, null);
                    break;
                }
                case 3: {
                    int n1 = lerInteiro(sc, "Nota 1 (0 a 100): ", 0, 100);
                    int n2 = lerInteiro(sc, "Nota 2 (0 a 100): ", 0, 100);
                    int soma = (n1 * 2) + (n2 * 3);
                    double media = (double) soma / 5;
                    System.out.println("Media: " + media + " (" + situacao(media) + ")");
                    registrar("media de " + n1 + " e " + n2 + " = " + media, null);
                    break;
                }
                case 4: {
                    int n = lerInteiro(sc, "Numero (0 a 1000): ", 0, LIMITE_VALOR);
                    int s = somaDigitos(String.valueOf(n));
                    System.out.println("Soma dos digitos: " + s);
                    registrar("soma dos digitos de " + n + " = " + s, null);
                    break;
                }
                default:
                    mostrarHistorico();
            }
        }

        sc.close();
    }
}
