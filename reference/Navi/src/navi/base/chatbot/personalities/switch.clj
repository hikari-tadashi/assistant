(ns netnavi.plugins.chatgpt.personalities.switch
  (:require [navi.plugins.chatbot.assist]))

(defn switch-personality
  ; this may require reworking core & assistant record. probably inits transcript
  "This function takes the current assistant and changes its conversational context"
  []
  (print "Which personality would you like: ")
  (flush)
  (let [input (read-line)]
    (cond
      (= input "netnavi-dev") (println "netnavi-dev")
      (= input "guru") (println "guru")
      :else (println "Sticking w/ the generic"))))