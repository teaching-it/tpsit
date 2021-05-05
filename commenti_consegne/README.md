# Esercizio Java: sistema di prenotazione multi-thread (teatro)

Cari ragazzi,

di seguito riporto, in forma anonima, alcune implementazioni dell'esercitazione assegnata per casa e relativi commenti, osservazioni, suggerimenti.

Mi auguro riusciate a trovare risposte alle vostre domande (beh, se non a tutte, quanto meno ad alcune!).

***

## Implementazione 1

### Classe Teatro

```java
public class Teatro {
    public static void main(String[] args) throws Exception {

        MySemaohore S = new MySemaohore(1);
        Platea platea = new Platea(S,5);
        Spettatore A = new Spettatore("A","A","A",123,platea);
        Spettatore B = new Spettatore("B","B","B",321,platea);
        Spettatore C = new Spettatore("C","C","C",321,platea);

        Spettatore D = new Spettatore("D","D","D",321,platea);


        A.start();
        B.start();
        D.start();
        C.start();

        A.join();
        B.join();
        D.join();
        C.join();
        
        platea.stampPosti();
        
    }
}
```

**Osservazioni.**

1. La selezione del posto viene effettuata all'interno del metoodo run() della classe Spettatore, in forma randomica. Idea carina, senza dubbio. In questo modo, però, è come se il "sistema" non offrisse all'utenza la possibilità di scegliere un posto autonomamente.
2. A tal proposito, potrebbe essere utile stampare a video la situazione della platea, successivamente a ciascuna prenotazione. Però, chiaramente, questo potrebbe comportare la necessità di gestione dell'accesso, in lettura, alla struttura dati condivisa.

### Classe Spettatore

```java
public class Spettatore extends Thread {
    private String nome;
    private String cognome;
    private String email;
    private int telefono;
    private Platea platea;
    
    private int x;
    private int y;
    

    public Spettatore(String nome,String cognome,String email,int telefono,Platea platea) {
        this.nome=nome;
        this.cognome=cognome;
        this.email=email;
        this.telefono=telefono;
        this.platea=platea;
    }


    public String getCognome() {
        return cognome;
    }

    public void run() {
        x=(int)Math.random()*4;
        y=(int)Math.random()*4;
        platea.occupaPosti(this,x,y);

    }

}
```

**Osservazione.**

Il costruttore della classe Spettatore, oltre agli attributi di natura anagrafica, prendere in input anche un'instanza della classe Platea. Giustissimo: poiché ciascun spettatore agirà sulla stessa "area di memoria condivisa", ovvero sullo stesso oggetto platea.

Da un punto di vista concettuale, però, uno spettatore dovrebbe poter essere creato (attraverso il costruttore) a prescindere dalla platea su cui effettuerà la prenotazione.

Ad esempio, pensate ad una classica piattaforma Web. In genere, in primis, un utente effettuare la registrazione (che, nel nostro programma, corrisponde alla creazione di un oggetto di tipo Spettatore, mediante il costruttore). Successivamente, tale utente può effettuare delle prenotazioni, agendo sull'oggetto platea.

Quindi, potrebbe valer la pena scorporare l'oggetto platea dal costruttore, definendo un metodo dedicato che, oltre allo stesso oggetto platea, riceva in input le coordinate del posto. Una cosa del genere:

```java
public void prenota(Platea platea, int i, int j) {
    this.platea = platea;
    this.i = i;
    this.j = j;
}
```

Quanto detto non è necessariamente legato ad un aspetto funzionale nella gestione delle dinamiche di concorrenza, su cui TPSIT intende invece concentrarsi. Dunque, va benissimo utilizzare il costruttore così com'è stato fatto.

### Classe Platea

```java
public class Platea {
    private boolean posto;
    private MySemaohore S;
    private Spettatore[][] platea;



    public Platea(MySemaohore S,int size) {
        this.posto=false;
        this.S=S;
        this.platea=new Spettatore[size][size];
    }

    public void setPosto(boolean posto) {
        this.posto=posto;
    }

    public boolean getPosto() {
        return posto;
    }

    public void occupaPosti(Spettatore utente,int x,int y) {
        S.P();

        if(!(platea[x][y] instanceof Spettatore)) {
            platea[x][y] = utente;
        }
        else
        {
            stampUtent(x,y);
        }
        S.V();
    }

    public void stampUtent(int x,int y) {
        System.out.println("il posto e' occupato da:"+platea[x][y].getCognome());
    }
    
    public void stampPosti() {
        for(int i=0;i<5;i++) {
            for(int j=0;j<5;j++) {
                if(platea[i][j] != null ) {
                System.out.println("il posto"+i+j+" e' occupato da:"+platea[i][j].getCognome());
                }
                else {
                    System.out.println("il posto"+i+j+"  e' libero");
                }
            }
        }
    }
}
```

