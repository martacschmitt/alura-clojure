(ns ecommerce.aula5
  (:use [clojure.pprint])
  (:require [datomic.api :as d]
            [ecommerce.db :as db]
            [ecommerce.model :as model]))

(def conn (db/abre-conexao))

(db/cria-schema conn)

(let [computador (model/novo-produto "Computador Novo" "/computador-novo" 2500.10M)
      celular (model/novo-produto "Celular Caro" "/celular" 8888.88M)
      resultado @(d/transact conn [computador, celular])]
  (pprint resultado))

; snapshot
(def fotografia-no-passado (d/db conn))

(let [calculadora {:produto/nome "Calculadora com 4 operações"}
      celular-barato (model/novo-produto "Celular Barato" "/celular-barato" 0.1M)
      resultado @(d/transact conn [calculadora, celular-barato])]
  (pprint resultado))

; um snapshot no instante do d/db
(pprint (count (db/todos-os-produtos (d/db conn))))

; rodando a query num banco filtrado com dados do passado
(pprint (count (db/todos-os-produtos fotografia-no-passado)))

; antes
(pprint (count (db/todos-os-produtos (d/as-of (d/db conn) #inst "2022-05-16T17:29:59.460"))))

; no meio
(pprint (count (db/todos-os-produtos (d/as-of (d/db conn) #inst "2022-05-16T17:28:59.460"))))

; depois
(pprint (count (db/todos-os-produtos (d/as-of (d/db conn) #inst "2022-05-16T17:30:59.460"))))


;(db/apaga-banco)