(ns ecommerce.db
  (:use [clojure.pprint])
  (:require [datomic.api :as d]
            [ecommerce.model :as model]
            [schema.core :as s]
            [clojure.walk :as walk]))

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
              :db/unique      :db.unique/identity}

             {:db/ident       :tx-data/ip
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one}])

(defn cria-schema! [conn]
  (d/transact conn schema))

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

(s/defn adiciona-ou-altera-produtos!
  ([conn produtos :- [model/Produto]]
   (d/transact conn produtos))
  ([conn produtos :- [model/Produto] ip]
   (let [db-add-ipp [:db/add "datomic.tx" :tx-data/ip ip]]
     (d/transact conn (conj produtos db-add-ipp)))))

(s/defn adiciona-categorias!
  [conn categorias :- [model/Categoria]]
  (d/transact conn categorias))

(defn dissoc-db-id [entidade]
  (if (map? entidade)
    (dissoc entidade :db/id)
    entidade))

(defn datomic-para-entidade [entidades]
  (walk/prewalk dissoc-db-id entidades))

(s/defn um-produto :- (s/maybe model/Produto) [db produto-id :- java.util.UUID]
  (let [resultado (d/pull db '[* {:produto/categoria [*]}] [:produto/id produto-id])
        produto (datomic-para-entidade resultado)]
    (if (:produto/id produto)
      produto
      nil)))

(s/defn um-produto! :- model/Produto [db produto-id :- java.util.UUID]
  (let [produto (um-produto db produto-id)]
    (when (nil? produto)
      (throw (ex-info "Não encontrei uma entidade" {:type :errors/not-found, :id produto-id})))
    produto))

(s/defn todas-as-categorias :- [model/Categoria] [db]
  (datomic-para-entidade (d/q '[:find [(pull ?categoria [*]) ...]
                                :where [?categoria :categoria/id]] db)))

(s/defn todos-os-produtos :- [model/Produto] [db]
  (datomic-para-entidade (d/q '[:find [(pull ?entidade [* {:produto/categoria [*]}]) ...]
                                :where [?entidade :produto/nome]] db)))

(defn cria-dados-de-exemplo [conn]
  (def eletronicos (model/nova-categoria "Eletrônicos"))
  (def esporte (model/nova-categoria "Esportes"))
  (pprint @(adiciona-categorias! conn [eletronicos, esporte]))

  (def computador (model/novo-produto (model/uuid) "Computador Novo" "/computador-novo" 2500.10M))
  (def celular (model/novo-produto (model/uuid) "Celular Caro" "/celular" 8888.88M))
  ;(def calculadora {:produto/nome "Calculadora com 4 operações"})
  (def celular-barato (model/novo-produto (model/uuid) "Celular Barato" "/celular-barato" 0.1M))
  (def xadrez (model/novo-produto (model/uuid) "Tabuleiro de xadrez" "/tabuleiro-de-xadrez" 30M))
  (pprint @(adiciona-ou-altera-produtos! conn [computador, celular, celular-barato, xadrez] "200.216.222.125"))

  (atribui-categorias! conn [computador, celular, celular-barato] eletronicos)
  (atribui-categorias! conn [xadrez] esporte))