(ns clj-lwjgl-vplayer.learn-opencv.learn-opengl.second
  (:import (org.lwjgl.opengl GL GL11)
           (org.lwjgl Version)
           (org.lwjgl.glfw GLFW Callbacks
                           GLFWErrorCallback GLFWKeyCallback)
           (org.opencv.videoio VideoCapture Videoio)
           (org.opencv.core Core Mat)
           (org.opencv.imgproc Imgproc)
           (org.lwjgl BufferUtils)))

(def example-filename "assets/world_is_mine.mp4")

(defn read-video-file [#^String filename]
  (VideoCapture. filename))

(defn get-infomation [#^VideoCapture my-video]
  (if (nil? my-video)
    (println "Cannot read file!")
    (do
      (println "We can open file!")
      (let [video-width (.get my-video Videoio/CAP_PROP_FRAME_WIDTH)
            video-height (.get my-video Videoio/CAP_PROP_FRAME_HEIGHT)
            video-frames (.get my-video Videoio/CAP_PROP_FRAME_COUNT)
            video-fps (.get my-video Videoio/CAP_PROP_FPS)
            opened? (.isOpened my-video)]
        (println "Width : " video-width "\n"
                 "Height: " video-height "\n"
                 "Frames: " video-frames "\n"
                 "FPS   : " video-fps "\n"
                 "We should create Video Player NOW!!!" opened? "\n"))))
  my-video)

(defn read-frame [my-video]
  (let [frame (Mat.)
        subframe (Mat.)
        _ (.read my-video frame)
        channels (.channels frame)]
    (println channels)))

(def my-video (let [video (read-video-file example-filename)]
                (get-infomation video)))

;; (type my-video)
;; (.isOpened my-video)
;; (def my-frame (atom (Mat.)))
;; (def my-sub-frame (atom (Mat.)))
;; (.read my-video @my-frame)
;; ;; (.channels @my-frame)
;; ;; (.rows @my-frame)
;; ;; (.cols @my-frame)
;; ;; warn
;;  (Core/flip @my-frame @my-sub-frame 0)
;; (Imgproc/cvtColor @my-sub-frame @my-frame Imgproc/COLOR_BGR2RGB)
;; exchange

(def my-frame-bytes (atom (byte-array
                           (* (.channels @my-frame)
                              (.rows @my-frame)
                              (.cols @my-frame)))))

(def my-frame-buffer (atom (BufferUtils/createByteBuffer
                            (* (.channels @my-frame)
                               (.rows @my-frame)
                               (.cols @my-frame)))))


;; (.get @my-frame 0 0 @my-frame-bytes)
;; (.flip (.put @my-frame-buffer @my-frame-bytes))

;; why ?

(defn display []
  (GL11/glClear GL11/GL_COLOR_BUFFER_BIT)
  (GL11/glDrawPixels (.cols @my-frame) (.rows @my-frame) GL11/GL_RGB, GL11/GL_UNSIGNED_BYTE,  @my-frame-buffer)
  (GL11/glFlush))

(defonce param (atom {:width (.cols @my-frame)
                      :height (.rows @my-frame)}))

(defonce window (ref 0))

(defn loop_ []
  (GL/createCapabilities)
  (GL11/glClearColor 1.0 0.0 0.0 0.0)
  (while (not (GLFW/glfwWindowShouldClose @window))
    (display)
    (Thread/sleep 100)
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
    (GLFW/glfwShowWindow @window)))

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

;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; ;; example-program
;; ;; (-> example-filename
;; ;;     read-video-file
;; ;;     get-infomation)
;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;
