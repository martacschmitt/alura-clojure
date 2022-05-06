(ns hospital.aula4
  (:use clojure.pprint))

(defrecord PacienteParticular [id, nome, nascimento, situacao])
(defrecord PacientePlanoDeSaude [id, nome, nascimento, situacao, plano])

(defprotocol Cobravel
  (deve-assinar-pre-autorizacao? [paciente procedimento valor]))

(defn nao-eh-urgente? [paciente]
  (not= :urgente (:situacao paciente :normal)))

(extend-type PacienteParticular
  Cobravel
  (deve-assinar-pre-autorizacao?
    [paciente, procedimento, valor]
    (and (>= valor 50) (nao-eh-urgente? paciente))))

(extend-type PacientePlanoDeSaude
  Cobravel
  (deve-assinar-pre-autorizacao?
    [paciente, procedimento, valor]
    (let [plano (:plano paciente)]
      (and (not (some #(= % procedimento) plano)) (nao-eh-urgente? paciente)))))

(let [particular (->PacienteParticular 15 "Guilherme" "18/9/1981" :normal)
      plano (->PacientePlanoDeSaude 15 "Guilherme" "18/9/1981" :normal [:raio-x, :ultrassom])]
  (pprint (deve-assinar-pre-autorizacao? particular, :raio-x, 500))
  (pprint (deve-assinar-pre-autorizacao? particular, :raio-x, 40))
  (pprint (deve-assinar-pre-autorizacao? plano, :raio-x, 1000))
  (pprint (deve-assinar-pre-autorizacao? plano, :coleta-de-sangue, 1000)))

(let [particular (->PacienteParticular 15 "Guilherme" "18/9/1981" :urgente)
      plano (->PacientePlanoDeSaude 15 "Guilherme" "18/9/1981" :urgente [:raio-x, :ultrassom])]
  (pprint (deve-assinar-pre-autorizacao? particular, :raio-x, 500))
  (pprint (deve-assinar-pre-autorizacao? particular, :raio-x, 40))
  (pprint (deve-assinar-pre-autorizacao? plano, :raio-x, 1000))
  (pprint (deve-assinar-pre-autorizacao? plano, :coleta-de-sangue, 1000)))


; nao se coloca multi no final, é apenas para fins comparativos das funções
(defmulti deve-assinar-pre-autorizacao-multi? class)

(defmethod deve-assinar-pre-autorizacao-multi? PacienteParticular [paciente]
  (println "Invocando PacienteParticular")
  true)

(defmethod deve-assinar-pre-autorizacao-multi? PacientePlanoDeSaude [paciente]
  (println "Invocando PacientePlanoDeSaude")
  false)

(let [particular (->PacienteParticular 15 "Guilherme" "18/9/1981" :urgente)
      plano (->PacientePlanoDeSaude 15 "Guilherme" "18/9/1981" :urgente [:raio-x, :ultrassom])]
  (pprint (deve-assinar-pre-autorizacao-multi? particular))
  (pprint (deve-assinar-pre-autorizacao-multi? plano)))


; explorando como funciona a funcao que define a estratégia de um defmulti
(defn minha-funcao [p]
  (pprint p)
  (class p))

(defmulti multi-teste minha-funcao)
;(multi-teste "Marta")
;(multi-teste :marta)



; um pouco feio pois está misturando keyword com classe
; é uma função, pode testar
(defn tipo-de-autorizador
  [pedido]
  (let [paciente (:paciente pedido)
        situacao (:situacao paciente)
        urgencia? (= :urgente situacao)]
    (if urgencia?
      :sempre-autorizado
      (class paciente))))

(defmulti deve-assinar-pre-autorizacao-do-pedido? tipo-de-autorizador)

(defmethod deve-assinar-pre-autorizacao-do-pedido? :sempre-autorizado [pedido]
  false)

(defmethod deve-assinar-pre-autorizacao-do-pedido? PacienteParticular [pedido]
  (>= (:valor pedido 0) 50))

(defmethod deve-assinar-pre-autorizacao-do-pedido? PacientePlanoDeSaude [pedido]
  (not (some #(= % (:procedimento pedido)) (:plano (:paciente pedido)))))

(let [particular (->PacienteParticular 15 "Guilherme" "18/9/1981" :urgente)
      plano (->PacientePlanoDeSaude 15 "Guilherme" "18/9/1981" :urgente [:raio-x, :ultrassom])]
  (pprint (deve-assinar-pre-autorizacao-do-pedido? {:paciente particular, :valor 1000, :procedimento :coleta-de-sangue}))
  (pprint (deve-assinar-pre-autorizacao-do-pedido? {:paciente plano, :valor 1000, :procedimento :coleta-de-sangue})))

(let [particular (->PacienteParticular 15 "Guilherme" "18/9/1981" :normal)
      plano (->PacientePlanoDeSaude 15 "Guilherme" "18/9/1981" :normal [:raio-x, :ultrassom])]
  (pprint (deve-assinar-pre-autorizacao-do-pedido? {:paciente particular, :valor 1000, :procedimento :coleta-de-sangue}))
  (pprint (deve-assinar-pre-autorizacao-do-pedido? {:paciente plano, :valor 1000, :procedimento :coleta-de-sangue})))

