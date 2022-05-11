(ns hospital.core
  (:require [clojure.test.check.generators :as gen]))

(println (gen/sample gen/boolean 3))
(println (gen/sample gen/int))
(println (gen/sample gen/small-integer 100))
(println (gen/sample gen/string))
(println (gen/sample gen/string-alphanumeric 100))

(println (gen/sample (gen/vector gen/small-integer 15) 5))
(println (gen/sample (gen/vector gen/small-integer) 100))
(println (gen/sample (gen/vector gen/small-integer 1 5) 100))