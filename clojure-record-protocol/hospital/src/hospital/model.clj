(ns hospital.model)

; Uma variação baseada na palestra do Sean Devlin, mas com extend-type e gregoriancalendar
; From Sean Devlin's talk on protocols at Clojure Conj
(defprotocol Dateable
  (to-ms [this]))

(extend-type java.lang.Number
  Dateable
  (to-ms [this]
    this))

(extend-type java.util.Date
  Dateable
  (to-ms [this]
    (.getTime this)))

(extend-type java.util.Calendar
  Dateable
  (to-ms [this]
    (to-ms (.getTime this))))