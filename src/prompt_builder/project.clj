;; File: project.clj

(defproject prompt-builder "0.1.0-SNAPSHOT"
  :description "Generates LLM prompts from INI data and agent templates."
  :url "http://example.com/FIXME" ; Optional: Replace with your project URL
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"} ; Choose your license
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojure/tools.cli "1.0.219"] ; For more robust arg parsing (optional)
                 [clojure-ini "0.0.2"]] ; Using a library for INI parsing
  :main ^:skip-aot prompt-builder.core ; Specify the main namespace
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
 