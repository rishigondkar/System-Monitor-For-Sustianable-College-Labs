from flask_cors import CORS, cross_origin
from flask import *
import json
from pandas import read_csv
import pandas as pd
import numpy as np
from plotly.offline import plot
from plotly.graph_objs import Scatter
import json
import os
import csv
from datetime import datetime
import calendar
import matplotlib.pyplot as plt
from  fbprophet import Prophet

app=Flask(__name__)
CORS(app)
app.config['CORS_HEADERS'] = 'Content-Type'


@app.route('/')
def my_form():
    return "HELLO"
    return render_template('')

@app.route('/', methods=['POST'])
def my_form_post():
    data = request.get_data()
    print(data)
    return "FUCK YOU"
    #return render_template('./src/firstRun.html')

@app.route('/get', methods=['GET'])
def shubham():
    print("SHUBHAM")
    return "Mihir"

# @app.route('/graph', methods=['GET'])
# def graph_data():
#     dataset = read_csv("./correctdata.txt",sep='\n',header=2, low_memory=False, infer_datetime_format=True)
#     dataset.columns = ['A']
    
    
#     for i in range(0, 1632, 3):
#         dataset['A'][i] = pd.to_numeric(dataset['A'][i])
#     for i in range(1, 1633, 3):
#         dataset['A'][i] = pd.to_numeric(dataset['A'][i])
        
        
#     pd.date_range(start ='2019-9-20 3:13:12', freq ='W', end = '2019-9-20 3:22:19')  
    
    
#     cpu = np.array(())
#     memory = np.array(())
#     time = np.array(())
    
#     for i in range(0, 1632, 3):                         # max = total rows - 2
#         cpu  = np.append(cpu,dataset['A'][i])           
    
#     for j in range(1, 1633, 3):                         
#         memory  = np.append(memory,dataset['A'][j])
        
#     for i in range(2, 1634, 3):
#         time  = np.append(time,dataset['A'][i])
    
#     data = pd.DataFrame(data = memory, index = time,columns = ['Memory used'])
#     data['CPU used'] = cpu
    
#     threshold_cpu = 4
#     threshold_memory = 5
#     new = pd.DataFrame((data['CPU used'] > threshold_cpu) & (data['Memory used'] > threshold_memory))
    
#     new.columns = ['A']
#     #time_idle = (~new.A).sum() #in seconds
    
    

#     index = list(range(1, 545))
#     data['index'] = index
    
#     data.set_index('index',inplace = True)
#     data['Timestamp'] = time
    
#     timestamp = np.array(())
#     for i in range(1,544,20):
#         timestamp = np.append(timestamp,data.iloc[i]['Timestamp'])

#     cpu2 = np.array(())
#     for i in range(1,544,20):
#         cpu2 = np.append(cpu2,data.iloc[i]['CPU used'])
#     memory2 = np.array(())
#     for i in range(1,544,20):
#         memory2 = np.append(memory2,data.iloc[i]['Memory used'])
        
    

#     sample = np.array(new['A'])
#     cost_idle = np.array(())
#     cost_on = np.array(())
#     factor = 0.15
#     usage = 400/360
#     value = np.array(())
    
#     for k in range(0,32):
#         cost_idle_temp = 0
#         cost_on_temp = 0
    
#         for i in range(k*17,(k+1)*17):
#             value = np.append(value,sample[i])
#         for j in range(k*17,(k+1)*17):
#             if value[j]==0:
#                     cost_idle_temp = cost_idle_temp + factor*usage
#             else:
#                 cost_on_temp = cost_on_temp + usage
            
#         cost_idle = np.append(cost_idle,cost_idle_temp)
#         cost_on = np.append(cost_on,cost_on_temp)
#     total_power = cost_on + cost_idle
#     timestamp_power = np.array(())
#     for i in range(1,544,17):
#         timestamp_power = np.append(timestamp_power,data.iloc[i]['Timestamp'])
#     my_plot_div = plot([Scatter(x=timestamp, y=memory2)], output_type='div')
#     cpu2_graph = plot([Scatter(x=timestamp, y=cpu2)], output_type='div')
#     total_power = plot([Scatter(x=timestamp_power, y=total_power)], output_type='div')
#     return render_template('cpuGraph.html', my_plot_div = Markup(my_plot_div), cpu2_graph = Markup(cpu2_graph), total_power = Markup(total_power))


