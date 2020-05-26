import random
import json

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


def find_quote_by_name(name):
    final_sentence=[".","?","!"]
    result=list()
    text_without_coord=re.sub("latitude[\s]*=[^\>]*longitude[\s]*=[^\> ]*", "", text)
    finds= re.findall('[.?!]\s*[A-ZÎĂȚȘ„-][^.?!]*entity_name=\"'+name+'\"[^.?!]*[.?!“]+', text_without_coord)
    print(name)
    for find in finds:
        sentence=find[2:]
        start_position = text_without_coord.find(sentence)
        stop_position = start_position+1000
        while(text_without_coord[stop_position] not in final_sentence):
            stop_position+=1
        quote=text_without_coord[start_position:stop_position+1]
        r = dict()
        r["quote"]=re.sub("\<[^\>]*\>", "",quote)
        r["author"]=re.findall('author=\"[^\"]*\"',quote)[0][8:-1]
        r["title"]=re.findall('title=\"[^\"]*\"',quote)[0][7:-1]
        r["place"]=name
        result.append(r)
    print (result)
    return result



def find_quote_by_location(lat, long):
    tag= '\<location[^\>]*latitude[\s]*=[\s]*'+str(lat)+"[0]*[^\>]*longitude[\s]*=[\s]*"+str(long)+'[0]*[^\>]*\>[^\<]*\</location\>'
    name =  re.findall(tag, text)[0]
    result=list()
    quotes=find_quote_by_name(re.findall('entity_name=\"[^\"]*\"', name)[0][13:-1])
    if quotes:
        print (quotes)
        return random.choice(quotes)
    else:
        return "Nu s-a returnat nici un citat."

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
    result=dict()
    result["quote"]="-"
    result["author"] = "-"
    result["title"] = "-"
    result["place"] = "-"
    latitude = float(request.args.get('latitude'))
    longitude = float(request.args.get('longitude'))
    radius = int(request.args.get('radius'))
    mimimum=radius*0.001
    mimimum_lat=0
    mimimum_long=0
    for lat, long in coordinates:
        if get_distance(lat, long, latitude, longitude) <= mimimum :
            mimimum_lat=lat
            mimimum_long=long
            mimimum=get_distance(lat, long, latitude, longitude)
    aux=find_quote_by_location(mimimum_lat, mimimum_long)
    result["quote"]=aux["quote"].replace("\n", " ")
    result["author"]=aux["author"].replace("\n", " ")
    result["title"] = aux["title"].replace("\n", " ")
    result["place"] = aux["place"].replace("\n", " ")
    result=json.dumps(result, ensure_ascii=False).encode('utf8')
    return result


app.run(host = '192.168.1.91')
