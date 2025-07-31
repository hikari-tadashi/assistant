(ns plugins.memory.memory-manipulation
  (:require [plugins.sandbox.util :as util]
            [core.features :as features]
            [plugins.memory.chat-memory :as chat]
            [plugins.memory.thread-manager :as threads]
            [clojure.string :as str]))

(defn init!
  "reset the assistant back to default by mutating the record"
  []
  ; TODO: The ability to reset the chat with X should be broken out
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

; ----------------------- THREAD MANAGEMENT COMMANDS -------------------------

(defn threads
  "List all available conversation threads"
  []
  (threads/list-threads))

(defn thread-create
  "Create a new conversation thread. Usage: thread-create then enter name when prompted"
  []
  (print "Enter thread name: ")
  (flush)
  (let [thread-name (read-line)]
    (if (empty? (str/trim thread-name))
      (println (format "%sThread name cannot be empty%s" util/RED util/RESET))
      (threads/create-thread (str/trim thread-name)))))

(defn thread-switch
  "Switch to an existing conversation thread. Usage: thread-switch then enter name when prompted"
  []
  (print "Enter thread name to switch to: ")
  (flush)
  (let [thread-name (read-line)]
    (when-not (empty? (str/trim thread-name))
      (threads/switch-thread (str/trim thread-name)))))

(defn thread-rename
  "Rename an existing thread. Usage: thread-rename then enter old and new names when prompted"
  []
  (print "Enter current thread name: ")
  (flush)
  (let [old-name (read-line)]
    (when-not (empty? (str/trim old-name))
      (print "Enter new thread name: ")
      (flush)
      (let [new-name (read-line)]
        (when-not (empty? (str/trim new-name))
          (threads/rename-thread (str/trim old-name) (str/trim new-name)))))))

(defn thread-delete
  "Delete a conversation thread. Usage: thread-delete then enter name when prompted"
  []
  (print "Enter thread name to delete: ")
  (flush)
  (let [thread-name (read-line)]
    (when-not (empty? (str/trim thread-name))
      (print (format "Are you sure you want to delete thread '%s'? (y/N): " thread-name))
      (flush)
      (let [confirmation (read-line)]
        (when (= "y" (str/lower-case (str/trim confirmation)))
          (threads/delete-thread (str/trim thread-name)))))))

(defn thread-current
  "Show the current active thread"
  []
  (if-let [current (threads/get-current-thread)]
    (println (format "%sCurrent thread: %s%s%s" util/CYAN current util/GREEN util/RESET))
    (println (format "%sNo current thread active%s" util/YELLOW util/RESET))))

; Initialize thread manager on namespace load
(threads/init-thread-manager)