(ns game-of-life.scenes
  (:require [portfolio.replicant :refer-macros [defscene]]
            [portfolio.ui :as portfolio]
            [game-of-life.ui :as ui]
            [replicant.dom :as r]))

(r/set-dispatch! (fn [_ event-data] (prn event-data)))

(defscene empty-cell
  (ui/render-cell {:clickable? true :alive? true}))


(defscene render-ui-inactive-simulation
  (let [simulate? false
        tic-counter 0]
    (ui/render-control-ui simulate? tic-counter)))


(defscene render-ui-active-simulation
  (let [simulate? true
        tic-counter 999]
    (ui/render-control-ui simulate? tic-counter)))


;; (defscene empty-board

;;   (let [event-func (fn [_] #(js/console.log "clicked " %1 %2))]
;;     (->> {:cells [[{:alive? true :on-click event-func} {:alive? true :on-click event-func}]
;;                   [{:alive? true :on-click event-func} {:alive? true :on-click event-func}]]
;;           :nrows 2
;;           :ncols 2
;;           :simulate false
;;           :tick 0}
;;          ui/game->ui-data
;;          ui/render-game)))


(defn main []
  (portfolio/start!
   {:config
    {:css-paths ["/styles.css"]
     :viewport/defaults
     {:background/background-color "#fdeddd"}}}))