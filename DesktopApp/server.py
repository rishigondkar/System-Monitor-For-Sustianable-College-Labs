#Server script
from flask_cors import CORS, cross_origin
from flask import *
import json
import os
import csv
from datetime import datetime
import calendar

app=Flask(_name_)
CORS(app)
app.config['CORS_HEADERS'] = 'Content-Type'


@app.route('/')
def my_form():
    return "HELLO"
    return render_template('')

@app.route('/', methods=['POST'])
def my_form_post():

    data = request.get_data()
    data_split = data.split()
    #os.chdir('path of admin folder') #Path of admin folder
    if('Admin_'+ str(month) not in os.listdir()):
      os.mkdir('Admin')
    os.chdir('Admin')
    if(data_split[0] not in os.listdir()):
      os.mkdir(data_split[0])
    os.chdir(data_split[0])
    if(data_split[1] not in os.listdir()):
      os.mkdir(data_split[1])
    os.chdir(data_split[1])
    if(data_split[4] not in os.listdir()):
      os.mkdir(data_split[4])
    os.chdir(data_split[4])
    with open(data_split[2] + '.csv', 'a') as csvFile:
      writer = csv.writer(csvFile)
      writer.writerow([data_split[3]])
    %cd ..
    %cd .. 
    %cd ..
    %cd ..

    return "FUCK YOU"
    #return render_template('./src/firstRun.html')
from datetime import datetime
import calendar
import os
import csv
month = datetime.today().strftime("%m")
month1 = datetime.now().month
year = datetime.today().strftime("%Y")
num_days = calendar.monthrange(int(year), month1)[1]
os.mkdir('Admin_' + str(month))
os.chdir('Admin_' + str(month))
timestamp = ['00:00:00']
for i in range(1,5):
  os.mkdir('lab'+str(i))
  os.chdir('lab'+str(i))
  for j in range(1,5):
    os.mkdir('pc'+str(j))
    os.chdir('pc'+str(j))
    os.mkdir('n')
    os.chdir('n')
    for k in range(0,num_days):
      if(k<9):
        with open('0'+str(k)+'-'+str(month)+'-'+str(year)+'.csv', 'w') as csvFile:
          writer = csv.writer(csvFile)
          writer.writerow(timestamp)
      else:
        with open(str(k)+'-'+str(month)+'-'+str(year)+'.csv', 'w') as csvFile:
          writer = csv.writer(csvFile)
          writer.writerow(timestamp)
    %cd ..
    os.mkdir('i')
    os.chdir('i')
    for k in range(0,num_days):
      if(k<9):
        with open('0'+str(k)+'-'+str(month)+'-'+str(year)+'.csv', 'w') as csvFile:
          writer = csv.writer(csvFile)
          writer.writerow(timestamp)
      else:
        with open(str(k)+'-'+str(month)+'-'+str(year)+'.csv', 'w') as csvFile:
          writer = csv.writer(csvFile)
          writer.writerow(timestamp)
    %cd ..
    %cd ..
  %cd ..
%cd ..


