#include <Encoder.h>

Encoder myEnc(7, 8);

int vitesse = 50;
void setup() {
   pinMode(9, OUTPUT);
   pinMode(10, OUTPUT);
   pinMode(11, OUTPUT);
   pinMode(12, OUTPUT);

   analogWriteFrequency(9, 16000);
   analogWriteFrequency(10, 16000);

   Serial.begin(9600);
   Serial.println("Basic Encoder Test:");
   // left
   analogWrite(9, vitesse);
   digitalWrite(11, HIGH);

   // right
   analogWrite(10, vitesse);
   digitalWrite(12, HIGH);
}

long oldPosition  = -999;

void loop() {
   /*  long newPosition = myEnc.read();
   if (newPosition != oldPosition) {
     oldPosition = newPosition;
     Serial.println(newPosition);

     double speed = (5000 - newPosition);
     Serial.print("p= ");
     Serial.print(newPosition);
     Serial.print(", speed= ");
     Serial.println(speed);

     if (speed > 0) {
       analogWrite(10, speed > 60 ? 60 : speed);
       digitalWrite(11, LOW);
     } else {
       analogWrite(10, -speed > 60 ? 60 : - speed);
       digitalWrite(11, HIGH);
     }*/

	if (Serial.available() > 0) {
		int incomingByte = Serial.parseInt();
		Serial.print("USB received: ");
		Serial.println(incomingByte);
               analogWrite(9, incomingByte);
	}

     // left

}
