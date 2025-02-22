""" Post a query using pycurl"""
import pycurl
import json
from io import BytesIO

def get_prompt():
    #prompt="Write a python function to add two numbers"
    #prompt="How to convert a java string to a stream of integers ?"
    prompt="""I want to write a java utility which does the following:
    1. Read the contents of a text file. The contents are the query.
    2. Create a json object with propery \'prompt\' and value is the content of the query from 1.
    3. Post the json object to the url="http://localhost:80/query".
    4. Receive the response. If status code is 200 (OK) retrieve the response as a json object and
    write the value of the propery \'modelResponse\' to stdout"""
    
    return prompt

def get_prompt_string( prompt ):
    data={'prompt' : prompt}
    #print(data['prompt'])
    data['prompt'].replace('\n', '\\n')
    return data
    

if __name__ == '__main__':
    url="http://localhost:80/query"
    #prompt="Write a python function to add two numbers"
    prompt = get_prompt()
    json_data = json.dumps(get_prompt_string(prompt))
    print(f"json_data type: {type(json_data)}")

    buffer = BytesIO()

    curl = pycurl.Curl()
    curl.setopt(curl.URL, url)
    curl.setopt(curl.POST,1)
    curl.setopt(curl.POSTFIELDS, json_data )
    curl.setopt(curl.WRITEDATA, buffer )

    curl.setopt(pycurl.TIMEOUT, 600 )
    curl.setopt(pycurl.CONNECTTIMEOUT, 10)     
    # Set headers
    curl.setopt(curl.HTTPHEADER, [
        "Content-Type: application/json",
        "Accept: application/json"
    ])

    print("posting request")
    curl.perform()
    print("request completed")
    
    http_code = curl.getinfo(pycurl.RESPONSE_CODE)
    print(f"HTTP response code: {http_code}")

    response_body = buffer.getvalue().decode("utf-8")

    if http_code == 200:
        response_json = json.loads(response_body)
        print(response_json["modelResponse"])
    else:
        print(response_body)

    #Print metadata information
    print("Total Time:", curl.getinfo(pycurl.TOTAL_TIME))           # Time taken for request
    print("Downloaded Bytes:", curl.getinfo(pycurl.SIZE_DOWNLOAD))  # Downloaded bytes
    print("Effective URL:", curl.getinfo(pycurl.EFFECTIVE_URL))     # Final URL after redirections

    # if http_code == 200:
    #     response_body = buffer.getvalue().decode("utf-8")
    #     response_json = json.loads(response_body)
    #     print(response_json["modelResponse"])
    # else:
    #     response_body = buffer.getvalue().decode("utf-8")
    #     #response_json = json.loads(response_body)
    #     print(response_body)
        
    curl.close()
