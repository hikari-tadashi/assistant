(ns navi.base.chatbot.thread  
    (:require [clojure.data.json :as json]
             [clj-http.client :as client]))

; Hold all available threads
(def ids [])

; Hold the currently available thread
(def current-thread (atom ""))

; make a new thread
(defn make-new-thread []
  ; I need to centralize this url
  (client/post "http://192.168.68.70:7000" {:content-type :json
                                                   :form-params {:object "thread"
                                                                 :title "Thread title"
                                                                 :assistants ["jan"]
                                                                 :metadata {}}}))
; get the id from a thread response
(defn get-thread-id [response]
  (let [body (get response :body)
        formatted-json (json/read-json body)
        id (get formatted-json :id)]
    id))

; update the value of the currently tracke thread
;(reset! current-thread (get-thread-id (make-new-thread)))

(defn send-message [thread-id]
  ; I need to centralize this url
  (client/post (str "http://192.168.68.70:7000/v1/threads/" thread-id "/messages") {:content-type :json
                                                                                :streaming? false
                                                                                :form-params {:role "user"
                                                                                              :content "How does AI work? Explain it in simple terms."}}))