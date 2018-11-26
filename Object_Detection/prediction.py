from concurrent import futures

import grpc
import time
import camera_pb2
import camera_pb2_grpc
import inms_pb2
import inms_pb2_grpc
import os
import sys
import cv2
import shutil
import math

sys.path.insert(1, "/home/amogh/RetinaNet/")

import keras
from keras_retinanet.models.resnet import custom_objects 
from keras_retinanet.utils.image import read_image_bgr, preprocess_image, resize_image 
import numpy as np
import tensorflow as tf

_ONE_DAY_IN_SECONDS = 60 * 60 * 24

class ImageDetection(camera_pb2_grpc.ImageDetectionServicer):
   
    def SendUrl(self, request, context):
        print ("Trigger received")

        def get_session():
            config = tf.ConfigProto()
            config.gpu_options.allow_growth = True
            return tf.Session(config=config)

        def run(classes_detected=[],*args):
            with grpc.insecure_channel('35.221.238.173:50051') as channel:
                stub = inms_pb2_grpc.DetectionStub(channel)
                response = stub.UpdateDetectedIngredients(inms_pb2.DetectedIngredientsRequest(container_id="inms",ingredients=classes_detected))
            print("Response from Backend ",response)

        keras.backend.tensorflow_backend.set_session(get_session())
        print("Loading weights and building model")
        
        model_path = '/home/amogh/ingredients.h5'

        print("Loading Model")
        model = keras.models.load_model(model_path, custom_objects=custom_objects)
        print("Model Loaded")

        labels_to_names = {0: "dont_know", 1: "bellpepper", 2: "carrot", 3: "egg", 4: "onion", 5: "tomato"}

        print("Starting Detection")
        start_time1 = time.time()
        print("start time = %s" %(start_time1))

        image = cv2.imread("capture.jpg")
        print("detection")
        draw = image.copy()
        draw = cv2.cvtColor(draw, cv2.COLOR_BGR2RGB)
        image = preprocess_image(image)
        image, scale = resize_image(image)

        _, _, detections = model.predict_on_batch(np.expand_dims(image, axis=0))
        predicted_labels = np.argmax(detections[0, :, 4:], axis=1)
        scores = detections[0, np.arange(detections.shape[1]), 4 + predicted_labels]
        detections[0, :, :4] /= scale
        ing_boxes = []
        ingredients_names = []
        for idx, (label, score) in enumerate(zip(predicted_labels, scores)):
            if score < 0.3:
                continue
            b = detections[0, idx, :4].astype(int)
            caption = "{} {:.3f}".format(labels_to_names[label], score)
            object_class = labels_to_names[label]
            print( [b[0], b[1], b[2], b[3], object_class])
            ingredients_names.append(object_class)
            ing_boxes.append([b[0], b[1], b[2], b[3], score])
            cv2.rectangle(draw, (b[0], b[1]), (b[2], b[3]), (0, 0, 255), 3)
        for t in ing_boxes:
            cv2.rectangle(draw, (t[0], t[1]), (t[2], t[3]), (0, 0, 255), 2)
            draw = cv2.cvtColor(draw, cv2.COLOR_RGB2BGR)
            cv2.imwrite("./output.jpg", draw)
            draw = cv2.cvtColor(draw, cv2.COLOR_RGB2BGR)

            time_taken = time.time() - start_time1
            print("Time taken to process = %s " %(time_taken))
        os.system("rm -rf capture.jpg")
        
        run(ingredients_names)

        return camera_pb2.UrlResponse(response=' %s '% request.image_url)


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    camera_pb2_grpc.add_ImageDetectionServicer_to_server(ImageDetection(), server)
    server.add_insecure_port('[::]:17005')
    server.start()
    try:
        while True:
            time.sleep(_ONE_DAY_IN_SECONDS)
    except KeyboardInterrupt:
        server.stop(0)


if __name__ == '__main__':
    print ("Server started")
    serve()
