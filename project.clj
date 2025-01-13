(defproject assistant "0.0.1-CLI"
  :description "A command line assistant, to be wrapped in a web interface eventually"
  :url "https://github.com/Tadashi-Hikari/assistant"
  :license {:name "MIT"
            :url "https://www.google.com"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojure/data.json "0.2.6"]
                 [net.clojars.wkok/openai-clojure "0.9.0"]
                 [org.clojure/tools.cli "1.0.219"]
                 [clj-http "3.13.0"]]
  :main ^:skip-aot 
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})