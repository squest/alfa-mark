(ns alfa.core.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [compojure.handler :refer [site]]
            [ring.adapter.jetty9 :refer [run-jetty]]
            [org.httpkit.server :refer [run-server]]
            [org.httpkit.client :as http]
            [selmer.parser :as selmer]
            [hiccup.core :as hc]
            [taoensso.carmine :as car :refer [wcar]]
            [net.cgrand.enlive-html :as html]
            [couchbase-clj.client :as cb]
            [me.raynes.laser :as las]
            [com.ashafa.clutch :as cl]
            [immutant.web :as web]
            [korma.db :as sql]
            [korma.core :as korma]
            [clojure.string :as cs]
            [alfa.core.psql :refer [get-prime get-primes]]))

(def conns {:pool {} :spec {:host "localhost" :port 6379}})

(sql/defdb msqldb (sql/mysql {:db "test"
                              :user "squest"
                              :password ""}))

(declare number status)

(korma/defentity primes
  (korma/database msqldb)
  (korma/entity-fields :number :status))

(defn timing
  [con]
  (let [start# (System/nanoTime)
        place (eval con)]
    [place (quot (- (System/nanoTime) start#) 1000000)]))


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
  [times lim servers]
  (let [k {3000 :http-kit 3001 :undertow 3002 :jetty9}]
    (doseq [t [:selmer :hiccup :enlive :laser]
            d ["couchbase" "mysql" "redis" "postgres"]
            s servers]
      (println (k s) t d
               (timing `(reduce +
                                (pmap #(do % (count (get-tmp ~t ~lim ~s ~d)))
                                      (range ~times)))) "ms"))))

(defn sort-all
  [times lim servers]
  (->> (vector (timing
                `(reduce +
                         (pmap #(do % (count (get-tmp ~t ~lim ~s ~d)))
                               (range ~times))))
               (k s) t d)
       
       (for [t [:selmer :hiccup :enlive :laser]
             d ["couchbase" "mysql" "redis" "postgres"]
             s servers])
       (let [k {3000 :http-kit 3001 :undertow 3002 :jetty9}])
       (sort-by #(second (first %)))
       (pmap #(vec (cons (str (second (first %)) "ms") (rest %))))))

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
              :couchbase (pmap #(cb/get-json
                                 cbd
                                 (keyword (str "num" %))) (range 2 lim))
              :couchdb (pmap :doc
                             (cl/all-documents couchdb
                                               {:keys (map #(str "prime" %)
                                                           (range 2 lim))
                                                :include_docs true}))
              :mysql (pmap #(first (korma/select primes
                                                 (korma/where {:number %})))
                           (range 2 lim))
              :postgres (pmap get-prime (range 2 lim))
              :redis (pmap #(wcar conns
                                  (car/get %))
                           (range 2 lim)))})

(defn webdata-single
  [lim dbase]
  {:template "Selmer/hiccup/enlive/laser"
   :numbers (condp = dbase
              :couchbase (vals
                          (cb/get-multi-json
                           cbd
                           (pmap #(str "num" %) (range 2 lim))))
              :couchdb (pmap :doc
                             (cl/all-documents couchdb
                                               {:keys (map #(str "prime" %)
                                                           (range 2 lim))
                                                :include_docs true}))
              :postgres (get-primes lim)
              :mysql (korma/select primes
                                   (korma/limit lim))
              :redis (pmap #(wcar conns
                                  (car/get %))
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
               (:numbers (webdata-single lim dbase)))]
     [:footer
      [:hr]
      [:p "Copyright PT Zenius Education"]]]]))

(defn lasertmp
  [lim dbase]
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
                                 (:numbers (webdata-single lim dbase)))))))

(html/deftemplate tmpenlive "templates/base.html"
  [lim dbase] 
  [:#firstvar] (html/content (str "The first variable " "this"))
  [:#secondvar] (html/content
                 (html/html
                  (pmap #(vector
                          :li
                          (str "The number "
                               (:number %)
                               " is prime? "
                               (:status %)))
                        (:numbers (webdata-single lim dbase))))))

(defn enlivetmp
  [lim dbase]
  (reduce str (tmpenlive lim dbase)))

(defn homepage
  [template? lim dbase]
  (condp = template?
    :selmer (selmer/render-file
             (str dir "index.html")
             (webdata-single lim dbase))
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


(defn start-all
  []
  (do (start-jetty)
      (start-undertow)
      (httpkit)))
