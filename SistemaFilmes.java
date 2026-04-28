import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SistemaFilmes {

    static List<Filme> filmes = new ArrayList<>();
    static List<Avaliacao> avaliacoes = new ArrayList<>();
    static int proximoIdFilme = 1;
    static int proximoIdAvaliacao = 1;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        carregarDadosIniciais();

        int opcao;
        do {
            exibirMenu();
            opcao = lerInteiro("Escolha uma opção: ");
            processarOpcao(opcao);
        } while (opcao != 0);

        scanner.close();
    }

    static void exibirMenu() {
        System.out.println("\n========== SISTEMA DE AVALIAÇÃO DE FILMES ==========");
        System.out.println("1. Cadastrar novo filme");
        System.out.println("2. Listar todos os filmes");
        System.out.println("3. Avaliar um filme");
        System.out.println("4. Ver avaliações de um filme");
        System.out.println("0. Sair");
        System.out.println("====================================================");
    }

    static void processarOpcao(int opcao) {
        switch (opcao) {
            case 1 -> cadastrarFilme();
            case 2 -> listarFilmes();
            case 3 -> avaliarFilme();
            case 4 -> verAvaliacoes();
            case 0 -> System.out.println("Saindo do sistema...");
            default -> System.out.println("Opção inválida.");
        }
    }

    static void cadastrarFilme() {
        System.out.println("\n--- CADASTRO DE FILME ---");
        scanner.nextLine();
        System.out.print("Título: ");
        String titulo = scanner.nextLine();
        System.out.print("Diretor: ");
        String diretor = scanner.nextLine();
        int ano = lerInteiro("Ano: ");
        scanner.nextLine();
        System.out.print("Gênero: ");
        String genero = scanner.nextLine();

        Filme novoFilme = new Filme(proximoIdFilme++, titulo, diretor, ano, genero);
        filmes.add(novoFilme);
        System.out.println("Filme cadastrado com sucesso!");
    }

    static void listarFilmes() {
        System.out.println("\n--- LISTA DE FILMES ---");
        if (filmes.isEmpty()) {
            System.out.println("Nenhum filme cadastrado.");
            return;
        }
        for (Filme f : filmes) {
            double media = calcularMedia(f.id);
            String mediaFormatada = media > 0
                    ? String.format(" | Média: %.1f/5", media)
                    : " | Sem avaliações";
            System.out.println(f + mediaFormatada);
        }
    }

    static void avaliarFilme() {
        listarFilmes();
        if (filmes.isEmpty()) return;

        int idFilme = lerInteiro("\nDigite o ID do filme: ");
        Filme filme = buscarFilmePorId(idFilme);
        if (filme == null) {
            System.out.println("Filme não encontrado.");
            return;
        }

        int nota;
        do {
            nota = lerInteiro("Nota (1 a 5): ");
        } while (nota < 1 || nota > 5);

        scanner.nextLine();
        System.out.print("Comentário: ");
        String comentario = scanner.nextLine();

        avaliacoes.add(new Avaliacao(proximoIdAvaliacao++, idFilme, nota, comentario));
        System.out.println("Avaliação registrada com sucesso!");
    }

    static void verAvaliacoes() {
        listarFilmes();
        if (filmes.isEmpty()) return;

        int idFilme = lerInteiro("\nDigite o ID do filme: ");
        Filme filme = buscarFilmePorId(idFilme);
        if (filme == null) {
            System.out.println("Filme não encontrado.");
            return;
        }

        List<Avaliacao> avaliacoesDoFilme = listarAvaliacoesPorFilme(idFilme);
        System.out.println("\n--- AVALIAÇÕES: " + filme.titulo.toUpperCase() + " ---");

        if (avaliacoesDoFilme.isEmpty()) {
            System.out.println("Ainda sem avaliações para este filme.");
            return;
        }

        for (Avaliacao a : avaliacoesDoFilme) {
            System.out.println(a);
        }

        double media = calcularMedia(idFilme);
        System.out.printf("\nMédia geral: %.2f/5 (%d avaliações)%n", media, avaliacoesDoFilme.size());
    }

    static void carregarDadosIniciais() {
        filmes.add(new Filme(proximoIdFilme++, "O Poderoso Chefão", "Francis Ford Coppola", 1972, "Drama/Crime"));
        filmes.add(new Filme(proximoIdFilme++, "Interestelar", "Christopher Nolan", 2014, "Ficção Científica"));
        filmes.add(new Filme(proximoIdFilme++, "Parasita", "Bong Joon-ho", 2019, "Thriller/Drama"));
    }

    static Filme buscarFilmePorId(int id) {
        for (Filme f : filmes) {
            if (f.id == id) return f;
        }
        return null;
    }

    static List<Avaliacao> listarAvaliacoesPorFilme(int filmeId) {
        List<Avaliacao> resultado = new ArrayList<>();
        for (Avaliacao a : avaliacoes) {
            if (a.filmeId == filmeId) resultado.add(a);
        }
        return resultado;
    }

    static double calcularMedia(int filmeId) {
        List<Avaliacao> lista = listarAvaliacoesPorFilme(filmeId);
        if (lista.isEmpty()) return 0;
        int soma = 0;
        for (Avaliacao a : lista) soma += a.nota;
        return (double) soma / lista.size();
    }

    static int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número.");
            }
        }
    }

    // ===================== CLASSES INTERNAS =====================

    static class Filme {
        int id;
        String titulo, diretor, genero;
        int ano;

        Filme(int id, String titulo, String diretor, int ano, String genero) {
            this.id = id;
            this.titulo = titulo;
            this.diretor = diretor;
            this.ano = ano;
            this.genero = genero;
        }

        @Override
        public String toString() {
            return String.format("[%d] %s (%d) - Dir: %s | Gênero: %s", id, titulo, ano, diretor, genero);
        }
    }

    static class Avaliacao {
        int id, filmeId, nota;
        String comentario;

        Avaliacao(int id, int filmeId, int nota, String comentario) {
            this.id = id;
            this.filmeId = filmeId;
            this.nota = nota;
            this.comentario = comentario;
        }

        String estrelasFormatadas() {
            return "★".repeat(nota) + "☆".repeat(5 - nota);
        }

        @Override
        public String toString() {
            return String.format("Nota: %s | Comentário: %s", estrelasFormatadas(), comentario);
        }
    }
}
