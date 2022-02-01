//variables pour la fonction Line (déco)
int yLine2 = 50, yLine3= -400, xLine1, xLine2 = -50;
int directionLineX; 
int directionLineY; 

//pour  le sens des joueurs//
char sens ='d'; //d = vers la droite, g = vers la gauche 
//variables pour la vie et l'energie
int lifeRed = 300;
int lifeBlue = 300;
int energyRed = 0;
int energyBlue = 0;
//variables pour les attaques des joueurs
int TimerFightRed = 0;
int TimerFightBlue = 0;
boolean can_attaqueRed = false;
boolean can_attaqueBlue = false;

void fight () {
  //--------------joueurs---------------//
  chargement(2, 2);//temps de chargement : mettre les commandes
  explicationFight();
  if (go) {
    displayPlayers();
    gameplay();
    WinFight();
    LifeEnergy();
    hitBox();
    Lines();//lignes de décoration///
  }
}
void gameplay() {
  background(fight);
  Timer(4);//décompte avant le fight
  timerY = width/3 ; //display timer au milieu de l'écran
  for (int i = 0; i<2; i++) {
    //players[i].ypos = 500; //bon niveaux en Y pour les joueurs//
    players[i].StayInWindow(130, 130);
  }
  //char qui permet de faire pivoter les joueurs quand ils changent de coté
  if (players[0].xpos >= players[1].xpos  ) sens = 'g';
  else sens ='d';  
  ///---------------------------------------------//
  ///-----------------GAMEPLAY--------------------//
  //différente position des joueurs : debout/accroupis/attack////
  //-------------------joueur rouge----------------//
  if (_squatRed) {//accroupis
    players[0].displayFight(loadImage("fight/redDown_"+sens+".png"), players[0].xpos, players[0].ypos);
    energyRed -= 0.25;//perte d'energie lorsqu'accroupis
    if (energyRed == 0) _squatRed = false;
  } else if (attackRed) { 
    if (players[0].xpos >= players[1].xpos  ) { //si les joueurs ont pivoté///      
      players[0].displayFight(loadImage("fight/redAtt_"+sens+".png"), players[0].xpos-132, players[0].ypos);//faire xpos -130 pour pas avoir de saut
    } else  players[0].displayFight(loadImage("fight/redAtt_"+sens+".png"), players[0].xpos, players[0].ypos);//dans l'autre cas ne pas bouger le xpos du joueur
  } else players[0].displayFight(loadImage("fight/red_"+sens+".png"), players[0].xpos, players[0].ypos);// position normal
  ////
  //-------------------joueur bleu----------------//
  if (_squatBlue) {//accroupis  
    players[1].displayFight(loadImage("fight/blueDown_"+sens+".png"), players[1].xpos, players[1].ypos);
    energyBlue -= 0.25;//perte d'energie lorsqu'accroupis
    if (energyBlue == 0) _squatBlue = false;
  } else if (attackBlue) { ///si les joueurs ont pivoté/// 
    if (players[0].xpos >= players[1].xpos  ) {
      players[1].displayFight(loadImage("fight/blueAtt_"+sens+".png"), players[1].xpos, players[1].ypos);//cas ne pas bouger le xpos du joueur
    } else players[1].displayFight(loadImage("fight/blueAtt_"+sens+".png"), players[1].xpos-132, players[1].ypos);//fautre cas où aire xpos -130 pour pas avoir de saut
  } else players[1].displayFight(loadImage("fight/blue_"+sens+".png"), players[1].xpos, players[1].ypos);//normal 
  ////attaque des joueurs////
  players[0].timerFight(); //cd joueur rouge
  players[1].timerFight(); //cd joueur bleu
  //déplacement des joueurs//
  //jump joueurs
  if (players[0].can_jump)players[0].jump(0.32);//arguement variable pour gérer le saut
  if (players[1].can_jump)players[1].jump(0.32);
  if (go1) {
    //joueur rouge
    if (_left)players[0].moveLeft();
    if (_right)players[0].moveRight(); 
    //joueur bleu
    if (_left2)players[1].moveLeft();  
    if (_right2)players[1].moveRight();
    displayFightPlayers = false;
  }
  //----------------------------FIN DE GAMEPLAY-----------------------------//
}

