(ns game-of-life.game-test
  (:require [clojure.test :refer [deftest is testing]]
            [game-of-life.game :as game]))

(deftest test-neighborhood-relationships

  (testing "Cell has no neighbors"
    (is (=
         (-> [[{} {}            {}]
              [{} {:alive? true} {}]
              [{} {}            {}]]
             (game/count-neighbors 1 1)))
        0))

  (testing "Cell has 1 neighbors"
    (is (=
         (-> [[{} {:alive? true}            {}]
              [{} {:alive? true} {}]
              [{} {}            {}]]
             (game/count-neighbors 1 1))

         1)))


  (testing "Cell has 2neighbors"
    (is (=
         (-> [[{:alive? true} {:alive? true}            {}]
              [{} {:alive? true} {}]
              [{} {}            {}]]
             (game/count-neighbors 1 1))

         2)))

  (testing "Cell has 3 neighbors"
    (is (=
         (-> [[{:alive? true} {:alive? true}            {}]
              [{} {:alive? true} {}]
              [{} {:alive? true}            {}]]
             (game/count-neighbors 1 1))

         3)))

  (testing "Cell has 4 neighbors"
    (is (=
         (-> [[{:alive? true} {:alive? true}            {}]
              [{} {:alive? true} {}]
              [{} {:alive? true}            {:alive? true}]]
             (game/count-neighbors 1 1))

         4))))

(deftest test-game-state-transition

  (testing "cell with less than 2 neighbors dies"

    (is (=
         (-> [[{:alive? false} {:alive? false} {:alive? false}]
              [{:alive? false} {:alive? true} {:alive? false}]
              [{:alive? false} {:alive? false} {:alive? false}]]
             game/advance-board)
         [[{:alive? false} {:alive? false} {:alive? false}]
          [{:alive? false} {:alive? false} {:alive? false}]
          [{:alive? false} {:alive? false} {:alive? false}]])))

  (testing "cell with 2 neighbors stays alive"

    (is (=
         (-> [[{:alive? false} {:alive? true} {:alive? true}]
              [{:alive? false} {:alive? true} {:alive? false}]
              [{:alive? false} {:alive? false} {:alive? false}]]
             game/advance-board)
         [[{:alive? false} {:alive? true} {:alive? true}]
          [{:alive? false} {:alive? true} {:alive? true}]
          [{:alive? false} {:alive? false} {:alive? false}]])))


  (testing "test case glider"

    (is (=
         (-> [[{:alive? false} {:alive? false} {:alive? true} {:alive? false}]
              [{:alive? true}  {:alive? false} {:alive? true} {:alive? false}]
              [{:alive? false} {:alive? true}  {:alive? true} {:alive? false}]]
             game/advance-board)
         [[{:alive? false} {:alive? true}   {:alive? false} {:alive? false}]
          [{:alive? false} {:alive? false} {:alive? true}  {:alive? true}]
          [{:alive? false} {:alive? true}  {:alive? true}  {:alive? false}]])))



  (testing "Evolve game board: state+metadata"

    (is (=
         (-> {:cells [[{:alive? true} {:alive? true} {:alive? true}]
                      [{:alive? false} {:alive? true} {:alive? false}]
                      [{:alive? false} {:alive? false} {:alive? true}]]
              :nrows 3
              :ncols 3
              :simulate? true
              :interval-id nil
              :tic-evolution 0
   ;
              }
             game/evolve)
         {:cells [[{:alive? true} {:alive? true} {:alive? true}]
                  [{:alive? true} {:alive? false} {:alive? false}]
                  [{:alive? false} {:alive? false} {:alive? false}]]
          :nrows 3
          :ncols 3
          :simulate? true
          :interval-id nil
          :tic-evolution 1
          ;
          }))))

(deftest test-neighborhood-function
  (testing "Test neighborhood function"

    (is (=
         (-> (game/create-matrix 36 27)
             (assoc-in [0 0 :alive?] true)
             (assoc-in [0 1 :alive?] true)
             (assoc-in [0 2 :alive?] true)
             (assoc-in [1 0 :alive?] true)
             (assoc-in [1 1 :alive?] true)
             (assoc-in [1 2 :alive?] true)
             (game/count-neighbors 0 0))
           ;
         3))))