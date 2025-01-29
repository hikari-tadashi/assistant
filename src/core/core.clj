; This is the main entry point of the program. the simple TUI 
(ns core.core
  (:require [plugins.sandbox.util :as util]
            [plugins.personality.personality :as personality]
            [plugins.memory.chat-memory :as chat]
            ; This is keeping it too tied to clojure, maybe a plugin
            [clojure.tools.cli :refer [cli]] ; to read command line input
            [core.features :as features]
            ; I have to load the namespace in to memory for it to be visible
            [plugins.memory.memory-manipulation :as mem-manip]
            [plugins.personality.switch :as switch]))

;------------------------- CORE LOOP ----------------------------------------------
(def commands ["core.features"
               "plugins.memory.memory-manipulation"
               "plugins.personality.switch"
               "plugins.sandbox.clipboard"])

(defn pipeline-configurable-loop
  "This is the main loop, leveraging pipeline confgurability"
  []
  (loop []
    ; This would be in the pre-pipeline, I think
    (print (format "%sWhat would you like to say to %s:%s" util/GREEN personality/navi-name util/RESET) "")
      ; Needed, otherwise print won't print before (read-line)
    (flush)
    (let [input (read-line)
        ; This should be '(run-pre-pipeline) 
          result (features/check-for-commands? input commands)]
      (if (nil? result)
          ; printing the util/line could be in the pre-pipeline
        (do (println util/line)
              ; this is the default pipeline.
            ; I need append the 'write to output' for this
            (println util/RED (chat/chat-with-assistant input) util/RESET)
              ; This should be (run-post-pipeline)
              ; printing the util/line for the CLI could be in  the post-pipeline
            (println util/line))))
    (recur)))

(def cli-opts
  ; The tricky part here is that :default only works if -q isn't flagged. -q with no value returns nil. set to nil for consistency
  ["-q" "--question" "ask a question" :default nil])

;--------------------------- ENTRY POINT --------------------------------------------

(defn -main
  [& args]
    (let [result (:question (first (cli args cli-opts)))]
     (if-not (nil? result)
       (println (chat/chat-with-assistant result))
       (pipeline-configurable-loop))))