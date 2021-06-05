//Predefine Bluetooth
#include "BluetoothSerial.h"

//NewPing for ultrasonic
#include <NewPing.h>

#define SONAR_NUM 2
#define MAX_DISTANCE_SENSORS 500
#define HIGH_MIC 3000
#define LOW_MIC 1000
#define MED_MIC 2000

#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error Bluetooth is not enabled! Please run 'make menuconfig' to and enable it
#endif

BluetoothSerial SerialBT;

//Parameters
const int micPin = A0;
const int cancelButton = 16;
const int emergencyButton = 17;
const int redLedPin = 21;
const int greenLenPin = 22;
const int buzzerPin = 27;
const int echoPin1 = 14;
const int trigPin1 = 15;
const int echoPin2 = 12;
const int trigPin2 = 32;
const int PIRSensorPin1 = 13;
const int PIRSensorPin2 = 23;

//Threshold
int SAFETY_THRESHOLD = 100; //=30 SECONDS
int MIC_THRESHOLD = 1100;
int FIRST_DANGER_THRESHOLD = 10; //=30 SECONDS

//People Counter
bool state1 = 1;
bool state2 = 0;
bool state3 = 0;
bool state4 = 0;
int currentPeople = 0;

//ultrasonic sensor
int sensor1Initial = 0;
int sensor2Initial = 0;

//NewPing declaration
NewPing sonar[SONAR_NUM] = {  
  NewPing(15, 14, MAX_DISTANCE_SENSORS),
  NewPing(32, 12, MAX_DISTANCE_SENSORS)
};

//Buzzer Features
int freq = 2000;
int channel = 0;
int resolution = 8;

//Variables
bool emergencyFlag = 0;
int micVal = 0;
bool peopleCounterFlag = 0;
int safeTimer = 0;
int dangerTimer = 0;
int EMERGENCY_CONTACT = NULL;
int MADA = 101;

//Functions
void receiveBT();
void sendBTFirstDanger();
void receiveBTFirstDanger();
void readMicrophone();
void PIRSensor();
void peopleCounter();
void firstDangerAlert();
void secondDangerAlert();
void buzzer();
void variableEditor(int safetyThreshold, int micThreshold, int firstDangerThreshold); //also receive emergency contact

void setup(){

    //Init serial USB
    Serial.begin(115200);

    //Init Bluetooth
    SerialBT.begin("ESP32test");

    //Init Mic
    pinMode(micPin,INPUT);

    //Init Buttons
    pinMode(cancelButton,INPUT_PULLUP);
    pinMode(emergencyButton,INPUT_PULLUP);

    //Init led
    pinMode(redLedPin,OUTPUT);
    pinMode(greenLenPin,OUTPUT);

    //Init PIRsensor
    pinMode(PIRSensorPin1,INPUT);
    pinMode(PIRSensorPin2,INPUT);

    //Init sensor Initial Ultrasonics
    delay(29);
    sensor1Initial = sonar[0].ping_cm();
    delay(29);
    sensor2Initial = sonar[1].ping_cm();
    
    //Init buzzer
    ledcSetup(channel,freq,resolution);
    ledcAttachPin(buzzerPin,channel);
    delay(500);

}

void loop(){
    receiveBT();
    digitalWrite(redLedPin,LOW);
    peopleCounter();
    Serial.print("current People: ");
    Serial.println(currentPeople);
    while(currentPeople){
        receiveBT();
        digitalWrite(redLedPin,LOW);
        digitalWrite(greenLenPin,HIGH);
//        readMicrophone();
        PIRSensor();
        if (!digitalRead(emergencyButton)){
            firstDangerAlert();
        }
        peopleCounter();
        Serial.print("current People");
        Serial.println(currentPeople);
    }
    safeTimer = 0;
    digitalWrite(greenLenPin,LOW);
}

