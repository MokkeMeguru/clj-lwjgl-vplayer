(ns clj-lwjgl-vplayer.audio-loader
  (:import (org.lwjgl.openal AL10 ALC10 ALC AL)
           (org.lwjgl.stb STBVorbis)
           (org.lwjgl.system MathUtil)
           (java.nio ByteBuffer)
           (org.lwjgl.system MemoryStack MemoryUtil)
           (org.lwjgl.stb STBVorbis)
           (org.lwjgl.system.libc LibCStdlib))
  (:require [clojure.java.io :as io]))

;; ;FIXME: I want to create an Atom for audio volume and etc ...

(def example-audio (str (io/file (io/resource "../assets/world_is_mine.ogg"))))

(def state
  (atom
   {:device nil
    :attributes nil
    :context nil
    :alcCapabilities nil
    :alCapabilitis nil
    :channels nil
    :sampleRate nil
    :rawAudioBuffer nil
    :bufferPointer nil
    :sourcePointer nil
    :error nil}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; initialize and close

(defn init-audio []
  (let [device (ALC10/alcOpenDevice
                (ALC10/alcGetString 0 ALC10/ALC_DEFAULT_DEVICE_SPECIFIER))
        attributes (int-array [0])
        context (ALC10/alcCreateContext device attributes)
        _ (ALC10/alcMakeContextCurrent context)
        alcCapabilities (ALC/createCapabilities device)
        alCapabilities  (AL/createCapabilities alcCapabilities)]
    (swap! state assoc :error nil)
    (swap! state assoc :device device)
    (swap! state assoc :attributes attributes)
    (swap! state assoc :context context)
    (swap! state assoc :alcCapabilities alcCapabilities)
    (swap! state assoc :alCapabilitis alCapabilities)))

;; (init-audio)

;; (let [st (:alCapabilitis @state)]
;;   (.OpenAL10 st));; check true => it's shows openAL10's support.

(defn close-audio []
  (AL10/alDeleteSources (:sourcePointer @state))
  (AL10/alDeleteBuffers (:bufferPointer @state))
  (ALC10/alcDestroyContext (:context @state))
  (ALC10/alcCloseDevice (:device @state)))

;; (close-audio)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; load sound

(defn load-audio [#^String file-name]
  (if-let [stack (MemoryStack/stackPush)]
      (let
          [channelBuffer (.mallocInt stack 1)
           samlpeRateBuffer (.mallocInt stack 1)
           rawAudioBufer (STBVorbis/stb_vorbis_decode_filename
                          file-name channelBuffer samlpeRateBuffer)]
        (swap! state assoc :channels (.get channelBuffer 0))
        (swap! state assoc :sampleRate (.get samlpeRateBuffer 0))
        (swap! state assoc :rawAudioBuffer rawAudioBufer))
      (swap! state assoc :error "Memory Stack Error")))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; play sound
(defn play-sound []
  (let [format (if (== 1 (:channels @state))
                 (AL10/AL_FORMAT_MONO16)
                 (AL10/AL_FORMAT_STEREO16))
        sourcePointer (AL10/alGenSources)
        bufferPointer (AL10/alGenBuffers)]
    (AL10/alBufferData bufferPointer
                       format
                       (:rawAudioBuffer @state)
                       (:sampleRate @state))
    (LibCStdlib/free (:rawAudioBuffer @state))
    (AL10/alSourcei sourcePointer
                    AL10/AL_BUFFER
                    bufferPointer)
    (AL10/alSourcePlay sourcePointer)
    (swap! state assoc :sourcePointer sourcePointer)
    (swap! state assoc :bufferPointer bufferPointer)
    ))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; terminate


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; test ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; (init-state)
;; (let [st (:alCapabilitis @state)]
;;   (.OpenAL10 st));; check true => it's shows openAL10's support.
;; (close-audio)


;; (init-audio)
;; (load-audio "./assets/world_is_mine.ogg")
;; (:channels @state)
;; (play-sound)


;; (close-audio)


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

