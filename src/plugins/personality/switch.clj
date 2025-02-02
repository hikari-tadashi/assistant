(ns plugins.personality.switch 
  (:require
   [plugins.memory.chat-memory :as mem]
   [plugins.memory.memory-storage :as storage]
   [plugins.personality.personality :as personality]))

(def personalities ["default" "enlisted" "assistant" "dev" "netnavi-dev" "guru"])

(defn print-personalities
  "Print the available personalities to pull from"
  []
  (print personalities))

(defn read-and-print-personalities [ns-sym]
  (doseq [item (keys (ns-publics ns-sym))]
    (println item))
  (flush)
  (let [input (read-line)
        resolved (resolve (symbol "plugins.personality.personality" input))]
    (if resolved
      (do
        (println "Changing to" input)
        ;TODO (save)
        (mem/personality-init! @(resolve (symbol "plugins.personality.personality" input)))
        (storage/save-memory "memory/config.memory")))))

(defn switch-personality
  ; this may require reworking core & assistant record. probably inits transcript
  "This function takes the current assistant and changes its conversational context"
  []
  (print "Which personality would you like: ")
  (flush)
  (read-and-print-personalities 'plugins.personality.personality))
  ; TODO: pull this list from what is available in plugins/personality file
  ;(let [input (read-line)]
  ;  (cond
  ;    (= input "netnavi-dev") (mem/personality-init! personality/netnavi-dev)
  ;    (= input "guru") (println "guru")
  ;    :else (println "Sticking w/ the generic"))))

;(println personality/netnavi-dev)