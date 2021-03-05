# Portfolio Martijn de Kam schoolplanner project
## Week 3
Deze week werk ik met Ewout aan het mogelijk maken om wijzigingen te maken in het rooster.

Iets dat al snel bleek was dat er aardig wat overlap was met wat de anderen gingen doen en dat we dat allemaal tegelijkertijd gingen doen.
Dit zorgde voor wat moeizaamheden tijdens het werken.

Zelf ben ik individueel doorgegaan, maar Ewout is na een tijdje samen met de anderen gaan zitten werken.

Tijdens het programmeren ben ik verder niet echt tegen problemen aangelopen.
Ik had echter wel een ernstige migraine op de donderdag waardoor ik aan het einde van de middag echt moest stoppen.
De dag daarna op vrijdag was ik ook nauwelijks in staat om ook maar iets te doen.
Ik heb een paar bugs opgelost die de anderen hadden aangegeven, verder heb ik niet veel kunnen doen.

## Week 4
In week vier ben ik gaan werken met het inladen en tekenen van de tiledmaps. Deze twee punten heb ik ook opgesplitst, door eerst te zorgen dat alles kan inladen en daarna pas te gaan kijken naar het tekenen.

Hiervoor heb ik de powerpoints van het opstart college erbij gepakt, hierin stonden een aantal punten over de structuur waarin dit kan en wat er moet gebeuren.

![TiledMapStructure.png](TiledMapStructure.png)

Verder heb ik Tiled erbij gepakt en een willekeurige tiledmap gemaakt met verschillende layers om te gebruiken om mee te testen.

Voor het werken heb ik in de simulatie package een nieuwe package bijgemaakt met daarin alles over de tiledmaps met ook een uitvoerbare Main klasse om puur deze functionaliteiten te testen los van de andere code.

Om te beginnen heb ik uit Tiled het bestand geëxporteerd als Json en daarin gekeken wat voor objecten en attribuuten er allemaal nodig zijn. Tijdens het kijken hiernaar heb ik ook opgemerkt dat dit heel erg in lijn is met wat er in de powerpoints staat over de structuur.

Met het programmeren van de data inlezen uit het Json bestand ben ik tegen twee problemen opgelopen:

- Met het lezen van het json bestand zelf kreeg ik een nullpointer exception.

Dit heb ik opgelost door eerst te kijken of de filepath string wel mee werd gegeven, dus het was niet dat er helemaal niets binnenkomt. 
Het volgende vermoeden is dat de filepath niet is herkend, om dit te testen heb ik de string een aantal keer aangepast en getest. 
Uiteindelijk heb ik de relative path copy gebruikt, deze werkte ook nog net niet, maar door een / ervoor te zetten werkte het wel.

- Tijdens het lezen van de array met tilesets kreeg ik een nullpointer exception bij het uitlezen van een specifieke waarde.

Met debuggen doormiddel van een paar breakpoints heb ik gevonden dat omdat een van de tilesets alleen "firstgid" en "source" als waardes heeft een nullpointer geeft wanneer bijvoorbeeld de "columns" wordt gevraagd.
Als oplossing heb ik de constructor een nullpointerexception laten throwen en bij het uitlezen van de array in een for loop een try catch gezet waarbij de tiledset alleen wordt toegevoegd als er geen nullpointer komt.

Uiteindelijk is het wel gelukt om de TiledMap in te lezen en te tekenen!

![TiledMapWorkingWK4.PNG](TiledMapWorkingWK4.PNG)

Verder ben ik ook gaan kijken of het mogelijk is om geroteerde tiles te herkennen en tekenen naar vraag van degene die het ontwerp van de simulatie maakt.

Problemen die ik daarbij ben tegengekomen zijn:

Met het lezen van geroteerde tiles, tegen het probleem aangekomen dat tijdens het lezen met de JsonReader de int waardes een overflow gaven (bij grote getallen werden deze negatief).

Het printen van de uitgelezen waardes bevestigd dat de getallen negatief zijn
Kijkend naar de getallen in Json en een google search geeft dat de getallen groter dan de int limiet zijn, namelijk groter dan 2,15 miljard.

Dus in plaats van lezen als een int moeten de getallen op een andere manier worden ingelezen.
Bij de jsonarray waaruit wordt gelezen zit een methode om als een jsonNumber te lezen, hiermee testen geeft dat dit de goede volledige decimale getallen geeft.
Van de jsonnumber kan ook de long value worden gepakt om in een data lijst te zetten.
Testen van de waardes geeft dat deze manier werkt!

Nu met het detecteren van de rotatie doormiddel van bit rekenen kan het programma alle geroteerde getallen zien

Uiteindelijke oplossing was dus het omzetten van de code van:
~~~
for (int i = 0; i < jsonArraySize; i++) {
    this.data.add(jsonArray.getInt(i));
}
~~~
Naar:
~~~
for (int i = 0; i < jsonArraySize; i++) {
    this.data.add(jsonArray.getJsonNumber(i).longValue());
}
~~~
Samen met het omzetten van de data ArrayList, van een Integer lijst naar een Long lijst.

#Applicaties die Json gebruiken

- Proglet

Json wordt gebruikt voor onder andere de configuratie, voor het parsen van de opgaven en voor het parsen van de resultaten.

- Visual studio code

Json wordt gebruikt voor de instellingen.

- Tiled

Hier hebben we gebruik van gemaakt met de proftaak.

