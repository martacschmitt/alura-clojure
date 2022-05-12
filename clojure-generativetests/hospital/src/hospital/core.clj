(ns hospital.core
  (:require [clojure.test.check.generators :as gen]
            [schema-generators.generators :as g]
            [hospital.model :as h.model]))

(println (gen/sample gen/boolean 3))
(println (gen/sample gen/int))
(println (gen/sample gen/small-integer 100))
(println (gen/sample gen/string))
(println (gen/sample gen/string-alphanumeric 100))

(println (gen/sample (gen/vector gen/small-integer 15) 5))
(println (gen/sample (gen/vector gen/small-integer) 100))
(println (gen/sample (gen/vector gen/small-integer 1 5) 100))

(println (g/sample 10 h.model/PacienteID))
(pprint (g/sample 10 h.model/Departamento))
(pprint (g/sample 10 h.model/Hospital))
(pprint (g/generate h.model/Hospital))