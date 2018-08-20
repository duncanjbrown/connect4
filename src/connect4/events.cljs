(ns connect4.events
  (:require 
    [connect4.board :as board]
    [re-frame.core :refer [reg-event-db]]))

(reg-event-db
  :initialize
  (fn [_ _]
    {:reds #{}
     :cursor-pos 0}))

(def board-max-x (count (first board/game-board)))
(def board-max-y (count board/game-board))

(reg-event-db
  :drop-piece
  (fn [db _]
    (update-in db [:reds]
      conj (board/next-coord-in-col
             (:cursor-pos db)
             (:reds db)
             (dec board-max-y))))) 

(reg-event-db
  :cursor-pos
  (fn [db [_ increment]]
    (let [current-pos (:cursor-pos db)]
      (assoc db :cursor-pos
        (mod
          (+ current-pos increment)
          board-max-x)))))
