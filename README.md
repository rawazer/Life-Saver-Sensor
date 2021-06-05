# Life-Saver-Sensor

This repo contains two folders:

1. ESP32 Code: This is the code that was installed inside the CPU of the physical device.
The code is stand-alone - it is a loop for the sensors which works at the clock frequency of our choice.

2. App Code: This is the code for the external app to control the device.
The app can be used for changing the device settings, such as microphone sensitivity, emergency alarm duration, no-movement duration, etc.
The app is also in communication with the device in case of an emergency - it receives an alert of an ongoing alarm, and can be turned off from the device.
The connection works with Bluetooth (was meant for showcase of ability during the Hackathon), but the desired ability is using a database.

You are welcome to look around!
