/*
 * Librería libre para conexión entre java y arduino
 * https://github.com/PanamaHitek/PanamaHitek_Arduino/releases/tag/3.0.0
 */

#include <LiquidCrystal.h>
#define RS 3
#define E  5
#define D4 6
#define D5 9
#define D6 10
#define D7 11

LiquidCrystal lcd(RS,E,D4,D5,D6,D7);

void setup() {
  lcd.begin(16,2);
  
  Serial.begin(9600);
}

void loop() {
  //lcd.autoscroll();
  if(Serial.available()) {
    delay(100);
    lcd.clear();
    //lcd.home();
    while(Serial.available() > 0) {
      lcd.write(Serial.read());
    }
  }
}
