# portefolio Luuk van Berkel 2169248

## week 3
Deze week heb ik samen met eddy en leon gewerkt aan het koppelen van gui. 
Hierbij moest de database worden gekoppeld aan de gui popupwindow. 
Deze is varandwoordelijk voor het wijzigen van de database. 

Problemen waarbij ik tegen aan liep was bijvoorbeeld de vbox met checkboxes. Ik heb daar een functie voor gemaakt die het aantal checkboxes tekenend aan de hand van de klasse in de database.
Deze methode is nog niet observable ik vindt het namelijk lastig om met observable te werken met de listview. 
Dit moet nog worden aangepast. Ik had eerst ook updat functies die alles deden syncen maar na refactoren waren deze niet meer nodig.

Code van de checkbox functie:

    public VBox selectClass(){
       VBox classselector = new VBox();
       classselector.setSpacing(5);
       classselector.setPrefHeight(110);
        for (String string : classes)
        {
            CheckBox box= new CheckBox(string);
            classselector.getChildren().addAll(box);
        }
        return classselector;
    }
    
    
##week 4
Deze week heb ik niet geprogrameerd maar gewerkt aan het ontwerp en het klasse diagram. Ook heb ik met leon samen gekeken hoe tiled werkt. Van deze week ben ik dus geen grote problemen tegen gekomen.
![ClassDiagramWeek4.jpg](ClassDiagramWeek4.jpg)

##week 5
Deze week heb ik het ontwerp verbeterd en ben ik begonnen met een sprite klasse die de data van de klasse npc relayd en hem teken met de correcte orrienatatie. Hierbij vraag ik de richting op en kijk ik of het tussen bepaalde graades valt. Aan de handt hiervan bepaal ik de richting van de sprite. 

De loop animatie werkt nu ook goed. Hierbij update hij hem elke aantal pixels dat is afgelegd. Hierdoor ben je niet afhankelijk van de delatime. Maar zorgt er wel voor dat het gesynced er mee is.

Ook ben ik verder gegaan met het ontwerpen. Ik heb het klassediagram aangepast, ook was ik vergeten een wireframe diagram te maken. Deze heb ik nu ook gemaakt. Hier zijn de diagramen die ik gemaakt heb:

![ClassDiagramWeek5.jpg](ClassDiagramWeek5.jpg)

![WireFrameDiagramWeek5.jpg](WireFrameDiagramWeek5.jpg)

De grootste problemen waar ik tegen aan liep was niet zo zeer het programmeren van mijn code maar het laten aansluiten van mijn code op de NPC klasse die Martijn had geschreven. Deze moest ik hiervoor namelijk tot de details begrijpen.

Nadat ik het begreep ging het vrij soepel met het maken van de npc sprite klasse. De functie die mij het meeste tijd kost was het animatie frame updaten.


/code


##week6
Deze week heb ik de npc sprite klasse van vorige week uitgebreid en aangepast ook heb ik behavior aan de npc toegevoegd als hij op locatie aan komt. Dit doet random bepalen. Ook heb ik de frames op een random punt te laten starten zodat een massa er natuurlijker uit ziet. Dit heb ik deze week aangepast aan de klasse NPC sprites er zijn nog andere verandering maar deze worden nog besproken in de andere alinea want deze waren noodzakelijk om de npc te laten zitten.

 





    


