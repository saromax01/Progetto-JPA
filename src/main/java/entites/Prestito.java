package entites;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
public class Prestito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;

    @ManyToOne
    @JoinColumn(name = "elemento_id")
    private ElementoCatalogo elementoPrestato;

    @Temporal(TemporalType.DATE)
    private Date dataInizioPrestito;

    @Temporal(TemporalType.DATE)
    private Date dataRestituzionePrevista;

    @Temporal(TemporalType.DATE)
    private Date dataRestituzioneEffettiva;

    public Prestito() {
        // Costruttore vuoto richiesto da JPA
    }

    public Prestito(Utente utente, ElementoCatalogo elementoPrestato, Date dataInizioPrestito) {
        this.utente = utente;
        this.elementoPrestato = elementoPrestato;
        this.dataInizioPrestito = dataInizioPrestito;

        // Calcola la dataRestituzionePrevista (30 giorni dalla data di inizio)
        Calendar cal = Calendar.getInstance();
        cal.setTime(dataInizioPrestito);
        cal.add(Calendar.DATE, 30);
        this.dataRestituzionePrevista = cal.getTime();

        // Inizializza la dataRestituzioneEffettiva a null
        this.dataRestituzioneEffettiva = null;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public ElementoCatalogo getElementoPrestato() {
        return elementoPrestato;
    }

    public void setElementoPrestato(ElementoCatalogo elementoPrestato) {
        this.elementoPrestato = elementoPrestato;
    }

    public Date getDataInizioPrestito() {
        return dataInizioPrestito;
    }

    public void setDataInizioPrestito(Date dataInizioPrestito) {
        this.dataInizioPrestito = dataInizioPrestito;
    }

    public Date getDataRestituzionePrevista() {
        return dataRestituzionePrevista;
    }

    public void setDataRestituzionePrevista(Date dataRestituzionePrevista) {
        this.dataRestituzionePrevista = dataRestituzionePrevista;
    }

    public Date getDataRestituzioneEffettiva() {
        return dataRestituzioneEffettiva;
    }

    public void setDataRestituzioneEffettiva(Date dataRestituzioneEffettiva) {
        this.dataRestituzioneEffettiva = dataRestituzioneEffettiva;
    }
}
