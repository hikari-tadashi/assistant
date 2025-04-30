(ns plugins.personality.switch 
  (:require
   [plugins.memory.chat-memory :as mem]
   [plugins.personality.personality :as personality]
   [clojure.java.shell :as shell]
   [clojure.string :as str]))

(def personalities ["default" "enlisted" "assistant" "dev" "netnavi-dev" "guru"])

(defn print-personalities
  "Print the available personalities to pull from"
  []
  (print personalities))

(defn switch-personality
  ; this may require reworking core & assistant record. probably inits transcript
  "This function takes the current assistant and changes its conversational context"
  []
  (print ()
         (flush)
         (print "Which personality would you like: ")
         (flush)
         ()
  ; TODO: pull this list from what is available in plugins/personality file
         (let [input (read-line)]
           (cond
             (= input "dev") (mem/personality-init! personality/netnavi-dev)
             (= input "assistant") (mem/personality-init! (str/trim (:out (shell/sh "bash" "-c" "cd /Users/chris/Lab/assistant/src/prompt_builder ; lein run assistant"))))
             :else (println "Sticking w/ the generic")))))

;(println personality/netnavi-dev)