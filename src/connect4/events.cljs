(ns connect4.events
  (:require 
    [connect4.board :as board]
    [re-frame.core :refer [reg-event-db reg-event-fx clear-event]]))

(def default-db {:red #{}
                 :yellow #{}
                 :state :playing
                 :current-player :red
                 :next-player :yellow
                 :winners nil
                 :winning-player nil
                 :cursor-pos 0})
(reg-event-db
  :reset
  (fn [db _]
    (merge db default-db)))

(reg-event-db
  :initialize
  (fn [db _]
    default-db))

;;
;; Judging
;;
(reg-event-fx
  :adjudicate
  (fn [{:keys [db]} [_ [player winning-runs next-player]]]
    (cond
      (not-empty winning-runs) {:dispatch [:won [player winning-runs]]}

      (=
       (count (clojure.set/union (:red db) (:yellow db)))
       (* 7 6)) {:dispatch [:draw]}

      :else {:dispatch [:next-turn next-player]})))

(def board-max-x (count (first board/game-board)))
(def board-max-y (count board/game-board))

(reg-event-db
  :won
  (fn [db [_ [player winning-runs]]]
    (-> db
      (assoc :winners winning-runs)
      (assoc :winning-player player)
      (assoc :state :won))))

(reg-event-db
  :draw
  (fn [db [_ [player winning-runs]]]
    (assoc db :state :draw)))

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
                (if (not= (:state db) :playing)
                  (assoc context :queue [])
                  context)))))

(defn all-pieces
  [db]
  (clojure.set/union (:red db) (:yellow db)))

(reg-event-fx
  :drop-piece
  [reject-unless-playing]
  (fn [{:keys [db]} _]
    (if-let [new-piece (board/next-coord-in-col (:cursor-pos db) (all-pieces db) (dec board-max-y))]
      (let [current-player (:current-player db)
            current-player-new-pieces (conj (current-player db) new-piece)
            winning-runs (board/find-winners-from-origin new-piece current-player-new-pieces)
            next-player (:next-player db)]
        {:db (assoc db current-player current-player-new-pieces)
         :dispatch [:adjudicate [current-player winning-runs next-player]]}))))

(reg-event-db
  :cursor-pos
  (fn [db [_ increment]]
    (let [current-pos (:cursor-pos db)]
      (assoc db :cursor-pos
        (mod
          (+ current-pos increment)
          board-max-x)))))
