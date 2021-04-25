from flask import Flask, render_template, redirect, url_for,request
from werkzeug import secure_filename

app = Flask(__name__)

@app.route('/upload')
def renderHtml():
    return render_template(upload.html)

#파일 업로드
@app.route('/fileUpload',methods=['GET','POST']) #GET = 특정 url을 요청, POST = 값을 넘겨주며 url을 요청
def uploadFile():
    if request.method=='POST':
        file=request.files['file']
        #저장할 경로 및 파일 명
        file.save('./uploads/'+secure_filename(file.filename))
        return 'uploads 완료'

#서버 실행 부
if __name__ == '__main__':
   app.run(debug=True)
