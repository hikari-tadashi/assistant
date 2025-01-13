(ns netnavi.plugins.chatgpt.gpt
  (:import [netnavi.assist Assistant]) 
  (:require [navi.plugins.chatbot.assist :as assistant]
            [netnavi.plugins.chatgpt.personalities.core :as personality]))

(require
 '[wkok.openai-clojure.api :as api]
 '[navi.plugins.chatbot.assist :as assistant]) 

; These two may not fit here, but was circular dependant in gpt.clj
(def empty-chat [{:role "system" :content personality/standard}])

; This should be moved to GPT Module
(def assistant (Assistant. (atom empty-chat)))

(defn format-prompt [prompt]
  (let [new-map {:role "user" :content prompt}]
    new-map))

(defn format-response [response]
  (let [new-map {:role "assistant" :content response}]
    new-map))

(defn log-prompt-update! [prompt]
  (swap! (:running-log assistant) conj (format-prompt prompt))
  prompt)

(defn log-response-update! [response]
  (swap! (:running-log assistant) conj (format-response response))
  response)

(defn add-new-message [message]
  (let [new-map {:role "user" :content message}]
    (conj empty-chat new-map)))

(defn quick-chat-with-assistant [message]
  (get-in (api/create-chat-completion {:model "gpt-3.5-turbo"
                                       :messages (add-new-message message)}) [:choices 0 :message :content]))

(defn chat-with-assistant 
  "Takes in a string, formats it for GPT, updates the atom! with the string prompt and response. prints response"
  [message]
  (add-new-message (log-prompt-update! message))
  (log-response-update! (get-in (api/create-chat-completion {:model "gpt-3.5-turbo"
                                       :messages @(:running-log assistant)}) [:choices 0 :message :content])))