**Osservazioni.**

Il metodo occupaPosti() contiene la sezione critica, opportunamente circoscritta dai costrutti semaforici.

In linea generale, è buona norma ridurre al minimo il numero di istruzioni "bloccanti", su cui gli altri thread vengono posti in attesa (waiting list). Questo perché, maggiore è il blocco di istruzioni perimetrato dal semaforo, minore è il grado di concorrenza raggiungibile.

Utile promemoria: *la sezione critica costituisce un blocco di istruzioni che NON deve essere eseguito in forma concorrete.*

Quindi, nel caso specifico, se pur sia interessante (e in parte utile) stampare a video una messaggio di cortesia (informando l'utente che il posto è già stato occupato), ciò nonostante tali istruzioni allungano l'attesa dei thread in coda, dunque è sconsigliato considerarle facenti parte la sezione critica.

Una possibile soluzione:

```java

// inizia la sezione critica
semaphore.P();

if ( ! (platea[x][y] instanceof Spettatore)) {
    platea[x][y] = utente;
}

semaphore.V();
// termina la sezione critica

if ( ! platea[x][y].equals(utente) ) {
    System.out.println("Il posto è stato occupato da: " + platea[x][y].getCognome());
}
```

Sì, capisco che questa soluzione possa essere meno efficiente da un punto di vista computazionale del singolo thread (abbiamo un doppio accesso alla struttura dati), ciò nonostante riduce la dimensione della sezione critica. Nb. il metodo equals() viene ereditato dalla classe Object ed usa semplicemente l'operatore == per confrontare due oggetti. Restituisce true solo se si tratta di due riferimenti allo stesso oggetto.

### Classe MySemaphore

**Osservazioni.**

Niente da segnalare: tale e quale all'implementazione condivisa e discussa in classe (semaforo di Dijkstra), che non riporto.

***

## Implementazione 2

### Classe Teatro

```java
public class Teatro {

    public static void main(String[] args) throws InterruptedException {
        MySemaphore s = new MySemaphore();
        Platea p = new Platea(s);
        
        Spettatore Mario = new Spettatore("Mario","Rossi","sjsffhi@gmail.com",334454756);
        Spettatore Antonio = new Spettatore("Antonio","Bianchi","uebjneo@icloud.com",25343454);
        Spettatore Marco = new Spettatore("Marco","Verdi","wrfcergeq@outlook.com",435564365);
        Spettatore Gianni = new Spettatore("Gianni","Giannini","ergerwhrth@email.it",346536544);
        Spettatore Beppino = new Spettatore("Beppino","Beppetti","ergewhert@mail.com", 353462543);
            
        p.prenota(Mario);
        p.prenota(Antonio);
        p.prenota(Marco);
        p.prenota(Gianni);
        p.prenota(Beppino);
        
        Mario.start();
        Antonio.start();
        Marco.start();
        Gianni.start();
        Beppino.start();
        
        Mario.join();
        Antonio.join();
        Marco.join();
        Gianni.join();
        Beppino.join();
        
        p.stampa();
    }
}
```

**Osservazioni.**

Un paio di aspetti da segnalare. Uno più rilevante, l'altro più marginale. Rispettivamente:

1. Dato che è il main a definire "l'ordine degli eventi" (mediante istanziazione di oggetti e avvio di thread), mi aspetterei di trovare, oltre alla definizione del semaforo, della platea, e degli spettatori, anche un aspetto altrettanto importante: le coordinate (i,j) dei posti scelti. A tal proposito, prima di giungere ad una valutazione di merito definitiva, sarà opportuno ispezionare le altre classi.
2. Potrebbe essere utile effettuare, per quanto riguarda la situazione delle prenotazioni dei posti, delle stampe a video intermedie. Come detto in precedenza, ciò potrebbe necessitare un'attenta gestione dell'accesso in lettura alla struttura dati condivisa (dunque, questo aspetto è da considerarsi secondario).

### Classe Spettatore

```java
public class Spettatore extends Thread {
    private String nome;
    private String cognome;
    private String email;
    private int telefono;
    private Platea p;
    
    public Spettatore(String nome, String cognome, String email, int telefono) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.telefono = telefono;
    }//costruttore
    
    public String getNome() {
        return nome;
    }
    public String getCognome() {
        return cognome;
    }
    public String getEmail() {
        return email;
    }
    public int getTelefono() {
        return telefono;
    }
    
    public void run(Spettatore s) {
        p.prenota(s);
    }
}

```

**Osservazioni.**

Bene la caratterizzazione anagrafica dello spettatore mediante l'impiego del metodo costruttore. Tuttavia, ancora mi domando, poicHé continuo a non trovarne traccia: come sarà stata gestita l'assegnazione del posto in platea?

### Classe Platea

```java
import java.util.*;

public class Platea {
    public final int N = 5;

    private Spettatore [][] posti;
    private MySemaphore S;

    public Platea(MySemaphore S) {
        posti = new Spettatore[N][N];
        this.S = S;
    }

    public void stampa() {
        int i;
        int j;

        for(i = 0; i < N; i++) {
            System.out.println("Fila "+ i +".");
            for(j = 0; j < N; j++) {
                if(posti[i][j] == null)
                    System.out.println("-- vuoto --");
                else
                    System.out.println("-- "+ posti[i][j].getCognome() +" --");
            }
            System.out.println("\n");
        }
    }

    public void prenota(Spettatore s) {
        Scanner l = new Scanner(System.in);

        int x;
        int y;

        //assegnazione posizione spettatori

        System.out.println("Inserisci numero compreso tra 0 e 4.\n");

        System.out.println("Inserisci numero fila:");
        x = l.nextInt();

        System.out.println("Inserisci posizione fila:");
        y = l.nextInt();

        //inizio semaforo
        S.acquire();

        if(postoLibero(x, y)){//se il posto scelto è libero, assegno il posto allo spettatore
            posti[x][y] = s; 
        }else{//se il posto scelto è occupato, assegno un'altro posto allo spettatore
            System.out.println("Inserisci un altro posto perchè già occupato.");
            prenota(s);
        }
        S.release();
        //fine semaforo
    }

    public boolean postoLibero(int x, int y) {
        boolean c = true;
        
        if(posti[x][y] != null) 
            c = false;
        else
            c = true;
        
        return c;
    }
}
```

**Osservazioni.**

Direi di approfondire l'analisi del metodo prenota(), poiché gli altri sono senz'altro di più facile comprensione e non contengono aspetti degni di nota. È sicuramente ammirevole il tentativo di introdurre l'acquisizione da tastiera delle coordinate del posto (x,y). Ciò nonostante, il seguente blocco di istruzioni (che evidenzio per maggior chiarezza) può determinare una situazione "pericolosa".

```java
//inizio semaforo
S.acquire();

if(postoLibero(x, y)){//se il posto scelto è libero, assegno il posto allo spettatore
    posti[x][y] = s; 
}else{//se il posto scelto è occupato, assegno un'altro posto allo spettatore
    System.out.println("Inserisci un altro posto perchè già occupato.");
    prenota(s);
}
S.release();
//fine semaforo
```

Il "lock" sul semaforo viene acquisito dal primo thread che accede al metodo. **Bene.** Viene successivamente verificata lo stato del posto in platea, rispetto alle coordinate x,y. **Bene.** Qualora il posto sia libero, viene correttamente assegnato allo spettatore e, successivamente, il semaforo avrà nuovamente "luce verde" (invocazione del metodo release). **Bene.**

Qualora, invece, il posto sia già occupato (ramo else), viene attivata un'invocazione di metodo ricorsiva. **NON bene**. Ora, la chiamata ricorsiva potrebbe essere fonte di fraintendimento. Se pur non sia tecnicamente la stessa cosa, potete momentaneamente sostituirla con un semplice costrutto iterativo (ciclo).

La problematica principale è la seguente: fintantoché lo spettatore che ha "acquisito il semaforo" non seleziona un posto libero, nessun altro spettatore può effettuare una prenotazione. In un contesto di concorrenza, lo scenario che potenzialmente protrebbe prefigurarsi è il seguente: a tutti gli spettatori è richiesto l'inserimento delle coordinate del posto da prenotare (ricordate: l'acquisizione da tastiera è esterna alla sezione critica); tuttavia, fintantoché il primo spettatore non ha terminato con successo la scelta del posto, tutti gli altri resteranno in attesa e non verrà notificato loro alcunché.

