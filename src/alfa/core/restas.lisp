(ql:quickload (list "restas" "cl-who" "cl-redis" "drakma"))

(restas:define-module #:hello-resta
  (:use :cl :restas :who :redis))

(in-package :hello-resta)

(defun homepage ()
  "the homepage"
  (with-html-output-to-string (s)
    (:html
     (:head
      (:title "Hello world"))
     (:body
      (:header)
      (:center
       (:h1 "Hellow world from restas!"))
      (:footer)))))

(defun cstr (&rest args)
  (apply 'concatenate 'string
	 (mapcar #'(lambda (x) (if (stringp x) x
			      (write-to-string x)))
		 args)))

(defun get-all (times lim)
  (loop for i from 1 to times 
     summing (length (drakma:http-request (cstr "http://localhost:3000/primes/" lim)))
     into sum
     finally (return sum)))

(defun ignore-all (times lim)
  (dotimes (i times)
    (drakma:http-request (cstr "http://localhost:3000/primes/" lim))))

(defun primes-page (lim)
  "the homepage"
  (with-html-output-to-string (s)
    (:html
     (:head
      (:title "Hello world"))
     (:body
      (:header)
      (:center
       (let ((nama "jojon"))
	 (htm (:h1 (str (cstr "Hellow world from restas " nama)))))
       (:ul
	(mapcar
	 #'(lambda (x)
	     (let ((res (read-from-string (red:get (cstr "prime" x)))))
	       (htm (:li (str (cstr "The number "
				    (getf res 'number)
				    " is prime? no "
				    (getf res 'status)))))))
	 (loop for i from 2 to lim collect i))))
      (:footer)))))


(define-route hello-world ("")
  (homepage))

(define-route primes ("/primes/:lim")
  (primes-page (parse-integer lim)))

(defun run (port)
  (progn (start '#:hello-resta :port port)
	 (connect)
	 (concatenate 'string
		      "Restas is listening on port "
		      (write-to-string port))))

(defun prime? (p)
  (labels ((looper (i)
	      (if (> (* i i) p)
		  t
		  (if (zerop (rem p i))
		      nil
		      (looper (+ i 2))))))
    (cond ((= 2 p) t)
	  ((evenp p) nil)
	  (t (looper 3)))))

(defun into-redis (lim)
  (loop for i from 2 to lim 
     do (red:set (cstr "prime" i)
		 (list :number i :status (if (prime? i) "true" "false")))))

(defun stop ()
  (progn (disconnect)
	 (stop-all)))
