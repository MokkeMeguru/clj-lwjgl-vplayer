(defproject clj-lwjgl-vplayer "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/core.async "0.4.474"]

                 ;; https://mvnrepository.com/artifact/org.bytedeco.javacpp-presets/opencv
                 [org.bytedeco.javacpp-presets/opencv "3.4.0-1.4"]

                 [org.lwjgl/lwjgl "2.7.1"]
                 [org.lwjgl/lwjgl-util "2.7.1"]
                 [org.lwjgl/lwjgl-native-platform "2.7.1"]
                 ]
  ;; :jvm-opts ["-Djava.library.path=./lib"]
  )
