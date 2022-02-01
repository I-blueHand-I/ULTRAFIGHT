float xposBall = 400;
float yposBall = 400; //variable de position de la balle
float directionXball = 4;
float directionYball = 1;
//score des joueurs
int scoreR = 0;
int scoreB = 0;

void pong() {
  chargement(2, 2); //temps de chargement : mettre les commandes
  PONG.play();
  explicationPong();
  
  //println(countChargement);
  if (go) {//une fois le chargement terminé//
    displayPlayers();
    background(0); //permet initialiser un nouveau fond///
    Timer(4);
    timerY = height/2; // display timer au milieu de l'écran
    balldisplay();
    //display player for the pong 
    players[0].displayPong(red);
    players[1].displayPong(blue);
    for (int i =0; i <2; i++) {
      players[i].StayInWindow(20, 70);
    }
    //positions initiale des joueurs
    players[0].xpos = 20;
    players[1].xpos = 800; 
    //socre des joueurs 
    players[0].score(scoreR, width/2-100, 50, red);
    players[1].score(scoreB, width/2+50, 50, blue);    
    winPong();//victoire d'un des deux joueurs
    //----------------------------
    if (go1 == true) { //une fois le decompte terminé
      //déplacement des joueurs
      //joueur rouge
      if (_up)players[0].moveUp(6); //vitesse/speed des joueurs est de 6 : peut etre changer directement ici   
      if (_down)players[0].moveDown(6);   
      //joueur bleu 
      if (_up2)players[1].moveUp(6);
      if (_down2)players[1].moveDown(6); 
      ballMove();//la balle commence à bouger
      displayPongPlayers = false;
    }
  }
}
void balldisplay() {
  //apparence de la balle
  noStroke();
  fill (208, 187, 98);
  rect(xposBall, yposBall, 20, 20); ////
  ////fonctions de comportement de la balle////
  bounce();
  BallAgain();
}
void ballMove() {
  xposBall = xposBall + directionXball;
  yposBall = yposBall + directionYball;
  if (yposBall > height-20 || yposBall < 0) directionYball = -directionYball;
}

void bounce() {
  //collision entre la balle des les joueurs
  //joueur rouge
  if ( xposBall <= players[0].xpos+20 &&
    yposBall+20 >= players[0].ypos &&
    yposBall <= players[0].ypos + 70) {
    directionXball = -directionXball;
    directionYball = random(-4, 4);
    accel();
  }
  //joueur bleu
  if ( xposBall + 20 >= players[1].xpos && 
    yposBall <= players[1].ypos + 70 && 
    yposBall + 20 >= players[1].ypos) {
    directionXball = -directionXball;
    directionYball = random(-4, 4);
    accel();
  }
}
void BallAgain() { //fonction qui fait en sorte que la balle revienne au milieu constamment apres un but d'un joueur
  //but du joueur rouge
  if (xposBall >= width) {
    xposBall = width/2; //balle au centre
    directionXball =-5; //balle en direction de celui qui a marqué le point 
    scoreR += 1;
  }
  //but du joueur bleu
  if (xposBall <= 0) {
    xposBall =width/2;
    directionXball = 5;
    scoreB += 1;
  }
}
void accel() {
  directionXball= directionXball*1.1;
}

void winPong() {
  if (scoreR >= 7) {
    go = false;
    winRed = true;
    if (GoldenRed ==1 && winRed == true) {
      AceRed =true; //win maze + win mini jeux rouge
    } else { 
      run = 1;
      goBackMaze = true;
      winRed = false;
    }
  }
  if (scoreB >= 7) {
    go = false;  
    winBlue = true;
    if (GoldenBlue ==1 && winBlue == true) {
      AceBlue =true;
    }//win maze + win mini jeux bleu
    else {
      run = 1;
      goBackMaze = true;
      winBlue = false;
    }
  }
  if (scoreB == 5 && scoreR == 5) {
    key0 = true;
    winRed = false;//reinit le jeu pong
    winBlue = false;//reinit le jeu pong
    run = 1;
    goBackMaze = true;
  }
}
