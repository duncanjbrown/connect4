(ns connect4.subscriptions
  (:require [re-frame.core :refer [reg-sub subscribe]]))

(println "Hello")

(reg-sub
  :reds
  (fn [db [_ _]]
    (println "sub fired")
    (:reds db)))
