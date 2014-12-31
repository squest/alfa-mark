(ns alfa.core.cl
  (:require [org.httpkit.client :as http]))

(defn timing
  [con]
  (let [start# (System/nanoTime)
        place (eval con)]
    [place (quot (- (System/nanoTime) start#) 1000000)]))

(defn bench-cl
  [times lim]
  (vector (str (timing
                `(pmap
                  #(do %
                       (count
                        (str
                         (:body
                          @(http/get ~(str "http://localhost:3000/times/"
                                          lim))))))
                  (range ~times)))
               "ms")
          :restas :cl-who "redis"))

(defn bench-side
  [times lim]
  (dotimes [i times]
    @(http/get (str "http://localhost:3000/times/" lim))))

(defn pbench
  [times lim]
  (reduce +
          (-> #(->> (:body @(http/get (str "http://localhost:3000/times/" lim)))
                    (str %) count)
              (pmap (range times)))))