import numpy as np
import pandas as pd
from datetime import datetime
import calendar
import os
import csv
month = datetime.today().strftime("%m")
os.chdir('Admin_' + str(month)) #path of admin folder
folder_names = os.listdir()
labs = []
labs_idle = []
for folder_name in folder_names:
  os.chdir(folder_name)
  folder_pcs = os.listdir()
  time_all_lab_pc = []
  time_all_lab_pc_idle = []
  for folder_pc in folder_pcs:
    os.chdir(folder_pc)
    filenames = os.listdir()
    time_pc = []
    time_pc_idle = []

    os.chdir('n')
    filenames = os.listdir()
    for filename in filenames:
      dataset = pd.read_csv(filename, header = None)
      time = list(dataset[0])
      time_hour = []
      for i in range(0,np.shape(time)[0]):
        var = time[i].split(':')
        time_hour.append(var[0])#Hour wise data for that pc
      time_combined = np.zeros(24, dtype=int)
      for j in range(0, np.shape(time_hour)[0]):
        time_combined[int(time_hour[j])] = time_combined[int(time_hour[j])] + 1 #Array of 24 for that pc for that date
      time_pc.append(time_combined) #Array of 24 for that pc for all dates
    %cd ..

    os.chdir('i')
    filenames = os.listdir()
    for filename in filenames:
      dataset = pd.read_csv(filename, header = None)
      time = dataset[0].tolist()
      time_hour_idle = []
      for i in range(0,np.shape(time)[0]):
        var = time[i].split(':')
        time_hour_idle.append(var[0])#Hour wise data for that pc
      time_combined_idle = np.zeros(24, dtype=int)
      for j in range(0, np.shape(time_hour_idle)[0]):
        time_combined_idle[int(time_hour_idle[j])] = time_combined_idle[int(time_hour_idle[j])] + 1 #Array of 24 for that pc for that date
      time_pc_idle.append(time_combined_idle) #Array of 24 for that pc for all dates
    %cd ..

    time_all_lab_pc.append(time_pc) #Array of 24 for all pc for all dates
    time_all_lab_pc_idle.append(time_pc_idle)
    %cd ..
  %cd ..
  month = []
  for k in range(0,np.shape(time_pc)[0]):
    combo = np.zeros(24, dtype=int)
    for l in range(0,np.shape(time_all_lab_pc)[0]):
      for m in range(0,24):
        combo[m] = combo[m] + time_all_lab_pc[l][k][m]
    month.append(combo)
  labs.append(month)

  month_idle = []
  for k in range(0,np.shape(time_pc_idle)[0]):
    combo_idle = np.zeros(24, dtype=int)
    for l in range(0,np.shape(time_all_lab_pc_idle)[0]):
      for m in range(0,24):
        combo[m] = combo[m] + time_all_lab_pc_idle[l][k][m]
    month_idle.append(combo_idle)
  labs_idle.append(month_idle)
%cd ..

total_use = np.sum(labs)
total_idle_time = np.sum(labs_idle)

total_cost_usage = (total_use * 400) / 60
wastage = (total_idle_time * 0.15 * 400 * 5) / 60 
co2_emission = 0.3 * total_cost_usage / 1000


#fbprophet model
!pip install  fbprophet 
import pandas as pd
from  fbprophet import Prophet
import numpy as np
import matplotlib.pyplot as plt
import os
from datetime import datetime
import calendar
import csv
month = datetime.today().strftime("%m")
year = datetime.today().strftime("%Y")
num_days = calendar.monthrange(int(year), month1)[1]
date_array = []
for i in range(1,num_days + 1):
  if(i < 10):
    date_array.append('0' + str(i) + '-' + str(month) + '-' + str(year))
  else:
    date_array.append(str(i) + '-' + str(month) + '-' + str(year))
fb_labs = []
for j in range(0,np.shape(labs)[0]):
  fb_hourwise = []
  for time in range(0,24):
    fb_datewise = []
    for date in range(0,num_days):
      fb_datewise.append(labs[j][date][time])
    fb_hourwise.append(fb_datewise)
    fb_datewise = []
  fb_labs.append(fb_hourwise)
  fb_hourwise = []
lab_predict = []
for k in range(0,4):
  fb_hourwise_prediction = []
  for hours in range(0,24):
    m = Prophet()
    arr = [date_array,fb_labs[k][hours]]
    arr = np.transpose(arr)
    df = pd.DataFrame(data=np.array(arr), columns = ['ds', 'y'])
    m.fit(df)
    future = m.make_future_dataframe(periods=30)
    forecast = m.predict(future)
    fb_hourwise_prediction.append(list(forecast['yhat']))
  lab_predict.append(fb_hourwise_prediction)
  fb_hourwise_prediction = []



if _name== 'main_':
    app.run(host='0.0.0.0')
    

