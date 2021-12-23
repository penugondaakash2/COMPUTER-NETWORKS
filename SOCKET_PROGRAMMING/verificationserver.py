import socket
import os
from _thread import *
import pandas as pd

connection = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
ThreadCount = 0
try:
    connection.bind((socket.gethostname(), 1574))
except socket.error as e:
    print(str(e))

print('WAITING FOR AN CONNECTION..')
connection.listen(5)

col_names = ["Name","Phone_No","District","Pincode","Area","Problem","Verification"]
filename = "problem.csv"
df = pd.read_csv(filename)
df.columns = col_names

def showData(df,column_name,value):
	g = df.groupby(column_name)
	return g.get_group(value)

def getUserData(df,Name):
	df = df.iloc[Name]
	print(df)
	return df

def modifyData(df, Name, new_value):
	for index in df.index:
 		if df.loc[index,"Name"] == Name:
 			df.loc[index,"Verification"] = new_value
	return df

def update(df):
	df["Verification"] = df["Verification"].fillna("NotVerified")
	return df




def threaded_client(connection):

	while True:
		data = connection.recv(4048).decode('utf-8')
		
		if not data:
			break
		data = str(data)
		print("From Connected User : "+data)

		if data == "V":
			showData = df.to_string()
			connection.send(showData.encode())
		elif data.find("M") != -1:
			split_data = data.split()
			showData = modifyData(df,split_data[1],split_data[2]).to_string()
			connection.send(showData.encode())
			df.to_csv("problem.csv",index=False)
		elif data.find("U") != -1:
			showData = update(df).to_string()
			connection.send(showData.encode())
			df.to_csv("problem.csv",index=False)

        
  
	connection.close()


while True:
    clt, adr = connection.accept()
    print(f"Connection established to {adr} established")
    start_new_thread(threaded_client, (clt,))
    ThreadCount += 1
    print('Thread Number: ' + str(ThreadCount))

connection.close()
