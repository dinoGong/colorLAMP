String inString="";
int r=0;
int g=0;
int b=0;
int pin_r=10;
int pin_g=11;
int pin_b=12;
boolean run=false;
void setup(){
  Serial.begin(115200);
  pinMode(pin_r, OUTPUT);
  pinMode(pin_g, OUTPUT);
  pinMode(pin_b, OUTPUT);
  
}
void loop(){
  while(Serial.available()){
    char inChar = Serial.read();
    inString += inChar;
    
    if (inChar == '@') {
      if(inString=="gpiopi@"){
        run=true;
      }
      inString = ""; 
    }
    if (inChar == '.') {
      if(g==0 && r!=0){
        g=inString.toInt();
      }
      if(r==0 && g==0){
        r=inString.toInt();
      }
      
      inString = ""; 
    }
    if (inChar == '\n' ) {

      // clear the string for new input:
      if(b==0){
        b=inString.toInt();
      }
      Serial.write(r);
      Serial.write(g);
      Serial.write(b);
      Serial.println(r);
      Serial.println(g);
      Serial.println(b);
      
      if(run){
         analogWrite(pin_r,r);
         analogWrite(pin_g,g);
         analogWrite(pin_b,b);
      }
      
     
      run=false;
      inString = ""; 
      r=0;
      g=0;
      b=0;
    }
 }
}
