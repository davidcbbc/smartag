#include <TheThingsNetwork.h>

// Set your AppEUI and AppKey
const char *appEui = "70B3D57ED001F216";
const char *appKey = "2055A7B0ACBC10482EEB14E571C87F07";

#define loraSerial Serial1
#define debugSerial Serial

// Replace REPLACE_ME with TTN_FP_EU868 or TTN_FP_US915
#define freqPlan TTN_FP_EU868

TheThingsNetwork ttn(loraSerial, debugSerial, freqPlan);


int sec = 0;        //variavel de tempo para controlar os polls e sincrozinar com o botao
void setup()
{
  loraSerial.begin(57600);
  debugSerial.begin(9600);
  pinMode(8,INPUT_PULLUP);

  // Wait a maximum of 10s for Serial Monitor
  while (!debugSerial && millis() < 10000)
    ;

  
  

  // Set callback for incoming messages
  ttn.onMessage(message);

  debugSerial.println("-- STATUS");
  //ttn.showStatus();

  debugSerial.println("-- JOIN");
  ttn.join(appEui, appKey);
}

void loop()
{
  if(sec != 100) {
    //botao
  int valor_botao = digitalRead(8);
  if(valor_botao == 0 ) {
    //BOTAO PRIMIDO
    Serial.println("BOTAO PRIMIDO");
    //ttn.sendBytes(0x01, 1);
    byte positivo  = 1;
    ttn.sendBytes(positivo,sizeof(positivo),4);
    
  }
    sec++;
  } else {
    //sending poll request
    Serial.println("SENDING POLL REQUEST ....");
    ttn.poll();
    sec = 0;
  }
  
  delay(100);
  //debugSerial.println("-- LOOP");

  // Send single byte to poll for incoming messages
  //ttn.poll();
  
  //delay(10000);
}

void message(const uint8_t *payload, size_t size, port_t port)
{
  debugSerial.println("-- MESSAGE");
  debugSerial.print("Received " + String(size) + " bytes on port " + String(port) + ":");

  for (int i = 0; i < size; i++)
  {
    debugSerial.print(" " + String(payload[i]));
  }

  debugSerial.println();
}
