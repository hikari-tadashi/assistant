(ns navi.plugins.calendar.core
  (:require [com.github.caldav4j/caldav4j :as caldav]
            [clojure.java.io :as io]
            [cheshire.core :as json]
            [org.httpkit.client :as http]
            [org.httpkit.server :as server]))

(defn get-daily-calendar [])

(def credentials-file-path "/credentials.json")
(def scopes ["https://www.googleapis.com/auth/calendar.readonly"])

(defn get-credentials []
  (let [client-secrets (json/parse-string (slurp (io/resource credentials-file-path)))
        flow (google-api-client.auth.oauth2.GoogleAuthorizationCodeFlow.
              (google-api-client.auth.oauth2.GoogleClientSecrets.
               {:web {:client_id (:client_id client-secrets)
                      :client_secret (:client_secret client-secrets)
                      :auth_uri "https://accounts.google.com/o/oauth2/auth"
                      :token_uri "https://accounts.google.com/o/oauth2/token"
                      :auth_provider_x509_cert_url "https://www.googleapis.com/oauth2/v1/certs"}}
               scopes))]
    (.setAccessType flow "offline")
    (.setApprovalPrompt flow "auto")
    (let [receiver (server/run-server (fn [req]
                                        (let [code (:code (server/request-params req))]
                                          (server/ok {:content-type "text/plain"
                                                      :body "Please close this window."})))
                                      {:port 8888})]
      (try
        (.load flow (io/file "tokens/calendar-quickstart.json"))
        (catch Exception _
          (let [url (.buildPromptUrl flow receiver)]
            (println "Open the following link in your browser:")
            (println url)
            (println "Enter verification code:")
            (flush)
            (let [code (read-line)]
              (.exchangeCode flow code receiver)))))
      (server/close-server! receiver))))

(defn main []
  (let [credentials (get-credentials)
        client (google-api-client