void peopleCounter(){
    delay(29);
    int sensor1Val = sonar[0].ping_cm();
    delay(29);
    int sensor2Val = sonar[1].ping_cm();
    int sensor1On = (sensor1Val < (sensor1Initial - 5));
    int sensor2On = (sensor2Val < (sensor2Initial - 5));

    //No one was passing
    if (state1){
        if((sensor1On && sensor2On) || (!sensor1On && !sensor2On)) { //Invalid or no change
            return;
        }
        if(sensor1On && !sensor2On){ //Entering
            state1 = 0;
            state2 = 1;
            return;
        }
        if(!sensor1On && sensor2On){ //Exiting
            state1 = 0;
            state3 = 1;
            return;
        }
    }
     //People entering
    if (state2){
        if(sensor1On && !sensor2On) { //no change
            return;
        }
        if(!sensor1On && !sensor2On){ //Invalid or no actual entrance
            state2 = 0;
            state1 = 1;
            return;
        }
        if(sensor1On && sensor2On){ //Entering fully
            state2 = 0;
            state4 = 1;
            currentPeople++;
            delay(500);
            return;
        }
        if(!sensor1On && sensor2On){ //Weird, but misssed both on - person entering
            currentPeople++;
            state2 = 0;
            state4 = 1;
            delay(500);
            return;
        }
    }
    //Person exiting
    if (state3){
        if(!sensor1On && sensor2On) { //no change
            return;
        }
        if(!sensor1On && !sensor2On){ //Invalid or no actual entrance
            state3 = 0;
            state1 = 1;
            return;
        }
        if(sensor1On && sensor2On){ //Entering fully
            if(currentPeople){
                currentPeople--;
            }
            state3 = 0;
            state4 = 1;
            delay(500);
            return;
        }
        if(sensor1On && !sensor2On){ //Weird, but misssed both on - person exiting
            if(currentPeople){
                currentPeople--;
            }
            state3 = 0;
            state4 = 1;
            delay(500);
            return;
        }
    }
    if(state4){
        if(!sensor1On && !sensor2On){ //Readjusted completly
            state4 = 0;
            state1 = 1;
            return;
        } //Else stuck on state 4 until readjusted
    }
    
}

void PIRSensor(){
    Serial.println(safeTimer);
    Serial.println(digitalRead(PIRSensorPin1));
    Serial.println(digitalRead(PIRSensorPin2));
    ++safeTimer;
    if(safeTimer > SAFETY_THRESHOLD){
        safeTimer = 0;
        firstDangerAlert();
    }
    if(digitalRead(PIRSensorPin1) == HIGH || digitalRead(PIRSensorPin2) == HIGH){
        safeTimer = 0;
    }
    delay(2);
}

void readMicrophone(){
    micVal = analogRead(micPin);
    Serial.println(micVal);
    if(micVal > MIC_THRESHOLD){
        firstDangerAlert();
    }
}

void firstDangerAlert(){
  emergencyFlag = 1;
  sendBTFirstDanger();
  Serial.println("FIRST DANGER");
  dangerTimer = 0;
  digitalWrite(redLedPin,HIGH);
  while(dangerTimer<FIRST_DANGER_THRESHOLD && emergencyFlag){
      buzzer();
      receiveBTFirstDanger();
      Serial.println(emergencyFlag);
      peopleCounter();
      if(!digitalRead(cancelButton)){
          emergencyFlag = 0;
          sendBTFirstDanger();
          digitalWrite(redLedPin,LOW);
      }
      delay(2);
      peopleCounter();
      dangerTimer++;
    }
    if (emergencyFlag){
        secondDangerAlert();
    }
}

void secondDangerAlert(){
    Serial.println("SECOND DANGER");
    currentPeople = 0;
    //SerialBT.println("EMERGENCY");
    //Call MDA, call family
    while (digitalRead(cancelButton)){
       buzzer(); 
    }
    digitalWrite(redLedPin,LOW);
}

void buzzer(){
    ledcWriteTone(channel, 2000);
    delay(500);
    ledcWriteTone(channel, 0);
    delay(498);
}

void receiveBT(){
  if(SerialBT.available()){
    
    String message = SerialBT.readString();
    Serial.print(message);
    int safetyThreshold = message.substring(0, 2).toInt();
    int micThreshold = message.substring(2,3).toInt();
    int firstDangerThreshold = message.substring(3, 5).toInt();
    //int emergencyContact = message.substring(5, 15).toInt();
    variableEditor(safetyThreshold, micThreshold, firstDangerThreshold); //also receive emergency contact
    delay(20);
  }
}

void receiveBTFirstDanger(){
  if(SerialBT.available()){
    SerialBT.readString();
    emergencyFlag = 0;
  }
}

void sendBTFirstDanger(){
  SerialBT.write((emergencyFlag) ? '1' : '0');
}

void variableEditor(int safetyThreshold, int micThreshold, int firstDangerThreshold){ //also receive emergency contact
    SAFETY_THRESHOLD = safetyThreshold * (10);
    switch(micThreshold){
      case 0:
        MIC_THRESHOLD = LOW_MIC;
        break;
      case 1:
        MIC_THRESHOLD = MED_MIC;
        break;
      case 2:
        MIC_THRESHOLD = HIGH_MIC;
        break;
    }
    FIRST_DANGER_THRESHOLD = firstDangerThreshold;
    Serial.println(MIC_THRESHOLD);
    Serial.println(SAFETY_THRESHOLD);
    Serial.println(FIRST_DANGER_THRESHOLD);
//    EMERGENCY_CONTACT = emergencyContact;
}