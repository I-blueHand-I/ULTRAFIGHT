class player {
  float xpos;
  float ypos;
  float dirX;//pour le MoveMaze
  float dirY;
  // pour le fight
  float ay, py, vy;//pour le jump du fight//
  boolean can_jump = false;

  player(float tempx, float tempy) {
    xpos= tempx;
    ypos= tempy;
  }
  //actions necéssaires pour le pong, DASH//
  void moveUp(int speed) { 
    ypos = ypos-speed;
  }
  void moveDown(int speed) {
    ypos=ypos+speed;
  }
  //actions necéssaires pour le DASH//
  void moveLeft() {
    xpos = xpos-5;
  }
  void moveRight() {
    xpos= xpos+5;
  }
  void jump(float ratio) {//uniquement pour le FIGHT
    ay = ratio;
    vy+=ay;   
    ypos = ypos +vy; 
    if (ypos>500) {
      ypos=500;
      vy = 0;
      can_jump = true;
      canJump();
    }
  }
  void canJump() {//permet de sauter fonction appeler dans jump//
    if (can_jump) {     
      vy = -15;//hauteur du bond//
      can_jump = false; //évite la possibilité de double jump voir triple... +1000 jump
    }
  }
  void timerFight() { //cooldown pour attaquer//
    //joueur rouge///
    TimerFightRed += 4; //vitesse de cd pour l'attaque
    if (TimerFightRed >= 300) {
      can_attaqueRed = true;
      rect(50, 30, 10, 10); //visuel qui permet de voir si l'attaque est disponiible
      if (attackRed) {
        TimerFightRed = 0;
        can_attaqueRed = false;
      }
    }
    //joueur bleu
    TimerFightBlue+= 4;
    if (TimerFightBlue >= 300) {
      can_attaqueBlue = true;
      rect(width-350, 30, 10, 10); 
      if (attackBlue) {
        TimerFightBlue = 0;
        can_attaqueBlue = false;
      }
    }
  }
  //actions nécéssaire pour le MAZE
  void moveLeftMaze() {
    boolean move = true;
    for (int i = 0; i < 20; i++) {
      for (int j = 0; j < dirX; j++) {
        color leftColor = get((int(xpos)-j-1 ), (int(ypos)+i)); 
        if (leftColor == color(255)) {
          move = false;
        }
      }
    }
    if (move) xpos = xpos - dirX;
  }
  void moveRightMaze() {
    boolean move = true;
    for (int i = 0; i< 20; i++) {
      for (int j =0; j < dirX; j++) {
        color rightColor = get((int(xpos)+ 20+j+1), (int(ypos)+i)); 
        if (rightColor == color(255)) {
          move = false;
        }
      }
    }
    if (move) xpos = xpos + dirX;
  }
  void moveUpMaze() {
    boolean move = true;
    for (int i =0; i<20; i++) {
      for (int j = 0; j< dirY; j ++) {
        color upColor = get((int(xpos) + i), (int(ypos)-1-j)); 
        if (upColor == color(255)) {
          move = false;
        }
      }
    }
    if (move) ypos = ypos - dirY;
  }
  void moveDownMaze() {
    boolean move= true;
    for ( int i =0; i<20; i++) {
      for (int j = 0; j< dirY; j++) {
        color downColor = get(int(xpos)+i, (int(ypos)+20+1+j)); 
        if (downColor == color(255)) {
          move = false;
        }
      }
    }
    if (move) ypos = ypos + dirY;
  }
  ////////////////////////////////////////////////////////
  void displayMaze(int i, color c) {//display pour le MAZE 
    for (int x = 0; x < maze.width; x++) {
      for (int y = 0; y < maze.height; y++ ) {       
        int loc = x + y*maze.width;
        float r;
        r = red(maze.pixels[loc]);
        float maxdist = 50;
        float d = dist(x, y, players[i].xpos +10, players[i].ypos +10);
        float adjustbrightness = 255*(maxdist-d)/maxdist;
        r += adjustbrightness;
        r = constrain(r, 0, 255);
        color black = color(0);
        color b = color(r);
        if (b != black) pixels[y*width + x] = b;
      }
    }
    fill(c);
    rect(xpos, ypos, 20, 20);
  }  
  //display pour le DASH 
  void displayDash(color c) {
    fill(c); //variable de couleur qui pemret d'invoquer un joueur rouge et un joueur bleu
    rect(xpos, ypos, 20, 20);
  }
  //display pour le pour PONG
  void displayPong(color c) {
    fill(c);
    rect(xpos, ypos, 20, 70);
  }
  //display pour le FIGHT
  void displayFight(PImage img, float x, float  y ) {
    image(img, x, y);
    x = xpos;
    y = ypos;
  }
  void StayInWindow(int x, int y) { //arguements en fonction des jeux//
    //contrainte sur l'axe des X
    if (xpos+ x >= width)xpos = width-x;
    if (xpos <= 0)xpos = 0;
    //contrainte sur l'axe Y
    if (ypos + y >= height)ypos = height-y;
    if (ypos <= 0)ypos = 0;
  }
  //////---display des scores et des barres de vie & barre de vie-------/////
  void score(int score, int x, int y, color c) { //pour le pong
    textFont(f);
    fill(c);
    text(score, x, y);
  }
  void life(int Xscore, color c, int x) {//pour le fight, Barre de vie pour les deux joueurs
    stroke(gold);
    fill(c);
    rect(x, 50, Xscore, 30);
  }
  void energy(int Xenergy, int x) {
    stroke(gold);
    fill(gold);
    rect(x, 100, Xenergy, 10);
  }
}
