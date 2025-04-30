;; File: src/prompt_builder/processor.clj
(ns prompt-builder.processor
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
            [clojure.java.shell :as shell]))

(defn- execute-command [command-str]
  "Executes a shell command and returns its stdout. Handles errors."
  (try
    (let [result (shell/sh "bash" "-c" command-str)] ; Execute using bash
      (if (= 0 (:exit result))
        (str/trim (:out result))
        (do
          (println (str "Error executing command '" command-str "': " (:err result)))
          (str "ERROR: Command failed: " command-str)))) ; Return error message in prompt
    (catch Exception e
      (println (str "Exception executing command '" command-str "': " (.getMessage e)))
      (str "ERROR: Exception during command execution: " command-str))))

(defn- process-value [value-str]
  "Processes a value string, executing any embedded commands like ${command}."
  ;; Simple regex to find ${...} commands
  (str/replace value-str #"\$\{(.*?)\}"
               (fn [[match cmd]]
                 (execute-command cmd))))

(defn- get-value-from-data [data group key]
  "Safely retrieves a value from the nested data map."
  (get-in data [(keyword group) (keyword key)])) ; Use keywords for lookup

(defn process-template
  "Processes an agent template file to build the final prompt string."
  [agents-dir template-name data]
  (let [template-file (io/file agents-dir template-name)]
    (if-not (.exists template-file)
      (str "Error: Template file not found: " (.getPath template-file))
      (with-open [reader (io/reader template-file)]
        (->> (line-seq reader)
             (map str/trim)
             (filter #(not (str/blank? %))) ; Ignore empty lines
             (map #(str/split % #"/" 2)) ; Split "GroupName/KeyName"
             (map (fn [[group key]]
                    (if (and group key)
                      (let [raw-value (get-value-from-data data group key)]
                        (if raw-value
                          (process-value raw-value) ; Process value for commands
                          (do
                            (println (str "Warning: Key not found in data - " group "/" key))
                            ""))) ; Return empty string for missing keys
                      (do
                        (println (str "Warning: Invalid line format in template '" template-name "': " group (when key (str "/" key))))
                        "")))) ; Handle invalid lines
             (str/join "\n")))))) ; Join processed values with newlines
