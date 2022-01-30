class obstacle {
  float X;
  float Y;
  float directionX, directionY;
  float Xwidth, Yheight;
  obstacle(float Mywidth, float Myheight) {
    X= Mywidth;
    Y= Myheight;
  }
  void move() {
    X=X+directionX;
    Y=Y+directionY;
  }
  void display() {
    fill(208, 187, 98); // COULEUR OR  
    rect(X, Y, Xwidth, Yheight); //parametres que l'on peut modifier dans le setup
    noStroke();
    if (Y-Yheight > height-Yheight*2 || Y < 0) directionY = -directionY; //rebond des obstacles en  Y
    if (X-Xwidth > width-Xwidth*2 || X < 0) directionX = -directionX;//rebond des obstacles en X
  }
}
void dash() {
  chargement(2, 2);
  DASH.play();
  PONG.pause();
  explicationDash();
  if (go) {
    displayPlayers();
    background(0);
    Timer(4);//delay avant que le jeu commence 
    timerY=100;//position spécifique du timer pour le DASH
    collide();//collision entre les joueurs et les obstcles
    //display les obstcales sur le jeu//
    for  (int i = 0; i<9; i++) {
      obstacles[i].display();//display des obstacles
    }
    //display (postions et couleurs) les joueurs sur le jeu//
    players[0].displayDash(red);
    players[1].displayDash(blue);
    for (int i =0; i <2; i++) {
      players[i].StayInWindow(20, 20);
    }
    //une fois le décompte terminé :
    if (go1) { 
      //obstacles bougent
      for (int i = 0; i<9; i++) {
        obstacles[i].move();
      }
      //déplacement des joueurs && les joueurs se déplacent//
      //joueur rouge
      if (_up)players[0].moveUp(5);   
      if (_down)players[0].moveDown(5);
      if (_left)players[0].moveLeft();
      if (_right)players[0].moveRight();       
      //joueur bleu
      if (_up2)players[1].moveUp(5); 
      if (_down2)players[1].moveDown(5);  
      if (_left2)players[1].moveLeft();  
      if (_right2)players[1].moveRight();
      displayDashPlayers = false;
    }
    //----------------------------
  }
}

void collide() { //fonction de collision entre joueur et obstcales//
  for (int i = 0; i<2; i++) {// on parcourt les joueurs  
    if (players[i].xpos + 20 >= obstacles[7].X &&
      players[i].xpos <= (obstacles[7].X + obstacles[7].Xwidth) &&
      obstacles[7].Y <= players[i].ypos+20 && 
      players[i].ypos <= (obstacles[7].Y + obstacles[7].Yheight)) {
      key1=true;
      winRed = false;//reinit le jeu dash
      winBlue = false;//reinit le jeu dash
      run = 1;
      goBackMaze = true;//reinit les valeurs
    }
    for (int j = 0; j<9; j++) {// on parcourt les obsstacles
      //condition qui pemret la collision//
      if (obstacles[j].X <= players[i].xpos + 20 && 
        players[i].xpos  <= (obstacles[j].X + obstacles[j].Xwidth) &&
        obstacles[j].Y <= players[i].ypos+20 && 
        players[i].ypos <= (obstacles[j].Y + obstacles[j].Yheight)) {
        // si le joueur rouge touche les obstacles 
        if (i == 0) { 
          winBlue = true;
          go1 = false;
          go = false;
          if (GoldenBlue ==1 && winBlue == true) {
            AceBlue =true;
          }//win maze + win mini jeux rouge 
          else { 
            run = 1;
            goBackMaze = true;
            ;//reinit les valeurs 
            winBlue = false;//reinit le jeu dash
          }
        }    
        //si le joueur bleu touche les obstacles 
        if (i == 1) { 
          winRed = true;
          go1 = false;
          go = false;
          if (GoldenRed ==1 && winRed == true) {
            AceRed =true;
          }//win maze + win mini jeux bleu
          else {
            run = 1;
            goBackMaze = true; //reinit les valeurs            
            winRed = false;//reinit le jeu dash
          }
        }
      }
    }
  }
}
