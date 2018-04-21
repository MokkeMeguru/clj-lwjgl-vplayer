(ns clj-lwjgl-vplayer.video-loader
  "This is a video-loader library"
  (:import (org.lwjgl.opengl GL GL11)
           (org.lwjgl.glfw GLFW Callbacks
                           GLFWErrorCallback GLFWKeyCallback)
           (org.opencv.videoio VideoCapture Videoio)
           (org.opencv.core Core Mat)
           (org.opencv.imgproc Imgproc)
           (org.lwjgl BufferUtils)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; load the video and return video information
;;
;; "info" which is atom contains the video's status.
;;   ex. fps width height channels
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defonce info (atom {:error nil
                     :fps 0
                     :width 0
                     :height 0
                     :frames 0}))

(defonce my-video (atom nil))

(defonce example-filename "assets/world_is_mine.mp4")

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; bytebuffer ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def my-frame-bytes (atom nil))

(def my-frame-buffer (atom nil))

(def frame (atom (Mat.)))

(def sub-frame (atom (Mat.)))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn read-video-file
  "read video file
  filename: it's the video file path
  return: org.opencv.videoio.VideoCapture"
  [#^String filename]
  (reset! my-video (VideoCapture. filename))) 

(defn set-infomation []
  (if (nil? @my-video)
    (swap! info assoc :error "Cannot Read the File!")
    (let [m-video @my-video
          fps (.get m-video Videoio/CAP_PROP_FPS)
          width (.get m-video Videoio/CAP_PROP_FRAME_WIDTH)
          height (.get m-video Videoio/CAP_PROP_FRAME_HEIGHT)
          frames (.get m-video Videoio/CAP_PROP_FRAME_COUNT)]
      (swap! info assoc :fps fps)
      (swap! info assoc :width width)
      (swap! info assoc :height height)
      (swap! info assoc :frames frames)
      (reset! my-frame-bytes (byte-array
                              (* 3 width height)))
      (reset! my-frame-buffer (BufferUtils/createByteBuffer
                               (* 3 width height))))))

(defn read-frame
  "read frame
  args: org.opencv.videoio.VideoCapture
  return: org.opencv.videoio.VideoCapture
  Usage:
  (loop [i (:frames info)]
    (when (> i 0)
       (read-frame)
       (display) ;; You should create this function
       (recur dec)))
  "
  []
  (let [_ (.read @my-video @frame)]
    (Core/flip @frame @sub-frame 0)
    (Imgproc/cvtColor @sub-frame @frame Imgproc/COLOR_BGR2RGB)
    (.get @frame 0 0 @my-frame-bytes)
    (.flip (.put @my-frame-buffer @my-frame-bytes))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; test

;;  (def example-filename "assets/world_is_mine.mp4")


(defn read-infomation []
  (set-infomation @my-video)
  (let [infomation @info]
    (println "Width : " (:width info))
    (println "Height: " (:height info))
    (println "Frames: " (:frames info))
    (println "FPS :" (:fps info))))


(read-video-file example-filename)
(set-infomation)
(read-frame)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


