(ns plugins.personality.switch
  ; This needs access to the assistant atom to swap out the personality. Is this too much, should it start a new convo?
  (:require [plugins.sandbox.memory-storage]))

(def personalities ["default" "enlisted" "assistant" "dev" "netnavi-dev" "guru"])

(defn print-personalities
  "Print the available personalities to pull from"
  []
  (print personalities))

(defn switch-personality
  ; this may require reworking core & assistant record. probably inits transcript
  "This function takes the current assistant and changes its conversational context"
  []
  (print "Which personality would you like: ")
  (flush)
  ; TODO: pull this list from what is available in plugins/personality file
  (let [input (read-line)]
    (cond
      (= input "netnavi-dev") (println "netnavi-dev")
      (= input "guru") (println "guru")
      :else (println "Sticking w/ the generic"))))