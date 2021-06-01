import flask
from flask.globals import request
import werkzeug
import time
import json

import cv2
import numpy as np

app = flask.Flask(__name__)

jsonProductName={}

#이미지 인식을 수행하는 메소드
def imgRecognition(timestr,filename):
    global jsonProductName
    net = cv2.dnn.readNet("yolo/yolov3_last.weights", "yolo/yolov3.cfg")
    classes = []
    with open("yolo/obj.names", "r") as f:
        classes = [line.strip() for line in f.readlines()]
    layer_names = net.getLayerNames()
    output_layers = [layer_names[i[0] - 1] for i in net.getUnconnectedOutLayers()]
    colors = np.random.uniform(0, 255, size=(len(classes), 3))

    #클라이언트로부터 받아와 저장한 이미지를 읽어와 제품 판별
    img=cv2.imread("./getImages/"+timestr+'_'+filename);
    
    img = cv2.resize(img, None, fx=0.4, fy=0.4)
    height, width, channels = img.shape

    #Object Detection
    blob = cv2.dnn.blobFromImage(img, 0.00392, (416, 416), (0, 0, 0), True, crop=False)
    net.setInput(blob)
    outs = net.forward(output_layers)

    #정보를 화면에 표시
    class_ids = []
    confidences = []
    boxes = []
    for out in outs:
        for detection in out:
            scores = detection[5:]
            class_id = np.argmax(scores)
            confidence = scores[class_id]
            if confidence > 0.5:
                #Object Detection
                center_x = int(detection[0] * width)
                center_y = int(detection[1] * height)
                w = int(detection[2] * width)
                h = int(detection[3] * height)
                #좌표
                x = int(center_x - w / 2)
                y = int(center_y - h / 2)
                boxes.append([x, y, w, h])
                confidences.append(float(confidence))
                class_ids.append(class_id)
                
    #박스 노이즈 제거
    indexes = cv2.dnn.NMSBoxes(boxes, confidences, 0.5, 0.4)

    #화면에 표시
    #글자 폰트
    font = cv2.FONT_HERSHEY_SIMPLEX
    nameNum=0;
    for i in range(len(boxes)):
        if i in indexes:
            x, y, w, h = boxes[i]
            label = str(classes[class_ids[i]])
            productNameindex='productName'+str(nameNum)
            nameNum=nameNum+1

            updateJson={productNameindex:label}

            jsonProductName.update(updateJson)

            color = colors[1]
            cv2.rectangle(img, (x, y), (x + w, y + h), color, 2)
            cv2.putText(img, label, (x, y + 15), font, 0.4, color, 1)
    
    if nameNum==0:
        productNameindex='productName'+str(nameNum)
        
        updateJson={productNameindex:"notFoundProduct"}

        jsonProductName.update(updateJson)

    #이미지 판별하여 박스 그린 후 저장하여 클라이언트에게 보내는 이미지로 사용
    cv2.imwrite("./sendImg/"+timestr+'_'+filename,img)
    
#클라이언트로부터 이미지 전송을 요청받고 반환하는 메소드
@app.route('/image', methods = ['GET', 'POST'])
def image_request():
    global jsonProductName
    jsonProductName={}
    timestr = time.strftime("%Y%m%d-%H%M%S")
    print('\n========================'+timestr+'===========================')
    imagefile = flask.request.files['image']

    filename = werkzeug.utils.secure_filename(imagefile.filename)
        
    imagefile.save("./getImages/"+timestr+'_'+filename) #타임스탬프와 함께 파일명 만들어서 저장
    imgRecognition(timestr,filename)
    
    return flask.send_file("./sendImg/"+timestr+'_'+filename,mimetype='image/jpg')

#클라이언트로부터 제품 이름 전송을 요청받고 반환하는 메소드
@app.route('/name', methods = ['GET'])
def name_request():
    global jsonProductName
    print('JSON File =',jsonProductName)

    return json.dumps(jsonProductName)

app.run(host="0.0.0.0", port=5000, debug=True)