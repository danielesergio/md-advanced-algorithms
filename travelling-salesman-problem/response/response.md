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
    <td>14</td>
    <td>4048</td>
    <td>0.01</td>
    <td>0.22</td>
    <td>14</td>
    <td>4003</td>
    <td></td>
    <td></td>
    <td></td>
  </tr>
<tr>
    <td>ulysses22.tsp</td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
  </tr>
  <tr>
    <td>eil51.tsp</td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
  </tr>
<tr>
    <td>kroD100.tsp</td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
  </tr>
<tr>
    <td>gr229.tsp</td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
  </tr>
<tr>
    <td>d493.tsp</td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
  </tr>
<tr>
    <td>dsj1000.tsp</td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
  </tr>
</table>

# Domanda 1
Commentate i risultati che avete ottenuto: come si comportano gli 
algoritmi rispetti alle varie istanze? C'è un algoritmo che riesce sempre a fare meglio degli altri rispetto all'errore di approssimazione? 
Quale dei tre algoritmi che avete implementato è più efficiente? 
## Risposta
