(ns connect4.board)

(defn empty-board
  "Creates a rectangular empty board of the specified width
  and height."
  [w h]
  (vec (repeat w (vec (repeat h nil)))))

(def game-board (empty-board 7 6))

(defn board-cell
  [value]
  (do
    (case value
      nil [:span.blank "·"]
      :winner [:span.winner "●"]
      :red [:span.red "●"]
      :yellow [:span.yellow "●"])))

(defn board-view
  [board]
  [:div#board
    (for [y (range (count board))]
      [:div.row
        (for [x (range (count (first board)))]
          [:div.cell
            (let [value (get-in board [y x])]
              [board-cell value])])])])

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
  (let [pieces-in-col (filter #(= col (second %)) coords)
        next-y-coord (- col-max (count pieces-in-col))]
    (if (neg? next-y-coord)
      nil
      [next-y-coord col])))

(defn consecutive-pieces
  [available-pieces transform origin]
  (let [candidate-coords (iterate #(vec (map + transform %)) origin)]
    (set (take-while available-pieces candidate-coords))))

(defn find-winners-from-origin
  [origin available-pieces]
  (let [run-right (consecutive-pieces available-pieces [0 1] origin)
        run-left (consecutive-pieces available-pieces [0 -1] origin)
        run-up (consecutive-pieces available-pieces [1 0] origin)
        run-down (consecutive-pieces available-pieces [-1 0] origin)
        run-rdiag-up (consecutive-pieces available-pieces [-1 1] origin)
        run-rdiag-down (consecutive-pieces available-pieces [1 -1] origin)
        run-ldiag-up (consecutive-pieces available-pieces [-1 -1] origin)
        run-ldiag-down (consecutive-pieces available-pieces [1 1] origin)]
    (filter #(= 4 (count %))
      [(clojure.set/union run-right run-left)
       (clojure.set/union run-up run-down)
       (clojure.set/union run-rdiag-up run-rdiag-down)
       (clojure.set/union run-ldiag-up run-ldiag-down)])))
