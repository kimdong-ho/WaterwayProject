#include "Buzzer.h"

Buzzer::Buzzer(int pin) {
  this->pin = pin;
  init();
}

void Buzzer::init() {
  pinMode(pin, OUTPUT);
  off();
  state = BUZZER_OFF;
}
void Buzzer::on() {
  for(int i=700;i<800;i++){
    tone(pin,i);
    delay(15);
  }
  
  for(int i=800;i>700;i--){
    tone(pin,i);
    delay(15);
  } 
  state = BUZZER_ON;
}
void Buzzer::off() {
  noTone(pin);
  state = BUZZER_OFF;
}

byte Buzzer::getState() {
  return state;
}
