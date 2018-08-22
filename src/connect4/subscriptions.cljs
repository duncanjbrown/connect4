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
  :winning-player
  (fn [db [_ _]]
    (:winning-player db)))

(reg-sub
  :state
  (fn [db [_ _]]
    (:state db)))

(reg-sub
  :winners
  (fn [db [_ _]]
    (apply clojure.set/union (:winners db))))

(reg-sub
  :cursor-pos
  (fn [db [_ _]]
   (:cursor-pos db)))
