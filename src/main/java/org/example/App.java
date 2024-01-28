package org.example;

import com.github.javafaker.Faker;
import entites.*;
import utility.CatalogoManager;
import utility.Periodicita;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class App {

  public static void main(String[] args) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("libreria");
    EntityManager em = emf.createEntityManager();

    Faker faker = new Faker();
    Scanner scanner = new Scanner(System.in);

    CatalogoManager manager = new CatalogoManager(em);

    for (int i = 0; i < 5; i++) {
      Libro libro = new Libro(
              faker.code().isbn13(),
              faker.book().title(),
              faker.number().numberBetween(1900, 2022),
              faker.number().numberBetween(50, 500),
              faker.book().author(),
              faker.book().genre()
      );
      manager.aggiungiElementoCatalogo(libro);
    }

    for (int i = 0; i < 5; i++) {
      Rivista rivista = new Rivista(
              faker.code().isbn13(),
              faker.book().title(),
              faker.number().numberBetween(1900, 2022),
              faker.number().numberBetween(20, 200),
              Periodicita.values()[faker.number().numberBetween(0, Periodicita.values().length)]
      );
      manager.aggiungiElementoCatalogo(rivista);
    }

    String inputIsbn = scanner.nextLine();
    ElementoCatalogo elementoByISBN = manager.ricercaElementoPerISBN(inputIsbn);
    if (elementoByISBN != null) {
      System.out.println("Elemento trovato per ISBN: " + elementoByISBN.getTitolo());
    } else {
      System.out.println("Nessun elemento trovato per l'ISBN specificato.");
    }

    List<ElementoCatalogo> elementiPerAnno = manager.ricercaElementiPerAnnoPubblicazione(2000);
    System.out.println("Numero di elementi trovati per l'anno 2000: " + elementiPerAnno.size());

    List<ElementoCatalogo> libriDiUnAutore = manager.ricercaElementiPerAutore("Autore_Del_Libro");
    System.out.println("Numero di libri trovati per l'autore: " + libriDiUnAutore.size());

    List<ElementoCatalogo> elementiPerTitolo = manager.ricercaElementiPerTitolo("Parola_Nel_Titolo");
    System.out.println("Numero di elementi trovati per il titolo: " + elementiPerTitolo.size());

    Utente utente = new Utente(
            faker.name().firstName(),
            faker.name().lastName(),
            faker.date().birthday().toString(),
            faker.number().digits(8)
    );
    manager.aggiungiUtente(utente);

    String inputIsbn2 = scanner.nextLine();
    ElementoCatalogo elementoPrestato = manager.ricercaElementoPerISBN(inputIsbn2);
    if (elementoPrestato != null) {
      Prestito prestito = new Prestito(
              utente,
              elementoPrestato,
              new Date()
      );
      manager.aggiungiPrestito(prestito);
    }

    List<Prestito> prestitiUtente = manager.ricercaPrestitiPerNumeroTessera(utente.getNumeroTessera());
    System.out.println("Numero di prestiti per l'utente: " + prestitiUtente.size());

    List<Prestito> prestitiScaduti = manager.ricercaPrestitiScadutiNonRestituiti();
    System.out.println("Numero di prestiti scaduti e non restituiti: " + prestitiScaduti.size());

    manager.chiudiEntityManager();
  }
}
