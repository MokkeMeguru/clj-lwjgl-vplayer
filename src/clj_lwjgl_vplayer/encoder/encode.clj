(ns clj-lwjgl-vplayer.encoder.encode
  (:import (org.lwjgl.opengl GL GL11)
           (org.lwjgl.glfw GLFW Callbacks
                           GLFWErrorCallback GLFWKeyCallback)
           (org.lwjgl BufferUtils)
           (net.jpountz.lz4 LZ4Compressor LZ4Decompressor LZ4Factory)
           (java.nio ByteBuffer))
  (:require [clojure.java.io :as io]
            [clj-lwjgl-vplayer.video-loader :as vl]))



(def output-file (io/file "./assets/output.b"))

(def Lfactory (atom (LZ4Factory/fastestInstance)))

(def Lcompressor (atom (.fastCompressor @Lfactory)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def example-filename "assets/.mp4")

(vl/read-video-file example-filename)

(vl/set-infomation)

(str @vl/info)

(vl/read-frame)

(def decomplessed-size (alength @vl/my-frame-bytes))

(def max-compressed-size (.maxCompressedLength @Lcompressor decomplessed-size))

(def temp-bytes (volatile! (byte-array
                            max-compressed-size)))

(def compress-size
  (atom (.compress @Lcompressor @vl/my-frame-bytes 0 (alength @vl/my-frame-bytes) @temp-bytes 0 max-compressed-size)))


(def make-compressed-file [^String filename]
  (let [info @vl/info
        width (:width info)
        height (:height info)
        size (* 3 width height)
        temp-bytes (volatile! (byte-array size))
        max-compressed-size (.maxCompressedLength @Lcompressor size)
        b-size (.putInt (ByteBuffer/allocate 4) size)]
   (vl/read-video-file filename)
   (vl/set-infomation)
   (loop [i (:frames @vl/info)]
     (when-not (zero? i)
       (do
         (vl/read-frame)
         (let [comp-size (.compress @Lcompressor
                                    @vl/my-frame-bytes
                                    0
                                    size
                                    @temp-bytes
                                    0
                                    max-compressed-size)]
           )
         (recur (dec i)))))))
