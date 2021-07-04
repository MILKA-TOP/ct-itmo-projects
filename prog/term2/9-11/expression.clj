;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;                                                           ;;
;;   Домашнее задание 9. Функциональные выражения на Clojure ;;
;;                                                           ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn variable [varType] (fn [applyMap] (applyMap varType)))
(defn constant [value] (fn [applyMap] value))

(defn binaryOperation [function] (fn [a b]
                                     (fn [evalValue]
                                         (function ((fn [arg] (arg evalValue)) a) ((fn [arg] (arg evalValue)) b)))))

(defn unaryOperation [function] (fn [a]
                                    (fn [evalValue]
                                        (function ((fn [arg] (arg evalValue)) a)))))
(def add (binaryOperation +))
(def subtract (binaryOperation -))
(def multiply (binaryOperation *))
(def divide (binaryOperation (fn [a b] (/ (double a) (double b)))))
(def negate (unaryOperation -))
(def sinh (unaryOperation (fn [a] (Math/sinh a))))
(def cosh (unaryOperation (fn [a] (Math/cosh a))))


(def operations {'+      add,
                 '-      subtract,
                 '*      multiply,
                 '/      divide,
                 'negate negate,
                 'sinh   sinh,
                 'cosh   cosh
                 })

(defn parseDoing [expression]
      (cond
        (number? expression) (constant expression)
        (symbol? expression) (variable (str expression))
        :else (apply (operations (first expression)) (map parseDoing (rest expression)))))

