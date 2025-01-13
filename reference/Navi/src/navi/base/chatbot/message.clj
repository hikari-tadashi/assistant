(ns navi.base.chatbot.message
  (:require [clojure.data.json :as json]
           [clj-http.client :as client]))


;(send-message @current-thread)

;(client/get (str "http://localhost:1337/v1/threads/" @current-thread))

;(json/read-json (get (client/get (str "http://localhost:1337/v1/threads/" @current-thread "/messages")) :body))

(defn get-response-message [thread]
  (let [body (get thread :body)
        json-object (json/read-json body)
        messages (get json-object :messages)
        last-message (last messages)]
    last-message))