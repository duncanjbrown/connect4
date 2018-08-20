(ns connect4.board)

(defn empty-board
  "Creates a rectangular empty board of the specified width
  and height."
  [w h]
  (vec (repeat w (vec (repeat h "-")))))

(defn board-view
  [board]
  [:div#board
    (for [y (range (count board))]
      [:div.row
        (for [x (range (count (first board)))]
          [:div.cell
            (get-in board [y x])])])]) 

(defn populate
  "Take red cells as sets of [y x] coords and write them into a board"
  [board reds]
  (reduce (fn [board coordinates]
            (assoc-in board coordinates "X"))
          board
          reds))

