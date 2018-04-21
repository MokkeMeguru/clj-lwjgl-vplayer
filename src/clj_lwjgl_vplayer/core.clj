(ns clj-lwjgl-vplayer.core
  (:import (org.lwjgl.opengl GL GL11)
           (org.lwjgl Version)
           (org.lwjgl.glfw GLFW Callbacks
                           GLFWErrorCallback GLFWKeyCallback))
  (:require [clj-lwjgl-vplayer.video-loader :as vl])
  (:gen-class))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; play video
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defonce window (ref 0))

;; raise
;; (def example-filename "assets/world_is_mine.mp4")
;;(vl/read-frame)
;;(type @vl/my-frame-buffer)

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
    (vl/read-frame)))

(defn loop_ []
  (GL/createCapabilities)
  (GL11/glClearColor 1.0 0.0 0.0 0.0)
  (while (not (GLFW/glfwWindowShouldClose @window))
    (display)
    (Thread/sleep 30) ;; this sleep to core.async
    (GLFW/glfwSwapBuffers @window)
    (GLFW/glfwPollEvents)))

(defn init []
  (let [info @vl/info]
    (.set (GLFWErrorCallback/createPrint System/err))
    (when (not (GLFW/glfwInit))
      (throw (IllegalStateException. (str "Unable to initialize GLFW"))))
    (GLFW/glfwDefaultWindowHints)
    (GLFW/glfwWindowHint GLFW/GLFW_VISIBLE GLFW/GLFW_FALSE)
    (let [width (int (:width info))
          height (int (:height info))]
      (dosync
       (ref-set window (GLFW/glfwCreateWindow width height "Hello World!" 0 0)))
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
    (init)
    (loop_)
    (GLFW/glfwDestroyWindow @window) ;; add
    (finally
      (Callbacks/glfwFreeCallbacks @window)
      (.free (GLFW/glfwSetErrorCallback nil))
      (GLFW/glfwTerminate)))) ;; add

;; (run)

(defn -main
  []
  (println "Good"))
