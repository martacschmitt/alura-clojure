(ns hospital.logic)

(defn cabe-na-fila?
  [hospital departamento]
  (-> hospital
      (get departamento)                                    ;(get hospital departamento)
      count                                                 ;(count fila)
      (< 5)))                                               ;(< tamanho-atual 5)

;(defn chega-em
;  [hospital departamento pessoa]
;  (let [fila (get hospital departamento)
;        tamanho-atual (count fila)
;        cabe? (< tamanho-atual 5)]
;    (if cabe?
;      (update hospital departamento conj pessoa)
;      (throw (ex-info "Filá já está cheia" {:tentando-adicionar pessoa})))))

(defn chega-em
  [hospital departamento pessoa]
  (if (cabe-na-fila? hospital departamento)
    (update hospital departamento conj pessoa)
    (throw (ex-info "Filá já está cheia" {:tentando-adicionar pessoa}))))




; apenas para testes, não fazer isso na prática
(defn chega-em-pausado
  [hospital departamento pessoa]
  (if (cabe-na-fila? hospital departamento)
    (do (Thread/sleep 1000)
    (update hospital departamento conj pessoa))
    (throw (ex-info "Filá já está cheia" {:tentando-adicionar pessoa}))))




; apenas para testes, não fazer isso na prática
(defn chega-em-pausado-logando
  [hospital departamento pessoa]
  (println "Tentando adicionar a pessoa" pessoa)
  (Thread/sleep (* (rand) 2000))
  (if (cabe-na-fila? hospital departamento)
    (do                                                     ;(Thread/sleep 1000)
      (println "Dando update na " pessoa)
      (update hospital departamento conj pessoa))
    (throw (ex-info "Filá já está cheia" {:tentando-adicionar pessoa}))))




(defn atende
  [hospital departamento]
  (update hospital departamento pop))