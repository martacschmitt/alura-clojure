(ns hospital.aula2
  (:use clojure.pprint)
  (:require [schema.core :as s]))

(s/set-fn-validation! true)

;(s/defrecord Paciente
;  [id :- Long, nome :- s/Str])
;
;(pprint (map->Paciente {15 "Guilherme"}))
;(pprint (map->Paciente {"15" "Guilherme"}))

(def Paciente
  "Schema de um paciente"
  {:id s/Num, :nome s/Str})

(pprint (s/explain Paciente))
(pprint (s/validate Paciente {:id 15, :nome "Guilherme"}))

; typo é pego pelo schema
;(pprint (s/validate Paciente {:id 15, :name "Guilherme"}))

; não aceita elementos a mais (não estamos sendo foward compatible)
;(pprint (s/validate Paciente {:id 15, :name "Guilherme", :plano [:raio-x]}))

; chaves que são keywords em schemas são por padrão obrigatórias
;(pprint (s/validate Paciente {:id 15}))

(s/defn novo-paciente :- Paciente
  [id :- s/Num, nome :- s/Str]
  ;{:id id, :nome nome, :plano []}
  {:id id, :nome nome})

(pprint (novo-paciente 15 "Guilherme"))

; função pura, simples e fácil de testar
(defn estritamente-positivo? [x]
  (> x 0))

;(def EstritamentePositivo (s/pred estritamente-positivo?))
(def EstritamentePositivo (s/pred estritamente-positivo? 'estritamente-positivo?))

(pprint (s/validate EstritamentePositivo 15))
;(pprint (s/validate EstritamentePositivo 0))
;(pprint (s/validate EstritamentePositivo -15))


(def Paciente
  "Schema de um paciente"
  {:id (s/constrained s/Int pos?), :nome s/Str})
; pos? retorna se número é positivo
; pos-int? retorna se é positivo e inteiro

(pprint (s/validate Paciente {:id 15, :nome "Guilherme"}))
;(pprint (s/validate Paciente {:id -15, :nome "Guilherme"}))
;(pprint (s/validate Paciente {:id 0, :nome "Guilherme"}))



;(def Paciente
;  "Schema de um paciente"
;  {:id (s/constrained s/Int #(> % 0) 'inteiro-estritamente-positivo), :nome s/Str})
;
;(pprint (s/validate Paciente {:id 15, :nome "Guilherme"}))
;(pprint (s/validate Paciente {:id -15, :nome "Guilherme"}))
;(pprint (s/validate Paciente {:id 0, :nome "Guilherme"}))
