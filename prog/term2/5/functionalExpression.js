function variable(varType) {
    return function (x, y, z) {
        if (varType === 'x') return x;
        if (varType === 'y') return y;
        if (varType === 'z') return z;
    }
}

function cnst(value) {
    return (x, y, z) => {
        return value;
    }
}

const unar = (complete) => (first) => (x, y, z) => complete(first(x, y, z));

const binar = function (complete) {
    return function (first, second) {
        return function (x, y, z) {
            return complete(first(x, y, z), second(x, y, z));
        }
    }
}

const add = binar(function (a, b) {
    return (a + b)
})

const subtract = binar(function (a, b) {
    return (a - b)
})

const multiply = binar(function (a, b) {
    return (a * b)
})

divide = binar(function (a, b) {
    return (a / b)
})

const negate = unar(a => -a)
const one = cnst(1)
const two = cnst(2)


/*let expr = subtract(
    multiply(
        cnst(2),
        variable("x")
    ),
    cnst(3)
);
console.log(expr(2))*/