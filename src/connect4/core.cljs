(ns ^:figwheel-hooks connect4.core
  (:require 
    [connect4.board :as board]
    [connect4.subscriptions]
    [connect4.events]
    [reagent.core :as r]
    [re-frame.core :as rf]))

(enable-console-print!)

(defn app
  []
  (let [game-board board/game-board
        reds (rf/subscribe [:reds])
        yellows (rf/subscribe [:yellows])]
    [:div#app
      [board/board-view (board/populate game-board @reds)]
      [:a {:href "#"
            :on-click #(rf/dispatch [:add-red])}
        "Go"]]))

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
  (render))
