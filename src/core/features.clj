; This is where the basic set of available features and the plugin reader is written
(ns core.features
  (:require [clojure.java.shell :as shell]
            [plugins.sandbox.util :as util]))

;(let [result (clojure.java.shell/sh "firefox")])

(defn clear
  "A simple expression to clear a bash shell"
  []
  ; Uses ANSI code for 'move input to start' and "clear terminal". Does not work in all terminals
  (print "\u001b[H\u001b[2J"))

(defn clear-emacs []
  (dotimes [i 20]
    (println)))

(defn help []
  (doseq [item (keys (ns-publics 'core.features))]
    (println item))
  (doseq [item (keys (ns-publics 'plugins.memory.memory-manipulation))]
    (println item)))

(defn exit []
  (System/exit 0))

; I might want this to return 
(defn check-for-command? 
  "Checks if a command exists. If so, it runs the command" 
  [prompt] 
  (let [resolved (resolve (symbol "plugins.sandbox.features" prompt))]
    (if resolved
     (do 
       (println "Command" prompt "executed")
       (println util/line)
       (resolved)
       true)
      nil)))

; This is a modified check-for-command? meant to check across multiple namespaces
(defn check-for-commands?
  [prompt namespaces]
  (some
   (fn [ns]
     (let [resolved (resolve (symbol ns prompt))]
       (when resolved
         (println "Command" prompt "executed in" ns)
         (println util/line)
         (resolved)
         true)))
   namespaces))