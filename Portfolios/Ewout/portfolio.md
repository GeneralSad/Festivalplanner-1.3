# Portfolio Proftaak

## Week 3

Deze week heb ik gewerkt samen met Martijn, maar na een tijdje kwam ik erachter dat het werk wat de andere aan het doen waren nodig was, voor mijn onderdelen. de rest was namelijk bezig met het maken van een functie om dingen toevoegen en ik moest dingen wijzingen.

Voor de comboBox van alle beschikbare tijden, was een hele was lijst met localdate times handmatig uitgeschreven, dit heb ik vervangen met een methode met een for loop, zo dat het makkelijk kan worden hergebruikt.

de klaslokalen stonden niet in schedule daardoor als klaslokalen waren aangemaakt maar niet waren gebruikt werden ze niet opgeslagen, omdat alleen schedule werd opgeslagen, en ze stonden daar niet in, dit is opgelost door een arrayList toe toevoegen van klaslokalen en mee te geven met de constructor.

Er was een vbox met standaard spacing en padding, deze werd heel vaak gekopieerd en her gebruikt, dus heb een methode gemaakt waar je alle nodes aan kan meegeven en die zo'n vbox terug geeft, zodat je niet heel veel duplicaten code hebt.

deze week is goed verlopen en alles was af voor de deadline.
## Week 4

deze week heb ik ervoor gezord dat lessen minimaal een half uur kunnen zijn, en dat dat ook juist word weergegeven.

terwijl ik bezig was heb ik ook nog meegeluisterd en input gegeven op het ontwerp van de simulatie module.

er miste een zwarte lijn aan het eind van een les dit kwam omdat er een grijze rechthoek overheen werd getekend, maar het omdraaien ging ook niet want dan stond er een lijn halverwege lessen die 2 tijd increments duren, de fixs was rechthoek 1 pixel kleiner maken zodat het er niet overheen werd getekend.

deze week was vooral veel uitzoek werk en deels trail en error, om het goed werkende te krijgen, hierdoor is de code van deze week niet het mooiste geworden.

##### Van Arraylist naar ObservableList 
we gebruikte eerst overal een arraylist voor, dit zorgde er alleen voor dat zodra er iets van data werd veranderd, dat we dan ook het rooster handmatig moest vertellen dat er iets veranderd was. Een Observable list doet dit automatisch hierdoor word de listview met alle lessen automatisch geüpdatet als er een les is toegevoegd.
de makkelijkste manier om dit aan te passen, was het aanpassen van de klassen schedule, maar wel zodat alle code die werkt met deze klassen wel blijft werken, in de klassen schedule zaten namelijk al die verwijder en toevoeg functies. deze heb ik aangepast om te werken met observable lists

    private ArrayList<Lesson> lessonArrayList;
    private ObservableList<Lesson> lessonObservableList;
    
    public Schedule(ArrayList<Lesson> lessons, ArrayList<Teacher> teacherArrayList, ArrayList<Group> groupArrayList)
    {
        this.lessonArrayList = lessons;
       
        this.lessonObservableList = FXCollections.observableList(lessons); //deze lijn heb ik toegevoegd zodat de obserable list de Arraylist Gebruikt om zijn gegevens op te slaan
   
    }
    public void removeLesson(Lesson lesson) //deze methode hoefde ik alleen maar lessonArrayList aan te passen naar lessonObservableList
    {
            lessonObservableList.remove(lesson);  
    }
het was dus makkelijk toe te passen door bij de verwijderd methodes de arraylist te vervangen voor de observable list.

##week 5

deze week moest ik een tijdsysteem gaan maken, waarmee we de tijd van de simulatie konden zien, en dan de  huidige lessen bij dit tijdstip kunnen ophalen.
ook moet er verschillende tijdsverlopen mogelijk zijn, zoals versneld.



TimeManager is het onderdeel wat je gebruikt om de "Huidige" tijd te krijgen van simulatie, 
deze klassen heeft een TimeType, dit is een klassen van de Interface TimeType. TimeType bevat de volgende dingen:
* getTime //hiermee vraag je de "huidige" tijd op
* update  //hiermee update je de tijd met een deltatime
* getSpeedFactor // hiermee krijg de versnellings factor, 1 als hij niet versneld word.


om te testen had ik een deltatime nodig, maar mijn code was nog niet gekoppeld aan de animatieTimer dus moest even mijn eigen functie maken om het te testen
    
       
     private Long lastTime;
     
     public long deltaTime()
    {
        long deltaTime = LocalTime.now().toNanoOfDay() - lastTime;
        lastTime = LocalTime.now().toNanoOfDay();
        return deltaTime;
    }
      
het verloop van tijd achteruit verschild heel weinig van de normale tijd enigste verschil is dat de deltatime negatief is, hierdoor word er een negatief getal toegevoegd aan de tijd en dus loopt de tijd achteruit.

om de huidige lessen om te halen, word een al bestaande methode gebruikt, deze was origneel bedoeld om met de begin en eindtijd van een les te kijken welke lessen er tijdens die tijd nog meer plaats vinden, maar als je hier de huidige tijd 2 maal instopt krijg je alle lessen die bezig zijn op dat moment.

#week 6
deze week heb ik gewerkt samen met luuk, voor een deel. we zijn aan de slag gegaan met het samenvoegen van de het tekenen van npc's en het tijdsysteem
dit zorgt ervoor dat npc's op de juiste locaties worden getekend op het juiste moment.
ook heb ik er voor gezorgd dat als je klas vult met leerlingen dat je geen dubbele krijgt.


#week 7

bug gefixed dat 10 uur werd geskipped, kwam omdat er niet naar veranderingen werden gekeken tijd een les.

#javaFX in het bedrijfsleven


javaFX is niet alleen iets wat wij leren gebruiken op school, het word namelijk ook gebruikt in het bedrijfsleven. 

##CrossPlatform
javaFX is CrossPlatform.
dit houd in dat voor verschillende platformen zoals (Windows, macOS, linux) dat je geen nieuwe code hoefd te schrijven, en het meteen zou moeten werken. (RayalSloth, 2020)

dit zorgt binnen het bedrijfsleven dat er niet extra geprogrammeerd hoefd te worden voor verschillenden platformen en dit bespaard weer geld.

ook als er dan ergens een wijzigeging word gemaakt, dan hoef je dat niet voor elk systeem los te doen.

##Verbinding met andere systemen
ook word javaFX vaak gebruikt om dat het in Java geschreven, dit maakt het makkelijk om te koppelen aan andere systemen. 

Dit gebeurd dan vaak in 1 van de volgende 2 manieren(Oracle, 2013)
* Thin Client Architecture
* Thick Client Architecture


bij thin word java en javaFX alleen maar gebruikt om de data te tonen, vaak word de data ontvangen door zoeits als json, en bijna direct getoond zonder teveel berekkeningen.

bij thick, word er ook gebruikt van api calls, ook heeft de applicatie ook de meer mogelijkheden dan alleen het tonen de data, het kan het ook wijzingen verwijderen en toevoegen.






# Json in Applicaties





# bron vermelding
Sad state of cross platform GUI frameworks. (2020, 16 maart). RoyalSloth. https://blog.royalsloth.eu/posts/sad-state-of-cross-platform-gui-frameworks/#javafx

oracle. (2013, 1 maart). Why, Where, and How JavaFX Makes Sense. https://www.oracle.com/technical-resources/articles/java/casa.html