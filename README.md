# Alfa-benchmark

Benchmarking several clojure libraries for web development and their combinations  
- Servers : http-kit, jetty9, immutant2 (using undertow)  
- Templating : selmer, hiccup, enlive, laser  
- Database : couchdb (using clutch), couchbase (using couchbase-clj)  

## The challenges

First challenge: Render a page with a size-50 loop content grabbed from a database (20 queries except mysql just one query) serving 100 requests each time.  

## Preliminary results

The numbers at the end indicates the number of total characters generated by the template  

"Elapsed time: 1075.274722 msecs"
:httpkit :selmer couchbase 266400  
"Elapsed time: 5945.567568 msecs"
:httpkit :selmer couchdb 266400  
"Elapsed time: 409.857788 msecs"
:httpkit :selmer mysql 275700  
"Elapsed time: 968.743046 msecs"
:httpkit :selmer redis 266400  
"Elapsed time: 547.630942 msecs"
:httpkit :hiccup couchbase 192500  
"Elapsed time: 4815.062124 msecs"
:httpkit :hiccup couchdb 192500  
"Elapsed time: 198.547044 msecs"
:httpkit :hiccup mysql 199200  
"Elapsed time: 886.187708 msecs"
:httpkit :hiccup redis 192500  
"Elapsed time: 773.166378 msecs"
:httpkit :enlive couchbase 299700  
"Elapsed time: 6906.737012 msecs"
:httpkit :enlive couchdb 299700  
"Elapsed time: 318.760516 msecs"
:httpkit :enlive mysql 310400  
"Elapsed time: 1686.188196 msecs"
:httpkit :enlive redis 299700  
"Elapsed time: 830.342994 msecs"
:httpkit :laser couchbase 299000  
"Elapsed time: 6314.254386 msecs"
:httpkit :laser couchdb 299000  
"Elapsed time: 373.32215 msecs"
:httpkit :laser mysql 309700  
"Elapsed time: 1143.825308 msecs"
:httpkit :laser redis 299000  
"Elapsed time: 636.180718 msecs"
:undertow :selmer couchbase 266400  
"Elapsed time: 5081.251786 msecs"
:undertow :selmer couchdb 266400  
"Elapsed time: 291.120392 msecs"
:undertow :selmer mysql 275700  
"Elapsed time: 1266.146874 msecs"
:undertow :selmer redis 266400  
"Elapsed time: 431.181148 msecs"
:undertow :hiccup couchbase 192500  
"Elapsed time: 4244.442678 msecs"
:undertow :hiccup couchdb 192500  
"Elapsed time: 133.298336 msecs"
:undertow :hiccup mysql 199200  
"Elapsed time: 807.871746 msecs"
:undertow :hiccup redis 192500  
"Elapsed time: 766.107792 msecs"
:undertow :enlive couchbase 299700  
"Elapsed time: 7114.774212 msecs"
:undertow :enlive couchdb 299700  
"Elapsed time: 267.14924 msecs"
:undertow :enlive mysql 310400  
"Elapsed time: 1675.191078 msecs"
:undertow :enlive redis 299700  
"Elapsed time: 402.094728 msecs"
:undertow :laser couchbase 299000  
"Elapsed time: 3612.397776 msecs"
:undertow :laser couchdb 299000  
"Elapsed time: 298.947544 msecs"
:undertow :laser mysql 309700  
"Elapsed time: 750.77602 msecs"
:undertow :laser redis 299000  
"Elapsed time: 832.224986 msecs"
:jetty9 :selmer couchbase 266400  
"Elapsed time: 3551.477036 msecs"
:jetty9 :selmer couchdb 266400  
"Elapsed time: 166.867752 msecs"
:jetty9 :selmer mysql 275700  
"Elapsed time: 753.021578 msecs"
:jetty9 :selmer redis 266400  
"Elapsed time: 389.115156 msecs"
:jetty9 :hiccup couchbase 192500  
"Elapsed time: 3394.540996 msecs"
:jetty9 :hiccup couchdb 192500  
"Elapsed time: 177.333272 msecs"
:jetty9 :hiccup mysql 199200  
"Elapsed time: 748.630634 msecs"
:jetty9 :hiccup redis 192500  
"Elapsed time: 582.510476 msecs"
:jetty9 :enlive couchbase 299700  
"Elapsed time: 6819.479104 msecs"
:jetty9 :enlive couchdb 299700  
"Elapsed time: 295.394088 msecs"
:jetty9 :enlive mysql 310400  
"Elapsed time: 1363.856984 msecs"
:jetty9 :enlive redis 299700  
"Elapsed time: 712.553164 msecs"
:jetty9 :laser couchbase 299000  
"Elapsed time: 4223.608218 msecs"
:jetty9 :laser couchdb 299000  
"Elapsed time: 263.829264 msecs"
:jetty9 :laser mysql 309700  
"Elapsed time: 913.674994 msecs"
:jetty9 :laser redis 299000  

## License

Copyright © 2014 PT Zenius Education
