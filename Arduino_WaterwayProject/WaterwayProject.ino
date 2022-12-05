// weight sensor1 관련
#include <DFRobot_HX711.h>
DFRobot_HX711 MyScale(A2, A3);

// weight sensor2 관련
#include "HX711.h" //HX711로드셀 엠프 관련함수 호출
#define calibration_factor -7050.0 // 로드셀 스케일 값 선언
#define DOUT 3 //엠프 데이터 아웃 핀 넘버 선언
#define CLK 2  //엠프 클락 핀 넘버 
HX711 scale(DOUT, CLK); //엠프 핀 선언

// Led 관련
#define LED_1_PIN 5
#include "Led.h"
Led led1(LED_1_PIN);

// Buzzer 관련
#define BUZZER_1_PIN 4  
#include "Buzzer.h"
Buzzer buzzer1(BUZZER_1_PIN);


// mkr wifi 관련
#include <ArduinoBearSSL.h>
#include <ArduinoECCX08.h>
#include <ArduinoMqttClient.h>
#include <WiFiNINA.h> // change to #include <WiFi101.h> for MKR1000

#include "arduino_secrets.h"
#include <ArduinoJson.h>

/////// Enter your sensitive data in arduino_secrets.h
const char ssid[]        = SECRET_SSID;
const char pass[]        = SECRET_PASS;
const char broker[]      = SECRET_BROKER;
const char* certificate  = SECRET_CERTIFICATE;

WiFiClient    wifiClient;            // Used for the TCP socket connection
BearSSLClient sslClient(wifiClient); // Used for SSL/TLS connection, integrates with ECC508
MqttClient    mqttClient(sslClient);

unsigned long lastMillis = 0;


void setup() {
  Serial.begin(9600);

  //weight sensor scale 설정&지정
  scale.set_scale(calibration_factor);  
  scale.tare();

  while (!Serial);

  if (!ECCX08.begin()) {
    Serial.println("No ECCX08 present!");
    while (1);
  }

  // Set a callback to get the current time
  // used to validate the servers certificate
  ArduinoBearSSL.onGetTime(getTime);

  // Set the ECCX08 slot to use for the private key
  // and the accompanying public certificate for it
  sslClient.setEccSlot(0, certificate);

  // Optional, set the client id used for MQTT,
  // each device that is connected to the broker
  // must have a unique client id. The MQTTClient will generate
  // a client id for you based on the millis() value if not set

  // mqttClient.setId("clientId");

  // Set the message callback, this function is
  // called when the MQTTClient receives a message
  mqttClient.onMessage(onMessageReceived);
}

void loop() {
  // wifi 연결
  if (WiFi.status() != WL_CONNECTED) {
    connectWiFi();
  }

  // mqtt 연결
  if (!mqttClient.connected()) {
    // MQTT client is disconnected, connect
    connectMQTT();
  }

  // poll for new MQTT messages and send keep alives
  mqttClient.poll();

  // publish a message roughly every 5 seconds.
  if (millis() - lastMillis > 5000) {
    lastMillis = millis();
    char payload[512];
    getDeviceStatus(payload);
    sendMessage(payload);
  }
}


unsigned long getTime() {
  // get the current time from the WiFi module  
  return WiFi.getTime();
}

//wifi 연결 
void connectWiFi() {
  Serial.print("Attempting to connect to SSID: ");
  Serial.print(ssid);
  Serial.print(" ");

  while (WiFi.begin(ssid, pass) != WL_CONNECTED) {
    // failed, retry
    Serial.print(".");
    delay(5000);
  }
  Serial.println();

  Serial.println("You're connected to the network");
  Serial.println();
}

// mqtt 연결
void connectMQTT() {
  Serial.print("Attempting to MQTT broker: ");
  Serial.print(broker);
  Serial.println(" ");

  while (!mqttClient.connect(broker, 8883)) {
    // failed, retry
    Serial.print(".");
    delay(5000);
  }
  Serial.println();

  Serial.println("You're connected to the MQTT broker");
  Serial.println();

  // subscribe to a topic
  // 자신의 aws 사물 이름으로 알맞게 변경 필수
  mqttClient.subscribe("$aws/things/WaterwayProject/shadow/update/delta");
}