void hitBox() {// fonction qui permet d'infliger des déguats à l'adversaire///
  //attaques des joueurs 
  //joueur rouge
  if (attackRed) { //condition pour l'attaque rouge pour infliger des dégats
    if (_squatBlue)lifeBlue = lifeBlue;//pour dire qu'aucun dégat ne sera infliger     
    else  if (players[0].xpos >= players[1].xpos && players[0].xpos - players[1].xpos <= 240 && players[0].ypos - players[1].ypos <= 100 //pour joueur rouge à droite
      || players[0].xpos <= players[1].xpos && players[1].xpos - players[0].xpos <= 240 && players[0].ypos - players[1].ypos <= 100) {//pour joueur rouge à gauche
      lifeBlue -=10;//fait perdre de la vie
      energyRed += 15;//fait gagner de l'energie
      TimerFightRed = 0;//cooldown de l'attaque 
      can_attaqueBlue = false;/////////////////
      attackRed = false;///////////////////
    }
  }
  //joueur bleu
  if ( attackBlue) {
    if (_squatRed)lifeRed = lifeRed;//pour dire qu'aucun dégat ne sera infliger
    else if (players[0].xpos >= players[1].xpos && players[0].xpos - players[1].xpos <= 240 && players[1].ypos - players[0].ypos <= 100 //pour joueur bleu à droite
      || players[0].xpos <= players[1].xpos && players[1].xpos - players[0].xpos <= 240 && players[1].ypos - players[0].ypos <= 100) {//pour joueur bleu à gauche
      lifeRed -=10;//fait perdre de la vie
      energyBlue += 15;//fait gagner de l'energie
      TimerFightBlue = 0;//cooldown de l'attaque 
      can_attaqueBlue = false;/////////////////
      attackBlue = false;/////////////////
    }
  }
} 
void LifeEnergy() {
  ///---------------------LIFE & ENERGY------------------///
  players[0].life(lifeRed, red, 50);
  players[1].life(lifeBlue, blue, width-350);
  players[0].energy(energyRed, 50);
  //condition pour avoir une barre d'energie max de 300//
  if ( energyRed >= 300)energyRed = 300;
  players[1].energy(energyBlue, width-350);
  if ( energyBlue >= 300)energyBlue = 300;
  //////--------UTILISATION ENERGY--------------///
  if (energyRed <= 0) energyRed = 0; //pour pas que la barre descende en dessous de 0
  if (energyBlue <= 0) energyBlue = 0;
}

void WinFight() {
  if ( lifeRed == 0) {
    winBlue = true;
    go = false;
    if (GoldenBlue ==1 && winBlue == true)AceBlue =true;//win maze + win mini jeux bleu
    else {
      run = 1;
      goBackMaze = true;
    }
  } 
  if (lifeBlue == 0) {
    winRed = true; 
    go = false;
    if (GoldenRed ==1 && winRed == true)AceRed =true; //win maze + win mini jeux rouge 
    else { 
      run = 1;
      goBackMaze = true;
    }
  }
  if ( lifeBlue == 10 && lifeRed == 10) { 
    key2 = true;
    winRed = false;//reinit FIGHT
    winBlue = false;//reinit FIGHT
    run = 1;
    goBackMaze = true;
  }
}


void Lines() { //fonction décorative//
  //line qui vont de haut en bas///
  stroke(208, 187, 98);
  line(0, yLine, width, yLine);
  yLine +=2;
  if (yLine > height) {
    yLine =0;
  }
  line(0, yLine2, width, yLine2);
  yLine2 += 2;
  if (yLine2 > height) {
    yLine2 = 0;
  }
  line(0, yLine3, width, yLine3);
  yLine3++;
  if (yLine3 > height) {
    yLine3 = 0;
  }
  //
  //lignes qui vont de droite à gauche///
  line(xLine1, 0, xLine1, height);
  xLine1++;
  if (xLine1 > width) {
    xLine1 = 0;
  }
  line(xLine2, 0, xLine2, height);
  xLine2++;
  if (xLine2 > width) {
    xLine2 = 0;
  }
}
////--------------------------------------////
