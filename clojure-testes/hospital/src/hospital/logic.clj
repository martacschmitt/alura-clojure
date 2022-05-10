(ns hospital.logic)

; problema quando o departamento é nil o count retorna 0 por padrão
;(defn cabe-na-fila?
;  [hospital departamento]
;  (-> hospital
;      departamento
;      count
;      (< 5)))

; funciona para o caso de não ter departamento
;(defn cabe-na-fila?
;  [hospital departamento]
;  (when-let [fila (get hospital departamento)]
;    (-> fila
;        count
;        (< 5))))

; também funciona mas usa o some->
; desvantagem: menos explícito e qualquer coisa que der nulo devolve nil
(defn cabe-na-fila?
  [hospital departamento]
  (some-> hospital
          departamento
          count
          (< 5)))

;(defn chega-em
;  [hospital departamento pessoa]
;  (if (cabe-na-fila? hospital departamento)
;    (update hospital departamento conj pessoa)
;    (throw (ex-info "Não cabe ninguém neste departamento" {:paciente pessoa}))))

;(defn chega-em
;  [hospital departamento pessoa]
;  (if (cabe-na-fila? hospital departamento)
;    (update hospital departamento conj pessoa)
;    (throw (IllegalStateException. "Não cabe ninguém neste departamento"))))

;(defn chega-em
;  [hospital departamento pessoa]
;  (if (cabe-na-fila? hospital departamento)
;    (update hospital departamento conj pessoa)))


; exemplo para extrair com ex-data
;(defn chega-em
;  [hospital departamento pessoa]
;  (if (cabe-na-fila? hospital departamento)
;    (update hospital departamento conj pessoa)
;    (throw (ex-info "Não cabe ninguém neste departamento" {:paciente pessoa :tipo :impossivel-colocar-pessoa-na-fila}))))



;(defn- tenta-colocar-na-fila
;  [hospital departamento pessoa]
;  (if (cabe-na-fila? hospital departamento)
;    (update hospital departamento conj pessoa)))

;(defn chega-em
;  [hospital departamento pessoa]
;  (if-let [novo-hospital (tenta-colocar-na-fila hospital departamento pessoa)]
;    {:hospital novo-hospital :resultado :sucesso}
;    {:hospital hospital :resultado :impossivel-colocar-pessoa-na-fila}))


(defn chega-em
  [hospital departamento pessoa]
  (if (cabe-na-fila? hospital departamento)
    (update hospital departamento conj pessoa)
    (throw (ex-info "Não cabe ninguém neste departamento" {:paciente pessoa}))))

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


