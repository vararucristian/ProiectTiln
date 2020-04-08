import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_tts/flutter_tts.dart';

void main() => runApp(TheApp());

class TheApp extends StatelessWidget{
  @override
  Widget build(BuildContext context){
    return MaterialApp(
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
    _speak() async{
      String text = await communicationChannel.invokeMethod('getSpeechText');
      await flutterTts.setLanguage("ro-RO");
      await flutterTts.speak(text);
    }
    _speak();
    SystemNavigator.pop();  }
}