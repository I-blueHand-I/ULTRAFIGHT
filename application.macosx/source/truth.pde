//booleans qui activent les clefs//
boolean key0 = false;//clef du PONG
boolean key1 = false;//clef du DASH
boolean key2 = false;//clef du FIGHT
//boolean du secret//
boolean fin;
boolean finRed; 
boolean finBlue;
//variables pour faire apparaitre le secret
float xBgTruth = 0;
float transparency = 255;
//variable pour les credit timer
int xposRectEnd = -500;
//final pour que le pixel violet monte//
boolean _upEnd;//déplacement pixel violet//
boolean _upEnd1;//déplacement pixel violet//
float yposPurple = 400;


void Truth() {
  strokeWeight(3); //contour de la fenetre de chargement en GOLD
  stroke(gold);
  chargement(2, 0);
  println(_upEnd);
  if (go) {
    displayPlayers();
    //invoquer fonction qui permet aux joueurs de réaliser le secret 
    //////---FAIRE APPARAITRE LE SECRET////
    /////Quand toutes les clefs ont été positionnées dans le labyrinth alors lancer la fin/////
    image(loadImage("bg_truth.png"), xBgTruth, 0); 
    PositionPlayers();
    if (fin) {//conditionskey0 && key1 && key2 && key3
      xBg = -10000; //remove the principale background to load the image pixels by pixels
      xBgTruth = -10000; //remove the principale background to load the image pixels by pixels
      xposTimer2 = 1000;//remove le download
      Xchargement = 10000;
      //créer la tranparence avec le fond///
      if (transparency > 0) { 
        transparency -= 15;
      }
      tint(255, transparency);
      image(loadImage("bg_truth.png"), 0, 0);   
      truth.loadPixels();
      for (int i = 0; i < 40; i++) {
        int varX = int(random (width));
        int varY = int(random (height)); 
        fill(truth.pixels[varY*width + varX]);  
        strokeWeight(0.1);
        rect(varX, varY, 4, 4);
      }
    }
    /////-----FIN APPARITION SECRET------///////////////
    //////Display players dans le MAZE////
    //display les joueurs sur le jeu//
    players[0].displayDash(red);
    players[1].displayDash(blue);   
    //déplacement
    //joueur rouge 
    if (_up)players[0].moveUpMaze();   
    if (_down)players[0].moveDownMaze();
    if (_left)players[0].moveLeftMaze();
    if (_right)players[0].moveRightMaze();
    players[0].dirX = 2;
    players[0].dirY = 2;
    //joueur bleu
    if (_up2)players[1].moveUpMaze(); 
    if (_down2)players[1].moveDownMaze();  
    if (_left2)players[1].moveLeftMaze();  
    if (_right2)players[1].moveRightMaze();
    players[1].dirX = 2;
    players[1].dirY = 2;
    displayMazePlayers = false;
  }
}

void PositionPlayers() {
  Xpixel = 410;
  Ypixel = 400;
  fill(0);
  stroke(gold);
  rect(Xpixel, Ypixel, 30, 30);
  if (( players[1].xpos + 20 >= Xpixel && players[1].xpos <= Xpixel+ 30  && players[1].ypos + 20 >= Ypixel && players[1].ypos <= Ypixel + 30 )) finBlue = true;
  if (( players[0].xpos + 20 >= Xpixel && players[0].xpos <= Xpixel+ 30  && players[0].ypos + 20 >= Ypixel && players[0].ypos <= Ypixel + 30 )) finRed = true;
  if (finRed == true && finBlue == true) {
    fin = true;
    players[0].xpos = 1000;
    players[1].xpos = 1000;
    strokeWeight(1);
    stroke(gold);
    fill(purple);
    rect(410, yposPurple, 30, 30);
    if (_upEnd == true && _upEnd1 == true )moveUpPurple();
    if (yposPurple <= 195)run=7;
  }
}
void moveUpPurple() {//faire bouger le pixel violet à deux//
  yposPurple  = yposPurple - 0.15;
}
void credit() { //crédit de fin du jeu 
  fill(0);
  rect(xposRectEnd, 0, 860, 780);
  xposRectEnd += 10;
  if (xposRectEnd >= 500) {
    textFont(f1);
    fill(gold);
    text("music & developing\nby Hugo Bijaoui", width/2 -100, height/2);
    text("Pour Raph", 700, 730);
  }
  if (xposRectEnd >= 2500) {
    fill(0);
    rect(0, 0, 860, 780);
  }
}
