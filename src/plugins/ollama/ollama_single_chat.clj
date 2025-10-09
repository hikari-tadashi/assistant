(ns plugins.ollama.ollama-single-chat
  (:require 
   [clojure.data.json :as json]
   ;; It looks like this library can be replaced w/ native JS functionality, making this application fully portable
   [clj-http.client :as client]))

;; Should this be moved to a utility
(defn convert-string-to-map [string]
  "convert a string to what the OpenAI style Ollama needs"
  (let [new-map {:role "user" :content string}]
    [new-map]))

;; a simple chat request, takes a string as input. meant to illustrate the foudational unit
(defn http-chat-completion [prompt]
  ; gemma-3-1b-it-qat
  (client/post "http://127.0.0.1:11434/v1/chat/completions" {:content-type :json
                                                             :form-params {:messages (convert-string-to-map prompt)
                                                                           :model "gemma3:1b"
                                                                           :stream false
                                                                           :max_tokens 8192}}))

;; a complex chat request, takes a json object as input, meant to illutrate the foundational philosopy
(defn http-custom-chat-completion [json-input]
  ; gemma-3-1b-it-qat
  (client/post "http://127.0.0.1:11434/v1/chat/completions" {:content-type :json
                                                             :form-params json-input}))

(defn extract-response [resp]
  (let [body (get resp :body)
        json-object (json/read-json body)
        content (get-in json-object [:choices 0 :message :content])]
    content))

;; companion to http-chat-completion
(defn ask [prompt]
  "input a string, get a string response from the default LLM"
  (extract-response (http-chat-completion prompt)))

;; companion to http-custom-chat-completion
(defn ask-custom-json [json-obj]
  "pass a custom json object that can be used for setting model parameters"
  (extract-response (http-custom-chat-completion json-obj)))