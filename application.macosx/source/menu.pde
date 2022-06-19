//variables pour le texte clignotant du menu
float timeInterval;
float timePast;
int textAlpha = 50;
int textFade = 3;
//golden Pixels company 
int counterGpc = 0;
//variable pour la ligne du menu qui descend
int yLine;
//variable pour le timer & le chargement
int xposTimer = 900; // pour décompte 
int xposTimer2 = 100; // pour chargement 
int countChargement = 0; //pour chargement
int timerX = 395; //position du decompte X
int timerY = height/2;// position décompte Y
int Xchargement = 265;
boolean go = false; // boolean qui lance les mini jeux
boolean go1 = false; // pour le chargement
///display des personnages avant chaque mini jeux
boolean displayFightPlayers = true;
boolean displayDashPlayers = true; 
boolean displayPongPlayers = true; 
boolean displayMazePlayers = true; 

void menu() {
    background(Menu);
    drawfont();
    Line();  
}
void textFade () {
  if (millis() > timeInterval + timePast) {
    timePast = millis();
    textFade *= -1;
  }
  textAlpha += textFade;
}
void drawfont () {
  textFade();
  textFont(f);
  fill(208, 187, 98, textAlpha);
  text ("PRESS ANY KEY TO START", 150, 700);
}
void Line() {
  stroke(208, 187, 98);
  line(0, yLine, width, yLine);
  yLine++;
  if (yLine > height) {
    yLine = 0;
  }
}
//fonctions qui permet d'avoir un timer pour enchainer les jeux agréablement : sous forme d'un décompte//
//rectangle qui avance ----------> /// quand il franchit la fenetre -> go
void Timer(int time) { //décompte avant les mini-jeux 
  fill(0);
  rect(xposTimer, -100, 20, 20);
  xposTimer -= time;
  textFont(f2); //décompte visuel pour voir quand le jeu va se lancer
  fill(gold);
  text(xposTimer/150, timerX, timerY);
  //activation de la bool 
  if (xposTimer <= 0) {
    go1 = true; //activer la boolean qui permet de lancer les jeux
    timerX = 1000; //ecarter le décompte de la fenêtre
  }
}
void chargement(int time, int charg) {//chargement en cours : rajouter les commandes des mini-jeux 
  fill(gold);
  rect(xposTimer2, 600, 100, 50);
  //image(commands,0,0);
  xposTimer2 += time;
  chargementEnCours();
  if (xposTimer2 == 650) {    
    xposTimer2 = 100;
    countChargement += 1;
  }
  if (countChargement >= charg) {   
    Xchargement = 10000;
    go = true;
  }
}
void chargementEnCours() {
  fill(gold, textAlpha);
  textFont(f);
  textFade();
  text("DOWNLOADING", Xchargement, 700);
}

void displayPlayers() {//fonction -> position initiale des joueurs avant chaque mini jeux
  //fight
  if (displayFightPlayers == true) {
    if ( run == 4) {
      players[0].xpos = 600;
      players[0].ypos = 500;
      players[1].xpos = 100;  
      players[1].ypos = 500;
    }
  }
  //dash
  if (displayDashPlayers == true) {
    if ( run == 3) {
      players[0].xpos = 50;
      players[0].ypos = 50;
      players[1].xpos = width - 70;  
      players[1].ypos = height -70;
    }
  }
  //pong 
  if (displayPongPlayers == true) {
    if ( run == 2) {
      players[0].ypos = height/2-35;
      players[1].ypos = height/2-35;
    }
  }
  //MAZE
  if (displayMazePlayers == true ) {
    if ( run == 1 || run ==6) {
      players[0].xpos =100;
      players[0].ypos = 100;
      players[1].xpos = width - 170;  
      players[1].ypos = height -170;
    }
  }
}
