(ns clj-lwjgl-vplayer.core
  (:import (org.lwjgl.opengl GL GL11)
           (org.lwjgl Version)
           (org.lwjgl.glfw GLFW Callbacks
                           GLFWErrorCallback GLFWKeyCallback)
           (java.util Timer TimerTask))
  (:require [clj-lwjgl-vplayer.video-loader :as vl]
            [clj-lwjgl-vplayer.audio-loader :as al
             :refer [init-audio load-audio play-sound close-audio]]
            [clojure.core.async :as async
             :refer [chan go-loop go >! >!! <! <!! timeout]]
            [clojure.java.io :as io :refer [input-stream file]])
  (:gen-class))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; play video
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def status (atom
             {:video-name
              "./assets/world_is_mine.mp4"
              :audio-name
              "./assets/LAMUNATION.ogg"}))

(def waiter (atom 15))

(def window (volatile! 0))

(defn load-video [#^String file-name]
  (vl/read-video-file file-name)
  (vl/set-infomation))

(defn display []
  (let [info @vl/info]
    (GL11/glClear GL11/GL_COLOR_BUFFER_BIT)
    (GL11/glDrawPixels
     (int (:width info))
     (int (:height info))
     GL11/GL_RGB
     GL11/GL_UNSIGNED_BYTE
     @vl/my-frame-buffer)
    (GL11/glFlush)
    (vl/read-frame)
    ))

(defn loop_ []
  (let [wait-time (/ 1000 (:fps @vl/info))
        ch (chan)
        waiter @waiter]
    (println (:fps @vl/info))
    (GL/createCapabilities)
    (GL11/glClearColor 1.0 0.0 0.0 0.0)
    (go-loop []
      (when (>! ch true)
        (<! (timeout wait-time))
        (recur)))
    (loop [frames (:frames @vl/info)] ;; go-loop だと 別スレッドにされてしまう？
      ;; loop にすることでメインスレッドで実行することができる。
      (if (< 0 frames)
          (if (<!! ch)
            (when (not (GLFW/glfwWindowShouldClose @window))
              (do
                (if-not (== (mod frames waiter) 0)
                  (display)
                  (do (display)
                      (display)
                      (dec frames)))
                (GLFW/glfwSwapBuffers @window)
                (GLFW/glfwPollEvents))
              (recur (dec frames))))))))

(defn init []
  (let [info (:video-info @status)]
    (.set (GLFWErrorCallback/createPrint System/err))
    (when (not (GLFW/glfwInit))
      (throw (IllegalStateException. (str "Unable to initialize GLFW"))))
    (GLFW/glfwDefaultWindowHints)
    (GLFW/glfwWindowHint GLFW/GLFW_VISIBLE GLFW/GLFW_FALSE)
    (let [width (int (:width info))
          height (int (:height info))]
      (vreset! window (GLFW/glfwCreateWindow width height "Hello World!" 0 0))
      (when (nil? @window)
        (throw (RuntimeException. "Failed to create GLFW window")))
      (GLFW/glfwSetKeyCallback
       @window
       (proxy [GLFWKeyCallback] []
         (invoke [w k scancode action mods]
           (when (and (== k GLFW/GLFW_KEY_ESCAPE)
                      (== action GLFW/GLFW_RELEASE))
             (GLFW/glfwSetWindowShouldClose @window true)))))
      (let [vidmode (GLFW/glfwGetVideoMode (GLFW/glfwGetPrimaryMonitor))]
        (GLFW/glfwSetWindowPos
         @window
         (/ (- (.width vidmode) width) 2)
         (/ (- (.height vidmode) height) 2)))
      (GLFW/glfwMakeContextCurrent @window)
      (GLFW/glfwSwapInterval 1)
      (GLFW/glfwShowWindow @window))))

(defn run []
  (println "Hello LWJGL " +  (Version/getVersion))
  (try
    (init-audio)
    (load-audio (:audio-name @status))
    (load-video (:video-name @status))
    (init)
    (play-sound)
    (loop_)
    (close-audio)
    (GLFW/glfwDestroyWindow @window) ;; add
    (finally
      (Callbacks/glfwFreeCallbacks @window)
      (.free (GLFW/glfwSetErrorCallback nil))
      (GLFW/glfwTerminate)))) ;; add

;;  (run)

(defn -main
  []
  (println (.getCanonicalPath (io/file "../assets/LAMUNATION.mp4")))  
  (swap! status assoc :video-name "../assets/world_is_mine.mp4")
  (swap! status assoc :audio-name "../assets/world_is_mine.ogg")
  (run)
  (println "Good"))


;;(def t (atom {:a 1 :b {:c 3}}))
;;(swap! t assoc-in [:b :c] 4 )

