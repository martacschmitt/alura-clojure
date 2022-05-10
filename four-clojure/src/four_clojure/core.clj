(ns four-clojure.core)

; 1 - NOTHING BUT THE TRUTH
; https://4clojure.oxal.org/#/problem/1

(= true true)


; 2 - SIMPLE MATH
; https://4clojure.oxal.org/#/problem/2

(= (- 10 (* 2 3)) 4)


; 3 - STRINGS
; https://4clojure.oxal.org/#/problem/3

(= "HELLO WORLD" (.toUpperCase "hello world"))


; 4 - LISTS
; https://4clojure.oxal.org/#/problem/4

(= (list :a :b :c) '(:a :b :c))


; 5 - CONJ ON LISTS
; https://4clojure.oxal.org/#/problem/5

(= `(1 2 3 4) (conj '(2 3 4) 1))
(= `(1 2 3 4) (conj '(3 4) 2 1))


; 6 - VECTORS
; https://4clojure.oxal.org/#/problem/6

(= [:a :b :c] (list :a :b :c) (vec '(:a :b :c)) (vector :a :b :c))


; 7 - CONJ ON VECTORS
; https://4clojure.oxal.org/#/problem/7

(= [1 2 3 4] (conj [1 2 3] 4))
(= [1 2 3 4] (conj [1 2] 3 4))


; 8 - SETS
; https://4clojure.oxal.org/#/problem/8

(= #{:c :b :d :a} (set '(:a :a :b :c :c :c :c :d :d)))
(= #{:c :b :d :a} (clojure.set/union #{:a :b :c} #{:b :c :d}))


; 9 - CONJ ON SETS
; https://4clojure.oxal.org/#/problem/9

(= #{1 2 3 4} (conj #{1 4 3} 2))


; 10 - MAPS
; https://4clojure.oxal.org/#/problem/10

(= 20 ((hash-map :a 10, :b 20, :c 30) :b))
(= 20 (:b {:a 10, :b 20, :c 30}))