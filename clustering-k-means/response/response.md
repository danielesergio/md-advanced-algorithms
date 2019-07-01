# Consegna
Consegna di Daniele Sergio 1127732

Componenti gruppo: Daniele Sergio

# Premessa
In quasi tutte le domande sono stati testati 4 varianti dell'algoritmo di clustering k-means.

* `algoritmo Es4`: l'algoritmo implementato per la precendente esercitazione chiamato 
* `algoritmo Parallel no threshold`: l'algoritmo parallelo implementato per questa esercitazione senza nessuna soglia per limitare il parallelismo nel calcolo del nuovo centro 
* `algoritmo Parallel with threshold`: l'algoritmo parallelo implementato per questa esercitazione con una soglia per limitare il parallelismo nel calcolo del nuovo centro (soglia pari a 100)
* `algoritmo Serial`: la serializzazione dell'algoritmo parallelo

E\` stata fatta questa scelta perche\` l'algoritmo implementato nella esercitazione precedente e\` piu\` efficiente di quello seriallizzato.
In particolare la complessita\` dei due algoritmi varia nella fase di aggirnamento dei centri:
* `algoritmo Serial` ha una complessita\` di O(nk) 
* `algoritmo Es4` ha una complessita\` di O(n)

dove `n` e\` il numero di punti e `k` e\` il numero di cluster

Per quanto riguarda il framework per il parallelismo e\` stato utilzzato il framework `Fork/Join` di java e, per i cicli `parellal`,
 sono stati utilizzati gli stream paralleli che a loro volta utilizzano il framework `Fork/Join`.
 
 Come pool di thread e\` stato utilizzato quello di default del framework che utilizza una soglia di parallelismo pari al numero di Thread - 1, (11 nel pc di test utilizzato)  
 
 Per quanto riguarda gli SpeedUp calcolati si sono confrontati:
  * `algoritmo Es4` con `algoritmo Parallel no threshold`
  * `algoritmo Es4` con `algoritmo Parallel with threshold`
  * `algoritmo Serial` con `algoritmo Parallel no threshold`
  * `algoritmo Serial` con `algoritmo Parallel with threshold`
  
# Domanda 1
Confrontate i tempi di calcolo dell'algoritmo seriale e dell'algoritmo parallelo per il clustering k-means, al variare del numero di punti. 

Usate le soglie di popolazione minima 250, 2.000, 5.000, 15.000, 50.000, e 100.000 per eliminare città e paesi con bassa popolazione e ottenere dataset più piccoli con meno punti. 

Misurate i tempi di calcolo dell'algoritmo seriale e di quello parallelo sul dataset complessivo con 38183 punti e su quelli ridotti. Per tutti i dataset, fissate il numero di cluster a 50 ed il numero di iterazioni a 100.

Dopo aver misurato i tempi, create un grafico che mostri la variazione dei tempi di calcolo dei due algoritmi al variare del numero di punti. La figura dovrebbe includere due curve disegnate, una per l'algoritmo seriale e una per l'algoritmo parallelo. L'asse orizzontale indica il numero di punti mentre l'asse verticale indica la il tempo di calcolo. 

Calcolate lo speedup ottenuto dall'algoritmo parallelo. Come varia rispetto al numero dei punti?

## Risposta

### Grafico Tempi esecuzioni 

![](./question_1.png?raw=true)

### Grafici Seepup

![](./question_1_speedUp.png?raw=true)

# Domanda 2
Confrontate i tempi di calcolo dell'algoritmo seriale e dell'algoritmo parallelo per il clustering k-means, al variare del numero di cluster. 

Usando il dataset complessivo con 38183 punti, misurate i tempi di calcolo dell'algoritmo seriale e di quello parallelo variando il numero di cluster da 10 a 100. Mantenete costante il numero di iterazioni a 100.

Dopo aver misurato i tempi, create un grafico che mostri la variazione dei tempi di calcolo dei due algoritmi al variare del numero di cluster. In questo caso l'asse orizzontale indica il numero di cluster mentre l'asse verticale indica la il tempo di calcolo.

Calcolate lo speedup ottenuto dall'algoritmo parallelo. Come varia rispetto al numero dei cluster?
## Risposta

### Grafico Tempi esecuzioni 

![](./question_2.png?raw=true)

### Grafici Seepup

![](./question_2_speedUp.png?raw=true)

# Domanda 3
Confrontate i tempi di calcolo dell'algoritmo seriale e dell'algoritmo parallelo per il clustering k-means, al variare del numero di iterazioni. 

Usando il dataset complessivo con 38183 punti, misurate i tempi di calcolo dell'algoritmo seriale e di quello parallelo variando il numero di iterazioni da 10 a 1000. Mantenete costate il numero di cluster a 50.

Dopo aver misurato i tempi, create un grafico che mostri la variazione dei tempi di calcolo dei due algoritmi al variare del numero di iterazioni. In questo caso l'asse orizzontale indica il numero di iterazioni mentre l'asse verticale indica la il tempo di calcolo.

Calcolate lo speedup ottenuto dall'algoritmo parallelo. Come varia rispetto al numero di iterazioni?
## Risposta

### Grafico Tempi esecuzioni 

![](./question_3.png?raw=true)

### Grafici Seepup

![](./question_3_speedUp.png?raw=true)

# Domanda 4
Per migliorare i tempi di calcolo di un algoritmo risulta spesso utile limitare il parallelismo, passando ad una procedura seriale quando la dimensione dell'input scende sotto una certa soglia. Per esempio, in un algoritmo divide-et-impera si può stabilire una soglia di cutoff al di sotto della quale si utilizza un algoritmo seriale iterativo, invece di dividere ulteriormente l'input e procedere in parallelo.

Usando il dataset complessivo con 38183 punti, misurate i tempi di calcolo dell'algoritmo di clustering k-means parallelo utilizzando diverse soglie di cutoff per controllare la granularità del parallelismo.

Dopo aver misurato i tempi, create un grafico che mostri la variazione dei tempi di calcolo al variare del cutoff. Quale valore di cutoff vi permette di ottenere le prestazioni migliori?
## Risposta

![](./question_4_threshold_centroid.png?raw=true)

#Domanda 5

Specificare le caratteristiche hardware del computer dove sono stati eseguiti i test, in particolare processore e numero di core disponibili.

## Risposta
Pc notebook `Dell XPS 15 9570`

Cpu [Intel Core i7-8750H](https://ark.intel.com/content/www/us/en/ark/products/134906/intel-core-i7-8750h-processor-9m-cache-up-to-4-10-ghz.html), 6 Core 12 Thread

Ram  `16GB`

