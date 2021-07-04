(defn vectorOperation [f & elements] (apply mapv f elements))

(defn tenzorOperation [f & elements] 
    (if (every? vector? elements)
        (apply mapv (partial tenzorOperation f) elements)
    (apply f elements)))

(def v+ (partial vectorOperation +))
(def v- (partial vectorOperation -))
(def v* (partial vectorOperation *))
(def vd (partial vectorOperation /))


(defn scalar [a b] (apply + (v* a b)))

(defn v*s [a b] (mapv * a (iterate identity b)))
(defn m*s [a b] (mapv v*s a (iterate identity b)))

(def m+ (partial vectorOperation v+))
(def m- (partial vectorOperation v-))
(def m* (partial vectorOperation v*))
(def md (partial vectorOperation vd))


(defn transpose [a] (apply mapv vector a))
(defn m*v [a b] (mapv scalar a (iterate identity b)))

(defn m*m [a, b] (mapv (fn [temp] (mapv scalar (transpose b) (iterate identity temp))) a))


(defn vect [a, b] [(- (* (nth a 1) (nth b 2)) (* (nth a 2) (nth b 1))), 
                     (- (* (nth a 2) (nth b 0)) (* (nth a 0) (nth b 2))), 
                     (- (* (nth a 0) (nth b 1)) (* (nth a 1) (nth b 0)))])

(def t+ (partial tenzorOperation +))
(def t- (partial tenzorOperation -))
(def t* (partial tenzorOperation *))
(def td (partial tenzorOperation /))