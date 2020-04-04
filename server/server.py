from flask import Flask, request
import re

app = Flask(__name__)
data= open("../Eugenia.txt", encoding="utf8")
text=data.read()


def find_location(lat, long):
    tag= '\<location[^\>]*latitude='+lat+"[^\>]*longitude="+long+'[^\>]*\>[^\>]*\</location\>'
    result =  re.findall('[.?!(...)]\s*[A-Z][^\.\?\!]*'+tag+'[^\.\?\!]*[.?!(...)]', text)
    r=""
    for ex in result:
        r=r+ex[1:]
    return r


@app.route('/quotes')
def return_quote():
    latitude = request.args.get('latitude')
    longitude = request.args.get('longitude')
    result=find_location(latitude, longitude)
    return result