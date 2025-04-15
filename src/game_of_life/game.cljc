(ns game-of-life.game)

(defn create-matrix [rows cols]
  (vec (repeat rows (vec (repeat cols {:alive? false})))))

(defn create-game [{:keys [cols rows]}]
  {:cells (create-matrix rows cols)
   :nrows rows
   :ncols cols
   :simulate? false
   :interval-id nil
   :tic-evolution 0
   ;
   })

(defn mark-cell [game y x]
  (update-in game [:cells y x :alive?] not))

(defn neighbors [game i j]
  (let [directions [[-1 -1] [-1 0] [-1 1]
                    [0 -1]          [0 1]
                    [1 -1] [1 0] [1 1]]]
    (for [[dx dy] directions
          :let [nx (+ i dx)
                ny (+ j dy)]]
      (get-in game [nx ny]))))

(defn count-neighbors [game i j]
  (->> (neighbors game i j)
       (filter #(and (:alive? %) true))
       (count)))


(defn advance-board [cells]
  (mapv (fn [row-index row]
          (mapv (fn [col-index cell]
                  (let [count (count-neighbors cells   row-index col-index)]
                    (cond (and (< count 2) (:alive? cell)) {:alive? false}
                          (and (>= count 2) (< count 4) (:alive? cell)) {:alive? true}
                          (and (:alive? cell) (> count 3)) {:alive? false} ;; overpopulation case
                          (and (not (:alive? cell)) (= count 3)) {:alive? true} ;; zombie cell
                          :else {:alive? false}))
                    ;
                  )
                (range (count row)) row))
        (range (count cells)) cells))


(defn evolve
  "Advances the game state by one tic"
  [{:keys [cells] :as board-info}]
  (let [new-state (advance-board cells)
        meta-data (dissoc board-info :cells)
        meta-data-evolved (update meta-data :tic-evolution inc)
        result (assoc meta-data-evolved :cells new-state)]
    result))


(defn toggle-tic-func [game store]
  (if (:simulate? game)
    (do
      (js/clearInterval (:interval-id game))
      (-> game
          (update  :simulate? not)
          (assoc :interval-id nil)))
    (-> game
        (assoc :interval-id (js/setInterval #(swap! store evolve) 250))
        (update :simulate? not))))

(defn flip-cell [cell]
  (let [flip? (< (rand) 0.18)]
    (if flip? (update cell :alive? not) cell)))

(defn random-flip [cells]
  (mapv (fn [row] (mapv flip-cell row)) cells))

(defn randomize [{:keys [cells] :as board-info}]
  (let [randomized-cells (random-flip cells)
        meta-data (dissoc board-info :cells)
        new-game-state  (assoc meta-data :cells randomized-cells)]
    new-game-state))