(ns clj-lwjgl-vplayer.core
  (:require [clj-lwjgl-vplayer.learn-opencv.learn-opengl.first :as first])
  (:gen-class))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(defn -main
  []
  (first/run)
  (println "Good"))
