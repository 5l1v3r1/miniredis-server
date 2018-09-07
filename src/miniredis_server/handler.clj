(ns miniredis-server.handler
  (:require [compojure.core :refer [defroutes routes]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [hiccup.middleware :refer [wrap-base-url]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [miniredis-server.routes.home :refer [home-routes]]
            [miniredis-server.routes.api :refer [api-routes]]))

(defn init []
  (println "miniredis-server is starting"))

(defn destroy []
  (println "miniredis-server is shutting down"))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (routes home-routes api-routes app-routes)
      (handler/site)
      (wrap-base-url)))
