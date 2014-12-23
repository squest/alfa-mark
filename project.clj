(defproject alfa "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.3.1"]
                 [ring/ring-defaults "0.1.3"]
                 [ring "1.3.2"]
                 [info.sunng/ring-jetty9-adapter "0.7.2"]
                 [http-kit "2.1.16"]
                 [ring-undertow-adapter "0.2.1"]
                 ;;[com.datomic/datomic-free "0.9.5078"]
                 [postgresql/postgresql "8.4-702.jdbc4"]
                 [selmer "0.7.7"]
                 [honeysql "0.4.3"]
                 [hiccup "1.0.5"]
                 [enlive "1.1.5"]
                 [me.shenfeng/mustache "1.1"]
                 [com.ashafa/clutch "0.4.0"]
                 [couchbase-clj "0.2.0"]
                 [com.taoensso/carmine "2.9.0"]
                 [me.raynes/laser "1.1.1"]
                 [org.immutant/web "2.0.0-beta1"]
                 [korma "0.4.0"]
                 [org.clojure/java.jdbc "0.3.6"]
                 [mysql/mysql-connector-java "5.1.25"]
                 [com.taoensso/carmine "2.9.0"]]
  
  :plugins [[lein-ring "0.8.13"]]
  :ring {:handler alfa.core.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})
