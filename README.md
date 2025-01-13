This project contains 3 projects within it. 

First is the goal project. It is at the projects root directory under src, and the remaining files are project or IDE related.
The goal is to write an entire clojurescript project for api calling a LM Studio/Jan/cortex/llama.cpp based ML model, so it can be used anywhere.
The sechmatics for the base server is coming, and ultimate goal is to self-host on a mobile device

Second is Navi, the functioning Clojure command line assistant. This is the base project we are growing out of, and it should remain functional through the transition process

The third is a figwheel based project, to be disected for helping with moving the second project into the first project

## To run Navi
Prereq: Have Clojure and Leiningen installed

- Navigate to reference/Navi/
- run "lein run"