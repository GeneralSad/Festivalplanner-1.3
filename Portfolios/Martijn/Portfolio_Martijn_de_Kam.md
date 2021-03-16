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

### Ontwerp

Hiervoor heb ik de powerpoints van het opstart college erbij gepakt, hierin stonden een aantal punten over de structuur waarin dit kan en wat er moet gebeuren.

![TiledMapStructure.png](TiledMapStructure.png)

Verder heb ik Tiled erbij gepakt en een willekeurige tiledmap gemaakt met verschillende layers om te gebruiken om mee te testen.

Voor het werken heb ik in de simulatie package een nieuwe package bijgemaakt met daarin alles over de tiledmaps met ook een uitvoerbare Main klasse om puur deze functionaliteiten te testen los van de andere code.

Om te kijken wat er nodig was qua code heb ik uit Tiled het bestand geëxporteerd als Json en daarin gekeken wat voor objecten en attribuuten er allemaal nodig zijn. Tijdens het kijken hiernaar heb ik ook opgemerkt dat dit heel erg in lijn is met wat er in de powerpoints staat over de structuur.

### Implementatie tiledmaps inladen

Na kijken naar het ontwerp en hoe ik dit wil gaan aanpakken ben ik gaan implementeren van de code. Hierbij ben ik tegen een paar punten aangelopen.

Met het programmeren van de data inlezen uit het Json bestand ben ik tegen twee problemen opgelopen:

- Met het lezen van het json bestand zelf kreeg ik een nullpointer exception.

Dit heb ik opgelost door eerst te kijken of de filepath string wel mee werd gegeven, dus het was niet dat er helemaal niets binnenkomt. 
Het volgende vermoeden is dat de filepath niet is herkend, om dit te testen heb ik de string een aantal keer aangepast en getest. 
Uiteindelijk heb ik de relative path copy gebruikt, deze werkte ook nog net niet, maar door een / ervoor te zetten werkte het wel.

- Tijdens het lezen van de array met tilesets kreeg ik een nullpointer exception bij het uitlezen van een specifieke waarde.

Met debuggen doormiddel van een paar breakpoints heb ik gevonden dat omdat een van de tilesets alleen "firstgid" en "source" als waardes heeft een nullpointer geeft wanneer bijvoorbeeld de "columns" wordt gevraagd.
Als oplossing heb ik de constructor een nullpointerexception laten throwen en bij het uitlezen van de array in een for loop een try catch gezet waarbij de tiledset alleen wordt toegevoegd als er geen nullpointer komt.

Uiteindelijk is het wel gelukt om de TiledMap in te lezen met correcte waardes.

### Tiledmaps tekenen

Met de ingeladen bestanden ben ik gaan kijken hoe ik het kon tekenen. Dit was echter heel simpel te doen met wat modulo en deel rekenen om de x en y positie te bepalen.
Verder ben ik niet tegen grote problemen aangelopen hier.

![TiledMapWorkingWK4.PNG](TiledMapWorkingWK4.PNG)

Bij de senior begeleiding heb ik wel wat feedback hierover gekregen. Namelijk dat in plaats van 1 afhalen van de data gid uit de tiledlayer, dat ik er de firstgid van de tiledset uit moet halen. Voor de eerste tiledset is het hetzelfde maar vanaf de tweede moet het op deze manier.

Verder was er ook laten zien dat het mogelijk is om op een grote afbeelding veel kleine afbeeldingen te plakken in plaats van die allemaal individueel naar het scherm te tekenen. Dit zou efficiënter kunnen zijn, maar dat was niet helemaal zeker.

### Tile rotatites herkenen

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

### tile rotaties tekenen

Een paar dagen later ben ik verder gaan kijken naar het tekenen van de geroteerde tiles.

Tijdens dit implementeren is opgevallen dat er twee rotaties mogelijk waren die ik hiervoor nog niet had gezien.
Namelijk een gespiegelde tile (horizontaal of verticaal gespiegeld) roteren links of rechtsom. Dit is nu wel toegevoegd.

Verder is dit onderdeel goed gegaan. Ik heb in een ander project met een simpele afbeelding getest hoe het roteren en spiegelen kan werken met de affinetransform en daarbij gekeken naar hoe het tekenen daarbij moet compenseren in de translatie.
Met die informatie ben ik begonnen met het daadwerkelijke project om daar het tekenen te implementeren, waarbij er nu aan de hand van de gedecteerde rotatie op de affinetransform rotaties, schalingen en translaties worden gemaakt om de tiles goed weer te geven.

Tijdens het implementeren ben ik tegen twee dingen aangelopen.

Een is dat in de tiledmap de tiledset niet altijd goed wordt meegegeven, door een beetje door de GUI knoppen van Tiled te spitten en proberen ben ik erachter gekomen dat de tiledset moet worden geëmbed in de tiledmap, anders wordt het niet meegenomen in de json.

