package utility;

import entites.ElementoCatalogo;
import entites.Prestito;
import entites.Utente;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import java.util.Date;
import java.util.List;

public class CatalogoManager {

    private EntityManager em;

    public CatalogoManager(EntityManager em) {
        this.em = em;
    }

    public void aggiungiElementoCatalogo(ElementoCatalogo elemento) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(elemento);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public ElementoCatalogo ricercaElementoPerISBN(String isbn) {
        return em.createQuery("SELECT e FROM ElementoCatalogo e WHERE e.isbn = :isbn", ElementoCatalogo.class)
                .setParameter("isbn", isbn)
                .getSingleResult();
    }

    public List<ElementoCatalogo> ricercaElementiPerAnnoPubblicazione(int anno) {
        return em.createQuery("SELECT e FROM ElementoCatalogo e WHERE e.annoPubblicazione = :anno", ElementoCatalogo.class)
                .setParameter("anno", anno)
                .getResultList();
    }

    public List<ElementoCatalogo> ricercaElementiPerAutore(String autore) {
        return em.createQuery("SELECT e FROM ElementoCatalogo e WHERE e.autore = :autore", ElementoCatalogo.class)
                .setParameter("autore", autore)
                .getResultList();
    }

    public List<ElementoCatalogo> ricercaElementiPerTitolo(String titolo) {
        return em.createQuery("SELECT e FROM ElementoCatalogo e WHERE e.titolo LIKE :titolo", ElementoCatalogo.class)
                .setParameter("titolo", "%" + titolo + "%")
                .getResultList();
    }

    public void aggiungiUtente(Utente utente) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(utente);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void aggiungiPrestito(Prestito prestito) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(prestito);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public List<Prestito> ricercaPrestitiPerNumeroTessera(String numeroTessera) {
        return em.createQuery("SELECT p FROM Prestito p WHERE p.utente.numeroTessera = :numeroTessera", Prestito.class)
                .setParameter("numeroTessera", numeroTessera)
                .getResultList();
    }

    public List<Prestito> ricercaPrestitiScadutiNonRestituiti() {
        Date today = new Date();
        return em.createQuery("SELECT p FROM Prestito p WHERE p.dataRestituzionePrevista < :today AND p.dataRestituzioneEffettiva IS NULL", Prestito.class)
                .setParameter("today", today)
                .getResultList();
    }
    public Utente ricercaUtentePerNumeroTessera(String numeroTessera) {
        try {
            return em.createQuery("SELECT u FROM Utente u WHERE u.numeroTessera = :numeroTessera", Utente.class)
                    .setParameter("numeroTessera", numeroTessera)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // Restituisci null se nessun utente viene trovato
        }
    }


    public void chiudiEntityManager() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}
