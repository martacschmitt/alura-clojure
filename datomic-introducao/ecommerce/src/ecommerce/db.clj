(ns ecommerce.db
  (:use [clojure.pprint])
  (:require [datomic.api :as d]))

(def db-uri "datomic:dev://localhost:4334/ecommerce")

(defn abre-conexao! []
  (d/create-database db-uri)
  (d/connect db-uri))

(defn apaga-banco! []
  (d/delete-database db-uri))


; Produtos
; id?
; nome String 1 ==> Computador Novo
; slug String 1 ==> /computador_novo
; preco ponto flutuante 1 ==> 3500.10

; id_entidade atributo valor
; 15 :produto/nome Computador Novo
; 15 :produto/slug /computador_novo
; 15 :produto/preco 3500.10
; 17 :produto/nome Telefone Caro
; 17 :produto/slug /telefone
; 17 :produto/preco 8888.88

(def schema [{:db/ident       :produto/nome
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc         "O nome de um produto"}
             {:db/ident       :produto/slug
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc         "O caminho para acessar esse produto via hhtp"}
             {:db/ident       :produto/preco
              :db/valueType   :db.type/bigdec
              :db/cardinality :db.cardinality/one
              :db/doc         "O preço de um produto com precisão monetária"}
             {:db/ident       :produto/palavra-chave
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/many}
             {:db/ident       :produto/id
              :db/valueType   :db.type/uuid
              :db/cardinality :db.cardinality/one
              :db/unique      :db.unique/identity}
             {:db/ident       :produto/categoria
              :db/valueType   :db.type/ref
              :db/cardinality :db.cardinality/one}

             {:db/ident       :categoria/nome
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one}
             {:db/ident       :categoria/id
              :db/valueType   :db.type/uuid
              :db/cardinality :db.cardinality/one
              :db/unique      :db.unique/identity}])

(defn cria-schema! [conn]
  (d/transact conn schema))

;(defn todos-os-produtos [db]
;  (d/q '[:find (pull ?entidade [:produto/nome :produto/preco :produto/slug])
;         :where [?entidade :produto/nome]] db))

(defn todos-os-produtos [db]
  (d/q '[:find (pull ?entidade [*])
         :where [?entidade :produto/nome]] db))

(defn todos-os-produtos-por-slug
  [db slug]
  (d/q '[:find ?entidade
         :in $ ?slug-a-ser-buscado
         :where [?entidade :produto/slug ?slug-a-ser-buscado]] db slug))

(defn todos-os-slugs [db]
  (d/q '[:find ?slug
         :where [_ :produto/slug ?slug]] db))

(defn todos-os-produtos-por-preco [db preco-minimo-requisitado]
  (d/q '[:find ?nome, ?preco
         :in $, ?preco-minimo
         :keys produto/nome, produto/preco
         :where [?id :produto/preco ?preco]
         [(> ?preco ?preco-minimo)]
         [?id :produto/nome ?nome]] db, preco-minimo-requisitado))


(defn todos-os-produtos-por-palavra-chave
  [db palavra-chave-buscada]
  (d/q '[:find (pull ?produto [*])
         :in $ ?palavra-chave
         :where [?produto :produto/palavra-chave ?palavra-chave]]
       db palavra-chave-buscada))


(defn um-produto-por-dbid [db db-id]
  (d/pull db '[*] db-id))

(defn um-produto [db produto-id]
  (d/pull db '[*] [:produto/id produto-id]))

(defn todas-as-categorias [db]
  (d/q '[:find (pull ?categoria [*])
         :where [?categoria :categoria/id]] db))

(defn db-adds-de-atribuicao-de-categorias
  [produtos categoria]
  (reduce (fn [db-adds produto] (conj db-adds [:db/add
                                               [:produto/id (:produto/id produto)]
                                               :produto/categoria
                                               [:categoria/id (:categoria/id categoria)]]))
          []
          produtos))

(defn atribui-categorias! [conn produtos categoria]
  (let [a-transacionar (db-adds-de-atribuicao-de-categorias produtos categoria)]
    (d/transact conn a-transacionar)))


(defn adiciona-produtos!
  [conn produtos]
  (d/transact conn produtos))

(defn adiciona-categorias!
  [conn categorias]
  (d/transact conn categorias))