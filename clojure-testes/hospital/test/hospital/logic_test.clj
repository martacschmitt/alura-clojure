(ns hospital.logic-test
  (:require [clojure.test :refer :all]
            [hospital.logic :refer :all]))

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
      ;(is (= {:espera [1 2 3 4 5]} (chega-em {:espera [1 2 3 4]} :espera 5)))
      ;(is (= {:espera [1 2 5]} (chega-em {:espera [1 2]} :espera 5)))

      (is (= {:hospital {:espera [1 2 3 4 5]} :resultado :sucesso} (chega-em {:espera [1 2 3 4]} :espera 5)))
      (is (= {:hospital {:espera [1 2 5]} :resultado :sucesso} (chega-em {:espera [1 2]} :espera 5)))

      )

    (testing "Não aceita quando não cabe na fila"

      ; verificando que uma exception foi jogada
      ; problema: qualquer outro erro genérico vai jogar essa exception
      ;(is (thrown? clojure.lang.ExceptionInfo (chega-em hospital-cheio :espera 76))))

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

      (is (= {:hospital hospital-cheio :resultado :impossivel-colocar-pessoa-na-fila})
          (chega-em hospital-cheio :espera 76)))))

