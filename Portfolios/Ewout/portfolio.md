# Portfolio Proftaak

## Week 3


Voor de comboBox van alle beschikbare tijden, was een hele was lijst met localdate times handmatig uitgeshreven, dit heb ik vervangen met een methode met een forloop, zo dat het makkelijk kan worden hergebruikt.

de klaslokalen stonden niet in schedule daardoor als klaslokalen waren angemaakt maar niet waren gebruikt werden ze niet opgeslagen, omdat alleen schedule werd opgeslagen, en ze stonden daar niet in, dit is opgelost door een arrayList toe tevoegen van klaslokalen en mee te geven met de constructor.

Er was een vbox met standaard spacing en padding, deze werd heel vaak gekopieerd en her gebruikt, dus heb een methode gemaakt waar je alle nodes aan kan meegeen en die zo'n vbox terug geeft, zodat je niet heel veel duplicate code hebt.
## Week 4

er miste een zwarte lijn aan het eind van een les dit kwam omdat er een grijze rechthoek overheen werd getekent, maar het omdraaien ging ook niet want dan stond er een lijn halverwege lessen die 2 tijd increments duren, de fixs was rechthoek 1 pixel kleiner maken zodat het er niet overheen werd getekend.

##### Van Arraylist naar ObservableList 
we gebruikte eerst overal een arraylist voor, dit zorgde er alleen voor dat zodra er iets van data werd veranderd, dat we dan ook het rooster handmatig moest vertellen dat er iets veranderd was. een Observable list doed dit automatisch hierdoor word de listview met alle lessen automatisch geupdate als er een les is toegevoegd.
de makkelijkste manier om dit aantepassen, was het aanpassen van de klassen schedule, maar wel zodat alle code die werkt met deze klassen wel blijft werken, in de klassen schedule zaten namelijk al die verwijder en toevoeg functies. deze heb ik aangepast om te werken met observable lists

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

om te testen had ik een deltatime nodig, maar mijn code was nog niet gekoppeld aan de animatieTimer dus moest even mijn eigen functie maken om het te testen
    
     private Long lastTime;
     
     public long deltaTime()
    {
        long deltaTime = LocalTime.now().toNanoOfDay() - lastTime;
        lastTime = LocalTime.now().toNanoOfDay();
        return deltaTime;
    }