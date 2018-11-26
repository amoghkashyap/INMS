from __future__ import print_function

import grpc
import camera_pb2
import camera_pb2_grpc
import RPi.GPIO as GPIO
import os
import time

GPIO.setwarnings(False)
GPIO.setmode(GPIO.BOARD)
GPIO.setup(11,GPIO.IN)

def run():
    with grpc.insecure_channel('35.221.238.173:50051') as channel:
        stub = camera_pb2_grpc.ImageDetectionStub(channel)
        res = stub.SendUrl(camera_pb2.UrlRequest(image_url='Trigger for Detection'))
        print("Response from GPU "+res.response)


def capture():
    returned_value = os.system("raspistill -o /home/pi/Desktop/capture.jpg")
    print("returned output from capture ",returned_value)


if __name__ == '__main__':
    while True:
        input=GPIO.input(11)
        if input==1:
        	print("Image capture in progress")
        	capture()
        	print("Capture completed")
        	os.system("curl -v -F upload@'/home/pi/Desktop/capture.jpg' http://35.221.238.173:50053/receiveImages")
        	print("Image sent to GPU")
            run()
            time.sleep(5)

         elif input==0:
         	print("Refrigerator closed")