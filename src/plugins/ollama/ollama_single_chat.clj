(ns plugins.ollama.ollama-single-chat
  (:require 
   [clojure.data.json :as json]
   [clj-http.client :as client]))

(defn extract-response [resp]
  (let [body (get resp :body)
        json-object (json/read-json body)
        content (get-in json-object [:choices 0 :message :content])]
    content))

(defn convert-string-to-map [string]
  "convert a string to what the OpenAI style Ollama needs"
  (let [new-map {:role "user" :content string}]
    [new-map]))

;; Huh, I guess I just wrote this out directly
(defn http-chat-completion [prompt]
  ; gemma-3-1b-it-qat
  (client/post "http://127.0.0.1:11434/v1/chat/completions" {:content-type :json
                                                             :form-params {:messages (convert-string-to-map prompt)
                                                                           :model "gemma3:1b"
                                                                           :stream false
                                                                           :max_tokens 8192}}))
                                                                           
(defn ask [prompt]
  "input a string, get a string response from the default LLM"
  (extract-response (http-chat-completion prompt)))