(ns clj-lwjgl-vplayer.learn-opencv.learn-opengl.first
  (:import (org.lwjgl.opengl GL GL11)
           (org.lwjgl Version)
           (org.lwjgl.glfw GLFW Callbacks
                           GLFWErrorCallback GLFWKeyCallback)))

(defonce param (atom {:width 300
                      :height 300}))

(defonce window (ref 0))

(defn loop_ []
  (GL/createCapabilities)
  (GL11/glClearColor 1.0 0.0 0.0 0.0)
  (while (not (GLFW/glfwWindowShouldClose @window))
    (GL11/glClear
     (bit-or GL11/GL_COLOR_BUFFER_BIT  GL11/GL_DEPTH_BUFFER_BIT))
    (GLFW/glfwSwapBuffers @window)
    (GLFW/glfwPollEvents)))

(defn init []
  (.set (GLFWErrorCallback/createPrint System/err))
  (when (not (GLFW/glfwInit))
    (throw (IllegalStateException. (str "Unable to initialize GLFW"))))
  (GLFW/glfwDefaultWindowHints)
  (GLFW/glfwWindowHint GLFW/GLFW_VISIBLE GLFW/GLFW_FALSE)
  (let [width (:width @param)
        height (:height @param)]
    (dosync
     (ref-set window (GLFW/glfwCreateWindow width height "Hello World!" 0 0)))
    (when (nil? @window)
      (throw (RuntimeException. "Failed to create GLFW window")))
    (GLFW/glfwSetKeyCallback
     @window
     (proxy [GLFWKeyCallback] []
       (invoke [window k scancode action mods]
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
    (GLFW/glfwShowWindow @window)))

(defn run []
  (println "Hello LWJGL " +  (Version/getVersion))
  (try
    (init)
    (loop_)
    (finally
      (Callbacks/glfwFreeCallbacks @window)
      (.free (GLFW/glfwSetErrorCallback nil)))))

(defn -main []
  (run))

;; (-main)
