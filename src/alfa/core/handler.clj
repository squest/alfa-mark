(ns alfa.core.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [compojure.handler :refer [site]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.adapter.jetty9 :refer [run-jetty]]
            [org.httpkit.server :refer [run-server]]
            [org.httpkit.client :as http]
            [ring.adapter.undertow :refer [run-undertow]]
            [selmer.parser :as selmer]
            [hiccup.core :as hc]
            [net.cgrand.enlive-html :as html]
            [couchbase-clj.client :as cb]
            [me.raynes.laser :as las]))

(defn gettmp
  [template? lim port]
  (condp = template?
    :selmer (:body @(http/get (str "http://localhost:" port "/selmer/" lim)))
    :hiccup (:body @(http/get (str "http://localhost:" port "/hiccup/" lim)))
    :laser (:body @(http/get (str "http://localhost:" port "/laser/" lim)))
    :enlive (:body @(http/get (str "http://localhost:" port "/enlive/" lim)))))

(cb/defclient cbd {:bucket "alfa"
                   :uris ["http://localhost:8091/pools"]})

(def dir "templates/")

(defn prime?
  [p]
  (cond
   (= p 2) true
   (even? p) false
   :else (let [lim (Math/sqrt p)]
           (loop [i (int 3)]
             (if (> i lim)
               true
               (if (zero? (rem p i))
                 false
                 (recur (+ i 2))))))))

(defn webdata
  [lim]
  {:template "Selmer/hiccup/enlive/laser"
   :numbers (pmap #(cb/get-json cbd (keyword (str "num" %)))
                  (range 2 lim))})

(defn hiccuptmp
  [lim]
  (hc/html
   [:head
    [:meta {:charset "UTF-8"}]
    [:title "Clojure benchmark"]]
   [:body
    [:center
     [:h1 "Clojure benchmark for several web development tools"]
     [:hr]
     [:h3 "This one is for the templating system"]
     [:h4 (str "Test the first variable" "weell")]
     [:ul (map #(vector :li
                        (str "Number "
                             (:number %)
                             " is prime? "
                             (:status %)))
               (:numbers (webdata lim)))]
     [:footer
      [:hr]
      [:p "Copyright PT Zenius Education"]]]]))

(defn lasertmp
  [lim]
  (las/document
   (las/parse (slurp (str "resources/" dir "base.html")))
   (las/id= "firstvar")
   (las/content (str "This is the first var " "this"))
   (las/id= "secondvar")
   (las/content (reduce str (map #(str "<li>"
                                       "The number "
                                       (:number %)
                                       " is prime? no? "
                                       (:status %)
                                       "</li>")
                                 (:numbers (webdata lim)))))))

(html/deftemplate tmpenlive "templates/base.html"
  [lim] 
  [:#firstvar] (html/content (str "The first variable " "this"))
  [:#secondvar] (html/content
                 (apply str (map #(str "<li>"
                                       "The number "
                                       (:number %)
                                       " is prime? no? "
                                       (:status %)
                                       "</li>")
                                 (:numbers (webdata lim))))))

(defn enlivetmp
  [lim]
  (reduce str (tmpenlive lim)))

(defn homepage
  [template? lim]
  (condp = template?
    :selmer (selmer/render-file
             (str dir "index.html")
             (webdata lim))
    :hiccup (hiccuptmp lim)
    :enlive (enlivetmp lim)
    :laser (lasertmp lim)))

(defroutes all-routes
  (GET "/:tmpl/:lim" [tmpl lim]
       (homepage (keyword tmpl)
                 (read-string lim)))
  (GET "/" [req] "hellow world")
  (route/files "/static/") 
  (route/not-found "<p>Page not found.</p>"))

(defonce server (atom nil))

(defn stop-server []
  (when-not (nil? @server)
    ;; graceful shutdown: wait 100ms for existing requests to be finished
    ;; :timeout is optional, when no timeout, stop immediately
    (@server :timeout 100)
    (reset! server nil)))

(defn httpkit []
  (reset! server (run-server (site all-routes) {:port 3000})))

(defonce undertow-server
  (atom nil))

(defn stop-undertow []
  (do (reset! undertow-server)
      (.stop @under-server)))

(defn start-undertow []
  (->> {:port 3001}
       (run-undertow (site all-routes))
       (reset! undertow-server)))

(defonce jetty-server
  (atom nil))

(defn start-jetty []
  (->> {:port 3002}
       (run-jetty (site all-routes))
       (reset! jetty-server)))

(defn stop-jetty []
  (do (.stop @jetty-server)
      (reset! jetty-server nil)))


