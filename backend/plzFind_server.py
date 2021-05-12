import flask
import werkzeug
import time
import json

app = flask.Flask(__name__)


@app.route('/image', methods = ['GET', 'POST'])
def image_request():
    files_ids = list(flask.request.files)
    print("\nNumber of Received Images : ", len(files_ids))
    image_num = 1
    for file_id in files_ids:
        print("\nSaving Image ", str(image_num), "/", len(files_ids))
        imagefile = flask.request.files[file_id]
        filename = werkzeug.utils.secure_filename(imagefile.filename)
        print("Image Filename : " + imagefile.filename)
        timestr = time.strftime("%Y%m%d-%H%M%S")
        imagefile.save("./findImages/"+timestr+'_'+filename)
        image_num = image_num + 1
    print("\n")
    
    #post메소드의 응답으로 이미지를 반환해줌(현재 이미지는 임시 이미지 파일)
    return flask.send_file('C:/Programing/PlzFind/backend/testreturnImg.jpg',mimetype='image/jpg')

@app.route('/name', methods = ['GET'])
def name_request():
    jsonString={'productName0':'testG_pro','productName1':'testG_104','productName2':'testG_Hero'}

    
    
    return json.dumps(jsonString)

app.run(host="0.0.0.0", port=5000, debug=True)