import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ddf.minim.*; 
import ddf.minim.analysis.*; 
import ddf.minim.effects.*; 
import ddf.minim.signals.*; 
import ddf.minim.spi.*; 
import ddf.minim.ugens.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class ULTRAFIGHT extends PApplet {

//***************************************************************************//
//*************************///          ///  /////////////////---------------//
//*************************///          ///  /////////////////
//*************************///          ///  ///
//*************************///          ///  ///
//*************************///          ///  ///////////////
//*************************///          ///  ///////////////
//*************************///          ///  ///
//*************************///          ///  ///
//*************************///          ///  ///
//*************************////////////////  ///-----------------------------//
//***************************************************************************//
//thigs to do : 








//variable for switch windows
int run= 0;
//variable pour le background principale 
int xBg = 0;
//////---------------variables de couleurs pour le jeu--------------//
int red = color(237, 30, 36);
int blue = color(53, 71, 157);
int gold = color(208, 187, 98);
int purple = color(148, 40, 182);
//---------------variable for music---------------//
Minim minim;
AudioPlayer MENU;
AudioPlayer MAZE;
AudioPlayer PONG;
AudioPlayer DASH; 
AudioPlayer FIGHT; 
AudioPlayer VICTORY;
AudioPlayer TRUTH;
////-------------variable for fonts----------------//
PFont f;//moyenne typo
PFont f1;//petite
PFont f2;//tres grosse
PFont f3;// golden pixel company
//----------------images du jeu-------------//
PImage Menu;//MENU
PImage explication;
PImage maze;//MAZES
PImage fight;//FIGHT
PImage truth;//**_*___*
//-------------class for the game--------------------------//
//players red and blue
player[] players =  new player[2];
//obstacle for ULTRA DASH
obstacle[] obstacles = new obstacle[9];
//-------------variable de déplacement des joueurs--------------------------//
boolean _left, _right, _up, _down;//déplacement joueur[0] rouge
boolean _left2, _right2, _up2, _down2;//déplacement joueur[1] bleu
//specialement pour le jeu du FIGHT
boolean attackRed, attackBlue;
boolean _squatRed, _squatBlue;
//-------------booléène pour passer d'un mini-jeux à l'autre--------------------------//
boolean runRandom = false;

public void setup() {
  
  background(0);
  //--------------------------load music for all the game-----------------------//
  minim = new Minim( this);
  MENU = minim.loadFile("music/theme.mp3");
  MAZE = minim.loadFile("music/maze.mp3");
  PONG = minim.loadFile("music/pong.mp3");
  DASH = minim.loadFile("music/dash.mp3");
  FIGHT = minim.loadFile("music/fight.mp3");
  TRUTH = minim.loadFile("music/outro.mp3");
  VICTORY = minim.loadFile("music/victory.mp3");
  //--------------------------load fonts for all the game-----------------------//
  f=loadFont("fonts/RobotRadicals-48.vlw");//titre et écran d'aaccueil
  f1=loadFont("fonts/RobotRadicals-20.vlw");//pour ecrire certains passages
  f2=loadFont("fonts/RobotRadicals-60.vlw");//tresse grosse police pour decompte parfois
  f3=loadFont("fonts/SnellRoundhand-Bold-48.vlw");
  //--------------------------load images for the game--------------------------//
  Menu = loadImage("menu.jpg");//menu
  explication = loadImage("explication.png");//explications
  //maze = loadImage("maze/maze00"+int(random(14))+".JPG"); //maze
  truth = loadImage("truth.png");
  //loadPixels du labyrinth lorsque le labyrinth est joué
  loadPixels();
  fight = loadImage("fight/bgFIGHT.png"); //fond pour le fight
  //init timer for the fonts 
  timePast = millis();
  timeInterval = 1500.0f; //faire des println 
  //--------------------------init class for the game--------------------------//
  //player red and blue//
  for (int i=0; i<2; i++) {
    players[i] = new player(100+i*30, 100);
  }
  //obstacles for ULTRA DASH && parametrages pour les tailles//
  for (int i =0; i<9; i++) {
    obstacles[i]= new obstacle(width/3, height/3);
    obstacles[i].Xwidth = random(20, 200);
    obstacles[i].Yheight = random(20, 200);
    obstacles[i].directionX = random(-3, 4);
    obstacles[i].directionY = random(-3, 4);
  }
  obstacles[7].Xwidth = 20;
  obstacles[7].Yheight = 20;
  //vitesse joueur rouge et bleu dans le labyrinth
  //vitesse du joueur rouge//
  players[0].dirX = 3;//le personnage rouge est ralenti
  players[0].dirY = 3;
  //vitesse du joueur bleu
  players[1].dirX = 5;
  players[1].dirY = 5;
  //---------------------------------------------------------------------------//
}
public void draw() {
  fill(0);// mettre un rectangle plutot qu'un background pour le déplacer si besoin
  rect(xBg, 0, 858, 780);
  switch(run) {
  case 0:
    menu();
    music();
    if (keyPressed == true)run = 11; //fais passer à la fenetre d'explication
    keyPressed = false;//permet de ne pas passer directement au labyrinth dans le cas où les joueurs tappe sur ESAPCE  
    break;
  case 11 : 
    explication();  
    if (keyPressed) { //condition qui permet de passer au labyrinthe et de commencer le jeu.
      if (key ==' ') {
        run = 1; 
        RandomMaze = true; // permet de en pas avoir tout le temps le maze000 en premier//
      }
      keyPressed = false;
    }  
    break;
  case 1 : //premier jeu : Maze
    RandomMaze = true;
    maze();
    music();   
    break;
    ///SI FIGHT CAR COLLISION 
  case 2 : //si le  nombre 2 est tiré alors PONG
    music();
    pong();
    break;
  case 3 : //si le  nombre 3 est tiré alors DASH
    music();
    dash();
    break;
  case 4 : //si le  nombre 4 est tiré alors FIGHT
    music();
    fight();
    break;
  case 5 :
    victory();
    music();
    //-------PLAY AGAIN------------------///
    if (play_again) {     
      if (keyPressed && key ==' ') {  
        run = 0;
        goBackMaze = true;   
        AceRed = false;
        AceBlue = false;
        keyPressed = false; //pemret de revenir au menu sans retourner des les explications directement
        play_again = false;
        key0 = false;//clef du PONG
        key1 = false;//clef du DASH
        key2 = false;//clef du FIGHT
      }
    }
    break;
  case 6 : 
    music();
    Truth();
    break;
  case 7 : 
    credit();
    break;
    //--------------------------------------
  }
  ////////
  ///random mini jeux parmi les 3/////
  if (runRandom == true) { //permet de lancer qu'une seule fois la fonction randomGame//
    randomGame();
    runRandom = false;
  }
  /////random maze////
  if (RandomMaze) {
    randomMaze();
  }
  //appeler la dernière partie : VICTOIRE d'un des deux joueurs
  if (AceRed == true||AceBlue == true) run = 5;
  //appeler la fin alternative
  if (key0 ==true && key1 == true && key2 == true) {
    run = 6;
  }
  //reinitialiser les parties 
  if (goBackMaze)GoBacktoMaze();
}
public void randomGame() { //fonction qui permet de tirer au sort un ULTRAFIGHT : PONG/DASH/FIGHT
  run = PApplet.parseInt(random(2, 5));
  //run =3;
}
public void randomMaze() {
  RandomMazeImg = PApplet.parseInt(random(0, 14));
}
public void keyPressed() {
  //touche du clavier pour déplacement
  //joueur rouge
  if (key == 'z') _up = true; 
  if (key == 'q') _left = true;  
  if (key == 'd')_right = true;  
  if (key == 's') _down = true; 
  //joueur bleu
  if (key == 'o')_up2 = true; 
  if (key == 'k')_left2 = true;
  if (key == 'm')_right2 = true;
  if (key == 'l') _down2 = true;
  //---------------------------------//
  //////specialement pour le fight/////////
  if ( run == 4) {  
    if (key == 'z')players[0].can_jump = true;//jump joueur rouge   
    if (key == 'o')players[1].can_jump = true;//jump joueur bleu
    if (can_attaqueRed) {//cooldown pour attaquer
      if (key == 'f')attackRed = true;
    }
    if (can_attaqueBlue) {//cooldown pour attaquer
      if (key == 'j')attackBlue = true;
    }
    if (energyRed >= 1) {//acroupis pour le joueur rouge
      if (key == 's') _squatRed = true;
    } 
    if (energyBlue >= 1) {//acroupis pour le joueur bleu
      if (key == 'l') _squatBlue = true;
    }
  } 
  ///specialement pour le secret////
  if (key =='z')_upEnd = true;
  if (key=='o')_upEnd1 = true;
  //----------------------------------------------//
}
public void keyReleased() {
  //touche du clavier pour déplacement
  //joueur rouge
  if (key == 'z')_up = false;
  if (key == 's')_down = false;
  if (key == 'q')_left = false;
  if (key == 'd')_right = false;
  //joueur bleu
  if (key == 'o')_up2 = false;
  if (key == 'l')_down2 = false;
  if (key == 'k')_left2 = false;
  if (key == 'm')_right2 = false;
  //---------------------------------//
  //////specialement pour le fight/////////
  if ( run == 4) {
    if (key == 'f')attackRed = false;
    if (key == 'j')attackBlue = false;
    if (key == 's') _squatRed = false;
    if (key == 'l') _squatBlue = false ;
  }
  //specialement pour le secret//
  if (key =='z')_upEnd = false;
  if (key=='o')_upEnd1 = false;

  //-------------------------------------//
}
//fonction switch music//
public void music() {
  switch (run) {
  case 0 : 
    VICTORY.pause();
    VICTORY.rewind();
    MENU.play();
    break;
  case 1 : 
    MAZE.play();
    MENU.pause();
    MENU.rewind(); 
    DASH.pause();
    DASH.rewind();
    FIGHT.pause();
    FIGHT.rewind();
    PONG.pause();
    PONG.rewind();  
    break;
  case 2 : 
    MAZE.pause();
    MAZE.rewind();
    PONG.play();
    break;
  case 3 : 
    MAZE.pause();
    MAZE.rewind();
    DASH.play();
    break;
  case 4 : 
    MAZE.pause();
    MAZE.rewind();
    FIGHT.play();
    break;
  case 5 : 
    VICTORY.play();
    MAZE.pause();
    MAZE.rewind();
    DASH.pause();
    DASH.rewind();
    FIGHT.pause();
    FIGHT.rewind();
    PONG.pause();
    PONG.rewind();
    break;
  case 6: 
    TRUTH.play();
    DASH.pause();
    DASH.rewind();
    FIGHT.pause();
    FIGHT.rewind();
    PONG.pause();
    PONG.rewind();
    break;
  }
}
class obstacle {
  float X;
  float Y;
  float directionX, directionY;
  float Xwidth, Yheight;
  obstacle(float Mywidth, float Myheight) {
    X= Mywidth;
    Y= Myheight;
  }
  public void move() {
    X=X+directionX;
    Y=Y+directionY;
  }
  public void display() {
    fill(208, 187, 98); // COULEUR OR  
    rect(X, Y, Xwidth, Yheight); //parametres que l'on peut modifier dans le setup
    noStroke();
    if (Y-Yheight > height-Yheight*2 || Y < 0) {
      directionY = -directionY;
      directionY *= 1.07f;//gradient qui permet de faire accelrer les blocks
    } //rebond des obstacles en  Y
    if (X-Xwidth > width-Xwidth*2 || X < 0) { 
      directionX = -directionX;
      directionX *= 1.07f;
    }//rebond des obstacles en X
  }
}
public void dash() {
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

public void collide() { //fonction de collision entre joueur et obstcales//
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
float yposRect; //variable qui permet de faire la petite animation sur le text 
float xposExpli;

public void explication() {
  background(0);
  image(explication, 0, 0);
  textFont(f1);
  fill(208, 187, 98, textAlpha);
  textFade();
  text("Press space to skip", 590, 50);
  animation();
}
//petit animation qui fait apparaitre les explications
public void animation() {
  noStroke();
  fill(0);
  rect(0, yposRect, width, height);
  yposRect += 1;
}

public void explicationMze() { 
  textFont(f1);
  fill(gold);
  text("Blue player is faster, red can pass throught\n             the walls when blue is close.", 175, height/2.2f);
  //textFont(f2);
  //text("THE MAZE", width/2-155, height/2.4);
}

public void explicationDash() {
  textFont(f1);
  fill(gold);
  text("avoid the golden block to win !", 265, height/2.2f);
  //textFont(f2);
  //text("DASH", width/2-75, height/2.4);
}

public void explicationPong() {
  textFont(f1);
  fill(gold);
  text("You can smatch if you hit the ball\n    with the angle of your racket ", 255, height/2.2f); //explication du smatch special//
  //textFont(f2);
  //text("PONG", width/2-70, height/2.4);
}

public void explicationFight() {
  textFont(f1);
  fill(gold);
  text("you can crouch only if you have energy\n          You get it by hiting the enemy", 215, height/2.2f); //explication du fight s'accroupir et energy
}
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

public void fight () {
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
public void gameplay() {
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
    energyRed -= 0.25f;//perte d'energie lorsqu'accroupis
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
    energyBlue -= 0.25f;//perte d'energie lorsqu'accroupis
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
  if (players[0].can_jump)players[0].jump(0.32f);//arguement variable pour gérer le saut
  if (players[1].can_jump)players[1].jump(0.32f);
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

public void hitBox() {// fonction qui permet d'infliger des déguats à l'adversaire///
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
public void LifeEnergy() {
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

public void WinFight() {
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


public void Lines() { //fonction décorative//
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

public void maze() {
  chargement(2, 2);//chargement avec argument : vitesse = 2 et nombre de répétition = 3
  explicationMze(); //explication des regles du maze 
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
public void GoldenPixel() {
  fill (208, 187, 98);
  rect (Xpixel, Ypixel, 15, 15);
  noStroke();
  //collision avec le pixels d'or et permet aux joueur de le récuprer
  for (int i =0; i<2; i++) {
    if (players[i].xpos +20 >= Xpixel && 
      players[i].xpos <= Xpixel +15 && 
      players[i].ypos +20 >= Ypixel && 
      players[i].ypos <= Xpixel +15) {
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
public void winAce() {
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

public void GoldenCollide() { //Fonction qui s'active dans le cas ou le joueur bleu et rouge rentrent en collision apres avoir récupérer le pixel
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

public void GoBacktoMaze() { // fonction qui permet de reinitialiser les paramètres du jeu
  //réinitialisation des valeurs présentes dans le labyrinthe
  //vitesse dans le labyrinth//
  //vitesse du joueur rouge//
  players[0].dirX = 3;//le personnage rouge est ralenti
  players[0].dirY = 3;
  //vitesse du joueur bleu//
  players[1].dirX = 5;
  players[1].dirY = 5;
  RandomMaze = true; // permet de changer de Maze a chaque fois qu'on y retourne//
  GoldenRed = 0;
  GoldenBlue = 0;
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
  players[0].dirX = 3;//le personnage rouge est ralenti
  players[0].dirY = 3;
  //bleu 
  players[1].dirX = 5;
  players[1].dirY = 5;
  //reinit position pour tous les jeux 
  displayMazePlayers =true;
  displayPongPlayers = true;
  displayDashPlayers = true;
  displayFightPlayers = true;
  //reinit obstcales 
  for (int i = 0; i <9; i++) {
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

public void menu() {
    background(Menu);
    drawfont();
    Line();  
}
public void textFade () {
  if (millis() > timeInterval + timePast) {
    timePast = millis();
    textFade *= -1;
  }
  textAlpha += textFade;
}
public void drawfont () {
  textFade();
  textFont(f);
  fill(208, 187, 98, textAlpha);
  text ("PRESS ANY KEY TO START", 150, 700);
}
public void Line() {
  stroke(208, 187, 98);
  line(0, yLine, width, yLine);
  yLine++;
  if (yLine > height) {
    yLine = 0;
  }
}
//fonctions qui permet d'avoir un timer pour enchainer les jeux agréablement : sous forme d'un décompte//
//rectangle qui avance ----------> /// quand il franchit la fenetre -> go
public void Timer(int time) { //décompte avant les mini-jeux 
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
public void chargement(int time, int charg) {//chargement en cours : rajouter les commandes des mini-jeux 
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
public void chargementEnCours() {
  fill(gold, textAlpha);
  textFont(f);
  textFade();
  text("DOWNLOADING", Xchargement, 700);
}

public void displayPlayers() {//fonction -> position initiale des joueurs avant chaque mini jeux
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
  public void moveUp(int speed) { 
    ypos = ypos-speed;
  }
  public void moveDown(int speed) {
    ypos=ypos+speed;
  }
  //actions necéssaires pour le DASH//
  public void moveLeft() {
    xpos = xpos-5;
  }
  public void moveRight() {
    xpos= xpos+5;
  }
  public void jump(float ratio) {//uniquement pour le FIGHT
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
  public void canJump() {//permet de sauter fonction appeler dans jump//
    if (can_jump) {     
      vy = -15;//hauteur du bond//
      can_jump = false; //évite la possibilité de double jump voir triple... +1000 jump
    }
  }
  public void timerFight() { //cooldown pour attaquer//
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
  public void moveLeftMaze() {
    boolean move = true;
    for (int i = 0; i < 20; i++) {
      for (int j = 0; j < dirX; j++) {
        int leftColor = get((PApplet.parseInt(xpos)-j-1 ), (PApplet.parseInt(ypos)+i)); 
        if (leftColor == color(255)) {
          move = false;
        }
      }
    }
    if (move) xpos = xpos - dirX;
  }
  public void moveRightMaze() {
    boolean move = true;
    for (int i = 0; i< 20; i++) {
      for (int j =0; j < dirX; j++) {
        int rightColor = get((PApplet.parseInt(xpos)+ 20+j+1), (PApplet.parseInt(ypos)+i)); 
        if (rightColor == color(255)) {
          move = false;
        }
      }
    }
    if (move) xpos = xpos + dirX;
  }
  public void moveUpMaze() {
    boolean move = true;
    for (int i =0; i<20; i++) {
      for (int j = 0; j< dirY; j ++) {
        int upColor = get((PApplet.parseInt(xpos) + i), (PApplet.parseInt(ypos)-1-j)); 
        if (upColor == color(255)) {
          move = false;
        }
      }
    }
    if (move) ypos = ypos - dirY;
  }
  public void moveDownMaze() {
    boolean move= true;
    for ( int i =0; i<20; i++) {
      for (int j = 0; j< dirY; j++) {
        int downColor = get(PApplet.parseInt(xpos)+i, (PApplet.parseInt(ypos)+20+1+j)); 
        if (downColor == color(255)) {
          move = false;
        }
      }
    }
    if (move) ypos = ypos + dirY;
  }
  ////////////////////////////////////////////////////////
  public void displayMaze(int i, int c) {//display pour le MAZE 
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
        int black = color(0);
        int b = color(r);
        if (b != black) pixels[y*width + x] = b;
      }
    }
    fill(c);
    rect(xpos, ypos, 20, 20);
  }  
  //display pour le DASH 
  public void displayDash(int c) {
    fill(c); //variable de couleur qui pemret d'invoquer un joueur rouge et un joueur bleu
    rect(xpos, ypos, 20, 20);
  }
  //display pour le pour PONG
  public void displayPong(int c) {
    fill(c);
    rect(xpos, ypos, 20, 70);
  }
  //display pour le FIGHT
  public void displayFight(PImage img, float x, float  y ) {
    image(img, x, y);
    x = xpos;
    y = ypos;
  }
  public void StayInWindow(int x, int y) { //arguements en fonction des jeux//
    //contrainte sur l'axe des X
    if (xpos+ x >= width)xpos = width-x;
    if (xpos <= 0)xpos = 0;
    //contrainte sur l'axe Y
    if (ypos + y >= height)ypos = height-y;
    if (ypos <= 0)ypos = 0;
  }
  //////---display des scores et des barres de vie & barre de vie-------/////
  public void score(int score, int x, int y, int c) { //pour le pong
    textFont(f);
    fill(c);
    text(score, x, y);
  }
  public void life(int Xscore, int c, int x) {//pour le fight, Barre de vie pour les deux joueurs
    stroke(gold);
    fill(c);
    rect(x, 50, Xscore, 30);
  }
  public void energy(int Xenergy, int x) {
    stroke(gold);
    fill(gold);
    rect(x, 100, Xenergy, 10);
  }
}
float xposBall = 400;
float yposBall = 400; //variable de position de la balle
float directionXball = 4;
float directionYball = 1;
//score des joueurs
int scoreR = 0;
int scoreB = 0;

public void pong() {
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
public void balldisplay() {
  //apparence de la balle
  noStroke();
  fill (208, 187, 98);
  rect(xposBall, yposBall, 20, 20); ////
  ////fonctions de comportement de la balle////
  bounce();
  BallAgain();
}
public void ballMove() {
  xposBall = xposBall + directionXball;
  yposBall = yposBall + directionYball;
  if (yposBall > height-20 || yposBall < 0) directionYball = -directionYball;
}

public void bounce() {
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
public void BallAgain() { //fonction qui fait en sorte que la balle revienne au milieu constamment apres un but d'un joueur
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
public void accel() {
  directionXball= directionXball*1.1f;
}

public void winPong() {
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


public void Truth() {
  strokeWeight(3); //contour de la fenetre de chargement en GOLD
  stroke(gold);
  chargement(2, 2);
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
        int varX = PApplet.parseInt(random (width));
        int varY = PApplet.parseInt(random (height)); 
        fill(truth.pixels[varY*width + varX]);  
        strokeWeight(0.1f);
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

public void PositionPlayers() {
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
public void moveUpPurple() {//faire bouger le pixel violet à deux//
  yposPurple  = yposPurple - 0.15f;
}
public void credit() { //crédit de fin du jeu 
  fill(0);
  rect(xposRectEnd, 0, 860, 780);
  xposRectEnd += 10;
  if (xposRectEnd >= 0) {
    xposRectEnd = 0;
    textFont(f1);
    fill(gold);
    text("music & developing\nby Hugo Bijaoui", width/2 -100, height/2);
    text("Pour Raph", 700, 730);
  }
}
//variables pour podium
int Ypodium = height+400;//1er
int Ypodium2 = height+400 ;//2ème
//boolean play again
boolean play_again = false;


public void victory() {
  background(0);
  //permet d'invoquer les podiums///
  podium1();
  podium2();
  //---------------------------------
  //-----contiditions pour  gagner la partie-----
  if (AceRed == true ) {
    fill(red);
    textFont(f2);
    ///display joueurs///
    //joueur rouge///
    text("RED PLAYER WINS", width/2-250, height/2);
    players[0].displayFight(loadImage("fight/red_d.png"), 225, Ypodium + 582);
    //joueur bleu 
    players[1].displayFight(loadImage("fight/blue_d.png"), 510, Ypodium2 + 624);
  }
  if (AceBlue == true) {
    fill(blue);
    textFont(f2);
    text("BLUE PLAYER WINS", width/2-250, height/2);
    ///display joueurs///
    //joueur bleu///
    players[1].displayFight(loadImage("fight/blue_g.png"), 230, Ypodium + 624);
    //joueur rouge///
    players[0].displayFight(loadImage("fight/red_g.png"), 510, Ypodium2 + 582);
  }
  //--------------------------------------
}
public void podium1() {//pour le deuxieme
  fill(gold);
  rect(190, height, 200, Ypodium);
  Ypodium -= 1; 
  if (Ypodium <= -560) {
    Ypodium = -560;
    drawfontVictory();
    play_again = true; //bool for play again//
  }
}
public void podium2() {//pour le deuxieme
  fill(gold);
  rect(470, height, 200, Ypodium2);
  Ypodium2 -= 1; 
  //println(Ypodium);
  if (Ypodium2 <= -500) {
    Ypodium2 = -500;   
  }
}
public void drawfontVictory () {
  textFade();
  textFont(f);
  fill(gold, textAlpha);
  text ("Press SPACE\nto play again", 450, 50);
}
  public void settings() {  size(860, 780); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--hide-stop", "ULTRAFIGHT" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
