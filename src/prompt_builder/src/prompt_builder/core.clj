;; File: src/prompt_builder/core.clj
(ns prompt-builder.core
  (:require [prompt-builder.parser :as parser]
            [prompt-builder.processor :as processor]
            [clojure.string :as str]
            [clojure.java.io :as io])
  (:gen-class))

(def ^:private data-directory "./data") ; Default data directory
(def ^:private agents-directory "./agents") ; Default agents directory

(defn- list-agent-templates
  "Lists the names of available agent templates."
  []
  (println "Available agent templates:")
  (let [agent-files (->> (io/file agents-directory)
                         (.listFiles)
                         (filter #(.isFile %))
                         (map #(.getName %)))]
    (if (empty? agent-files)
      (println (str " No templates found in " agents-directory))
      (doseq [fname agent-files]
        (println (str " - " fname)))))) ; Print filenames directly

(defn -main
  "Main entry point. Loads data, processes template or lists templates."
  [& args]
  (let [data (parser/load-ini-data data-directory)]
    (if (seq data) ; Proceed only if data was loaded successfully
      (if-let [template-name (first args)]
        (let [prompt (processor/process-template agents-directory template-name data)]
          (println prompt)) ; Print the generated prompt
        (list-agent-templates)) ; List templates if no arg provided
      (println "Failed to load INI data. Exiting."))))

;; Example Usage:
;; lein run                  # Lists available agents in ./agents/
;; lein run my_agent         # Processes ./agents/my_agent using data from ./data/*.ini

;; Example data/example.ini:
;; [User]
;; Name = Alice
;; Location = ${pwd} ; Example command execution
;;
;; [System]
;; Role = Assistant
;; Goal = Be helpful
;; Date = ${date +%Y-%m-%d}

;; Example agents/basic_agent:
;; System/Role
;; User/Name
;; System/Goal
;; User/Location
;; System/Date
