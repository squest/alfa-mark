# Alfa-benchmark

Benchmarking several clojure libraries for web development and their combinations  
- Servers : http-kit, jetty9, immutant2 (using undertow)  
- Templating : selmer, hiccup, enlive, laser  
- Database : couchdb (using clutch), couchbase (using couchbase-clj)  

## The challenges

First challenge: Render a page with a size-20 loop content grabbed from a database (20 queries) serving 100 requests each time.  

## Preliminary results

The numbers at the end indicates the number of total characters generated by the template  

"Elapsed time: 282.691848 msecs"
:httpkit :selmer couchbase 126100  
"Elapsed time: 6439.976232 msecs"
:httpkit :selmer couchdb 126100  
"Elapsed time: 106.188932 msecs"
:httpkit :hiccup couchbase 91200  
"Elapsed time: 6488.098168 msecs"
:httpkit :hiccup couchdb 91200  
"Elapsed time: 284.794812 msecs"
:httpkit :enlive couchbase 138400  
"Elapsed time: 12101.072032 msecs"
:httpkit :enlive couchdb 138400  
"Elapsed time: 203.002256 msecs"
:httpkit :laser couchbase 137700  
"Elapsed time: 5729.288974 msecs"
:httpkit :laser couchdb 137700  
"Elapsed time: 312.23334 msecs"
:undertow :selmer couchbase 126100  
"Elapsed time: 8522.080004 msecs"
:undertow :selmer couchdb 61789  
"Elapsed time: 120.891256 msecs"
:undertow :hiccup couchbase 91200  
"Elapsed time: 8570.529116 msecs"
:undertow :hiccup couchdb 41952  
"Elapsed time: 281.330734 msecs"
:undertow :enlive couchbase 138400  
"Elapsed time: 10118.394098 msecs"
:undertow :enlive couchdb 94112  
"Elapsed time: 191.589084 msecs"
:undertow :laser couchbase 137700  
"Elapsed time: 9584.527824 msecs"
:undertow :laser couchdb 50949  
"Elapsed time: 142.489832 msecs"
:jetty9 :selmer couchbase 126100  
"Elapsed time: 7318.132402 msecs"
:jetty9 :selmer couchdb 68254  
"Elapsed time: 112.307118 msecs"
:jetty9 :hiccup couchbase 91200  
"Elapsed time: 9033.375958 msecs"
:jetty9 :hiccup couchdb 50320  
"Elapsed time: 309.22547 msecs"
:jetty9 :enlive couchbase 138400  
"Elapsed time: 9110.190098 msecs"
:jetty9 :enlive couchdb 65536  
"Elapsed time: 226.440632 msecs"
:jetty9 :laser couchbase 137700  
"Elapsed time: 9327.615668 msecs"
:jetty9 :laser couchdb 62100  

## License

Copyright © 2014 PT Zenius Education
