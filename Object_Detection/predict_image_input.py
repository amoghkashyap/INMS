import os
import sys
import cv2
import time
import shutil
import math

sys.path.insert(1, "/home/akashyap/RetinaNet/")
#sys.path.insert(1, "/home/NNObjectDetect_RetinaNet/generated/")

import keras
from keras_retinanet.models.resnet import custom_objects 
from keras_retinanet.utils.image import read_image_bgr, preprocess_image, resize_image 
import numpy as np
import tensorflow as tf

image_url = sys.argv[1]

def get_session():
    config = tf.ConfigProto()
    config.gpu_options.allow_growth = True
    return tf.Session(config=config)

keras.backend.tensorflow_backend.set_session(get_session())
print("Loading weights and building model")

model_path = '/home/akashyap/ingredients.h5'


print("Loading Model")
model = keras.models.load_model(model_path, custom_objects=custom_objects)
print("Model Loaded")

labels_to_names = {0: "dont_know", 1: "bellpepper", 2: "carrot", 3: "egg", 4: "onion", 5: "tomato"}

print("Starting Detection")

start_time1 = time.time()
print("start time = %s" %(start_time1))

image = cv2.imread(image_url)    
print("detection")
##image = cv2.resize(image,(640,480))
draw = image.copy()
draw = cv2.cvtColor(draw, cv2.COLOR_BGR2RGB)
image = preprocess_image(image)
image, scale = resize_image(image)

_, _, detections = model.predict_on_batch(np.expand_dims(image, axis=0))
predicted_labels = np.argmax(detections[0, :, 4:], axis=1)
scores = detections[0, np.arange(detections.shape[1]), 4 + predicted_labels]
detections[0, :, :4] /= scale
car_boxes = []
for idx, (label, score) in enumerate(zip(predicted_labels, scores)):
    if score < 0.3:
        continue
    b = detections[0, idx, :4].astype(int)
    #cv2.rectangle(draw, (b[0], b[1]), (b[2], b[3]), (0, 0, 255), 3)
    caption = "{} {:.3f}".format(labels_to_names[label], score)
    object_class = labels_to_names[label]
    ##if object_class == class_to_detect:
    print( [b[0], b[1], b[2], b[3], object_class])
    car_boxes.append([b[0], b[1], b[2], b[3], score])
    cv2.rectangle(draw, (b[0], b[1]), (b[2], b[3]), (0, 0, 255), 3)

for t in car_boxes:
    cv2.rectangle(draw, (t[0], t[1]), (t[2], t[3]), (0, 0, 255), 2)
draw = cv2.cvtColor(draw, cv2.COLOR_RGB2BGR)
cv2.imwrite("./output.jpg", draw)

draw = cv2.cvtColor(draw, cv2.COLOR_RGB2BGR)

time_taken = time.time() - start_time1
print("Time taken to process = %s " %(time_taken))
