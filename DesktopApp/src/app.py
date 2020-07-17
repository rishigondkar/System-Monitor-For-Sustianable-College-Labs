from flask_cors import CORS, cross_origin
from flask import *
import json

app=Flask(__name__)
CORS(app)
app.config['CORS_HEADERS'] = 'Content-Type'


@app.route('/')
def my_form():
    return render_template('admin.html')

@app.route('/', methods=['POST'])
def my_form_post():
    data = request.get_data()
    print(data)
    return render_template('admin.html')

if __name__== '__main__':
    app.run(debug=True)
