#include <Arduino.h>

#define BUZZER_OFF 0
#define BUZZER_ON 1

class Buzzer {
  private:
    int pin;
    byte state;

  public:
    Buzzer(int pin);
    void init();
    void on();
    void off();
    byte getState();
};
