[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=plastic)](https://android-arsenal.com/api?level=16)
# JumpGame
*Android FlappyBird-like game*

## What is JumpGame
Learning project.
JumpGame is a simple android, FlappyBird-like game.

## What was used to build app
To code and build this app was used:

**Tools**
* Java 7
* Android Studio 2.3

**Pattern**
* MVVM with Data Binding

**Libraries**
* <a href="https://github.com/ReactiveX/RxJava" title="GraphView">RxJava</a>

**Tested on**
* Nexus 4
* API version 22

## How does it look like
Game has only one view ( state 20.04.2017).

Navigation between activities and fragments is done through Navigarion Drawer | Main view section, consists lists of saved recording sessions
:-------------------------:|:-------------------------:
<img src="https://rawgit.com/GregoryIwanek/AltiMeter/GregoryIwanek-readme/screenshot/nav_drawer.png" title="nav drawer" height="250" />  |  <img src="https://rawgit.com/GregoryIwanek/AltiMeter/master/screenshot/main_view.png" title="main view" height="250" />

Recording session section, works as operation center for recording altitude and locations | Details section, shows basic information and statistics of choosen session
:-------------------------:|:-------------------------:
<img src="https://rawgit.com/GregoryIwanek/AltiMeter/GregoryIwanek-readme/screenshot/recording_session.png" title="recording session" height="250" />  |  <img src="https://rawgit.com/GregoryIwanek/AltiMeter/GregoryIwanek-readme/screenshot/details.png" title="details section" height="250" />

Statistics section, shows global statistics of the all recording sessions | Map section, shows recorded locations as a path on a google map
:-------------------------:|:-------------------------:
<img src="https://rawgit.com/GregoryIwanek/AltiMeter/GregoryIwanek-readme/screenshot/statistics.png" title="statistics section" height="250" />  |  <img src="https://rawgit.com/GregoryIwanek/AltiMeter/GregoryIwanek-readme/screenshot/map.png" title="map section" height="250" />

About section, consists series of fragments with descriptions of other sections | Settings section, holds bunch of custom user settings
:-------------------------:|:-------------------------:
<img src="https://rawgit.com/GregoryIwanek/AltiMeter/master/screenshot/about.png" title="about section" height="250" />  |  <img src="https://rawgit.com/GregoryIwanek/AltiMeter/master/screenshot/settings.png" title="settings section" height="250" />

## To do
* add sound effects
* replace java interface callbacs with RxJava
* add new types of enemies ( random movement pattern)
* add attack ( under bomb button -> remove all enemies upon use)
* add new types of bonuses