Het tweede waar ik tegen aan ben gelopen is dat na de export naar json en het plakken in intelliJ de jsonreader het json bestand niet herkent, na meerdere malen nieuwe exports te proberen, de file directory aan te passen en de bestandsnaam aan te passen ben ik naar een vermoeden gekomen van het probleem.
Namelijk wanneer ik de bestandsnaam refactor werkt het laden wel en wanneer ik het nog een keer refactor naar het origineel werkt het nog steeds. Dus ik denk dat er iets mis gaat met het overzetten naar intelliJ. 
Hier ga ik bij de senior begeleiding over vragen of de senior een antwoord weet.

Bij de senior begeleiding wist de senior niet wat het probleem zou kunnen zijn.
Daarbij had ik ook live tijdens de begeleiding een nieuw json bestand in het project gezet en die geprobeerd te laden, naar dat werkte wel in een keer.
Dus ik weet niet zo goed meer wat het probleem was. Maar zolang het werkt voor het project is het zo goed.

## Week 5

In week 5 ben ik aan de slag gegaan met de NPC logica.

### Aanpak en ontwerp

Hiervoor heb ik een NPC klasse aangemaakt en een NPCManager klasse die de NPCs beheert en kan updaten en tekenen.

Verder heb ik om de functionaliteiten te testen een uitvoerbare klasse gemaakt: NPCTester, die de NPCs als simpele rechthoeken kan tekenen om het bewegen te laten zien.

### Implementatie basis logica

Tijdens de implementatie ben ik niet tegen grote problemen of beslissingsmomenten gekomen.
Het is allemaal redelijk recht aan direct dingen implementeren van wat ik al weet van ogp of 2dgraphics.

De punten die ik heb geimplementeerd zijn:

- Een NPC heeft wat simpele data, hierbij is het vooral belangrijk dat een NPC altijd een persoon is en daarop geïdentificeerd wordt.

- Een NPC kan bewegen doormiddel van een x en y snelheid attribuut en een x en y positie. Waarbij met de deltaTime van de update de speed waardes bij de desbetreffende positie waardes worden opgeteld met de deltaTime vermenigvuldigd.

- Een NPC kan collision zien met andere npcs, waarbij de npcs dan niet verder kunnen bewegen. Dit wordt gedaan door alle andere NPCs na te gaan. Dit is een prima oplossing voor kleine aantallen npcs, maar voor grotere aantallen moet er een efficiëntere methode komen (honderden of duizenden).

- Een NPC kan naar een bepaald punt worden gestuurd, waarbij die dan in een rechte lijn daarnaartoe loopt. Dit wordt gedaan door te kijken wat de verhouding is van de x en y afstand van de totale afstand, zodat de snelheden afgesteld zijn op elkaar om in de goede richting te gaan.

### Rotatie logica NPC

Een paar dagen later ben ik verdergegaan met de NPC logica en heb daarvoor de rotatie geïmplementeerd.
Hierbij waren de doelen het mogelijk maken om een NPC naar een bepaalde richting te laten draaien en voor de NPC om naar een bepaalde positie te wijzen waarbij die eerst draait en dan in de goede richting gaat lopen rechtvooruit.

In eerste instantie had ik de rotatie in graden gezet (0 tot 360), maar al snel ben ik overgestapt naar radialen (0 naar 2 PI) omdat veel methodes radialen gebruiken en het dan makkelijker is om het zo direct te doen.

Verder heb ik er ook voor gekozen om het oude systeem die met een xspeed en yspeed naar een punt toe gaat in de code te laten samen met het systeem voor het bewegen met de rotatie.
Hiervoor heb ik gekozen omdat we volgende week met pathfinding gaan werken en het goed kan zijn dat het oude systeem daar veel makkelijker op werkt dan met de rotaties, of dat een combinatie van beide beter is. Dus voor nu erin laten en later kijken wat beter is.

Omdat ik de verschillende systemen heb behouden heb ik ervoor gekozen om de verschillende onderdelen van de update methode te scheiden van elkaar in prive methodes.
Dit zijn dan een update om te checken of de NPC op de bestemming is, of de NPC collision heeft met andere NPCs, en natuurlijk de verschillende methodes voor xyupdate en rotatieupdate.
Dit geeft wat meer structuur, waarbij alleen een methode in comments hoeft worden gezet, in plaats van een heel blok code.

Om te zorgen dat de NPC ook stopt bij de bestemming zijn alle bewegingsupdates nu binnen een if statement die kijkt of de NPC al op de bestemming zit. 
Bij de destinationupdate veranderd de boolean naar true wanneer de NPC op de besttemming zit en bij het veranderen van de destination staat die op false.

Verder ziet het eruit na het testen dat de rotatie goed werkt. De npc draait een stukje naar de goede richting en gaat dan rechtstreeks naar een gekozen punt.

#Applicaties die Json gebruiken

- Proglet

Json wordt gebruikt voor onder andere de configuratie, voor het parsen van de opgaven en voor het parsen van de resultaten.

- Visual studio code

Json wordt gebruikt voor de instellingen.

- Tiled

Hier hebben we gebruik van gemaakt met de proftaak. Het gemaakte in het programma kan worden opgeslagen als een json bestand en een json bestand kan ook worden geïmporteerd.


