# Alfa-benchmark

Benchmarking several clojure libraries for web development and their combinations  
- Servers : http-kit, jetty9, undertow (through immutant2)  
- Templating : selmer, hiccup, enlive, laser  
- Database : couchdb (clutch), couchbase (couchbase-clj), mysql (korma), redis (carmine)  

Couchdb isn't included in some of the test since it's very slow for this kind of benchmark, it's better suited to other things.

## The challenges

First challenge: 1000 http-requests each rendering a page containing 30 data points taken from 1 query.

Preliminary results  

(["505ms" :http-kit :hiccup "mysql"]  
 ["510ms" :http-kit :selmer "mysql"]  
 ["519ms" :jetty9 :hiccup "mysql"]  
 ["530ms" :jetty9 :selmer "mysql"]  
 ["534ms" :undertow :selmer "mysql"]  
 ["575ms" :undertow :hiccup "mysql"]  
 ["1266ms" :jetty9 :selmer "couchbase"]  
 ["1272ms" :http-kit :selmer "couchbase"]  
 ["1274ms" :http-kit :laser "mysql"]  
 ["1275ms" :undertow :laser "mysql"]  
 ["1275ms" :jetty9 :laser "mysql"]  
 ["1313ms" :undertow :selmer "couchbase"]  
 ["1401ms" :jetty9 :hiccup "couchbase"]  
 ["1455ms" :undertow :hiccup "couchbase"]  
 ["1816ms" :http-kit :hiccup "couchbase"]  
 ["1929ms" :jetty9 :enlive "mysql"]  
 ["1944ms" :http-kit :enlive "mysql"]  
 ["2024ms" :undertow :enlive "mysql"]  
 ["2037ms" :undertow :laser "couchbase"]  
 ["2118ms" :http-kit :laser "couchbase"]  
 ["3113ms" :http-kit :selmer "redis"]  
 ["3327ms" :http-kit :enlive "couchbase"]  
 ["3405ms" :http-kit :hiccup "redis"]  
 ["3482ms" :undertow :selmer "redis"]  
 ["3586ms" :jetty9 :enlive "couchbase"]  
 ["3986ms" :undertow :enlive "couchbase"]  
 ["4078ms" :http-kit :laser "redis"]  
 ["4960ms" :jetty9 :enlive "redis"]  
 ["5390ms" :jetty9 :laser "couchbase"]  
 ["5831ms" :undertow :laser "redis"]  
 ["6387ms" :undertow :hiccup "redis"]  
 ["8178ms" :http-kit :enlive "redis"]  
 ["21847ms" :undertow :enlive "redis"]  
 ["30433ms" :jetty9 :selmer "redis"]  
 ["31410ms" :jetty9 :hiccup "redis"]  
 ["31892ms" :jetty9 :laser "redis"])  
  

  
Round 2 : 300 times rendering a page with 10 data points grabbed from 1 query per http-request.  
  
Preliminary result   

(["85ms" :jetty9 :hiccup "couchbase"]  
 ["87ms" :http-kit :hiccup "couchbase"]  
 ["91ms" :undertow :hiccup "couchbase"]  
 ["97ms" :http-kit :selmer "couchbase"]  
 ["100ms" :jetty9 :selmer "couchbase"]  
 ["101ms" :undertow :selmer "couchbase"]  
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

(["1681ms" :http-kit :selmer "couchbase"]  
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
  
## License

Copyright © 2014 PT Zenius Education
