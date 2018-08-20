(ns connect4.test-runner
  (:require
   [cljs.test]
   [cljs-test-display.core]
   [connect4.board-test])
  (:require-macros
   [cljs.test]))

(cljs.test/run-tests
 (cljs-test-display.core/init! "app-tests") ;;<-- initialize cljs-test-display here
 'connect4.board-test)
