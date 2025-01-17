package brcomncn;

import brcomncn.dao.FilmeDAO;
import brcomncn.model.Filme;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final FilmeDAO filmeDAO = new FilmeDAO();
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        int opcao;
        do {
            try {
                System.out.println("\n=== MEUS FILMES ASSISTIDOS ===");
                System.out.println("1 - Cadastrar filme");
                System.out.println("2 - Listar filmes");
                System.out.println("3 - Buscar filme por nome");
                System.out.println("0 - Sair");
                System.out.print("Escolha uma opção: ");

                opcao = Integer.parseInt(scanner.nextLine());

                switch (opcao) {
                    case 1:
                        cadastrarFilme();
                        break;
                    case 2:
                        listarFilmes();
                        break;
                    case 3:
                        buscarFilmes();
                        break;
                    case 0:
                        System.out.println("Programa encerrado!");
                        break;
                    default:
                        System.out.println("Opção inválida!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite um número válido!");
                opcao = -1;
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
                opcao = -1;
            }
        } while (opcao != 0);
    }

    private static void cadastrarFilme() {
        try {
            System.out.println("\n=== CADASTRO DE FILME ===");

            System.out.print("Nome do filme: ");
            String nome = scanner.nextLine().trim();
            if (nome.isEmpty()) {
                throw new IllegalArgumentException("O nome do filme não pode estar vazio");
            }

            System.out.print("Data assistido (dd/MM/yyyy): ");
            String dataStr = scanner.nextLine().trim();
            LocalDate dataAssistido;
            try {
                dataAssistido = LocalDate.parse(dataStr, formatter);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Data inválida. Use o formato dd/MM/yyyy");
            }

            System.out.print("Local assistido: ");
            String localAssistido = scanner.nextLine().trim();
            if (localAssistido.isEmpty()) {
                throw new IllegalArgumentException("O local não pode estar vazio");
            }

            Filme filme = new Filme(nome, dataAssistido, localAssistido);

            // Adiciona log antes de salvar
            System.out.println("\nTentando salvar filme...");
            System.out.println("Nome: " + filme.getNome());
            System.out.println("Data: " + filme.getDataAssistido());
            System.out.println("Local: " + filme.getLocalAssistido());

            filmeDAO.salvar(filme);

            System.out.println("\nFilme cadastrado com sucesso!");
            System.out.println("ID gerado: " + filme.getId());

        } catch (IllegalArgumentException e) {
            System.out.println("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar filme: " + e.getMessage());
            e.printStackTrace(); // Importante para debug
        }
    }

    private static void listarFilmes() {
        try {
            System.out.println("\n=== FILMES ASSISTIDOS ===");

            List<Filme> filmes = filmeDAO.listarTodos();

            if (filmes.isEmpty()) {
                System.out.println("Nenhum filme cadastrado!");
                return;
            }

            for (Filme filme : filmes) {
                System.out.println("\nID: " + filme.getId());
                System.out.println("Nome: " + filme.getNome());
                System.out.println("Data assistido: " + filme.getDataAssistido().format(formatter));
                System.out.println("Local: " + filme.getLocalAssistido());
                System.out.println("Cadastrado em: " + filme.getDataCadastro());
                System.out.println("----------------------------------------");
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar filmes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void buscarFilmes() {
        try {
            System.out.println("\n=== BUSCA DE FILMES ===");

            System.out.print("Digite o nome do filme para buscar: ");
            String nomeBusca = scanner.nextLine().trim();

            List<Filme> filmes = filmeDAO.buscarPorNome(nomeBusca);

            if (filmes.isEmpty()) {
                System.out.println("Nenhum filme encontrado com este nome!");
                return;
            }

            System.out.println("\nFilmes encontrados:");
            for (Filme filme : filmes) {
                System.out.println("\nID: " + filme.getId());
                System.out.println("Nome: " + filme.getNome());
                System.out.println("Data assistido: " + filme.getDataAssistido().format(formatter));
                System.out.println("Local: " + filme.getLocalAssistido());
                System.out.println("Cadastrado em: " + filme.getDataCadastro());
                System.out.println("----------------------------------------");
            }
        } catch (Exception e) {
            System.out.println("Erro na busca: " + e.getMessage());
            e.printStackTrace();
        }
    }
}