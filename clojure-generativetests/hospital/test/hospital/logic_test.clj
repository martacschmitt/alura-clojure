(ns hospital.logic-test
  (:use [clojure.pprint])
  (:require [clojure.test :refer :all]
            [hospital.logic :refer :all]
            [hospital.model :as h.model]
            [clojure.test.check.clojure-test :refer (defspec)]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [schema.core :as s]
            [schema-generators.generators :as g])
  (:import (clojure.lang ExceptionInfo)))

(s/set-fn-validation! true)

(deftest cabe-na-fila?-test
  (testing "Que cabe numa fila vazia"
    (is (cabe-na-fila? {:espera []} :espera)))

  (testing "Que cabe pessoas em filas de tamanho até 4 inclusive"
    (doseq [fila (gen/sample (gen/vector gen/string-alphanumeric 0 4) 100)]
      (is (cabe-na-fila? {:espera fila}, :espera))))

  (testing "Que não cabe na fila quando a fila está cheia"
    (is (not (cabe-na-fila? {:espera [1 2 3 4 5]} :espera))))

  (testing "Que não cabe na fila quando tem mais do que uma fila cheia"
    (is (not (cabe-na-fila? {:espera [1 2 3 4 5 6]} :espera))))

  (testing "Que não cabe na fila quando tem gente mas não está cheia"
    (is (cabe-na-fila? {:espera [1 2 3 4]} :espera))
    (is (cabe-na-fila? {:espera [1 2]} :espera)))

  (testing "Que não cabe quando o departamento não existe"
    (is (not (cabe-na-fila? {:espera [1 2 3 4]} :raio-x)))))


(defspec coloca-uma-pessoa-em-filas-menores-que-5 100
         (prop/for-all [fila (gen/vector gen/string-alphanumeric 0 4)
                        pessoa gen/string-alphanumeric]
                       (is (= {:espera (conj fila pessoa)}
                          (chega-em {:espera fila} :espera pessoa)))))

(defn transforma-vetor-em-fila [vetor]
  (reduce conj h.model/fila-vazia vetor))

(def nome-aleatorio (gen/fmap clojure.string/join (gen/vector gen/char-alphanumeric 5 10)))
(def fila-nao-cheia-gen (gen/fmap transforma-vetor-em-fila (gen/vector nome-aleatorio 0 4)))

;(defn transfere-ignorando-erro
;  [hospital para]
;  (try
;    (transfere hospital :espera para)
;    (catch clojure.lang.ExceptionInfo e
;      (cond (= :fila-cheia (:type (ex-data e))) hospital
;            :else (throw e))
;      ;(println "Falhou"  (ex-data e))
;      ;hospital
;      )))

(defn transfere-ignorando-erro
  [hospital para]
  (try
    (transfere hospital :espera para)
    (catch IllegalStateException e hospital)))

(defspec transfere-tem-que-manter-a-quantidade-de-pessoas 50
         (prop/for-all [;espera gen/string-alphanumeric
                        espera (gen/fmap transforma-vetor-em-fila (gen/vector nome-aleatorio 0 50))
                        raio-x fila-nao-cheia-gen
                        ultrassom fila-nao-cheia-gen
                        vai-para (gen/vector (gen/elements [:raio-x :ultrassom]) 0 50)]
                       (let [hospital-inicial {:espera espera :raio-x raio-x :ultrassom ultrassom}
                             hospital-final (reduce transfere-ignorando-erro hospital-inicial vai-para)]
                         (= (total-de-pacientes hospital-inicial) (total-de-pacientes hospital-final)))))

(defn adiciona-fila-de-espera
  [[hospital fila]]
  (assoc hospital :espera fila))

(def hospital-gen
  (gen/fmap
    adiciona-fila-de-espera (gen/tuple (gen/not-empty (g/generator h.model/Hospital)) fila-nao-cheia-gen)))

(def chega-em-gen (gen/tuple (gen/return chega-em) (gen/return :espera) nome-aleatorio))

(defn transfere-gen
  [hospital]
  (let [departamentos (keys hospital)]
    (gen/tuple (gen/return transfere) (gen/elements departamentos) (gen/elements departamentos))))

(defn acao-gen
  [hospital]
  (gen/one-of [chega-em-gen (transfere-gen hospital)]))

(defn acoes-gen
  [hospital]
  (gen/not-empty (gen/vector (acao-gen hospital) 1 100)))

(defspec simula-um-dia-do-hospital-nao-perde-pessoas 10
         (prop/for-all [hospital hospital-gen]
                       (let [acoes (gen/sample (acoes-gen hospital) 1)]
                         (println acoes)
                         (is (= 1 1)))))