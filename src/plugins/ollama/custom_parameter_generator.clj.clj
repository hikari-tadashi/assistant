(ns plugins.ollama.custom-parameter-generator.clj
  (:require [plugins.ollama.ollama-single-chat :as chat])) 

(def model "gemma3:1b")
(def prompt "please let the user know they are recieving the generic prompt from ollama_single_chat_custom.clj")

;; --- Utility junk just to manage the JSON data for the object 
;; basically boilerplate. to be moved in the future

;; stack
(def empty-chat [{:role "system" :content "you are a simple assistant"}])

; create a new message (?) - stack
;; this is using an env data, rather than having it as an input
;; is this a code smell? 
(defn generate-single-use-prompt [prompt]
  (let [new-map {:role "user" :content prompt}]
    ; take the existing chat (no longer empty) and add the new text to it
    (conj empty-chat new-map)))

; does just chainging this change the state of the program? 
(def messages (generate-single-use-prompt "This is a demo prompt"))

;;------ The actual important part of the file

;; JSON of the Ollama request
(def ollama-request
  {:model model
   :messages messages
   :stream false
   :max_tokens 8192
   :frequency_penalty 0
   :presence_penalty 0
   :temperature 0.7
   :top_p 0.95})

;; Test functionality
;;(chat/ask-custom-json ollama-request)