Cosa potrebbe accadere nel caso in cui **i posti sono esauriti?** Situazione "limite", scarsamente probabile, ma comunque possibile, dunque assolutamente da scongiurare!

Quindi, per concludere: occhio ad attivare operazioni di acquisizione da tastiera 1) in un contesto di concorrenza 2) in prossimità della gestione di una sezione critica. Sono aspetti che non abbiamo approfondito a lezione; aprezzo molto il tentativo, tuttavia consiglio di fare un piccolo passo indietro su questo aspetto.

### Classe MySemaphore

```java
public class MySemaphore {

    public boolean available = true;//lock mutex

    public void acquire() {
        while(!available) {
            available = false;
        }//attesa attiva
    }//lock

    public void release() {
        available = true;
    }//unlock
}
```

**Osservazioni.**

L'implementazione del semaforo è concettualmente appropriata, tuttavia non formalmente corretta. Emergono due aspetti fondamentali:

1. Nel metodo acquire() è presente un ciclo while che, come correttamente commentato, innesca un'attesa attiva dei thread in coda e, di conseguenza, una inefficiente gestione della risorsa più preziosa: la CPU. **Suggerimento:** perché non optare per l'implementazione del semaforo di Dijkstra?
2. Entrambi i metodi **non** sono "protetti" da eventuali accessi concorrenti e ciò potrebbe condurre a configurazioni esecutive non appropriate o assolutamente indesiderate. **Suggerimento:** utilizzare il qualificatore di metodo synchronized.

