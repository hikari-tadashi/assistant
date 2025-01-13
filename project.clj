(defproject assistant "0.0.1-CLI"
  :description "A command line assistant, to be wrapped in a web interface eventually"
  :url "https://github.com/Tadashi-Hikari/assistant"
  :license {:name "MIT"
            :url "https://www.google.com"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojure/data.json "0.2.6"]
                 [net.clojars.wkok/openai-clojure "0.9.0"]
                 [org.clojure/tools.cli "1.0.219"]
                 [clj-http "3.13.0"]
                 ;; https://mvnrepository.com/artifact/com.google.oauth-client/google-oauth-client
                 [com.google.oauth-client/google-oauth-client "1.36.0"]
                 ;; https://mvnrepository.com/artifact/com.google.api-client/google-api-client
                 [com.google.api-client/google-api-client "2.5.0"]
                 ;; https://mvnrepository.com/artifact/com.google.api-client/google-api-client
                 [com.google.api-client/google-api-client "2.5.0"]]
  :main ^:skip-aot core.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})