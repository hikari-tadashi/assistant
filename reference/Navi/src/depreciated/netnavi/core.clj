(ns netnavi.core
  (:require [netnavi.util :as util]
            [navi.plugins.chatbot.assist :as assistant]
            [clojure.tools.cli :refer [cli]]
            [netnavi.plugins.chatgpt.gpt :as gpt]
            [netnavi.plugins.chatgpt.personalities.core :as personality]
            [navi.plugins.commands.features :as features]
            [navi.plugins.chatbot.external :as external]
            ; This should be changed to GPT installer
            [navi.plugins.system.installer :as installer])
  (:gen-class))

(defn perpetual-loop
  "The main entry point to a NetNavi"
  []
  (loop []
     (print (format "%sWhat would you like to say to %s:%s" util/GREEN personality/navi-name util/RESET) "")
      ; Needed, otherwise print won't print before (read-line)
     (flush)
     (let [input (read-line)]
       (let [result (features/check-for-command? input)]
         (if result
           nil
           (do (println util/line)
               (println util/RED ('netnavi.plugins.jan.core/chat input) util/RESET)
               (println util/line)))))
     (recur)))

;(print @(:running-log netnavi.plugins.gpt/assistant)) 

(defn focused-session
"Launch a conversation with your Navi.jar that will take control of this terminal instance"
[]
(doall
 (installer/do-startup-check)
 (print util/BLUE (format "\n%s initalized\n%s" personality/navi-name util/RESET))
 (println util/line)
 (perpetual-loop)))

(def cli-opts
  ; The tricky part here is that :default only works if -q isn't flagged. -q with no value returns nil. set to nil for consistency
  ["-q" "--question" "ask a question" :default nil])

(defn -main
  "Main entry for the program. command line args processed here"
  [& args]
  (let [input (:question (first (cli args cli-opts)))]
    (if-not (nil? input)
      (do
        (external/init-external-assist)
        (external/append-to-current-assistant-memory input)
        (external/append-to-memory (gpt/format-prompt input))
        (flush)
           ; This pulls from the global atomic :running-log. No need for input var 
        (let [temp (gpt/format-response (external/external-chat-with-assistant))]
          (external/append-to-current-assistant-memory temp)
          (external/append-to-memory temp))
           ; Print the result
        (println (:content (last @(:running-log external/assistant)))))
    (focused-session))))
