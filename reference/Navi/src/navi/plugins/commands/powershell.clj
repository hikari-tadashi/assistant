(ns navi.plugins.commands.powershell
  (:require [clojure.java.io :as io]))

(def shell "powershell.exe")

(defn get-operating-system []
  (let [os-name (System/getProperty "os.name")]
    (cond
      (re-find #"(?i)windows" os-name) "Windows"
      (re-find #"(?i)linux|unix|mac" os-name) "Linux/Unix/Mac"
      :else "Unkown")))

(println (get-operating-system))

; This is needed when I am working in my VSCode projects, to not lose track of thoughts and organization
(defn portal-link 
  "This expresion is for making a symbolic link workspace in Windows"
  []
  (println "TODO:cmd.exe /C 'mklink /D C:\\Users\\%CURRENTUSER%\\Documents\\Notebook'"))
