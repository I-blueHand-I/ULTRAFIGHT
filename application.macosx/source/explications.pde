float yposRect; //variable qui permet de faire la petite animation sur le text 
float xposExpli;

void explication() {
  background(0);
  image(explication, 0, 0);
  textFont(f1);
  fill(208, 187, 98, textAlpha);
  textFade();
  text("Press space to skip", 590, 50);
  animation();
}
//petit animation qui fait apparaitre les explications
void animation() {
  noStroke();
  fill(0);
  rect(0, yposRect, width, height);
  yposRect += 1;
}

void explicationMze() { 
  textFont(f1);
  fill(gold);
  text("Blue player is faster, red can walkthrought\n             the walls when blue is close.", 175, height/2.2);
  //textFont(f2);
  //text("THE MAZE", width/2-155, height/2.4);
}

void explicationDash() {
  textFont(f1);
  fill(gold);
  text("avoid the golden block to win !", 265, height/2.2);
  //textFont(f2);
  //text("DASH", width/2-75, height/2.4);
}

void explicationPong() {
  textFont(f1);
  fill(gold);
  text("You can smatch if you hit the ball\n    with the angle of your racket ", 255, height/2.2); //explication du smatch special//
  //textFont(f2);
  //text("PONG", width/2-70, height/2.4);
}

void explicationFight() {
  textFont(f1);
  fill(gold);
  text("you can crouch only if you have energy\n          You get it by hiting the enemy", 215, height/2.2); //explication du fight s'accroupir et energy
}
