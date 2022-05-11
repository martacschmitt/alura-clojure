(ns hospital.logic-test
  (:use [clojure.pprint])
  (:require [clojure.test :refer :all]
            [hospital.logic :refer :all]
            [hospital.model :as h.model]
            [schema.core :as s]))

(s/set-fn-validation! true)

(deftest cabe-na-fila?-test
  ; boundary tests
  ; exatamente na borda e one off. -1, +1, <=, >=, =.

  ; borda do zero
  (testing "Que cabe numa fila vazia"
    (is (cabe-na-fila? {:espera []} :espera)))

  ; borda do limite
  (testing "Que não cabe na fila quando a fila está cheia"
    (is (not (cabe-na-fila? {:espera [1 2 3 4 5]} :espera))))

  ; one off da borda do limite pra cima
  (testing "Que não cabe na fila quando tem mais do que uma fila cheia"
    (is (not (cabe-na-fila? {:espera [1 2 3 4 5 6]} :espera))))

  (testing "Que não cabe na fila quando tem gente mas não está cheia"
    (is (cabe-na-fila? {:espera [1 2 3 4]} :espera))
    (is (cabe-na-fila? {:espera [1 2]} :espera)))

  (testing "Que não cabe quando o departamento não existe"
    (is (not (cabe-na-fila? {:espera [1 2 3 4]} :raio-x)))))


(deftest chega-em-test

  (let [hospital-cheio {:espera [1 35 42 64 21]}]

    (testing "Aceita pessoas enquanto cabem pessoas na fila"
      (is (= {:espera [1 2 3 4 5]} (chega-em {:espera [1 2 3 4]} :espera 5)))
      (is (= {:espera [1 2 5]} (chega-em {:espera [1 2]} :espera 5)))

      ;(is (= {:hospital {:espera [1 2 3 4 5]} :resultado :sucesso} (chega-em {:espera [1 2 3 4]} :espera 5)))
      ;(is (= {:hospital {:espera [1 2 5]} :resultado :sucesso} (chega-em {:espera [1 2]} :espera 5)))

      )

    (testing "Não aceita quando não cabe na fila"

      ; verificando que uma exception foi jogada
      ; problema: qualquer outro erro genérico vai jogar essa exception
      (is (thrown? clojure.lang.ExceptionInfo (chega-em hospital-cheio :espera 76))))

      ;(is (thrown? IllegalStateException (chega-em hospital-cheio :espera 76)))

      ; string de texto solto são super faceis de quebrar
      ;(is (thrown? clojure.lang.ExceptionInfo "Não cabe ninguém neste departamento" (chega-em hospital-cheio :espera 76)))

      ; outra abordagem, do nil
      ; mas o perigo do swap
      ;(is (nil? (chega-em hospital-cheio :espera 76)))

      ;(is (try (chega-em hospital-cheio :espera 76)
      ;         false
      ;         (catch clojure.lang.ExceptionInfo e
      ;           (= :impossivel-colocar-pessoa-na-fila (:tipo (ex-data e))))
      ;         ))

      ;(is (= {:hospital hospital-cheio :resultado :impossivel-colocar-pessoa-na-fila})
      ;    (chega-em hospital-cheio :espera 76))

      ))


(deftest transfere-test

  (testing "Aceita pessoas se cabe"
    (let [hospital-original {:espera (conj h.model/fila-vazia "5") :raio-x h.model/fila-vazia}]
      (is (= {:espera [] :raio-x ["5"]}
             (transfere hospital-original :espera :raio-x))))

    (let [hospital-original {:espera (conj h.model/fila-vazia "51" "5") :raio-x (conj h.model/fila-vazia "13")}]
      (is (= {:espera ["5"] :raio-x ["13" "51"]}
             (transfere hospital-original :espera :raio-x)))))

  (testing "Recusa pessoas se não cabe"
    (let [hospital-cheio {:espera (conj h.model/fila-vazia "5") :raio-x (conj h.model/fila-vazia "1" "2" "53" "42" "13")}]
      (is (thrown? clojure.lang.ExceptionInfo
             (transfere hospital-cheio :espera :raio-x)))))

  (testing "Não pode invocar transferência sem hospital"
    (is (thrown? clojure.lang.ExceptionInfo (transfere nil :espera :raio-x))))

  (testing "Condições obrigatórias"
    (let [hospital {:espera (conj h.model/fila-vazia "5") :raio-x (conj h.model/fila-vazia "1" "2" "53" "42")}]
      (is (thrown? AssertionError (transfere hospital :nao-existe :raio-x)))
      (is (thrown? AssertionError (transfere hospital :raio-x :nao-existe))))))
