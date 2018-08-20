(ns connect4.subscriptions
  (:require [re-frame.core :refer [reg-sub subscribe]]))

(reg-sub
  :reds
  (fn [db [_ _]]
    (:reds db)))

(reg-sub
  :yellows
  (fn [db [_ _]]
    (:yellows db)))
