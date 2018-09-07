(ns miniredis-server.routes.api
  (:require [compojure.core :refer :all]
            [miniredis-server.miniredis.commands :as commands]))


(defn api-handler [params]
  (let [cmd (str (get params :command))]
    (commands/run cmd)))


(defroutes api-routes
  (GET "/api" {params :params} [] (api-handler params))
  (POST "/api" {params :params} [] (api-handler params)))

