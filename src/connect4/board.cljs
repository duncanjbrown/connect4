(ns connect4.board)

(defn empty-board
  "Creates a rectangular empty board of the specified width
  and height."
  [w h]
  (vec (repeat w (vec (repeat h nil)))))

(def game-board (empty-board 7 6))

(defn board-view
  [board]
  [:div#board
    (for [y (range (count board))]
      [:div.row
        (for [x (range (count (first board)))]
          [:div.cell
            (let [value (get-in board [y x])]
              (case value
                nil [:span.blank "_"]
                :red [:span.red "O"]
                :yellow [:span.yellow "O"]))])])])

(defn populate
  "Take cells as sets of [y x] coords and write them into a board"
  [board cells colour]
  (reduce (fn [board coordinates]
            (assoc-in board coordinates colour))
          board
          cells))

(defn next-coord-in-col
  "Coords appear as [y x] and [0 0] is at top left"
  [col coords col-max]
  (let [pieces-in-col (filter #(= col (second %)) coords)]
    [(- col-max (count pieces-in-col)) col]))

