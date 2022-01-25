//VARIABLES POSITIONS DU PIXELS D OR
int Xpixel = 410; //INITIALE
int Ypixel= 400;
int newX = 1000;//lorsqu'il est récupéré
int newY = 1000; 
//Random maze
boolean RandomMaze = false;
int RandomMazeImg;
//VARIABLES BOOLEENES lorsqu'un joueur attrape le pixel d'or 
int GoldenRed = 0; 
int GoldenBlue = 0;
//variables de victoire//
boolean AceRed =false; // si joueur rouge Ace
boolean AceBlue =false;// si joueur bleu Ace
//boolean de victoire pour tous les mini jeux 
boolean winRed = false; 
boolean winBlue = false;
//boolean de reinitialisation go back to maze//
boolean goBackMaze = false;

void maze() {
  chargement(2, 2);//chargement avec argument : vitesse = 2 et nombre de répétition = 3
  Xchargement = 265;//remettre chargement au milieu pendant le temps
  if (go) {
    maze=loadImage("maze/maze00"+RandomMazeImg+".JPG");
    maze.loadPixels();
    displayPlayers();
    updatePixels();
    GoldenPixel(); //invocation du pixel d'or
    winAce();
    //display les joueurs sur le jeu//
    players[0].displayMaze(0, red); //invoke joueur rouge
    players[1].displayMaze(1, blue); //invoke joueur bleu
    //Collision entre les joueurs
    GoldenCollide();    
    //déplacement des joueurs//
    //joueur rouge
    if (_up)players[0].moveUpMaze();   
    if (_down)players[0].moveDownMaze();
    if (_left)players[0].moveLeftMaze();
    if (_right)players[0].moveRightMaze();
    //joueur bleu
    if (_up2)players[1].moveUpMaze(); 
    if (_down2)players[1].moveDownMaze();  
    if (_left2)players[1].moveLeftMaze();  
    if (_right2)players[1].moveRightMaze();
    countChargement = 0; 
    displayMazePlayers = false; 
    RandomMaze = false;
    //----------------------------
  }
}
void GoldenPixel() {
  fill (208, 187, 98);
  rect (Xpixel, Ypixel, 15, 15);
  noStroke();
  //collision avec le pixels d'or et permet aux joueur de le récuprer
  for (int i =0; i<2; i++) {
    color leftColor = get((int(players[i].xpos)-1 ), (int(players[i].ypos)+i));
    color rightColor = get((int(players[i].xpos + 20)), (int(players[i].ypos)+20+i));
    if (( leftColor == color(208, 187, 98)  || ( rightColor == color(208, 187, 98)))) {
      Xpixel = newX;
      Ypixel = newY;
      if (i==0) {
        players[0].dirX = 2;//le personnage rouge est ralenti
        players[0].dirY = 2;
        GoldenRed = 1;
        //éventuellement changer la couleur du joueur si il recupère le pixel///
      } else if (i == 1) {//le personnage bleu est ralenti
        players[1].dirX = 2;
        players[1].dirY = 2;
        GoldenBlue = 1;
      }
    }
  }
}
void winAce() {
  //fonction qui s'applique seulement si un des deux joueurs récupère le pixel d'or et s'enfuit du Maze sans se faire
  //attraper par l'autre
  //condition pour gagner 
  //joueur rouge
  if (GoldenRed == 1) {
    if (players[0].xpos >= width  || players[0].xpos <= 0 ||
      players[0].ypos >= height || players[0].ypos <= 0) {
      textFont(f);
      fill(255, 0, 0);
      go = false; 
      AceRed = true; // joueur rouge a gagné
      GoldenRed = 0; //init again pour la partie suivante
    }
  }
  //joueur bleu
  if (GoldenBlue == 1) {
    if (players[1].xpos >= width  || players[1].xpos <= 0 ||
      players[1].ypos >= height || players[1].ypos <= 0) {
      textFont(f);
      fill(0, 0, 255);
      go = false; 
      AceBlue = true; // joueur bleu à gagné
      GoldenBlue = 0;//init again pour la partie suivante
    }
  }
}

void GoldenCollide() { //Fonction qui s'active dans le cas ou le joueur bleu et rouge rentrent en collision apres avoir récupérer le pixel
  if (GoldenRed ==1 || GoldenBlue == 1 ) {
    if (players[0].xpos <= players[1].xpos+20 && 
      players[0].xpos + 20 >= players[1].xpos && 
      players[0].ypos <= players[1].ypos +20 &&
      players[0].ypos + 20 >= players[1].ypos) {
      runRandom = true;
      fill(gold);
      textFont(f);
      go = false;
    }
  }
}

void GoBacktoMaze() { // fonction qui permet de reinitialiser les paramètres du jeu
  //réinitialisation des valeurs présentes dans le labyrinthe
  RandomMaze = true; // permet de changer de Maze a chaque fois qu'on y retourne//
  GoldenRed = 0;
  GoldenRed = 0;
  Xpixel = 410; //position du pixel d'or
  Ypixel = 400;
  //réinitialisation des valeurs présentes dans le Pong
  winBlue = false; //victoire d'un des deux joueurs 
  winRed = false;
  scoreB = 0;//leurs score
  scoreR = 0;
  xposBall = 400;//position de la balle remise au centre
  yposBall = 400;
  //réinitialisation du timer1
  xposTimer = 900;
  go = false;
  go1 = false;
  timerX = 395;
  //réinitialisation du temps de chargement 
  countChargement = 0;
  Xchargement = 265;
  //reinit de la vitesse des joueurs dans le layrinthe
  //rouge
  players[0].dirX = 4;//le personnage rouge est ralenti
  players[0].dirY = 4;
  //bleu 
  players[1].dirX = 4;
  players[1].dirY = 4;
  //reinit position pour tous les jeux 
  displayMazePlayers =true;
  displayPongPlayers = true;
  displayDashPlayers = true;
  displayFightPlayers = true;
  //reinit obstcales 
  for (int i = 0; i <9; i++){
  obstacles[i].X = height/3;
  obstacles[i].Y = height/3;  
  }  
  //reinit victory//
  Ypodium = height/2;
  Ypodium2 = height/2;
  //--FIGHT---////
  lifeRed = 300;
  lifeBlue = 300;
  energyRed = 0;
  energyBlue = 0;
  goBackMaze = false;
  ///////////////////
}
