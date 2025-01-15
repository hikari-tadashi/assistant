(ns plugins.memory.memory-manipulation
  (:require [plugins.sandbox.util :as util]
            [core.features :as features]
            [plugins.memory.chat-memory :as chat]))

(defn init!
  "reset the assistant back to default by mutating the record"
  []
  (reset! (:running-log chat/assistant) chat/empty-chat)
  (features/clear)
  (println (format "%sReinitialized%s" util/RED util/RESET)))

(defn init []
  (init!))

(defn strike-last-input!
  "This form removes the last prompt/response pair"
  []
  (if (< (count @(:running-log chat/assistant)) 2)
    (println "Nothing to do!")
    (swap! (:running-log chat/assistant) #(subvec % 0 (- (count %) 2)))))

(defn print-running-log []
  (println @(:running-log chat/assistant)))

(defn print-last-prompt []
  (println (let [result (:content (get @(:running-log chat/assistant) (- (count @(:running-log chat/assistant)) 2)))]
             (format "%s%s%s" util/RED result util/BLUE))))

(defn print-last-response []
  (println (let [result (:content (last @(:running-log chat/assistant)))]
             (format "%s%s%s" util/RED result util/BLUE))))

(defn echo-transcript []
  (print "Enter filename: ")
  (flush) 
  (let [filename (read-line)]
    (doseq [index (range (count @(:running-log chat/assistant)))]
      (spit filename (nth @(:running-log chat/assistant) index) :append true)
      (spit filename "\n" :append true))))


(defn echo
  "Used to print the last output to a file"
  []
  (println "Enter filename to spit: ")
  (let [filename (read-line)]
    (spit filename (last @(:running-log chat/assistant)))
    (spit filename "\n")))

; TODO: Break this out a bit more
;(defn c-clip 
;  "Append the clipboards text to the next message"
;  []
;  (print (format "%sWhat would you like to say to %s:%s" util/GREEN personality/navi-name util/RESET) "")
;  (flush)
  ; This is almost the (perpetual-loop) expression in netnavi.core
;  (let [text (clipboard/get-clipboard-string)
;        input (read-line)
;        added-data (str (format "The following data has been attached as supplementary to the prior text. Please consider it in your response: %s" text))] 
;    (do (println util/line)
;        (println util/RED (gpt/chat-with-assistant (format "%s %s" input added-data)) util/RESET)
;        (println util/line))))

(defn echo-append []
  ; This could/should be pulled from a let? then I can call it from elsewhere
  (print "Enter filename: ")
  (flush)
  (let [filename (read-line)]
    (spit filename (get (last @(:running-log chat/assistant)) :content) :append true)
    (spit filename "\n" :append true)))

;(check-for-command? "init!")

;(print netnavi.plugins.gpt/assistant)
;(count @(:running-log netnavi.plugins.gpt/assistant))
;(strike-last-input!)
;(init!)