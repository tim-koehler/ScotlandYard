# Scotland Yard (board game)

[![Build Status](https://travis-ci.com/tim-koehler/ScotlandYard.svg?branch=master)](https://travis-ci.com/tim-koehler/ScotlandYard)
[![Coverage Status](https://coveralls.io/repos/github/tim-koehler/ScotlandYard/badge.svg?branch=master)](https://coveralls.io/github/tim-koehler/ScotlandYard?branch=master)

## About

Scotland Yard is a board game in which a team of players, as police, cooperate to track down a player controlling a criminal around a board representing the streets of London, first published in 1983. It is named after Scotland Yard, the headquarters of London's Metropolitan Police Service. Scotland Yard is an asymmetric board game, with the detective players cooperatively solving a variant of the pursuit-evasion problem. The game is published by Ravensburger in most of Europe and Canada and by Milton Bradley in the United States. It received the Spiel des Jahres (Game of the Year) award in 1983.

![alt text](https://raw.githubusercontent.com/tim-koehler/ScotlandYard/master/resources/readMeScreenshot.PNG)

## Installation

```
git clone https://github.com/tim-koehler/ScotlandYard.git
```
then run the following to make the gui work (only works on linux):
```
xhost +local:$(id -un)
```
and finally
```
docker-compose up -d
```

## Background
This is a project build during the "Software Engeneering" lecture at the HTWG Konstanz
