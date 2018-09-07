(ns miniredis-server.routes.home
  (:require [compojure.core :refer :all]
            [miniredis-server.views.layout :as layout]))

(defn home []
  (layout/common [:h1 "Miniredis Http Server!"]))


(defroutes home-routes
  (GET "/" [] (home)))

