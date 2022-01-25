float yposRect; //variable qui permet de faire la petite animation sur le text 

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
