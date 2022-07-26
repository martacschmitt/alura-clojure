(ns ecommerce.aula1
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

(pprint (db.produto/todos (d/db conn)))

(pprint (g/sample 10 model/Categoria))
(pprint (g/sample 10 model/Variacao generators/leaf-generators))

