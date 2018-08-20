(ns connect4.events
  (:require [re-frame.core :refer [reg-event-db]]))

(defn- next-coord-in-col
  [col coords]
  (let [pieces-in-col (filter #(= col (second %)) coords)]
    [(count pieces-in-col) col]))

(reg-event-db
  :initialize
  (println "init")
  (fn [_ _]
    {:reds #{[1 2]}}))

(reg-event-db
  :add-red
  (fn [db _]
    (println db)
    (update-in db [:reds]
      conj (next-coord-in-col
             3
             (:reds db)))))
