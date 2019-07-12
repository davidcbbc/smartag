# Smartag
https://raw.githubusercontent.com/espressif/arduino-esp32/master/README.md
https://github.com/espressif/arduino-esp32/blob/master/README.md




#Smart Tag - Criação do dispositivo

###O objetivo deste projeto consiste na criação de um dispositivo interligado que possibilite o agendamento de reuniões, numa determinada sala, acedendo a uma determinada conta do Google Calendar. Este dispositivo acede ao calendário e verifica se algum evento se encontra marcado para a hora em que se quer reservar a sala retornando a sua disponibilidade, ou não disponibilidade.



##Material utilizado:
- Board ESP32 LORA:
+ Bluetooth
+ Wifi
+ Lora 868 MHz
+ Dual core
+ 512KB ram
+ 4MB Flash
- E-ink Display
+ Trim-Color display (white, red, black)
+ 1,56 inches
- BadgerBoard V2.1
- Botão



##Ferramentas utilizadas:
- Java
- IntelliJ idea
- The Things Network [https://www.thethingsnetwork.org/]
- Arduino [https://www.arduino.cc/]



##Conteúdo:
- [Ligações] (#ligacoes)
- [Configuração conta e dispositivo no The Things Network] (#ttn)
- [Configuração da API] (#api)



##Ligações

| ESP32 | BadgerBoard | E-ink Display | BOTÃO |
| --- | --- | --- | --- |
| D4 | — | BUSY | — |
| --- | --- | --- | --- |
| RX2 | — | RESET | — |
| --- | --- | --- | --- |
| D12 | — | RESET | — |
| --- | --- | --- | --- |
| D5 | — | C5 | — |
| --- | --- | --- | --- |
| D18 | — | CLK | — |
| --- | --- | --- | --- |
| D23 | — | SDI | — |
| --- | --- | --- | --- |
| GND | — | GND | — |
| --- | --- | --- | --- |
| 3.3V | — | VCC | — |
| --- | --- | --- | --- |
| GND | — | — | GND |
| --- | --- | --- | --- |
| 3.3V | — | — | VIN |
| --- | --- | --- | --- |
| 14 | — | — | VRY |
| --- | --- | --- | --- |
| D12 | — | — | SW |
| --- | --- | --- | --- |
| D21 | D8 | — | — |
| --- | --- | --- | --- |
| D19 | D9 | — | —|

NOTA: 
- O VRX do botão não é necessário conectar.
- Fotos do circuito podem ser vistas na pasta IMAGENS



##Configuração conta e dispositivo no The Things Network
Para criar a conta e conectar o dispositivo, seguir os passos apresentados no seguinte link: https://www.thethingsnetwork.org/docs/devices/node/quick-start.html até ao ponto ‘The Things Npde example’.
Nota: na secção Connect your Device, ponto 4, utilizar ‘Board > LilyPad Arduino USB’.



##Configuração da API





