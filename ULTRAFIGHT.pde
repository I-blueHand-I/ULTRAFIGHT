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
//debugger labyrinth rouge qui traverse les murs
//details : blur effect sur le pong //musique sur les différents jeux
//changer le labyrinth quand goBacktoMaze//
//fin secrète//

import ddf.minim.*;
import ddf.minim.analysis.*;
import ddf.minim.effects.*;
import ddf.minim.signals.*;
import ddf.minim.spi.*;
import ddf.minim.ugens.*;

//variable for switch windows
int run= 0;
//variable pour le background principale 
int xBg = 0;
//////---------------variables de couleurs pour le jeu--------------//
color red = color(237, 30, 36);
color blue = color(53, 71, 157);
color gold = color(208, 187, 98);
color purple = color(148, 40, 182);
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

void setup() {
  size(860, 780);
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
    obstacles[i].directionX = random(-3, 6);
    obstacles[i].directionY = random(-3, 6);
  }
  obstacles[7].Xwidth = 20;
  obstacles[7].Yheight = 20;
  //---------------------------------------------------------------------------//
}
void draw() {
  fill(0);// mettre un rectangle plutot qu'un background pour le déplacer si besoin
  rect(xBg, 0, 860, 780);
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
      if (keyPressed) {  
        if (key ==' ')run = 0;
        goBackMaze = true;   
        AceRed = false;
        AceBlue = false;
        keyPressed = false; //pemret de revenir au menu sans retourner des les explications directement
        play_again = false;
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
void randomGame() { //fonction qui permet de tirer au sort un ULTRAFIGHT : PONG/DASH/FIGHT
  run = int(random(2, 5));
}
void randomMaze() {
  RandomMazeImg = int(random(0, 14));
}
void keyPressed() {
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
  //----------------------------------------------//
}
void keyReleased() {
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
  //-------------------------------------//
}
//fonction switch music//
void music() {
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
