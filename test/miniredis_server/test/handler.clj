(ns miniredis-server.test.handler
  (:use clojure.test
        ring.mock.request
        miniredis-server.handler))

(deftest test-app
  (testing "main route"
    (let [response (app (request :get "/"))]
      (is (= (:status response) 200))
      (is (.contains (:body response) "Miniredis Http Server"))))

  ; (testing "api.GET key"
  ;   (let [response (app (request :get "/api?command=GET key"))]
  ;     (is (= (:status response) 200))
  ;     (is (.contains (:body response) "Miniredis Http Server!"))))

  (testing "not-found route"
    (let [response (app (request :get "/invalid"))]
      (is (= (:status response) 404)))))
