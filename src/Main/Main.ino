#include <Encoder.h>

Encoder leftEncoder(5, 6);
Encoder rightEncoder(7, 8);

IntervalTimer timer;

void setup() {
    pinMode(9, OUTPUT);
    pinMode(10, OUTPUT);
    pinMode(11, OUTPUT);
    pinMode(12, OUTPUT);

    analogWriteFrequency(9, 16000);
    analogWriteFrequency(10, 16000);

    Serial.begin(115200);

    analogWrite(9, 0);
    digitalWrite(11, LOW);
    analogWrite(10, 0);
    digitalWrite(12, LOW);

    timer.begin(refresh, 1000);
}

int32_t dist_right, dist_left, cmd_right, cmd_left, target_dist, target_angle;

void loop() {
    while (Serial.available() == 0) {
        ;
    }

    long int i;
    unsigned char premierCaractere = Serial.read();
    switch (premierCaractere) {
        case 'a':
            litEntierLong(&i);
            target_dist = i;
            Serial.println(target_dist);
            break;
        case 'b':
            litEntierLong(&i);
            target_angle = i;
            Serial.println(target_angle);
            break;
        default:
            break;
    }
}

void refresh() {
    int32_t dist, cmd_dist, angle, cmd_angle, err_dist, err_angle;

    dist_right = leftEncoder.read();
    dist_left = rightEncoder.read();

    dist = (dist_left + dist_right) / 2;
    angle = dist_right - dist_left;

    err_dist = target_dist - dist;
    err_angle = target_angle - angle;

    cmd_dist = maximize(err_dist, 50);
    cmd_angle = maximize(err_angle, 50);

    cmd_right = cmd_dist + cmd_angle;
    cmd_left = cmd_dist - cmd_angle;

    if (cmd_right > 0) {
        analogWrite(9, cmd_right);
        digitalWrite(11, LOW);
    } else {
        analogWrite(9, -cmd_right);
        digitalWrite(11, HIGH);
    }

    if (cmd_left > 0) {
        analogWrite(10, cmd_left);
        digitalWrite(12, HIGH);
    } else {
        analogWrite(10, -cmd_left);
        digitalWrite(12, LOW);
    }
}

int32_t maximize(int32_t value, int32_t max)
{
    if(max == -1) return value;

    if (abs(value) > max) {
        if(value > 0) {
            return max;
        } else {
            return -max;
        }
    }
    return value;
}

void litEntierLong(long int *i)
{
    long int aux = 0;
    unsigned char j;
    long int k = 10000000;
    unsigned char c = 0; // les unsigned feront l'affaire ! -- Yann Sionneau
    for (j = 0; j < 8; j++) {
        while (Serial.available()==0) { 
            asm("nop");
        }
        c = Serial.read();
        if (c < 48 || c > 57) {
            aux = -1;
            break;
        }
        aux += (c - 48) * k;
        k /= 10;
    } 
    *i = aux;
}
