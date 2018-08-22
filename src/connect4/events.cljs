(ns connect4.events
  (:require 
    [connect4.board :as board]
    [re-frame.core :refer [reg-event-db reg-event-fx clear-event]]))

(reg-event-db
  :initialize
  (fn [_ _]
    {:red #{}
     :yellow #{}
     :current-player :red
     :next-player :yellow
     :winners nil
     :winning-player nil
     :cursor-pos 0}))

(reg-event-db
  :turn-taken
  (fn [db [_ player]]
    (if (not-empty (:winners db))
      (do
        (assoc db :winning-player player))
      db)))

(def board-max-x (count (first board/game-board)))
(def board-max-y (count board/game-board))

(reg-event-fx
  :drop-piece
  (fn [{:keys [db]} _]
    (let [current-player (:current-player db)
          all-pieces (clojure.set/union (:red db) (:yellow db))
          new-piece (board/next-coord-in-col (:cursor-pos db) all-pieces (dec board-max-y))
          current-player-new-pieces (conj (current-player db) new-piece)
          next-player (:next-player db)]
      {:db
        (-> db
          (assoc current-player current-player-new-pieces)
          (assoc :winners (board/find-winners-from-origin new-piece current-player-new-pieces))
          (assoc :current-player next-player)
          (assoc :next-player current-player))
        :dispatch [:turn-taken current-player]})))

(reg-event-db
  :cursor-pos
  (fn [db [_ increment]]
    (let [current-pos (:cursor-pos db)]
      (assoc db :cursor-pos
        (mod
          (+ current-pos increment)
          board-max-x)))))
