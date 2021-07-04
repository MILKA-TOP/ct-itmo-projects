"use strict";

let position = 0;
let line;
let stopSymbols = [')', '(', ' ', '\n', '\t']
let numberElements = '0123456789'
let alpElements = /^[a-zA-z]+$/

function toArray(args) {
    let arr = [];
    for (const a of args) {
        arr.push(a);
    }
    return arr;
}

function nowSymbol() {
    return line[position]
}

function getText(outLine) {
    while (hasNext() && stopSymbols.indexOf(nowSymbol()) === -1) {
        outLine = outLine + nextSymbol()
    }
    skipWhitespace()
    return outLine
}

function parseConst(getLine) {
    getLine = getText(getLine)

    if (isNaN(getLine)) throw new ParsingError("===Invalid Number===\n" + showException())
    return new Const(Number(getLine))

}

function parseVar(inputSymbol) {
    inputSymbol = getText(inputSymbol)

    if (varArray.indexOf(inputSymbol) !== -1) return new Variable(inputSymbol)
    throw new ParsingError("===Unknown variable===\n" + showException())
}

function isItNumber(x) {
    return (numberElements.indexOf(x) !== -1 || x === '-');
}

function nextSymbol() {
    return line[position++]
}

function isWhitespace() {
    return line[position] === '\n' || line[position] === ' ' || line[position] === '\t';

}

function skipWhitespace() {
    while (isWhitespace()) {
        position++;
    }
}

function hasNext() {
    return position < line.length
}

function showException() {
    return line + '\n' + ' '.repeat(position - 1) + '^'
}

///////////////////////////////////////

function Variable(varType) {
    this.varType = varType;
}

Variable.prototype.toString = function () {
    return this.varType;
}

Variable.prototype.evaluate = function (x, y, z) {
    switch (this.varType) {
        case "x":
            return x;
        case "y":
            return y;
        case "z":
            return z;
    }
}

Variable.prototype.prefix = function () {
    return this.varType.toString()
}

///////////////\\\\\\\\\\\\\\\

function Const(number) {
    this.number = number;
}

Const.prototype.toString = function () {
    return this.number.toString();
}

Const.prototype.evaluate = function (x, y, z) {
    return this.number;
}

Const.prototype.prefix = function () {
    return this.number.toString()
}

///////////////\\\\\\\\\\\\\\\
///////////////\\\\\\\\\\\\\\\

function AllOperations(func, typeOp, ...args) {
    this.function = func;
    this.typeOperation = typeOp;
    this.argum = toArray(args);
}

AllOperations.prototype.toString = function () {
    let outString = "";
    for (const a of this.argum) {
        outString = outString + a.toString() + " ";
    }
    return outString + this.typeOperation;
}

AllOperations.prototype.evaluate = function (x, y, z) {
    let array = []
    for (const a of this.argum) {
        array.push(a.evaluate(x, y, z))
    }
    return this.function(array);
}

AllOperations.prototype.prefix = function () {
    let line = "(" + this.typeOperation
    for (const a of this.argum) {
        line = line + " " + a.prefix()
    }
    return line + ")"

}

///////////////////////////////////////


Add.prototype = Object.create(AllOperations.prototype);
Subtract.prototype = Object.create(AllOperations.prototype);
Multiply.prototype = Object.create(AllOperations.prototype);
Divide.prototype = Object.create(AllOperations.prototype);
Negate.prototype = Object.create(AllOperations.prototype);
Avg5.prototype = Object.create(AllOperations.prototype);
Med3.prototype = Object.create(AllOperations.prototype);
ArithMean.prototype = Object.create(AllOperations.prototype);
GeomMean.prototype = Object.create(AllOperations.prototype);
HarmMean.prototype = Object.create(AllOperations.prototype);


///////////////////////////////////////


function Add(func, typeOp, f1, f2) {
    AllOperations.call(this, func, typeOp, f1, f2)
}

function Add(f1, f2) {
    return new AllOperations(function (arg) {
        return arg[0] + arg[1];
    }, "+", f1, f2)
}

///////////////\\\\\\\\\\\\\\\

function Subtract(func, f1, f2, typeOp) {
    AllOperations.call(this, func, f1, f2, typeOp)
}

function Subtract(f1, f2) {
    return new AllOperations(function (arg) {
        return arg[0] - arg[1];
    }, "-", f1, f2)
}

///////////////\\\\\\\\\\\\\\\


function Multiply(func, typeOp, f1, f2) {
    AllOperations.call(this, func, typeOp, f1, f2)
}

function Multiply(f1, f2) {
    return new AllOperations(function (arg) {
        return arg[0] * arg[1];
    }, "*", f1, f2)
}

///////////////\\\\\\\\\\\\\\\

function Divide(func, typeOp, f1, f2) {
    AllOperations.call(this, func, typeOp, f1, f2)
}

function Divide(f1, f2) {
    return new AllOperations(function (arg) {
        return arg[0] / arg[1];
    }, "/", f1, f2)
}

///////////////\\\\\\\\\\\\\\\


