(ns ecommerce.aula3
  (:use [clojure.pprint])
  (:require [datomic.api :as d]
            [ecommerce.db.config :as db.config]
            [ecommerce.db.produto :as db.produto]
            [schema-generators.generators :as g]
            [schema.core :as s]
            [ecommerce.model :as model]
            [ecommerce.generators :as generators]))

(s/set-fn-validation! true)

(db.config/apaga-banco!)
(def conn (db.config/abre-conexao!))
(db.config/cria-schema! conn)
(db.config/cria-dados-de-exemplo! conn)

(defn gera-10000-produtos [conn]
  (dotimes [_ 50]
    (def produtos-gerados (g/sample 200 model/Produto generators/leaf-generators))
    (db.produto/adiciona-ou-altera! conn produtos-gerados)))

(println "A geração de produtos")
(time (gera-10000-produtos conn))
(println "Busca do mais caro")
(time (dotimes [_ 100] (db.produto/busca-mais-caro (d/db conn))))
(println "Busca dos mais caros")
(time (dotimes [_ 100] (db.produto/busca-mais-caro-que (d/db conn) 50000M)))
(println "Busca por preco")
(def preco-mais-caro (db.produto/busca-mais-caro (d/db conn)))
(time (dotimes [_ 100] (db.produto/busca-por-preco (d/db conn) preco-mais-caro)))

(println "Busca por preco e nome")
(time (dotimes [_ 100] (db.produto/busca-por-preco-e-nome (d/db conn) 1000M "com")))

