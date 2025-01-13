(ns navi.chat-memory
  (:require [navi.base.chatbot.personalities.core :as personality]
            ; Needed to make our own api call out
            [clojure.data.json :as json]
            [clj-http.client :as client]))

(require
 '[wkok.openai-clojure.api :as api])

; defrecord only works for clojure
; create a record for a conversation
(defrecord Assistant [running-log])

; create an preconfigured baseline chat
; This is where personality is getting called
(def empty-chat [{:role "system" :content personality/standard}])

; Make one for our programs own purposes. 
(def assistant (Assistant. (atom empty-chat)))

; format user input for storage
(defn format-prompt [prompt]
  (let [new-map {:role "user" :content prompt}]
    new-map))

; format model response for storage
(defn format-response [response]
  (let [new-map {:role "assistant" :content response}]
    new-map))

; update the record with a user prompt
(defn log-prompt-update! [prompt]
  (swap! (:running-log assistant) conj (format-prompt prompt))
  prompt)

; update the record with a model response
(defn log-response-update! [response]
  (swap! (:running-log assistant) conj (format-response response))
  response)

; create a new message (?)
(defn add-new-message [message]
  (let [new-map {:role "user" :content message}]
    ; take the existing chat (no longer empty) and add the new text to it
    (conj empty-chat new-map)))

; -------------------- FROM CHATBOT CORE -------------------------------------------------
(defn chat [messages] 
  (client/post "http://192.168.68.70:7000/v1/chat/completions" {:content-type :json
                                                                :form-params {:messages messages
                                                                              :model "llama-3.2-8b-instruct"
                                                                              :stream false
                                                                              :max_tokens 4096
                                                                              :frequency_penalty 0
                                                                              :presence_penalty 0
                                                                              :temperature 0.7
                                                                              :top_p 0.95}}))

(defn extract-response [resp]
  (let [body (get resp :body)
        json-object (json/read-json body)
        content (get-in json-object [:choices 0 :message :content])]
    content))
; -------------------- FROM CHATBOT CORE -------------------------------------------------

; stateful conversation with model, uses api library
(defn chat-with-assistant
  "Takes in a string, formats it for GPT, updates the atom! with the string prompt and response. returns response"
  [prompt]
  ; Add the user prompt to the running log
  (add-new-message (log-prompt-update! prompt))
  ; [:choices 0 :message :content]
  (log-response-update! (extract-response (chat @(:running-log assistant)))))
