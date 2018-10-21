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

#define sensortemperatura 0
float voltaje = 0;
float valorTemperatura = 0;

const long A = 1000; //Resistencia en oscuridad en KΩ
const int B = 15; //Resistencia a la luz (10 Lux) en KΩ
const int Rc = 10; //Resistencia calibracion en KΩ
const int PINLUZ = A1;
int valorLuzAnalogica;
int iluminacion;

int input;
boolean esClima;

LiquidCrystal lcd(RS,E,D4,D5,D6,D7);

void setup() {
  lcd.begin(16,2);
  Serial.begin(9600);
}


void loop() {
  //Analizadores del clima
  if(input == '1') {
    mostrarTemperatura();  
  } else if(input == '2') {
    mostrarHumedad();
  } else if(input == '3') {
    mostrarLuminosidad();
  }

  if(Serial.available()) {
    delay(100);
    input = Serial.read();
    esClima = false;
    lcd.clear();

    //Checa si la entrada corresponde a mostrar algún estado climatológico
    if(Serial.available() > 0) {
      if(input == '1' || input == '2' || input == '3')
      esClima = true;        
    }

    //En caso de que la entrada no sea un estado climatológico,
    //Quiere decir que corresponde a un mensaje:
    if(!esClima) {               
      while(Serial.available() > 0) {
        lcd.write(Serial.read());  
      }
    }  
  }
}


//MÉTODOS PARA CALCULAR ESTADOS CLIMATOLÓGICOS:
void mostrarTemperatura() {
  while(input == '1'){
    voltaje = analogRead(sensortemperatura)*3.3/1023;
    valorTemperatura = voltaje*100;
    lcd.home();
    lcd.print("Temperatura: ");
    lcd.setCursor(0,1);
    lcd.print(valorTemperatura);
    lcd.print("C");
    Serial.println(valorTemperatura);
    delay(1000);
    //nuevaEntrada();
    //input = Serial.read();
    //Serial.println(input);
    if(Serial.read() != 10) break;
        
  }
}

void mostrarHumedad() {
  while(input == '2'){
    lcd.home();
    lcd.write("hum");
    nuevaEntrada();
  }
}

void mostrarLuminosidad() {
  while(input == '3'){
    valorLuzAnalogica = analogRead(PINLUZ);
    iluminacion = ((long)valorLuzAnalogica*A*10)/
                  ((long)B*Rc*(1024-valorLuzAnalogica));
    lcd.clear();
    lcd.home();
    lcd.print("luminosidad: ");
    lcd.setCursor(0,1);
    lcd.print(iluminacion);
    lcd.print("  ");
    delay(1000);
    if(Serial.read() != 10) break;
  }
}

//Sirve para detectar si se ingresa una nueva entrada
void nuevaEntrada() {
  if(Serial.available() > 0) {
    input = Serial.read();
    Serial.println(input);    
  }
}