// 사물의 상태
void getDeviceStatus(char* payload) {
  /*
   * t1 : weight sensor 1
   * t2 : weight sensor 2
   * led : led
   * buzzer : buzzer
   */
  
  float t1 = MyScale.readWeight();
  float t2 = scale.get_units(); 

  // Read led status
  const char* led = (led1.getState() == LED_ON)? "ON" : "OFF";

  const char* buzzer = (buzzer1.getState() == BUZZER_ON)? "ON" : "OFF";

  if(MyScale.readWeight() < 1.0 ){
    float t1 = MyScale.readWeight();
    float t2 = scale.get_units(); 
    t1 *= -1.0;
    
    if(scale.get_units()<0.0){
      t2 *= -1.0;
    }
    // make payload for the device update topic ($aws/things/WaterwayProject/shadow/update)
    sprintf(payload,"{\"state\":{\"reported\":{\"weight1\":\"%0.2f\",\"weight2\":\"%0.2f\",\"LED\":\"%s\",\"BUZZER\":\"%s\"}}}",t1,t2,led,buzzer);
  }
  
  else{
    float t1 = MyScale.readWeight();
    float t2 = scale.get_units();
    if(scale.get_units()<0.0){
       t2 *= -1.0;
    }
    // make payload for the device update topic ($aws/things/WaterwayProject/shadow/update)
     sprintf(payload,"{\"state\":{\"reported\":{\"weight1\":\"%0.2f\",\"weight2\":\"%0.2f\",\"LED\":\"%s\",\"BUZZER\":\"%s\"}}}",t1,t2,led,buzzer);
  }
}


void sendMessage(char* payload) {
  // 자신의 aws 사물 이름으로 알맞게 변경 필수
  char TOPIC_NAME[]= "$aws/things/WaterwayProject/shadow/update";
  
  Serial.print("Publishing send message:");
  Serial.println(payload);
  mqttClient.beginMessage(TOPIC_NAME);
  mqttClient.print(payload);
  mqttClient.endMessage();
}

void onMessageReceived(int messageSize) {
  // we received a message, print out the topic and contents
  Serial.print("Received a message with topic '");
  Serial.print(mqttClient.messageTopic());
  Serial.print("', length ");
  Serial.print(messageSize);
  Serial.println(" bytes:");

  // store the message received to the buffer
  char buffer[512] ;
  int count=0;
  while (mqttClient.available()) {
     buffer[count++] = (char)mqttClient.read();
  }
  buffer[count]='\0'; // 버퍼의 마지막에 null 캐릭터 삽입
  Serial.println(buffer);
  Serial.println();


  DynamicJsonDocument doc(1024);
  deserializeJson(doc, buffer);
  JsonObject root = doc.as<JsonObject>();
  JsonObject state = root["state"];
  
  const char* led = state["LED"];
  Serial.println(led);
  
  const char* buzzer = state["BUZZER"];
  Serial.println(buzzer);
  
  char payload[512];
  
  // make payload for the device update topic ($aws/things/WaterwayProject/shadow/update)

  /*
   * LED와 BUZEER를 하나의 제어로 하게 했음.
   * LED가 ON -> BUZZER도 ON
   * LED가 OFF -> BUZZER도 OFF
   */
 if (strcmp(led,"ON")==0 /*&& strcmp(buzzer,"ON")==0*/) {
    led1.on();
    buzzer1.on();
    sprintf(payload,"{\"state\":{\"reported\":{\"LED\":\"%s\",\"BUZZER\":\"%s\"}}}","ON","ON");
    sendMessage(payload);
    
  } else if (strcmp(led,"OFF")==0 /*&& strcmp(buzzer,"OFF")==0*/) {
    led1.off();
    buzzer1.off();
    sprintf(payload,"{\"state\":{\"reported\":{\"LED\":\"%s\",\"BUZZER\":\"%s\"}}}","OFF","OFF");
    sendMessage(payload);
  }
}