@app.route('/graph', methods=['GET'])
def get_graph_data():
    dataset = read_csv("./correctdata.txt",sep='\n',header=2, low_memory=False, infer_datetime_format=True)
    dataset.columns = ['A']
    
    cnt = dataset['A'].count() - 1
    
    for i in range(0, cnt-2, 3):
        dataset['A'][i] = pd.to_numeric(dataset['A'][i])
    for i in range(1, cnt-1, 3):
        dataset['A'][i] = pd.to_numeric(dataset['A'][i])
        
        
    pd.date_range(start ='2019-9-20 3:13:12', freq ='W', end = '2019-9-20 3:22:19')  
    
    
    cpu = np.array(())
    memory = np.array(())
    time = np.array(())
    
    for i in range(0, cnt-2, 3):                         # max = total rows - 2
        cpu  = np.append(cpu,dataset['A'][i])           
    
    for j in range(1, cnt-1, 3):                         
        memory  = np.append(memory,dataset['A'][j])
        
    for i in range(2, cnt, 3):
        time  = np.append(time,dataset['A'][i])
    
    data = pd.DataFrame(data = memory, index = time,columns = ['Memory used'])
    data['CPU used'] = cpu
    
    threshold_cpu = 4
    threshold_memory = 5
    new = pd.DataFrame((data['CPU used'] > threshold_cpu) & (data['Memory used'] > threshold_memory))
    
    new.columns = ['A']
    #time_idle = (~new.A).sum() #in seconds
    
    

    index = list(range(1, int((cnt+1)/3)))
    data['index'] = index
    
    data.set_index('index',inplace = True)
    data['Timestamp'] = time
    
    timestamp = np.array(())
    for i in range(1,int((cnt+1)/3),60):
        timestamp = np.append(timestamp,data.iloc[i]['Timestamp'])

    cpu2 = np.array(())
    for i in range(1,int((cnt)/3),60):
        cpu2 = np.append(cpu2,data.iloc[i]['CPU used'])
    memory2 = np.array(())
    for i in range(1,int((cnt)/3),60):
        memory2 = np.append(memory2,data.iloc[i]['Memory used'])
        
    

    sample = np.array(new['A'])
    cost_idle = np.array(())
    cost_on = np.array(())
    factor = 0.15
    usage = 400/360
    value = np.array(())
    
    for k in range(0,int(int((cnt)/3)/17)):
        cost_idle_temp = 0
        cost_on_temp = 0
    
        for i in range(k*17,(k+1)*17):
            value = np.append(value,sample[i])
        for j in range(k*17,(k+1)*17):
            if value[j]==0:
                    cost_idle_temp = cost_idle_temp + factor*usage
            else:
                cost_on_temp = cost_on_temp + usage
            
        cost_idle = np.append(cost_idle,cost_idle_temp)
        cost_on = np.append(cost_on,cost_on_temp)
    total_power = cost_on + cost_idle
    
    timestamp_power = np.array(())
    for i in range(1,int((cnt)/3),17):
        timestamp_power = np.append(timestamp_power,data.iloc[i]['Timestamp'])
    power = np.sum(total_power)
    power_reduction = np.sum(cost_idle)
    power_cost = 5 
    total_power_cost = (power*power_cost)/100
    saved_cost = power_reduction*power_cost/100
    my_plot_div = plot([Scatter(x=timestamp, y=memory2)], output_type='div')
    cpu2_graph = plot([Scatter(x=timestamp, y=cpu2)], output_type='div')
    total_power = plot([Scatter(x=timestamp_power, y=total_power)], output_type='div')
    total_power_cost = round(total_power_cost, 2)
    return render_template('cpuGraph.html', my_plot_div = Markup(my_plot_div), cpu2_graph = Markup(cpu2_graph), total_power = Markup(total_power), power_cost = total_power_cost, saved_cost = saved_cost)


if __name__== '__main__':
    app.run(host='0.0.0.0')



























 # data = request.body
    # print(data)
    # tid=data['tid']
    # broker_id=data['broker_id']
    # client_id=data['client_id']
    # product_id=data['product_id']
    # amount=data['amount']
    # status=data['status']
    # transtype=data['transaction_type']
    # action=data['type']