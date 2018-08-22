(ns ^:figwheel-hooks connect4.core
  (:require 
    [connect4.board :as board]
    [connect4.subscriptions]
    [connect4.events]
    [reagent.core :as r]
    [re-frame.core :as rf]
    [re-pressed.core :as rp]))

(enable-console-print!)

(defn cursor-ui
  [width pos]
  [:div.row
    (for [x (range width)]
      [:div.cell.cursor
        (if (= x pos) "V" "")])])

(defn app
  []
  (let [game-board board/game-board
        reds (rf/subscribe [:reds])
        yellows (rf/subscribe [:yellows])
        winners (rf/subscribe [:winners])
        game-state (rf/subscribe [:state])]
    [:div#app
      [:div.board
        [cursor-ui (count (first board/game-board)) @(rf/subscribe [:cursor-pos])]
        [board/board-view
          (-> game-board
              (board/populate @reds :red)
              (board/populate @yellows :yellow)
              (board/populate @winners :winner))]]

      (case @game-state
        :won
        [:p.outcome (str @(rf/subscribe [:winning-player]) " has won")]
        :draw
        [:p.outcome "It's a draw"]
        :playing nil)]))

(defn render []
  (let [node (.getElementById js/document "app")]
    (r/render [app] node)))

(defn rerender []
  (let [node (.getElementById js/document "app")]
    (r/unmount-component-at-node node)
    (render)))

(defn ^:after-load rerender-on-reload []
    (rerender))

(defn ^:export run
  []
  (rf/dispatch-sync [:initialize])
  (rf/dispatch-sync [::rp/add-keyboard-event-listener "keydown"])
  (rf/dispatch
    [::rp/set-keydown-rules
      {:event-keys [
                    [[:cursor-pos -1]
                     [{:which 37}]]
                    [[:drop-piece]
                     [{:which 32}]]
                    [[:cursor-pos 1]
                     [{:which 39}]]]}])
  (render))