***

## Implementazione 3

### Classe Teatro

```java
public class Teatro {
    //classe principale
    public static void main(String[] args) throws InterruptedException
    {
        MySemaphore s = new MySemaphore();
        Platea p = new Platea(s);
        
        Spettatore Franco = new Spettatore(p, "Franco", "deFranchi", "francodefranchi@franco.cavallo", "123", 0, 2);
        Spettatore Franco2 = new Spettatore(p, "Franco2", "deFranchi", "franco2defranchi@franco.com", "234", 3, 2);
        Spettatore Roberto = new Spettatore(p, "Roberto", "deFranchini", "robertodefranchini@franco.com", "3298", 0, 2);
        Spettatore Franco3 = new Spettatore(p, "Franco3", "Rossi", "ilterzofranco@gmail.com", "3333", 1, 3);
        
        Franco.start();
        Franco2.start();
        Roberto.start();
        Franco3.start();
        
        Franco.join();
        Franco2.join();
        Roberto.join();
        Franco3.join();
        
        p.stampa();
    }
}

```

**Osservazioni.**

Nessuna novità sostanziale da segnalare rispetto a quanto già detto in precedenza.

### Classe Spettatore

```java
public class Spettatore extends Thread 
{
    //thread
    
    //attributi
    private String nome;
    private String cognome;
    private String email;
    private String telefono; //stringa poiché non è tanto un numero quanto un codice univoco
    
    //coordinate del posto prenotato
    private int x;
    private int y;
    
    public Platea platea;
    
    //metodi
    public Spettatore(Platea platea)
    {
        super();
        nome = null;
        cognome = null;
        email = null;
        telefono = null;
        this.platea = platea;
    }
    
    //costruttore con inserimento attributi manuale
    public Spettatore(Platea platea, String nome, String cognome, String email, String telefono, int x, int y) 
    {
        super();
        
        this.x = x;
        this.y = y;
        
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.telefono = telefono;
        this.platea = platea;
    }
    
    public void run()
    {
        platea.prenota(x, y, this);
    }
    
    //metodi get e set
    public String getNome()
    {
        return nome;
    }
    
    public String getCognome()
    {
        return cognome;
    }
    
    public String getEmail()
    {
        return email;
    }
    
    public String getTelefono()
    {
        return telefono;
    }
    
    public void setNome(String nome)
    {
        this.nome = nome;
    }
    
    public void setCognome(String cognome)
    {
        this.cognome = cognome;
    }
    
    public void setEmail(String email)
    {
        this.email = email;
    }
    
    public void setTelefono(String telefono)
    {
        this.telefono = telefono;
    }
}

```

