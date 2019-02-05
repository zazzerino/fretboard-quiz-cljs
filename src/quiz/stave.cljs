(ns quiz.stave
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            ["vexflow" :as Vex]
            [quiz.utils :as utils]
            [quiz.subs :as subs]))

(def vf (.-Flow Vex))

(defn draw-stave [id width height]
  (utils/remove-children (.getElementById js/document id))
  (let [renderer (doto (new (.-Renderer vf)
                            (.getElementById js/document "stave-inner")
                            (-> vf .-Renderer .-Backends .-SVG))
                   (.resize width height))
        context (.getContext renderer)
        stave (doto (new (.-Stave vf) 0 0 (dec width))
                (.addClef "treble")
                (.setContext context)
                .draw)]
    {:renderer renderer :context context :stave stave}))

(defn add-slash [notename]
  (clojure.string/replace notename #"(\d)" "/$1"))

(def note-regex #"([a-gA-G])(#{1,2}||b{1,2})?(\d)")

(defn parse-note [notename]
  (if-let [[_ white-key accidental octave] (re-matches note-regex notename)]
    {:white-key white-key :accidental accidental :octave octave}))

(defn draw-note [context stave notename]
  (let [stave-note (doto (new (.-StaveNote vf)
                              (clj->js {:keys [(add-slash notename)]
                                        :duration "w"}))
                     (.setExtraLeftPx (/ (.-width stave) 3.8)))
        accidental (:accidental (parse-note notename))]
    (if accidental
      (.addAccidental stave-note 0 (new (.-Accidental vf) accidental)))
    (.FormatAndDraw (.-Formatter vf) context stave #js [stave-note])))

(defn stave-inner []
  (let [width 200
        height 100
        vf-objs (atom nil)]
    (reagent/create-class
     {:display-name "stave-inner"
      :reagent-render (fn [] [:div {:id "stave-inner"}])
      :component-did-mount
      (fn [this]
        (reset! vf-objs (draw-stave "stave-inner" 200 100))
        (draw-note (:context @vf-objs) (:stave @vf-objs)
                   (:note (reagent/props this))))
      :component-did-update
      (fn [this]
        (reset! vf-objs (draw-stave "stave-inner" 200 100))
        (draw-note (:context @vf-objs) (:stave @vf-objs)
                   (:note (reagent/props this))))})))

(defn stave-outer []
  (let [note (re-frame/subscribe [::subs/note-to-id])]
    (fn []
      [:div {:class "stave-outer"}
       [stave-inner {:note @note}]])))
