(ns navi.base.chatbot.core 
  (:require [clojure.data.json :as json]
            [clj-http.client :as client]))



(defn chat [input]
  (client/post "http://192.168.68.70:7000/v1/chat/completions" {:content-type :json
                                                                             :form-params {:messages [{:content "You are a helpful assistant."
                                                                                                       :role "system"} {:content input
                                                                                                                        :role "user"}]
                                                                                           :model "llama-3.2-8b-instruct"
                                                                                           :stream false
                                                                                           :max_tokens 4096
                                                                                           :frequency_penalty 0
                                                                                           :presence_penalty 0
                                                                                           :temperature 0.7
                                                                                           :top_p 0.95}}))
