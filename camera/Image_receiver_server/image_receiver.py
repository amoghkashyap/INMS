import os
from flask import request, url_for
from flask_api import FlaskAPI
from flask import jsonify
from flask import json
from flask import Response
from flask import send_from_directory, send_file
from flask_cors import CORS

app = FlaskAPI(__name__)
CORS(app)

@app.route('/receiveImages', methods=['POST'])
def receiveImages():
    print('file url received')
    if request is None:
        print("request is none")
    else:
        print(request.method)

    if request.method == 'POST':
        print("method is post")
        print(request.files)
        if request.files is None:
            print("request.files are none")
        else:
            print("requestfileitems are:")
            receivedFiles = request.files['upload']
            receivedFiles.save(os.path.join("/home/akashyap/INMS/Object_Detection/",receivedFiles.filename))

        print("Saved successfully")
        resp = Response('saved successfully', status=200, mimetype='application/json')
        return resp
    else:
        print("method is not post")
        return {'status': 'noPost'}

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=50053)
