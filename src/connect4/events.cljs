(ns connect4.events
  (:require 
    [connect4.board :as board]
    [re-frame.core :refer [reg-event-db]]))

(reg-event-db
  :initialize
  (fn [_ _]
    {:red #{}
     :yellow #{}
     :current-player :red
     :next-player :yellow
     :cursor-pos 0}))

(def board-max-x (count (first board/game-board)))
(def board-max-y (count board/game-board))

(reg-event-db
  :drop-piece
  (fn [db _]
    (let [current-player (:current-player db)
          next-player (:next-player db)
          all-pieces (clojure.set/union (:red db) (:yellow db))]
      (-> db
        (update-in [current-player]
          conj (board/next-coord-in-col
                (:cursor-pos db)
                all-pieces
                (dec board-max-y)))
        (assoc :current-player next-player)
        (assoc :next-player current-player)))))

(reg-event-db
  :cursor-pos
  (fn [db [_ increment]]
    (let [current-pos (:cursor-pos db)]
      (assoc db :cursor-pos
        (mod
          (+ current-pos increment)
          board-max-x)))))
