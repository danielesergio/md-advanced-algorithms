# Consegna
Consegna di Daniele Sergio 1127732

Componenti gruppo: Daniele Sergio

# Domanda 1
Eseguite i tre algoritmi che avete implementato (Held-Karp, euristica costruttiva e 2-approssimato) 
sui 7 grafi del dataset. Mostrate i risultati che avete ottenuto in una tabella come quella sottostante.
Le righe della tabella corrispondono alle istanze del problema. Le colonne mostrano, per ogni algoritmo,
il peso della soluzione trovata, il tempo di esecuzione e l'errore relativo calcolato come 
(SoluzioneTrovata−SoluzioneOttima)/SoluzioneOttima.

## Risposta
Alla tabella è stata aggiunta la colonna percorso che indica da quanti 'passi' è composta la soluzione.
Il valore è indicato come frazione: NumeroPassiSoluzioneTrovata / NumeroPassiSoluzioneCompleta.
Questo campo è stato introdotto per quantificare il valore della soluzione parziale trovata dall'algoritmo
Held-Karp.
<table>
   <tr>
    <th style="border: 0"></th>
    <th colspan="4"> Held-Karp</th>
    <th colspan="4"> Nearest Neighbor</th>
    <th colspan="4"> 2-approssimato </th>
</tr>
  <tr>
    <th >Istanza</td>
    <th>Soluzione</td>
    <th>Tempo(s)</td>
    <th>Errore</td>
    <th>Percorso</th>
    <th>Soluzione</td>
    <th>Tempo(s)</td>
    <th>Errore</td>
    <th>Percorso</th>
    <th>Soluzione</td>
    <th>Tempo(s)</td>
    <th>Errore</td>
    <th>Percorso</th>
  </tr>
  <tr>
    <td>burma14.tsp</td>
    <td>3323</td>
    <td>0.773</td>
    <td>0.0</td>
    <td>14/14</td>
    <td>4048</td>
    <td>0.001</td>
    <td>0.22</td>
    <td>14/14</td>
    <td>4003</td>
    <td>0.038</td>
    <td>0.20</td>
    <td>14/14</td>
  </tr>
<tr>
    <td>ulysses22.tsp</td>
    <td>7105</td>
    <td>300.002</td>
    <td>0.01</td>
    <td>22/22</td>
    <td>10586</td>
    <td>0.001</td>
    <td>0.51</td>
    <td>22/22</td>
    <td>8308</td>
    <td>0.002</td>
    <td>0.18</td>
    <td>22/22</td>
  </tr>
  <tr>
    <td>eil51.tsp</td>
    <td>310</td>
    <td>303.144</td>
    <td>-0.27</td>
    <td>22/51</td>
    <td>497</td>
    <td>0.001</td>
    <td>0.17</td>
    <td>51/51</td>
    <td>599</td>
    <td>0.005</td>
    <td>0.41</td>
    <td>51/51</td>
  </tr>
<tr>
    <td>kroD100.tsp</td>
    <td>11851</td>
    <td>328.771</td>
    <td>-0.44</td>
    <td>22/100</td>
    <td>26906</td>
    <td>0.001</td>
    <td>0.26</td>
    <td>100/100</td>
    <td>28553</td>
    <td>0.007</td>
    <td>0.34</td>
    <td>100/100</td>
  </tr>
<tr>
    <td>gr229.tsp</td>
    <td>45204</td>
    <td>300</td>
    <td>-0.66</td>
    <td>22/229</td>
    <td>162430</td>
    <td>0.035</td>
    <td>0.21</td>
    <td>229/229</td>
    <td>179335</td>
    <td>0.127</td>
    <td>0.33</td>
    <td>229/229</td>
  </tr>
<tr>
    <td>d493.tsp</td>
    <td>5261</td>
    <td>303.048</td>
    <td>-0.85</td>
    <td>22/493</td>
    <td>43244</td>
    <td>0.005</td>
    <td>0.24</td>
    <td>493/493</td>
    <td>45540</td>
    <td>0.11</td>
    <td>0.30</td>
    <td>493/493</td>
  </tr>
<tr>
    <td>dsj1000.tsp</td>
    <td>3763883</td>
    <td>300.217</td>
    <td>-80</td>
    <td>22/1000</td>
    <td>24630468</td>
    <td>0.086</td>
    <td>0.32</td>
    <td>1000/1000</td>
    <td>25525517</td>
    <td>0.098</td>
    <td>0.37</td>
    <td>1000/1000</td>
  </tr>
</table>

# Domanda 1
Commentate i risultati che avete ottenuto: come si comportano gli 
algoritmi rispetti alle varie istanze? C'è un algoritmo che riesce sempre a fare meglio degli altri rispetto all'errore di approssimazione? 
Quale dei tre algoritmi che avete implementato è più efficiente? 
## Risposta
L'algoritmo Held-Karp risulta essere sostanzialmente inutilizzabile per grafi di dimensioni modeste. Questo dato risulta evidente analizzando la
colonna *Percorso* che in circa 5 minuti riesce a trovare una soluzione parziale che visita al massimo 23 nodi. La soluzione trovata risulta essere quindi
totalmente initulizzabile in quanto l'algoritmo non riesce a trovare una soluzione parziale, ma che visiti almeno tutti i nodi in tempo accettabile.

Gli altri due algoritmi si comportano in maniera simile per grafi di queste dimensione anche se Nearest Neighbor tenderà a comportarsi sempre pessio
essendo un algoritmo log(n)-approssimato 

### Complessità Algoritmi
Held-Karp ha una complessità di O(n^2·2^n)
Nearest Neighbor ha una complessità di O(n^2)
2-approssimato ha una complessità di O(n^2·log(n))

Come si evince anche dai risultati l'algoritmo più efficiente è Nearest Neighbor 
con una complessità di O(n^2).