(ns alfa.core.psql
  (:require [korma.db :refer [defdb]]
            [korma.core :refer :all]))

(def pdb {:subprotocol "postgresql"
          :subname "//localhost/macquest"
          :user "macquest"
          :password ""})

(defdb posdb pdb)

(defentity primes
  (database posdb)
  (entity-fields :number :status))

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

(defn add-primes
  [lim]
  (doseq [i (range 2 lim)]
    (insert primes
            (values {:id i :number i :status (prime? i)}))))

(defn get-prime
  [i]
  (first (select primes
                 (where {:number i}))))

(defn get-primes
  [lim]
  (select primes (limit lim)))



