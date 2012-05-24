# Squic: Simple Quiz Creator

Squic is a tool allowing to create simple quizz applications for android. 
this is a work-in-progress in its early steps, so please be patient...

## Files

The project is composed of:
- squic-core: the core library of the quizzes (generic, independant of the quizzes)
- squic-test: the test application used to test squic
- soon another module/script/archetype/whatever allowing to create your own quizzes (but you can already create one by copying and adapting squic-test...)


## Quizzes supported

Quizzes are composed of questions, which can be:
- registered sounds (ogg format)
- text questions spoken by textToSpeech engine

Responses are for now predetermined and can be:
- images
- words/letters
- colors

the number of questions is configurable, as well as the number of possible responses shown on a screen. 

Some evolutions are planned, like allowing mathematic questions with calculated responses, or showing text questions instead of speaking them.

## How to build your quiz application
There is no script to generate the full application yet, but you can start with the existing squic-test application and replace its quizzes with yours:
* the quizzes composing the application are listed by name in res/values/quizzes.xml
* each of these quizzes has its own xml file in res/raw decsribing the full quiz, named "yourquizname.xml" and following the XMLSchema that you can find in squic-core/schemas
* all other resources of the application are in res subdirectories, depending on resource type. Take care that they are prefixed with "id" followed by the id of the quiz (exemple: if a png is referenced as "abc" in xml of quiz 1, the file should be named "id1abc.png")

Than you can adapt your app name and other information, and generate the application with your preferred android packaging tool (Eclipse or Ant, after installing the android sdk).

## Requirements
For now, only Android 4.0.3 and later is supported (API level 15) but this could probably be changed with a little effort...
