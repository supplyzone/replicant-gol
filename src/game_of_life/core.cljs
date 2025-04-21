(ns game-of-life.core
  (:require [replicant.dom :as r]
            [game-of-life.game :as game]
            [game-of-life.ui :as ui]))

(def square-length 20) ;; pixel
(def gap 1) ;; pixel


(defn calculate-board-dimensions []
  (let [board-el (js/document.querySelector ".board")
        board-container-width (.-offsetWidth board-el)
        board-container-height (.-offsetHeight board-el)
        num-cols (Math/floor (quot board-container-width (+ square-length gap)))
        num-rows (Math/floor (quot board-container-height (+ square-length gap)))]

    {:cols num-cols :rows num-rows}))

(defn reset-board [store]
  (let [board-dimensions (calculate-board-dimensions)
        cols (:cols board-dimensions)
        rows (:rows board-dimensions)
        board (game/create-game {:cols cols :rows rows})]
    (reset! store board)))


(defn perform-action [store action args]
  (case action
    :flip-cell (apply swap! store game/mark-cell args)
    :reset (reset-board store)
    :simulate (swap! store game/toggle-tic-func store)
    :pause (swap! store game/toggle-tic-func store)
    :randomize (swap! store game/randomize store)))

(defn main []
  (let [store (atom nil)
        el (js/document.getElementById "app")]

    (.addEventListener js/window "resize" (fn [_] (reset-board store)))

    (r/set-dispatch!
     (fn [_ [action & args]]
       (perform-action store action args)))

    (add-watch store ::render
               (fn [_ _ _ game]
                 (->>
                  (ui/game->ui-data game)
                  ui/render-game
                  (r/render el))))

    ;; init first draw
    (reset-board store))
    ;
  )