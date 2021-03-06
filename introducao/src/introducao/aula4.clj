(ns introducao.aula4)

(def precos [30 700 1000])

(println (precos 0))
(println (get precos 0))
(println (get precos 2))
;(println (precos 10))
(println "valor padrão nil" (get precos 10))
(println "valor padrão 0" (get precos 10 0))
(println "valor padrão 0, mas existe" (get precos 2 0))

(println (conj precos 5))
(println precos)

(println (+ 5 1))
(println (inc 5))
(println (update precos 0 inc))
(println (update precos 1 dec))
(println precos)

(defn soma-1
  [valor]
  (println "estou somando 1 em" valor)
  (+ valor 1))

(println (update precos 0 soma-1))

(defn soma-3
  [valor]
  (println "estou somando 3 em" valor)
  (+ valor 3))

(println (update precos 0 soma-3))


; CÓDIGO DA AULA ANTERIOR

(defn aplica-desconto?
  [valor-bruto]
  (> valor-bruto 100))

(defn valor-descontado
  "Retorna o valor com desconto de 10% se o valor bruto for estritamente maior que 100."
  [valor-bruto]
  (if (aplica-desconto? valor-bruto)
    (let [taxa-de-desconto (/ 10 100)
          desconto (* valor-bruto taxa-de-desconto)]
      (- valor-bruto desconto))
    valor-bruto))

(println precos)

(println "map" (map valor-descontado precos))

(println (range 10))
(println (filter even? (range 10)))

(println "vetor" precos)
(println "filter" (filter aplica-desconto? precos))

(println "map após o filter" (map valor-descontado (filter aplica-desconto? precos)))

(println "vetor" precos)
(println (reduce + precos))

(defn minha-soma
  [valor1 valor2]
  (println "somando" valor1 valor2)
  (+ valor1 valor2))

(println (reduce minha-soma precos))
(println (reduce minha-soma (range 10)))
(println (reduce minha-soma [15]))

(println (reduce minha-soma 0 precos))
(println (reduce minha-soma 0 [15]))
(println (reduce minha-soma 0 []))

