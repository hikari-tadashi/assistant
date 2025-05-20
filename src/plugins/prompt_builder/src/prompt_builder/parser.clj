;; File: src/prompt_builder/parser.clj
(ns prompt-builder.parser
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure-ini.core :as ini]) ; Import the INI parsing library
  (:import [java.io File]))

(defn- list-files [dir-path extension]
  "Lists files with a specific extension in a directory."
  (->> (io/file dir-path)
       (.listFiles)
       (filter #(and (.isFile %) (str/ends-with? (.getName %) extension)))))

(defn load-ini-data
  "Loads and merges data from all .ini files in the specified directory."
  [data-dir]
  (let [ini-files (list-files data-dir ".ini")]
    (if (empty? ini-files)
      (do
        (println (str "Warning: No .ini files found in directory: " data-dir))
        {}) ; Return empty map if no files found
      (->> ini-files
           (map #(ini/read-ini % :keywordize? true)) ; Read each INI file, keywordize keys
           (apply merge-with merge))))) ; Deep merge maps from all files
