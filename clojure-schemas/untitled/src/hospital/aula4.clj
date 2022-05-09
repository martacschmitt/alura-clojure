(ns hospital.aula4
  (:use clojure.pprint)
  (:require [schema.core :as s]))

(s/set-fn-validation! true)

(def PosInt (s/pred pos-int?))

(def Plano [s/Keyword])

(def Paciente
  {:id                          PosInt
   :nome                        s/Str
   :plano                       Plano
   (s/optional-key :nascimento) s/Str})

(pprint (s/validate Paciente {:id 15, :nome "Guilherme", :plano [:raio-x, :ultrassom]}))
(pprint (s/validate Paciente {:id 15, :nome "Guilherme", :plano [:raio-x]}))
(pprint (s/validate Paciente {:id 15, :nome "Guilherme", :plano []}))
(pprint (s/validate Paciente {:id 15, :nome "Guilherme", :plano nil}))
(pprint (s/validate Paciente {:id 15, :nome "Guilherme", :plano [], :nascimento "18/9/1981"}))


(def Pacientes
  {PosInt Paciente})

(pprint (s/validate Pacientes {}))

(let [guilherme {:id 15, :nome "Guilherme", :plano [:raio-x]}
      daniela {:id 20, :nome "Daniela", :plano []}]
  (pprint (s/validate Pacientes {15 guilherme}))
  (pprint (s/validate Pacientes {20 daniela}))
  ;(pprint (s/validate Pacientes {-15 guilherme}))
  ;(pprint (s/validate Pacientes {15 15}))
  ;(pprint (s/validate Pacientes {15 {:id 15, :nome "Guilherme"}}))
  )

(def Visitas
  {PosInt [s/Str]})