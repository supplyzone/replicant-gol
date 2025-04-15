(ns game-of-life.ui)

(defn render-cell [{:keys [on-click alive?]}]
  [:div.cell
   {:on {:click on-click}
    :class (cond-> []
             alive? (conj "alive")
             true (conj "clickable")
             ;
             )}])

(defn render-board [cells]
  [:div.board
   (for [row cells]

     (for [cell row]
       (render-cell cell)))])

(defn render-control-ui [simulate? tic-counter]
  [:div.ui-section
   [:div.ui-counter
    [:h1 {:class (when (not simulate?) "animated-text")} "Game of life " (if (not (= tic-counter 0)) (str "step " tic-counter) "")]]

   [:div.ui-button-control
    [:button  {:on {:click [:reset]} :class (when simulate? "greyed-out")}  "🗑️ reset"]
    [:button  {:on {:click [:simulate]} :class (when simulate? "greyed-out")} "▶️ simulate"]
    [:button  {:on {:click [:pause]} :style (when (not simulate?) {:visibility "hidden"})} "⏸️ pause"]
    [:button  {:on {:click [:randomize]} :class (when simulate? "greyed-out")} "🎰 random"]]
   ;
   ]
  ;
  )

(defn render-game [{:keys [cells simulate? tic-evolution]}]
  [:div
   (render-control-ui simulate? tic-evolution)
   (render-board cells)
  ;
   ])



(defn game->ui-data [{:keys [cells nrows ncols simulate? tic-evolution interval-id]}]
  {:cells
   (vec
    (for [row (range nrows)]
      (vec
       (for [col (range ncols)]
         {:on-click [:flip-cell row col]
          :alive? (get-in cells [row col :alive?])})
       ;
       )))
   :ncols ncols
   :nrows nrows
   :simulate? simulate?
   :tic-evolution tic-evolution
   :interval-id interval-id})