**Osservazioni.**

Onestamente non mi è chiaro il motivo dell'overload del costruttore; chiedo scusa all'autore del listato. Oltre a questo aspetto, non risulta niente di particolare da segnalare.

### Classe Platea

```java
public class Platea {
    
    //dimensione matrice
    private final int DIM = 5;
    
    private MySemaphore s = new MySemaphore();
    public Spettatore platea[][];
    
    //metodi
    public Platea(MySemaphore s)
    {
        platea = new Spettatore[DIM][DIM];
        this.s = s;
    }
    
    //prenotazione di un posto
    public void prenota(int x, int y, Spettatore prenotante)
    {
        //blocco della risorsa
        s.acquire();
        //controllo se il posto e' libero
        if(! (platea[x][y] instanceof Spettatore))
        {
            //posto vuoto
            setPosto(x, y, prenotante);
            System.out.println("Posto prenotato con successo");
        }
        else
        {
            //posto occupato
            System.out.println("Posto gia' occupato");
        }
        //rilascio
        s.release();
    }
    
    //stampa della matrice
    public void stampa() {
        int i;
        int j;

        for(i = 0; i < DIM; i++) 
        {
            System.out.println("Fila "+ i + ": ");
            for(j = 0; j < DIM; j++) 
            {
                if(platea[i][j] == null)
                    System.out.println("-- vuoto --");
                else
                    System.out.println("-- "+ platea[i][j].getNome() +" --");
            }
        }
    }
    
    public void setPosto(int x, int y, Spettatore spettatore)
    {
        platea[x][y] = spettatore;
    }
}

```

**Osservazioni.**

