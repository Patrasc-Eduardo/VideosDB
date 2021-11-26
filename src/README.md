# Tema POO - VideosDB

_Patrasc Andrea-Eduardo, 321CDb_

## Pachete si Entitati

* database
   * **Database** : Clasa unde sunt stocate listele cu actori, useri, filme, seriale si actiuni.
   * **User** : Clasa care retine informatii specifice unui user (username, subscription type, etc.) si implementeaza metode specifice acestuai (addRating, getNumberOfViews).

* video
   * **Video** : Clasa care defineste un video. Aceasta contine informatii si metode comune atat filmelor cat si serialelor. 
      * **Movie** : Clasa cu informatii si metode specifice unui film.
      * **Show** : Clasa cu informatii si metode specifice unui serial.
   * **IRating** : Interfata care declara metodele comune filmelor si serialelor (getRating, setRating).
   * **RatingVisitor** : Clasa care implementeaza un design pattern de tip "visitor" pentru optiunea "Rating" oferita 
   drept comanda de utilizator. Aceasta abordare a fost aleasa deoarece ratingul se face diferit la filme si seriale, iar 
   noi la run-time nu stim tipul videoclipului (daca este film sau serial).
   * **VideoVisitable** : Interfata ajutatoare pentru patternul "visitor".
   * **VideoVisitor** : Interfata ajutatoare pentru patternul "visitor".

* actor
  * **Actor** : Clasa cu informatii si metode specifice unui actor.
  * **ActorsAwards** : Enum cu awards.

* entertainment
  * **Genre** : Enum cu genuri de filme.
  * **Season** : Clasa cu informatii specifice sezonului unui serial. Aceasta este o subclasa a clasei "Show".

* action
  * **Action** : Clasa care stocheaza informatiile referitoare la o actiune.
  * **Commands** : Clasa contine metodele care verifica si apeleaza tipul de comanda a utilizatorului (favorite,
   view, rating).
  * **Query** : Clasa contine metodoele care verifica si apeleaza tipul de query cerut de utilizator.
    * **ActorQuery** : Aici sunt implementate query-urile de average, awards si filter_description cerute in enunt.
    * **VideoQuery** : Aici sunt implementate query-urile de rating, favorite, longest si most_viewed cerute in enunt.
    * **UserQuery** : Aici este implmentat query-ul care intoarce lista de utilizatori sortati dupa numarul de ratings 
    oferite.
  * **Recommendation** : Clasa care implementeaza recomandarile cerute in enunt.
  * **SortMap** : Aceasta este o interfata ce contine o metoda default care care sorteaza un dictionar ce are drept cheie
  un string (acesta poate reprezenta numele unui video, user, etc.) si drept valoare un double (care poate repr un rating,
  numar de vizionari, numar de ratinguri oferite).
  
```java
int order = sortType.equals("asc") ? 1 : -1;
```

```java
list.sort(
        (o1, o2) -> {
          if (o1.getValue().compareTo(o2.getValue()) == 0) {
            if (apearCriteria != null) {
              if (apOrder.indexOf(o1.getKey()) < apOrder.indexOf(o2.getKey())) {
                return -1;
              } else {
                return 1;
              }
            } else {
              return o1.getKey().compareTo(o2.getKey()) * order;
            }
          }
          return o1.getValue().compareTo(o2.getValue()) * order;
        });
```

Secventele de cod de mai sus sunt responsabile, in functie de optiunea utilizatorului, de o sortare numerica 
crescatoare/descrescatoare , sortare alfabetica crescatoare/descrescatoare, sau sortare in functie de ordinea
de aparitie in baza de date (pentru Recommendation).

* checker
* fileio : pachet ce contine clasele care citesc si parseaza datele din json-uri.
* utils
* common
  * **Constants** : Clasa cu constantele folosite in cod pentru lizibilitate.
* main
  * **Main** : Entry point-ul programului.
  * **Test**

# Flow-ul Programului

Entry point-ul programului se afla in clasa "Main". Aici sunt citite toate datele din clasa Input care mai apoi 
vor fi stocate in clasa Database. Din database vom prelua lista de actiuni si vom itera prin aceasta , modificand succesiv
JSONarray-ul prin intermediul caruia se va face afisarea in fisierul out. Daca actiunea este de tip "command" , cream o
instanta a clasei "Command" apoi apelam metoda metoda "init" din interiorul acesteia care, in functie de tipul de comanda
specificat in "Action", va realiza "view", "favorite" sau "rating" (aceste actiuni aduc modificari la nivelul elementelor
din baza de date -> ex : "View" va modifica istoricul de vizualizari al userului respectiv).
Procesul este analog si pentru restul de actiuni ("Query" si "Recommendation"). La final, JSONArray-ul in care au fost 
stocate rezultatele actiunilor, va fi afisat intr-un alt json prin instructiunea de mai jos.

```java
fileWriter.closeJSON(arrayResult);
```
 