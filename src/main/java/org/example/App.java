package org.example;

import com.github.javafaker.Faker;
import entites.ElementoCatalogo;
import entites.Libro;
import entites.Prestito;
import entites.Utente;
import utility.CatalogoManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
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

    while (true) {
      System.out.println("Scegli un'operazione:");
      System.out.println("1. Aggiungi elemento al catalogo");
      System.out.println("2. Cerca elemento per ISBN");
      System.out.println("3. Cerca elementi per anno di pubblicazione");
      System.out.println("4. Cerca elementi per autore");
      System.out.println("5. Cerca elementi per titolo");
      System.out.println("6. Aggiungi utente");
      System.out.println("7. Aggiungi prestito");
      System.out.println("8. Cerca prestiti per numero tessera utente");
      System.out.println("9. Cerca prestiti scaduti e non restituiti");
      System.out.println("0. Esci");

      int scelta = 0;

      try {
        scelta = Integer.parseInt(scanner.nextLine());
      } catch (NumberFormatException e) {
        System.out.println("Inserisci un numero valido.");
        scanner.nextLine();
        continue; // Torna all'inizio del ciclo
      }

      switch (scelta) {
        case 1:
          // Aggiungi elemento al catalogo
          Libro libro = new Libro(
                  faker.code().isbn13(),
                  faker.book().title(),
                  faker.number().numberBetween(1900, 2022),
                  faker.number().numberBetween(50, 500),
                  faker.book().author(),
                  faker.book().genre()
          );
          manager.aggiungiElementoCatalogo(libro);
          break;
        case 2:
          // Cerca elemento per ISBN
          System.out.println("Inserisci l'ISBN:");
          String isbn = scanner.nextLine();
          try {
            ElementoCatalogo elementoByISBN = manager.ricercaElementoPerISBN(isbn);
            if (elementoByISBN != null) {
              System.out.println("Elemento trovato per ISBN: " + elementoByISBN.getTitolo());
            } else {
              System.out.println("Nessun elemento trovato per l'ISBN specificato.");
            }
          } catch (NoResultException e) {
            System.out.println("Nessun elemento trovato per l'ISBN specificato.");
          }
          break;

        case 3:
          // Cerca elementi per anno di pubblicazione
          System.out.println("Inserisci l'anno di pubblicazione:");
          int anno = scanner.nextInt();
          List<ElementoCatalogo> elementiPerAnno = manager.ricercaElementiPerAnnoPubblicazione(anno);
          if (!elementiPerAnno.isEmpty()) {
            System.out.println("Numero di elementi trovati per l'anno " + anno + ": " + elementiPerAnno.size());
          } else {
            System.out.println("Nessun elemento trovato per l'anno specificato.");
          }
          break;

        case 4:
          // Cerca elementi per autore
          System.out.println("Inserisci l'autore:");
          String autore = scanner.nextLine();
          List<ElementoCatalogo> elementiPerAutore = manager.ricercaElementiPerAutore(autore);
          if (!elementiPerAutore.isEmpty()) {
            System.out.println("Numero di elementi trovati per l'autore " + autore + ": " + elementiPerAutore.size());
          } else {
            System.out.println("Nessun elemento trovato per l'autore specificato.");
          }
          break;

        case 5:
          // Cerca elementi per titolo
          System.out.println("Inserisci il titolo:");
          String titolo = scanner.nextLine();
          List<ElementoCatalogo> elementiPerTitolo = manager.ricercaElementiPerTitolo(titolo);
          if (!elementiPerTitolo.isEmpty()) {
            System.out.println("Numero di elementi trovati per il titolo " + titolo + ": " + elementiPerTitolo.size());
          } else {
            System.out.println("Nessun elemento trovato per il titolo specificato.");
          }
          break;

        case 6:
        // Aggiungi utente
        System.out.println("Inserisci il numero tessera utente:");
        String numeroTesser = scanner.nextLine();
        // Consuma il carattere di nuova linea nel buffer
        scanner.nextLine();

        System.out.println("Inserisci il nome:");
        String nome = scanner.nextLine();

        System.out.println("Inserisci il cognome:");
        String cognome = scanner.nextLine();

        System.out.println("Inserisci la data di nascita:");
        String dataNascita = scanner.nextLine();

        Utente utente = new Utente(numeroTesser, nome, cognome, dataNascita);
        manager.aggiungiUtente(utente);
        System.out.println("Utente aggiunto con successo.");
        break;

        case 7:
          // Aggiungi prestito
          System.out.println("Inserisci il numero tessera utente:");
          String numeroTesseraUtente = scanner.nextLine();
          Utente utentePrestito = manager.ricercaUtentePerNumeroTessera(numeroTesseraUtente);

          if (utentePrestito != null) {
            System.out.println("Inserisci l'ISBN del libro:");
            String isbnPrestito = scanner.nextLine();
            ElementoCatalogo elementoPrestito = manager.ricercaElementoPerISBN(isbnPrestito);

            if (elementoPrestito != null) {
              // Dichiarazione e inizializzazione di dataInizioPrestito
              Date dataInizioPrestito = faker.date().future(10, java.util.concurrent.TimeUnit.DAYS);

              // Creazione di un nuovo prestito
              Prestito prestito = new Prestito();
              prestito.setUtente(utentePrestito);
              prestito.setElementoPrestato(elementoPrestito);
              prestito.setDataInizioPrestito(dataInizioPrestito);

              // Aggiungi il prestito usando il manager
              manager.aggiungiPrestito(prestito);
              System.out.println("Prestito aggiunto con successo.");
            } else {
              System.out.println("Elemento non trovato per l'ISBN specificato.");
            }
          } else {
            System.out.println("Utente non trovato per il numero tessera specificato.");
          }
          break;

        case 8:
          // Cerca prestiti per numero tessera utente
          System.out.println("Inserisci il numero tessera utente:");
          String numeroTessera = scanner.nextLine();
          List<Prestito> prestitiUtente = manager.ricercaPrestitiPerNumeroTessera(numeroTessera);
          if (!prestitiUtente.isEmpty()) {
            System.out.println("Numero di prestiti per l'utente " + numeroTessera + ": " + prestitiUtente.size());
          } else {
            System.out.println("Nessun prestito trovato per l'utente specificato.");
          }
          break;

        case 9:
          // Cerca prestiti scaduti e non restituiti
          List<Prestito> prestitiScaduti = manager.ricercaPrestitiScadutiNonRestituiti();
          if (!prestitiScaduti.isEmpty()) {
            System.out.println("Numero di prestiti scaduti e non restituiti: " + prestitiScaduti.size());
          } else {
            System.out.println("Nessun prestito scaduto e non restituito trovato.");
          }
          break;

        case 0:
          // Esci
          manager.chiudiEntityManager();
          scanner.close();
          System.exit(0);
        default:
          System.out.println("Scelta non valida. Riprova.");
          break;
      }
    }
  }
}
