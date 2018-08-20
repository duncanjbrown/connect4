(ns connect4.board-test
  (:require
    [connect4.board :as board]
    [cljs.test :refer-macros [deftest is testing run-tests]]))

(deftest empty-board-takes-height-and-width
  (is (= (board/empty-board 3 2) [[nil nil]
                                  [nil nil]
                                  [nil nil]]))
  (is (= (board/empty-board 1 1) [[nil]])))

(deftest populate-fills-red-coords
  (let [example-board (board/empty-board 3 3)]
    (is
      (=
        (board/populate example-board #{[1 1]})
        [[nil nil nil]
         [nil :red nil]
         [nil nil nil]]))
    (is
      (=
        (board/populate example-board #{[1 1] [1 2]})
        [[nil nil nil]
         [nil :red :red]
         [nil nil nil]]))))

(deftest next-coord-in-col
  (testing "it returns the bottom of the column when no pieces are present"
    (let [example-board (board/empty-board 3 3)]
      (is
        (=
          (board/next-coord-in-col 2 #{} 2)
          [2 2]))))
  (testing "it returns the next position up when a piece is present"
    (let [population #{[2 2]}]
      (is
        (=
          (board/next-coord-in-col 2 population 2)
          [1 2])))))
