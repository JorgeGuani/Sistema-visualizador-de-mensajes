/*
 * Librería libre para conexión entre java y arduino
 * https://github.com/PanamaHitek/PanamaHitek_Arduino/releases/tag/3.0.0
 */
//Librerías usadas
#include <LiquidCrystal.h> //LCD
#include "DHT.h"           //Sensor de humedad

//Pines de alimentación del LCD
#define RS 3
#define E  5
#define D4 6
#define D5 9
#define D6 10
#define D7 11

//Variables para el sensor de humedad
#define DHTPIN 2          //Entrada digital 2
#define DHTTYPE DHT11     //Tipo de sensor DHT
DHT dht(DHTPIN, DHTTYPE); //Comunicación con el sensor
float valorHumedad = 0;

//Variables para el sensor de temperatura
#define sensortemperatura 0  //Entrada analógica 0
float voltaje = 0;
float valorTemperatura = 0;

//Variables para el sensor de luminosidad
const long A = 1000;      //Resistencia en oscuridad en KΩ
const int B = 15;         //Resistencia a la luz (10 Lux) en KΩ
const int Rc = 10;        //Resistencia calibracion en KΩ
const int PINLUZ = A1;    //Entrada analógica 1
int valorAnalogicoLuz = 0;
int valorLuminosidad = 0;

//Variables para el control de las entradas seriales
int input;
boolean esClima;

//Indicarle al LCD sus pines de alimentación
LiquidCrystal lcd(RS,E,D4,D5,D6,D7);


void setup() {
  lcd.begin(16,1);
  dht.begin();
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
      //lcd.begin(16,1);
      lcd.setCursor(16,0);
      while(Serial.available() > 0) {
        lcd.autoscroll();
        lcd.write(Serial.read());  //Escribe el mensaje en el LCD
        delay(275);
      }
    }  
  }
}


//MÉTODOS PARA CALCULAR ESTADOS CLIMATOLÓGICOS:
void mostrarTemperatura() {
  lcd.noAutoscroll();
  while(input == '1'){
    voltaje = analogRead(sensortemperatura)*3.3/1023;
    valorTemperatura = voltaje*100;
    lcd.home();
    lcd.print("Temperatura: ");
    lcd.print((int)valorTemperatura);
    lcd.print("C");
    lcd.print("  ");
    Serial.println(valorTemperatura);
    delay(1000);
    if(Serial.read() != 10) break; //Si se detecta un cambio en la entrada serial      
  }
}

void mostrarHumedad() {
  lcd.noAutoscroll();
  while(input == '2'){
    valorHumedad = dht.readHumidity(); //Se lee la humedad
    lcd.home();
    lcd.print("Humedad: ");
    lcd.print(valorHumedad);
    lcd.print("  ");
    Serial.println("Humedad: ");
    Serial.println(valorHumedad);
    delay(1000);
    if(Serial.read() != 10) break; //Si se detecta un cambio en la entrada serial      
  }
}

void mostrarLuminosidad() {
  lcd.noAutoscroll();
  while(input == '3'){
    valorAnalogicoLuz = analogRead(PINLUZ);
    valorLuminosidad = ((long)valorAnalogicoLuz*A*10)/
                  ((long)B*Rc*(1024-valorAnalogicoLuz));
    lcd.home();
    lcd.print("luminosidad: ");
    lcd.print(valorLuminosidad);
    lcd.print("  ");
    delay(1000);
    if(Serial.read() != 10) break;
  }
}
