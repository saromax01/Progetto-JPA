package entites;

import javax.persistence.*;
import java.util.List;

@Entity
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cognome;
    private String dataNascita;
    private String numeroTessera;

    @OneToMany(mappedBy = "utente")
    private List<Prestito> prestiti;

    public Utente() {
        // Costruttore vuoto richiesto da JPA
    }

    public Utente(Long id, String nome, String cognome, String dataNascita, String numeroTessera, List<Prestito> prestiti) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.numeroTessera = numeroTessera;
        this.prestiti = prestiti;
    }

    public Utente(String nome, String cognome, String dataNascita, String numeroTessera) {
        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.numeroTessera = numeroTessera;
        this.prestiti = null; // o puoi inizializzare con una lista vuota se preferisci
    }

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

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(String dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getNumeroTessera() {
        return numeroTessera;
    }

    public void setNumeroTessera(String numeroTessera) {
        this.numeroTessera = numeroTessera;
    }

    public List<Prestito> getPrestiti() {
        return prestiti;
    }

    public void setPrestiti(List<Prestito> prestiti) {
        this.prestiti = prestiti;
    }
}
