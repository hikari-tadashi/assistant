; This isn't actually for the plugin, but it is clj specific. I am going to make a cljc util
(ns plugins.sandbox.util)
(require
 '[clojure.java.shell :as shell]
 '[clojure.java.io :as io]
 '[clojure.data.json :as json])

(defn run-subprocess 
  "This runs a simple subprocess"
  [command]
  (let [process (:out (shell/sh command))]
    (println "Subprocess output:")
    (println (:out process))))

;(run-subprocess "echo \"hello\""

(defn setup-user-environment [])

(defn file-exists? [path]
  (.exists (io/file path)))

;ANSI colors. should in terminal
(def RED "\033[31m")
(def GREEN "\033[32m")
(def YELLOW "\033[33m")
(def BLUE "\033[34m")
(def MAGENTA "\033[35m")
(def CYAN "\033[36m")
(def WHITE "\033[37m")
(def BLACK "\033[30m")
(def RESET "\033[0m")

(def line "-----------------------------------------")

(defn check-for-config [file]
  (if (file-exists? file)
    (println "File exists")
    (println "File doesn't exist")))

;(check-for-config "src/sapphire/core.clj")