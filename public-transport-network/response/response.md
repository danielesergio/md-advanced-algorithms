# Domanda 1
Modellate il problema da risolvere usando un grafo orientato e pesato. Descrivete l'approccio 
che avete usato per creare il grafo, indicando cosa rappresentano i vertici del grafo, cosa 
rappresentano gli archi e quali pesi e valori avete associato agli archi.

## Risposta
I nodi del grafo rappresentano le stazioni e sono caratterizzate da:
* codice
* nome
* coordinate(latitudine e longitudine)

Un arco rappresenta tutti i possibili collegamenti diretti tra due stazioni (partendo dalla stazione 1 è possibile arrivare alla 
stazione due senza passare per altre stazioni).

Il peso di un arco è una funzione che prende in input l'orario di partenza e che ritorna l'orario di arrivo minimo es:

```
da stazion1 a stazione2 tramite bus1-> orario di partenza 17:30 orario di arrivo 18:00 

da stazion1 a stazione2 tramite bus2-> orario di partenza 17:50 orario di arrivo 18:45 

da stazion1 a stazione2 tramite treno-> orario di partenza 19:30 orario di arrivo 19:05

l'orario di arrivo minimo partendo alle 17:35 è 18:45.

l'orario di arrivo minimo partendo alle 19:31 è 42:00 -> le 18:00 del giorno sucessivo
 ```
 
E` stata scelta questa rappresentazione perchè:
* minimizza il numero dei vertici e degli archi
* presupponendo che gli orari siano gli stessi tutti i giorni, è semplice calcolare l'orario di arrivo nel caso in cui 
  si debba prendere il miglior treno nella giornata sucessiva

# Domanda 2
Risolvete il problema utilizzando uno degli algoritmi per il problema dei cammini minimi visti a lezione. Indicate quale
algoritmo avete utilizzato e se e come è stato modificato per poter risolvere il problema.
## Risposta
E` stato utilizzato l'algorimoto di DijkstraSSSP con heap binaria.
Sono state apportate le seguenti modifiche:
* la ricerca finisce non appena viene trovato il miglior percorso per la destinazione voluta.

# Domanda 3
Testate la vostra implementazione con i seguenti viaggi:

* Da 200415016 a 200405005, partenza non prima delle 09:30
* Da 300000032 a 400000122, partenza non prima delle 05:30
* Da 210602003 a 300000030, partenza non prima delle 06:30
* Da 200417051 a 140701016, partenza non prima delle 12:00
* Da 200417051 a 140701016, partenza non prima delle 23:55
* Altre tre combinazioni di viaggio scelte a piacere

Per ogni viaggio, fornite l'elenco delle corse che lo compongono, le stazioni di cambio con gli orari di partenza e l'orario di arrivo a destinazione.
## Risposta
### Da 200415016 a 200405005, partenza non prima delle 09:30
```
Viaggio da 200415016 a 200405005
Orario di partenza 09:30 
Orario di arrivo 09:52 
09:30: corsa 00360 RGTR-- da 200415016 a 200405026
09:40: corsa 01797 AVL--- da 200405026 a 200405020
09:50: corsa 06602 RGTR-- da 200405020 a 200405005
```
![](./from_200415016_to_200405005-00930.png?raw=true)
### Da 300000032 a 400000122, partenza non prima delle 05:30
```
Viaggio da 300000032 a 400000122
Orario di partenza 05:30
Orario di arrivo 13:50
06:26: corsa 07608 C88--- da 300000032 a 110606001
06:35: corsa 03781 C82--- da 110606001 a 200405035
07:46: corsa 00055 C82--- da 200405035 a 400000047
12:07: corsa 09879 C82--- da 400000047 a 400000122
```
![](./from_300000032_to_400000122-00530.png?raw=true)

### Da 210602003 a 300000030, partenza non prima delle 06:30
```
Viaggio da 210602003 a 300000030
Orario di partenza 06:30
Orario di arrivo 10:53
06:41 : corsa 00030 CFLBUS da 210602003 a 210201002
06:46 : corsa 00036 CFLBUS da 210201002 a 210502001
06:55 : corsa 00037 CFLBUS da 210502001 a 200404028
07:07 : corsa 01306 CFLBUS da 201103004 a 200404016
07:20 : corsa 00031 CFLBUS da 200404016 a 200405036
07:24 : corsa 01173 RGTR-- da 200405036 a 200405026
07:27 : corsa 04278 AVL--- da 200405026 a 200405035
07:40 : corsa 07630 C82--- da 200405035 a 300000030
```
![](./from_210602003_to_300000030-00630.png?raw=true)

