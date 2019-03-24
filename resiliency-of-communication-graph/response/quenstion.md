# Premesse - algoritmo UPA
L'algoritmo UPA è identico all'algoritmo DPA salvo che per il metodo RUNTRIAL(m)
 che viene modificato come segue:

```
function RUNTRIAL(m)
    V' = {}
    for i=1 to m do
        u = RANDOMCHOICE(nodeNumbers)
        V'= V' U {u}
    end for
    aggiungi |V'| + 1 copie di numNodes a nodeNumbers //Riga modificata rispetto a DPA
    APPEND(V',nodeNumbers)
    numNodes = numNodes + 1
    return V'
end function
```  

In particolare la riga segnata invece di aggiungere 1 copia del nuovo nodo 
ne aggiunge |V'| + 1; questo è necessario perchè la probabilità di estrarre 
ogni nodo deve essere uguale a (in-degree(v) + 1) / (totindeg + |V|) -> in 
un grafo non orientato in-degree(v) = out-degree(v) = degree(v) quindi 
il grado del nuovo nodo è uguale |V'|. 

# Domanda 1
Iniziamo la nostra analisi esaminando la resilienza della rete di calcolatori 
rispetto ad un attacco che sceglie i server da disattivare in modo casuale, 
e confrontandola con quella di un grafo ER e un grafo UPA di dimensioni simili.

Come prima cosa determinate dei valori di n, p e m tali che la procedura 
ER e la procedura UPA generino un grafo con lo stesso numero di nodi
ed un numero di archi simile a quello della rete reale.
Come valore del parametro m per la procedura UPA potete usare il numero 
intero più vicino al grado medio al grado medio diviso 2 dei vertici della rete reale.

Quindi, per ognuno dei tre grafi (rete reale, ER, UPA), simulate un attacco che 
disabiliti i nodi della rete uno alla volta seguendo un ordine casuale, 
fino alla disattivazione di tutti i nodi del grafo, e calcolate la resilienza 
del grafo dopo ogni rimozione di un nodo.

Dopo aver calcolato la resilienza dei tre grafi, mostrate il risultato 
in un grafico con scala lineare che combini le tre curve ottenute. 
Usate un grafico a punti oppure a linea per ognuna delle curve. 
L'asse orizzontale del grafico deve corrispondere al numero di nodi 
disattivati dall'attacco (che variano da 0 a n), mentre l'asse verticale 
alla dimensione della componente connessa più grande rimasta dopo aver 
rimosso un certo numero di nodi. Aggiungete una legenda al grafico che 
permetta di distinguere le tre curve e che specifici i valori di p e m 
utilizzati. Allegate il file con la figura nell'apposito spazio.

## Risposta

Il valore p da utilizzare con l'algoritmo ER per ottenere un grafo con
lo stesso numero di vertici di un grafo G(V,E) ed un numero di archi simile è stato 
calcolato con la  seguente formula:

`p = |E| / (|V|^2 - |V|)`

Al numeratore ci sono il numeor di archi voluti, mentre al denominatore
ci sono il numero di archi presenti in un grafo completo (escludendo archi
che hanno il nodo di origine uguale al nodo di destinazione).


Come scritto nella cosegna il valore da utilizzare per m nell'algoritmo UDA 
e` pari al grado medio dei nodi del grafo diviso due. 
Il grado medio di un vertice in un grafo(V,E) non orientato è uguale a 
2|E|/|V| quindi la formula utilizzata è la senguente:

`m = RAUND(|E| / |V|)`

L'immagine seguente mostra la variazione della dimensione della componente connessa 
più grande man mano che dei nodi (scelti in maniera casuale) vengono rimossi dal grafo.
I punti evendenziati nel grafico mostrano il rapporto tra la dimensione della componente
connessa più grande ed il numero di vertici dopo che sono stati rimossi 
il 20% dei nodi.

![](./random.png?raw=true)

# Domanda 2
Considerate quello che succede quando si rimuove una frazione significativa 
dei nodi del grafo usando l'attacco con ordine casuale. Diremo che un 
grafo è resiliente a questo tipo di attacco quando la dimensione della 
componente connessa più grande è superiore al 75% del numero dei nodi 
ancora attivi. 

Esaminate l'andamento delle tre curve del grafico ottenuto nella Domanda 1, 
e dite quali dei tre grafi sono resilienti dopo che l'attacco in ordine 
casuale ha rimosso il 20% dei nodi della rete.

## Risposta
Come è possibile vedere dal grafico precendete tutti e tre i grafi risultano
essere resilienti all'attaco con ordine casuale.


# Domanda 3
Consideriamo ora un attacco che sceglie i nodi da rimuovere sulla base della 
struttura del grafo. In particolare, una strategia di attacco che ad ogni 
passo disattiva un nodo tra quelli di grado massimo rimasti nella rete. 

Per ognuno dei tre grafi (rete reale, ER, UPA), simulate un attacco di 
questo tipo fino alla disattivazione di tutti i nodi del grafo, e calcolate 
la resilienza dopo ogni rimozione di un nodo.

Mostrate il risultato in un grafico che combini le tre curve come nella 
Domanda 1 e allegate il file con la figura nell'apposito spazio.

## Risposta
L'immagine seguente mostra la variazione della dimensione della componente connessa 
più grande man mano che i nodi (con grado massimo) vengono rimossi dal grafo.
I punti evendenziati nel grafico mostrano il rapporto tra la dimensione della componente
connessa più grande ed il numero di vertici dopo che sono stati rimossi 
il 20% dei nodi.

![](./max_grade.png?raw=true)

# Domanda 4.

Considerate quello che succede quando si rimuove una frazione significativa 
dei nodi del grafo usando l'attacco che sceglie i nodi con grado massimo.  
Esaminate l'andamento delle tre curve del grafico ottenuto nella Domanda 3, 
e dite quali dei tre grafi sono resilienti dopo che l'attacco ha rimosso il
20% dei nodi della rete.

## Risposta

Osservando il grafico precedente si puo osservare che l'unico grafo resiliente
a questo tipo di attacco è quello generato con l'algoritmo ER.

Il comportamento dei grafi generati è perfettamente coerente con gli algoritmi
che li hanno generati:
* l'algoritmo ER genera dei grafi in cui il grado di ogni nodo tende al grado 
medio dei nodi del grafo, quindi resiste bene a questo tipo di attaco.
* l'algoritmo UPA invece genera grafi in cui pochi nodi hanno un grado molto elevato
è quindi normale che sia più fragile ad attacchi di questo tipo (la rimozione
di pochi nodi comporta la perdita di un gran numero di archi).

Da quanto si può osservare dal grafico precendente, la rete reale è particolarmente 
vulnerabile a questo tipo di attaco. Suppongo sia dovuto al fatto che nella rete
ci siano due tipi di nodi:
* nodi 'master': che collegano un gran numero di nodi client
* nodi 'client': che sono collegati ad un nodo master.


