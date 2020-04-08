import random

from flask import Flask, request
import re
import math

app = Flask(__name__)
data= open("../Eugenia.txt", encoding="utf8")
text=data.read()


def  get_possible_coordinates():
    coordinates=list()
    coordinates_expressions=re.findall('latitude=[0-9\.\s]*[^\>]*longitude=[0-9\.\s]*', text)
    for coordinate in coordinates_expressions:
        x=re.findall("[0-9\.]+", coordinate)
        coordinates.append(tuple(float(i) for i in x))
    return coordinates

coordinates = get_possible_coordinates()

def find_location(lat, long):
    tag= '\<location[^\>]*latitude=[\s]*'+str(lat)+"[^\>]*longitude=[\s]*"+str(long)+'[^\>]*\>[^\>]*\</location\>'
    result =  re.findall('[.?!(...)]\s*[A-Z][^\.\?\!]*'+tag+'[^\.\?\!]*[.?!(...)]', text)
    if result:
        return random.choice(result)
    else:
        return str(lat)+str(long)

#functe care calculeaza distanta in km
def get_distance(lat1, lon1, lat2, lon2):
    if lat1 == lat2 and lon1 == lon2:
        return 0
    else:
        radlat1 = math.pi * lat1/180
        radlat2 = math.pi * lat2/180
        theta = lon1-lon2
        radtheta = math.pi * theta/180
        dist = math.sin(radlat1) * math.sin(radlat2) + math.cos(radlat1) * math.cos(radlat2) * math.cos(radtheta)
        if (dist > 1):
            dist = 1
        dist = math.acos(dist)
        dist = dist * 180/math.pi
        dist = dist * 60 * 1.1515
        dist = dist * 1.609344
        return dist


@app.route('/quotes')
def return_quote():
    latitude = float(request.args.get('latitude'))
    longitude = float(request.args.get('longitude'))
    radius = int(request.args.get('radius'))
    quotes=list()
    for lat, long in coordinates:
        if get_distance(lat, long, latitude, longitude) <= radius:
            quotes.append(find_location(lat, long))
    return random.choice(quotes)


app.run(host = '192.168.1.91')