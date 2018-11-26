from __future__ import print_function

import grpc
import camera_pb2
import camera_pb2_grpc

def run():
    with grpc.insecure_channel('localhost:17001') as channel:
        stub = camera_pb2_grpc.ImageDetectionStub(channel)
        res = stub.SendUrl(camera_pb2.UrlRequest(image_url='http://10.142.138.195:8000/capture.jpg'))
        print("Response from GPU "+res.response)


if __name__ == '__main__':
    run()

