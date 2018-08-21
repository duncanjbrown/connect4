(ns connect4.subscriptions
  (:require [re-frame.core :refer [reg-sub subscribe]]))

(reg-sub
  :reds
  (fn [db [_ _]]
    (:red db)))

(reg-sub
  :yellows
  (fn [db [_ _]]
    (:yellow db)))

(reg-sub
  :cursor-pos
  (fn [db [_ _]]
   (:cursor-pos db)))
