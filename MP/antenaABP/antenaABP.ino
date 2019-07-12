#include <TheThingsNetwork.h>

// Set your DevAddr, NwkSKey, AppSKey and the frequency plan
const char *devAddr = "260110FE";
const char *nwkSKey = "C8AF2C2604392300A544EC468559385F";
const char *appSKey = "342AF3E8AE8CAE4781516698E4C21620";

#define loraSerial Serial1
#define debugSerial Serial


// Replace REPLACE_ME with TTN_FP_EU868 or TTN_FP_US915
#define freqPlan TTN_FP_EU868
int count = 0;

TheThingsNetwork ttn(loraSerial, debugSerial, freqPlan);

void setup()
{
  loraSerial.begin(57600);
  debugSerial.begin(9600);

  // Wait a maximum of 10s for Serial Monitor
  while (!debugSerial && millis() < 10000)
    ;

  pinMode(8, INPUT);            // recebe pedidos de marcaçao de reuniao
  pinMode(9, OUTPUT);           //escreve tensao para o controlador
  pinMode(10, INPUT);           // recebe pedidos de refresh

  ttn.onMessage(message);

  debugSerial.println("-- PERSONALIZE");
  ttn.personalize(devAddr, nwkSKey, appSKey);

  debugSerial.println("-- STATUS");
  ttn.showStatus();
}

void loop()
{

  //debugSerial.println("-- LOOP");


  if (count < 50000) {
    int valor_refres = digitalRead(10);
    int valor_botao = digitalRead(8);
    //Serial.println(valor_botao);
    if (valor_botao == HIGH  || valor_refres == HIGH) {
      //BOTAO PRIMIDO
      if (valor_botao == HIGH) {
        //marcar reuniao
        valor_botao = LOW;
        Serial.println("botao de marcacao foi premido ....");
        byte payload;
        payload = 1;
        ttn.sendBytes(payload, sizeof(payload), 4);
      } else {
        //refresh
        valor_refres = LOW;
        Serial.println("botao de refresh foi premido ....");
        byte payload;
        payload = 1;
        ttn.sendBytes(payload, sizeof(payload), 5);
      }

    }
  } else {
    ttn.poll();
    count = 0;
  }

  count++;


  // Send single byte to poll for incoming messages

  /*debugSerial.println("-- LOOP");

    // Prepare payload of 1 byte to indicate LED status
    byte payload[1];
    payload[0] = (digitalRead(LED_BUILTIN) == HIGH) ? 1 : 0;

    // Send it off
    ttn.sendBytes(payload, sizeof(payload));

    delay(10000);*/
}


void message(const uint8_t *payload, size_t size, port_t port)
{
  debugSerial.println("-- MESSAGE");
  debugSerial.print("Received " + String(size) + " bytes on port " + String(port) + ":");

  
  if (port == 1) digitalWrite(9, LOW);
  else digitalWrite(9, HIGH);

  for (int i = 0; i < size; i++)
  {
    debugSerial.print(" " + String(payload[i]));
  }

  debugSerial.println();
}
