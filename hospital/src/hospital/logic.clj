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
;      (throw (ex-info "Fila já está cheia" {:tentando-adicionar pessoa})))))

(defn chega-em
  [hospital departamento pessoa]
  (if (cabe-na-fila? hospital departamento)
    (update hospital departamento conj pessoa)
    (throw (ex-info "Fila já está cheia" {:tentando-adicionar pessoa}))))




; apenas para testes, não fazer isso na prática
(defn chega-em-pausado
  [hospital departamento pessoa]
  (if (cabe-na-fila? hospital departamento)
    (do (Thread/sleep 1000)
        (update hospital departamento conj pessoa))
    (throw (ex-info "Fila já está cheia" {:tentando-adicionar pessoa}))))




; apenas para testes, não fazer isso na prática
(defn chega-em-pausado-logando
  [hospital departamento pessoa]
  (println "Tentando adicionar a pessoa" pessoa)
  (Thread/sleep (* (rand) 2000))
  (if (cabe-na-fila? hospital departamento)
    (do                                                     ;(Thread/sleep 1000)
      (println "Dando update na " pessoa)
      (update hospital departamento conj pessoa))
    (throw (ex-info "Fila já está cheia" {:tentando-adicionar pessoa}))))




(defn atende
  [hospital departamento]
  (update hospital departamento pop))


(defn proxima
  "Retorna o próximo paciente da fila"
  [hospital departamento]
  (-> hospital
      departamento
      peek))

(defn transfere
  "Transefere o próximo paciente da fila 'de' para a fila 'para'"
  [hospital de para]
  (let [pessoa (proxima hospital de)]
    (-> hospital
        (atende de)
        (chega-em para pessoa))))



(defn atende-completo
  "Somente para demonstrar que é possível já retornar os dois (quem foi removido 'peek' e o resto da fila 'pop') "
  [hospital departamento]
  {:paciente (update hospital departamento peek)
   :hospital     (update hospital departamento pop)})


(defn atende-completo-que-chama-ambos
  "Somente para demonstrar que é possível já retornar os dois (quem foi removido 'peek' e o resto da fila 'pop') "
  [hospital departamento]
  (let [fila (get hospital departamento)
        peek-pop (juxt peek pop)
        [pessoa fila-atualizada] (peek-pop fila)
        hospital-atualizado (update hospital assoc departamento fila-atualizada)]
    {:paciente pessoa
     :hospital hospital-atualizado}))