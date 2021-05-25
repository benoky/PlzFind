import flask
import werkzeug
import time
import json

import cv2
import numpy as np

app = flask.Flask(__name__)

jsonProductName={}

#이미지 인식을 수행하는 메소드
def imgRecognition(timestr,filename):
    net = cv2.dnn.readNet("yolo/yolov3_last.weights", "yolo/yolov3.cfg")
    classes = []
    with open("yolo/obj.names", "r") as f:
        classes = [line.strip() for line in f.readlines()]
    layer_names = net.getLayerNames()
    output_layers = [layer_names[i[0] - 1] for i in net.getUnconnectedOutLayers()]
    colors = np.random.uniform(0, 255, size=(len(classes), 3))

    #이미지 불러오기
    #img = cv2.imread("C:/Programing/PlzFind/backend/findImages/20210523-150248_androidFlask.jpg")

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
    for i in range(len(boxes)):
        if i in indexes:
            x, y, w, h = boxes[i]
            label = str(classes[class_ids[i]])
            productNameindex='productName'+str(i-1)
            
            global jsonProductName
            updateJson={productNameindex:label}

            jsonProductName.update(updateJson)

            color = colors[1]
            cv2.rectangle(img, (x, y), (x + w, y + h), color, 2)
            cv2.putText(img, label, (x, y + 15), font, 0.4, color, 1)
    #cv2.imshow("Image", img)
    #이미지 판별하여 박스 그린 후 저장하여 클라이언트에게 보내는 이미지로 사용
    cv2.imwrite("./sendImg/"+timestr+'_'+filename,img)
    #cv2.waitKey(0)
    #cv2.destroyAllWindows()

@app.route('/image', methods = ['GET', 'POST'])
def image_request():
    print('asdasd')
    files_ids = list(flask.request.files)
    print("\nNumber of Received Images : ", len(files_ids))
    image_num = 1
    for file_id in files_ids:
        print("\nSaving Image ", str(image_num), "/", len(files_ids))
        imagefile = flask.request.files[file_id]
        
        filename = werkzeug.utils.secure_filename(imagefile.filename)
        print("Image Filename : " + imagefile.filename)
        timestr = time.strftime("%Y%m%d-%H%M%S")
        imagefile.save("./getImages/"+timestr+'_'+filename) #타임스탬프와 함께 파일명 만들어서 저장
        image_num = image_num + 1
        imgRecognition(timestr,filename)
    #post메소드의 응답으로 이미지를 반환해줌(현재 이미지는 임시 이미지 파일)
    #return flask.send_file('C:/Programing/PlzFind/backend/testreturnImg.jpg',mimetype='image/jpg')
    return flask.send_file("./sendImg/"+timestr+'_'+filename,mimetype='image/jpg')

@app.route('/name', methods = ['GET'])
def name_request():
    #jsonString={'productName0':'testG_pro','productName1':'testG_104','productName2':'testG_Hero'}
    global jsonProductName
    print('test2 =',jsonProductName)
    return json.dumps(jsonProductName)

app.run(host="0.0.0.0", port=5000, debug=True)

