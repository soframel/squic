# Squic: Simple Quiz Creator

Squic is a tool allowing to create simple quizz applications for Android phones (android 4.0 minimum). 
this is a work-in-progress in its early steps, so please be patient...

## Files

The project is composed of:
- squic-lib: a java library needed for the project, containing mainly the main objects and XML parser/serializer
- squic-core: the core library of the quizzes (generic, independant of the quizzes)
- squic-test: the test application used to test squic
- squic-preschool-fr: a squic application oriented toward small children, in french (for now contains 2 color quizzes and 1 animals quiz)
- squic-lesen-de: a squic application for children starting to read, in german, to recognize letters and simple words
- soon another module/script/archetype/whatever allowing to create your own quizzes (but you can already create one by copying and adapting squic-test...)


## Quizzes supported
### Multiple Choices Quizzes

Multiple Choices Quizzes are composed of questions, which can be:
- registered sounds (ogg format)
- spoken text questions (spoken by textToSpeech engine)
- text questions (no sound)

Responses can be:
- images
- text
- colors

In addition, simple arithmetic operations are also supported (+, -, * and / but with limitations), which generate their own responses. 
Questions can also be computed automatically from a "dictionary" file, in order to facilitate text-based questions with a lot of data (for example genre questions in german: der/die/das questions, or in a future version, writing questions). 

The number of questions is configurable, as well as the number of possible responses shown on a screen. 

### Writing Quizzes

It is now possible to create simple "writing" quizzes, asking the user to enter a text. 
In particular, this can be used to test spelling of words. 

This kind of game is in its first stage, and could be used in a next also to ask for calculations, etc. 

### Game Mode

In addition, the game has now 2 modes:
- the default mode "retryUntilCorrect" asks again for the same question until the right answer is given. 
- the "countPointsInGame" mode continues to the next question, and counts the points (but only in one game, there is no history/high scores yet). Points are specified in the quiz for right and wrong answers (points can be negative), and the total is shown at the end of the game. 

In addition, in the "countPointsInGame" mode, there is now a "reward" system allowing to show a reward when user has more than the required points. The reward can for now be specified as an Intent (for example to open another game, to play a video from YouTube, etc).

## How to create your quiz application
There is no script to generate the full application yet, but you can start with the existing squic-test application and replace its quizzes with yours:
* the quizzes composing the application are listed by name in res/values/quizzes.xml
* each of these quizzes has its own xml file in res/raw decsribing the full quiz, named "yourquizname.xml" and following the XMLSchema that you can find in squic-core/schemas
* all other resources of the application are in res subdirectories, depending on resource type. Take care that they are prefixed with "id" followed by the id of the quiz (exemple: if a png is referenced as "abc" in xml of quiz 1, the file should be named "id1abc.png")

Than you can adapt your app name and other information, and generate the application with your preferred android packaging tool (Eclipse or Ant, after installing the android sdk).

### How to build using Maven
You can use Maven to build squic, as well as your project.
Copy the pom.xml from any of the test projects (from squic-test for example) and change your module name, build, and that's it!

### How to build using Ant
Note that it is easier to build using Maven...
To use the provided Ant build.xml file, follow android's documentation (http://developer.android.com/guide/developing/building/building-cmdline.html). 
But as a summary:
* install android-sdk 
* install an avd emulator (see http://developer.android.com/guide/developing/devices/managing-avds.html)
* change local.properties to match your local installation folder of android-sdk
* type "ant debug" to generate application in debug mode
* start the emulator ("android avd" and then launch device)
* install the application (adb install <path_to_app>.apk)
Debug application can also be installed directly on an android device, if it is configured to accept applications in "debug" mode.

Note: if migrating to Eclipse Juno (4.2), eclipse names projects using the "android:name" from the AndroidManifest.xml. Since these are the same for all squic "modules", you have to rename projects after importing each one...

## Requirements
Android 4.0 (API levels 14)

## Next Features

The next features on the list are:
* calculation quizzes asking to enter a value (not only calculations for Multiple Choice Quizzes)
* a "repeat" button for spoken/sound questions
* a better documentation...