(defn parseFunction [expression]
      (parseDoing (read-string expression)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;                                                           ;;
;;    Домашнее задание 10. Объектные выражения на Clojure    ;;
;;                                                           ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn toString [line] (.stringValue line))
(defn evaluate [line, value] ((.evaluateComp line) value))
(defn diff [line, value] ((.df line) value))
(defn toStringSuffix [exp] (.toSuffix exp))

(declare ZERO)
(declare ONE)

(definterface Expression
              (stringValue [])
              (evaluateComp [])
              (df [])
			  (toSuffix [])
              )

(deftype tempConstant [val]
         Expression
         (stringValue [this] (format "%.1f" (double val)))
         (evaluateComp [this] (fn [varMap] val))
         (df [this] (fn [varType] ZERO))
		 (toSuffix [this] (format "%.1f" (double val)))
         )

(deftype tmepVariable [varType]
         Expression
         (stringValue [this] (str varType))
		 (toSuffix [this] (str varType))
         (evaluateComp [this] (fn [varValue] (varValue (str (get (clojure.string/lower-case varType) 0)))))
         (df [this] (fn [varTypeDf] (if (= varTypeDf varType) ONE ZERO)))
         )

(deftype UnaryOperation [stringType, evaluateOperation, a, diffOperation]
         Expression
         (stringValue [this] (str "(" stringType " " (.stringValue a) ")"))
		 (toSuffix [this] (str "(" (.toSuffix a) " " stringType ")"))
         (evaluateComp [this] (fn [varNames] (evaluateOperation (evaluate a varNames))))
         (df [this] (fn [varTypeDf] (diffOperation a (diff a varTypeDf))))
         )

(deftype BinaryOperation [stringType, evaluateOperation, a, b, diffOperation]
         Expression
         (stringValue [this] (str "(" stringType " " (.stringValue a) " " (.stringValue b) ")"))
		 (toSuffix [this] (str "(" (.toSuffix a) " " (.toSuffix b) " " stringType ")"))
         (evaluateComp [this] (fn [varNames] (evaluateOperation (evaluate a varNames) (evaluate b varNames))))
         (df [this] (fn [varTypeDf] (diffOperation a b (diff a varTypeDf) (diff b varTypeDf))))
         )

(defn Variable [varName] (tmepVariable. varName))
(defn Constant [value] (tempConstant. value))

(def ZERO (Constant 0))
(def ONE (Constant 1))

(defn Add [a b] (BinaryOperation. "+", +, a, b, (fn [a b da db] (Add da db))))
(defn Subtract [a b] (BinaryOperation. "-", -, a, b, (fn [a b da db] (Subtract da db))))
(defn Multiply [a b] (BinaryOperation. "*", *, a, b, (fn [a b da db] (Add (Multiply a db) (Multiply da b)))))
(defn Divide [a b] (BinaryOperation. "/", (fn [a b] (/ (double a) (double b))), a, b, (fn [a b da db]
                                                                                          (Divide
                                                                                            (Subtract
                                                                                              (Multiply da b)
                                                                                              (Multiply a db))
                                                                                            (Multiply b b)))))
(defn Negate [a] (UnaryOperation. "negate", -, a, (fn [a da] (Negate da))))
(declare Sinh)
(declare Cosh)

(defn Sinh [a] (UnaryOperation. "sinh", (fn [s] (Math/sinh s)), a, (fn [a da] (Multiply da (Cosh a)))))
(defn Cosh [a] (UnaryOperation. "cosh", (fn [s] (Math/cosh s)), a, (fn [a da] (Multiply da (Sinh a)))))

(defn toBool [a] (if (> a 0) true false))
(defn toInt [a] (if a 1 0))

(defn And [a b] (BinaryOperation. "&&", (fn [a b] (if (and (if (> a 0) true false) (if (> b 0) true false)) 1 0)), a,b,(fn [a b da db] (Add da db))))
(defn Or [a b] (BinaryOperation. "||",(fn [a b] (if (or (if (> a 0) true false) (if (> b 0) true false)) 1 0)), a,b, (fn [a b da db] (Add da db))))
(defn Xor [a b] (BinaryOperation. "^^",(fn [a b] (if (not (= (if (> a 0) true false) (if (> b 0) true false))) 1 0)), a,b, (fn [a b da db] (Add da db))))

(def operations_object {'+      Add,
                        '-      Subtract,
                        '*      Multiply,
                        '/      Divide,
                        'negate Negate
                        'sinh   Sinh,
                        'cosh   Cosh,
						'|| Or,
						'&& And,
						(symbol "^^") Xor
                        })

(defn parsing_func [expression]
      (if (number? expression) (Constant expression)
                               (if (symbol? expression) (Variable (str expression))
                                                        (if (list? expression) (apply (operations_object (first expression)) (map parsing_func (rest expression)))
                                                                               ))))

(defn parseObject [line] (parsing_func (read-string line)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;                                                           ;;
;;   	  Домашнее задание 11. Комбинаторные парсеры         ;;
;;                                                           ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn -return [value tail] {:value value :tail tail})
(def -valid? boolean)
(def -value :value)
(def -tail :tail)
(defn _empty [value] (partial -return value))
(defn _char [p]
           (fn [[c & cs]]
             (if (and c (p c))
               (-return c cs))))
(defn _map [f result]
           (if (-valid? result)
             (-return (f (-value result)) (-tail result))))
(defn _combine [f a b]
           (fn [input]
             (let [ar ((force a) input)]
               (if (-valid? ar)
                 (_map (partial f (-value ar))
                       ((force b) (-tail ar)))))))
(defn _either [a b]
           (fn [input]
             (let [ar ((force a) input)]
               (if (-valid? ar)
                 ar
                 ((force b) input)))))
(defn _parser [p]
           (let [pp (_combine (fn [v _] v) p (_char #{\u0000}))]
             (fn [input] (-value (pp (str input \u0000))))))
(defn +char [chars]
           (_char (set chars)))			 
(defn +char-not [chars]
           (_char (comp not (set chars))))			 
(defn +map [f parser]
           (comp (partial _map f) parser))			 
(def +parser _parser)			 
(def +ignore
           (partial +map (constantly 'ignore)))
(defn iconj [coll value]
           (if (= value 'ignore)
             coll
             (conj coll value)))
(defn +seq [& ps]
           (reduce (partial _combine iconj) (_empty []) ps))
(defn +seqf [f & ps]
           (+map (partial apply f) (apply +seq ps)))
(defn +seqn [n & ps]
           (apply +seqf #(nth %& n) ps))
(defn +or [p & ps]
           (reduce _either p ps))
(defn +opt [p]
           (+or p (_empty nil)))
(defn +star [p]
           (letfn [(rec [] (+or (+seqf cons p (delay (rec))) (_empty ())))]
             (rec)))
(defn +plus [p]
           (+seqf cons p (+star p)))
(defn +str [p] (+map (partial apply str) p))
(def *digit (+char "0123456789"))
(def *number (+map read-string (+str (+plus *digit))))
(def *string
           (+seqn 1
                  (+char "\"")
                  (+str (+star (+char-not "\"")))
                  (+char "\"")))
(def *space (+char " \t\n\r"))
(def *ws (+ignore (+star *space)))				  
(def *null (+seqf (constantly 'null) (+char "n") (+char "u") (+char "l") (+char "l")))
(def *all-chars (mapv char (range 32 128)))
(apply str *all-chars)
(def *letter (+char (apply str (filter #(Character/isLetter %) *all-chars))))
(def *identifier (+str (+seqf cons *letter (+star (+or *letter *digit)))))

(declare =const)
(declare =var)
(declare =opsymb)
(declare =list)
(declare =operation)
(declare =expr)
(def *varSymb (+char "XYZxyz"))
(def *opChar (+char "+-/*negate&|^"))
		
(def =const 
	(+map 
		(comp Constant read-string) 
		(+str (+seq (+opt(+char "-")) 
		*number 
		(+opt (+seq (+char ".") *digit))))))
		

(def =var
	(+map 
		Variable 
		(+str (+plus *varSymb))))
		

(def =list 
	(+seqn 
		1 (+char "(") 
		(+plus (+seqn 0 *ws (delay =expr)))
		*ws (+char ")")))
		
		
(def =operation 
	(+map (fn [line] (apply (operations_object (last line)) (butlast line))) =list))
	
(def =opsymb 
	(+map 
		symbol 
		(+str (+plus *opChar))))

(def =expr 
	(+or =operation =const =var =opsymb))

(def parseObjectSuffix 
	(+parser 
		(+seqn 0 *ws =expr *ws)))












