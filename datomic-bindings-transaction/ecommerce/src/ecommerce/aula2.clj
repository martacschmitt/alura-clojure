(ns ecommerce.aula2
  (:use [clojure.pprint])
  (:require [datomic.api :as d]
            [ecommerce.db :as db]
            [ecommerce.model :as model]
            [schema.core :as s]))

(s/set-fn-validation! true)

(db/apaga-banco!)
(def conn (db/abre-conexao!))
(db/cria-schema! conn)

(db/cria-dados-de-exemplo conn)

(def produtos (db/todos-os-produtos (d/db conn)))
(def primeiro (first produtos))
(pprint primeiro)

(pprint @(db/atualiza-preco! conn (:produto/id primeiro) 2500.10M 3000M))

(def segundo (second produtos))
(pprint segundo)
(def a-atualizar {:produto/id (:produto/id segundo) :produto/preco 9000M :produto/estoque 8})

(pprint @(db/atualiza-produto! conn segundo a-atualizar))