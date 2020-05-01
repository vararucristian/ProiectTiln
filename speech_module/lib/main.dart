
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
                                  'Titlu:' +snapshot.data,
                                  textAlign: TextAlign.center,
                                  overflow: TextOverflow.ellipsis,
                                  style: TextStyle(fontWeight: FontWeight.bold, fontSize: 35),
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
                                  'Autor:' +snapshot.data,
                                  textAlign: TextAlign.center,
                                  overflow: TextOverflow.ellipsis,
                                  style: TextStyle(fontWeight: FontWeight.bold, fontSize: 35),
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
                                  'Locatie:' +snapshot.data,
                                  textAlign: TextAlign.center,
                                  overflow: TextOverflow.ellipsis,
                                  style: TextStyle(fontWeight: FontWeight.bold, fontSize: 35),
                                );
                              } else {
                                return CircularProgressIndicator();
                              }

                            }
                        ),

                      )
                  ),
                  FlatButton(
                    color: Colors.blue,
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