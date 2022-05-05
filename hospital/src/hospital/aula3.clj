(ns hospital.aula3
  (:use [clojure pprint])
  (:require [hospital.logic :as h.logic]
            [hospital.model :as h.model]))

; simbolo que qualquer thread que acessar esse namespace vai ter acesso a ele com o valor padrao 'guilherme'
(def nome "guilherme")

; redefinir o simbolo (refiz o binding)
(def nome 12345)

(let [nome "guilherme"]
  (println nome)
  ; não refaz o binding do simbolo local, apenas 'esconde' o valor do simbolo anterior
  ; SHADOWING
  (let [nome "daniela"]
    (println nome))
  (println nome))


(defn testa-atomao
  []
  (let [hospital-silveira (atom {:espera h.model/fila-vazia})]
    (println hospital-silveira)
    (pprint hospital-silveira)
    (pprint (deref hospital-silveira))
    (pprint @hospital-silveira)

    ; não é assim que altera conteúdo dentro do átomo
    (pprint (assoc @hospital-silveira :laboratorio1 h.model/fila-vazia))
    (pprint @hospital-silveira)

    ; quando tem ! indica que haverá efeito colateral
    ; swap chama o assoc e altera o valor do átomo
    (swap! hospital-silveira assoc :laboratorio1 h.model/fila-vazia)
    (pprint @hospital-silveira)

    (swap! hospital-silveira assoc :laboratorio2 h.model/fila-vazia)
    (pprint @hospital-silveira)

    ; update tradicional imutavel, com dereferencia, que nao trara efeito
    (update @hospital-silveira :laboratorio1 conj "111")

    ;indo para swap
    (swap! hospital-silveira update :laboratorio1 conj "111")
    (pprint hospital-silveira)

    ))

; (testa-atomao)

(defn chega-em-malvado!
  [hospital pessoa]
  (swap! hospital h.logic/chega-em-pausado-logando :espera pessoa)
  (println "apos inserir" pessoa))

(defn simula-um-dia-em-paralelo
  []
  (let [hospital (atom (h.model/novo-hospital))]
    (.start (Thread. (fn [] (chega-em-malvado! hospital "111"))))
    (.start (Thread. (fn [] (chega-em-malvado! hospital "222"))))
    (.start (Thread. (fn [] (chega-em-malvado! hospital "333"))))
    (.start (Thread. (fn [] (chega-em-malvado! hospital "444"))))
    (.start (Thread. (fn [] (chega-em-malvado! hospital "555"))))
    (.start (Thread. (fn [] (chega-em-malvado! hospital "666"))))
    (.start (Thread. (fn [] (Thread/sleep 8000)
                       (pprint hospital))))))

;(simula-um-dia-em-paralelo)


(defn chega-sem-malvado!
  [hospital pessoa]
  (swap! hospital h.logic/chega-em :espera pessoa)
  (println "apos inserir" pessoa))

(defn simula-um-dia-em-paralelo
  []
  (let [hospital (atom (h.model/novo-hospital))]
    (.start (Thread. (fn [] (chega-sem-malvado! hospital "111"))))
    (.start (Thread. (fn [] (chega-sem-malvado! hospital "222"))))
    (.start (Thread. (fn [] (chega-sem-malvado! hospital "333"))))
    (.start (Thread. (fn [] (chega-sem-malvado! hospital "444"))))
    (.start (Thread. (fn [] (chega-sem-malvado! hospital "555"))))
    (.start (Thread. (fn [] (chega-sem-malvado! hospital "666"))))
    (.start (Thread. (fn [] (Thread/sleep 8000)
                       (pprint hospital))))))

(simula-um-dia-em-paralelo)