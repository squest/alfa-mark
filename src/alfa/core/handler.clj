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
            [me.raynes.laser :as las]
            [com.ashafa.clutch :as cl]
            [immutant.web :as web]))

(defn get-tmp
  [template? lim port dbase]
  (condp = template?
    :selmer (:body @(http/get
                     (str "http://localhost:" port "/selmer/" lim "/" dbase)))
    :hiccup (:body @(http/get
                     (str "http://localhost:" port "/hiccup/" lim "/" dbase)))
    :laser (:body @(http/get
                    (str "http://localhost:" port "/laser/" lim "/" dbase)))
    :enlive (:body @(http/get
                     (str "http://localhost:" port "/enlive/" lim "/" dbase)))
    :broklin false))

(defn bench-all
  [times lim]
  (let [k {3000 :httpkit 3001 :undertow 3002 :jetty9}]
    (doseq [s [3000 3001 3002]
            t [:selmer :hiccup :enlive :laser]
            d ["couchbase" "couchdb"]]
      (println (k s) t d
               (time (reduce + (for [i (range times)]
                                 (count (get-tmp t lim s d)))))))))

(cb/defclient cbd {:bucket "alfa"
                   :uris ["http://localhost:8091/pools"]})

(def couchdb "kalfak")

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
  [lim dbase]
  {:template "Selmer/hiccup/enlive/laser"
   :numbers (condp = dbase
              :couchbase (pmap #(cb/get-json cbd (keyword (str "num" %)))
                               (range 2 lim))
              :couchdb (pmap #(cl/get-document couchdb
                                               (str "prime" %))
                             (range 2 lim)))})

(defn hiccuptmp
  [lim dbase]
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
               (:numbers (webdata lim dbase)))]
     [:footer
      [:hr]
      [:p "Copyright PT Zenius Education"]]]]))

(defn lasertmp
  [lim dbase]
  (las/document
   (las/parse-fragment (slurp (str "resources/" dir "base.html")))
   (las/id= "firstvar")
   (las/content (str "This is the first var " "this"))
   (las/id= "secondvar")
   (las/content (reduce str (map #(str "<li>"
                                       "The number "
                                       (:number %)
                                       " is prime? no? "
                                       (:status %)
                                       "</li>")
                                 (:numbers (webdata lim dbase)))))))

(html/deftemplate tmpenlive "templates/base.html"
  [lim dbase] 
  [:#firstvar] (html/content (str "The first variable " "this"))
  [:#secondvar] (html/content
                 (apply str (map #(str "<li>"
                                       "The number "
                                       (:number %)
                                       " is prime? no? "
                                       (:status %)
                                       "</li>")
                                 (:numbers (webdata lim dbase))))))

(defn enlivetmp
  [lim dbase]
  (reduce str (tmpenlive lim dbase)))

(defn homepage
  [template? lim dbase]
  (condp = template?
    :selmer (selmer/render-file
             (str dir "index.html")
             (webdata lim dbase))
    :hiccup (hiccuptmp lim dbase)
    :enlive (enlivetmp lim dbase)
    :laser (lasertmp lim dbase)))

(defroutes all-routes
  (GET "/:tmpl/:lim/:dbase" [tmpl lim dbase]
       (homepage (keyword tmpl)
                 (read-string lim)
                 (keyword dbase)))
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
  (do (web/stop @undertow-server)
      (reset! undertow-server nil)))

(defn start-undertow []
  (reset! undertow-server (web/run (site all-routes) {:port 3001})))

(defonce jetty-server
  (atom nil))

(defn start-jetty []
  (->> {:port 3002}
       (run-jetty (site all-routes))
       (reset! jetty-server)))

(defn stop-jetty []
  (do (.stop @jetty-server)
      (reset! jetty-server nil)))


