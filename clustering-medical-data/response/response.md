# Consegna
Consegna di Daniele Sergio 1127732

Componenti gruppo: Daniele Sergio

# Domanda 1
Create un'immagine dei 15 cluster generati applicando l'algoritmo di Clustering Gerarchico al set di dati completo con 3108 contee. 

Utilizzate un colore diverso per identificare ogni cluster. È possibile allegare un'immagine con le 3108 contee colorate per cluster o una visualizzazione ottimizzata con le contee colorate per cluster e collegate al centro dei relativi cluster con delle linee. Non è necessario includere assi, etichette degli assi o un titolo per questa immagine. ## Risposta
# Risposta
![](./question_1.png?raw=true)

# Domanda 2
Create un'immagine dei 15 cluster generati applicando 5 iterazioni dell'algoritmo k-means al set di dati completo con 3108 contee. I cluster iniziali devono corrispondere alle 15 contee con la popolazione maggiore.

Utilizzate un colore diverso per identificare ogni cluster. È possibile allegare un'immagine con le 3108 contee colorate per cluster o una visualizzazione ottimizzata con le contee colorate per cluster e collegate al centro dei relativi cluster con delle linee. Non è necessario includere assi, etichette degli assi o un titolo per questa immagine.
### Risposta
![](./question_2.png?raw=true)

# Domanda 3
Quale metodo di clustering è più veloce quando il numero di cluster di output è un numero piccolo o una piccola frazione del numero di punti del dataset? Fornite una breve spiegazione in termini dei tempi di esecuzione asintotici di entrambi i metodi. Assumete che HierarchicalClustering usi FastClosestPair e che k-means usi sempre un piccolo numero di iterazioni.
### Risposta

# Domanda 4
Create un'immagine dei 9 cluster generati applicando l'algoritmo di Clustering Gerarchico al set di dati con 212 contee. 

Utilizzate un colore diverso per identificare ogni cluster. È possibile allegare un'immagine con le 212 contee colorate per cluster o una visualizzazione ottimizzata con le contee colorate per cluster e collegate al centro dei relativi cluster con delle linee. Non è necessario includere assi, etichette degli assi o un titolo per questa immagine.

### Risposta
![](./question_4.png?raw=true)

# Domanda 5
Create un'immagine dei 9 cluster generati applicando 5 iterazioni dell'algoritmo k-means al set di dati con 212 contee. I cluster iniziali devono corrispondere alle 9 contee con la popolazione maggiore. 

Utilizzate un colore diverso per identificare ogni cluster. È possibile allegare un'immagine con le 212 contee colorate per cluster o una visualizzazione ottimizzata con le contee colorate per cluster e collegate al centro dei relativi cluster con delle linee. Non è necessario includere assi, etichette degli assi o un titolo per questa immagine.
### Risposta
![](./question_5.png?raw=true)

# Domanda 6
Calcolate la distorsione dei clustering che avete ottenuto per le domande 4 e 5. Specificate i valori di distorsione (con almeno quattro cifre significative) dei due clustering nella casella di testo sottostante, indicando a quale dei due algoritmi si riferisce ogni valore. 
### Risposta
Distorsione algoritmo Gerarchico: 1.9675221337495978 10^11

Distorsione algoritmo K-Means: 9.538276536533678 10^10

# Domanda 7
Esaminate i clustering generati nelle domande 4 e 5. In particolare, concentrate la vostra attenzione sul numero e sulla forma dei cluster situati nella costa occidentale degli Stati Uniti.

Descrivete la differenza tra le forme dei cluster prodotti da questi due metodi sulla costa occidentale degli Stati Uniti. Per quale motivo un metodo produce un clustering con una distorsione molto più alta dell'altro? Per aiutarvi a rispondere a questa domanda, dovreste considerare in che modo k-means clustering genera il clustering iniziale in questo caso. Nello spiegare la vostra risposta, rivedete la geografia della costa occidentale degli Stati Uniti.
### Risposta
L'algoritmo di clustering gerarchico riduce il numero di cluster ad ogni iterazione unendo i due cluster che sono alla distanza minima;
poichè le città più popolose degli USA sulla costa occidentale sono tutte addensate in pochi punti (zone di Los Angeles, San Francisco) l'algoritmo tenderà 
ad unire le varie città in pochi cluster. In questa zona infatti è presenti un unico cluster nella zone di San Francisco.

L'algoritmo K-Means invece genera i cluster partendo da un set di punti arbitrari che vengono presi come centri del cluster. 
Tuttli gli altri punti del dataset vengo aggiunti al cluster da cui distano meno (la distanza di un cluster viene calcolata calcolando la distanza dal suo centro). 
Dopo ogni iterazione i centri dei cluster vengono ricalcolati e il procedimento si ripete fino alla terminazione del algoritmo.
Per come funziona K-Means è chiaro che i centri del cluster dipendono fortemente da come vengono selezionati i centri iniziali (specialmente se si effettuano poche iterazioni) 
Nel nostro caso i punti scelti come centri iniziali dei cluster sono le città con una popolazione più numerosa, il risultato è che vengono generati
molti cluster di dimensioni minori uno vicino all'altro. In questo caso i cluster nella zona occidentale sono 3.

# Domanda 8
In base alla risposta alla domanda 7, quale metodo (clustering gerarchico o k-means clustering) richiede meno 
supervisione umana per produrre clustering con distorsione relativamente bassa?

