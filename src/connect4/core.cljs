(ns ^:figwheel-hooks connect4.core
  (:require 
    [connect4.board :as board]
    [connect4.subscriptions]
    [connect4.events]
    [reagent.core :as r]
    [re-frame.core :as rf]))

(enable-console-print!)

(def empty-board (board/empty-board 7 6))

(defn app
  []
  (println "inside app")
  (let [reds (rf/subscribe [:reds])]
    [:div#app
      [board/board-view (board/populate empty-board @reds)]
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
