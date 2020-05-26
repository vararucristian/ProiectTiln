
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_tts/flutter_tts.dart';

void main() => runApp(TheApp());

class TheApp extends StatelessWidget{
  @override
  Widget build(BuildContext context){
    return MaterialApp(
        debugShowCheckedModeBanner: false,
      home:Scaffold(
        body: MyApp(),
      ),
    );
  }
}

class MyApp extends StatelessWidget{

  final FlutterTts flutterTts = FlutterTts();
  static const communicationChannel = const MethodChannel("speechDataChannel");

  @override
  Widget build(BuildContext context){
    String text;
    String textTitle;
    String textAuthor;
    String textLocation;

    getTextData() async{
      text = await communicationChannel.invokeMethod('getSpeechText');
    }

    Future<String> getTextTitleData() async {
      textTitle = await communicationChannel.invokeMethod('getTextTitle');
      return textTitle;
    }

    Future<String> getTextAuthorData() async{
      textAuthor = await communicationChannel.invokeMethod('getTextAuthor');
      return textAuthor;
    }
    Future<String> getTextLocationData() async{
      textLocation = await communicationChannel.invokeMethod('getTextLocation');
      return textLocation;
    }

    _speak() async{

      await flutterTts.setLanguage("ro-RO");
      await flutterTts.speak(text);
    }

    getTextData();
    getTextTitleData();
    _speak();


    return Container(
      decoration: BoxDecoration(
        image: DecorationImage(
          image: AssetImage("assets/images/geo.jpg"),
          fit:BoxFit.cover,
        )
      ),
        child: Center(
            child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: CrossAxisAlignment.center,
                children:<Widget>[
                  Container(
                      child:Center(
                        child:FutureBuilder<String>(
                            future: getTextTitleData(),
                            builder: (context, AsyncSnapshot<String> snapshot) {
                              if (snapshot.hasData) {
                                return Text(
                                  'Titlu:',
                                  textAlign: TextAlign.center,
                                  overflow: TextOverflow.ellipsis,
                                  style: TextStyle(fontWeight: FontWeight.normal, fontSize: 25, color: Colors.blueGrey[50]),
                                );
                              } else {
                                return CircularProgressIndicator();
                              }

                            }
                        ),

                      )
                  ),
                  Container(
                      child:Center(
                        child:FutureBuilder<String>(
                            future: getTextTitleData(),
                            builder: (context, AsyncSnapshot<String> snapshot) {
                              if (snapshot.hasData) {
                                return Text(
                                  snapshot.data,
                                  maxLines: 4,
                                  textAlign: TextAlign.center,
                                  overflow: TextOverflow.ellipsis,
                                  style: TextStyle(fontWeight: FontWeight.normal, fontSize: 25, color: Colors.blueGrey[50]),
                                );
                              } else {
                                return CircularProgressIndicator();
                              }

                            }
                        ),

                      )
                  ),
                  Container(
                      child:Center(
                        child:FutureBuilder<String>(
                            future: getTextAuthorData(),
                            builder: (context, AsyncSnapshot<String> snapshot) {
                              if (snapshot.hasData) {
                                return Text(
                                  'Autor:',
                                  textAlign: TextAlign.center,
                                  overflow: TextOverflow.ellipsis,
                                  style: TextStyle(fontWeight: FontWeight.normal, fontSize: 25,  color: Colors.blueGrey[50]),
                                );
                              } else {
                                return CircularProgressIndicator();
                              }

                            }
                        ),

                      )
                  ),
                  Container(
                      child:Center(
                        child:FutureBuilder<String>(
                            future: getTextAuthorData(),
                            builder: (context, AsyncSnapshot<String> snapshot) {
                              if (snapshot.hasData) {
                                return Text(
                                  snapshot.data,
                                  maxLines: 4,
                                  textAlign: TextAlign.center,
                                  overflow: TextOverflow.ellipsis,
                                  style: TextStyle(fontWeight: FontWeight.normal, fontSize: 25,  color: Colors.blueGrey[50]),
                                );
                              } else {
                                return CircularProgressIndicator();
                              }

                            }
                        ),

                      )
                  ),
                  Container(
                      child:Center(
                        child:FutureBuilder<String>(
                            future: getTextLocationData(),
                            builder: (context, AsyncSnapshot<String> snapshot) {
                              if (snapshot.hasData) {
                                return Text(
                                  'Locatie:',
                                  textAlign: TextAlign.center,
                                  overflow: TextOverflow.ellipsis,
                                  style: TextStyle(fontWeight: FontWeight.normal, fontSize: 25,  color: Colors.blueGrey[50]),
                                );
                              } else {
                                return CircularProgressIndicator();
                              }

                            }
                        ),

                      )
                  ),
                  Container(
                      child:Center(
                        child:FutureBuilder<String>(
                            future: getTextLocationData(),
                            builder: (context, AsyncSnapshot<String> snapshot) {
                              if (snapshot.hasData) {
                                return Text(
                                  snapshot.data,
                                  maxLines: 4,
                                  textAlign: TextAlign.center,
                                  overflow: TextOverflow.ellipsis,
                                  style: TextStyle(fontWeight: FontWeight.normal, fontSize: 25,  color: Colors.blueGrey[50]),
                                );
                              } else {
                                return CircularProgressIndicator();
                              }

                            }
                        ),

                      )
                  ),
                  FlatButton(
                    color: Colors.purple[300],
                    textColor: Colors.white,
                    disabledColor: Colors.grey,
                    disabledTextColor: Colors.black,
                    padding: EdgeInsets.all(8.0),
                    splashColor: Colors.blueAccent,
                    onPressed: (){  SystemNavigator.pop();},
                    child: Text(
                      "Back",
                      style: TextStyle(fontSize: 20.0),
                    )
                  )
                ])
        ),
    );

  }
}