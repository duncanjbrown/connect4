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

;;
;; Judging
;;
(reg-event-fx
  :adjudicate
  (fn [{:keys [db]} [_ [player winning-runs next-player]]]
    (if (not-empty winning-runs)
      {:dispatch [:won [player winning-runs]]}
      {:dispatch [:next-turn next-player]})))

(def board-max-x (count (first board/game-board)))
(def board-max-y (count board/game-board))

(reg-event-db
  :won
  (fn [db [_ [player winning-runs]]]
    (-> db
      (assoc :winners winning-runs)
      (assoc :winning-player player))))

(reg-event-db
  :next-turn
  (fn [db [_ next-player]]
    (let [current-player (:current-player db)]
      (-> db
        (assoc :current-player next-player)
        (assoc :next-player current-player)))))

;;
;; Playing
;;

(def reject-unless-playing
  (re-frame.core/->interceptor
    :id     :reject-unless-playing
    :before (fn [context]
              (let [{:keys [db _]} (:coeffects context)]
                (if (:winning-player db)
                  (assoc context :queue [])
                  context)))))

(reg-event-fx
  :drop-piece
  [reject-unless-playing]
  (fn [{:keys [db]} _]
    (let [current-player (:current-player db)
          all-pieces (clojure.set/union (:red db) (:yellow db))
          new-piece (board/next-coord-in-col (:cursor-pos db) all-pieces (dec board-max-y))
          current-player-new-pieces (conj (current-player db) new-piece)
          winning-runs (board/find-winners-from-origin new-piece current-player-new-pieces)
          next-player (:next-player db)]
      {:db (assoc db current-player current-player-new-pieces)
       :dispatch [:adjudicate [current-player winning-runs next-player]]})))

(reg-event-db
  :cursor-pos
  (fn [db [_ increment]]
    (let [current-pos (:cursor-pos db)]
      (assoc db :cursor-pos
        (mod
          (+ current-pos increment)
          board-max-x)))))
