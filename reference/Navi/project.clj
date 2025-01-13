(defproject netnavi "0.0.1-CLI"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
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
  :main ^:skip-aot navi.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
