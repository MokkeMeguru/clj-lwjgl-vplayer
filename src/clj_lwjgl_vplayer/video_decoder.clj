(ns clj-lwjgl-vplayer.video-decoder
  (:import (org.opencv.videoio VideoCapture Videoio)
           (org.opencv.core Core Mat)
           (org.opencv.imgproc Imgproc))
  (:require [clojure.java.io
               :refer [output-stream file
                       input-stream]]
            [clj-lwjgl-vplayer.video-loader :as vl]))

;; ;; load opencv?
;; (clojure.lang.RT/loadLibrary org.opencv.core.Core/NATIVE_LIBRARY_NAME)

;; (defn set-info [#^String example-filename]
;;   (vl/read-video-file example-filename)
;;   (vl/set-infomation)
;;   (println @vl/info))


;; (defn wf->byte []
;;   []
;;   (let [_ (.read @vl/my-video @vl/frame)]
;;     (Core/flip @vl/frame @vl/sub-frame 0)
;;     (Imgproc/cvtColor @vl/sub-frame @vl/frame Imgproc/COLOR_BGR2RGB)
;;     (.get @vl/frame 0 0 @vl/my-frame-bytes)))

;; (defn wf->byte->file []
;;   (with-open [out (output-stream (file "./assets/LAMUNATION.b"))]
;;     (loop [f (int (:frames @vl/info))]
;;       (when-not (zero? f)
;;           (do
;;             (wf->byte)
;;             (.write out @vl/my-frame-bytes)
;;             (recur (dec f)))))))

;; (defn -main [#^String str]
;;   (set-info str)
;;   (wf->byte->file))

;; (-main example-filename)
