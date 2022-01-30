//variables pour podium
int Ypodium = height+400;//1er
int Ypodium2 = height+400 ;//2ème
//boolean play again
boolean play_again = false;


void victory() {
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
void podium1() {//pour le deuxieme
  fill(gold);
  rect(190, height, 200, Ypodium);
  Ypodium -= 1; 
  if (Ypodium <= -560) {
    Ypodium = -560;
    drawfontVictory();
    play_again = true; //bool for play again//
  }
}
void podium2() {//pour le deuxieme
  fill(gold);
  rect(470, height, 200, Ypodium2);
  Ypodium2 -= 1; 
  //println(Ypodium);
  if (Ypodium2 <= -500) {
    Ypodium2 = -500;   
  }
}
void drawfontVictory () {
  textFade();
  textFont(f);
  fill(gold, textAlpha);
  text ("Press SPACE\nto play again", 450, 50);
}
