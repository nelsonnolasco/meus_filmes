package brcomncn.dao;

import brcomncn.config.ConnectionFactory;
import brcomncn.model.Filme;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FilmeDAO {

    // CREATE - Salvar filme
    public void salvar(Filme filme) {
        String sql = "INSERT INTO filmes (nome, data_assistido, local_assistido) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, filme.getNome());
            stmt.setDate(2, Date.valueOf(filme.getDataAssistido()));
            stmt.setString(3, filme.getLocalAssistido());

            stmt.executeUpdate();

            // Recupera o ID gerado
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    filme.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar filme", e);
        }
    }

    // READ - Listar todos os filmes
    public List<Filme> listarTodos() {
        List<Filme> filmes = new ArrayList<>();
        String sql = "SELECT * FROM filmes ORDER BY data_assistido DESC";

        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                filmes.add(extrairFilme(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar filmes", e);
        }
        return filmes;
    }

    // READ - Buscar filme por ID
    public Optional<Filme> buscarPorId(Long id) {
        String sql = "SELECT * FROM filmes WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(extrairFilme(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar filme por ID", e);
        }
        return Optional.empty();
    }

    // READ - Buscar filmes por nome
    public List<Filme> buscarPorNome(String nome) {
        List<Filme> filmes = new ArrayList<>();
        String sql = "SELECT * FROM filmes WHERE nome LIKE ? ORDER BY data_assistido DESC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nome + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    filmes.add(extrairFilme(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar filmes por nome", e);
        }
        return filmes;
    }

    // UPDATE - Atualizar filme
    public void atualizar(Filme filme) {
        String sql = "UPDATE filmes SET nome = ?, data_assistido = ?, local_assistido = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, filme.getNome());
            stmt.setDate(2, Date.valueOf(filme.getDataAssistido()));
            stmt.setString(3, filme.getLocalAssistido());
            stmt.setLong(4, filme.getId());

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new RuntimeException("Filme não encontrado para atualização");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar filme", e);
        }
    }

    // DELETE - Excluir filme
    public void excluir(Long id) {
        String sql = "DELETE FROM filmes WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new RuntimeException("Filme não encontrado para exclusão");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir filme", e);
        }
    }

    // Método auxiliar para extrair filme do ResultSet
    private Filme extrairFilme(ResultSet rs) throws SQLException {
        Filme filme = new Filme();
        filme.setId(rs.getLong("id"));
        filme.setNome(rs.getString("nome"));
        filme.setDataAssistido(rs.getDate("data_assistido").toLocalDate());
        filme.setLocalAssistido(rs.getString("local_assistido"));
        filme.setDataCadastro(rs.getTimestamp("data_cadastro").toLocalDateTime());
        return filme;
    }

    // READ - Buscar filmes por período
    public List<Filme> buscarPorPeriodo(LocalDate inicio, LocalDate fim) {
        List<Filme> filmes = new ArrayList<>();
        String sql = "SELECT * FROM filmes WHERE data_assistido BETWEEN ? AND ? ORDER BY data_assistido DESC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(inicio));
            stmt.setDate(2, Date.valueOf(fim));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    filmes.add(extrairFilme(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar filmes por período", e);
        }
        return filmes;
    }

    // READ - Buscar filmes por local
    public List<Filme> buscarPorLocal(String local) {
        List<Filme> filmes = new ArrayList<>();
        String sql = "SELECT * FROM filmes WHERE local_assistido LIKE ? ORDER BY data_assistido DESC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + local + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    filmes.add(extrairFilme(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar filmes por local", e);
        }
        return filmes;
    }

    // READ - Contar total de filmes
    public long contarTotal() {
        String sql = "SELECT COUNT(*) FROM filmes";

        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar total de filmes", e);
        }
    }
}
