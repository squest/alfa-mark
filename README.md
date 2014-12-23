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
  

  
Round 2 : 300 times rendering a page with 20 queries each http-request.  
  
Preliminary result   

"Elapsed time: 604.919688 msecs"
:jetty9 :selmer couchbase 378300  
"Elapsed time: 3885.75229 msecs"
:jetty9 :selmer mysql 378300  
"Elapsed time: 923.853082 msecs"
:jetty9 :selmer redis 378300  
"Elapsed time: 473.007546 msecs"
:jetty9 :hiccup couchbase 273600  
"Elapsed time: 4158.362238 msecs"
:jetty9 :hiccup mysql 273600  
"Elapsed time: 769.688038 msecs"
:jetty9 :hiccup redis 273600  
"Elapsed time: 1021.005418 msecs"
:jetty9 :enlive couchbase 328800  
"Elapsed time: 7747.257286 msecs"
:jetty9 :enlive mysql 328800  
"Elapsed time: 1628.95876 msecs"
:jetty9 :enlive redis 328800  
"Elapsed time: 714.613206 msecs"
:jetty9 :laser couchbase 413100  
"Elapsed time: 4296.988022 msecs"
:jetty9 :laser mysql 413100  
"Elapsed time: 1159.235924 msecs"
:jetty9 :laser redis 413100  

"Elapsed time: 453.98066 msecs"
:http-kit :selmer couchbase 378300  
"Elapsed time: 2392.419206 msecs"
:http-kit :selmer mysql 378300  
"Elapsed time: 763.88123 msecs"
:http-kit :selmer redis 378300  
"Elapsed time: 394.239746 msecs"
:http-kit :hiccup couchbase 273600  
"Elapsed time: 2596.736218 msecs"
:http-kit :hiccup mysql 273600  
"Elapsed time: 618.408036 msecs"
:http-kit :hiccup redis 273600  
"Elapsed time: 835.038888 msecs"
:http-kit :enlive couchbase 328800  
"Elapsed time: 5452.861826 msecs"
:http-kit :enlive mysql 328800  
"Elapsed time: 1506.332592 msecs"
:http-kit :enlive redis 328800  
"Elapsed time: 642.54605 msecs"
:http-kit :laser couchbase 413100  
"Elapsed time: 2744.846044 msecs"
:http-kit :laser mysql 413100  
"Elapsed time: 1004.650072 msecs"
:http-kit :laser redis 413100    
  
"Elapsed time: 339.553257 msecs"
:undertow :selmer couchbase 378300  
"Elapsed time: 3744.896082 msecs"
:undertow :selmer mysql 378300  
"Elapsed time: 696.91626 msecs"
:undertow :selmer redis 378300  
"Elapsed time: 478.389282 msecs"
:undertow :hiccup couchbase 273600  
"Elapsed time: 4308.705594 msecs"
:undertow :hiccup mysql 273600  
"Elapsed time: 696.853006 msecs"
:undertow :hiccup redis 273600  
"Elapsed time: 792.049436 msecs"
:undertow :enlive couchbase 328800  
"Elapsed time: 7501.820148 msecs"
:undertow :enlive mysql 328800  
"Elapsed time: 1615.149756 msecs"
:undertow :enlive redis 328800  
"Elapsed time: 613.540148 msecs"
:undertow :laser couchbase 413100  
"Elapsed time: 3825.39754 msecs"
:undertow :laser mysql 413100  
"Elapsed time: 925.44777 msecs"
:undertow :laser redis 413100   

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
