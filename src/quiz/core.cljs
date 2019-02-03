(ns quiz.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [quiz.events :as events]
            [quiz.views :as views]))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "root")))

(defn ^:dev/after-load main []
  (re-frame/dispatch-sync [::events/initialize-db])
  (mount-root))
