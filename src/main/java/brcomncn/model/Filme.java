package brcomncn.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Filme {
    private Long id;
    private String nome;
    private LocalDate dataAssistido;
    private String localAssistido;
    private LocalDateTime dataCadastro;

    // Construtores
    public Filme() {}

    public Filme(String nome, LocalDate dataAssistido, String localAssistido) {
        this.nome = nome;
        this.dataAssistido = dataAssistido;
        this.localAssistido = localAssistido;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataAssistido() {
        return dataAssistido;
    }

    public void setDataAssistido(LocalDate dataAssistido) {
        this.dataAssistido = dataAssistido;
    }

    public String getLocalAssistido() {
        return localAssistido;
    }

    public void setLocalAssistido(String localAssistido) {
        this.localAssistido = localAssistido;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
}