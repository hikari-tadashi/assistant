(ns assistant.core
  (:require [navi.plugins.cli.util :as util]
            [navi.base.chatbot.personalities.core :as personality]
            [navi.base.chatbot.core :as chatbot]
            ; This is keeping it too tied to clojure, maybe a plugin?
            [clojure.tools.cli :refer [cli]]
            [navi.plugins.commands.features :as features]))

;------------------------- CORE LOOP ----------------------------------------------

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
          result (features/check-for-command? input)]
      (if (nil? result)
          ; printing the util/line could be in the pre-pipeline
        (do (println util/line)
              ; this is the default pipeline.
            (println util/RED (chatbot/extract-response (chatbot/chat input)) util/RESET)
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
       (println (chatbot/extract-response (chatbot/chat result)))
       (pipeline-configurable-loop))))