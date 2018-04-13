(defproject clj-lwjgl-vplayer "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/core.async "0.4.474"]
                 ;; I add below, get some error...
                 [org.lwjgl/lwjgl "2.7.1"]
                 [org.lwjgl/lwjgl-util "2.7.1"]
                 [org.lwjgl/lwjgl-native-platform "2.7.1"]

                 ]
  :jvm-opts ["-Djava.library.path=./lib"]
  :resource-paths ["./lib/opencv-340.jar"]
  :injections [(clojure.lang.RT/loadLibrary org.opencv.core.Core/NATIVE_LIBRARY_NAME)]
  )


;; Retrieving org/lwjgl/lwjgl/2.7.1/lwjgl-2.7.1.pom from clojars
;; Retrieving org/lwjgl/lwjgl-util/2.7.1/lwjgl-util-2.7.1.pom from clojars
;; Retrieving org/lwjgl/lwjgl-native-platform/2.7.1/lwjgl-native-platform-2.7.1.pom from clojars
;; Retrieving org/lwjgl/lwjgl-native-platform/2.7.1/lwjgl-native-platform-2.7.1.jar from clojars
;; Retrieving org/lwjgl/lwjgl-util/2.7.1/lwjgl-util-2.7.1.jar from clojars
;; Retrieving org/lwjgl/lwjgl/2.7.1/lwjgl-2.7.1.jar from clojars
;; Exception in thread "main" java.lang.UnsatisfiedLinkError: no opencv_java340 in java.library.path, compiling:(/tmp/form-init6207154050652343726.clj:1:73)
;; 	at clojure.lang.Compiler.load(Compiler.java:7391)
;; 	at clojure.lang.Compiler.loadFile(Compiler.java:7317)
;; 	at clojure.main$load_script.invokeStatic(main.clj:275)
;; 	at clojure.main$init_opt.invokeStatic(main.clj:277)
;; 	at clojure.main$init_opt.invoke(main.clj:277)
;; 	at clojure.main$initialize.invokeStatic(main.clj:308)
;; 	at clojure.main$null_opt.invokeStatic(main.clj:342)
;; 	at clojure.main$null_opt.invoke(main.clj:339)
;; 	at clojure.main$main.invokeStatic(main.clj:421)
;; 	at clojure.main$main.doInvoke(main.clj:384)
;; 	at clojure.lang.RestFn.invoke(RestFn.java:421)
;; 	at clojure.lang.Var.invoke(Var.java:383)
;; 	at clojure.lang.AFn.applyToHelper(AFn.java:156)
;; 	at clojure.lang.Var.applyTo(Var.java:700)
;; 	at clojure.main.main(main.java:37)
;; Caused by: java.lang.UnsatisfiedLinkError: no opencv_java340 in java.library.path
;; 	at java.lang.ClassLoader.loadLibrary(ClassLoader.java:1867)
;; 	at java.lang.Runtime.loadLibrary0(Runtime.java:870)
;; 	at java.lang.System.loadLibrary(System.java:1122)
;; 	at clojure.lang.RT.loadLibrary(RT.java:495)
;; 	at user$eval1142.invokeStatic(form-init6207154050652343726.clj:1)
;; 	at user$eval1142.invoke(form-init6207154050652343726.clj:1)
;; 	at clojure.lang.Compiler.eval(Compiler.java:6927)
;; 	at clojure.lang.Compiler.eval(Compiler.java:6916)
;; 	at clojure.lang.Compiler.load(Compiler.java:7379)
;; 	... 14 more
