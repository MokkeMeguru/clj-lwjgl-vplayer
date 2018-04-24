(ns clj-lwjgl-vplayer.learn-async.async.clj
  (:require [clojure.core.async :refer
             [chan go-loop go >! <! timeout]]))

(import '[java.util Date])
(def ch (chan))

;; 書き込み非同期ブロック
(go-loop []
  (when (>! ch (Date.)) ; チャネルに書く
    (<! (timeout 2000)) ; 2秒待つ
    (recur)))

;; 読み込み非同期ブロック
(go-loop []
  (when-let [date (<! ch)] ; チャネルを読む
    (println "now:" date)
    (recur)))


(defn loop_ []
  (let [wait-time (/ 1000 30)
        ch (chan)]
    (go-loop []
      (when (>! ch true)
        (<! (timeout 10000))
        (recur)))
    (go-loop []
      (when-let [_ (<! ch)]
        (println "hoge")
        (recur)))))

(loop_)
