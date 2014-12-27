# Alfa-benchmark

Benchmarking several clojure libraries for web development and their combinations  
- Servers : http-kit, jetty9, undertow (through immutant2)  
- Templating : selmer, hiccup, enlive, laser  
- Database : couchdb (clutch), couchbase (couchbase-clj), mysql (korma), redis (carmine)  
- Common Lisp setup : servers restas (hunchentoot), wookie, templating cl-who, and redis

Couchdb isn't included in some of the test since it's very slow for this kind of benchmark, it's better suited to other things.

## The challenges

First challenge: 100 http-requests each rendering a page containing 30 data points taken from 1 query.

Preliminary results  

(["46ms" :restas :cl-who "redis"]  
 ["47ms" :http-kit :selmer "mysql"]  
 ["50ms" :jetty9 :selmer "mysql"]  
 ["55ms" :undertow :selmer "mysql"]  
 ["56ms" :http-kit :hiccup "mysql"]  
 ["62ms" :jetty9 :hiccup "mysql"]  
 ["63ms" :undertow :hiccup "mysql"]  
 ["122ms" :undertow :laser "mysql"]  
 ["125ms" :http-kit :hiccup "couchbase"]  
 ["127ms" :jetty9 :selmer "couchbase"]  
 ["129ms" :jetty9 :hiccup "couchbase"]  
 ["137ms" :http-kit :laser "mysql"]  
 ["144ms" :undertow :hiccup "couchbase"]  
 ["154ms" :undertow :selmer "couchbase"]  
 ["161ms" :jetty9 :laser "mysql"]  
 ["162ms" :http-kit :selmer "couchbase"]  
 ["172ms" :http-kit :enlive "mysql"]  
 ["181ms" :jetty9 :laser "couchbase"]  
 ["183ms" :jetty9 :enlive "mysql"]  
 ["186ms" :undertow :enlive "mysql"]  
 ["210ms" :undertow :laser "couchbase"]  
 ["237ms" :http-kit :laser "couchbase"]  
 ["312ms" :http-kit :enlive "couchbase"]  
 ["314ms" :undertow :enlive "couchbase"]  
 ["331ms" :http-kit :selmer "redis"]  
 ["337ms" :undertow :hiccup "redis"]  
 ["346ms" :http-kit :hiccup "redis"]  
 ["364ms" :undertow :selmer "redis"]  
 ["368ms" :jetty9 :hiccup "redis"]  
 ["369ms" :jetty9 :enlive "couchbase"]  
 ["378ms" :jetty9 :selmer "redis"]  
 ["426ms" :http-kit :laser "redis"]  
 ["468ms" :jetty9 :laser "redis"]  
 ["681ms" :undertow :laser "redis"]  
 ["759ms" :jetty9 :enlive "redis"]  
 ["775ms" :http-kit :enlive "redis"]  
 ["776ms" :undertow :enlive "redis"])  

  
Round 2 : 300 times rendering a page with 10 data points grabbed from 1 query per http-request.  
  
Preliminary result   

(["85ms" :jetty9 :hiccup "couchbase"]  
 ["87ms" :http-kit :hiccup "couchbase"]  
 ["91ms" :undertow :hiccup "couchbase"]  
 ["97ms" :http-kit :selmer "couchbase"]  
 ["100ms" :jetty9 :selmer "couchbase"]  
 ["101ms" :undertow :selmer "couchbase"]  
 ["110ms" :restas :cl-who "redis"]  
 ["111ms" :jetty9 :hiccup "mysql"]  
 ["118ms" :http-kit :selmer "mysql"]  
 ["118ms" :undertow :hiccup "mysql"]  
 ["120ms" :jetty9 :selmer "mysql"]  
 ["127ms" :http-kit :hiccup "redis"]  
 ["133ms" :http-kit :hiccup "mysql"]  
 ["135ms" :http-kit :selmer "redis"]  
 ["137ms" :undertow :selmer "mysql"]  
 ["138ms" :jetty9 :hiccup "redis"]  
 ["148ms" :jetty9 :selmer "redis"]  
 ["159ms" :undertow :hiccup "redis"]   
 ["163ms" :undertow :selmer "redis"]   
 ["213ms" :jetty9 :enlive "couchbase"]  
 ["215ms" :undertow :enlive "couchbase"]  
 ["242ms" :http-kit :enlive "couchbase"]  
 ["257ms" :http-kit :enlive "mysql"]  
 ["280ms" :jetty9 :enlive "mysql"]  
 ["281ms" :http-kit :enlive "redis"]  
 ["291ms" :http-kit :laser "couchbase"]  
 ["294ms" :undertow :laser "couchbase"]  
 ["308ms" :jetty9 :laser "couchbase"]  
 ["322ms" :undertow :laser "mysql"]  
 ["328ms" :http-kit :laser "mysql"]  
 ["329ms" :http-kit :laser "redis"]  
 ["335ms" :undertow :enlive "redis"]  
 ["340ms" :undertow :enlive "mysql"]  
 ["343ms" :undertow :laser "redis"]  
 ["355ms" :jetty9 :laser "mysql"]  
 ["370ms" :jetty9 :enlive "redis"]  
 ["372ms" :jetty9 :laser "redis"])  

Round 3 : Serving 1000 http-requests each rendering a page containing data that needs 30 queries each.  

