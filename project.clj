(defproject clj-lwjgl-vplayer "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/core.async "0.4.474"]
                 [org.lwjgl/lwjgl "3.1.0"]
                 ;;[org.lwjgl/lwjgl-util "2.7.1"]
                 ;;[org.lwjgl/lwjgl-native-platform "2.7.1"]
                 ]
  :jvm-opts ["-Djava.library.path=./lib"]
  :resource-paths ["./lib/opencv-340.jar"]
  :injections [(clojure.lang.RT/loadLibrary org.opencv.core.Core/NATIVE_LIBRARY_NAME)]
  )

;; Unlinked Error for opencv
;; => I think you can remove ~/.m2/repository and restart leiningen or cider
