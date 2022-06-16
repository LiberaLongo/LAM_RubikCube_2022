# LAM_RubikCube_2022
Progetto per l'esame di Laboratorio Applicazioni Mobili,
Anno: 2021-2022

#Features
Features Cubo di Rubik
App Android

Scopo dell'app:
imparare a risolvere il cubo di Rubik imparando
un algoritmo "completo" per la risoluzione.

- l'utente con l'app è in grado di risolvere il cubo di Rubik se conosce e sa applicare un algoritmo risolutivo.
- l'utente è in grado di visualizzare contemporaneamente il cubo di Rubik e un algoritmo risolutivo.
- l'utente è in grado di scrivere il proprio algoritmo risolutivo, sottoforma di testo.
- l'utente è in grado di importare un algoritmo risolutivo, sottoforma di pdf.
- l'utente può in qualunque momento salvare la configurazione del cubo nella memoria interna dell'applicazione.
- l'utente può in qualunque momento caricare la configurazione del cubo salvato dalla memoria interna dell'applicazione.
- l'utente può in qualunque momento scegliere di resettare il cubo.
- l'utente può in qualunque momento scegliere di randomizzare il cubo,
  facendo fare un certo numero di mosse casuali all'applicazione
  (in modo da avere sempre un cubo sempre "valido" e mai "impossibile").
- l'utente è in grado di vedere le ultime mosse che ha fatto con l'applicazione,
  se queste sono state fatte prima di aver salvato/ricaricato/resettato/randomizzato il cubo
  (poiché queste operazioni modificano la storia del cubo e non devono essere memorizzate a lungo termine).
- l'applicazione salva automaticamente l'ultima configurazione del cubo all'uscita dall'applicazione nella memoria interna,
  e durante lo spostamento tra finestre all'interno dell'applicazione, e si riavvia con l'ultima configurazione salvata.
- l'utente può cercare in rete un algoritmo in formato pdf o txt per visualizzarlo.
- l'utente può inviare e ricevere una configurazione del cubo da applicazioni di messaggistica,
  in modo da poter risolvere quella particolare "sfida" inviatagli.
- l'utente può cambiare i colori del cubo, per poterlo adattare
  ad un cubo che possiede in real life, personalizzarlo, o resettare i colori di default
  (ma visto che il primo cubo al mondo era mono-colore
  l'applicazione non si preoccupa di controllare quante facce abbiano lo stesso colore,
  se si volesse un cubo con 1 <= N <= 6 facce colorate allo stesso modo
  l'applicazione si limita a creare un cubo con le facce del colore richiesto).


Note:
per configurazione si intende lo stato attuale del cubo, ovvero dove si trova ogni tessera.

un cubo "originale" è un cubo tutte le tessere di ogni faccia ha lo stesso colore della tessera centrale di quella faccia.

un cubo "valido" è un cubo in cui facendo un certo numero di mosse si può tornare al cubo "originale".

un cubo "impossibile" è un cubo in cui non c'è nessun modo lecito (mosse) per tornare al cubo "originale",
esempio1: prendo un cubo "originale" in real life (6 colori distinti) e con la forza ruoto un solo vertice.
esempio2: prendo un cubo "originale" in real life (6 colori distinti) e gli stacco e riattacco male i colori delle tessere.

Nell'algoritmo risolutivo che conosco io (l'algoritmo a strati)
ci sono alcuni algoritmi per risolvere il cubo che possono "mettere nel posto giusto un vertice o un lato" (non necessariamente con angolazione giusta) o "mettere un vertice o un lato nella angolazione giusta)".
Per algoritmo "completo" intendo la strategia e l'insieme degli algoritmi che permettono, tramite un certo numero di mosse lecite, di partire da un qualsiasi configurazione "valida" di tornare alla configurazione "originale".
l'algoritmo a strati è completo poiché, pezzo-per-pezzo, strato-per-strato, mette a posto con mosse lecite il cubo da qualsiasi configurazione "valida" a quella "originale".