### Da 200417051 a 140701016, partenza non prima delle 012:00
```
Viaggio da 200417051 a 140701016
Orario di partenza 12:00 
Orario di arrivo 12:43 
12:20 : corsa 03762 C82--- da 200417051 a 140701016
```
![](./from_200417051_to_140701016-01200.png?raw=true)

### Da 200417051 a 140701016, partenza non prima delle 023:55
```
Viaggio da 200417051 a 140701016
Orario di partenza 23:55 
Orario di arrivo 00:44 (1 giorni dopo)
00:09 (1 giorni dopo): corsa 03623 C82--- da 200417051 a 140701016
```
![](./from_200417051_to_140701016-02355.png?raw=true)

### Da 500000136 a 140701016, partenza non prima delle 021:33
```
Viaggio da 500000136 a 220901003
Orario di partenza 21:33 
Orario di arrivo 07:20 (1 giorni dopo)
04:51 (1 giorni dopo): corsa 08441 RGTR-- da 500000136 a 140701016
06:16 (1 giorni dopo): corsa 03780 C82--- da 140701016 a 200405035
06:50 (1 giorni dopo): corsa 05056 C82--- da 200405035 a 220901002
07:17 (1 giorni dopo): corsa 00226 TIC--- da 220901002 a 22090100
```
![](./from_500000136_to_220901003-02133.png?raw=true)

### Da 110101015 a 220201064, partenza non prima delle 011:13
```
Viaggio da 110101015 a 220201064
Orario di partenza 11:13 
Orario di arrivo 13:33 
11:32 : corsa 08689 RGTR-- da 110101015 a 110101002
11:44 : corsa 00110 C88--- da 110101002 a 200405035
12:50 : corsa 05062 C82--- da 200405035 a 220902006
13:23 : corsa 05229 CFLBUS da 220902006 a 220201064
```
![](./from_110101015_to_220201064-01113.png?raw=true)

### Da 500000079 a 400000122, partenza non prima delle 014:23
```
Viaggio da 500000079 a 400000122
Orario di partenza 14:23 
Orario di arrivo 13:50 (1 giorni dopo)
14:37 : corsa 05114 C82--- da 500000079 a 200405035
15:32 : corsa 06765 C82--- da 200405035 a 220102005
15:51 : corsa 00040 C82--- da 220102005 a 400000093
16:03 : corsa 00037 C82--- da 400000093 a 400000047
08:08 (1 giorni dopo): corsa 09881 C82--- da 400000047 a 400000121
13:32 (1 giorni dopo): corsa 09879 C82--- da 400000121 a 400000122
```
![](./from_500000079_to_400000122-01423.png?raw=true)

# Domanda 4
Commentate la qualità delle soluzioni trovate dalla vostra implementazione: rappresentano soluzioni di viaggio ragionevoli 
oppure no? In caso negativo, quali sono i motivi che portano la vostra implementazione a generare soluzioni di viaggio 
irragionevoli? Ci sono dei modi per evitare che lo faccia?

# Risposta
No non sono soluzioni di viaggio ragionevoli.
La soluzione proposta minimizza l'orario di arrivo senza considerare minimanente altri parametri importanti per un viaggio:
* numero di cambi effettuati
* tempi morti (attesa di cambio)

Per ottenere delle soluzioni ragionevoli bisognerebbe modificare la funzione che calcola il peso totale di un percorso perche' tenga conto di questi
fattori.

Una possibile soluzione potrebbe essere definita come segue:
* modificare la funzione peso degli archi perchè ritorini la durata effettiva del viaggio in secondi (invece che un orario).
* calcolare il peso del percorso come segue: 
```
w(u,v) = α * numero_cambi + β * secondi_attesa_stazione + secondi_trascorsi_in_treno

con α e β costanti arbitrarie

numero_cambi = numero di cambi effettuati durante il tragitto

secondi_attesa_stazione = numero di secondi rimasti ad attendere che arrivi il prossimo treno

secondi_trascorsi_in_treno = numero di secondi rimasto in treno

u, v due stazioni
```
