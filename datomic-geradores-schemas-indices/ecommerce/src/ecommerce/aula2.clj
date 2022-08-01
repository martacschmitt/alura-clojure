(ns ecommerce.aula2
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
  (dotimes [atual 50]
    (def produtos-gerados (g/sample 200 model/Produto generators/leaf-generators))
    (println atual (count @(db.produto/adiciona-ou-altera! conn produtos-gerados)))))

(time (gera-10000-produtos conn))

(time (dotimes [_ 100] (db.produto/busca-mais-caro (d/db conn))))
(time (dotimes [_ 100] (db.produto/busca-mais-caro-que (d/db conn) 50000M)))