(["431ms" :restas :cl-who "redis"]  
 ["1681ms" :http-kit :selmer "couchbase"]  
 ["1828ms" :http-kit :hiccup "couchbase"]  
 ["1851ms" :undertow :hiccup "couchbase"]  
 ["2005ms" :jetty9 :selmer "couchbase"]  
 ["2195ms" :undertow :selmer "couchbase"]  
 ["2631ms" :undertow :laser "couchbase"]  
 ["2760ms" :http-kit :laser "couchbase"]  
 ["3273ms" :http-kit :selmer "redis"]  
 ["3374ms" :jetty9 :hiccup "couchbase"]  
 ["3381ms" :jetty9 :laser "couchbase"]  
 ["3542ms" :undertow :selmer "redis"]  
 ["3682ms" :http-kit :hiccup "redis"]  
 ["3933ms" :undertow :hiccup "redis"]  
 ["4252ms" :jetty9 :selmer "redis"]  
 ["4688ms" :http-kit :laser "redis"]  
 ["4885ms" :jetty9 :enlive "redis"]  
 ["5050ms" :http-kit :enlive "couchbase"]  
 ["5193ms" :undertow :laser "redis"]  
 ["6995ms" :jetty9 :enlive "couchbase"]  
 ["8213ms" :undertow :enlive "couchbase"]  
 ["9908ms" :http-kit :enlive "redis"]  
 ["13904ms" :undertow :enlive "redis"]  
 ["13942ms" :http-kit :hiccup "mysql"]  
 ["14982ms" :http-kit :selmer "mysql"]  
 ["18919ms" :http-kit :laser "mysql"]  
 ["20029ms" :undertow :selmer "mysql"]  
 ["20677ms" :jetty9 :hiccup "mysql"]  
 ["20781ms" :jetty9 :selmer "mysql"]  
 ["21235ms" :jetty9 :laser "mysql"]  
 ["21669ms" :undertow :laser "mysql"]  
 ["23599ms" :undertow :hiccup "mysql"]  
 ["31733ms" :jetty9 :hiccup "redis"]  
 ["31825ms" :http-kit :enlive "mysql"]  
 ["32020ms" :jetty9 :laser "redis"]  
 ["42575ms" :undertow :enlive "mysql"]  
 ["44124ms" :jetty9 :enlive "mysql"])  
 
Round 4 : Nothing special, just added a new contender (postgresql), 300requests with 20queries (1 query for sql) per page.

(["120ms" :restas :cl-who "redis"]  
 ["173ms" :http-kit :hiccup "mysql"]  
 ["178ms" :http-kit :selmer "mysql"]  
 ["192ms" :jetty9 :hiccup "mysql"]  
 ["200ms" :undertow :selmer "mysql"]  
 ["204ms" :jetty9 :selmer "mysql"]  
 ["216ms" :undertow :hiccup "mysql"]  
 ["317ms" :http-kit :hiccup "couchbase"]  
 ["346ms" :jetty9 :hiccup "couchbase"]  
 ["361ms" :undertow :hiccup "couchbase"]  
 ["405ms" :undertow :laser "mysql"]  
 ["409ms" :http-kit :laser "mysql"]  
 ["420ms" :jetty9 :laser "mysql"]  
 ["465ms" :jetty9 :selmer "couchbase"]  
 ["480ms" :jetty9 :enlive "mysql"]  
 ["527ms" :http-kit :enlive "mysql"]  
 ["548ms" :undertow :enlive "mysql"]  
 ["560ms" :http-kit :selmer "couchbase"]  
 ["573ms" :jetty9 :laser "couchbase"]  
 ["597ms" :undertow :laser "couchbase"]  
 ["599ms" :http-kit :selmer "redis"]  
 ["617ms" :http-kit :hiccup "redis"]  
 ["640ms" :jetty9 :enlive "couchbase"]  
 ["698ms" :jetty9 :hiccup "redis"]  
 ["712ms" :undertow :selmer "couchbase"]  
 ["732ms" :undertow :hiccup "redis"]  
 ["769ms" :jetty9 :selmer "redis"]  
 ["796ms" :http-kit :enlive "couchbase"]  
 ["836ms" :undertow :enlive "couchbase"]  
 ["888ms" :undertow :laser "redis"]  
 ["894ms" :jetty9 :laser "redis"]  
 ["985ms" :http-kit :selmer "postgres"]  
 ["986ms" :undertow :selmer "redis"]  
 ["995ms" :undertow :hiccup "postgres"]  
 ["1062ms" :http-kit :hiccup "postgres"]  
 ["1093ms" :http-kit :laser "redis"]  
 ["1177ms" :jetty9 :selmer "postgres"]  
 ["1255ms" :http-kit :laser "postgres"]  
 ["1312ms" :undertow :laser "postgres"]  
 ["1318ms" :http-kit :laser "couchbase"]  
 ["1372ms" :jetty9 :laser "postgres"]  
 ["1428ms" :jetty9 :enlive "redis"]  
 ["1470ms" :jetty9 :hiccup "postgres"]  
 ["1480ms" :http-kit :enlive "redis"]  
 ["1594ms" :undertow :enlive "redis"]  
 ["2313ms" :undertow :selmer "postgres"]  
 ["2330ms" :http-kit :enlive "postgres"]  
 ["2450ms" :undertow :enlive "postgres"]  
 ["30041ms" :jetty9 :enlive "postgres"])  
 
 
### Conclusion so far:

Common Lisp stack is really-really FAST!!, and this is before using async server like woo or wookie.
On Clojure side, not much difference between the servers nor the templates (enlive is a bit slower yet have a good design, so just the right trade-off), however on the database side, it's obvious which database is suitable for what task then.  
  
## License

Copyright © 2014 PT Zenius Education
