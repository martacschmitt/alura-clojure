(ns hospital.core
  (:use [clojure pprint])
  (:require [hospital.model :as h.model]))

(let [hospital-do-gui (h.model/novo-hospital)]
  (pprint hospital-do-gui))

(pprint h.model/fila-vazia)


