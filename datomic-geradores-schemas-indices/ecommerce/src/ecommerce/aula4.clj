(ns ecommerce.aula4
  (:use [clojure.pprint])
  (:require [ecommerce.db.config :as db.config]
            [schema.core :as s]
            [ecommerce.model :as model])
  (:import (java.util UUID)
           (schema.core OptionalKey)))

(s/set-fn-validation! true)

(db.config/apaga-banco!)
(def conn (db.config/abre-conexao!))
(db.config/cria-schema! conn)
(db.config/cria-dados-de-exemplo! conn)

(defn propriedades-do-valor [valor]
  (if (vector? valor)
    (merge {:db/cardinality :db.cardinality/many} (propriedades-do-valor (first valor)))
    (cond (= valor UUID) {:db/valueType :db.type/uuid
                          :db/unique    :db.unique/identity}
          (= valor s/Str) {:db/valueType :db.type/string}
          (= valor BigDecimal) {:db/valueType :db.type/bigdec}
          (= valor Long) {:db/valueType :db.type/long}
          (= valor s/Bool) {:db/valueType :db.type/boolean}
          (map? valor) {:db/valueType :db.type/ref}
          :else {:db/valueType (str "desconhecido: " (type valor) valor)})))

(defn extrai-nome-da-chave [chave]
  (cond (keyword? chave) chave
        (instance? OptionalKey chave) (get chave :k)
        :else chave))

(defn chave-valor-para-definicao [[chave valor]]
  (let [base {:db/ident       (extrai-nome-da-chave chave)
              :db/cardinality :db.cardinality/one}
        extra (propriedades-do-valor valor)
        schema-do-datomic (merge base extra)]
    schema-do-datomic))

(defn schema-to-datomic [definicao]
  (mapv chave-valor-para-definicao definicao))

(pprint (schema-to-datomic model/Categoria))
(pprint (schema-to-datomic model/Variacao))
(pprint (schema-to-datomic model/Venda))
(pprint (schema-to-datomic model/Produto))
