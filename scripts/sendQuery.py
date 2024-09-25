import requests
import json

url="http://localhost:8080/query"
data={'prompt' : 'How to pretty print the json response from a spring rest controller ?'}
print("Sending post request for json data:", data )
r=requests.post(url, json=data)
print ("Got response. Status code:", r.status_code)

json_response = r.json()
if r.status_code == 200:
    print (json_response["modelResponse"])
