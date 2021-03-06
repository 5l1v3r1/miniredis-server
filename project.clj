(defproject miniredis-server "0.1.0-SNAPSHOT"
  :description "A simple redis clone implemented in Clojure"
  :url "https://bitbucket.org/finixbit/miniredis-server"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.5.2"]
                 [hiccup "1.0.5"]
                 [ring-server "0.4.0"]]
  :plugins [[lein-ring "0.8.12"]]
  :ring {:handler miniredis-server.handler/app
         :init miniredis-server.handler/init
         :destroy miniredis-server.handler/destroy}
  :profiles
  {:uberjar {:aot :all}
   :production
   {:ring
    {:open-browser? false, :stacktraces? false, :auto-reload? false}}
   :dev
   {:dependencies [[ring-mock "0.1.5"] [ring/ring-devel "1.5.1"]]}})
