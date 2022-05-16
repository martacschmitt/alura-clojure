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


; 11 - CONJ ON MAPS
; https://4clojure.oxal.org/#/problem/11

(= {:a 1, :b 2, :c 3} (conj {:a 1} [:b 2] [:c 3]))


; 12 - SEQUENCES
; https://4clojure.oxal.org/#/problem/12

(= 3 (first '(3 2 1)))
(= 3 (second [2 3 4]))
(= 3 (last (list 1 2 3)))


; 13 - REST
; https://4clojure.oxal.org/#/problem/13

(= [20 30 40] (rest [10 20 30 40]))


; 14 - FUNCTIONS
; https://4clojure.oxal.org/#/problem/14

(= 8 ((fn add-five [x] (+ x 5)) 3))
(= 8 ((fn [x] (+ x 5)) 3))
(= 8 (#(+ % 5) 3))
(= 8 ((partial + 5) 3))


; 15 - DOUBLE DOWN
; https://4clojure.oxal.org/#/problem/15

(= (* 2 2) 4)
(= (* 2 3) 6)
(= (* 2 11) 22)
(= (* 2 7) 14)


; 16 - HELLO WORLD
; https://4clojure.oxal.org/#/problem/16

((fn hello-world
  [name]
  (apply str (concat "Hello, " name "!"))) "Dave")

(= ((fn hello-world [name] (apply str (concat "Hello, " name "!"))) "Dave") "Hello, Dave!")
(= ((fn hello-world [name] (apply str (concat "Hello, " name "!"))) "Jenn") "Hello, Jenn!")
(= ((fn hello-world [name] (apply str (concat "Hello, " name "!"))) "Rhea") "Hello, Rhea!")


; 17 - MAP
; https://4clojure.oxal.org/#/problem/17

(= '(6 7 8) (map #(+ % 5) '(1 2 3)))


; 18 - FILTER
; https://4clojure.oxal.org/#/problem/18

(= '(6 7) (filter #(> % 5) '(3 4 5 6 7)))


; 19 - LAST ELEMENT
; https://4clojure.oxal.org/#/problem/19

(= (last [1 2 3 4 5]) 5)
(= (last '(5 4 3)) 3)
(= (last ["b" "c" "d"]) "d")


; 20 - PENULTIMATE ELEMENT
; https://4clojure.oxal.org/#/problem/20

((fn penultimate-element
  [elements]
  (second (reverse elements))) [1 2 3 4 5])

(= ((fn penultimate-element [elements] (second (reverse elements))) (list 1 2 3 4 5)) 4)
(= ((fn penultimate-element [elements] (second (reverse elements))) ["a" "b" "c"]) "b")
(= ((fn penultimate-element [elements] (second (reverse elements))) [[1 2] [3 4]]) [1 2])


; 21 - NTH ELEMENT
; https://4clojure.oxal.org/#/problem/21

(= (nth '(4 5 6 7) 2) 6)
(= (nth [:a :b :c] 0) :a)
(= (nth [1 2 3 4] 1) 2)
(= (nth '([1 2] [3 4] [5 6]) 2) [5 6])


; 22 - COUNT A SEQUENCE
; https://4clojure.oxal.org/#/problem/22

(= (count '(1 2 3 3 1)) 5)
(= (count "Hello World") 11)
(= (count [[1 2] [3 4] [5 6]]) 3)
(= (count '(13)) 1)
(= (count '(:a :b :c)) 3)


; 23 - REVERSE A SEQUENCE
; https://4clojure.oxal.org/#/problem/23

(= (reverse [1 2 3 4 5]) [5 4 3 2 1])
(= (reverse (sorted-set 5 7 2 7)) '(7 5 2))
(= (reverse [[1 2][3 4][5 6]]) [[5 6][3 4][1 2]])


; 24 - SUM IT ALL UP
; https://4clojure.oxal.org/#/problem/24

(= (reduce + [1 2 3]) 6)
(= (reduce + (list 0 -2 5 5)) 8)
(= (reduce + #{4 2 1}) 7)
(= (reduce + '(0 0 -1)) -1)
(= (reduce + '(1 10 3)) 14)


; 25 - FIND THE ODD NUMBERS
; https://4clojure.oxal.org/#/problem/25

(= (filter odd? #{1 2 3 4 5}) '(1 3 5))
(= (filter odd? [4 2 1 6]) '(1))
(= (filter odd? [2 2 4 6]) '())
(= (filter odd? [1 1 1 3]) '(1 1 1 3))


; 26 - FIBONACCI SEQUENCE
; https://4clojure.oxal.org/#/problem/26

; 1 1 2 3 5 8 ...

((fn [x]
   (loop [iter 1 fib [1]]
     (if (= iter x)
       fib
       (recur (inc iter)
              (reduce #(conj %2 (+ (if (< (count %2) 2) 0 (second (reverse %2))) (last %2))) [[] fib]))))) 10)

(= ((fn [x]
      (loop [iter 1 fib [1]]
        (if (= iter x)
          fib
          (recur (inc iter)
                 (reduce #(conj %2 (+ (if (< (count %2) 2) 0 (second (reverse %2))) (last %2))) [[] fib]))))) 3) '(1 1 2))
(= ((fn [x]
      (loop [iter 1 fib [1]]
        (if (= iter x)
          fib
          (recur (inc iter)
                 (reduce #(conj %2 (+ (if (< (count %2) 2) 0 (second (reverse %2))) (last %2))) [[] fib]))))) 6) '(1 1 2 3 5 8))
(= ((fn [x]
      (loop [iter 1 fib [1]]
        (if (= iter x)
          fib
          (recur (inc iter)
                 (reduce #(conj %2 (+ (if (< (count %2) 2) 0 (second (reverse %2))) (last %2))) [[] fib]))))) 8) '(1 1 2 3 5 8 13 21))



; 27 - PALINDROME DETECTOR
; https://4clojure.oxal.org/#/problem/27

((fn [x]
  (= (apply str x) (apply str (reverse x)))) '(:a :b :c))


(false? (#(= (apply str %) (apply str (reverse %))) '(1 2 3 4 5)))
(true? (#(= (apply str %) (apply str (reverse %))) "racecar"))
(true? (#(= (apply str %) (apply str (reverse %))) [:foo :bar :foo]))
(true? (#(= (apply str %) (apply str (reverse %))) '(1 1 3 3 1 1)))
(false? (#(= (apply str %) (apply str (reverse %))) '(:a :b :c)))


; 28 - FLATTEN A SEQUENCE
; https://4clojure.oxal.org/#/problem/28

(= (flatten '((1 2) 3 [4 [5 6]])) '(1 2 3 4 5 6))
(= (flatten ["a" ["b"] "c"]) '("a" "b" "c"))
(= (flatten '((((:a))))) '(:a))


; 29 - GET THE CAPS
; https://4clojure.oxal.org/#/problem/29

(fn [s]
  (apply str (filter #(Character/isUpperCase %) s)))

(= ((fn [s] (apply str (filter #(Character/isUpperCase %) s))) "HeLlO, WoRlD!") "HLOWRD")
(empty? ((fn [s] (apply str (filter #(Character/isUpperCase %) s))) "nothing"))
(= ((fn [s] (apply str (filter #(Character/isUpperCase %) s))) "$#A(*&987Zf") "AZ")


; 30 - COMPRESS A SEQUENCE
; https://4clojure.oxal.org/#/problem/30

#(map first (partition-by identity %))

; outra alternativa
(dedupe [1 1 2 3 3 2 2 3])

(= (apply str (#(map first (partition-by identity %)) "Leeeeeerrroyyy")) "Leroy")
(= (#(map first (partition-by identity %)) [1 1 2 3 3 2 2 3]) '(1 2 3 2 3))
(= (#(map first (partition-by identity %)) [[1 2] [1 2] [3 4] [1 2]]) '([1 2] [3 4] [1 2]))


; 31 - PACK A SEQUENCE
; https://4clojure.oxal.org/#/problem/31

(partition-by identity [1 1 2 1 1 1 3 3])

(= (partition-by identity [1 1 2 1 1 1 3 3]) '((1 1) (2) (1 1 1) (3 3)))
(= (partition-by identity [:a :a :b :b :c]) '((:a :a) (:b :b) (:c)))
(= (partition-by identity [[1 2] [1 2] [3 4]]) '(([1 2] [1 2]) ([3 4])))


; 32 - DUPLICATE A SEQUENCE
; https://4clojure.oxal.org/#/problem/32

(fn [s] (apply concat (map #(apply concat (repeat 2 %)) (partition-by identity s))))

(= ((fn [s] (apply concat (map #(apply concat (repeat 2 %)) (partition-by identity s)))) [1 2 3]) '(1 1 2 2 3 3))
(= ((fn [s] (apply concat (map #(apply concat (repeat 2 %)) (partition-by identity s)))) [:a :a :b :b]) '(:a :a :a :a :b :b :b :b))
(= ((fn [s] (apply concat (map #(apply concat (repeat 2 %)) (partition-by identity s)))) [[1 2] [3 4]]) '([1 2] [1 2] [3 4] [3 4]))
(= ((fn [s] (apply concat (map #(apply concat (repeat 2 %)) (partition-by identity s)))) [44 33]) [44 44 33 33])


; 33 - REPLICATE A SEQUENCE
; https://4clojure.oxal.org/#/problem/33

(fn [s times] (apply concat (map #(apply concat (repeat times %)) (partition-by identity s))))

(= ((fn [s times] (apply concat (map #(apply concat (repeat times %)) (partition-by identity s)))) [1 2 3] 2) '(1 1 2 2 3 3))
(= ((fn [s times] (apply concat (map #(apply concat (repeat times %)) (partition-by identity s)))) [:a :b] 4) '(:a :a :a :a :b :b :b :b))
(= ((fn [s times] (apply concat (map #(apply concat (repeat times %)) (partition-by identity s)))) [4 5 6] 1) '(4 5 6))
(= ((fn [s times] (apply concat (map #(apply concat (repeat times %)) (partition-by identity s)))) [[1 2] [3 4]] 2) '([1 2] [1 2] [3 4] [3 4]))
(= ((fn [s times] (apply concat (map #(apply concat (repeat times %)) (partition-by identity s)))) [44 33] 2) [44 44 33 33])


; 34 - IMPLEMENT RANGE
; https://4clojure.oxal.org/#/problem/34

(= (range 1 4) '(1 2 3))
(= (range -2 2) '(-2 -1 0 1))
(= (range 5 8) '(5 6 7))


; 35 - LOCAL BINDINGS
; https://4clojure.oxal.org/#/problem/35

(= 7 (let [x 5] (+ 2 x)))
(= 7 (let [x 3, y 10] (- y x)))
(= 7 (let [x 21] (let [y 3] (/ x y))))


; 36 - LET IT BE
; https://4clojure.oxal.org/#/problem/36

(let [z 1, y 3, x 7])

(= 10 (let [z 1, y 3, x 7] (+ x y)))
(= 4 (let [z 1, y 3, x 7] (+ y z)))
(= 1 (let [z 1, y 3, x 7] z))


; 37 - REGULAR EXPRESSIONS
; https://4clojure.oxal.org/#/problem/37

(= "ABC" (apply str (re-seq #"[A-Z]+" "bA1B3Ce ")))