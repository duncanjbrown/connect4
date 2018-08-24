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
        (board/populate example-board #{[1 1]} :red)
        [[nil nil nil]
         [nil :red nil]
         [nil nil nil]]))
    (is
      (=
        (board/populate example-board #{[1 1] [1 2]} :red)
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

(deftest consecutive-pieces
  (testing "it returns consecutive pieces"
    (let [population #{[2 2] [2 1] [2 0]}]
      (is (=
           #{[2 2] [2 1] [2 0]}
           (board/consecutive-pieces population [0 -1] [2 2])))))
  (testing "it does not return non-consecutive pieces"
    (let [population #{[2 2] [2 0]}]
      (is (=
            #{[2 2]}
            (board/consecutive-pieces population [0 -1] [2 2]))))))

(deftest find-winners-from-origin
  (testing "horizontal winners"
    (let [population #{[2 0] [2 1] [2 2] [2 3]}]
      (testing "it returns the set of coords for a horizontal winner to the right"
        (is (=
              [population]
              (board/find-winners-from-origin [2 0] population))))
      (testing "it returns the set of coords for a horizontal winner to the left"
        (is (=
              [population]
              (board/find-winners-from-origin [2 3] population))))
      (testing "it returns the set of coords for a horizontal winner split on the origin"
        (is (=
              [population]
              (board/find-winners-from-origin [2 1] population))))))

  (testing "vertical winners"
    (let [population #{[0 0] [1 0] [2 0] [3 0]}]
      (testing "it returns the set of coords for a vertical winner down"
        (is (=
              [population]
              (board/find-winners-from-origin [0 0] population))))
      (testing "it returns the set of coords for a vertical winner up"
        (is (=
              [population]
              (board/find-winners-from-origin [3 0] population))))
      (testing "it returns the set of coords for a vertical winner split on the origin"
        (is (=
              [population]
              (board/find-winners-from-origin [2 0] population))))))

  (testing "diagonal winners /"
    (let [population #{[3 0] [2 1] [1 2] [0 3]}]
      (testing "it returns the set of coords for a / winner up"
        (is (=
              [population]
              (board/find-winners-from-origin [3 0] population))))
      (testing "it returns the set of coords for a / winner down"
        (is (=
              [population]
              (board/find-winners-from-origin [0 3] population))))
      (testing "it returns the set of coords for a / winner split on the origin"
        (is (=
              [population]
              (board/find-winners-from-origin [2 1] population)))))

    (testing "diagonal winners \\"
      (let [population #{[0 0] [1 1] [2 2] [3 3]}]
        (testing "it returns the set of coords for a \\ winner down"
          (is (=
                [population]
                (board/find-winners-from-origin [0 0] population))))
        (testing "it returns the set of coords for a \\ winner up"
          (is (=
                [population]
                (board/find-winners-from-origin [3 3] population))))
        (testing "it returns the set of coords for a \\ winner split on the origin"
          (is (=
                [population]
                (board/find-winners-from-origin [1 1] population))))))))

    (testing "simultaneous winners"
      (let [population #{[0 0] [0 1] [0 2] [0 3] [1 0] [2 0] [3 0]}]
        (testing "a vector containing both winners is returned"
          (is (=
               [#{[0 0] [0 1] [0 2] [0 3]} #{[0 0] [1 0] [2 0] [3 0]}]
               (board/find-winners-from-origin [0 0] population))))))
