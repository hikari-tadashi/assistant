(ns plugins.memory.memory-storage
  (:import [plugins.memory.chat-memory Assistant])
  (:require [plugins.memory.chat-memory :as memory]))

; I think this is the same as above, I am just calling it a different way
(require
 '[wkok.openai-clojure.api :as api]) 

; This should be moved to GPT Module
(def assistant (Assistant. (atom memory/empty-chat)))

(def memory "memory/autosave.memory")

(defn save-memory [] 
   (doseq [value @(:running-log assistant)]
     (spit memory (str value "\n") :append true)))

(defn load-memory []
  (->> (slurp memory)
       (clojure.string/split-lines)
       ; This is used to convert each line into a Clojure map representing the k-v pairs
       (map #(read-string %))
       ; load them into their respective maps
       (map (partial into {}))
       ; load them into their vector
       (into [])))

(defn init-external-assist []
  (reset! (:running-log assistant) (load-memory)))

; This should be at the end of the pipeline?
; This is spitting a dict to the end of a file, when it needs to append it to the list
(defn append-to-memory [data]
  (spit memory (str data "\n") :append true))

(defn external-chat-with-assistant []
  (get-in (api/create-chat-completion {:model "llama-3.2-8b-instruct"
                                       :messages @(:running-log assistant)}) [:choices 0 :message :content]))

; current assistant is external/assistant, not gpt/assistant. Need to make this more clear
(defn append-to-current-assistant-memory [data]
   (swap! (:running-log assistant) conj (memory/format-prompt data)))

; ---------- TESTING -------------

; reset to proper assistant default
;(reset! (:running-log assistant) gpt/empty-chat)
; init loads from memory file
;(init-external-assist)
;@(:running-log assistant)

