(ns navi.core
  (:require [navi.plugins.cli.util :as util]
            [navi.base.chatbot.personalities.core :as personality]
            [navi.base.chatbot.core :as jan] 
            [navi.chat-memory :as chat]
            ; This is tying it to clojure, rather than clojurescript
            [clojure.tools.cli :refer [cli]]
            [navi.plugins.commands.features :as features]))

(defn pipeline-configurable-loop
  "This is the main loop, leveraging pipeline confgurability"
  []
  (loop []
    ; This would be in the pre-pipeline, I think
    (print (format "%sWhat would you like to say to %s:%s" util/GREEN personality/navi-name util/RESET) "") 
    (flush) ; Needed, otherwise print won't print before (read-line)
    (let [input (read-line)
        ; This should be '(run-pre-pipeline) 
          result (features/check-for-command? input)]
      (if (nil? result)
          ; printing the util/line could be in the pre-pipeline
        (do (println util/line)
              ; this is the default pipeline.
            (println util/RED (chat/chat-with-assistant input) util/RESET)
              ; This should be (run-post-pipeline)
              ; printing the util/line for the CLI could be in  the post-pipeline
            (println util/line))))
    (recur)))

(def cli-opts
  ; The tricky part here is that :default only works if -q isn't flagged. -q with no value returns nil. set to nil for consistency
  ["-q" "--question" "ask a question" :default nil])

(defn -main
  [& args]
    (let [result (:question (first (cli args cli-opts)))]
     (if-not (nil? result)
       ; This is for the one-off cli question
       (println (chat/chat-with-assistant result))
       ; This is the start of a the long-running application
       (pipeline-configurable-loop))))