(ns connect4.events
  (:require 
    [connect4.board :as board]
    [re-frame.core :refer [reg-event-db]]))

(reg-event-db
  :initialize
  (fn [_ _]
    {:reds #{}}))

(reg-event-db
  :add-red
  (fn [db _]
    (update-in db [:reds]
      conj (board/next-coord-in-col
             (rand-int ((comp dec count) (first board/game-board)))
             (:reds db)
             ((comp dec count) board/game-board)))))