# Risposta
L'algoritmo di clustering gerarchico
# Domanda 9
Calcolare la distorsione dei clustering prodotti da Clustering Gerarchico e k-means clustering (utilizzando 5 iterazioni) sui set di dati con 212, 562 e 1041 contee, rispettivamente, variando il numero di cluster di output da 6 a 20 (estremi inclusi) 
# Risposta
![](./question_9_UCD_212.png?raw=true)
## 212 Contee - Clustering Gerarchico
|Numero Cluster   | Distorsione  |
|---|---|
| 6  | 3.5513 * 10^11  |
| 7  | 2.1198 * 10^11  |
| 8  | 2.0799 * 10^11  |
| 9  | 1.9680 * 10^11  |
|10  | 1.4589 * 10^11  |
|11  | 7.3340 * 10^10  |
|12  | 7.2987 * 10^10  |
|13  | 6.2926 * 10^10  |
|14  | 3.7490 * 10^10  |
|15  | 3.0755 * 10^10  |
|16  | 3.0523 * 10^10  |
|17  | 1.9654 * 10^10  |
|18  | 1.8993 * 10^10  |
|19  | 1.8909 * 10^10  |
|20  | 1.4493 * 10^10  |


## 212 Contee - KMeans
|Numero Cluster   | Distorsione  |
|---|---|
| 6  | 1.9420 * 10^11  |
| 7  | 1.2166 * 10^11  |
| 8  | 1.2082 * 10^11  |
| 9  | 9.5383 * 10^10  |
|10  | 7.6125 * 10^10  |
|11  | 6.9404 * 10^10  |
|12  | 5.6846 * 10^10  |
|13  | 5.1057 * 10^10  |
|14  | 4.5060 * 10^10  |
|15  | 4.1440 * 10^10  |
|16  | 3.6589 * 10^10  |
|17  | 3.6454 * 10^10  |
|18  | 1.8494 * 10^10  |
|19  | 1.7545 * 10^10  |
|20  | 1.6128 * 10^10  |

![](./question_9_UCD_212.png?raw=true)
## 562 Contee - Clustering Gerarchico
|Numero Cluster   | Distorsione  |
|---|---|
| 6  | 1.3412 * 10^12  |
| 7  | 1.3350 * 10^12  |
| 8  | 1.0182 * 10^12  |
| 9  | 8.9593 * 10^11  |
|10  | 4.7519 * 10^11  |
|11  | 4.1574 * 10^11  |
|12  | 3.9222 * 10^11  |
|13  | 3.0600 * 10^11  |
|14  | 2.8606 * 10^11  |
|15  | 2.6736 * 10^11  |
|16  | 2.2646 * 10^11  |
|17  | 2.0552 * 10^11  |
|18  | 1.4841 * 10^11  |
|19  | 1.3979 * 10^11  |
|20  | 1.3094 * 10^11  | 

## 562 Contee - KMeans
|Numero Cluster   | Distorsione  |
|---|---|
| 6  | 1.4881 * 10^12  |
| 7  | 8.8882 * 10^11  |
| 8  | 8.0678 * 10^11  |
| 9  | 6.6741 * 10^11  |
|10  | 5.6450 * 10^11  |
|11  | 5.4751 * 10^11  |
|12  | 4.1403 * 10^11  |
|13  | 2.8350 * 10^11  |
|14  | 2.5196 * 10^11  |
|15  | 4.3805 * 10^11  |
|16  | 3.8629 * 10^11  |
|17  | 3.8264 * 10^11  |
|18  | 3.0532 * 10^11  |
|19  | 2.0460 * 10^11  |
|20  | 2.0373 * 10^11  |

![](./question_9_UCD_212.png?raw=true)
## 1041 Contee - Clustering Gerarchico
|Numero Cluster   | Distorsione  |
|---|---|
| 6  | 2.4972 * 10^12  |
| 7  | 2.2226 * 10^12  |
| 8  | 1.3757 * 10^12  |
| 9  | 1.1404 * 10^12  |
|10  | 8.6870 * 10^11  |
|11  | 8.4616 * 10^11  |
|12  | 8.4074 * 10^11  |
|13  | 8.3098 * 10^11  |
|14  | 6.1959 * 10^11  |
|15  | 5.7537 * 10^11  |
|16  | 5.0106 * 10^11  |
|17  | 4.6706 * 10^11  |
|18  | 4.2163 * 10^11  |
|19  | 3.7037 * 10^11  |
|20  | 3.6666 * 10^11  |

## 1041 Contee - KMeans
|Numero Cluster   | Distorsione  |
|---|---|
| 6  | 2.5249 * 10^12  |
| 7  | 1.5683 * 10^12  |
| 8  | 1.1807 * 10^12  |
| 9  | 1.0899 * 10^12  |
|10  | 8.4690 * 10^11  |
|11  | 7.7549 * 10^11  |
|12  | 7.7428 * 10^11  |
|13  | 7.7276 * 10^11  |
|14  | 7.1196 * 10^11  |
|15  | 6.4390 * 10^11  |
|16  | 6.2434 * 10^11  |
|17  | 4.5857 * 10^11  |
|18  | 4.2541 * 10^11  |
|19  | 4.2045 * 10^11  |
|20  | 3.8342 * 10^11  | 

# Domanda 10
### Risposta