Così come detto in precedenza (si veda commento al metodo prenota dall'implementazione 1), la sezione critica dovrebbe contenere un blocco di istruzioni che è imprescindibile **non** venga eseguito in forma concorrente. I vari output a video non sono operazioni "sensibili", dunque è auspicabile non inserire le relative istruzioni all'interno del blocco delimitato dal semaforo.

### Classe MySemaphore

```java
public class MySemaphore {
    //semaforo 
    boolean available = true;
    
    //lock
    public synchronized void acquire() {
        //attesa attiva
        while(!available) {
            
        }
        available = false;
    }
    
    //unlock
    public void release() {
        available = true;
    }
}

```

**Osservazioni.**

Rimando alle osservazioni già esplicitate nella classe MySemaphore dell'implementazione 2. In particolare, occhio ad applicare il qualificatore synchronized anche al metodo release().

***

## Implementazione 4

### Classe Teatro

```java
public class Teatro {

    public static void main(String[] args) throws Exception{
        // Utilizziamo l'implementazione del semaforo di Dijkstra contenuto in MySemaphore.java
        // Nb. l'oggetto MySemaphore è unico, ed è condiviso dai thread Correntista.
        MySemaphore S = new MySemaphore(1);
        Platea platea = new Platea(S);
                
        Spettatore Mario = new Spettatore("Mario", "Rossi", "mariorossi@gmail.com", "3386598123", platea);
        Spettatore Luigi = new Spettatore("Luigi", "Pratesi", "luigipratesi@libero.it", "3389835787", platea);
                
        Mario.start();
        Luigi.start();
                
        /*
         * In questo preciso momento, sono attivi 3 thread
         * Mario, Luigi, main.
         */
                
        Mario.join();
        Luigi.join();
                
        platea.visualizzaMatrice();
    }

}
```

**Osservazioni.**

Niente di particolare da segnalare, oltre a quanto già riportato in precedenza.

### Classe Spettatore

```java
public class Spettatore extends Thread{
    private String nome;
    private String cognome;
    private String email;
    private String telefono;
    private Platea p;
    private Spettatore S;
    
    public Spettatore(String nome, String cogn, String mail, String telefono, Platea p) {
        this.nome = nome;
        cognome = cogn;
        email = mail;
        this.telefono = telefono;
        this.p = p;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void run() {
        p.prenota(S, 12);
    }
}
```

**Osservazioni.**

L'assegnazione del posto in platea è *built-in* all'interno del metodo run(), ovvero: `p.prenota(S, 12);`, dove il valore numerico `12` rappresenta univocamente un posto tra i 25 posti disponibili (matrice 5x5). L'idea è senza dubbio interessante, poiché prevede l'utilizzo di un unico valore numerico, piuttosto che una coppia coordinate (i,j). Ciò nonostante, definire "staticamente" il valore senza che esso sia parametrizzato (mediante una variabile), rende l'applicazione estremamente rigida e scarsamente "testabile".

Sì, capisco che al metodo run() non sia possibile passare parametri direttamente (è ereditato dalla classe Thread e, oltretutto, non è direttamente eseguibile, se non mediante l'invocazione *start*), tuttavia potete impiegare alcune alternative:

1. Utilizzare direttamente il costruttore. Tuttavia, come già detto in precedenza, questa possibilità, se pur non errata, potrebbe essere impropria da un punto di vista concettuale. Ovvero: uno spettatore "esiste" anagraficamente (e ne viene dunque creata un'istanza mediante il costruttore di classe) a prescindere dal posto in platea che occuperà; questa informazione può essere attribuita in un secondo momento (ed ecco che si delinea la seconda ipotesi).
2. Utilizzare un metodo di supporto. Ovvero, volgarmente una specie di "secondo costruttore", oppure uno o più metodi set, al fine di attribuire i parametri richiesti dalla prenotazione, cioè: la platea e le coordinate del posto da prenotare.

Altro aspetto **importante** da considerare nell'istruzione `p.prenota(S, 12);`. Cos'è `S`? Sicuramente una variabile di istanza di tipo Spettatore, poiché abbiamo la seguente istruzione dichiarativa, in testa alla classe: `private Spettatore S;`. Tuttavia, quando viene valorizzata `S`? Beh, **mai**! Dunque `S` non è un riferimento esplicito all'istanza corrente, bensì una variabile impostata a null (volgarmente: un oggetto vuoto).

Per raggiungere lo scopo preposto, è necessario utilizzare la keyword **this**. Lo snippet di codice formalmente corretto (al netto delle riflessioni sul valore numerico `12`, fatte in precedenza), è il seguente:

```java
public void run() {
    p.prenota(this, 12);
}
```

### Classe Platea

```java
public class Platea {
    private Spettatore M[][];
    private MySemaphore semaphore;
    
    public Platea(MySemaphore semaphore) {
        this.semaphore = semaphore;
        M = new Spettatore [5][5];
    }
    
    public void visualizzaMatrice() {
        for(int I = 0; I < 5; I++) {
            for(int J = 0; J < 5; J++) {
                if(M[I][J] == null)
                    System.out.print("|          |");
                else
                    System.out.print("| " + M[I][J].getNome() + " |");
            }
            System.out.println("");
            System.out.println("");
        }
    }
    
    public void prenota(Spettatore S, int posto) {
        semaphore.P();
        for(int I = 0; I < 5; I++)
            for(int J = 0; J < 5; J++) {
                if(I == posto / 5 && J == posto % 5) 
                    if(M[I][J] == null) 
                        M[I][J] = S;
                    else
                        System.out.println("Il posto è già prenotato!!!");
            }
        semaphore.V();
    }
}
```

**Osservazioni.**

Direi di sofferemarci in particolare sul metodo prenota. Come discusso in precedenza, è buona norma ridurre il numero di istruzioni contenute all'interno della sezione critica (delimitata dalle invocazioni semaforiche). La, stampa a video, dunque, può/dovrebbe essere spostata all'esterno della sezione critica.

Inoltre, ricordiamo che il posto è rappresentato da un unico valore numerico nel range [0,24], piuttosto che da una coppia di coordinate (i.j). Tuttavia, perché usare una coppia di cicli for annidati? È davvero necessario sfogliare l'intera matrice? Direi di no. Basta "giocare" con le operazioni aritmetiche di divisione e modulo, esattamente come intuito dallo studente.

Le coordinate del posto 12, in una matrice 5x5 con indici (i,j) nel range [0,4], possono essere calcolate come segue:

```java
i = 12 / 5;
j = 12 % 5;
```

### Classe MySemaphore

Niente da segnalare: tale e quale all'implementazione condivisa e discussa in classe (semaforo di Dijkstra), che non riporto.
