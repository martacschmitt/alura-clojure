(ns loja.aula1)

; COLEÇÕES
;["daniela" "guilherme" "carlos" "paulo" "lucia" "ana"]  -- vetor
;{"guilherme" 37, "paulo" 39}  -- mapa (HashMap)
;'(1 2 3 4 5)  -- lista ligada
;#{}  -- conjunto (Set)

; map
; reduce
; filter

(map println ["daniela" "guilherme" "carlos" "paulo" "lucia" "ana"])

(println (first ["daniela" "guilherme" "carlos" "paulo" "lucia" "ana"]))
(println (rest ["daniela" "guilherme" "carlos" "paulo" "lucia" "ana"]))
(println (rest []))
(println (next ["daniela" "guilherme" "carlos" "paulo" "lucia" "ana"]))
(println (next []))
(println (seq []))
(println (seq [1 2 3 4 5]))

(println "\n\n\n\nMEU MAPA")

;(defn meu-mapa
;  [funcao sequencia]
;  (let [primeiro (first sequencia)]
;    (funcao primeiro)
;    (meu-mapa funcao (rest sequencia))))

;(meu-mapa println ["daniela" "guilherme" "carlos" "paulo" "lucia" "ana"])


(println "\n\n\n\nMEU MAPA com parada no false")
(defn meu-mapa
  [funcao sequencia]
  (let [primeiro (first sequencia)]
    (if primeiro
      (do
        (funcao primeiro)
        (meu-mapa funcao (rest sequencia))))))

(meu-mapa println ["daniela" "guilherme" "carlos" "paulo" "lucia" "ana"])
(meu-mapa println ["daniela" false "carlos" "paulo" "lucia" "ana"])



(println "\n\n\n\nMEU MAPA com parada no nil")
(defn meu-mapa
  [funcao sequencia]
  (let [primeiro (first sequencia)]
    (if (not (nil? primeiro))
      (do
        (funcao primeiro)
        (meu-mapa funcao (rest sequencia))))))

(meu-mapa println ["daniela" "guilherme" "carlos" "paulo" "lucia" "ana"])
(meu-mapa println ["daniela" false "carlos" "paulo" "lucia" "ana"])
(meu-mapa println [])
(meu-mapa println nil)

;(meu-mapa println (range 100000))  -- StackOverFlowError


; TAIL RECURSION
(defn meu-mapa
  [funcao sequencia]
  (let [primeiro (first sequencia)]
    (if (not (nil? primeiro))
      (do
        (funcao primeiro)
        (recur funcao (rest sequencia))))))

(meu-mapa println (range 10000))


