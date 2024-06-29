# PeakPulse

PeakPulse è un'applicazione web sviluppata con Java 17 e Spring Boot 3.3.0. L'applicazione permette agli utenti di esplorare attività sportive e di avventura in montagna. Gli utenti possono registrarsi come esperti per proporre attività e recensioni, mentre gli amministratori possono gestire tutte le funzionalità del sito.

## Descrizione

Il progetto "PeakPulse" è un'applicazione web che offre una piattaforma per esplorare e proporre attività montane. Gli utenti non registrati possono visualizzare esperti e attività disponibili, mentre gli utenti registrati come esperti possono proporre nuove attività, aggiungere attrezzature e recensire le attività di altri esperti. Gli amministratori hanno il controllo completo per gestire tutte le funzionalità del sito.

## Modello di dominio
![MODELLO_PP_1 (1)](https://github.com/Ziappo24/SIW-PEAKPULSE-REPO/assets/128827674/be50cf16-f827-420b-a7d8-fb7c1c826083)


## Struttura del Progetto e Tecnologie Utilizzate

**PostgreSQL:** Database relazionale utilizzato per memorizzare i dati dell'applicazione.
- **Thymeleaf:** Motore di template utilizzato per creare le pagine web dinamiche.
- **Java 17:** Linguaggio di programmazione utilizzato per sviluppare l'applicazione.
- **Maven:** Strumento di gestione dei progetti utilizzato per gestire le dipendenze e il ciclo di vita del progetto.

## Prerequisiti

- Java 17
- Maven
- PostgreSQL

## Dipendenze

Le principali dipendenze utilizzate nel progetto sono:

- `spring-boot-starter-data-jpa`: per la persistenza dei dati.
- `spring-boot-starter-thymeleaf`: per il motore di template.
- `spring-boot-starter-validation`: per la validazione dei dati.
- `spring-boot-starter-web`: per creare applicazioni web.
- `spring-boot-starter-security`: per la sicurezza dell'applicazione.
- `spring-boot-starter-oauth2-client`: per l'autenticazione e l'autorizzazione OAuth2.
- `lombok`: per ridurre il codice boilerplate.
- `postgresql`: driver JDBC per PostgreSQL.
- `spring-boot-starter-test`: per il testing.

## Configurazione del Database

Assicurati di avere PostgreSQL installato e in esecuzione. Crea un database per l'applicazione e aggiorna le credenziali di connessione nel file `application.properties`.

Esempio di configurazione in `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/peakpulse
spring.datasource.username=tuo_username
spring.datasource.password=tuo_password
spring.jpa.hibernate.ddl-auto=update
```
## Struttura dei Pacchetti

- **controller:** Contiene i controller dell'applicazione che gestiscono le richieste HTTP.
- **model:** Contiene le entità JPA che rappresentano il modello di dominio.
- **repository:** Contiene le interfacce che estendono `JpaRepository` per la persistenza dei dati.
- **service:** Contiene la logica di business dell'applicazione.

## Comunicazione tra gli Strati dell'Architettura

L'architettura dell'applicazione è suddivisa in vari strati che comunicano tra loro utilizzando le tecnologie specifiche di Spring Boot:

- **Controller:** I controller gestiscono le richieste HTTP provenienti dagli utenti. Utilizzando le annotazioni di Spring MVC come `@Controller` e `@RequestMapping`, i controller ricevono le richieste, interagiscono con i servizi per elaborare i dati e restituiscono le viste generate da Thymeleaf.

- **Service:** Il livello di servizio contiene la logica di business dell'applicazione. I servizi sono annotati con `@Service` e vengono utilizzati dai controller per eseguire operazioni complesse. Questo strato interagisce con i repository per recuperare e salvare i dati nel database.

- **Repository:** Il livello di repository è responsabile della persistenza dei dati. Utilizza Spring Data JPA per interagire con il database PostgreSQL. Le interfacce dei repository estendono `JpaRepository` e sono annotate con `@Repository`, permettendo così l'uso di metodi predefiniti per operazioni CRUD.

- **Model:** Il modello contiene le entità JPA che rappresentano le tabelle del database. Le entità sono annotate con `@Entity` e mappano le relazioni tra gli oggetti e le tabelle del database.

- **Thymeleaf:** Thymeleaf è utilizzato come motore di template per generare pagine web dinamiche. I controller passano i dati al motore di template, che li utilizza per creare le viste HTML da restituire agli utenti.

Questa suddivisione in strati e l'utilizzo di Spring Boot consente una comunicazione chiara e strutturata tra le varie componenti dell'applicazione, facilitando la manutenzione e l'estendibilità del codice.

# Alcune preview del sito!

# Schermata Iniziale Dinamica
![Immagine 2024-06-29 133119](https://github.com/Ziappo24/SIW-PEAKPULSE-REPO/assets/128827644/72c36e11-ce57-4285-9bc5-856c1fd4477d)

# Elenchi presenti sul sito
![Immagine 2024-06-29 133147](https://github.com/Ziappo24/SIW-PEAKPULSE-REPO/assets/128827644/d8f0d96e-ae62-4dec-8a31-3a99a60bcc28)

# Pagina di Login con accesso a Google
![Immagine 2024-06-29 133212](https://github.com/Ziappo24/SIW-PEAKPULSE-REPO/assets/128827644/0dcf8736-2a0f-45fc-aabb-9fca6fd163b7)

# Pagina di una singola attività
![Immagine 2024-06-29 133312](https://github.com/Ziappo24/SIW-PEAKPULSE-REPO/assets/128827644/64c48322-4059-449d-ae25-87979634f0c7)

# Sezione di attrezzature richieste per l'attività
![Immagine 2024-06-29 133333](https://github.com/Ziappo24/SIW-PEAKPULSE-REPO/assets/128827644/fe68cf4f-30aa-48f9-b28a-aba970998187)

# Sezione deidcata alle recensioni degl altri esperti utenti
![Immagine 2024-06-29 133357](https://github.com/Ziappo24/SIW-PEAKPULSE-REPO/assets/128827644/6472f9fe-510b-4400-96c5-dffd19491ad0)







