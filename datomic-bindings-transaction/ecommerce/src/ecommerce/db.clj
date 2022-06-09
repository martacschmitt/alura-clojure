(ns ecommerce.db
  (:use [clojure.pprint])
  (:require [datomic.api :as d]
            [ecommerce.model :as model]
            [schema.core :as s]
            [clojure.walk :as walk]
            [clojure.set :as cset]
            [clojure.core :as c])
  (:import (java.util UUID)))

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
             {:db/ident       :produto/estoque
              :db/valueType   :db.type/long
              :db/cardinality :db.cardinality/one}
             {:db/ident       :produto/digital
              :db/valueType   :db.type/boolean
              :db/cardinality :db.cardinality/one}
             {:db/ident       :produto/variacao
              :db/isComponent true
              :db/valueType   :db.type/ref
              :db/cardinality :db.cardinality/many}
             {:db/ident       :produto/visualizacoes
              :db/valueType   :db.type/long
              :db/cardinality :db.cardinality/one
              :db/noHistory   true}

             {:db/ident       :variacao/id
              :db/valueType   :db.type/uuid
              :db/cardinality :db.cardinality/one
              :db/unique      :db.unique/identity}
             {:db/ident       :variacao/nome
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one}
             {:db/ident       :variacao/preco
              :db/valueType   :db.type/bigdec
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

(s/defn um-produto :- (s/maybe model/Produto) [db produto-id :- UUID]
  (let [resultado (d/pull db '[* {:produto/categoria [*]}] [:produto/id produto-id])
        produto (datomic-para-entidade resultado)]
    (if (:produto/id produto)
      produto
      nil)))

(s/defn um-produto! :- model/Produto [db produto-id :- UUID]
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

  (def computador (model/novo-produto (model/uuid) "Computador Novo" "/computador-novo" 2500.10M 10))
  (def celular (model/novo-produto (model/uuid) "Celular Caro" "/celular" 8888.88M))
  ;(def calculadora {:produto/nome "Calculadora com 4 operações"})
  (def celular-barato (model/novo-produto (model/uuid) "Celular Barato" "/celular-barato" 0.1M))
  (def xadrez (model/novo-produto (model/uuid) "Tabuleiro de xadrez" "/tabuleiro-de-xadrez" 30M 5))
  (def jogo (assoc (model/novo-produto (model/uuid) "Jogo online" "/jogo-online" 20M) :produto/digital true))
  (adiciona-ou-altera-produtos! conn [computador, celular, celular-barato, xadrez, jogo] "200.216.222.125")

  (atribui-categorias! conn [computador, celular, celular-barato, jogo] eletronicos)
  (atribui-categorias! conn [xadrez] esporte))

(def regras
  '[
    [(estoque ?produto ?estoque)
     [?produto :produto/estoque ?estoque]]
    [(estoque ?produto ?estoque)
     [?produto :produto/digital true]
     [(ground 100) ?estoque]]
    [(pode-vender? ?produto)
     (estoque ?produto ?estoque)
     [(> ?estoque 0)]]
    [(produto-na-categoria ?produto ?nome-da-categoria)
     [?categoria :categoria/nome ?nome-da-categoria]
     [?produto :produto/categoria ?categoria]]
    ])

(s/defn todos-os-produtos-vendaveis :- [model/Produto] [db]
  (datomic-para-entidade (d/q '[:find [(pull ?produto [* {:produto/categoria [*]}]) ...]
                                :in $ %
                                :where (pode-vender? ?produto)] db regras)))

(s/defn um-produto-vendavel :- (s/maybe model/Produto) [db produto-id :- UUID]
  (let [query '[:find (pull ?produto [* {:produto/categoria [*]}]) .
                :in $ % ?id
                :where [?produto :produto/id ?id]
                (pode-vender? ?produto)]
        resultado (d/q query db regras produto-id)
        produto (datomic-para-entidade resultado)]
    (if (:produto/id produto)
      produto
      nil)))

(s/defn todos-os-produtos-nas-categorias :- [model/Produto]
  [db, categorias :- [s/Str]]
  (datomic-para-entidade (d/q '[:find [(pull ?produto [* {:produto/categoria [*]}]) ...]
                                :in $ % [?nome-da-categoria ...]
                                :where (produto-na-categoria ?produto ?nome-da-categoria)] db, regras, categorias)))

(s/defn todos-os-produtos-nas-categorias-e-digital :- [model/Produto]
  [db, categorias :- [s/Str], digital? :- s/Bool]
  (datomic-para-entidade (d/q '[:find [(pull ?produto [* {:produto/categoria [*]}]) ...]
                                :in $ % [?nome-da-categoria ...] ?eh-digital?
                                :where (produto-na-categoria ?produto ?nome-na-categoria)
                                [?produto :produto/digital ?eh-digital?]]
                              db, regras, categorias, digital?)))

;(s/defn atualiza-preco [conn produto-id :- UUID
;                        preco-antigo :- BigDecimal
;                        preco-novo :- BigDecimal]
;  (if (= preco-antigo (:produto/preco (d/pull conn [*] (:produto/id produto-id))))
;    ; não garante atomicidade
;    (d/transact conn [{:produto/id produto-id :produto/preco preco-novo}])
;    (throw (ex-info "Valor foi alterado entre leitura e escrita" {:kind :errors/transaction-validation-error}))))

(s/defn atualiza-preco!
  [conn produto-id :- UUID
   preco-antigo :- BigDecimal
   preco-novo :- BigDecimal]
  (d/transact conn [[:db/cas [:produto/id produto-id] :produto/preco preco-antigo preco-novo]]))


(s/defn atualiza-produto!
  [conn
   antigo :- model/Produto
   a-atualizar :- model/Produto]
  (let [produto-id (:produto/id antigo)
        atributos (cset/intersection (set (keys antigo)) (set (keys a-atualizar)))
        atributos (disj atributos :produto/id)
        txs (map
              (fn [atributo]
                [:db/cas [:produto/id produto-id] atributo (get antigo atributo) (get a-atualizar atributo)])
              atributos)]
    (d/transact conn txs)))

(s/defn adiciona-variacao!
  [conn
   produto-id :- UUID
   variacao :- s/Str
   preco :- BigDecimal]
  (d/transact conn [{:db/id          "variacao-temporaria"
                     :variacao/nome  variacao
                     :variacao/preco preco
                     :variacao/id    (model/uuid)}
                    {:produto/id       produto-id
                     :produto/variacao "variacao-temporaria"}]))

(defn total-de-produtos [db]
  (d/q '[:find [(count ?produto)]
         :where [?produto :produto/nome]]
       db))

(s/defn remove-produto!
  [conn
   produto-id :- UUID]
  (d/transact conn [[:db/retractEntity [:produto/id produto-id]]]))

(s/defn visualizacoes
  [db
   produto-id :- UUID]
  (or (d/q '[:find ?visualizacoes .
             :in $ ?id
             :where [?p :produto/id ?id]
             [?p :produto/visualizacoes ?visualizacoes]]
           db produto-id) 0))

(s/defn visualizacao!
  [conn
   produto-id :- UUID]
  (let [ate-agora (visualizacoes (d/db conn) produto-id)
        novo-valor (inc ate-agora)]
    (d/transact conn [{:produto/id            produto-id
                       :produto/visualizacoes novo-valor}])))