from __future__ import print_function

import grpc

import inms_pb2
import inms_pb2_grpc

classes_test = []

def run(classes = [],*args):
    # NOTE(gRPC Python Team): .close() is possible on a channel and should be
    # used in circumstances in which the with statement does not fit the needs
    # of the code.
    with grpc.insecure_channel('10.142.149.124:50051') as channel:
        stub = inms_pb2_grpc.DetectionStub(channel)
        response = stub.UpdateDetectedIngredients(inms_pb2.DetectedIngredientsRequest(container_id='testing',ingredients = classes ))
    print("Greeter client received: " + response.status)


if __name__ == '__main__':
    classes_test.append("testing_ingredients")
    run(classes_test)