function Negate(func, typeOp, f1) {
    AllOperations.call(this, func, typeOp, f1)
}

function Negate(f1) {
    return new AllOperations(function (arg) {
        return -arg[0];
    }, "negate", f1)
}

///////////////\\\\\\\\\\\\\\\

function Avg5(func, type, a, b, c, d, e) {
    AllOperations.call(this, func, type, a, b, c, d, e)
}

function Avg5(a, b, c, d, e) {
    return new AllOperations(function (args) {
        return (args[0] + args[1] + args[2] + args[3] + args[4]) / 5;
    }, "avg5", a, b, c, d, e)
}


///////////////\\\\\\\\\\\\\\\

function Med3(func, type, a, b, c) {
    AllOperations.call(this, func, type, a, b, c)
}

function Med3(a, b, c) {
    return new AllOperations(function (args) {
        args.sort(function (a, b) {
            return b - a;
        });
        return args[1]
    }, "med3", a, b, c)
}

///////////////\\\\\\\\\\\\\\\

function ArithMean(func, type, ...a) {
    AllOperations.call(this, func, type, ...a)
}

function ArithMean(...a) {
    return new AllOperations(function (numbers) {
        let counts = 0
        let sum = 0
        for (const a of numbers) {
            counts += +1
            sum += +a
        }
        return sum / counts
    }, "arith-mean", ...a)
}

///////////////\\\\\\\\\\\\\\\

function GeomMean(func, type, ...a) {
    AllOperations.call(this, func, type, ...a)
}

function GeomMean(...a) {
    return new AllOperations(function (numbers) {
        let sum = 1
        for (const a of numbers) {
            sum *= +a
        }
        return Math.pow(Math.abs(sum), 1 / a.length)
    }, "geom-mean", ...a)
}


///////////////\\\\\\\\\\\\\\\

function HarmMean(func, type, ...a) {
    AllOperations.call(this, func, type, ...a)
}

function HarmMean(...a) {
    return new AllOperations(function (numbers) {
        let sum = 0
        for (const a of numbers) {
            sum += 1 / (+a)
        }
        return a.length / sum
    }, "harm-mean", ...a)
}

///////////////\\\\\\\\\\\\\\\


let varArray = ["x", "y", "z"]

let operation = {
    "+": [Add, 2],
    "-": [Subtract, 2],
    "*": [Multiply, 2],
    "/": [Divide, 2],
    "negate": [Negate, 1],
    "avg5": [Avg5, 5],
    "med3": [Med3, 3],
    "arith-mean": [ArithMean, -1],
    "geom-mean": [GeomMean, -1],
    "harm-mean": [HarmMean, -1]
}

function parsing() {
    skipWhitespace()

    let symbol = nextSymbol()
    let operationSymbol = ''

    if (symbol === '(') {
        skipWhitespace()
        operationSymbol = getText("")
    } else if (isItNumber(symbol)) {
        return parseConst(symbol);
    } else if (symbol.match(alpElements)) {
        return parseVar(symbol)
    }

    catchOperationError(symbol, operationSymbol)

    let elementsOfOperation = getElementsOfOperation(operationSymbol)

    catchArgumentMissingError()

    return operation[operationSymbol][0].apply(null, elementsOfOperation);
}

function getElementsOfOperation(operationSymbol) {
    let elementsOfOperation = []
    if (operation[operationSymbol][1] !== -1) {
        for (let i = 0; i < operation[operationSymbol][1]; i++) {
            elementsOfOperation.push(parsing())
        }
    } else {
        while (hasNext() && nowSymbol() !== ")") {
            elementsOfOperation.push(parsing())
            skipWhitespace()
        }
    }
    skipWhitespace()
    return elementsOfOperation
}

function catchOperationError(symbol, operationSymbol) {
    if (!(operationSymbol in operation)) {
        if (symbol === '(' && operationSymbol === '') {
            throw new ParsingError("===Empty operation===\n" + showException())
        } else if (symbol === '(' && operationSymbol !== '') {
            throw new ParsingError("===Unknown operation===\n" + showException())
        }
        throw new ParsingError("===Invalid counts of arguments(Less than expected)===\n" + showException())
    }
}

function catchArgumentMissingError() {
    if (!hasNext()) {
        throw new ParsingError("===Missing ')'===\n" + showException())
    } else if (nextSymbol() !== ")") {
        throw new ParsingError("===Invalid counts of arguments(More than expected)===\n" + showException())
    }
}

function parsePrefix(expression) {
    position = 0
    line = expression

    if (line.length === 0) throw new ParsingError("===Empty input===")

    let funcOut = parsing(expression)
    skipWhitespace()
    if (position < expression.length) {
        throw new ParsingError("===Excessive info===\n" + showException())
    }
    return funcOut
}

///////////////\\\\\\\\\\\\\\\

function ParsingError(message) {
    this.name = "ParsingError";
    this.message = '\n' + message;
}

ParsingError.prototype = Error.prototype;


//console.log(parsePrefix ('( +       x 2 )'))


