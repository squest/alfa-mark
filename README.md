# Alfa-benchmark

Benchmarking several clojure libraries for web development and their combinations  
- Servers : http-kit, jetty9, immutant2 (using undertow)  
- Templating : selmer, hiccup, enlive, laser  
- Database : couchdb (using clutch), couchbase (using couchbase-clj)  

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Preliminary results

"Elapsed time: 208.309164 msecs" :httpkit :selmer couchbase   
"Elapsed time: 2684.39471 msecs" :httpkit :selmer couchdb  
"Elapsed time: 138.66083 msecs" :httpkit :hiccup couchbase   
"Elapsed time: 2721.151796 msecs" :httpkit :hiccup couchdb  
"Elapsed time: 338.366428 msecs" :httpkit :enlive couchbase  
"Elapsed time: 5552.357592 msecs" :httpkit :enlive couchdb  
"Elapsed time: 164.40236 msecs" :httpkit :laser couchbase  
"Elapsed time: 2757.393252 msecs" :httpkit :laser couchdb  
"Elapsed time: 153.943818 msecs" :undertow :selmer couchbase  
"Elapsed time: 2710.305416 msecs" :undertow :selmer couchdb  
"Elapsed time: 137.992418 msecs" :undertow :hiccup couchbase  
"Elapsed time: 2717.68108 msecs" :undertow :hiccup couchdb  
"Elapsed time: 263.219628 msecs" :undertow :enlive couchbase  
"Elapsed time: 5351.05426 msecs" :undertow :enlive couchdb  
"Elapsed time: 344.837644 msecs" :undertow :laser couchbase   
"Elapsed time: 2716.392736 msecs" :undertow :laser couchdb  
"Elapsed time: 156.493912 msecs" :jetty9 :selmer couchbase  
"Elapsed time: 2840.36937 msecs" :jetty9 :selmer couchdb  
"Elapsed time: 112.843992 msecs" :jetty9 :hiccup couchbase  
"Elapsed time: 2799.596788 msecs" :jetty9 :hiccup couchdb  
"Elapsed time: 345.571684 msecs" :jetty9 :enlive couchbase  
"Elapsed time: 5510.156326 msecs" :jetty9 :enlive couchdb  
"Elapsed time: 276.539348 msecs" :jetty9 :laser couchbase  
"Elapsed time: 3389.100112 msecs" :jetty9 :laser couchdb  

## License

Copyright © 2014 PT Zenius Education
