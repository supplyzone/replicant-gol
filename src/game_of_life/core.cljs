(ns game-of-life.core
  (:require [replicant.dom :as r]
            [game-of-life.game :as game]
            [game-of-life.ui :as ui]))

(def square-length 20) ;; pixel
(def gap 1) ;; pixel

(defn start-new-game [store rows cols]

  (let [board (game/create-game {:cols cols :rows rows})]
    (reset! store board)))

(defn main []
  (let [store (atom nil)
        el (js/document.getElementById "app")
        board-el (js/document.querySelector ".board")
        board-container-width (.-offsetWidth board-el)
        board-container-height (.-offsetHeight board-el)
        cols (Math/floor (quot board-container-width (+ square-length gap)))
        rows (Math/floor (quot board-container-height (+ square-length gap)))
        ;
        ]

    (r/set-dispatch!
     (fn [_ [action & args]]
       (case action
         :flip-cell (apply swap! store game/mark-cell args)
         :reset (start-new-game store rows cols)
         :simulate (swap! store game/toggle-tic-func store)
         :pause (swap! store game/toggle-tic-func store)
         :randomize (swap! store game/randomize store)
         ;
         )))

    (add-watch store ::render
               (fn [_ _ _ game]
                 (->>
                  (ui/game->ui-data game)
                  ui/render-game
                  (r/render el))))

    ;; init first draw
    (start-new-game store rows cols))

  ())