import requests
import json

url="http://localhost:80/query"
#data={'prompt' : 'Is *(a+b) same as *a+b in C ?'}
#data={'prompt' : 'why is the sky blue ?'}
#prompt="Why is the sky blue ?"
#prompt="Write a python function to add two numbers"
#prompt="""Write a python function
#to add two numbers""" 
prompt="""I want to write a java utility which does the following:
1. Read the contents of a text file. The contents are the query.
2. Create a json object with propery \'prompt\' and value is the content of the query from 1.
3. Post the json object to the url="http://localhost:80/query".
4. Receive the response. If status code is 200 (OK) retrieve the response as a json object and
   write the value of the propery \'modelResponse\' to stdout"""
#prompt="How to convert a java string to a stream of integers ?"


data={'prompt' : prompt}
print(data['prompt'])
data['prompt'].replace('\n', '\\n')
print(data['prompt'])

print("Sending post request for json data:", data )
try:
    r=requests.post(url, json=data, timeout=600)
    print ("Got response. Status code:", r.status_code)
    print("response type:", type(r))
    if not r.ok:
        print(r.reason)
except requests.exceptions.RequestException as e:
    print (f"Caught exception: {e}")
except json.decoder.JSONDecodeError as e:
    print(f"Caught JSONDecodeError: {e}")

json_response = r.json()

if r.status_code == 200:
    print ("model:", json_response["model"])
    print ("prompt:", json_response["prompt"])
    print (json_response["modelResponse"])
else:
    print("error in response")
